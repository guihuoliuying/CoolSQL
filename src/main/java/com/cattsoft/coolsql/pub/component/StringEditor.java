package com.cattsoft.coolsql.pub.component;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.Serializable;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

//import java.awt.event.*;
/**
 * @author liu_xlin �����ַ������Ƶ��ı�����༭���
 */
public class StringEditor extends TextEditor implements Serializable {
    /**
     * �ı��༭�����󳤶�
     */
    private int maxLength;
    public StringEditor() {
        this(10000);
    }

    public StringEditor(int MaxLen) {
        maxLength=MaxLen;
        setPreferredSize(new Dimension(100, 25));
        setDocument(new StringEditorDocument(MaxLen));
    }

    protected class StringEditorDocument extends PlainDocument {
        int MaxLength;

        public StringEditorDocument(int MaxLen) {
            MaxLength = MaxLen;
        }

        public void insertString(int offset, String s, AttributeSet attributeSet)
                throws BadLocationException {
            int sLen = s.length();
            int conLen = getLength();
            String str = getText(0, conLen);
            if (((sLen + conLen) > MaxLength) || s.equals("\'")
                    || s.equals("\"")) {

                if (conLen != MaxLength) {
                    s = s.substring(0, MaxLength - conLen);

                } else {

                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
            }
            super.insertString(offset, s, null);
        }
        protected void setMaxLength(int maxLength)
        {
            MaxLength=maxLength;
        }
    }
    /**
     * @return Returns the maxLength.
     */
    public int getMaxLength() {
        return maxLength;
    }
    /**
     * @param maxLength The maxLength to set.
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        ((StringEditorDocument)this.getDocument()).setMaxLength(maxLength);
    }
}