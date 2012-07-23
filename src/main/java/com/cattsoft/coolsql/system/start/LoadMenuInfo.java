/*
 * Created on 2007-5-18
 */
package com.cattsoft.coolsql.system.start;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.Task;
import com.cattsoft.coolsql.system.menubuild.MenuBuilder;
import com.cattsoft.coolsql.system.menubuild.MenubarAvailabilityManage;

/**
 * @author liu_xlin
 *װ��ϵͳ�˵�
 */
public class LoadMenuInfo implements Task {

    /* (non-Javadoc)
     * @see com.coolsql.system.Task#getDescribe()
     */
    public String getDescribe() {
        
        return PublicResource.getString("system.launch.loadmenuinfo");
    }

    /* (non-Javadoc)
     * @see com.coolsql.system.Task#execute()
     */
    public void execute() {
        /**
         * ���ز˵�
         */
        MenuBuilder.getInstance().loadSystemMenu();
        JMenuBar mb=MenuBuilder.getInstance().getMenuBar();
        GUIUtil.getMainFrame().setJMenuBar(mb);
        registerMenuBarForEnableCheck();

    }
    /**
     * ���˵����еĶ���˵�����ע�������У��
     *
     */
    private void registerMenuBarForEnableCheck()
    {
        JMenuBar bar=MenuBuilder.getInstance().getMenuBar();
        for(int i=0;i<bar.getMenuCount();i++)
        {
            JMenu menu=bar.getMenu(i);

            MenubarAvailabilityManage.getInstance().register(menu);
        }
    }
    /* (non-Javadoc)
     * @see com.coolsql.system.Task#getTaskLength()
     */
    public int getTaskLength() {
        return 1;
    }

}
