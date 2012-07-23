/*
 * �������� 2006-10-27
 */
package com.cattsoft.coolsql.action.common;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JTable;

import com.cattsoft.coolsql.exportdata.Actionable;
import com.cattsoft.coolsql.exportdata.ExportData;
import com.cattsoft.coolsql.exportdata.ExportFactory;
import com.cattsoft.coolsql.exportdata.ExportThread;
import com.cattsoft.coolsql.exportdata.excel.ExcelUtil;
import com.cattsoft.coolsql.pub.component.CommonFrame;
import com.cattsoft.coolsql.pub.component.WaitDialog;
import com.cattsoft.coolsql.pub.component.WaitDialogManage;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *����excel�ļ����¼�������
 */
public class ExportExcelOfTableAction extends PublicAction {

    private JTable table=null;
    private ExportData export=null;
    public ExportExcelOfTableAction(JTable table)
    {
       super(null);
       this.table=table;
       export=ExportFactory.createExportForTable(table);
    }
    /* ���� Javadoc��
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {

        try {
            File tmpFile=export.selectFile(ExportData.getFileType(ExportData.EXPORT_EXCEL));
            if(tmpFile==null)
                return;
            export.setFile(tmpFile);
        } catch (UnifyException e1) {
            LogProxy.errorReport(e1);
            return;
        }
        //������ݵ����߳�
        ExportThread thread=new ExportThread(export,ExportData.EXPORT_EXCEL);
        final WaitDialog waiter = WaitDialogManage.getInstance().register(
                thread, CommonFrame.getTopOwner(table));
        waiter.addQuitAction(new Actionable()   //��Ӷ�excel��ɵ�ȡ��
                {

                    public void action() {
                        ExcelUtil util=ExcelUtil.getInstance();
                        if(util.isRunning()) //�������ִ��,ȡ��excel�ĵ���
                            util.setRun(false);
                        waiter.setPrompt(PublicResource.getString("waitdialog.prompt.quit"));
                    }
            
                }
        );
        thread.addAction(new CloseWaitDialog(waiter));
        waiter.setPrompt(PublicResource.getString("waitdialog.prompt.excel"));
        thread.start();        
        waiter.setVisible(true);
    }
    /**
     * @author liu_xlin
     *�˳��رյȴ�Ի���
     */
    protected static class CloseWaitDialog implements Actionable
    {
        private WaitDialog waiter;
        public CloseWaitDialog(WaitDialog waiter)
        {
           this.waiter=waiter;   
        }
        public void action() {
           
            waiter.dispose();
        }

    }
    /**
     * @return ���� table��
     */
    public JTable getTable() {
        return table;
    }
    /**
     * @param table Ҫ���õ� table��
     */
    public void setTable(JTable table) {
        this.table = table;
        export.setSource(table);
    }
}
