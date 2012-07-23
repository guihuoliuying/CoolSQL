package com.cattsoft.coolsql.pub.component.selectabletree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;

import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.cattsoft.coolsql.view.bookmarkview.model.DefaultTreeNode;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;

/**
 * Implementation of a tree node that can be selected.
 * It can be used as node of <code>TreeChooserControl</code>.
 * 
 * @author kenny liu
 */
public class SelectableTreeNode extends DefaultTreeNode {

	private static final long serialVersionUID = 1L;

	/**
	 * select only this node, not the children
	 */
	public final static int SINGLE_SELECTION = 0;

	/**
	 * select this node and children 
	 */
	public final static int DIG_IN_SELECTION = 1;

	protected int state;
	private int selectionMode;
	protected boolean orderable;
	
	protected int filterTypes[]; //node type that should be filtered
	
	/**
	 * Creates a tree node
	 * @param userObject 		object to be selected
	 * @param allowsChildren	allow having children or not
	 * @param isOrderable		by ordering, is the node included.
	 * @param state				initial state
	 */
	public SelectableTreeNode(Object userObject, boolean allowsChildren, boolean orderable, int state) {
		super(userObject, allowsChildren);
		this.state = state;
		this.orderable = orderable;
		setSelectionMode(DIG_IN_SELECTION);
	}

	/**
	 * @see #SelectableTreeNode(Object, String, Icon, Icon, boolean, int)
	 */
	public SelectableTreeNode(Object userObject,
			boolean allowsChildren, boolean selected) {
		this(userObject, allowsChildren,false,
				selected ? TristateCheckBox.SELECTED
						: TristateCheckBox.NOT_SELECTED);
	}
	/**
	 * @see #SelectableTreeNode(Object, String, Icon, Icon, boolean, int)
	 */
	public SelectableTreeNode(Object userObject, boolean allowsChildren) {
		this(userObject, allowsChildren, false);
	}

	/**
	 * @see #SelectableTreeNode(Object, String, Icon, Icon, boolean, int)
	 */
	public SelectableTreeNode(Object userObject) {
		this(userObject, true, false);
	}

	/**
	 * @see #SelectableTreeNode(Object, String, Icon, Icon, boolean, int)
	 */
	public SelectableTreeNode() {
		this(new Object());
	}

	/**
	 * Sets the selection mode
	 * 
	 * @param mode
	 * 		SINGLE_SELECTION or DIG_IN_SELECTION
	 */
	public void setSelectionMode(int mode) {
		selectionMode = mode;
	}

	/**
	 * Gets the selection mode
	 * 
	 * @return SINGLE_SELECTION or DIG_IN_SELECTION
	 * @see #setSelectionMode(int)
	 */
	public int getSelectionMode() {
		return selectionMode;
	}

	/**
	 * Gets the state of tree node
	 * 
	 * @return the state
	 * @see #setSelected(boolean)
	 * @see #getSelected()
	 */
	public int getState() {
		return state;
	}

	/**
	 * Gets the state of the node
	 * 
	 * @return the state
	 */
	public boolean getSelected() {
		return state == TristateCheckBox.SELECTED;
	}

	/**
	 * Sets the state of node
	 * 
	 * @param b new state
	 */
	public void setSelected(boolean b) {
		setState(b ? TristateCheckBox.SELECTED : TristateCheckBox.NOT_SELECTED);
	}

	/**
	 * Sets the property of orderable
	 * a node is orderable if it can be ordered in ordering tree control
	 * 
	 * @param b
	 */
	public void setOrderable(boolean b) {
		this.orderable = b;
	}

	/**
	 * @see #setOrderable(boolean)
	 */
	public boolean getOrderable() {
		return this.orderable;
	}

	/**
	 * refreshs the states of THIS NODE AND ITS CHILDREN to keep consistent 
	 */
	public void refresh() {
		if (children != null) {
			Iterator<?> it = children.iterator();
			while (it.hasNext()) {
				SelectableTreeNode child = (SelectableTreeNode) it.next();
				child.refresh();
			}
			refreshState();
		}
	}
	public void setFilterTypes(int type[])
	{
		this.filterTypes=type;
	}
	public int[] getFilterTypes()
	{
		return filterTypes;
	}
	/**
	 * To validate whether specified node type is valid or not
	 * @return true if valid,otherwise false.
	 */
	protected boolean isValidType(int type)
	{
		boolean baseCheck=BookmarkTreeUtil.getInstance().isSelectableNode(type);
		if(!baseCheck)
			return false;
		
		if(filterTypes==null)
			return true;
		for(int i=0;i<filterTypes.length;i++)
		{
			if(type==filterTypes[i])
				return false;
		}
		return true;
	}
	@Override
	protected DefaultTreeNode createTreeNode(Identifier id) {
    	return new SelectableTreeNode(id);
    }
	@Override
    public void addChildren(Identifier[] ids) {
		if(ids==null)
    		return;
        Arrays.sort(ids, new SortIdentifer());
        for (int i = 0; i < ids.length; i++) {
        	if(!isValidType(ids[i].getType()))
        		continue;
            SelectableTreeNode treeNode=generateTreeNode(ids[i]);
            add(treeNode);
        }
    }
	@Override
	public boolean isLeaf()
	{
		return super.isLeaf()||!getAllowsChildren();
	}
	@Override
    public void addChild(Identifier id)
    {
		if(id==null)
			return;
		if(!isValidType(id.getType()))
			return;
        int count=this.getChildCount();
        if(count<1)
        {
            Identifier tmpId=(Identifier)getUserObject();
            tmpId.setHasChildren(true);
            
            SelectableTreeNode treeNode=generateTreeNode(id);
            insert(treeNode,0);
            return;
        }
        for(int i=0;i<count;i++)
        {
            DefaultTreeNode node=(DefaultTreeNode)this.getChildAt(i);
            Identifier itf=(Identifier)node.getUserObject();
            if(id.getContent().compareTo(itf.getContent())<=0)
            {
            	SelectableTreeNode treeNode=generateTreeNode(id);
                insert(treeNode,i);
                return;
            }
        }
        insert(createTreeNode(id), count);
    }
	@Override
    public void addChild(Identifier id,int index)
    {
		if(id==null)
			return;
		if(!isValidType(id.getType()))
			return;
        int count=this.getChildCount();
        if(index>=count){
        	SelectableTreeNode treeNode=generateTreeNode(id);
            insert(treeNode,count);
        }
        if(count<1)
        {
            Identifier tmpId=(Identifier)getUserObject();
            tmpId.setHasChildren(true);
            
            SelectableTreeNode treeNode=generateTreeNode(id);
            insert(treeNode,0);
            return;
        }
        for(int i=index;i<count;i++)
        {
            DefaultTreeNode node=(DefaultTreeNode)this.getChildAt(i);
            Identifier itf=(Identifier)node.getUserObject();
            if(id.getContent().compareTo(itf.getContent())<=0)
            {
            	SelectableTreeNode treeNode=generateTreeNode(id);
                insert(treeNode,i);
                return;
            }
        }
        insert(createTreeNode(id), count);
    }
	@Override
	public boolean expand()
	{
		if(!isExpanded())
			removeAllChildren();
		return super.expand();
	}
	protected SelectableTreeNode generateTreeNode(Identifier id)
	{
		SelectableTreeNode treeNode = (SelectableTreeNode) createTreeNode(id);
    	if(id.getType() == BookMarkPubInfo.NODE_TABLE)
        	treeNode.setAllowsChildren(false);
    	
    	return treeNode;
	}
	@Override
	public boolean equals(Object ob)
	{
		boolean parentResult = super.equals(ob);
		if(!parentResult)
			return false;
		if (!(ob instanceof SelectableTreeNode)) {
			return false;
		}
		SelectableTreeNode tn = (SelectableTreeNode)ob;
		return state == tn.state && selectionMode == tn.selectionMode && orderable == tn.orderable;
	}

	/**
	 * Get the orderable children of this node
	 * 
	 * @return list of orderable nodes
	 */
	public List<SelectableTreeNode> getOrderableChildren() {
		List<SelectableTreeNode> result = new ArrayList<SelectableTreeNode>();
		if (this.getOrderable()) {
			result.add(this);
		} else if (children != null) {
			Iterator<?> it = children.iterator();
			while (it.hasNext()) {
				SelectableTreeNode child = (SelectableTreeNode) it.next();
				result.addAll(child.getOrderableChildren());
			}
		} else {
			SelectableTreeNode parent = (SelectableTreeNode) this.getParent();
			while (parent != null) {
				if (parent.getOrderable()) {
					result.add(parent);
					break;
				}
				parent = (SelectableTreeNode) parent.getParent();
			}
		}

		return result;
	}

	private void setState(int state) {
		this.state = state;

		if (selectionMode == DIG_IN_SELECTION) {
			setChildrenState(this, state);
		}
		setParentsState(this);
	}

	protected void setParentsState(SelectableTreeNode node) {
		SelectableTreeNode parent = (SelectableTreeNode) node.getParent();
		if (parent != null) {
			parent.refreshState();
			setParentsState(parent);
		}
	}

	protected void setChildrenState(SelectableTreeNode father, int state) {
		if (father.children == null)
			return;

		Iterator<?> it = father.children.iterator();
		while (it.hasNext()) {
			SelectableTreeNode node = (SelectableTreeNode) it.next();
			node.state = state;
			setChildrenState(node, state);
		}
	}

	/**
	 * To refresh ONLY the state of this node according to direct children
	 */
	protected void refreshState() {
		if (children != null) {
			Iterator<?> it = children.iterator();
			int count = children.size();
			int selected = 0;
			while (it.hasNext()) {
				SelectableTreeNode child = (SelectableTreeNode) it.next();
				if (child.state == TristateCheckBox.HALF_SELECTED) {
					this.state = TristateCheckBox.HALF_SELECTED;
					return;
				}
				if (child.getSelected())
					selected++;
			}
			if (selected == 0)
				this.state = TristateCheckBox.NOT_SELECTED;
			else if (selected < count)
				this.state = TristateCheckBox.HALF_SELECTED;
			else
				this.state = TristateCheckBox.SELECTED;
		}
	}
}
