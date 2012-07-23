/*
 * �������� 2006-6-8
 *
 */
package com.cattsoft.coolsql.view;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.cattsoft.coolsql.pub.component.BaseMenuManage;
import com.cattsoft.coolsql.pub.component.ViewPopMenu;


/**
 * View manager .
 * @author liu_xlin
 */
public class ViewManage {
	public static final String VIEWKEY_BOOKMARKVIEW="bookmark";
	public static final String VIEWKEY_SQLEDITORVIEW="sqleditor";
	public static final String VIEWKEY_RESULTSETVIEW="resultset";
	public static final String VIEWKEY_LOGVIEW="log";
	
    private static ViewManage viewManage;
//    private MainFrame main;
	private BookmarkView bookmarkView;
	private ResultSetView resultView;
	private SqlEditorView sqlEditor;
	private LogView logView;
	
	private Map<String,View> views=Collections.synchronizedMap(new HashMap<String,View>());
	/**
	 * ��ͼ�Ҽ�˵�������
	 */
	private BaseMenuManage viewMenu=null;
	private ViewManage()
	{
//		bookmarkView=new ConnectInfoView();
//		resultLog=new ResultSetView();
//		sqlEditor=new SqlEditor();
	}
	public static synchronized ViewManage  getInstance()
	{
		if(viewManage==null)
			viewManage=new ViewManage();
		return viewManage;
	}
	/**
	 * get size of views
	 * @return
	 */
	public int getViewCount()
	{
		return views.size();
	}
	public Collection<View> getViews()
	{
		return views.values();
	}
	/**
	 * add a view into the views list.
	 * @param view
	 */
	public void addView(String key,View view)
	{
		//TODO:���һ�������ɷ�����
		if(views.containsKey(key))
			throw new IllegalArgumentException("repeated view key");
		views.put(key,view);
	}
	/**
	 * delete view according to viewkey from viewmap.
	 * @param viewKey -- key according to view object.
	 */
	public void removeView(String viewKey)
	{
		views.remove(viewKey);
	}
	/**
	 * ɾ��ѡ������ͼ
	 * @param view
	 */
//	public void removeView(View view)
//	{
//		if(view==null)
//			return ;
//		Container p1=view.getParent();
//		if(p1!=null&&(p1 instanceof JSplitPane))
//		{
//			JSplitPane sp=(JSplitPane)p1;
//			//��ͬһJSplitPane����ͼ
//			View vb=null;
//			if(sp.getTopComponent()==view)
//			{
//				vb=(View)sp.getBottomComponent();
//			}else
//			{
//				vb=(View)sp.getTopComponent();
//			}
//			
//			Container p2=p1.getParent();
//			if(p2 instanceof JPanel)
//			{
//				p2.removeAll();
//				p2.add(vb,BorderLayout.CENTER);
//				p2.validate();
//				p2.repaint();
//			}else if(p2 instanceof JSplitPane)
//			{
//				sp=(JSplitPane)p2;
//				if(p1==sp.getTopComponent())
//				{
//				    sp.remove(p1);
//				    sp.setTopComponent(vb);
//				}else
//				{
//					sp.remove(p1);
//					sp.setBottomComponent(vb);
//				}
//				sp.validate();
//				sp.repaint();
//				sp.resetToPreferredSizes();
//			}
//		}
//	}
	/**
	 * @return ���� bookmarkView��
	 */
	public BookmarkView getBookmarkView() {
		if(bookmarkView==null)
		{
			bookmarkView=new BookmarkView();
			addView(VIEWKEY_BOOKMARKVIEW,bookmarkView);
		}
		return bookmarkView;
	}
	/**
	 * @return ���� resultLog��
	 */
	public ResultSetView getResultView() {
		if(resultView==null)
		{
			resultView=new ResultSetView();
			addView(VIEWKEY_RESULTSETVIEW,resultView);	
		}
		return resultView;
	}
	/**O
	 * @return ���� sqlEditor��
	 */
	public synchronized SqlEditorView getSqlEditor() {
		if(sqlEditor==null)
		{
			sqlEditor=new SqlEditorView();
			addView(VIEWKEY_SQLEDITORVIEW,sqlEditor);	
		}
		return sqlEditor;
	}
	/**
	 * @return ���� logView��
	 */
	public LogView getLogView() {
		if(logView==null)
		{
			logView=new LogView();
			addView(VIEWKEY_LOGVIEW,logView);	
		}
		return logView;
	}
	/**
	 *Update all view theme color when theme color has been changed.
	 */
	public void updateViewTheme()
	{
		for(View view:views.values())
		{
			view.updateThemeColor();
		}
	}
	/**
	 * ��ȡ��ͼ�Ҽ�˵��Ĺ�����
	 * @return  --��ͼ�Ҽ�˵�������(BaseMenuManage)
	 */
	public BaseMenuManage getViewMenuManage()
	{
	    if(viewMenu==null)
	        viewMenu=new ViewPopMenu(null);
	    return viewMenu;
	}
}
