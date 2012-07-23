/*
 * Created on 2007-5-18
 */
package com.cattsoft.coolsql.system.menubuild;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

import com.cattsoft.coolsql.pub.display.GUIUtil;

/**
 * @author liu_xlin
 *�˵����¼��������࣬�����װ����㴫���ActionListener
 */
public class TempAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private ActionListener listener;
    
    private MenuItemEnableCheck menuChecker=null;
    public TempAction(ActionListener listener,MenuItemEnableCheck menuChecker)
    {
        if(listener==null)
            throw new IllegalArgumentException("no listener object was passed");
        this.listener=listener;
        this.menuChecker=menuChecker;
    }
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource()==GUIUtil.getMainFrame().getJMenuBar())  //if the acton is global
		{
			if(menuChecker!=null&&!menuChecker.check())
			{
				return ;
			}
		}
        MenubarAvailabilityManage.getInstance().processState();
        listener.actionPerformed(e);
    }

}
