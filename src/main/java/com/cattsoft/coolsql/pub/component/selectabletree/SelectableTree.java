/**
 * 
 */
package com.cattsoft.coolsql.pub.component.selectabletree;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.cattsoft.coolsql.pub.component.BaseTree;
import com.cattsoft.coolsql.system.lookandfeel.SystemLookAndFeel;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-3-16 create
 */
public class SelectableTree extends BaseTree {

	private static final long serialVersionUID = 1L;
	
	private MouseListener selectionTreeListener;
	public SelectableTree() {
		super();
	}
	public SelectableTree(Object[] value) {
		super(value);
	}
	public SelectableTree(Vector<?> value) {
		super(value);
	}
	public SelectableTree(Hashtable<?,?> value) {
		super(value);
	}
	public SelectableTree(TreeNode root) {
		super(root);
	}
	public SelectableTree(TreeNode root, boolean asksAllowsChildren) {
		super(root,asksAllowsChildren);
	}
	public SelectableTree(TreeModel newModel) {
		super(newModel);
	}
	@Override
	protected void initTree()
	{
		super.initTree();
		if(!SystemLookAndFeel.getInstance().isMetalLookAndFeel())
			setRowHeight(getRowHeight()+3);
		selectionTreeListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				if(e.getButton()!=MouseEvent.BUTTON1)
					return;
				int x = e.getX();
				int y = e.getY();
				int row = getRowForLocation(x, y);
				if(row == -1)
					return;
				TreePath path = getPathForRow(row);
				Rectangle rowRect = getPathBounds(path);
				if (!isInCheckBox(rowRect, e.getPoint())) {
					return;
				}

				if (path != null) {
					SelectableTreeNode node = (SelectableTreeNode) path
							.getLastPathComponent();
					if (node.getOrderable()) {
						return;
					}
					setNodeSelected(node, !node.getSelected());
				}
			}
		};
		getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		putClientProperty("JTree.lineStyle", "Angled");
		setShowsRootHandles(true);
		setCellRenderer(new SelectableTreeNodeRenderer());
		addMouseListener(selectionTreeListener);
	}
	private boolean isInCheckBox(Rectangle rowRect, Point p) {
		TreeCellRenderer render = getCellRenderer();
		if (!(render instanceof SelectableTreeNodeRenderer)) {
			return false;
		}
		SelectableTreeNodeRenderer stRender = (SelectableTreeNodeRenderer) render;
		Insets renderInsets = stRender.getInsets();
		return p.x < (rowRect.x + renderInsets.left + stRender.getCheckBoxWith());
	}
	/**
	 * Select or deselect a node, update selectionTree and orderingTree
	 * 		1. update selectionTree
	 * 
	 * @param node 	
	 * 			the node to be selected or deselected
	 * @param selected
	 */
	protected void setNodeSelected(SelectableTreeNode node, boolean selected) {
		// update selection tree
		node.setSelected(selected);

		DefaultTreeModel model = ((DefaultTreeModel)getModel());
		SelectableTreeNode updated = node;
		while (updated != null) {
			model.nodeChanged(updated);
			updated = (SelectableTreeNode) updated.getParent();
		}
	}
}
