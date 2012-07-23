package com.cattsoft.coolsql.pub.component.selectabletree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.TreeCellRenderer;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.model.DefaultTreeNode;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;


/**
 * Render the node of TreeChooserControl with a 3-state checkbox, an icon and a
 * label
 * 
 * @author kenny liu
 */
public class SelectableTreeNodeRenderer extends JPanel 
										implements TreeCellRenderer
{
	private static final long serialVersionUID = 1L;
	protected TristateCheckBox check;
	protected TreeNodeLabel label;

	/**
	 * @see #SelectableTreeNodeRenderer(boolean)
	 */
	public SelectableTreeNodeRenderer() {
		this(false);
	}
	
	/**
	 * Creates the rendering
	 * 
	 * @param showOrdering
	 */
	public SelectableTreeNodeRenderer(boolean showOrdering) {
		setLayout(new FlowLayout());
		add(check = new TristateCheckBox());
		add(label = new TreeNodeLabel());
		check.setEnabled(!showOrdering);
		check.setBackground(UIManager.getColor("Tree.textBackground")); 
		label.setForeground(UIManager.getColor("Tree.textForeground")); 
	}

	/**
	 * Sets the value of the current tree cell to <code>value</code>. If
	 * <code>selected</code> is true, the cell will be drawn as if selected.
	 * If <code>expanded</code> is true the node is currently expanded and if
	 * <code>leaf</code> is true the node represets a leaf and if
	 * <code>hasFocus</code> is true the node currently has focus.
	 * <code>tree</code> is the <code>JTree</code> the receiver is being
	 * configured for. Returns the <code>Component</code> that the renderer
	 * uses to draw the value.
	 * 
	 * @return the <code>Component</code> that the renderer uses to draw the
	 *         value
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean isSelected, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {
        setEnabled(tree.isEnabled());

        //*********
        
        Identifier userOb=(Identifier)((DefaultTreeNode)value).getUserObject();
        Icon icon = null;
        if(userOb instanceof Bookmark)
        {
        	icon=BookMarkPubInfo.getBookmarkIcon(((Bookmark)userOb).isConnected());
        }else
        {
        	icon=BookMarkPubInfo.getIconList()[userOb.getType()];
        }
        
        if (value instanceof SelectableTreeNode) {
        	SelectableTreeNode treeNode = (SelectableTreeNode) value;
            check.setState(treeNode.getState());
            check.setEnabled(!treeNode.getOrderable());
        }
        
        label.setIcon(icon);
        label.setFont(tree.getFont());
        label.setText(userOb.getDisplayLabel());
        label.setSelected(isSelected);
        label.setFocus(hasFocus);
        return this;
    }

	/*
	 * @see java.awt.Component#getPreferredSize()
	 */
	public Dimension getPreferredSize() {
		Dimension d_check = check.getPreferredSize();
		Dimension d_label = label.getPreferredSize();
		return new Dimension(d_check.width + d_label.width + 2,
				(d_check.height < d_label.height ? d_label.height
						: d_check.height));
	}

	/*
	 * @see java.awt.Component#doLayout()
	 */
	public void doLayout() {
		Dimension d_check = check.getPreferredSize();
		Dimension d_label = label.getPreferredSize();
		int y_check = 0;
		int y_label = 0;
		if (d_check.height < d_label.height) {
			y_check = (d_label.height - d_check.height) / 2;
		} else {
			y_label = (d_check.height - d_label.height) / 2;
		}

		check.setBounds(0, y_check, d_check.width, d_check.height);
		label.setBounds(d_check.width + 2, y_label, d_label.width,
				d_label.height);
	}
	/**
	 * Return the width of checkbox displayed in the render panel.
	 */
	public int getCheckBoxWith() {
		return check.getWidth();
	}
	
	/*
	 * @see java.awt.Component#setBackground(java.awt.Color)
	 */
	public void setBackground(Color color) {
		if (color instanceof ColorUIResource)
			color = null;
		super.setBackground(color);
	}

	protected class TreeNodeLabel extends JLabel {

		private static final long serialVersionUID = 1L;
		boolean isSelected;
		boolean hasFocus;
		
		public TreeNodeLabel() {
		}

		public void setBackground(Color color) {
			if (color instanceof ColorUIResource)
				color = null;
			super.setBackground(color);
		}

		public void paint(Graphics g) {
			String str = getText();
			if (str != null) {
				if (0 < str.length()) {
					if (isSelected)
						g.setColor(UIManager
								.getColor("Tree.selectionBackground")); 
					else
						g.setColor(UIManager.getColor("Tree.textBackground"));

					Dimension d = getPreferredSize();
					int imageOffset = 0;
					Icon currentI = getIcon();
					if (currentI != null) {
						imageOffset = currentI.getIconWidth()
								+ Math.max(0, getIconTextGap() - 1);
					}
					g.fillRect(imageOffset, 0, d.width - 1 - imageOffset,
							d.height);
					if (hasFocus) {
						g.setColor(UIManager
								.getColor("Tree.selectionBorderColor")); //$NON-NLS-1$
						g.drawRect(imageOffset, 0, d.width - 1 - imageOffset,
								d.height - 1);
					}
				}
			}
			super.paint(g);
		}

		public Dimension getPreferredSize() {
			Dimension retDimension = super.getPreferredSize();
			if (retDimension != null) {
				retDimension = new Dimension(retDimension.width + 3,
						retDimension.height + 5);
			}
			return retDimension;
		}

		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}

		public void setFocus(boolean hasFocus) {
			this.hasFocus = hasFocus;
		}
	}
}
