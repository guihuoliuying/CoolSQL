/*
 * Created on 2007-1-8
 */
package com.cattsoft.coolsql.pub.display;

import java.awt.Dimension;

import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

/**
 * @author liu_xlin
 *�ɻ��б༭���ı��༭���
 */
public class WrapTextPane extends JTextPane {
	private static final long serialVersionUID = 1L;
	/**
     * �Ƿ��Զ�����
     */
	private boolean isWrap;
	
	public WrapTextPane()
	{
	    super();
	}
	public WrapTextPane(StyledDocument doc)
	{
	    super(doc);
	}
	/**
	 * ��д�༭�����Ƿ�ɹ�����У�鷽��
	 * �뷽��setSize(Dimension)���ʹ��
	 */
    public boolean getScrollableTracksViewportWidth() {
        return isWrap ? super.getScrollableTracksViewportWidth()
                : (getSize().width < getParent().getSize().width - 1);
    }

    public void setSize(Dimension d) {
        if (!isWrap) {
            if (d.width < getParent().getSize().width) {
                d.width = getParent().getSize().width;
            }
            d.width += 1;
        }
        super.setSize(d);
    }
    /**
     * @return ���� isWrap��
     */
    public boolean isWrap() {
        return isWrap;
    }
    /**
     * @param isWrap
     *            Ҫ���õ� isWrap��
     */
    public void setWrap(boolean isWrap) {
        this.isWrap = isWrap;
        this.updateUI();
    }
}
