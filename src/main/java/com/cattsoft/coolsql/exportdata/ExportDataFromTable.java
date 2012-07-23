/*
 * �������� 2006-10-24
 */
package com.cattsoft.coolsql.exportdata;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFCell;

import com.cattsoft.coolsql.exportdata.excel.CellDefined;
import com.cattsoft.coolsql.exportdata.excel.ExcelComponentSet;
import com.cattsoft.coolsql.exportdata.excel.ExcelProcessException;
import com.cattsoft.coolsql.exportdata.excel.ExcelUtil;
import com.cattsoft.coolsql.exportdata.html.HtmlExportException;
import com.cattsoft.coolsql.exportdata.html.PageListData;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.sql.SQLResultSetResults;
import com.cattsoft.coolsql.sql.util.TypesHelper;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.resultset.ButtonTableHeader;
import com.cattsoft.coolsql.view.resultset.DataSetTable;

/**
 * @author liu_xlin ����ؼ���������ı���ʽ����
 */
public class ExportDataFromTable extends ExportComponentData {

    /**
     * @param source
     */
    public ExportDataFromTable(JTable source) {
        super(source);

    }

    /**
     * �÷������Ե���JTable���͵���ݣ�ͬʱ��DataSetTable���ͽ��������⴦��
     */
    public void exportToTxt() throws UnifyException {
        File file = this.getFile();
        if (file == null)
            return;
        FileOutputStream out = null;
        try {
            GUIUtil.createDir(file.getAbsolutePath(),false, false);
            out = new FileOutputStream(file);

            JTable table = (JTable) this.getSource();
            //��ȡѡ�е��к���
            int cols = table.getColumnCount();
            int rows = table.getRowCount();

            String delimiter=Setting.getInstance().getProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_TEXT_DELIMITER, "\t");
            
            StringBuffer buffer = new StringBuffer();
            for (int n = 0; n < cols && isRunning(); n++) {
                buffer.append(table.getColumnName(n) + delimiter);
            }
            if (buffer.length() > 0) {
                buffer.deleteCharAt(buffer.length() - 1); //ɾ�������ַ�'\t'
                buffer.append("\r\n"); //��ӻس�����
                out.write(buffer.toString().getBytes());
            }

            buffer.delete(0, buffer.length());
            for (int i = 0; i < rows && isRunning(); i++) {
                buffer.delete(0, buffer.length());
                int j = 0;
                for (; j < cols; j++) {
                    Object tmpOb = null;
                    if (table instanceof DataSetTable) //�ô��ж϶�������DataSetTable�ؼ���Ҫ���⴦��
                        tmpOb = ((DataSetTable) table).getDisplayData(i, j);
                    else
                        tmpOb = table.getValueAt(i, j);
                    buffer.append((tmpOb != null ? tmpOb.toString() : "")
                            + delimiter);
                }
                buffer.deleteCharAt(buffer.length() - 1); //ɾ�������ַ�'\t'
                buffer.append("\r\n"); //��ӻس�����
                out.write(buffer.toString().getBytes());
            }

        } catch (FileNotFoundException e) {
            throw new UnifyException(PublicResource
                    .getSQLString("export.filenotfound")
                    + e.getMessage());
        } catch (IOException e) {
            throw new UnifyException(PublicResource
                    .getSQLString("export.filewriteerror"));
        } catch (Exception e) {
            LogProxy.internalError(e);
        } catch (Throwable e) {
            if (e instanceof Exception)
                LogProxy.errorReport((Exception) e);
            else
                LogProxy.errorMessage(PublicResource
                        .getSQLString("export.error.outofmemory"));
        } finally {
            if (isRunning())
                this.stopExport();
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * ����excel�ļ�
     * 
     * @throws UnifyException
     */
    public void exportToExcel() throws UnifyException {
        File file = this.getFile();
        if (file == null)
            return;
        ExcelUtil util = ExcelUtil.getInstance();
        JTable table = (JTable) this.getSource();

        CellDefined[] colsDefine = getExcelHeadDefine(table); //��ȡexcel��ͷ����

        //��ȡexcel��������Ϣ��
        Color headColor=Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_HEADCOLOR, Color.RED);
        
        ExcelComponentSet excelSet = new ExcelComponentSet(colsDefine, false,
                Setting.getInstance().getBoolProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_ISDISPLAYHEAD, true),
                ExcelComponentSet.MAXROWS_ONESHEET,
                Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_MAXROWSWRITE,
                		ExcelComponentSet.DEFAULT_WRITE),
                		headColor);

        try {
            util.setWaitDialog(getWaiter());
            util.buildExcel(file, excelSet, getExcelData(table));
        } catch (FileNotFoundException e) {
            throw new UnifyException(PublicResource
                    .getSQLString("export.filenotfound")
                    + e.getMessage());
        } catch (ExcelProcessException e) {
            LogProxy.errorReport(e);
        } catch (IOException e) {
            throw new UnifyException(PublicResource
                    .getSQLString("export.filewriteerror"));
        } catch (Throwable e) {
            if (e instanceof Exception)
                LogProxy.errorReport((Exception) e);
            else
                LogProxy.errorMessage(PublicResource
                        .getSQLString("export.error.outofmemory"));
        }
    }

    /**
     * �������html����ʽ����
     * 
     * @throws UnifyException
     *  
     */
    public void exportToHtml() throws UnifyException {

        File file = this.getFile();
        if (file == null)
            return;

        FileOutputStream out = null;

        try {
            JTable table = (JTable) this.getSource();
            Vector header = new Vector(table.getColumnCount());
            for (int i = 0; i < table.getColumnCount(); i++) {
                header.add(table.getColumnName(i));
            }

            Vector data = getExcelData(table);
            PageListData list = new PageListData(header, data, file.getName(),
                    "information!");
            list.setPageSize(Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_HTML_PAGESIZE, 50));

            String name = PageListData.filterFileName(file.getName());

            while (list.hasNext()) {
                String seqNo=list.getPage()==0?"":(list.getPage()+"");
                GUIUtil.createDir(file.getAbsolutePath(),false, false);
                out = new FileOutputStream(new File(file.getParent(), name
                        + seqNo + ".html"));
                out.write(list.getNextPageCode().getBytes());
                out.close();
            }
        } catch (FileNotFoundException e) {
            throw new UnifyException(PublicResource
                    .getSQLString("export.filenotfound")
                    + e.getMessage());
        } catch (HtmlExportException e) {
            throw new UnifyException(e.getMessage());
        } catch (IOException e) {
            throw new UnifyException(PublicResource
                    .getSQLString("export.filewriteerror"));
        } catch (Exception e) {
            LogProxy.internalError(e);
        } catch (Throwable e) {
            if (e instanceof Exception)
                LogProxy.errorReport((Exception) e);
            else
                LogProxy.errorMessage(PublicResource
                        .getSQLString("export.error.outofmemory"));
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * ��ȡ��ؼ������,�����������(Vector)����ʽ����
     * 
     * @param table
     * @return Vector
     */
    private Vector getExcelData(JTable table) {
        Vector tmpData = new Vector(table.getRowCount());
        Vector rowData = null;
        if (table instanceof DataSetTable) {

            DataSetTable dataTable = (DataSetTable) table;
            for (int i = 0; i < table.getRowCount(); i++) {
                rowData = new Vector(table.getColumnCount());
                for (int j = 0; j < table.getColumnCount(); j++) {
                    rowData.add(dataTable.getDisplayData(i, j));
                }
                tmpData.add(rowData);
            }
        } else { //����table�ؼ�

            TableModel model = table.getModel();

            for (int i = 0; i < table.getRowCount(); i++) {
                rowData = new Vector(table.getColumnCount());
                for (int j = 0; j < table.getColumnCount(); j++)
                    rowData.add(model.getValueAt(i, j));
                tmpData.add(rowData);
            }
        }
        return tmpData;
    }

    /**
     * ��ȡ���ؼ�һ�µ�excelͷ����
     * 
     * @param table
     *            --JTable
     * @return --CellDefined[]
     */
    private CellDefined[] getExcelHeadDefine(JTable table) {
        CellDefined[] colsDefind = new CellDefined[table.getColumnCount()];

        if (table instanceof DataSetTable) { //���Ϊ��ݿ����չʾ��ؼ�����Ҫ���⴦��
            DataSetTable dataTable = (DataSetTable) table;
            ButtonTableHeader header = (ButtonTableHeader) dataTable
                    .getTableHeader();

            for (int i = 0; i < table.getColumnCount(); i++) {
                TableColumn column = header.getColumnModel().getColumn(i);
                Object ob = column.getHeaderValue();
                if (ob instanceof SQLResultSetResults.Column) {
                    if (TypesHelper.isNumberic(((SQLResultSetResults.Column) ob)
                            .getSqlType())) //�������������
                    {
                        colsDefind[i] = new CellDefined(
                                HSSFCell.CELL_TYPE_NUMERIC, ob.toString());
                    } else
                        //��������ͨ��Ϊ�ַ���
                        colsDefind[i] = new CellDefined(
                                HSSFCell.CELL_TYPE_STRING, ob.toString());
                } else
                    colsDefind[i] = new CellDefined(HSSFCell.CELL_TYPE_STRING,
                            ob.toString());
            }
        } else {
            for (int i = 0; i < table.getColumnCount(); i++) {
                //Ĭ��Ϊ�ı�����
                colsDefind[i] = new CellDefined(HSSFCell.CELL_TYPE_STRING,
                        table.getColumnName(i));
            }
        }

        return colsDefind;
    }
}
