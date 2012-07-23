/*
 * �������� 2006-9-10
 */
package com.cattsoft.coolsql.view.bookmarkview.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.sql.commonoperator.EntityPropertyOperator;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.sql.model.Column;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.cattsoft.coolsql.view.bookmarkview.INodeFilter;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ��ͼ�ڵ�
 */
public class ViewNode extends Identifier {
	private static final long serialVersionUID = 1L;
	//ʵ����ݶ���
    private Entity dataOb = null;

    public ViewNode() {
        super();
        super.setType(BookMarkPubInfo.NODE_VIEW);
    }

    public ViewNode(String content, Bookmark bookmark) {
        super(BookMarkPubInfo.NODE_VIEW, content, bookmark);
    }

    public ViewNode(String content, Bookmark bookmark, boolean isHasChild) {
        super(BookMarkPubInfo.NODE_VIEW, content, bookmark, isHasChild);
    }

    public ViewNode(String content, Bookmark bookmark, Entity dataOb) {
        super(BookMarkPubInfo.NODE_VIEW, content, bookmark);
        this.dataOb = dataOb;
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#expand(com.coolsql.view.bookmarkview.model.ITreeNode)
     */
    public void expand(DefaultTreeNode parent, INodeFilter filter) throws UnifyException, SQLException {
        //��ȡ���е�����Ϣ
        Column[] cols = dataOb.getColumns();
        if (cols == null)
            return;

        //�����ڵ��ʶ
        List<Identifier> list=new ArrayList<Identifier>();
        for (int i = 0; i < cols.length; i++) {
        	Identifier id = new ColumnNode(cols[i].getName(), cols[i].getName()
                    + " : " + cols[i].getTypeName() + "(" + cols[i].getSize()
                    + ")", getBookmark(), cols[i]);
        	if(filter==null||filter.filter(id))
        		list.add(id);
        }

        //��ӽڵ�
        parent.addChildren(list.toArray(new Identifier[list.size()]));
    }

    /**
     * �ڵ��ˢ�´���ʵ�� ���� Javadoc��
     * 
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#refresh(com.coolsql.view.bookmarkview.model.DefaultTreeNode)
     */
    public void refresh(DefaultTreeNode parent, INodeFilter filter)
			throws SQLException, UnifyException {
		if (!parent.isExpanded())
			return;
		boolean changed = false; // �Ƿ��б仯
		Map<String, DefaultTreeNode> temp = new HashMap<String, DefaultTreeNode>();
		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			DefaultTreeNode tmp = (DefaultTreeNode) parent.getChildAt(i);
			temp.put(((Identifier) tmp.getUserObject()).getContent(), tmp);
		}
		dataOb.refresh();
		// ��ȡ���е�����Ϣ
		Column[] cols = dataOb.getColumns();
		for (int i = 0; i < cols.length; i++) {
			DefaultTreeNode node = (DefaultTreeNode) temp.remove(cols[i]
					.getName());
			if (node == null) {
				ColumnNode tmpNode = new ColumnNode(cols[i].getName(), cols[i]
						.getName()
						+ " : "
						+ cols[i].getTypeName()
						+ "("
						+ cols[i].getSize() + ")", this.getBookmark(), cols[i]);
				if (filter != null && !filter.filter(tmpNode))
					continue;

				parent.addChild(tmpNode);
				changed = true;
			} else {
				if (filter != null && !filter.filter(node.getUserObject())) {// The node will be removed
					temp.put(cols[i].getName(), node);
				} else {
					Column column = (Column) (((Identifier) node
							.getUserObject()).getDataObject());
					if (!(cols[i].getTypeName().equals(column.getTypeName()))
							|| cols[i].getSize() != column.getSize()) {
						ColumnNode tmpNode = new ColumnNode(cols[i].getName(),
								cols[i].getName() + " : "
										+ cols[i].getTypeName() + "("
										+ cols[i].getSize() + ")", this
										.getBookmark(), cols[i]);
						if (filter != null && !filter.filter(tmpNode))
							continue;

						parent.addChild(tmpNode);
						changed = true;
					}
				}
			}
		}

		for (Iterator<DefaultTreeNode> it = temp.values().iterator(); it.hasNext();) // ɾ����ڵ�ģʽ
		{
			parent.remove((DefaultTreeNode) it.next());
			changed = true;
		}
		if (changed)
			BookmarkTreeUtil.getInstance().refreshBookmarkTree(parent); // ˢ�½ڵ���ģ��
	}

    /**
     * ��дObjectHolder�ķ���
     */
    public Object getDataObject() {
        return dataOb;
    }

    /**
     * ��д���Է���
     */
    public void property() throws UnifyException, SQLException {
        try {
            Operatable operator = OperatorFactory
                    .getOperator(EntityPropertyOperator.class);
            List<Identifier> list=new ArrayList<Identifier>();
            list.add(this);
            operator.operate(list);
        } catch (ClassNotFoundException e) {
            LogProxy.errorReport(e);
        } catch (InstantiationException e) {
            LogProxy.internalError(e);
        } catch (IllegalAccessException e) {
            LogProxy.internalError(e);
        }
    }
}
