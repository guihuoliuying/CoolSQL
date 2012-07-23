/*
 * �������� 2006-10-24
 */
package com.cattsoft.coolsql.action.common;

import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.SQLException;

import com.cattsoft.coolsql.action.common.ExportExcelOfTableAction.CloseWaitDialog;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.exportdata.Actionable;
import com.cattsoft.coolsql.exportdata.ExportDBData;
import com.cattsoft.coolsql.exportdata.ExportData;
import com.cattsoft.coolsql.exportdata.ExportFactory;
import com.cattsoft.coolsql.exportdata.ExportThread;
import com.cattsoft.coolsql.exportdata.excel.ExcelUtil;
import com.cattsoft.coolsql.pub.component.WaitDialog;
import com.cattsoft.coolsql.pub.component.WaitDialogManage;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.ConnectionUtil;
import com.cattsoft.coolsql.view.SqlEditorView;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.sqleditor.ScriptParser;

/**
 * @author liu_xlin ����ݿ��л�ȡ��ݣ�Ȼ�󵼳�Ϊ�ı��ļ�
 */
public class ExportDataOfDBAction extends PublicAction {
	private static final long serialVersionUID = 1L;

	/**
     * ��ݵ�����
     */
    private ExportDBData export = null;

    /**
     * ��������
     */
    private int processType = -1;

    public ExportDataOfDBAction(int processType) {
        export = (ExportDBData) ExportFactory.createExportForSql(null, null);
        this.processType = processType;
    }

    /*
     * ���� Javadoc��
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (!updateData())
            return;

        try {
            File tmpFile = export.selectFile(ExportData
                    .getFileType(processType));
            if (tmpFile == null)
                return;
            export.setFile(tmpFile);

        } catch (UnifyException e1) {
            LogProxy.errorReport(e1);
            return;
        }

        //������ݵ����߳�
        final ExportThread thread = new ExportThread(export, processType);
        final WaitDialog waiter = WaitDialogManage.getInstance().register(
                thread, GUIUtil.getMainFrame());
        waiter.addQuitAction(new Actionable() //��Ӷ�excel��ɵ�ȡ��
                {

                    public void action() {
                        if (processType == ExportData.EXPORT_EXCEL) {
                            ExcelUtil util = ExcelUtil.getInstance();
                            if (util.isRunning()) //�������ִ��,ȡ��excel�ĵ���
                                util.setRun(false);
                        } 
                        export.stopExport();

                        waiter.setPrompt(PublicResource
                                .getString("waitdialog.prompt.quit"));
                        
                        try {
                            ConnectionUtil.quitLongTimeStatement(thread);
                        } catch (SQLException e) {
                            LogProxy.errorReport(e);
                        }
                    }

                });
        thread.addAction(new CloseWaitDialog(waiter));

        waiter.setPrompt(getWaitPrompt());
        thread.start();       
        waiter.setVisible(true);

    }

    private String getWaitPrompt() {
        if (processType == ExportData.EXPORT_TEXT)
            return PublicResource.getString("waitdialog.prompt.txt");
        else if (processType == ExportData.EXPORT_EXCEL) {
            return PublicResource.getString("waitdialog.prompt.excel");
        } else if (processType == ExportData.EXPORT_HTML) {
            return PublicResource.getString("waitdialog.prompt.html");
        } else
            return "";
    }

    /**
     * ����
     * 
     * @return
     */
    private boolean updateData() {
        SqlEditorView view = ViewManage.getInstance().getSqlEditor();
        Bookmark bookmark = BookmarkManage.getInstance().getDefaultBookmark();
        if (bookmark == null) {
            LogProxy.infoMessage(PublicResource
                    .getSQLString("export.sql.nodatabase"));
            return false;
        }
        if (!bookmark.isConnected()) {
            LogProxy.infoMessage(PublicResource
                    .getSQLString("export.sql.notconnected"));
            return false;
        }
        export.setSource(bookmark); //�������Դ

        ScriptParser sqlParser=new ScriptParser(view.getSelectedText());
        
        /**
         * ���sql�Ƿ����
         */
        if (sqlParser.getSize() == 0) //���û��ѡ��sql
        {
            LogProxy.infoMessage(PublicResource.getSQLString("export.nosql"));
            return false;
        }
        /**
         * ���sql��������
         */
        if (sqlParser.getSize() > 1) {
            LogProxy.infoMessage(PublicResource
                    .getSQLString("export.sql.outofamount"));
            return false;
        }

        /**
         * ���sql������
         */
        String tmpSQL = (String) sqlParser.getCommand(0);
        if (tmpSQL != null && !tmpSQL.equals("")) {
            if (!checkSelectSQLType(tmpSQL)) {
                LogProxy.infoMessage(PublicResource
                        .getSQLString("export.sql.typeerror"));
                return false;
            }
        } else
            return false;

        export.setSql(tmpSQL);
        return true;

    }

    /**
     * У������sql����Ƿ�Ϊ��ѯ����
     * 
     * @param s
     * @return
     */
    private boolean checkSelectSQLType(String s) {
        String tmp = StringUtil.trim(s).toLowerCase();
        if (tmp.startsWith("select ")) {
            return true;
        } else
            return false;
    }
}
