/*
 * �������� 2006-9-11
 */
package com.cattsoft.coolsql.gui.property;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @author liu_xlin
 *����������Ⱦ��
 */
public class PropertyTreeRender extends DefaultTreeCellRenderer {
	public PropertyTreeRender()
	{
	}
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
          
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object userOb=node.getUserObject();
        if(userOb instanceof NodeKey)
        {
            NodeKey key=(NodeKey)userOb;
        	setIcon(key.getIcon());
        	setText(key.getName());
        }
		return this;
	}
}
