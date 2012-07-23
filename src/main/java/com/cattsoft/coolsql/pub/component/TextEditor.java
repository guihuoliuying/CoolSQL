/*
 * �������� 2006-6-27
 *
 */
package com.cattsoft.coolsql.pub.component;

import javax.swing.JTextField;
import javax.swing.plaf.metal.MetalTheme;

import com.cattsoft.coolsql.pub.display.GUIUtil;


/**
 * @author liu_xlin
 * 
 * Custom text field component, add a text popup menu to component, including copy, paste, cut and select all.
 */
public class TextEditor extends JTextField {
	private static final long serialVersionUID = 1L;
	/**
     * �Ƿ������ʾ  true:�����ʾ��Ϣ   false:�������ʾ��Ϣ
     */
    private boolean isTooltip=false;
    public TextEditor() {
        super();
        GUIUtil.installDefaultTextPopMenu(this);
        MetalTheme theme=SubTheme.getCurrentTheme();
        if (theme!=null&&theme.getClass() == MyMetalTheme.class) {
            this.setSelectionColor(DisplayPanel.getSelectionColor());
        }
    }

    public TextEditor(int length) {
        super(length);
        GUIUtil.installDefaultTextPopMenu(this);
        MetalTheme theme=SubTheme.getCurrentTheme();
        if (theme!=null&&theme.getClass() == MyMetalTheme.class) {
            this.setSelectionColor(DisplayPanel.getSelectionColor());
        }

    }
    public boolean isTooltip() {
        return isTooltip;
    }
    /**
     * ��д�����ı��༭���ķ���
     *@param txt  --��ӵ��ı���Ϣ
     */
    public void setText(String txt)
    {
        if(isTooltip && getToolTipText() == null)//���������ʾ,�޸���Ӧ����ʾ��Ϣ
        {
            this.setToolTipText(txt);
        }
        super.setText(txt);
    }
    /**
     * ���ø��ı����Ƿ������ʾ��Ϣ
     * @param isTooltip
     */
    public void setTooltipEnable(boolean isTooltip) {
        if(this.isTooltip==isTooltip)
            return;
        this.isTooltip = isTooltip;
        if(isTooltip)
        {
            this.setToolTipText(this.getText());
        }else
            this.setToolTipText(null);
    }
}
