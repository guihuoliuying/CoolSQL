/*
 * �������� 2006-9-8
 */
package com.cattsoft.coolsql.view.bookmarkview.model;

import java.util.Iterator;
import java.util.List;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.cattsoft.coolsql.view.bookmarkview.INodeFilter;
import com.cattsoft.coolsql.view.bookmarkview.RecentSQL;
import com.cattsoft.coolsql.view.bookmarkview.RecentSQLManage;

/**
 * @author liu_xlin
 *���ִ��sql����Ͻڵ�
 */
public class SQLGroupNode extends Identifier {
    public static int MAXCHILDCOUNT=10;
	public SQLGroupNode()
	{
        super();
        init();
	}
	public SQLGroupNode(String content,Bookmark bookmark)
	{
        super(BookMarkPubInfo.NODE_RECENTSQLGROUP,content,bookmark);
        init();
	}  
	public SQLGroupNode(int type,String content,Bookmark bookmark,boolean isHasChild)
	{
        super(BookMarkPubInfo.NODE_RECENTSQLGROUP,content,bookmark,isHasChild);
        init();
	}  
	private void init()
	{
	}
    /* ���� Javadoc��
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#expand(com.coolsql.view.bookmarkview.model.ITreeNode)
     */
    public void expand(DefaultTreeNode parent,INodeFilter filter) {
        Bookmark bookmark=this.getBookmark();
        List sqls=RecentSQLManage.getInstance().getRecentSQLList(bookmark);
        if(sqls==null)
            return;
        Iterator it=sqls.iterator();
        int count=0;
        while(it.hasNext()&&count<MAXCHILDCOUNT)
        {
            count++;
            RecentSQL sql=(RecentSQL)it.next();
            //��ȡ��Ӧ��ǩ��sql��Ͻڵ�
            DefaultTreeNode groupNode=BookmarkTreeUtil.getInstance().getSQLGroupNode(sql.bookmark());
            
            SQLNode sqlData=new SQLNode(sql.getSql(),sql.bookmark(),sql);
            if(filter!=null&&!filter.filter(sqlData))
            	continue;
            
            DefaultTreeNode child=new DefaultTreeNode(sqlData);
            BookmarkTreeUtil.getInstance().addNode(child,groupNode,-1); 
        }
    }
}
