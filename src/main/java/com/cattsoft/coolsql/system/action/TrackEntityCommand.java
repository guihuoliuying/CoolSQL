/**
 * 
 */
package com.cattsoft.coolsql.system.action;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import com.cattsoft.coolsql.action.common.ActionCommand;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.TableCellObject;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.pub.util.SqlUtil;
import com.cattsoft.coolsql.search.SearchOfEntityFrame;
import com.cattsoft.coolsql.search.SearchResultFrame;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.view.SqlEditorView;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.bookmarkview.model.TableNode;
import com.cattsoft.coolsql.view.bookmarkview.model.ViewNode;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.sqleditor.EditorPanel;

/**
 * @author ��Т��(kenny liu)
 * 
 * 2008-3-8 create
 */
public class TrackEntityCommand implements ActionCommand {
	/** Internationalized strings for this class. */
    private static final StringManager s_stringMgr =
        StringManagerFactory.getStringManager(TrackEntityCommand.class);
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.action.common.ActionCommand#exectue()
	 */
	public void exectue() throws Exception {
		// create Entity object
		SqlEditorView view = ViewManage.getInstance().getSqlEditor();
		Bookmark bookmark = BookmarkManage.getInstance().getDefaultBookmark();
		try
		{
			if (!bookmark.isConnected()) {
				LogProxy.message("bookmark:" + bookmark.getAliasName()
						+ " has not connected to database", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String objectName = getWordAtCursor();
			Entity en = SqlUtil.getTableObject(bookmark, objectName);
			if(en==null)
				return;
			List<?> list = bookmark.getDbInfoProvider().queryEntities(bookmark,
					bookmark.getConnection(), en.getCatalog(), en.getSchema(),
					en.getName(), null);
			if (list == null || list.size() == 0) {
				LogProxy.errorMessage(s_stringMgr.getString("objectselected.notfound"));
				return;
			}
			Entity[] result = (Entity[]) list.toArray(new Entity[list.size()]);

			if (result.length == 1) // select the node directly in bookmark tree
									// according to entity finded in database.
			{
				Identifier id = null;
				if (result[0].getType().equals(SqlUtil.VIEW)) {
					id = new ViewNode(result[0].getName(), bookmark, result[0]);
				} else {
					id = new TableNode(result[0].getName(), bookmark, result[0]);
				}
				BookmarkTreeUtil util = BookmarkTreeUtil.getInstance();
				util.selectNode(id, false);
			} else {

				SearchResultFrame resultFrame = new SearchOfEntityFrame(GUIUtil
						.getMainFrame(), bookmark);
				resultFrame.setCount(result.length);
				Object[] rowData = null;

				for (int i = 0; i < result.length; i++) {
					rowData = new Object[4];
					String entityType = result[i].getType();

					rowData[0] = new TableCellObject(result[i].getName(),
							BookMarkPubInfo.getTableTypeIcon(entityType));
					rowData[1] = result[i].getCatalog();
					rowData[2] = result[i].getSchema();
					rowData[3] = result[i].getType();
					resultFrame.addRow(rowData);
				}
				resultFrame.adjustGUI();
				resultFrame.setVisible(true);
			}
		} catch (UnifyException e1) {
			LogProxy.errorReport(
					s_stringMgr.getString("objectselected.findfail")+ e1.toString(), e1);
		} catch (SQLException e1) {
			LogProxy.SQLErrorReport(e1);
		}finally
		{
			view.clearTrackEntity();
			EditorPanel panel = view.getEditorPane();
			panel.getPainter().repaint();
		}

	}
	private String getWordAtCursor() {
		EditorPanel panel = ViewManage.getInstance().getSqlEditor()
				.getEditorPane();
		int caretPos = panel.getCaretPosition();
		return panel.getWordOnOffset(caretPos);

	}

}
