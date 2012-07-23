/*
 * �������� 2006-7-2
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu.Separator;

import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.MenuCheckable;

/**
 * @author liu_xlin
 * �����Ҽ�˵��Ļ���
 */
public abstract class BaseMenuManage {
    protected BasePopupMenu popMenu = null;
	private JComponent com=null;
    /**
     * �˵���У����򣬷����������
     */
    private Vector<MenuCheckable> checkSet=new Vector<MenuCheckable>();
    public BaseMenuManage(JComponent com)
    {
    	super();
    	this.com=com;
    }
    /**
     * ���������˵�
     *
     */
    protected abstract void createPopMenu();
    /**
     * �Բ˵���Ŀ����Խ�������
     * @return
     */
    public abstract BasePopupMenu itemCheck();
    /**
     * ��ȡ��ͼ���Ҽ�˵�
     * @return
     */
    public abstract BasePopupMenu getPopMenu();
	/**
	 * @return ���� view��
	 */
	public JComponent getComponent() {
		return com;
	}
	public void setComponent(JComponent com) {
	    this.com=com;
	}
	/**
	 * �����µĲ˵���
	 * 
	 * @param txt
	 * @param icon
	 * @param action
	 * @return
	 */
	protected JMenuItem createMenuItem(String txt, Icon icon, ActionListener action) {
		JMenuItem it = new JMenuItem(txt, icon);
		if (action != null)
			it.addActionListener(action);

		return it;
	}
	/**
	 * �󶨿�ݼ�
	 * @param key  --��ݼ�
	 * @param action  --ִ�еĶ���
	 * @param isGlobal �Ƿ���ȫ�ֵ�ʹ�øÿ�ݼ�
	 */
	protected void bindKey(String key,Action action,boolean isGlobal)
	{
	    GUIUtil.bindShortKey(com,key,action,isGlobal);

	}
	/**
	 * �󶨿�ݼ�,ָ���˾�����������
	 * @param componnet  --��Ҫ���󶨿�ݼ�����
	 * @param key   --��ݼ�
	 * @param action  --ִ�еĶ���
	 * @param isGlobal �Ƿ���ȫ�ֵ�ʹ�øÿ�ݼ�
	 */
	protected void bindKey(JComponent componnet,String key,Action action,boolean isGlobal)
	{
	    GUIUtil.bindShortKey(componnet,key,action,isGlobal);

	}
    /**
     * ��Ӳ˵���
     * 
     * @param label
     *            ��ǩ
     * @param aciton
     *            �˵�ѡ�е��¼�����
     * @param icon
     *            �˵���ͼ��
     * @param isAddSeparator
     *            �Ƿ���ӷָ���
     * @return �����´����Ĳ˵���,�����˵�û�г�ʼ��,������nullֵ
     */
    public JMenuItem addMenuItem(String label, ActionListener action, Icon icon,
            boolean isAddSeparator) {
        if (popMenu == null)
            createPopMenu();

        JMenuItem tmpItem = null;
        int index = getAddPostion();
        if (index >= 0) {
            if (isAddSeparator) //��ӷָ���
            {
                popMenu.insert(new Separator(), index);
                index++;
            }
            tmpItem = createMenuItem(label, icon, action);

            popMenu.insert(tmpItem, index);
        }
        return tmpItem;
    }
    /**
     * ��Ӳ˵�����������ڸò˵�����Ӳ˵����������Ӳ˵���Ȼ���ȡ�ò˵�������������Ӧ�Ĳ˵��
     * @param label  --�˵����ı���ʾ
     * @param isAddSeparator  --�Ƿ���ӷָ���
     * @return  --����ӵĲ˵�����
     */
    public JMenu addMenu(String label,boolean isAddSeparator)
    {
        if (popMenu == null)
            createPopMenu();
        
        JMenu tmpMenu = null;
        int index = getAddPostion();
        if (index >= 0) {
            if (isAddSeparator) //��ӷָ���
            {
                popMenu.insert(new Separator(), index);
                index++;
            }
            tmpMenu = new JMenu(label);

            popMenu.insert(tmpMenu, index);
        }
        return tmpMenu;
    }
    /**
     * ��ȡ��Ӳ˵���λ�ã��÷������Ա�����
     * @return  --�²˵���ӵ�λ�ã����˵�δ����������-1
     */
    public int getAddPostion()
    {
        if(popMenu==null)
            return -1;
        
        return popMenu.getComponentCount();
    }
    /**
     * ��Ӳ˵�У�����
     * @param checkable
     */
    public void addMenuCheck(MenuCheckable checkable)
    {
        if(checkable!=null)
        {
            checkSet.add(checkable);
        }
    }
    /**
     * �˵�������Դ���
     *
     */
    protected void menuCheck()
    {
        for(int i=0;i<checkSet.size();i++)
        {
            MenuCheckable check=(MenuCheckable)checkSet.get(i);
            check.check(com);
        }
    }
}
