/*
 * �������� 2006-11-9
 */
package com.cattsoft.coolsql.view.bookmarkview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cattsoft.coolsql.bookmarkBean.BookmarkEvent;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkListener;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.bookmarkview.model.DefaultTreeNode;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.bookmarkview.model.SQLGroupNode;
import com.cattsoft.coolsql.view.bookmarkview.model.SQLNode;

/**
 * @author liu_xlin �������ִ�е�sql
 */
public class RecentSQLManage {

    private static RecentSQLManage manager = null;

    /**
     * ��������sql����
     */
    private int maxSQL;

    /**
     * key��Bookmark value=LinkedList
     */
    private Map<Bookmark,LinkedList> sqlsData = null;

    /**
     * ���Ա仯�����������
     */
    private PropertyChangeSupport pcs = null;

    /**
     * �Ƚ���,��ִ�е�sql�����������
     */
    private RecentSQLComparator comparator=null;
    private RecentSQLManage() {
        maxSQL = Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_HISTORYSIZE, 200);;
        sqlsData = Collections.synchronizedMap(new HashMap<Bookmark,LinkedList>());
        pcs = new PropertyChangeSupport(this);
        this.addPropertyChangeListener(new SQLAddListener());
        BookmarkManage.getInstance().addBookmarkListener(new BookmarkManageListener());
        
        comparator=new RecentSQLComparator();
        
        Setting.getInstance().addPropertyChangeListener(new PropertyChangeListener()
        {
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_HISTORYSIZE))
				{
					try
					{
						maxSQL=Integer.parseInt(evt.getNewValue().toString());
					}catch(Exception e)
					{
						//Do nothing.
						return;
					}
					GUIUtil.processOnSwingEventThread(new Runnable(){
						public void run()
						{
							Iterator<Bookmark> bookmarks=sqlsData.keySet().iterator();
							while(bookmarks.hasNext())
							{
								Bookmark b=bookmarks.next();
								LinkedList list=sqlsData.get(b);
								int additionSize=list.size()-maxSQL;
								if(additionSize<=0)
									continue;
								for(int i=0;i<additionSize;i++)
								{
									pcs.firePropertyChange("removesql", null, list.removeLast());
								}
							}
						}
					});
				}
			}
        	
        }
        , PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_HISTORYSIZE);
    }

    public static synchronized RecentSQLManage getInstance() {
        if (manager == null)
            manager = new RecentSQLManage();
        return manager;
    }
    /**
     * ��ȡ��Ӧ��ǩ�µ��������ִ�е�sql
     * 
     * @param bookmark
     * @return --List
     */
    public List getRecentSQLList(Bookmark bookmark) {
        return (LinkedList) sqlsData.get(bookmark);
    }
    public List getRecentSQLList(String bookmark) {
        Bookmark tmpBm=BookmarkManage.getInstance().get(bookmark);
        if(tmpBm==null)
            return null;
        return getRecentSQLList(tmpBm);
    }
    public int getMaxSQL() {
        return maxSQL;
    }

    /**
     * ���sql�������
     * 
     * @param sql
     * @param bookmark
     */
    public void addSQL(RecentSQL sql, String aliasName) {
        Bookmark bookmark = BookmarkManage.getInstance().get(aliasName);
        addSQL(sql, bookmark);
    }

    public void addSQL(RecentSQL sql, Bookmark bookmark) {
        if (bookmark == null)
            return;
        LinkedList sqls = (LinkedList)getRecentSQLList(bookmark);
        if (sqls == null) {
            sqls = new LinkedList();
            sqlsData.put(bookmark, sqls);
        }
        synchronized (sqls) {
            addSQLToList((LinkedList) sqls, sql);
        }
    }
    /**
     * ���������sql����
     * 
     * @param sqls
     * @param sql
     */
    private void addSQLToList(LinkedList sqls, RecentSQL sql) {
        /**
         * �����sql���󳬹��������ɾ����ʱ��������λ�õ���ݣ�
         */
        if (sqls.size() >= maxSQL) {
            Object ob = sqls.removeLast();
            pcs.firePropertyChange("removesql", null, ob);
        }
        sqls.addFirst(sql);
        pcs.firePropertyChange("addsql", null, sql);
    }

    /**
     * ���Ա仯���ɶ���Ĵ��?��
     * 
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
    /**
     * ��ȡ��ǩ��Ӧ��sql�����б?Ȼ��Ը��б��������
     * @param bookmark
     */
    public void adjustOrder(Bookmark bookmark)
    {
        List tmp=this.getRecentSQLList(bookmark);
        if(tmp==null)
            return;
        sortRecentSQLs(tmp);
    }
    /**
     * ��ִ�е�sql����ʼʱ���������
     * @param list
     */
    public void sortRecentSQLs(List list)
    {
        Collections.sort(list,comparator);
    }
    /**
     * ������sql�����������
     *
     */
    public void adjustAll()
    {
        Set keys=sqlsData.keySet();
        Iterator it=keys.iterator();
        while(it.hasNext())
        {
            adjustOrder((Bookmark)it.next());
        }
    }
    /**
     * 
     * @author liu_xlin
     *��sql������������µ�sql�������ɾ��sql����ʱ��������ǩ���ṹ
     */
    private class SQLAddListener implements PropertyChangeListener
    {
        private SQLAddListener(){}

        /* ���� Javadoc��
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent evt) {
            String name=evt.getPropertyName();
            if(name.equals("addsql"))   //��������sql����
            {
                RecentSQL data=(RecentSQL)evt.getNewValue();
                
                //��ȡ��Ӧ��ǩ��sql��Ͻڵ�
                DefaultTreeNode groupNode=BookmarkTreeUtil.getInstance().getSQLGroupNode(data.bookmark());
                if(groupNode==null)
                    return;
                if(!groupNode.isExpanded())  //���sql��Ͻڵ�û��չ����ֱ�ӷ���
                    return;
                
                SQLNode sqlData=new SQLNode(data.getSql(),data.bookmark(),data);
                DefaultTreeNode child=new DefaultTreeNode(sqlData);
                BookmarkTreeUtil.getInstance().addNode(child,groupNode,0); 
                
                Identifier id=(Identifier)groupNode.getUserObject();
                if(!id.isHasChildren())
                    id.setHasChildren(true);
                
                /**
                 * ���sql��ͽڵ��ӽڵ�����ڶ������ֵ��������ִ�е�sql�ڵ�ɾ��
                 */
                int childCount=groupNode.getChildCount();
                if(childCount>SQLGroupNode.MAXCHILDCOUNT)
                {
                	BookmarkTreeUtil.getInstance().removeNode(childCount-1,groupNode);
                }
                
                BookmarkTreeUtil.getInstance().repaintTree();
            }else if(name.equals("removesql"))
            {
                RecentSQL data=(RecentSQL)evt.getNewValue();
                SQLNode sqlData=new SQLNode(data.getSql(),data.bookmark(),data);
                
                //��ȡ��Ӧ��ǩ��sql��Ͻڵ�
                DefaultTreeNode groupNode=BookmarkTreeUtil.getInstance().getSQLGroupNode(data.bookmark());
                if(groupNode==null)
                    return;
                if(!groupNode.isExpanded())  //���sql��Ͻڵ�û��չ����ֱ�ӷ���
                    return;
                
                BookmarkTreeUtil.getInstance().removeNode(sqlData,groupNode); 
                
                Identifier id=(Identifier)groupNode.getUserObject();
                if(id.isHasChildren()&&groupNode.getChildCount()<1)
                    id.setHasChildren(false);
                BookmarkTreeUtil.getInstance().repaintTree();
            }
        }
        
    }
    /**
     * 
     * @author liu_xlin
     * ����ǩ�������ļ����ࡣ����ǩ��ɾ���ʱ��ɾ�����ǩ���Ӧ��sql����
     */
    private class BookmarkManageListener implements BookmarkListener
    {

        /* ���� Javadoc��
         * @see com.coolsql.bookmarkBean.BookMarkListener#bookMarkAdded(com.coolsql.bookmarkBean.BookMarkEvent)
         */
        public void bookmarkAdded(BookmarkEvent e) {                       
        }

        /* ���� Javadoc��
         * @see com.coolsql.bookmarkBean.BookMarkListener#bookMarkDeleted(com.coolsql.bookmarkBean.BookMarkEvent)
         */
        public void bookmarkDeleted(BookmarkEvent e) {
           Bookmark bookmark=e.getBookmark();
           sqlsData.remove(bookmark);
        }

        /* ���� Javadoc��
         * @see com.coolsql.bookmarkBean.BookMarkListener#bookMarkUpdated(com.coolsql.bookmarkBean.BookMarkEvent)
         */
        public void bookMarkUpdated(BookmarkEvent e) {            
        }
        
    }
    /**
     * 
     * @author liu_xlin
     *��RecentSQL����������������������ִ��ʱ���,��ִ�е�����ǰ��
     */
    protected class RecentSQLComparator implements Comparator
    {

        /* ���� Javadoc��
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object o1, Object o2) {
            if (o1 == null && o2 == null)
                return 0;
            if(o1==null||o1 instanceof RecentSQL)
                return 1;
            if(o2==null||o2 instanceof RecentSQL)
                return -1;
            
            RecentSQL data1=(RecentSQL)o1;
            RecentSQL data2=(RecentSQL)o2;
            return (int)(data2.getTime()-data1.getTime());
        }
        
    }
}
