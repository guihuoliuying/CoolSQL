package com.cattsoft.coolsql.pub.component;

import java.io.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * The number editor.
 * @author Kenny Liu
 */
@SuppressWarnings("serial")
public class NumberEditor extends TextEditor implements ActionListener,
        FocusListener, Serializable {
    public NumberEditor() {
        this(true);
    }

    public NumberEditor(boolean addAction) {
        this(16, 0, addAction);
    }

    public NumberEditor(int intPartLen) {
        this(intPartLen, true);
    }

    public NumberEditor(int intPartLen, boolean addAction) {
        this(intPartLen, 0, addAction);
    }

    public NumberEditor(int maxLen, int decLen) {
        this(maxLen, decLen, true);
    }

    public NumberEditor(int maxLen, int decLen, boolean addAction) {
        setPreferredSize(new Dimension(150, 25));
        setDocument(new NumberDocument(maxLen, decLen));
        super.setHorizontalAlignment(JTextField.RIGHT);
        if (addAction)
            addActionListener(this);
        addFocusListener(this);
    }

    public NumberEditor(int maxLen, int decLen, double minRange,
            double maxRange, boolean addAction) {
        setPreferredSize(new Dimension(150, 25));
        setDocument(new NumberDocument(maxLen, decLen, minRange, maxRange));
        super.setHorizontalAlignment(JTextField.RIGHT);
        if (addAction)
            addActionListener(this);
        addFocusListener(this);
    }

    /**
     * set up the max length of integer port.
     */
    public void setMaxLength(int maxLength)
    {
        if(maxLength>16)
            return;
        NumberDocument doc=(NumberDocument)getDocument();
        doc.setMaxLength(maxLength);
    }
    public void setDecLength(int decLength) {
        NumberDocument doc=(NumberDocument)getDocument();
        doc.setDecLength(decLength);
    }
    public void actionPerformed(ActionEvent e) {
        transferFocus();
    }

    public void focusGained(FocusEvent e) {
        selectAll();
    }

    public void focusLost(FocusEvent e) {
    }

	protected class NumberDocument extends PlainDocument {
        int maxLength = 16;

        int decLength = 0;

        double minRange = -Double.MAX_VALUE;

        double maxRange = Double.MAX_VALUE;

        public NumberDocument(int maxLen, int decLen) {
            maxLength = maxLen;
            decLength = decLen;
        }

        /**
         * @param decLen
         *             The length of decimal
         * @param maxLen
         *             The max length of value (include decimal)
         * @param minRange
         *            double The min value.
         * @param maxRange
         *            double The max value.
         */
        public NumberDocument(int maxLen, int decLen, double minRange,
                double maxRange) {
            this(maxLen, decLen);
            this.minRange = minRange;
            this.maxRange = maxRange;
        }

        public NumberDocument(int decLen) {
            decLength = decLen;
        }

        public NumberDocument() {
        }

        /**
         * 1��"f,F,d,D" can't be inputed
         * 2��If the fisrt number is zero, the second char must be decimal point.
         * 3��Decimal point can't be inputed in Integer mode.
         */
        public void insertString(int offset, String s, AttributeSet a)
                throws BadLocationException {
            String str = getText(0, getLength());

            if (s.equals("F")
                    || s.equals("f")
                    || s.equals("D")
                    || s.equals("d")

                    || (str.trim().equals("0")
                            && !s.substring(0, 1).equals(".") && offset != 0)

                    || (s.equals(".") && decLength == 0)) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            String strIntPart = "";
            String strDecPart = "";
            String strNew = str.substring(0, offset) + s
                    + str.substring(offset, getLength());
            strNew = strNew.replaceFirst("-", ""); //
            int decPos = strNew.indexOf(".");
            if (decPos > -1) {
                strIntPart = strNew.substring(0, decPos);
                strDecPart = strNew.substring(decPos + 1);
            } else {
                strIntPart = strNew;
            }
            if (strIntPart.length() > (maxLength - decLength)
                    || strDecPart.length() > decLength
                    || (strNew.length() > 1
                            && strNew.substring(0, 1).equals("0") && !strNew
                            .substring(1, 2).equals("."))) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            try {
                if (!strNew.equals("") && !strNew.equals("-")) {
                    double d = Double.parseDouble(strNew);
                    if (d < minRange || d > maxRange) {
                        throw new Exception("");
                    }
                }
            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            super.insertString(offset, s, a);
        }

        /**
         * @return Returns the decLength.
         */
        public int getDecLength() {
            return decLength;
        }
        /**
         * @param decLength The decLength to set.
         */
        public void setDecLength(int decLength) {
            this.decLength = decLength;
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
        }
    }
}
