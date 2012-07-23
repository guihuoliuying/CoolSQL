/*
 * Created on 2006-8-30
 *
 */
package com.cattsoft.coolsql.exportdata.excel;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.cattsoft.coolsql.pub.component.WaitDialog;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.ConnectionUtil;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ����Excel�ļ������Excel�ļ�����ָ�������д���ļ�
 */
public class ExcelUtil {
	
	public static short transColor(Color color,HSSFWorkbook workbook)
	{
		 HSSFPalette palette = workbook.getCustomPalette();
		  HSSFColor hssfColor = palette.findColor((byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue());
		  if (hssfColor == null ){
		      palette.setColorAtIndex(HSSFColor.LAVENDER.index, (byte)color.getRed(), (byte)color.getGreen(),
		(byte)color.getBlue());
		      hssfColor = palette.getColor(HSSFColor.LAVENDER.index);
		  }
		  return hssfColor.getIndex();
	}
	
    private static ExcelUtil instance = null;

    private int index = 0; //��ǰ������������

    private int total = 1; //�ܵ��������

    private boolean isRun; //�ñ�������������ݵ������ж�

    private boolean isRunning;//��ǰexcel������Ƿ�����ִ�е�������

    private WaitDialog waiter = null;

    private ExcelUtil() {
        isRun = true;
        isRunning = false;
    }

    public static synchronized ExcelUtil getInstance() {
        if (instance == null)
            instance = new ExcelUtil();
        return instance;
    }

    public File createFile(String name) {
        File tmp = new File(name);
        //		if (tmp.exists()) {
        //			return null;
        //		}
        return tmp;
    }

    public void setWaitDialog(WaitDialog waiter) {
        this.waiter = waiter;
    }

    public WaitDialog getWaitDialog() {
        return waiter;
    }

    /**
     * ���excel�ļ����,����������Ϊ��ݻ�
     * 
     */
    public void buildExcel(File file, ExcelComponentSet setting, Vector<Vector<Object>> data)
            throws ExcelProcessException, IOException, FileNotFoundException {
        if (isRunning) {
            throw new ExcelProcessException(PublicResource
                    .getSQLString("export.error.isrunning"));
        }
        setRun(true); //����������־

        HSSFWorkbook wb = new HSSFWorkbook();
        checkExcelSet(setting);

        /**
         * ���ü���������������Ϣ
         */
        index = 0;
        total = data.size();

        waiter.setTaskLength(data.size());

        //��ʼ��ݵ���
        FileOutputStream out = null;
        try {
            GUIUtil.createDir(file.getAbsolutePath(),false, false);
            out = new FileOutputStream(file);

            isRunning = true;
            do {
                index = createSheet(wb, setting, data, "");
            } while (++index < data.size()&&isRun);
            wb.write(out);
            out.flush();
        } finally {
            isRunning = false;
            disposeWork(wb);
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * ���excel�ļ����,����ݿ���Ϊ��ݻ�
     * 
     * @param file
     * @param setting
     * @param set
     */
    public void buildExcel(File file, ExcelComponentSet setting, ResultSet set)
            throws SQLException, ExcelProcessException, IOException,
            FileNotFoundException {
        if (isRunning) {
            throw new ExcelProcessException(PublicResource
                    .getSQLString("export.error.isrunning"));
        }
        setRun(true); //����������־

        HSSFWorkbook wb = null;
        checkExcelSet(setting);

        if (isRun) {
            //��ȡ�����ܼ�¼��
            total = ResultUtil.countResultRow(set);

            if (total > 0) {
                waiter.setTaskLength(total);
            }
        }
        //�Ѿ�����������
        index = 0;

        int fileCount = 0;
        try {
            isRunning = true;
            do {
                if(!isRun)
                    return ;
                
                wb = new HSSFWorkbook();
                createSheet(wb, setting, set, getNextFile(file, fileCount,
                        "xls"), "");
                fileCount++;
            } while (ConnectionUtil.hasNext(set) && checkBound(index)&&isRun);
        } finally {
            set.close();
            set = null;
            isRunning = false;
        }
    }

    /**
     * �жϸ������Ƿ������Դ��Χ֮�ڣ�����޷���ȡ�ܵ�������ôֱ�ӷ���true
     * 
     * @param count
     * @return
     */
    private boolean checkBound(int count) {
        if (total != -1) {
            return count < total;
        } else
            return true;
    }

    /**
     * ���wb����
     * 
     * @param wb
     */
    private void disposeWork(HSSFWorkbook wb) {
        if (wb == null)
            return;
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            HSSFSheet sheet = wb.getSheetAt(i);
            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                sheet.removeRow(sheet.getRow(j));
            }
            wb.removeSheetAt(i);
        }
    }

    /**
     * ���һ���?���ñ?��ݽ����ڽ��Ļ��� ͬʱ���Բ�����Ĺ������������ע����
     * 
     * @param wb
     * @param setting
     * @param set
     * @param sheetName
     * @throws SQLException
     * @return --���ص�ǰ�?������
     */
    public int createSheet(HSSFWorkbook wb, ExcelComponentSet setting,
            ResultSet set, String fileName, String sheetName)
            throws SQLException, IOException {
        if (sheetName == null || sheetName.trim().equals(""))
            sheetName = "sheet" + (wb.getNumberOfSheets() + 1);
        HSSFSheet sheet = wb.createSheet(sheetName);

        int count = 0;
        if (setting.isDisplayHead()) //�Ƿ��ڵ�һ����Ӹ��еı�������
        {
            createExcelHeader(wb, sheet, setting);
            count++;
        }
        //������ã���ȡ��ǰ�?���������
        int maxRows = setting.isSheets() ? setting.getRowsOfSheet()
                : ExcelComponentSet.MAXROWS_ONESHEET;

        int maxWriteRows = setting.getWriteRows();

        CellDefined[] tmpDefined = setting.getHeadDefined();
        FileOutputStream out = new FileOutputStream(fileName);
        try {
            /**
             * �ô�ʹ�õķ�ʽ��Ϊ�˱���ʹ�÷���ResultSet.isLast()
             */
            while (isRun && count < maxRows && set.next()) {
                createRow(count, tmpDefined, set, sheet);
                count++;
                index++;
                updateProgress();
                for (int j = 0; isRun && count < maxRows && j < maxWriteRows
                        && set.next(); count++, j++) {
                    createRow(count, tmpDefined, set, sheet);
                    index++;
                    updateProgress();
                }
            }
            wb.write(out);
            out.flush();
        } finally {
            if (out != null)
                out.close();
            if (wb != null)
                disposeWork(wb);
            wb = null;
        }
        return count;
    }

    /**
     * ����һ�����
     * 
     * @param currentCount
     *            ��ǰ�е��к�
     * @param tmpDefined
     *            �ж���
     * @param set
     *            --������
     * @param sheet
     *            ��������������
     * @throws SQLException
     */
    public void createRow(int currentCount, CellDefined[] tmpDefined,
            ResultSet set, HSSFSheet sheet) throws SQLException {
        HSSFRow row = sheet.createRow(currentCount);
        for (int i = 0; isRun && i < tmpDefined.length; i++) {
            HSSFCell cell = row.createCell((short) i);
            int type = tmpDefined[i].getType();

            cell.setCellType(type); //���ñ������
//            if (type == HSSFCell.CELL_TYPE_STRING) {
//                cell.setEncoding(HSSFCell.ENCODING_UTF_16); //���ñ����ʽ
//            }
            if (tmpDefined[i].getCellStyle() != null) {
                cell.setCellStyle(tmpDefined[i].getCellStyle()); //���ñ��Ԫ�صķ��
            }
            if (tmpDefined[i].getEncoding() != null) //���ָ���˱����ʽ�����ձ����ʽ����ת��
            {
                byte[] b = set.getBytes(i);
                try {
                    cell.setCellValue(new HSSFRichTextString(new String(b, tmpDefined[i + 1]
                            .getEncoding())));
                } catch (UnsupportedEncodingException e) {
                    LogProxy.outputErrorLog(e);
                    cell.setCellValue(new HSSFRichTextString("no supported encoding!"));
                }
            } else
            {
                //�ޱ����ʽ������ȡ��
            	String tmpStr=set.getString(i + 1);
                cell.setCellValue(new HSSFRichTextString(set.wasNull()?"<NULL>":tmpStr));
            }
        }
    }

    /**
     * ���һ���?��ͬʱ������ݼ��ϵ�������
     * 
     * @return �?������һ�ж�Ӧ��ݼ��ϵ�����
     */
    protected int createSheet(HSSFWorkbook wb, ExcelComponentSet setting,
            Vector<Vector<Object>> data, String sheetName) {
        if (sheetName == null || sheetName.trim().equals(""))
            sheetName = "sheet" + (wb.getNumberOfSheets() + 1);
        HSSFSheet sheet = wb.createSheet(sheetName);

        CellDefined[] tmpDefined = setting.getHeadDefined();
        int count = 0;
        if (setting.isDisplayHead()) //�Ƿ��ڵ�һ����Ӹ��еı�������
        {
            createExcelHeader(wb, sheet, setting);
            count++;
        }
        //������ã���ȡ��ǰ�?���������
        int maxRows = setting.isSheets() ? setting.getRowsOfSheet()
                : ExcelComponentSet.MAXROWS_ONESHEET;

        //��ʼ�������
        for (; isRun && count < maxRows && index < data.size(); count++, index++) {
            HSSFRow row = sheet.createRow(count);
            Vector<Object> rowData = (Vector<Object>) data.get(index);
            for (int i = 0; isRun && i < rowData.size(); i++) {
                HSSFCell cell = row.createCell((short) i);
                int type = tmpDefined[i].getType();
                cell.setCellType(type);
//                if (type == HSSFCell.CELL_TYPE_STRING) {
//                    cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//                }
                if (tmpDefined[i].getCellStyle() != null) {
                    cell.setCellStyle(tmpDefined[i].getCellStyle());
                }
                Object tmpData=rowData.get(i);
                cell.setCellValue(new HSSFRichTextString(tmpData==null?"<NULL>":tmpData.toString()));
            }

            updateProgress();
        }
        return index;
    }

    /**
     * ����Excel��ͷ��Ϣ������������ɫΪ��ɫ������
     */
    protected void createExcelHeader(HSSFWorkbook wb, HSSFSheet sheet,
            ExcelComponentSet setting) {
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(transColor(setting.getHeadColumnColor(), wb));
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);
        CellDefined tmpDefined[] = setting.getHeadDefined();
        for (int i = 0; i < tmpDefined.length; i++) {
            HSSFCell cell = row.createCell((short) i);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//            cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(tmpDefined[i].getHeaderName()));
        }
    }

    /**
     * Check setting of excel exporter.
     * 
     * @param setting
     * @throws ExcelProcessException
     */
    private void checkExcelSet(ExcelComponentSet setting)
            throws ExcelProcessException {
        if (setting == null)
            throw new ExcelProcessException("Can't get Excel setting information,setting=null");
        CellDefined[] tmpDefined = setting.getHeadDefined();
        if (tmpDefined == null || tmpDefined.length == 0) {
            throw new ExcelProcessException("No define for columns��");
        }
    }

    /**
     * ���½����Ϣ
     *  
     */
    private void updateProgress() {
        waiter.setProgressValue(index);
    }

    /**
     * �����Ϣ
     * 
     * @return
     */
    public int getProcessInfo() {
        if (total == -1)
            return -1;
        return (index * 100 / total);
    }

    /**
     * @param isRun
     *            Ҫ���õ� isRun��
     */
    public synchronized void setRun(boolean isRun) {
        this.isRun = isRun;
    }

    /**
     * ���ظ�excel������Ƿ��������excel�ļ�
     * 
     * @return
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * ��ȡ��һ��������ļ���
     * 
     * @param file
     * @param seqNo
     * @param fileType
     * @return
     */
    public static String getNextFile(File file, int seqNo, String fileType) {
        if (seqNo < 0)
            throw new IllegalArgumentException("seqNo must be positive");
        if (seqNo == 0)
            return file.getAbsolutePath();
        String type = StringUtil.getFileType(file.getName());
        if (type == null)
            return file.getAbsolutePath() + seqNo;
        else if (type.equals(fileType))
            return StringUtil.getNoTypeFileStr(file.getAbsolutePath()) + seqNo
                    + "." + type;
        else
            return file.getAbsolutePath() + seqNo + "." + fileType;
    }
}
