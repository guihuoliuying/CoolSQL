/*
 * �������� 2006-7-13
 *
 */
package com.cattsoft.coolsql.view.log;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.cattsoft.coolsql.pub.display.WrapTextPane;

/**
 * @author liu_xlin
 *  
 */
public class LogTextPane extends WrapTextPane {

    private static String family = "";

    public LogTextPane() {
        super();
        this.setDocument(LogDocument.getInstance());
        this.setEditable(false);
        MutableAttributeSet myAttributeSet=new SimpleAttributeSet();
		StyleConstants.setFontSize(myAttributeSet,12);
		this.setParagraphAttributes(myAttributeSet,true);
    }

    /**
     * @return ���� family��
     */
    public static String getFamily() {
        return family;
    }

    /**
     * @param family
     *            Ҫ���õ� family��
     */
    public static void setFamily(String family) {
        LogTextPane.family = family;
    }
}
