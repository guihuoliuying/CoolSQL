/**
 * 
 */
package com.cattsoft.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSplitPane;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.ViewManage;

/**
 * @author xiaolin
 * 
 */
public class BookmarkViewDisplayAction extends CsAction{

	private static final long serialVersionUID = 1L;

	public BookmarkViewDisplayAction()
	{
		initMenuDefinitionById("BookmarkViewDisplayAction");
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void executeAction(ActionEvent arg0) {
		Object ob = arg0.getSource();
		if (ob instanceof JCheckBoxMenuItem) {
			JCheckBoxMenuItem item = (JCheckBoxMenuItem) ob;
			if (item.isSelected()) {
				View view = ViewManage.getInstance().getBookmarkView();
//				GUIUtil.controlSplit(view, false);
				if (!view.isViewVisible())
					view.showPanel(false);
				GUIUtil.controlSplit(view, false);
				
				// �ָ�ԭʼλ��
				JSplitPane split = GUIUtil.getSplitContainer(view);
				Integer location = (Integer) split
						.getClientProperty(View.LASTLOCATION);
				if(location==null)
					location=GUIUtil.DEFAULT_VIEWWIDTH;
				split.setDividerLocation(location);
				if(!GUIUtil.isMaxSplitToSelf(split))
					split.putClientProperty(View.LASTLOCATION, null); // ����Ѿ��ָ������
			} else {
				View view = ViewManage.getInstance().getBookmarkView();

				/**
				 * ����ԭʼλ��
				 */

				JSplitPane split = GUIUtil.getSplitContainer(view);
				if (split.getClientProperty(View.LASTLOCATION) == null) {
					int location = split.getDividerLocation();
					split.putClientProperty(View.LASTLOCATION, location);
				}
				if (view.isViewVisible())
					view.hidePanel(false);
				GUIUtil.controlSplit(view, true);
			}
			Setting.getInstance().setBooleanProperty(
					PropertyConstant.PROPERTY_VIEW_BOOKMARK_ISDISPLAY, item.isSelected());
		}

	}

}
