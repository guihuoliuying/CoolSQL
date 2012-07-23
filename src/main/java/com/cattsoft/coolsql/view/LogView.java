/*
 * �������� 2006-6-8
 *
 */
package com.cattsoft.coolsql.view;

import javax.swing.JScrollPane;

import com.cattsoft.coolsql.action.common.ClearTextAction;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.view.log.LogPopMenu;
import com.cattsoft.coolsql.view.log.LogTextPane;
import com.cattsoft.coolsql.view.mouseEventProcess.PopupAction;


/**
 * @author liu_xlin
 *��־��ӡ����
 */
public class LogView extends TabView {

	private static final long serialVersionUID = 1L;
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(LogView.class);
	
	private LogTextPane log=null;
	private LogPopMenu popMenu=null;
	public LogView()
	{
		super();
		init();
	}
	public void init()
	{
		log=new LogTextPane(); 
		log.addMouseListener(new PopupAction(this));
		this.setContent(new JScrollPane(log));
		popMenu=new LogPopMenu(log);
		
		createIconButton();
		
	}
    /**
     * ����ͼ�갴ť
     *  
     */
    private void createIconButton() {
        this.addIconButton(PublicResource
                .getIcon("logView.popmenu.icon.clearall"),
                new ClearTextAction(log), PublicResource
                        .getString("logView.iconbutton.clear.tooltip")); //ǰһҳ��ݰ�ť
    }
	/* ���� Javadoc��
	 * @see java.awt.Component#getName()
	 */
	public String getName() {
		return stringMgr.getString("view.log.title");
	}
	/* ���� Javadoc��
	 * @see src.view.Display#dispayInfo()
	 */
	public void dispayInfo() {
		
	}
	/* ���� Javadoc��
	 * @see src.view.Display#popupMenu()
	 */
	public void popupMenu(int x,int y) {
		popMenu.getPopMenu().show(log, x, y);
		
	}
    /* (non-Javadoc)
     * @see com.coolsql.view.View#createActions()
     */
    protected void createActions() {
        
    }
    @Override
    public String getTabViewTitle()
    {
    	return PublicResource.getString("logView.tabtitle");
    }
    @Override
    public int getTabViewIndex()
    {
    	return 1;
    }
	/* (non-Javadoc)
	 * @see com.coolsql.view.View#doAfterMainFrame()
	 */
	@Override
	public void doAfterMainFrame() {
		
	}
}
