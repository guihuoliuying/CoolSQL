/**
 * 
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.jidesoft.plaf.basic.ThemePainter;
import com.jidesoft.swing.JideMenu;
import com.jidesoft.swing.JideSplitButton;

/**
 * @author kenny liu
 *
 * 2007-11-17 create
 */
public class SplitButton extends JideSplitButton {

	private static final long serialVersionUID = 1L;

	private static final String DATAKEY = "dataKey";

    /**
     * ����ѡ��˵�
     */
    private JPopupMenu popMenu=null;
    
    /**
     * �������Ǹÿؼ��ĺ������,ÿ��ѡ��һ���˵����,���¸�ֵ
     */
    private Object selectedData = null;
    
    /**
     * ��ǰ�ؼ���Ĭ��ѡ��˵���������߰�ť������ִ�иò˵������Ӧ�Ĳ�����
     */
    private JMenuItem defaultItem = null;
    
    private MenuItemClickListener menuListener = null;
    
    /**
     * �˵��ķָ��ߣ����ڽ�defautItem��˵���ָ�����
     */
    private JSeparator separator = null;
    
    /**
     * ��߰�ť���¼������
     */
    private Vector actions = null;
    public SplitButton(Icon icon)
    {
    	super();
    	setAction(getLeftButtonAction());
    	setIcon(icon);
    	init();
    }
    public SplitButton(String label, Icon icon)
    {
    	super();
    	setAction(getLeftButtonAction());
    	setText(label);
    	setIcon(icon);
    	init();
    }
    /**
     * ��߰�ť��������ļ����?
     * @return
     */
    protected Action getLeftButtonAction()
    {
    	return new AbstractAction()
    	{
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				runAction(e);
				
			}
    		
    	};
    }
    protected void init() {
		actions = new Vector();
		separator = new JSeparator();
		popMenu=getPopupMenu();
		defaultItem = new JMenuItem();
		menuListener = new MenuItemClickListener();
		defaultItem.addActionListener(menuListener);
		setBackgroundOfState(ThemePainter.STATE_ROLLOVER,BasePanel.getThemeColor());
//		setBackgroundOfState(ThemePainter.STATE_PRESSED,BasePanel.getThemeColor());
		
		setPopupMenuCustomizer(new JideMenu.PopupMenuCustomizer()
		{

			public void customize(JPopupMenu menu) {
			}			
		}
		);
	}
    public void setPopupMenuVisible(boolean b)
    {
    	super.setPopupMenuVisible(b);
    }
    /**
	 * ѡ��ĳһ�˵���󣬸���ȱʡ�˵�����
	 * 
	 * @param selectedItem
	 *            ѡ�еĲ˵������
	 */
    private void updateDefaultItemData(JMenuItem selectedItem) {
        JMenuItem tmpItem = (JMenuItem) popMenu.getComponent(0);
        if (tmpItem == defaultItem) //�Ѿ������ȱʡ�˵���
        {
            if (selectedItem.getClientProperty(DATAKEY) != defaultItem
                    .getClientProperty(DATAKEY)) //���ȱʡ������һ���˵���(��ȥȱʡ�˵��ͷָ���)��Ӧ����ݲ����,����ȱʡ�˵������
            {
                defaultItem.setText(selectedItem.getText());
                defaultItem.setIcon(selectedItem.getIcon());
                Object ob = selectedItem.getClientProperty(DATAKEY);
                defaultItem.putClientProperty(DATAKEY, ob);
                
                Object oldObject=this.selectedData;
                selectedData = ob;
                this.firePropertyChange("selectedData",oldObject,selectedData);
            }
        }
    }
    /**
     * ִ�а�ť�¼�����
     * 
     * @param e
     */
    protected void runAction(ActionEvent e) {
        if (selectedData == null && popMenu.getComponentCount() == 1)//�����˵�ֻ��һ��,��ôĬ��ѡȡ�������
        {
            Object oldObject=this.selectedData;
            selectedData = ((JMenuItem) popMenu.getComponent(0))
                    .getClientProperty(DATAKEY);//ȡ��һ��
            this.firePropertyChange("selectedData",oldObject,selectedData);
        }
        for (int i = 0; i < actions.size(); i++) {
            SplitBtnListener action = (SplitBtnListener) actions.elementAt(i);
            action.action(e, selectedData);
        }
    }
    /**
     * �����߰�ť�¼�����
     * 
     * @param action
     */
    public void addAction(SplitBtnListener action) {
        actions.add(action);
    }
    /**
     * ��ӵ����˵���
     * 
     * @param item
     * @throws UnifyException
     */
    public synchronized void addDataItem(String label, Icon icon, Object data)
            throws UnifyException {
        if (data == null)
            throw new IllegalArgumentException("argument:data is null!");
        if (checkExist(data)) {
            throw new UnifyException(PublicResource
                    .getString("sqlEditorView.combobutton.datarepeat"));
        }
        JMenuItem item = new JMenuItem(label, icon);
        item.putClientProperty(DATAKEY, data);
        item.addActionListener(menuListener);
        popMenu.add(item);

        addDefaultItem();
    }
    /**
     * �����ݶ�����ɾ��˵���
     * 
     * @param ob --�Ѿ����������˵������ݶ���
     */
    public synchronized void deleteDataItem(Object ob) {
        for (int i = 0; i < popMenu.getComponentCount(); i++) {
            Component com = popMenu.getComponent(i);
            if (com instanceof JMenuItem && com != defaultItem) { //���ָ��ߺ�ȱʡ�˵�����
                JMenuItem tmpItem = (JMenuItem) com;
                if (tmpItem.getClientProperty(DATAKEY) == ob) {
                    tmpItem.removeActionListener(menuListener);
                    popMenu.remove(tmpItem);
                    if (selectedData == ob)
                        selectedData = null;

                    Component firstComponent = popMenu.getComponentCount() > 0 ? popMenu
                            .getComponent(0)
                            : null;
                    if (firstComponent != null) {
                        if (firstComponent == defaultItem) { //���ȱʡ�˵����Ѿ����

                            if (ob == defaultItem.getClientProperty(DATAKEY)) //�����ȱʡ�˵���һ�£������¸���ȱʡ�˵�����Ϣ
                            {
                                updateDefaultItemData((JMenuItem) popMenu
                                        .getComponent(2)); //����Ϊ��һ���˵�����Ϣ(��ȥȱʡ�˵��ͷָ���)
                            }
                            if (popMenu.getComponentCount() == 3) //ֻʣ��һ����Ч�˵�
                            {
                                popMenu.remove(defaultItem); //ȥ��ȱʡ�˵�
                                popMenu.remove(separator); //ȥ��ָ���
                                
                                Object oldObject=this.selectedData;
                                selectedData = ((JMenuItem) popMenu
                                        .getComponent(0))
                                        .getClientProperty(DATAKEY);//����ΪΨһ�˵����ֵ
                                this.firePropertyChange("selectedData",oldObject,selectedData);
                            }
                        }
                    } else //û�в˵���
                    {
                        selectedData = null; //������ݵ�����
                    }
                    return;
                }

            }
        }
    }
    /**
     * ���ص�ǰ��ϰ�ťĬ��ѡ�������
     * 
     * @return
     */
    public Object getSelectData() {
        if (selectedData == null && popMenu.getComponentCount() == 1)//�����˵�ֻ��һ��,��ôĬ��ѡȡ�������
        {
            Object oldObject=this.selectedData;
            selectedData = ((JMenuItem) popMenu.getComponent(0))
                    .getClientProperty(DATAKEY);//ȡ��һ��
            this.firePropertyChange("selectedData",oldObject,selectedData);
        }
        return selectedData;
    }
    /**
     * У���Ӧ����ݶ����Ƿ��Ѿ�����
     * 
     * @param data
     * @return
     */
    public boolean checkExist(Object data) {
        if (getItemByData(data) != null)
            return true;
        else
            return false;
    }
    /**
     * ͨ����ݶ��������Ҳ˵���
     * 
     * @param data
     * @return
     */
    private JMenuItem getItemByData(Object data) {
        for (int i = 0; i < popMenu.getComponentCount(); i++) {
            Component com = popMenu.getComponent(i);
            if (com instanceof JMenuItem && com != defaultItem) { //���ָ��ߺ�ȱʡ�˵�����
                JMenuItem tmpItem = (JMenuItem) com;
                if (data == tmpItem.getClientProperty(DATAKEY)) //�ҵ���Ӧ�˵���
                    return tmpItem;
            }
        }
        return null;
    }
    /**
     * �ı�ȱʡ�˵��ͨ������ݶ���data���������ҵ���Ӧ�Ĳ˵��Ȼ�����ȱʡ�˵������ʾ�Լ���ݶ���
     * 
     * @param data
     *            --��ݶ��󣬱����ڲ˵�����
     * @param isAdd
     *            --��û����ʾȱʡ�˵�������£��Ƿ����ȱʡ�˵�  true:�ڲ����ڵ���������ӣ�false�������ڵ�����²����
     * @throws UnifyException
     *             --����Ҳ�����Ӧ��data�����׳����쳣
     */
    public void changeDefaultItem(Object data, boolean isAdd)
            throws UnifyException {
        JMenuItem item = getItemByData(data);
        if (item == null) {
            throw new UnifyException("data don't exist!");
        }

        /**
         * �ж���ȱʡ�˵��Ƿ��Ѿ���ӣ����û�У�������õĲ���isAdd�����ж��Ƿ����
         */
        JMenuItem tmpItem = (JMenuItem) popMenu.getComponent(0);
        if (tmpItem != defaultItem) //û�����ȱʡ�˵���
        {
            if (isAdd) {
                popMenu.insert(defaultItem, 0);
                popMenu.insert(separator, 1);
            } else
                return;
        }

        /**
         * ����ݶ���data��Ӧ�Ĳ˵������Ϊȱʡ�˵�
         */
        updateDefaultItemData(item);
    }
    /**
     * ��ȡ��Ӧ�����Ĳ˵�ͼ��
     * 
     * @param data
     * @return
     */
    public Icon getItemIconByData(Object data) {
        JMenuItem item = this.getItemByData(data);
        if (item == null)
            return null;
        else
            return item.getIcon();
    }

    /**
     * ��ȡ��Ӧ�����ı�ǩ
     * 
     * @param data
     * @return
     */
    public String getLabelByData(Object data) {
        JMenuItem item = this.getItemByData(data);
        if (item == null)
            return null;
        else
            return item.getText();
    }
    /**
     * ������ݲ˵���
     * 
     * @param data
     * @param newLabel
     * @param newIcon
     */
    public void updateItemByData(Object data, String newLabel, Icon newIcon) {
        JMenuItem item = this.getItemByData(data);
        if (item == null)
            return;
        String label = StringUtil.trim(newLabel);
        if (!label.equals("")) {
            item.setText(label);
            if (data == defaultItem.getClientProperty(DATAKEY))
                defaultItem.setText(label);
        }

        if (newIcon != null) {
            item.setIcon(newIcon);
            if (data == defaultItem.getClientProperty(DATAKEY))
                defaultItem.setIcon(newIcon);
        }
    }
    /**
     * ��Ӳ˵����,���˵����������,����˵�������Ĭ�ϲ˵���
     *  
     */
    private void addDefaultItem() {
        if (popMenu.getComponentCount() > 1) //���˵������1
        {
            JMenuItem tmpItem = (JMenuItem) popMenu.getComponent(0);
            if (tmpItem != defaultItem) //û�����ȱʡ�˵���
            {
                popMenu.insert(defaultItem, 0);
                popMenu.insert(separator, 1);
                updateDefaultItemData(tmpItem);
            }
        }
    }
    /**
     * ȥ��ȱʡ�˵���
     *  
     */
    private void removeDefaultItem() {
        popMenu.remove(0); //ȥ��ȱʡ�˵�
        popMenu.remove(1); //ȥ��ָ���
    }
    /**
     * 
     * @author liu_xlin �˵�����¼�����
     */
    private class MenuItemClickListener implements ActionListener {
        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() != defaultItem) //��ͨ�˵���
            {
                JMenuItem item = (JMenuItem) e.getSource();
                updateDefaultItemData(item);//����ȱʡ�˵���

            }
            runAction(e); //ͬʱִ����ӵ��¼�����
        }

    }
}
