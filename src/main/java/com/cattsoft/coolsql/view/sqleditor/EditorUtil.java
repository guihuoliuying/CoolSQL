/*
 * �������� 2006-6-11
 *
 */
package com.cattsoft.coolsql.view.sqleditor;

import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


/**
 * @author liu_xlin
 *��sql�༭������ʾ������з�װ�������ؼ�����ɫ���ؼ��ֵ�У�飬
 */
public class EditorUtil {
    public static String KEYWORDS[] = {
            "SELECT","DELETE","INSERT","UPDATE","FROM","AND","WHERE","NOT", "NULL","DESC","BY","LIKE","COUNT", "SET",
            "DISTINCT","ALTER","SUM","AS", "ASC","IS", "IN","GROUP", "MAX", "MIN", "JOIN", "OUTER", "RIGHT","LEFT", 
            "WITH","VALUES", "AVA","BINARY", "BIT", "BOOLEAN",  "CREATE", "BYTE", "CHAR", "CHARACTER","DROP", "EXISTS",
            "AUTOINCREMENT","COUNTER", "CURRENCY", "DATABASE", "DATE", "DATETIME",  "DISALLOW",  
            "DISTINCTROW", "DOUBLE", "FLOAT", "FLOAT4", "FLOAT8", "FOREIGN", "GENERAL", "BETWEEN", "ANY","ALL", "ADD",
            "GUID", "HAVING", "INNER", "IGNORE", "IMP", "INDEX", "INT", "CONSTRAINT",
            "INTEGER", "INTEGER1", "INTEGER2", "INTEGER4", "INTO", "KEY", "LEVEL", 
            "LOGICAL", "LONG", "LONGBINARY", "LONGTEXT","MEMO","MOD", "MONEY", "COLUMN",
            "NUMBER", "NUMERIC", "OLEOBJECT", "ON", "PIVOT", "OPTION", "PRIMARY", "ORDER", 
            "OWNERACCESS", "PARAMETERS", "PERCENT", "REAL", "REFERENCES",  "SHORT", 
            "SINGLE", "SMALLINT", "SOME", "STDEV", "STDEVP", "STRING","TABLE", "TABLEID", "TEXT", 
            "TIME", "TIMESTAMP", "TOP", "TRANSFORM", "UNION", "UNIQUE",  "VALUE",  "VAR", 
            "VARBINARY", "VARCHAR", "VARP", "YESNO"
        };
    /**
     * �ؼ��ֵ���ɫ
     */
    public final static Color keywordColor=new Color(126, 0, 75);
    /**
     * ������±༭�������ֵ���ɫ
     */
    public final static Color normalColor=Color.BLACK;
    /**
     * ������ڵ�ֵ����ɫ
     */
    public final static Color valueColor=Color.BLUE;
    /**
     * ���ֵ���ɫ
     */
    public final static Color numberColor=Color.RED;
    /**
     * ע����ɫ
     */
    public final static Color commentColor=Color.GREEN;
    /**
     * ����
     */
    public final static Color spaceColor=Color.BLACK;
    
    /*
     * sql�༭��������Ԫ����������
     */
    public final static MutableAttributeSet KEYWORD_SET =new SimpleAttributeSet();

    public final static MutableAttributeSet NORMAL_SET =new SimpleAttributeSet();

    public final static MutableAttributeSet VALUE_SET = new SimpleAttributeSet();

    public final static MutableAttributeSet NUMBER_SET = new SimpleAttributeSet();
    
    public final static MutableAttributeSet COMMENT_SET = new SimpleAttributeSet();
       
    static
    {
        //****************
        StyleConstants.setForeground(KEYWORD_SET, EditorUtil.keywordColor);
        StyleConstants.setBold(KEYWORD_SET, true);

        StyleConstants.setForeground(NORMAL_SET, EditorUtil.normalColor);
        StyleConstants.setBold(NORMAL_SET, false);

        StyleConstants.setForeground(VALUE_SET, EditorUtil.valueColor);
        StyleConstants.setBold(VALUE_SET, false);

        StyleConstants.setForeground(NUMBER_SET, EditorUtil.numberColor);
        StyleConstants.setBold(NUMBER_SET, false);

        StyleConstants.setForeground(COMMENT_SET, EditorUtil.commentColor);
        StyleConstants.setBold(COMMENT_SET, false);     
    }
	/**
	 * У���Ƿ�Ϊsql�ؼ���
	 * @param s
	 * @return
	 */
	public static boolean isKeyWord(String s)
	{
	
		for(int i=0;i<KEYWORDS.length;i++)
		{
			if(KEYWORDS[i].equals(s.toUpperCase()))
				return true;
		}
		return false;
	}
	public static Color getKeyWordColor(String s)
	{
		if(s.indexOf("'")>-1)
			return null;
		for(int i=0;i<10;i++)
		{
			if(s.indexOf(String.valueOf(i))>-1)
				return null;
		}
		return isKeyWord(s) ? keywordColor: normalColor;
	}
	/**
	 * ��ȡָ��λ�õ�����ǰ��ɫ
	 * @param doc
	 * @param offset
	 * @return
	 */
    public static Color getDocForeground(Document doc,int offset)
	{
    	Element e=getDocElement(doc,offset);
    	AttributeSet set=e.getAttributes().copyAttributes();
        return StyleConstants.getForeground(set); 
	}
    public static Element getDocElement(Document doc,int offset)
    {
    	Element root = doc.getDefaultRootElement();
    	int line = root.getElementIndex(offset); //ȡoffset��ǰ��
    	Element lineElement=root.getElement(line); //ȡ����Ԫ��
    	int index=lineElement.getElementIndex(offset);   //ȡ����Ԫ�ص���Ԫ��
    	return  lineElement.getElement(index);
    	
    }
    public static int getElementStart(Document doc,int offset,Color c)
	{
    	Element e=getDocElement(doc,offset-1);
    	String str="";
    	try {
			str=doc.getText(e.getStartOffset(),e.getEndOffset()-e.getStartOffset());
		} catch (BadLocationException e1) {
		    return -1;
		}
    	AttributeSet set=e.getAttributes().copyAttributes();
    	Color foreColor=StyleConstants.getForeground(set);
    	if(c==foreColor)
    	{
    		return e.getStartOffset();
    	}
        return offset; 
	}
}
