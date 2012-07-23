/*
 * �������� 2006-6-8
 *
 */
package com.cattsoft.coolsql.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JComponent;

import com.cattsoft.coolsql.pub.component.BaseMenuManage;
import com.cattsoft.coolsql.pub.component.DisplayPanel;
import com.cattsoft.coolsql.pub.display.PopMenuMouseListener;

/**
 * @author liu_xlin ��ͼ�ඨ�壬������ͼ�Ļ���Ϣ
 */
public abstract class View extends DisplayPanel implements Display {
	public static final String LASTLOCATION="lastlocation"; //�ϴ���splitpane�е�λ��
	
    public static Color THEME_COLOR = new Color(160, 207, 184);

    //�������Ҽ���˵���������
    protected PopMenuListener popMenuListener = null;

    /**
     * �Զ����¼��������ļ���
     */
    protected Map actionsMap;

    public View() {
        super();
        popMenuListener = new PopMenuListener();
        getTopPane().addMouseListener(popMenuListener);
        actionsMap=new HashMap();
        createActions();
    }
    /**
     * ����ȱʡ��ʱ�䴦�����
     *
     */
    protected abstract void createActions();
    /**
     * ��ͼ���
     * 
     * @return ���� name��
     */
    public abstract String getName();

    /**
     * this method will be invoke after main frame has been initialized.
     *
     */
    public abstract void doAfterMainFrame() throws Exception;
    /**
     * show view if this view is hidden
     *@param isFire --notice all propertychangelistener that listen to property: hidden if true
     */
    public void showPanel(boolean isFire)
    {
    	boolean oldValue=isVisible();
    	setVisible(true);
    	if(isFire)
    		firePropertyChange(PROPERTY_HIDDEN,oldValue , true);
    }
    public boolean isViewVisible()
    {
    	return isVisible();
    }
	/**
	 * override parent'method,notice all listener that listen to property:"hidden".
	 */
//    @Override
//	public void setVisible(boolean isVisible)
//	{
//    	boolean oldValue=isVisible();
//		super.setVisible(isVisible);
//		firePropertyChange(PROPERTY_HIDDEN, oldValue, isVisible);
//	}
    /**
     * ��ȡkey���Ӧ���¼��������
     * @param key  --��ֵ
     * @return  --Ԥ��ʵ����¼��������,����Ҳ�����Ӧ��ʱ�䴦����,����null
     */
    public Action getAction(Object key)
    {
        return (Action)actionsMap.get(key);
    }
    /**
     * 
     * @author liu_xlin ����ͼ������������������¼�����Ҫ�����Ҽ���˵�����ʾ
     */
    protected class PopMenuListener extends PopMenuMouseListener {
        
        /**
         * �Ҽ��ڱ����ϵ��������Ҫ����ͼ��Ϊ�������
         */
        public void mouseReleased(MouseEvent e) {
            if(!isPopupTrigger(e))
                return;
                
            JComponent source = (JComponent) e.getSource();
            
            BaseMenuManage viewMenu=ViewManage.getInstance().getViewMenuManage();
            viewMenu.setComponent(View.this);
            viewMenu.getPopMenu().show(source,e.getX(),e.getY());
        }
    }
}
