/*
 * �������� 2006-9-8
 */
package com.cattsoft.coolsql.view.bookmarkview;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.cattsoft.coolsql.pub.component.BaseTree;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.bookmarkview.model.DefaultTreeNode;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;

/**
 * @author liu_xlin ��ǩ������д����ҪΪ��������չ�����۵��ܹ���¼չ��·��
 */
public class BookmarkTree extends BaseTree {

	private static final long serialVersionUID = 1662291272269068909L;

	/**
	 * determine whether or not row of bookmark tree displays border.
	 */
	private boolean isDisplayNodeBorder = Setting.getInstance().getBoolProperty(
			PropertyConstant.PROPERTY_VIEW_BOOKMARK_ISSHOWNODEBORDER, true);
	
	/**
	 * used to save the row of treenode  which mouse is on
	 */
	protected int nodeRow=-2; 
    public BookmarkTree(DefaultTreeNode node) {
        super(node);
        addMouseMotionListener(new MouseMoveListener());
        addMouseListener(new MouseExitListener());
        
        ToolTipManager.sharedInstance().registerComponent(this);  //ע����Ϣ��ʾ
    }

    public String getToolTipText(MouseEvent e)
    {
        /**
         * ��ȡ���λ�ö�Ӧ���ؼ��ڵ�
         */
        Point p = e.getPoint();
        int selRow = getRowForLocation(p.x, p.y);
        if(selRow<0)
            return null;
        TreePath path = getPathForRow(selRow);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        Object userOb=node.getUserObject();
        if(!(userOb instanceof Identifier))
           return null;
        
        Identifier id=(Identifier)userOb;
        if(id.getType()!=BookMarkPubInfo.NODE_RECENTSQL)  //���ýڵ㲻Ϊsql�ڵ㣬������ʾ
            return null;
        
        if(id.getContent().equals(id.getDisplayLabel()))  //���sql�ڵ���Ϣ��ȫչʾ��Ҳ������ʾ
        	return null;
        
        return id.getContent();
    }
	/**
	 * @return the isDisplayNodeBorder
	 */
	public boolean isDisplayNodeBorder() {
		return this.isDisplayNodeBorder;
	}

	/**
	 * @param isDisplayNodeBorder the isDisplayNodeBorder to set
	 */
	public void setDisplayNodeBorder(boolean isDisplayNodeBorder) {
		this.isDisplayNodeBorder = isDisplayNodeBorder;
		repaint();
	}
    /**
     * ��дJTree��getToolTipLocation()����
     * ��tipλ�����������
     */
    public Point getToolTipLocation(MouseEvent e)
    {
        Point p = e.getPoint();
        int selRow = getRowForLocation(p.x, p.y);
        if(selRow<0)
            return null;
        TreePath path = getPathForRow(selRow);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        Object userOb=node.getUserObject();
        if(!(userOb instanceof Identifier))
           return null;
        Identifier id=(Identifier)userOb;
        if(id.getType()!=BookMarkPubInfo.NODE_RECENTSQL)  //���ýڵ㲻Ϊsql�ڵ㣬������ʾ
            return null;
       Rectangle rect=getPathBounds(path);
       return new Point((int)rect.getLocation().getX()+16,(int)rect.getLocation().getY()); //��Ϊͼ���ԭ��ʹλ������ƫ��16������
    }

    private class MouseMoveListener extends MouseMotionAdapter
    {
    	@Override
    	public void mouseMoved(MouseEvent e)
    	{
    		processStat(e);
    	}
    	@Override
    	public void mouseDragged(MouseEvent e)
    	{
    		processStat(e);
    	}
    	private void processStat(MouseEvent e)
    	{
    		int current=getRowForLocation(e.getX(), e.getY());
    		if(current!=nodeRow)
    		{
    			nodeRow=current;
    			repaint();
    		}
    	}
    }
    private class MouseExitListener extends MouseAdapter
    {
    	@Override
    	public void mouseExited(MouseEvent e)
    	{
    		nodeRow=-1;
			repaint();
    	}
    }
}
