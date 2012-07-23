/*
 * Created on 2007-1-22
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Component;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

import com.cattsoft.coolsql.pub.display.GUIUtil;

/**
 * @author liu_xlin
 *���Ҽ�˵������ѡ��ؼ�
 */
public class EditComboBox extends JComboBox {

	private static final long serialVersionUID = 1L;
	public EditComboBox()
    {
        super();
//        setEditor(new IComboBoxEditor());
        initUI();
    }
    public EditComboBox(ComboBoxModel model)
    {
        super(model);
//        setEditor(new IComboBoxEditor().get);
        initUI();
    }
    public EditComboBox(Object[] items)
    {
        super(items);
//        setEditor(new IComboBoxEditor());
        initUI();
    }
    public EditComboBox(Vector items)
    {
        super(items);
//        setEditor(new IComboBoxEditor());
        initUI();
    }
    protected void initUI()
    {
    	Component component=getEditor().getEditorComponent();
    	if(component instanceof JTextComponent)
    	{
    		GUIUtil.installDefaultTextPopMenu((JTextComponent)component);
    	}
    }
}
