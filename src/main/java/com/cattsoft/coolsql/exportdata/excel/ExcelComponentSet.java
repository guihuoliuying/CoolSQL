/*
 * �������� 2006-8-30
 *
 */
package com.cattsoft.coolsql.exportdata.excel;

import java.awt.Color;

/**
 * @author liu_xlin
 *         ����excel����ʱ�Ļ������Ϣ������������Ϣ��ÿ���ĵ��������������ÿ������������������Ƿ���������������������?��������������
 */
public class ExcelComponentSet {
    public static final int MAXROWS_ONESHEET = 60000; //�ļ�ֻ��һ���?ʱ�����Ƶ��������

    public static final int maxSheet = 6; //���?��

    public static final int MAXROWS_SHEETS = 20000; //�ļ��������?ʱ��ÿ���?���������

    public static final int MAXROWS_WRITE = 60000; //���������ۼ�60000��ʱ���ļ�дһ��

    public static final int DEFAULT_WRITE=6000;
    /**
     * �Ƿ񻮷�Ϊ��?
     */
    private boolean isSheets = false;

    /**
     * ���ֵı?�� ***�ݲ�ʹ��***
     */
    private int sheets = 1;

    /**
     * ÿ���?����������
     */
    private int rowsOfSheet = MAXROWS_SHEETS;

    /**
     * �ڴ��ۼ����д���ļ�������
     */
    private int writeRows = DEFAULT_WRITE;

    /**
     * ����ж���
     */
    private CellDefined[] headDefined = null;

    /**
     * ��һ���Ƿ���ʾ��������
     */
    private boolean isDisplayHead = false;

    private Color headColumnColor;
    public ExcelComponentSet() {
        this(null, false, true, MAXROWS_ONESHEET, DEFAULT_WRITE,Color.RED);
    }

    public ExcelComponentSet(CellDefined[] headDefined, boolean isSheets,
            boolean isDisplayHead, int rowsOfSheet, int writeRows,Color headColumnColor) {
        this.headDefined = headDefined;
        this.isSheets = isSheets;
        this.isDisplayHead = isDisplayHead;

        if (rowsOfSheet < 1)
            this.rowsOfSheet = MAXROWS_SHEETS;
        else if (rowsOfSheet > MAXROWS_SHEETS)
            this.rowsOfSheet = MAXROWS_SHEETS;
        else
            this.rowsOfSheet = rowsOfSheet;

        if (writeRows < 1 || writeRows > MAXROWS_WRITE) {
            this.writeRows = MAXROWS_WRITE;
        } else
            this.writeRows = writeRows;
        
        if(headColumnColor==null)
        {
        	this.headColumnColor=Color.RED;
        }else
        {
        	this.headColumnColor=headColumnColor;
        }
    }

    public CellDefined[] getHeadDefined() {
        return headDefined;
    }

    public void setHeadDefined(CellDefined[] headDefined) {
        this.headDefined = headDefined;
    }

    public boolean isDisplayHead() {
        return isDisplayHead;
    }

    public void setDisplayHead(boolean isDisplayHead) {
        this.isDisplayHead = isDisplayHead;
    }

    public boolean isSheets() {
        return isSheets;
    }

    public void setSheets(boolean isSheets) {
        this.isSheets = isSheets;
    }

    public int getSheets() {
        return sheets;
    }

    public void setSheets(int sheets) {
        this.sheets = sheets;
    }

    /**
     * @return ���� rowsOfSheet��
     */
    public int getRowsOfSheet() {
        return rowsOfSheet;
    }

    /**
     * @param rowsOfSheet
     *            Ҫ���õ� rowsOfSheet��
     */
    public void setRowsOfSheet(int rowsOfSheet) {
        this.rowsOfSheet = rowsOfSheet;
    }

    /**
     * @return ���� writeRows��
     */
    public int getWriteRows() {
        return writeRows;
    }

    /**
     * @param writeRows
     *            Ҫ���õ� writeRows��
     */
    public void setWriteRows(int writeRows) {
        this.writeRows = writeRows;
    }

	/**
	 * @return the headColumnColor
	 */
	public Color getHeadColumnColor() {
		return this.headColumnColor;
	}
	/**
	 * @param headColumnColor the headColumnColor to set
	 */
	public void setHeadColumnColor(Color headColumnColor) {
		this.headColumnColor = headColumnColor;
	}
}
