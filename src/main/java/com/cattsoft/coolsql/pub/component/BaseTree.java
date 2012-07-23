/**
 * 
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Cursor;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.cattsoft.coolsql.view.bookmarkview.model.DefaultTreeNode;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-3-16 create
 */
public class BaseTree extends JTree {

	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public BaseTree() {
		super();
		initTree();
	}

	/**
	 * @param value
	 */
	public BaseTree(Object[] value) {
		super(value);
		initTree();
	}

	/**
	 * @param value
	 */
	public BaseTree(Vector<?> value) {
		super(value);
		initTree();
	}

	/**
	 * @param value
	 */
	public BaseTree(Hashtable<?, ?> value) {
		super(value);
		initTree();
	}

	/**
	 * @param root
	 */
	public BaseTree(TreeNode root) {
		super(root);
		initTree();
	}

	/**
	 * @param newModel
	 */
	public BaseTree(TreeModel newModel) {
		super(newModel);
	}
	protected void initTree()
	{
		addTreeExpansionListener(new IAccessibleJTree());
	}
	/**
	 * @param root
	 * @param asksAllowsChildren
	 */
	public BaseTree(TreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
	}
    public class IAccessibleJTree extends AccessibleJTree {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void treeExpanded(TreeExpansionEvent event) {

            TreePath path = event.getPath();
            if (path == null || path.getLastPathComponent() == null)
                return;
            JTree tree = (JTree) event.getSource();
            DefaultTreeNode node = (DefaultTreeNode) path.getLastPathComponent();
            try {
                tree.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                if (node.expand()) {

                    DefaultTreeModel treeModel = (DefaultTreeModel) tree
                            .getModel();
                    treeModel.nodeStructureChanged(node);
                }
            } finally {
                tree.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }

        public void treeCollapsed(TreeExpansionEvent evt) {

        }
    }

}
