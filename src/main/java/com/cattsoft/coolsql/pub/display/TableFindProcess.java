/*
 * Created on 2007-4-28
 */
package com.cattsoft.coolsql.pub.display;

import java.awt.Component;

import javax.swing.JTable;

import com.cattsoft.coolsql.pub.component.FindProcess;

/**
 * @author liu_xlin table�ؼ��в���ƥ����Ĵ�����
 */
public class TableFindProcess implements FindProcess {

    /**
     * ��ǰƥ���������������
     */
    private int row;

    private int column;

//    private boolean isFind = false;//�Ƿ��ҵ�ƥ��ı��
    private int count=0;  //��¼��ǰѭ�����ҵĴ���

    /**
     * ����ݵ�����ȷ
     * @param config
     * @param table
     */
    private void checkLoactionValidation(FindProcessConfig config, JTable table) {
        if (config.getForward() == FindProcessConfig.FORWARD) {
            if(row>=table.getRowCount()&&!config.isCircle())
                return ;
            row++;
            column++;
        }else
        {
            if(row<0&&!config.isCircle())
                return;
            row--;
            column--;
        }
        
        if (row > table.getRowCount() || row < -1) {
            if (config.getForward() == FindProcessConfig.FORWARD) {

                row = 0;
                column=0;

            }else
            {
                row = table.getRowCount()-1;
                column=table.getColumnCount()-1;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coolsql.pub.display.FindProcess#find(com.coolsql.data.display.FindProcessConfig,
     *      java.awt.Component)
     */
    public boolean find(FindProcessConfig config, Component com)
            throws Exception {
        JTable table = (JTable) com;
        
        /**
         * ����ؼ���ѡ�У���ô��ѡ�еı��ʼ����
         */
        row=table.getSelectedRow();
        column=table.getSelectedColumn();
        if(row==-1) //���û�б�ѡ��,��ͷ��ʼ����
        {
            row=0;
            column=0;
        }
        
        count=1;
        return findTable(config,table);
    }
    private boolean findTable(FindProcessConfig config,JTable table)
    {
        checkLoactionValidation(config,table);
        
        if (config.getForward() == FindProcessConfig.FORWARD) {
            
            for (; row < table.getRowCount(); row++) {
                count++;
                column=0;
                for (; column < table.getColumnCount(); column++) {
                    Object ob = table.getValueAt(row, column);
                    if (ob == null)
                        continue;
                    String str = ob.toString();
                    if (match(config, str)) {
                        table.changeSelection(row, column, false, false);
                        
                        return true;
                    }
                }
            }
        } else {
            for (; row > -1; row--) {
                count++;
                column=table.getColumnCount()-1;
                for (; column > -1; column--) {
                    Object ob = table.getValueAt(row, column);
                    if (ob == null)
                        continue;
                    String str = ob.toString();
                    if (match(config, str)) {
                        table.changeSelection(row, column, false, false);
                        
                        return true;
                    }
                }
            }
        }
       
        /**
         * �����ؼ���ͷ����β����λ��ֵ���е���
         */
//        if (row == -1) //������
//        {
//            row = table.getRowCount() - 1;
//            column = table.getColumnCount() - 1;
//        } else //��ǰ����
//        {
//            row = 0;
//            column = 0;
//        }
        /**
         * ���û���ҵ������Ҵ���ƥ����������ҡ�
         */
        int tmp = count / table.getRowCount();
        if (tmp < 1) {  //����;���б���
            if (config.isCircle()) {

                return findTable(config, table);
            }else
                return false;
        }else
        
            //���ѭ��������û���ҵ�������false
            return false;
    }
    /**
     * �Ը�ı��Ԫ��ֵ����ƥ��
     * 
     * @param config
     *            --ƥ��ѡ��
     * @param value
     *            --���Ԫ��ֵ
     * @return --true��ƥ��ɹ� false��ƥ�䲻�ɹ�
     */
    private boolean match(FindProcessConfig config, String value) {
        if (config.getCaseMatch() == FindProcessConfig.IGNORECASE) //�����Դ�Сд
        {
            value = value.toLowerCase();
            config.setKeyWord(config.getKeyWord().toLowerCase());
        }
        if (config.getMatchMode() == FindProcessConfig.MATCH_FULL) {
            return value.equals(config.getKeyWord());
        } else
            return value.indexOf(config.getKeyWord()) > -1;
    }

    /**
     * @return Returns the column.
     */
    public int getColumn() {
        return column;
    }

    /**
     * @param column
     *            The column to set.
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * @return Returns the row.
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row
     *            The row to set.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /* (non-Javadoc)
     * @see com.coolsql.pub.display.FindProcess#resultInfo()
     */
    public String resultInfo() {
        return (row+1)+","+(column+1);
    }
}
