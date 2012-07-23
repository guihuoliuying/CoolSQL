/*
 * �������� 2006-10-26
 */
package com.cattsoft.coolsql.exportdata;

import java.util.Vector;

import com.cattsoft.coolsql.pub.component.WaitDialogManage;
import com.cattsoft.coolsql.pub.display.exception.NotRegisterException;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ��ݵ���ʱ��Ϊ�˱��ⷢ��swing����̲߳���ȫ���¼�����д���������������
 */
public class ExportThread extends Thread {
    private ExportData export = null;

    /**
     * ��ݵ���������
     */
    private int processType;

    /**
     * �������������߳�ִ����ɺ�����Ҫִ�еĶ���
     */
    private Vector actions = new Vector();

    public ExportThread(ExportData export, int processType) {
        super("exportData");
        if (export == null) {
            throw new IllegalArgumentException("exporter is null");
        }
        if (processType != ExportData.EXPORT_TEXT
                && processType != ExportData.EXPORT_EXCEL
                && processType != ExportData.EXPORT_HTML)
            throw new IllegalArgumentException("export type is unknown:"
                    + processType);

        this.export = export;
        this.processType = processType;
    }

    public void run() {
        try {
            Thread.sleep(50);
            export.setWaiter(WaitDialogManage.getInstance().getDialogOfCurrent());
            if (processType == ExportData.EXPORT_TEXT) {

                export.exportToTxt();

            } else if (processType == ExportData.EXPORT_EXCEL) {
                export.exportToExcel();
            } else if (processType == ExportData.EXPORT_HTML) {
                export.exportToHtml();
            }
        } catch (UnifyException e) {
            LogProxy.errorReport(e);
        } catch (InterruptedException e) {
            LogProxy.outputErrorLog(e);
        } catch (NotRegisterException e) {
            LogProxy.errorReport(e);
        } finally {
            WaitDialogManage.getInstance().disposeRegister(this);
            runActions();
        }
    }

    /**
     * ����߳�ִ����ɺ�Ķ����¼�
     * 
     * @param action
     */
    public void addAction(Actionable action) {
        synchronized (actions) {
            if (action != null)
                actions.add(action);
        }
    }

    private void runActions() {
        synchronized (actions) {
            for (int i = 0; i < actions.size(); i++) {
                Actionable action = (Actionable) actions.get(i);
                action.action();
            }
        }
    }

    /**
     * @return ���� processType��
     */
    public int getProcessType() {
        return processType;
    }
}
