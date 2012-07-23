/*
 * �������� 2006-12-22
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.view.View;

/**
 * @author liu_xlin
 *��ͼ���������Ҽ�˵�
 */
public class ViewPopMenu extends BaseMenuManage {

    private JMenuItem maxItem=null;  //��󻯲˵���
    private JMenuItem hiddenItem=null;   //���ز˵���
    private JMenuItem restore=null;  //�ָ�ԭʼ��С
    /**
     * @param com
     */
    public ViewPopMenu(View com) {
        super(com);
    }

    protected void createPopMenu()
    {
        if(popMenu==null)
        {
            popMenu=new BasePopupMenu();
            
            //��󻯲˵���
            ActionListener toMaxListener=new ActionListener()
            {

                public void actionPerformed(ActionEvent e) {
                    getView().sizeToMax();                    
                }
                
            };
            maxItem=createMenuItem(PublicResource.getString("view.popup.max.label"),null,toMaxListener);
            popMenu.add(maxItem);
           
            //�ָ�ԭʼ��С�˵���
            ActionListener restoreListener=new ActionListener()
            {

                public void actionPerformed(ActionEvent e) {
                    getView().sizeToNormal();                    
                }
                
            };
            restore=createMenuItem(PublicResource.getString("view.popup.restore.label"),null,restoreListener);
            popMenu.add(restore);
            
            //������ͼ�˵���
            ActionListener hiddenListener=new ActionListener()
            {

                public void actionPerformed(ActionEvent e) {
    				/**
    				 * ����ԭʼλ��
    				 */
    				JSplitPane split=GUIUtil.getSplitContainer(getView());
    				int location=split.getDividerLocation();
    				split.putClientProperty(View.LASTLOCATION, location);
                	
                    getView().hidePanel(true);     
                    
                }
                
            };
            hiddenItem=createMenuItem(PublicResource.getString("view.popup.hidden.label"),null,hiddenListener);
            hiddenItem.setEnabled(true); 
            popMenu.add(hiddenItem);
        }
    }
    /* ���� Javadoc��
     * @see com.coolsql.pub.display.BaseMenuManage#itemSet()
     */
    public BasePopupMenu itemCheck() {
        createPopMenu();
        
        JSplitPane pane=getView().isMax(getView());
        if(pane==null)  //�����
        {
            GUIUtil.setComponentEnabled(true,maxItem);           
            GUIUtil.setComponentEnabled(false,restore);
        }else   //�Ѿ��������״̬
        {
            GUIUtil.setComponentEnabled(false,maxItem);
            GUIUtil.setComponentEnabled(true,restore);
        }
        return popMenu;
    }

    /* ���� Javadoc��
     * @see com.coolsql.pub.display.BaseMenuManage#getPopMenu()
     */
    public BasePopupMenu getPopMenu() {
       
        return  itemCheck();
    }
    public void setComponent(JComponent com)
    {
        if(!(com instanceof View))
            return ;
        
        super.setComponent(com);
    }
    protected View getView()
    {
        return (View)getComponent();
    }
}
