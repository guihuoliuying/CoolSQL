/*
 * �������� 2006-9-15
 */
package com.cattsoft.coolsql.action.common;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JTable;

/**
 * @author liu_xlin
 * ��ؼ��ĸ����¼�����
 */
public class TableCopyAction extends ComponentCopyAction {

    /**
     * @param com
     */
    public TableCopyAction(JTable com) {
        super(com);
    }
    /* ���� Javadoc��
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        copy(this.getComponent());
    }
    /* ���� Javadoc��
     * @see com.coolsql.action.button.ComponentCopyAction#copy()
     */
    public void copy(JComponent com) {
        if(!(com instanceof JTable))
            return;
        //��ȡѡ�е��к���
        int[] cols=((JTable)com).getSelectedColumns();
        int[] rows=((JTable)com).getSelectedRows();
        
        if(cols==null||rows==null||rows.length<1||cols.length<1)
            return;
        
        StringBuffer buffer=new StringBuffer();
        for(int i=0;i<rows.length;i++)
        {
            for(int j=0;j<cols.length;j++)
            {
                Object tmpOb=((JTable)this.getComponent()).getValueAt(rows[i],cols[j]);
                buffer.append((tmpOb!=null?tmpOb.toString():"")+"\t");
            }
            buffer.deleteCharAt(buffer.length() - 1);  //ɾ��tab
            buffer.append("\n");   
        }
        buffer.deleteCharAt(buffer.length() - 1); //ɾ����
        StringSelection ss = new StringSelection(buffer.toString());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss,ss);
    }

}
