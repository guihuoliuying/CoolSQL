/*
 * Created on 2007-3-9
 */
package com.cattsoft.coolsql.data.display.action;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.cattsoft.coolsql.pub.component.FindFrame;
import com.cattsoft.coolsql.pub.component.FindProcess;
import com.cattsoft.coolsql.pub.display.BaseTable;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.TableFindProcess;

/**
 * @author liu_xlin
 *
 */
public class BaseTableFindAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	
	BaseTable table;
    public BaseTableFindAction(BaseTable table)
    {
        this.table=table;
    }
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Window upParent=(Window)GUIUtil.getUpParent(table,java.awt.Window.class);
        FindFrame frame=null;
        FindProcess processer=new TableFindProcess();
        if(upParent instanceof Frame)
            frame=FindFrame.getFindFrameInstance((Frame)upParent,"table information find",table,processer);
        else
            frame=FindFrame.getFindFrameInstance((Dialog)upParent,"table information find",table,processer);
        
        GUIUtil.centerFrameToFrame(upParent,frame);
        frame.setVisible(true);
    }

}
