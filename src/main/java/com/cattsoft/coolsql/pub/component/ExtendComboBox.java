/*
 * Created on 2007-1-22
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

/**
 * @author liu_xlin
 *�Կɱ༭��Ͽ����������չ,����������һ��Ͽ���з�װ,ʹ���߽���Э��ѡ�����.
 */
public class ExtendComboBox extends EditComboBox {
	private static final long serialVersionUID = 1L;
	private int queryMode = 0;

    public ExtendComboBox() {
        super();
    }

    public class SearchModeSelect extends JComboBox implements ItemListener {
		private static final long serialVersionUID = 1L;

		public SearchModeSelect() {
            this.addItem("==");
            this.addItem("����%");
            this.addItem("%����");
            this.addItem("%����%");
            this.setSelectedIndex(3);
            queryMode=3;
            this.addItemListener(this);
        }

        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
         */
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) { //���ָ���ѡ���Ժ�,������Ӧ�ĵ���
                queryMode = this.getSelectedIndex();
            }
        }

    }

    /**
     * @return ���� queryMode��
     */
    public int getQueryMode() {
        return queryMode;
    }

    /**
     * @param queryMode
     *            Ҫ���õ� queryMode��
     */
    public void setQueryMode(int queryMode) {
        this.queryMode = queryMode;
    }
}
