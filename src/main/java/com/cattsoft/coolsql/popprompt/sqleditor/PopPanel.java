/*
 * Created on 2007-2-7
 */
package com.cattsoft.coolsql.popprompt.sqleditor;

import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * @author liu_xlin
 *���������������࣬���е�������̳и������
 */
public class PopPanel extends JPanel {

    public PopPanel()
    {
        super();
    }
    public PopPanel(boolean isDoubleBuffer)
    {
        super(isDoubleBuffer);
    }
    public PopPanel(LayoutManager layout)
    {
        super(layout);
    }
    public PopPanel(LayoutManager layout,boolean isDoubleBuffer)
    {
        super(layout,isDoubleBuffer);
    }
}
