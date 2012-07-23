/*
 * Created on 2007-5-18
 */
package com.cattsoft.coolsql.system.menubuild;

import java.awt.Component;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.cattsoft.coolsql.pub.display.GUIUtil;

/**
 * @author liu_xlin
 *�˵����и����˵�(���˶���˵�)�����Ե�У��
 */
public class MenubarAvailabilityManage{

    static String visibleProperty="isVisibled";
    public final static String CHECKER="checker";
    private AvailabilityListener listener=null;
    private static MenubarAvailabilityManage manager=null;
    public static MenubarAvailabilityManage getInstance()
    {
        if(manager==null)
            manager=new MenubarAvailabilityManage();
        
        return manager;
    }
    private MenubarAvailabilityManage()
    {
        listener=new AvailabilityListener();
    }
    /**
     * �Բ˵�������п�����У���ע��
     * @param menu  --��Ҫע��Ĳ˵�����
     * @return
     */
    public boolean register(JMenu menu)
    {
        JPopupMenu pop=menu.getPopupMenu();
        pop.addPopupMenuListener(listener);
        return true;
    }
    private class AvailabilityListener implements PopupMenuListener
    {

        /* (non-Javadoc)
         * @see javax.swing.event.PopupMenuListener#popupMenuCanceled(javax.swing.event.PopupMenuEvent)
         */
        public void popupMenuCanceled(PopupMenuEvent e) {
            processState();
        }

        /* (non-Javadoc)
         * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent)
         */
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            
        }

        /* (non-Javadoc)
         * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent)
         */
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
           
            JPopupMenu pop=(JPopupMenu)e.getSource();
            String str=(String)pop.getClientProperty(visibleProperty);
            if(str==null)  //��һ����ҪУ��
            {
                pop.putClientProperty(visibleProperty,"true");
                
                //�Ըõ����˵��е����в˵������У��
                processEnableCheck(pop);
            }
        }
        
    }
    /**
     * ȥ�����в˵���״̬
     *
     */
    public void processState()
    {
        JMenuBar bar=MenuBuilder.getInstance().getMenuBar();
        for(int i=0;i<bar.getMenuCount();i++)
        {
            JPopupMenu menu=bar.getMenu(i).getPopupMenu();
            menu.putClientProperty(MenubarAvailabilityManage.visibleProperty,null);
        }
    }
    /**
     * ���?��ʽ�˵��в˵�/�˵���Ŀ�����У��
     * @param pop
     */
    public void processEnableCheck(JPopupMenu pop)
    {
        
        for(int i=0;i<pop.getComponentCount();i++)
        {
            Component com=pop.getComponentAtIndex(i);
            if(com instanceof JMenuItem)
            {
                processEnableCheck((JMenuItem)com);               
            }

        }
    }
    /**
     * �Բ˵���������ν��п����Ե�У��
     * @param item
     */
    private void processEnableCheck( JMenuItem item)
    {
        MenuItemEnableCheck checker=(MenuItemEnableCheck)item.getClientProperty(CHECKER);
        if(checker!=null)
        {
            GUIUtil.setComponentEnabled(checker.check(),item);
        }
        if(item instanceof JMenu)  //���Ϊ�˵�
        {
            for(int i=0;i<item.getComponentCount();i++)
            {
                Component com=item.getComponent(i);
                if(com instanceof JMenuItem)
                {
                    processEnableCheck((JMenuItem)com);
                }
            }
        }
    }
}
