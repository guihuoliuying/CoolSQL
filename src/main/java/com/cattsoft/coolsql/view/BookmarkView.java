/*
 * �������� 2006-5-31
 *
 */
package com.cattsoft.coolsql.view;

import java.util.Iterator;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import com.cattsoft.coolsql.action.bookmarkmenu.AddSqlToEditorAction;
import com.cattsoft.coolsql.action.bookmarkmenu.ConnectAction;
import com.cattsoft.coolsql.action.bookmarkmenu.SearchAction;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkEvent;
import com.cattsoft.coolsql.bookmarkBean.BookmarkListener;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.bookmarkBean.DefaultBookmarkChangeEvent;
import com.cattsoft.coolsql.bookmarkBean.DefaultBookmarkChangeListener;
import com.cattsoft.coolsql.pub.component.BaseMenuManage;
import com.cattsoft.coolsql.pub.component.DoubleClickListener;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.menu.action.NewBookmarkMenuAction;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkMenuManage;
import com.cattsoft.coolsql.view.bookmarkview.BookmarkTree;
import com.cattsoft.coolsql.view.bookmarkview.BookmarkTreeRender;
import com.cattsoft.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.cattsoft.coolsql.view.bookmarkview.actionOfIconButton.CollapseNodesAction;
import com.cattsoft.coolsql.view.bookmarkview.model.DefaultTreeNode;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.bookmarkview.model.RootNode;
import com.cattsoft.coolsql.view.mouseEventProcess.PopupAction;
import com.cattsoft.coolsql.view.sqleditor.action.NextDefaultBookmarkAction;
import com.cattsoft.coolsql.view.sqleditor.action.PreviousDefaultBookmarkAction;

/**
 * @author liu_xlin ��ݿ�������Ϣ��չʾ���
 */
public class BookmarkView extends View implements BookmarkListener {
	private static final long serialVersionUID = -8019702055986761450L;

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(BookmarkView.class);
	
	private JTree connectTree = null;

    private DefaultTreeNode root = null;

    private BaseMenuManage menuManage = null;

    public BookmarkView() {
        super();
        root = new DefaultTreeNode(new RootNode(PublicResource.getString("bookmark.rootName"), null));
        root.setAllowsChildren(true);
        init();
    }

    public BookmarkView(DefaultTreeNode info) {
        super();
        if (info == null) {
            root = new DefaultTreeNode(new RootNode(PublicResource.getString("bookmark.rootName"), null));
            root.setAllowsChildren(true);
        } else {
            root = info;
        }
        init();
    }

    public BookmarkView(DefaultTreeNode connectNode[]) {
        super();
        root = new DefaultTreeNode(new RootNode(PublicResource.getString("bookmark.rootName"), null, true));
        root.setAllowsChildren(true);
        if (connectNode != null) {
            for (int i = 0; i < connectNode.length; i++) {
                root.add(connectNode[i]);
            }
        }
        init();
    }

    private void init() {
        connectTree = new BookmarkTree(root);
        connectTree.setCellRenderer(new BookmarkTreeRender());
        connectTree.setDragEnabled(false);
        connectTree.setShowsRootHandles(true);

        /**
         *��˫����ǩ��ʱ��ִ�еĶ���
         */
        connectTree.addMouseListener(new DoubleClickListener(new AddSqlToEditorAction(this)));
        connectTree.addMouseListener(new DoubleClickListener(new ConnectAction(this)));
        connectTree.addMouseListener(new PopupAction(this));
        DefaultTreeModel model = (DefaultTreeModel) connectTree.getModel();
        model.addTreeModelListener(new NodeStrutListener()); //�����ģ�ͼ���
        DefaultTreeSelectionModel selectModel = (DefaultTreeSelectionModel) connectTree
                .getSelectionModel();
        selectModel
                .setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION); //����ֻ����ѡ����
        loadBookmarksInfo();
        menuManage = new BookMarkMenuManage(this);
        this.setContent(new JScrollPane(connectTree));

        addIconButtons();
        
        BookmarkManage.getInstance().addDefaultBookmarkListener(new DefaultBookmarkChangeListener()
        {

			public void defaultChanged(DefaultBookmarkChangeEvent e) {
				connectTree.repaint();
			}
        	
        }
        );
    }
    /**
     * ���ͼ�갴ť
     *
     */
    protected void addIconButtons()
    {
//        this.addIconButton(PublicResource
//                .getIcon("bookmarkView.popup.new.icon"),
//                getAction("newbookmark"), PublicResource
//                        .getString("bookmarkView.iconbutton.newbookmark.tooltip")); //�½���ǩ
    	
    	addIconButton(Setting.getInstance().getShortcutManager().
    			getActionByClass(NewBookmarkMenuAction.class).getToolbarButton());
    	addIconButton(Setting.getInstance().getShortcutManager().
    			getActionByClass(PreviousDefaultBookmarkAction.class).getToolbarButton());
    	addIconButton(Setting.getInstance().getShortcutManager().
    			getActionByClass(NextDefaultBookmarkAction.class).getToolbarButton());
    	
        this.addIconButton(PublicResource
                .getIcon("bookmarkView.iconbutton.collapse.icon"),
                new CollapseNodesAction(this), PublicResource
                        .getString("bookmarkView.iconbutton.collapse.tooltip")); //��Ӻϲ���ť
        addIconButton(Setting.getInstance().getShortcutManager().
    			getActionByClass(SearchAction.class).getToolbarButton());
    }
    /**
     * ������ǩ������
     * 
     * @param bookMarkManage
     */
    protected void loadBookmarksInfo() {
    	BookmarkManage bookMarkManage = BookmarkManage.getInstance();
        bookMarkManage.addBookmarkListener(this);
        Set<String> set = bookMarkManage.getAliases();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            BookmarkTreeUtil.getInstance().addBookMarkNode((Bookmark) bookMarkManage.get(name));
        }
    }

    public BaseMenuManage getBookmarkMenuManage()
    {
    	return menuManage;
    }
    /**
     * @return ���� connectTree��
     */
    public JTree getConnectTree() {
        return connectTree;
    }
    /**
     * @return ���� root��
     */
    public DefaultTreeNode getRoot() {
        return root;
    }

    /**
     * @param root
     *            Ҫ���õ� root��
     */
    public void setRoot(DefaultTreeNode root) {
        this.root = root;
    }

    public String getName() {
        return stringMgr.getString("view.bookmark.title");
    }

    /*
     * ���� Javadoc��
     * 
     * @see src.view.Display#dispayInfo()
     */
    public void dispayInfo() {

    }

    /*
     * ���� Javadoc��
     * 
     * @see src.view.Display#popupMenu()
     */
    public void popupMenu(int x, int y) {
        menuManage.getPopMenu().show(connectTree, x, y);
    }

    /*
     * ���� Javadoc��
     * 
     * @see src.bookmark.BookMarkListener#bookMarkAdded(src.bookmark.BookMarkEvent)
     */
    public void bookmarkAdded(BookmarkEvent e) {
        BookmarkTreeUtil.getInstance().addBookMarkNode(e.getBookmark());

    }

    /*
     * ���� Javadoc��
     * 
     * @see src.bookmark.BookMarkListener#bookMarkDeleted(src.bookmark.BookMarkEvent)
     */
    public void bookmarkDeleted(BookmarkEvent e) {
        BookmarkTreeUtil.getInstance().removeBookMarkNode(e.getBookmark());
    }

    /*
     * ���� Javadoc��
     * 
     * @see src.bookmark.BookMarkListener#bookMarkUpdated(src.bookmark.BookMarkEvent)
     */
    public void bookMarkUpdated(BookmarkEvent e) {

    }
    /**
     * 
     * @author liu_xlin �ڵ�ṹ����仯ʱ�ļ�����
     */
    protected class NodeStrutListener implements TreeModelListener {

        /*
         * ���� Javadoc��
         * 
         * @see javax.swing.event.TreeModelListener#treeNodesChanged(javax.swing.event.TreeModelEvent)
         */
        public void treeNodesChanged(TreeModelEvent e) {

        }

        /*
         * ���� Javadoc��
         * 
         * @see javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event.TreeModelEvent)
         */
        public void treeNodesInserted(TreeModelEvent e) {

        }

        /*
         * ���� Javadoc��
         * 
         * @see javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event.TreeModelEvent)
         */
        public void treeNodesRemoved(TreeModelEvent e) {

        }

        /**
         * ���ڵ�ṹ����仯ʱ���÷������нڵ��Ƿ����ӽڵ��״̬����
         * 
         * @see javax.swing.event.TreeModelListener#treeStructureChanged(javax.swing.event.TreeModelEvent)
         */
        public void treeStructureChanged(TreeModelEvent e) {
            TreePath path = e.getTreePath();
            DefaultTreeNode node = (DefaultTreeNode) path.getLastPathComponent();
            Object ob = node.getUserObject();
            if (ob instanceof Identifier) {
                Identifier id = (Identifier) ob;
                if (node.getChildCount() > 0) {
                    if (!id.isHasChildren())
                        id.setHasChildren(true);
                } else {
                    if (id.isHasChildren())
                        id.setHasChildren(false);
                }
            }
        }

    }
    /* 1���½���ǩ:newbookmark
     * @see com.coolsql.view.View#createActions()
     */
    @Override
    protected void createActions() {
       //�½���ǩ
//       actionsMap.put("newbookmark",new NewBookMarkAction(this));
        
    }

	/* (non-Javadoc)
	 * @see com.coolsql.view.View#doAfterMainFrame()
	 */
	@Override
	public void doAfterMainFrame() {
		
	}
}
