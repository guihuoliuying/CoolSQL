/*
 * �������� 2006-6-6
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JSplitPane;

import com.cattsoft.coolsql.main.frame.MainFrame;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;


/**
 * @author liu_xlin
 * 
 * �Զ��幤����� ������Ϊ����ʾ�ÿ���ʹ��16*16��ͼƬ
 */
public class DisplayPanel extends BasePanel {

	public static final String PROPERTY_HIDDEN="hidden";
	private static final long serialVersionUID = 1L;
	/**
     * ����������ԣ���Ϊ�ڸ����г�ʼ��������������ʱ��Ҫ��ֵ
     */
    private IconButton resizeBtn;
    private Icon toMax;      //��󻯵İ�ť
    private Icon toNormal;   //�ָ��İ�ť
    
	protected void extraInit() {
		this.getTopPane().addMouseListener(new ResizeMouse());
		toMax=PublicResource.getIcon("view.iconbutton.resize.tomax.icon");
		toNormal=PublicResource.getIcon("view.iconbutton.resize.tonormal.icon");
		addResizeButton();
		
	}
    private void addResizeButton()
    {
        resizeBtn = new IconButton(null);
        resizeBtn.addActionListener(new ActionListener()
                {

                    public void actionPerformed(ActionEvent e) {
                        resizePanel();
                    }
            
                }
        );

        topRight.add(resizeBtn);
        topRight.validate();
        super.addTopPanelListener(new ResizeListener());
    }
	public void sizeToMax() {
		Container p = this.getParent();
		Container tmp = this;//��¼splitpane��ֱ�������
		while (!(p instanceof MainFrame)) {
			if (p != null && p instanceof JSplitPane) {

				JSplitPane split = (JSplitPane) p;
				int type = GUIUtil.isMaxState(split);

				if (type != -1) {
					if (type == JSplitPane.HORIZONTAL_SPLIT) //����Ǻ���ֲ�
					{
						int location = (int) split.getSize().getWidth();
						if (tmp == split.getLeftComponent()) {
							split.setDividerLocation(location);
						} else if (tmp == split.getRightComponent()) {
							split.setDividerLocation(0);
						}
					} else //����ֲ�
					{
						int location = (int) split.getSize().getHeight();
						if (tmp == split.getTopComponent()) {
							split.setDividerLocation(location);
						} else if (tmp == split.getBottomComponent()) {
							split.setDividerLocation(0);
						}
					}
					split.putClientProperty("isModify",new Boolean(true));
				}
			}
			tmp = p;
			p = p.getParent();
		}
	}

	public void sizeToNormal() {
	    JSplitPane first=getSplitContainer();
		Container p = this.getParent();
//		Container tmp = this;//��¼splitpane��ֱ�������
		while (!(p instanceof MainFrame)) {
			if (p != null && p instanceof JSplitPane) {

				JSplitPane split = (JSplitPane) p;
				int type=GUIUtil.isMaxState(split);
				if(type==-1)
				{
				   Boolean isModify=(Boolean)split.getClientProperty("isModify");
				   if(first==split||(isModify!=null&&isModify.booleanValue()))
				   {
				     split.setDividerLocation(split.getLastDividerLocation());
				     isModify=new Boolean(false);
				     split.putClientProperty("isModify",isModify); 
				   }
				}
			}
//			tmp = p;
			p = p.getParent();
		}
	}
	/**
	 * ���ظ���ͼ
	 *
	 */
	public void hidePanel(boolean isFire)
	{
		JSplitPane split=getSplitContainer();
		if(split!=null)
		{
			boolean oldValue=isVisible();
			setVisible(false);
			if(isFire)
			{
				firePropertyChange(PROPERTY_HIDDEN,oldValue , false);
			}
		}
	}
    /**
     * У����������splitpane�Ƿ������
     * @param con
     * @return  �������󣬷�����splitpane�����򷵻�null
     */
	public JSplitPane isMax(Container con) {
		Container p;
		for (p = con.getParent(); p != null && !(p instanceof JSplitPane); p = p
				.getParent())
			;
		JSplitPane split = (JSplitPane) p;
		if(GUIUtil.isMaxSplitToParent(split))
			return split;
		else 
			return null;
	}
	public JSplitPane getSplitContainer()
	{
		return GUIUtil.getSplitContainer(this);
	}
	/**
	 * ��������
	 *
	 */
    private void resizePanel()
    {
		JSplitPane pane=isMax(this);
		if (pane==null)
			sizeToMax();
		else
		{
			if(!pane.isEnabled())
				return;
			sizeToNormal();
		}
    }
	private class ResizeMouse extends MouseAdapter {
		public ResizeMouse() {

		}

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
			    resizePanel();
			}
		}
	}
	/**
	 * ������巢�����仯ʱ�����µ���ť��״̬
	 *
	 */
	public void updateResizeState()
	{
        JSplitPane pane=isMax(this);
		if (pane==null)  //��״̬
		{
			resizeBtn.setIcon(toMax);
			resizeBtn.setToolTipText(PublicResource.getString("view.iconbutton.resize.tomax.tooltip"));
		}
		else      //�Ѿ����
		{
			resizeBtn.setIcon(toNormal);
			resizeBtn.setToolTipText(PublicResource.getString("view.iconbutton.resize.tonormal.tooltip"));
		}
	}
	/**
	 * 
	 * @author liu_xlin
	 *����ͼ�������ĵ�����м����Ա���������ȷ�ĵ���״̬
	 */
	private class ResizeListener implements ComponentListener
	{

        /* ���� Javadoc��
         * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
         */
        public void componentResized(ComponentEvent e) {
            updateResizeState();
            
        }

        /* ���� Javadoc��
         * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
         */
        public void componentMoved(ComponentEvent e) {
        }

        /* ���� Javadoc��
         * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
         */
        public void componentShown(ComponentEvent e) {
            updateResizeState();
        }

        /* ���� Javadoc��
         * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
         */
        public void componentHidden(ComponentEvent e) {
            
        }
	    
	}
}
