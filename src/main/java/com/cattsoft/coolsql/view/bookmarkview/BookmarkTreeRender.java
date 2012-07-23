/*
 * �������� 2006-7-1
 *
 */
package com.cattsoft.coolsql.view.bookmarkview;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.pub.component.DisplayPanel;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;


/**
 * @author liu_xlin ��ǩ���ڵ�ͼ�����Ⱦ��
 */
public class BookmarkTreeRender extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 3510073639903461022L;
	private int cRow=-1;
	private JTree tree;
	private boolean isSelected;
	public static Color highLight=DisplayPanel.getThemeColor().darker().darker();
	
	private Color defaultBookmarkColor;
	private Color defaultBookmarkColor2;
	private Font defaultBookmarkFont;
	private Font oldFont;
	public BookmarkTreeRender()
	{
		
		defaultBookmarkColor=Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_BOOKMARK_DEFAULT_HIGHLIGHTCOLOR, Color.BLUE);
		defaultBookmarkColor2 = new Color(233, 252, 225);
		oldFont = getFont();
		defaultBookmarkFont = new Font(oldFont.getFamily(), Font.BOLD, oldFont.getSize());
		Setting.getInstance().addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent evt) {
				defaultBookmarkColor=Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_BOOKMARK_DEFAULT_HIGHLIGHTCOLOR, Color.BLUE);
			}
			
		}
		, PropertyConstant.PROPERTY_VIEW_BOOKMARK_DEFAULT_HIGHLIGHTCOLOR);
	}
	public void paint(Graphics g)
	{
		super.paint(g);
        if(tree instanceof BookmarkTree)
        {
        	if(isSelected)
        		return;
        	BookmarkTree bt=(BookmarkTree)tree;
        	if(bt.isDisplayNodeBorder()&&cRow==bt.nodeRow)
        	{
				int imageOffset = getLabelStart();

				g.setColor(highLight);
				if (getComponentOrientation().isLeftToRight()) {
					g.drawRect(imageOffset, 0,
							getWidth() - imageOffset-1, getHeight()-1);
				} else {
					g.drawRect( 0, 0, getWidth()
							- imageOffset-1, getHeight()-1);
				}
			}
        }
	}
    private int getLabelStart() {
    	Icon currentI = getIcon();
    	if(currentI != null && getText() != null) {
    	    return currentI.getIconWidth() + Math.max(0, getIconTextGap() - 1);
    	}
    	return 0;
    }
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
          
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object userOb=node.getUserObject();
        if(userOb instanceof Bookmark)
        {
        	setIcon(BookMarkPubInfo.getBookmarkIcon(((Bookmark)userOb).isConnected()));
        	setText(((Bookmark)userOb).getDisplayLabel());
        	if(BookmarkManage.getInstance().getDefaultBookmark()==userOb)
        	{
        		if (sel) 
        			setForeground(defaultBookmarkColor2);
        		else
        			setForeground(defaultBookmarkColor);
        		setFont(defaultBookmarkFont);
        	} else {
        		setFont(oldFont);
        	}
        }else
        {
        	setFont(oldFont);
        	Identifier id=(Identifier)userOb;
				setIcon(BookMarkPubInfo.getIconList()[id.getType()]);
				setText(id.getDisplayLabel());
        }
        cRow=row;
        this.tree=tree;
        isSelected=sel;
		return this;
	}
//	private static class TreeNodeBorder extends AbstractBorder {
//		private static final long serialVersionUID = -2742346622423778706L;
//		private int thickness=1;
//		private Color borderColor=null;
//		public TreeNodeBorder(int thickness,Color borderColor)
//		{
//			this.thickness=thickness;
//			this.borderColor=borderColor;
//		}
//		/**
//		 * draw border for component:c
//		 */
//		@Override
//		public void paintBorder(Component c, Graphics g, int x, int y, int width,
//				int height) {
//			g.setColor(borderColor);
//			g.drawLine(x+1, y, width - thickness, 0);
//			g.drawLine(x+1, y + height - thickness, x + width - thickness, y + height - thickness);
//			// g.setColor(type==RAISE?Color.gray:Color.white);
//			g.drawLine(x+1, y, x+1, y + height - thickness);
//			g.drawLine(x + width - thickness, y, x + width - thickness, y + height - thickness);
//		}
//		/**
//		 * Returns the insets of the border.
//		 * 
//		 * @param c
//		 *            the component for which this border insets value applies
//		 */
//		public Insets getBorderInsets(Component c) {
//			return new Insets(0, thickness,thickness, thickness);
//		}
//
//		/**
//		 * Reinitialize the insets parameter with this Border's current Insets.
//		 * 
//		 * @param c
//		 *            the component for which this border insets value applies
//		 * @param insets
//		 *            the object to be reinitialized
//		 */
//		public Insets getBorderInsets(Component c, Insets insets) {
//			insets.left =0;
//			 insets.top = insets.right = insets.bottom = thickness;
//			return insets;
//		}
//	}
}
