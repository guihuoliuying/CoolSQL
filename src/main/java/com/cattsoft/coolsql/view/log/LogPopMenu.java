/*
 * �������� 2006-7-13
 *
 */
package com.cattsoft.coolsql.view.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

import com.cattsoft.coolsql.action.common.ClearTextAction;
import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.action.common.TextCopyAction;
import com.cattsoft.coolsql.action.common.TextSelectAllAction;
import com.cattsoft.coolsql.exportdata.ExportData;
import com.cattsoft.coolsql.exportdata.ExportFactory;
import com.cattsoft.coolsql.pub.component.BaseMenuManage;
import com.cattsoft.coolsql.pub.component.BasePopupMenu;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;


/**
 * @author liu_xlin
 * ��־�༭�˵�
 */
public class LogPopMenu extends BaseMenuManage {
    /**
     * ����
     */
	private JMenuItem copy = null;
	
	/**
	 * ���
	 */
	private JMenuItem clearAll = null;

	/**
	 * ȫѡ
	 */
	private JMenuItem selectAll = null;
	
	/**
	 * �Ƿ�����ʾ
	 */
	private JCheckBoxMenuItem wrapSet=null;
	
	/**
	 * ������־����
	 */
	private JMenuItem export=null;
	public LogPopMenu(JTextComponent com) {
		super(com);
		createPopMenu();
	}
    /* ���� Javadoc��
     * @see com.coolsql.pub.display.BaseMenuManage#itemSet()
     */
    public BasePopupMenu itemCheck() {
        JTextComponent tCom=(JTextComponent)this.getComponent();
		if (tCom.getSelectedText() == null
				||tCom.getSelectedText().trim().equals("")) 
		{
		    if(copy.isEnabled())
		    {
		        copy.setEnabled(false);
		    }
		}else
		{
		    if(!copy.isEnabled())
		    {
		        copy.setEnabled(true);
		    }
		}
		if(tCom.getText().equals(""))
		{
		    if(clearAll.isEnabled())
		        clearAll.setEnabled(false);
		}else
		{
		    if(!clearAll.isEnabled())
		        clearAll.setEnabled(true);
		}
        return popMenu;
    }
    /* ���� Javadoc��
     * @see com.coolsql.pub.display.BaseMenuManage#getPopMenu()
     */
    public BasePopupMenu getPopMenu() {
        return itemCheck();
    }
	protected void createPopMenu() {
		popMenu = new BasePopupMenu();

		//���Ʋ˵���
		Action copyAction = new TextCopyAction((JTextComponent)this.getComponent());
		copy = this.createMenuItem(PublicResource
				.getString("TextEditor.popmenu.copy"), PublicResource.getIcon("TextMenu.icon.copy"),
				copyAction);
		popMenu.add(copy);

		//��ղ˵���
		clearAll=this.createMenuItem(PublicResource
				.getString("logView.popmenu.clearall"), PublicResource.getIcon("logView.popmenu.icon.clearall"),
				new ClearTextAction((JTextComponent)this.getComponent()));
		popMenu.add(clearAll);
		
		
		popMenu.addSeparator();
		
		//������־�˵���
		Action exportAction=new PublicAction()
		{

            public void actionPerformed(ActionEvent e) {
                ExportData exporter=ExportFactory.createExportForTextComponent((JTextComponent)LogPopMenu.super.getComponent()); 
                try {
                    exporter.exportToTxt();
                } catch (UnifyException e1) {
                    LogProxy.errorReport(e1);
                }
            }
		     
		};
		export=this.createMenuItem(PublicResource
				.getString("logView.log.exportlog"), PublicResource.getIcon("logView.log.exportlog.icon"),
				exportAction);
		popMenu.add(export);
		
		//�Ƿ��Զ����в˵���
		wrapSet=new JCheckBoxMenuItem(PublicResource.getString("logView.popmenu.iswrap"));
		wrapSet.addActionListener(new ActionListener()
		        {

                    public void actionPerformed(ActionEvent e) {
                        if(wrapSet.getState())
                        {
                            ((LogTextPane)getComponent()).setWrap(true);                           
                        }else
                            ((LogTextPane)getComponent()).setWrap(false); 
                    }
		            
		        }
		);
		popMenu.add(wrapSet);
		
		popMenu.addSeparator();
		
		//ȫѡ�˵���
		Action selectAllAction = new TextSelectAllAction((JTextComponent)this.getComponent());
		selectAll = this.createMenuItem(PublicResource
				.getString("TextEditor.popmenu.selectAll"), PublicResource.getIcon("popmenu.icon.blank"),
				selectAllAction);
		popMenu.add(selectAll);
		

	}
}
