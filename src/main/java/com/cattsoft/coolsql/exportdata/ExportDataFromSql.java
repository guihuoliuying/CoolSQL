package com.cattsoft.coolsql.exportdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.exportdata.excel.ExcelComponentSet;
import com.cattsoft.coolsql.exportdata.excel.ExcelProcessException;
import com.cattsoft.coolsql.exportdata.excel.ExcelUtil;
import com.cattsoft.coolsql.exportdata.excel.ResultUtil;
import com.cattsoft.coolsql.pub.component.WaitDialog;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.sql.ConnectionUtil;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * Saving the result of querying to local file. 
 * @author Kenny liu
 */
public class ExportDataFromSql extends ExportDBData {

    /**
     * The statement should be executed.
     */
    private String sql;

    /**
     * @param source
     */
    public ExportDataFromSql(String sql, Bookmark bookmark) {
        super(bookmark);
        this.sql = sql;

    }

    /**
     * Update the progress information of executing.
     * 
     */
    private void updateProgress(int value) throws UnifyException {
        WaitDialog waiter = getWaiter();
        waiter.setProgressValue(value);
    }

    /*
     * @see com.coolsql.exportdata.Exportable#exportToTxt()
     */
    public void exportToTxt() throws UnifyException {

        File file = this.getFile();
        if (file != null) {
            FileOutputStream out = null;
            ResultSet set = null;
            try {
                GUIUtil.createDir(file.getAbsolutePath(),false, false);
                out = new FileOutputStream(file);
                set = ConnectionUtil.executeQuery((Bookmark) this.getSource(),
                        sql, true);
                ResultSetMetaData metaData = set.getMetaData(); 

                String delimiter=Setting.getInstance().getProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_TEXT_DELIMITER, "\t");
                /**
                 * Write the header information to local file.
                 */
                int i = 0;
                StringBuffer buffer = new StringBuffer();
                for (; i < metaData.getColumnCount() - 1 && isRunning(); i++) {
                    buffer.append(metaData.getColumnName(i + 1) + delimiter);

                }
                buffer.append(metaData.getColumnName(i + 1) + "\r\n");
                out.write(buffer.toString().getBytes());

                int count = 0;
                if (isRunning()) {
                    int max = ResultUtil.countResultRow(set);
                    if (max > 0) {
                        getWaiter().setTaskLength(max);
                    }
                }
                /**
                 * Retrieve the row data.
                 */
                while (set.next() && isRunning()) {
                    out.write((getRowData(set,delimiter) + "\r\n").getBytes());
                    count++;
                    updateProgress(count);
                }
            } catch (SQLException e) {
                LogProxy.SQLErrorReport(e);
            } catch (FileNotFoundException e) {
                throw new UnifyException(PublicResource
                        .getSQLString("export.filenotfound")
                        + e.getMessage());
            } catch (IOException e) {
                throw new UnifyException(PublicResource
                        .getSQLString("export.filewriteerror"));
            } catch (UnifyException e) {
                throw e;
            } catch (Throwable e) {
                if (e instanceof Exception)
                    LogProxy.errorReport((Exception) e);
                else
                {
                    LogProxy.outputErrorLog(e);
                    LogProxy.errorMessage(PublicResource
                            .getSQLString("export.error.outofmemory"));
                }
            } finally {
                if (set != null) {
                    try {
                        set.close();
                        ConnectionUtil.closeStatement(set);
                    } catch (Exception e) {
                    	LogProxy.errorReport(e);
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                    	LogProxy.errorReport(e);
                    }
                }
            }
        }

    }

    /*
     * 
     * @see com.coolsql.exportdata.Exportable#exportToExcel()
     */
    public void exportToExcel() throws UnifyException {
        File file = this.getFile();
        if (file != null) {
            ResultSet set = null;
            try {
                set = ConnectionUtil.executeQuery((Bookmark) this.getSource(),
                        sql,true);
                ExcelComponentSet setting = ResultUtil.getExcelSet(set);
                ExcelUtil util = ExcelUtil.getInstance();
                util.setWaitDialog(getWaiter());
                if(isRunning())
                    util.buildExcel(file, setting, set);
            } catch (FileNotFoundException e) {
                throw new UnifyException(PublicResource
                        .getSQLString("export.filenotfound")
                        + e.getMessage());
            } catch (SQLException e) {
                LogProxy.SQLErrorReport(e);
            } catch (ExcelProcessException e) {
                LogProxy.errorReport(e);
            } catch (IOException e) {
                throw new UnifyException(PublicResource
                        .getSQLString("export.filewriteerror"));
            } catch (UnifyException e) {
                throw e;
            } catch (Throwable e) {
                if (e instanceof Exception)
                    LogProxy.errorReport((Exception) e);
                else {
                    LogProxy.outputErrorLog(e);
                    LogProxy.errorMessage(PublicResource
                            .getSQLString("export.error.outofmemory"));
                }
            } finally {
                if (set != null) {
                    try {
                        set.close();
                        ConnectionUtil.closeStatement(set);
                    } catch (Exception e) {
                    	LogProxy.errorReport(e);
                    }
                }
            }
        }
    }

    /*
     * 
     * @see com.coolsql.exportdata.Exportable#exportToHtml()
     */
    public void exportToHtml() throws UnifyException {

    }

    /**
     * Parse one row to columns. 
     */
    private String getRowData(ResultSet set,String delimiter) throws SQLException, IOException {
        StringBuffer buffer = new StringBuffer();

        int cols = set.getMetaData().getColumnCount();
        int i = 0;
        for (; i < cols - 1; i++) {
            buffer.append(getColumValue(i + 1, set) + delimiter);
        }
        buffer.append(getColumValue(i + 1, set));
        return buffer.toString();
    }

    /**
     * Return the value of specified column.
     */
    private String getColumValue(int columnNumber, ResultSet set)
            throws SQLException, IOException {
        Reader reader = set.getCharacterStream(columnNumber);
        if (reader != null) {
            StringBuffer buffer = new StringBuffer();
            int retVal = reader.read();
            while (retVal >= 0) {
                buffer.append((char) retVal);
                retVal = reader.read();
            }
            reader.close();
            return buffer.toString();
        } else {
            return null;
        }
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
