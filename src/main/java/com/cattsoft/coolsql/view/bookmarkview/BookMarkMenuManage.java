/*
 * �������� 2006-7-2
 *
 */
package com.cattsoft.coolsql.view.bookmarkview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;

import com.cattsoft.coolsql.action.bookmarkmenu.AddEntityDataAction;
import com.cattsoft.coolsql.action.bookmarkmenu.AddSqlToEditorAction;
import com.cattsoft.coolsql.action.bookmarkmenu.CearTableAction;
import com.cattsoft.coolsql.action.bookmarkmenu.ConnectAction;
import com.cattsoft.coolsql.action.bookmarkmenu.CopyAction;
import com.cattsoft.coolsql.action.bookmarkmenu.CopyCommand;
import com.cattsoft.coolsql.action.bookmarkmenu.DeleteBookMarkAction;
import com.cattsoft.coolsql.action.bookmarkmenu.DisconnectAction;
import com.cattsoft.coolsql.action.bookmarkmenu.PropertyAction;
import com.cattsoft.coolsql.action.bookmarkmenu.QueryAllDataOfTableAction;
import com.cattsoft.coolsql.action.bookmarkmenu.ReNameAction;
import com.cattsoft.coolsql.action.bookmarkmenu.RefreshAction;
import com.cattsoft.coolsql.action.bookmarkmenu.SearchAction;
import com.cattsoft.coolsql.action.bookmarkmenu.SetAsDefaultBookmarkAction;
import com.cattsoft.coolsql.action.bookmarkmenu.ViewCountOfTableAction;
import com.cattsoft.coolsql.action.common.Command;
import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.component.BaseMenuManage;
import com.cattsoft.coolsql.pub.component.BasePopupMenu;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.menu.action.NewBookmarkMenuAction;
import com.cattsoft.coolsql.system.menubuild.IconResource;
import com.cattsoft.coolsql.view.BookmarkView;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;

/**
 * @author liu_xlin
 *  
 */
public class BookMarkMenuManage extends BaseMenuManage {

    private BasePopupMenu bookMarkMenu = null; //popup menu of bookmark node

    private BasePopupMenu pubMenu = null; //popup menu of root,tabletype,column node

    private BasePopupMenu sqlMenu = null;  ////popup menu of sql node

    private BasePopupMenu tableMenu = null;//popup menu of table node

    //************************************************************bookmark
    /**
     * bookmark��ɾ��������
     */
    private JMenuItem deleteBookMark = null;

    private JMenuItem renameBookMark = null;

    private JMenuItem copyBookMark = null;

    private JMenuItem setAsDefault = null;

    /**
     * bookmark�����ӺͶϿ�����
     */
    private JMenuItem connect = null;

    private JMenuItem disconnect = null;

    /**
     * bookmark��ˢ�º�����
     */
    private JMenuItem refresh = null;

    private JMenuItem property = null;

    //*************************************************************pubmenu
    private JMenuItem copyForPub = null;

    private JMenuItem refreshForPub = null;

    private JMenuItem propertyForPub = null;

    //*************************************************************sqlmenu
    private JMenuItem copyForSql = null;

    private JMenuItem openSql = null;

    //    private JMenuItem execute=null;
    private JMenuItem propertyForSql = null;

    //*************************************************************tablemenu
    private JMenuItem copyForTable = null;

    private JMenuItem copyQualifiedName=null;
    private JMenuItem viewAllRows = null;

    private JMenuItem refreshForTable = null;

    private JMenuItem propertyForTable = null;
    
    /**
     * View the count of data in selected table.
     */
    private JMenuItem viewCountOfTable;
    
    private JMenuItem addData=null;  //��������
    
    private JMenuItem clearAll=null;//����������

    /**
     * �����ĸ��ƺ����Բ鿴�¼�����
     */
    private CsAction newBookmarkAction = null;

    private PublicAction copyAction = null;

    private PublicAction propertyAction = null;

    private CsAction searchAction = null;

    private PublicAction refreshAction = null;

    private DeleteBookMarkAction deleteAction=null;
    /**
     * @param view
     */
    public BookMarkMenuManage(View view) {
        super(view);
        newBookmarkAction = Setting.getInstance().getShortcutManager().getActionByClass(NewBookmarkMenuAction.class);
        
        copyAction = new CopyAction((BookmarkView) view);
        propertyAction = new PropertyAction((BookmarkView) view);

        //�����¼�����
        searchAction =Setting.getInstance().getShortcutManager().getActionByClass(SearchAction.class);
//        bindKey("control F", searchAction, true);

        //ˢ���¼�����
        refreshAction = new RefreshAction((BookmarkView) view);
        bindKey(((BookmarkView) view).getConnectTree(), "F5",
                refreshAction, false);

        deleteAction=new DeleteBookMarkAction((View)getComponent());
        bindKey(((BookmarkView) view).getConnectTree(), "DELETE",
                deleteAction, false);
        
        this.createPubMenu();
    }

    /**
     * ������ͼ�ͱ�ڵ���Ҽ�˵�
     *  
     */
    protected void createTableMenu() {
        tableMenu = new BasePopupMenu();
        tableMenu.add(newBookmarkAction.getMenuItem());
        
        copyForTable = createMenuItem(PublicResource
                .getString("bookmarkView.popup.copy"), PublicResource
                .getIcon("bookmarkView.popup.copy.icon"), copyAction);
        copyForTable.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        tableMenu.add(copyForTable);
        
        ActionListener copyQualifiedListener=new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (((BookmarkView) getComponent())
        				.getConnectTree().getLastSelectedPathComponent());
        		Identifier userOb=(Identifier)(node.getUserObject());
        		if(userOb.getType()==BookMarkPubInfo.NODE_TABLE||BookMarkPubInfo.NODE_VIEW==userOb.getType())
        		{
        			Entity en=(Entity)userOb.getDataObject();
        			Command cp=new CopyCommand(en.getQualifiedName());
        			cp.execute();
        		}
        	}
        };
        copyQualifiedName = createMenuItem(PublicResource
                .getString("bookmarkView.popup.copyqualifiedname"), IconResource.getBlankIcon(), copyQualifiedListener);
        tableMenu.add(copyQualifiedName);

        viewAllRows = createMenuItem(PublicResource
                .getString("bookmarkView.popup.queryrows"), PublicResource
                .getIcon("bookmarkView.popup.queryrows.icon"),
                new QueryAllDataOfTableAction((BookmarkView) this
                        .getComponent()));
        viewAllRows.setMnemonic('A');
        tableMenu.add(viewAllRows);
        
        viewCountOfTable = createMenuItem(PublicResource
                .getString("bookmarkView.popup.viewcountoftable"), IconResource.getBlankIcon(),
                new ViewCountOfTableAction((BookmarkView) this
                        .getComponent()));
        viewCountOfTable.setMnemonic('C');
        tableMenu.add(viewCountOfTable);
        
        addData=createMenuItem(PublicResource
                .getString("bookmarkView.popup.adddata"), IconResource
                .getIcon("system.icon.addrows"), new AddEntityDataAction());
        tableMenu.add(addData);
        
        CearTableAction clearAction=new CearTableAction();
        clearAll=createMenuItem(PublicResource
                .getString("bookmarkView.popup.deleteall"), IconResource.getBlankIcon(), clearAction);
        tableMenu.add(clearAll);
        
        refreshForTable = createMenuItem(PublicResource
                .getString("bookmarkView.popup.refresh"), PublicResource
                .getIcon("bookmarkView.popup.refresh.icon"), refreshAction);
        tableMenu.add(refreshForTable);

        refreshForTable.setAccelerator(KeyStroke.getKeyStroke("F5"));
        
        propertyForTable = createMenuItem(PublicResource
                .getString("bookmarkView.popup.property"), PublicResource
                .getIcon("bookmarkView.popup.property.icon"), propertyAction);
        tableMenu.add(propertyForTable);
    }

    /**
     * ����sql�ڵ�ר�õĲ˵�
     *  
     */
    protected void createSqlMenu() {
        sqlMenu = new BasePopupMenu();

        sqlMenu.add(newBookmarkAction.getMenuItem());
        copyForSql = createMenuItem(PublicResource
                .getString("bookmarkView.popup.copy"), PublicResource
                .getIcon("bookmarkView.popup.copy.icon"), copyAction);
        copyForSql.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        sqlMenu.add(copyForSql);

        /**
         * ��sql�˵���
         */
        openSql = createMenuItem(PublicResource
                .getString("bookmarkView.popup.open.txt"), PublicResource
                .getIcon("bookmarkView.popup.open.icon"),
                new AddSqlToEditorAction((BookmarkView) this.getComponent()));
        sqlMenu.add(openSql);

        sqlMenu.add(searchAction.getMenuItem());
        propertyForSql = createMenuItem(PublicResource
                .getString("bookmarkView.popup.property"), PublicResource
                .getIcon("bookmarkView.popup.property.icon"), propertyAction);
        sqlMenu.add(propertyForSql);
    }

    /**
     * ���������˵�
     *  
     */
    protected void createPubMenu() {
        pubMenu = new BasePopupMenu();

        pubMenu.add(newBookmarkAction.getMenuItem());
        
        copyForPub = createMenuItem(PublicResource
                .getString("bookmarkView.popup.copy"), PublicResource
                .getIcon("bookmarkView.popup.copy.icon"), copyAction);
        copyForPub.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        pubMenu.add(copyForPub);

        pubMenu.add(searchAction.getMenuItem());

        refreshForPub = createMenuItem(PublicResource
                .getString("bookmarkView.popup.refresh"), PublicResource
                .getIcon("bookmarkView.popup.refresh.icon"), refreshAction);
        pubMenu.add(refreshForPub);
        //        this.bindKey(refreshForPub, "F5", refreshAction, false);
        refreshForPub.setAccelerator(KeyStroke.getKeyStroke("F5"));

        propertyForPub = createMenuItem(PublicResource
                .getString("bookmarkView.popup.property"), PublicResource
                .getIcon("bookmarkView.popup.property.icon"), propertyAction);
        pubMenu.add(propertyForPub);

    }

    /**
     * �����Ҽ�˵�(��ǩ)
     *  
     */
    protected void createBookMarkMenu() {
        bookMarkMenu = new BasePopupMenu();

        bookMarkMenu.add(newBookmarkAction.getMenuItem());
        
        deleteBookMark = createMenuItem(PublicResource
                .getString("bookmarkView.popup.delete"), PublicResource
                .getIcon("bookmarkView.popup.delete.icon"),
                deleteAction);
        deleteBookMark.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
        bookMarkMenu.add(deleteBookMark);
        renameBookMark = createMenuItem(PublicResource
                .getString("bookmarkView.popup.rename"), PublicResource
                .getIcon("bookmarkView.popup.rename.icon"), new ReNameAction(
                (View) this.getComponent()));
        bookMarkMenu.add(renameBookMark);

        copyBookMark = createMenuItem(PublicResource
                .getString("bookmarkView.popup.copy"), PublicResource
                .getIcon("bookmarkView.popup.copy.icon"), copyAction);
        copyBookMark.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        bookMarkMenu.add(copyBookMark);
        bookMarkMenu.add(new JSeparator());
        connect = createMenuItem(PublicResource
                .getString("bookmarkView.popup.connect"), PublicResource
                .getIcon("bookmarkView.popup.connect.icon"), new ConnectAction(
                (View) this.getComponent()));
        bookMarkMenu.add(connect);
        disconnect = createMenuItem(PublicResource
                .getString("bookmarkView.popup.disconnect"), PublicResource
                .getIcon("bookmarkView.popup.disconnect.icon"),
                new DisconnectAction((View) this.getComponent()));
        bookMarkMenu.add(disconnect);
        bookMarkMenu.add(new JSeparator());

        setAsDefault = createMenuItem(PublicResource
                .getString("bookmarkView.popup.setasdefault"), PublicResource
                .getIcon("bookmarkView.popup.setasdefault.icon"),
                new SetAsDefaultBookmarkAction((BookmarkView) getComponent()));
        bookMarkMenu.add(setAsDefault);

        refresh = createMenuItem(PublicResource
                .getString("bookmarkView.popup.refresh"), PublicResource
                .getIcon("bookmarkView.popup.refresh.icon"), refreshAction);
        bookMarkMenu.add(refresh);
        //        this.bindKey(refresh, "F5", refreshAction, false);
        refresh.setAccelerator(KeyStroke.getKeyStroke("F5"));

        bookMarkMenu.add(searchAction.getMenuItem());
        property = createMenuItem(PublicResource
                .getString("bookmarkView.popup.property"), PublicResource
                .getIcon("bookmarkView.popup.property.icon"), propertyAction);
        bookMarkMenu.add(property);

    }
    /**
     * Add a menuitem/menu to bookmark popup menu,this method is used by else projects that will extend this system.
     * @param m --menu added to bookmark popup menu.
     */
    public JMenuItem addMenuOnBookmark(JMenuItem m)
    {
    	BasePopupMenu pop=getBookMarkMenu();
    	if(pop==null||m==null)
    		return null;
    	
    	return pop.add(m);
    }
    /**
     * Add separator to bookmark popup menu.
     */
    public void addSeparatorOnBookmark() {
    	BasePopupMenu pop = getBookMarkMenu();
    	pop.addSeparator();
    }
    /**
     * add a menuitem/menu to pub popup menu,this method is used by else projects that will extend this system.
     * @param m --menu added to pub popup menu.
     */
    public JMenuItem addMenuOnPub(JMenuItem m)
    {
    	BasePopupMenu pop=getPubMenu();
    	if(pop==null||m==null)
    		return null;
    	
    	return pop.add(m);
    }
    /**
     * Add separator to pub popup menu.
     */
    public void addSeparatorOnPub() {
    	BasePopupMenu pop = getPubMenu();
    	pop.addSeparator();
    }
    /**
     * add a menuitem/menu to sql popup menu,this method is used by else projects that will extend this system.
     * @param m --menu added to sql popup menu.
     * @return
     */
    public JMenuItem addMenuOnSql(JMenuItem m)
    {
    	BasePopupMenu pop=getSqlMenu();
    	if(pop==null||m==null)
    		return null;
    	
    	return pop.add(m);
    }
    /**
     * Add separator to Sql popup menu.
     */
    public void addSeparatorOnSql() {
    	BasePopupMenu pop = getSqlMenu();
    	pop.addSeparator();
    }
    /**
     * add a menuitem/menu to table popup menu,this method is used by else projects that will extend this system.
     * @param m --menu added to table popup menu.
     */
    public JMenuItem addMenuOnTable(JMenuItem m)
    {
    	BasePopupMenu pop = getTableMenu();
    	if(pop == null || m == null)
    		return null;
    	
    	return pop.add(m);
    }
    /**
     * Add separator to Sql popup menu.
     */
    public void addSeparatorOnTable() {
    	BasePopupMenu pop = getTableMenu();
    	pop.addSeparator();
    }
    /**
     * @return ���� pop��
     */
    protected BasePopupMenu getBookMarkMenu() {
        if (bookMarkMenu == null)
            createBookMarkMenu();
        return bookMarkMenu;
    }

    /**
     * @return ���� pubMenu��
     */
    protected BasePopupMenu getPubMenu() {
        if (pubMenu == null)
            createPubMenu();
        return pubMenu;
    }

    /**
     * @return ���� sqlMenu��
     */
    protected BasePopupMenu getSqlMenu() {
        if (sqlMenu == null)
            createSqlMenu();
        return sqlMenu;
    }

    /**
     * @return ���� sqlMenu��
     */
    protected BasePopupMenu getTableMenu() {
        if (tableMenu == null)
            createTableMenu();
        return tableMenu;
    }

    /*
     * ���� Javadoc��
     * 
     * @see src.pub.display.BaseMenuManage#getPopMenu()
     */
    public BasePopupMenu getPopMenu() {

        return itemCheck();
    }

    /*
     * ���� Javadoc��
     * 
     * @see src.pub.display.BaseMenuManage#itemSet()
     */
    public BasePopupMenu itemCheck() {
        BookmarkView tmpView = (BookmarkView) this.getComponent();
        DefaultMutableTreeNode tmpNode = (DefaultMutableTreeNode) tmpView
                .getConnectTree().getLastSelectedPathComponent();
        int tmpType = tmpNode == null ? -1 : ((Identifier) tmpNode
                .getUserObject()).getType();
        BasePopupMenu pop = null;
        if (BookMarkPubInfo.isBookmarkNode(tmpType)) {
            pop = getBookMarkMenu();
            Bookmark bookmark = (Bookmark) tmpNode.getUserObject();

            if (bookmark.getConnectState() == 0) //�����ǩ�ڵ�״̬��
            {
                if (bookmark.isConnected()) {
                    if (connect.isEnabled()) {
                        connect.setEnabled(false);
                    }
                    if (!disconnect.isEnabled()) {
                        disconnect.setEnabled(true);
                    }
                } else {
                    if (!connect.isEnabled()) {
                        connect.setEnabled(true);
                    }
                    if (disconnect.isEnabled()) {
                        disconnect.setEnabled(false);
                    }
                }

                //����ˢ�²˵��Ƿ����
                if (bookmark.isConnected()) {
                    if (!refresh.isEnabled()) {
                        refresh.setEnabled(true);
                    }
                } else {
                    if (refresh.isEnabled()) {
                        refresh.setEnabled(false);
                    }
                }
            } else { //״̬���� �������������ӣ��������ڶϿ�
                if (connect.isEnabled()) {
                    connect.setEnabled(false);
                }
                if (disconnect.isEnabled()) {
                    disconnect.setEnabled(false);
                }
                if (refresh.isEnabled()) {
                    refresh.setEnabled(false);
                }
            }
        } else if (tmpType == BookMarkPubInfo.NODE_RECENTSQL) { //���ִ�е�sql�б�
            pop = getSqlMenu();
            if (!copyForPub.isEnabled())
                copyForSql.setEnabled(true);

            if (refreshForPub.isEnabled())
                refreshForPub.setEnabled(false);
            //				getSqlMenu();
        } else if (tmpType == BookMarkPubInfo.NODE_SCHEMA //ģʽ�ڵ� ���нڵ� ������ڵ�
                || tmpType == BookMarkPubInfo.NODE_COLUMN
                || tmpType == BookMarkPubInfo.NODE_KEYCOLUMN) {
            pop = getPubMenu();
            if (!copyForPub.isEnabled())
                copyForPub.setEnabled(true);

            if (tmpType == BookMarkPubInfo.NODE_SCHEMA) { //�����ģʽ�ڵ㣬��ˢ��
                if (!refreshForPub.isEnabled())
                    refreshForPub.setEnabled(true);
                if (propertyForPub.isEnabled())
                    propertyForPub.setEnabled(false);
            } else { //������нڵ㣬����ˢ��
                if (refreshForPub.isEnabled())
                    refreshForPub.setEnabled(false);
                if (!propertyForPub.isEnabled())
                    propertyForPub.setEnabled(true);
            }
        } else if (tmpType == BookMarkPubInfo.NODE_VIEW //��ͼ�ڵ� ����ڵ�
                || tmpType == BookMarkPubInfo.NODE_TABLE) {
            pop = getTableMenu();
        } else {
            pop = getPubMenu();
            if (tmpType != -1) {
                if (!copyForPub.isEnabled())
                    copyForPub.setEnabled(true);
                if (tmpType == BookMarkPubInfo.NODE_VIEWS //������Ͻڵ㣨views,tables��,ʹˢ�¿���
                        || tmpType == BookMarkPubInfo.NODE_TABLES) {
                    if (!refreshForPub.isEnabled())
                        refreshForPub.setEnabled(true);
                } else if (tmpType == BookMarkPubInfo.NODE_HEADER) {
                    if (refreshForPub.isEnabled())
                        refreshForPub.setEnabled(false);
                }

            } else //û��ѡ���κνڵ�
            {
                if (copyForPub.isEnabled())
                    copyForPub.setEnabled(false);
                if (refreshForPub.isEnabled())
                    refreshForPub.setEnabled(false);
            }
            if (propertyForPub.isEnabled())
                propertyForPub.setEnabled(false);
        }
        
        menuCheck();
        return pop;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coolsql.pub.display.BaseMenuManage#createPopMenu()
     */
    protected void createPopMenu() {

    }

}
