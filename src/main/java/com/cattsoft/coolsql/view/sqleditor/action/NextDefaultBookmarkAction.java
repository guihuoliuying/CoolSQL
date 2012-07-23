/**
 * 
 */
package com.cattsoft.coolsql.view.sqleditor.action;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.action.framework.AutoCsAction;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;

/**
 * Change default bookmark rotatively .
 * @author ��Т��(kenny liu)
 *
 * 2008-4-2 create
 */
public class NextDefaultBookmarkAction extends AutoCsAction {

	private static final long serialVersionUID = 1L;

	@Override
	public void executeAction(ActionEvent e) {
		
		BookmarkManage.getInstance().nextBookmarkAsDefault();
//		if(BookmarkManage.getInstance().getBookmarkCount()<2)
//			return;
//		Bookmark bookmark=BookmarkManage.getInstance().getDefaultBookmark();
//		List<Bookmark> list=BookmarkManage.getInstance().getBookMarks();
//		Bookmark firstBookmark=null;
//		
//		for(int i=0;i<list.size();i++)
//		{
//			Bookmark tmp=list.get(i);
//			if(firstBookmark==null)
//			{
//				firstBookmark=tmp;
//			}
//			if(tmp==bookmark)
//			{
//				if(i<list.size()-1)
//					ViewManage.getInstance().getSqlEditor().setDefaultBookmark(list.get(i+1));
//				else
//					ViewManage.getInstance().getSqlEditor().setDefaultBookmark((firstBookmark));
//			}
//		}

	}
}
