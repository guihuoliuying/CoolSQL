/*
 * �������� 2006-8-19
 */
package com.cattsoft.coolsql.view.bookmarkview;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import com.cattsoft.coolsql.pub.component.TextEditor;

/**
 * @author liu_xlin ������ݿⴰ��ר�õĵ����ı������
 */
public class TextEditorExtend extends TextEditor {
    private int queryMode = 0;

    public TextEditorExtend() {
        super();
    }

    public TextEditorExtend(int length) {
        super(length);
    }

    public class SearchModeSelect extends JComboBox implements ItemListener {
        public SearchModeSelect() {
            this.addItem("==");
            this.addItem("..%");
            this.addItem("%..");
            this.addItem("%..%");
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
