package com.cattsoft.coolsql.exportdata;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.cattsoft.coolsql.pub.component.WaitDialog;
import com.cattsoft.coolsql.pub.display.FileSelectFilter;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.system.PropertyManage;

/**
 * The base class for exporting data.
 * @author Kenny liu
 */
public abstract class ExportData implements Exportable {

    public final static int EXPORT_TEXT = 0;

    public final static int EXPORT_EXCEL = 1;

    public final static int EXPORT_HTML = 2;

    /**
     * The data source object. Currently may be database or table component.
     */
    protected Object source = null;

    /**
     * The file into which data will be saved
     */
    private File file=null;
    
    /**
     * The flag indicates whether exporting should be stopped.
     */
    private boolean isTextRun;
    
    /**
     * The prompting dialog.
     */
    private WaitDialog waiter=null;
    public ExportData(Object source) {
        this.source = source;
        isTextRun=true;
    }

    /**
     * @return source��
     */
    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
    
    private void setTextRun(boolean isTextRun) {
        this.isTextRun = isTextRun;
    }

    public boolean isRunning()
    {
        return isTextRun;
    }
    public void launchTextRun()
    {
        isTextRun=true;
    }
    /**
     * Stop exporting.
     *
     */
    public void stopExport()
    {
        setTextRun(false);
    }
    /**
     * Select file for exporting.
     * @see com.coolsql.exportdata.Exportable#getFile()
     */
    public File selectFile(String type) throws UnifyException {
        String tmp=StringUtil.trim(type);
        FileFilter filter=null;
        if(!tmp.equals(""))
            filter=new FileSelectFilter("."+tmp,tmp);
        
        File file = null;
        if(filter==null)
            file=GUIUtil.selectFileNoFilter(GUIUtil.getMainFrame(),PropertyManage.getSystemProperty().getSelectFile_exportData());
        else
            file=GUIUtil.selectFileByFilter(GUIUtil.getMainFrame(),filter,PropertyManage.getSystemProperty().getSelectFile_exportData());
        
        if (file == null)
            return null;
        PropertyManage.getSystemProperty().setSelectFile_exportData(file.getAbsolutePath());
        
        if (file.exists() && !file.canWrite()) //the file can't be written
        {
            throw new UnifyException(PublicResource
                    .getSQLString("export.filediabledwrite"));
        }
        
        String oldName = file.getAbsolutePath();
        String newName = filterFileName(oldName, type);
        if (!oldName.equals(newName))
            file=new File(newName);
        return file;

    }

    private String filterFileName(String oldName, String fileType) {
        if (StringUtil.trim(fileType).equals(""))
            return oldName;
        int index = -1;
        if ((index = oldName.lastIndexOf(".")) != -1) {
            String tmp = oldName.substring(index + 1, oldName.length());
            if (tmp.equals(fileType)) 
            {
                return oldName;
            } else {
                return oldName + "." + fileType;
            }
        } else {
            return oldName + "." + fileType;
        }
    }
    /**
     * Return the suffix of file into which data will be saved.
     */
   public static String getFileType(int processType)
   {
       if(processType==EXPORT_TEXT)
       {
           return null;
       }else if(processType==EXPORT_EXCEL)
       {
           return "xls";
       }else if(processType==EXPORT_HTML)
       {
           return "html";
       }else
           return null;
   }
    public WaitDialog getWaiter() {
        return waiter;
    }
    public void setWaiter(WaitDialog waiter) {
        this.waiter = waiter;
    }
}
