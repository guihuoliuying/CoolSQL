/*
 * Created on 2007-2-6
 */
package com.cattsoft.coolsql.pub.display;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;

import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.view.sqleditor.SyntaxHighlighter;

/**
 * @author liu_xlin
 *�ĵ�����ؼ��ָ�����ʾ��������
 */
public class HighlightDocumentListener implements DocumentListener {

    private boolean isHighlight=true;  //�Ƿ�����ĵ�
    private DefaultStyledDocument document=null;
    
    private SyntaxHighlighter keywordRender;
    public HighlightDocumentListener(DefaultStyledDocument document)
    {
        this.document=document;
        keywordRender=new SyntaxHighlighter(document);
        keywordRender.setName(StringUtil.getHashNameOfObject(document));
        SyntaxHighlighter.addThread(document,keywordRender);
    }
    /* (non-Javadoc)
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
     */
    public void changedUpdate(DocumentEvent e) {
    }

    /* (non-Javadoc)
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
     */
    public void insertUpdate(DocumentEvent e) {
        if(!isHighlight)
            return;
            
        int offset=e.getOffset();   //�������ʼλ��
        int end=offset+e.getLength();  //�������ݵ�β��ƫ����
        Element element=getLineElement(offset);  //��ȡ��ʼλ�����ڵ���Ԫ��       
        int elementEnd=-1;
        int elementStart=-1;
        if(element!=null)
        {
            elementStart=element.getStartOffset(); //��Ԫ�ص���ʼƫ��
            elementEnd=element.getEndOffset();  //��Ԫ�ص�β��ƫ��
        }
        
        int txtLength=0;
        if(end<elementEnd)  //������һ���н���
        {
            if(elementStart>=end)
                return;
            txtLength=elementEnd-elementStart;
        }else   //�ڶ������޸�
        {
            txtLength=end-elementStart;
        }
        try {

            keywordRender.updateText(document.getText(elementStart, txtLength), elementStart);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
     */
    public void removeUpdate(DocumentEvent e) {
        if(!isHighlight)
            return;
        
        int offset=e.getOffset()-e.getLength();   //ɾ���Ĺ��λ��
        Element element=getLineElement(offset);  //��ȡ��ʼλ�����ڵ���Ԫ��       
        int elementEnd=-1;
        int elementStart=-1;
        if(element!=null)
        {
            elementStart=element.getStartOffset(); //��Ԫ�ص���ʼƫ��
            elementEnd=element.getEndOffset();  //��Ԫ�ص�β��ƫ��
        }
        if(elementStart==elementEnd)
            return;
        int txtLength=elementEnd-elementStart;
        try {
            keywordRender.updateText(document.getText(elementStart,txtLength), elementStart);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }
    /**
     * ��ȡָ��λ�����ڵ���Ԫ��
     * 
     * @param offset
     * @return
     */
    private Element getLineElement(int offset) {
        if (offset > document.getLength() - 1)
            return null;
        Element root = document.getDefaultRootElement();
        int line = root.getElementIndex(offset);
        Element lineElement = root.getElement(line);
        return lineElement;
    }
    /**
     * @return Returns the isHighlight.
     */
    public boolean isHighlight() {
        return isHighlight;
    }
    /**
     * @param isHighlight The isHighlight to set.
     */
    public void setHighlight(boolean isHighlight) {
        this.isHighlight = isHighlight;
    }
}
