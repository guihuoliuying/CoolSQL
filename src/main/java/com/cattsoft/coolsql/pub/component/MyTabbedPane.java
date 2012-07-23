/*
 * �������� 2006-9-28
 *
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.MenuCheckable;
import com.cattsoft.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin ����tabҳ�����ӹرհ�ť��ͬʱ��ҳͷ�������Ҽ�˵�
 */
public class MyTabbedPane extends BaseTabbedPane {

	private static final long serialVersionUID = 1L;

	/**
     * tabҳ�����ϵ��ʼ��˵�
     */
    private JPopupMenu headPopMenu = null;

    /**
     * tab������ݲ��ֵ��Ҽ�˵�
     */
    private JPopupMenu contentPopMenu = null;

    /**
     * �˵�У�鼯��
     */
    private Vector headCheckSet = null;

    private Vector contentCheckSet = null;

    /**
     * �ر�ҳ���ʱ�������������ִ�д������
     */
    private Vector closeProcess=null;
    
    /**
     * ҳͷ�Ҽ�˵���У��
     */
    private List headPopMenuCheck=null;
    
    private HeadPopMenuListener headPopMenuListener;
    
    private int currentIndexOnMouse;//the index value on which mouse is 

    /**key:index of tab, value:the state of tab indicates whether specified tab is being edited */
    protected Map<Integer,Boolean> stateMap;
    public MyTabbedPane() {
        super();
        init();
    }

    public MyTabbedPane(int tabPlacement) {
        super(tabPlacement);
        init();
    }

    public MyTabbedPane(int tabPlacement, int tabLayoutPolicy) {
        super(tabPlacement, tabLayoutPolicy);
        init();
    }

    private void init() {
        MouseLcationListener tmpListener = new MouseLcationListener();
        this.addMouseListener(tmpListener);
        this.addMouseMotionListener(tmpListener);
        headCheckSet = new Vector();
        contentCheckSet = new Vector();
        closeProcess=new Vector();
        
        headPopMenuListener=new HeadPopMenuListener();
        headPopMenuCheck=new ArrayList();
        
        stateMap=new HashMap<Integer,Boolean>();
    } 

    /**
     * ��ӿɹرյ�ҳ
     * 
     * @param title  --ҳ����
     * @param com   --����ӵ����
     */
    public void addCloseTab(String title, JComponent com) {
        super.addTab(title, new MyTabbedPaneIcon(), com);
    }
    public void setTabState(int index,boolean isUpdating)
    {
    	Boolean oldValue=stateMap.put(index, isUpdating);
    	if(oldValue==null)
    	{
    		if(isUpdating)
    		{
    			repaint();
    		}
    	}else
    	{
    		if(!oldValue.equals(isUpdating))
    		{
    			repaint();
    		}
    	}
    }
    /**
     * 
     * @param index the index of tab component
     * @return return true if tab according to index is being edited, otherwise return false;
     */
    public boolean getTabState(int index)
    {
    	Boolean value=stateMap.get(index);
    	if(value==null)
    		value=false;
    	return value;
    }
    @Override
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
    	super.insertTab(title, icon, component, tip, index);
    	if(icon instanceof MyTabbedPaneIcon)
    	{
    		((MyTabbedPaneIcon)icon).setIndex(index);
    	}
    }
    /**
     * ��ӿɹرյ�ҳ������ָ����ҳ����ʾ��Ϣ
     * @param title  --ҳ����
     * @param com   --����ӵ����
     * @param toolTip   --ҳ����ʾ��Ϣ
     */
    public void addCloseTab(String title,JComponent com,String toolTip) {
        super.addTab(title, new MyTabbedPaneIcon(), com,toolTip);
    }
    /**
     * ɾ��������ҳ�����ͬʱִ��ɾ���Ĺر�
     */
    @Override
    public void removeTabAt(int index) {    
        Component pageComponent=getComponentAt(index);
        super.removeTabAt(index);
        runCloseProcess(pageComponent);
        
        //Remove the state according to index
        stateMap.remove(index);
        
        int count=getTabCount();
        for(int i=0;i<count;i++)
        {
        	Icon in=getIconAt(i);
        	if(in instanceof MyTabbedPaneIcon)
        		((MyTabbedPaneIcon)in).setIndex(i);
        }
    }
    @Override
    public String getTitleAt(int index) {
    	String title=super.getTitleAt(index);
    	Boolean isUpdating=stateMap.get(index);
    	if(isUpdating==null||!isUpdating)
    	{
    		return title;
    	}else
    		return "*"+title;
    }
    /**
     * ���ر�tab�����ĳһҳʱ��ִ��ָ�����߼�����
     * @param pageComponent  --��ɾ������
     */
    private void runCloseProcess(Component pageComponent)
    {
        for(int i=0;i<closeProcess.size();i++)
        {
            TabClose closer=(TabClose)closeProcess.get(i);
            closer.doOnClose(pageComponent);
        }
    }
    protected class MouseLcationListener extends MouseAdapter implements
            MouseMotionListener {
        private MyTabbedPaneIcon icon = null;

        private int pressedIndex = -1;

        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
         */
        public void mousePressed(MouseEvent e) {
            int index = indexAtLocation(e.getX(), e.getY());
            if (SwingUtilities.isLeftMouseButton(e)) { //������������´���
                if (index != -1) {
                    pressedIndex = index;
                    icon = (MyTabbedPaneIcon) getIconAt(index);
                    if (icon.getBounds().contains(e.getPoint())) {
                        icon.setPressImage();
                    }
                }
            }
        }

        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
         */
        public void mouseReleased(MouseEvent e) {

            if (SwingUtilities.isLeftMouseButton(e)) { //������
                if (icon != null) {
                    if (icon.pressed && pressedIndex != -1
                            && icon.getBounds().contains(e.getPoint())) //���ڰ���״̬,ɾ��ѡ��ҳ
                    {
                        remove(pressedIndex);
                        icon.setPressed(false);
                        icon = null;
                        updateUI();
                    }
                    if (icon != null) //���û��ɾ��
                    {
                        icon.setEnterTabImage();
                        icon.setPressed(false);
                        icon = null;
                    }
                }
            } else if (SwingUtilities.isRightMouseButton(e)) { //����Ҽ�

                if (indexAtLocation(e.getX(), e.getY()) != -1) {  //��ҳͷ�ϵ���Ҽ�
                    if (headPopMenu != null) {
                        getHeadPopMenu().show(e.getComponent(), e.getX(),
                                e.getY());
                    }
                }else  //��tab����ڲ�����Ҽ�
                {
                    if (contentPopMenu != null) {
                        getContentPopMenu().show(e.getComponent(), e.getX(),
                                e.getY());
                    }
                }

            }
            if (pressedIndex > -1)
                pressedIndex = -1;

        }

        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
         */
        public void mouseEntered(MouseEvent e) {
            
        }

        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
         */
        public void mouseExited(MouseEvent e) {

            if (icon != null) {
                icon.setPressed(false);
                icon.setEnterTabImage();
            }
        }

        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
         */
        public void mouseDragged(MouseEvent e) {
        }

        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
         */
        public void mouseMoved(MouseEvent e) {
        	currentIndexOnMouse = indexAtLocation(e.getX(), e.getY());
            if (currentIndexOnMouse != -1) {
                icon = (MyTabbedPaneIcon) getIconAt(currentIndexOnMouse);
                if (icon.getBounds().contains(e.getPoint())) {
                    icon.setEnterIconImage();
                } else
                    icon.setEnterTabImage();
            }
        }

    }

    protected class MyTabbedPaneIcon extends ImageIcon {
    	private int index=-1;
        //ͼ�����ʾ��Χ
        private Rectangle rec = null;

        //��갴�µ�״̬
        private boolean pressed = false;

        private Image enterTabImage = null; //����ҳ�Ĺر�ͼ��

        private Image enterIconImage = null; //�������ͼ���ϵ���ʾ

        private Image pressImage = null; //���µ�ͼ��

        private Runnable paintRun=null;
        public MyTabbedPaneIcon()
        {
        	this(-1);
        }
        public MyTabbedPaneIcon(int index) {
            super();
            this.index=index;
            rec = new Rectangle(0, 0, 16, 16);
            enterTabImage = PublicResource.getIcon(
                    "view.tab.delete.entertab.icon").getImage();
            this.setImage(enterTabImage);
            enterIconImage = PublicResource.getIcon(
                    "view.tab.delete.entericon.icon").getImage();
            pressImage = PublicResource.getIcon("view.tab.delete.press.icon")
                    .getImage();
            paintRun=new Runnable()
            {
            	public void run()
            	{
            		repaint();
            	}
            };
        }
        public int getIndex()
        {
        	return index;
        }
        public void setIndex(int index)
        {
        	this.index=index;
        }
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        	if(currentIndexOnMouse!=index&&this.getImage()==enterIconImage)
        		this.setImage(enterTabImage);
        	
            if (pressed)
                super.paintIcon(c, g, ++x, ++y);
            else
                super.paintIcon(c, g, x, y);
            rec.x = x;
            rec.y = y;
            rec.width = this.getIconWidth();
            rec.height = this.getIconHeight();
        }

        public void setEnterTabImage() {
            if (enterTabImage != this.getImage()) {
                this.setImage(enterTabImage);
                repaintIcon();
            }
        }

        public void setEnterIconImage() {
            if (enterIconImage != this.getImage()) {
                this.setImage(enterIconImage);
                repaintIcon();
            }
        }

        public void setPressImage() {
            if (pressImage != this.getImage()) {
                this.setImage(pressImage);
                repaintIcon();
            }
            if (!pressed)
                pressed = true;
        }

        public void setPressed(boolean pressed) {
            this.pressed = pressed;
        }

        public Rectangle getBounds() {
            return rec;
        }
        private void repaintIcon()
        {
        	GUIUtil.processOnSwingEventThread(paintRun);
        }
    }

    /**
     * ���ҳͷ�˵�У�����
     * 
     * @param checkable
     */
    public void addHeadMenuCheck(MenuCheckable checkable) {
        if (checkable != null) {
            headCheckSet.add(checkable);
        }
    }

    /**
     *���ҳɾ��ʱ,��Ҫ�Ĵ����߼�
     * @param close  --�����߼���
     */
    public void addTabCloseProcess(TabClose close)
    {
        closeProcess.add(close);
    }
    /**
     * ɾ��ҳɾ��ʱ�Ĵ����߼�
     * @param close --�����߼���
     */
    public void removeTabCloseProcess(TabClose close)
    {
        closeProcess.remove(close);
    }
    /**
     * ������ݲ˵�У�����
     * 
     * @param checkable
     */
    public void addContentMenuCheck(MenuCheckable checkable) {
        if (checkable != null) {
            contentCheckSet.add(checkable);
        }
    }

    /**
     * �ڵ���ҳͷ�˵�ǰ���в˵���Ŀ�����У��
     *  
     */
    private void headMenuCheck() {
        for (int i = 0; i < headCheckSet.size(); i++) {
            MenuCheckable check = (MenuCheckable) headCheckSet.get(i);
            check.check(this);
        }
    }

    /**
     * �ڵ������ݲ˵�ǰ���в˵���Ŀ�����У��
     *  
     */
    private void contentMenuCheck() {
        for (int i = 0; i < contentCheckSet.size(); i++) {
            MenuCheckable check = (MenuCheckable) contentCheckSet.get(i);
            check.check(this);
        }
    }

    /**
     * ���ҳͷ�Ҽ�˵���
     * 
     * @param label
     * @param icon
     * @param action
     * @return
     */
    public JMenuItem addHeadMenuItem(String label, Icon icon,
            ActionListener action) {
        return addMenuItem(headPopMenu, label, icon, action);
    }

    /**
     * ��������Ҽ�˵���
     * 
     * @param label
     * @param icon
     * @param action
     * @return
     */
    public JMenuItem addContentMenuItem(String label, Icon icon,
            ActionListener action) {
        return addMenuItem(contentPopMenu, label, icon, action);
    }

    /**
     * ��ӵ����˵���
     * 
     * @param label
     * @param icon
     * @param action
     * @return
     */
    private JMenuItem addMenuItem(JPopupMenu popMenu, String label, Icon icon,
            ActionListener action) {
        JMenuItem tmp = new JMenuItem(label, icon);
        tmp.addActionListener(action);
        if (headPopMenu == null)
            headPopMenu = new BasePopupMenu();
        popMenu.add(tmp);
        return tmp;
    }

    public void addMenuSeparator(JPopupMenu popMenu) {
        if (popMenu != null)
            popMenu.addSeparator();
    }

    /**
     * @return ���� popMenu��
     */
    public JPopupMenu getHeadPopMenu() {
        headMenuCheck();
        return headPopMenu;
    }

    /**
     * @return ���� popMenu��
     */
    public JPopupMenu getContentPopMenu() {
        contentMenuCheck();
        return contentPopMenu;
    }

    /**
     * @param popMenu
     *            Ҫ���õ� popMenu��
     */
    public void setHeadPopMenu(JPopupMenu popMenu) {
        if( this.headPopMenu!=null)
            this.headPopMenu.removePopupMenuListener(headPopMenuListener);
        this.headPopMenu = popMenu;
        if( this.headPopMenu!=null)
            this.headPopMenu.addPopupMenuListener(headPopMenuListener);
    }
    /**
     * @param popMenu
     *            Ҫ���õ� popMenu��
     */
    public void setContentPopMenu(JPopupMenu popMenu) {
        this.contentPopMenu = popMenu;
    }
    /**
     * ���ҳͷ�Ҽ�˵����У��
     * @param check  --У�鴦����
     */
    public void addHeadPopMenuCheck(MenuCheckable check)
    {
        if(check!=null)
            headPopMenuCheck.add(check);
    }
    private void runPopMenuCheck()
    {
        for(int i=0;i<headPopMenuCheck.size();i++)
        {
            MenuCheckable check=(MenuCheckable)headPopMenuCheck.get(i);
            check.check(getSelectedComponent());
        }
    }
    /**
     * 
     * @author liu_xlin
     *�ռ��ҳͷ�Ҽ�˵��ڵ���֮ǰ���в˵�������Ե�У��
     */
    private class HeadPopMenuListener implements PopupMenuListener
    {

        /* (non-Javadoc)
         * @see javax.swing.event.PopupMenuListener#popupMenuCanceled(javax.swing.event.PopupMenuEvent)
         */
        public void popupMenuCanceled(PopupMenuEvent e) {            
            
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
            runPopMenuCheck();
        }
        
    }
}
