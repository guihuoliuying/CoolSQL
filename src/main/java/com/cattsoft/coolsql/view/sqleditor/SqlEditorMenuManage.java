/*
 * Create date: 2006-7-3
 *
 */
package com.cattsoft.coolsql.view.sqleditor;

import javax.swing.JMenuItem;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.cattsoft.coolsql.action.common.ExportDataOfDBAction;
import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.action.sqleditormenu.AutoSelectAction;
import com.cattsoft.coolsql.exportdata.ExportData;
import com.cattsoft.coolsql.pub.component.BaseMenuManage;
import com.cattsoft.coolsql.pub.component.BasePopupMenu;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.menu.action.AddSelectedTextAsFavoriteAction;
import com.cattsoft.coolsql.view.sqleditor.action.CommitAction;
import com.cattsoft.coolsql.view.sqleditor.action.MultiStatementExecuteAction;
import com.cattsoft.coolsql.view.sqleditor.action.RollbackAction;
import com.cattsoft.coolsql.view.sqleditor.action.SQLEditorFindAction;
import com.cattsoft.coolsql.view.sqleditor.action.SQLExcuteAction;
import com.jidesoft.swing.JideMenu;

/**
 * Popup menu attached to sql editor view.
 * @author liu_xlin 
 */
public class SqlEditorMenuManage extends BaseMenuManage {

	private CsAction autoSelectAction;

	// sqlִ��
	private CsAction sqlExecute;
	/**
	 * execute sqls considered as script.
	 */
	private CsAction executeAsScript;

	private CsAction formatSQLAction;
	/**
	 * �������
	 */
	private JideMenu export;

	// �������ı�
	private JMenuItem exportTxt;

	// ������excel�ļ�
	private JMenuItem exportExcel;
	
	//Add selected text in the sql editor.
	private CsAction addSelectedTextAsFavoriteAction;

	/**
	 * ���ڱ����Ƿ�����˵����˵��ĳ�ʼ����������createPopMenu()�����������Ϊtrue
	 */
	private boolean isInit = false;
	public SqlEditorMenuManage(SqlPanel textPane) {
		super(textPane);
		this.popMenu = new BasePopupMenu();
		this.popMenu.addPopupMenuListener(new SQLEditorPopmenuListener());

		autoSelectAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(AutoSelectAction.class);


		formatSQLAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(FormatSQLAction.class);
		
	}

	/**
	 * ��д�ı��༭����ķ���
	 */
	protected void createPopMenu() {
		if (sqlExecute == null) {
			sqlExecute = Setting.getInstance().getShortcutManager()
					.getActionByClass(SQLExcuteAction.class);
		}
		if (executeAsScript == null) {
			executeAsScript = Setting.getInstance().getShortcutManager()
					.getActionByClass(MultiStatementExecuteAction.class);
		}
		if(addSelectedTextAsFavoriteAction==null)
		{
			addSelectedTextAsFavoriteAction=Setting.getInstance().getShortcutManager()
			.getActionByClass(AddSelectedTextAsFavoriteAction.class);
		}
		
		CsAction undoAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(com.cattsoft.coolsql.system.menu.action.UndoMenuAction.class);
		CsAction redoAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(com.cattsoft.coolsql.system.menu.action.RedoMenuAction.class);
		popMenu.add(undoAction.getMenuItem());
		popMenu.add(redoAction.getMenuItem());
		
		popMenu.addSeparator();
		
		//cut
		CsAction cutAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(com.cattsoft.coolsql.system.menu.action.CutMenuAction.class);
		popMenu.add(cutAction.getMenuItem());

		//copy
		CsAction copyAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(com.cattsoft.coolsql.system.menu.action.CopyMenuAction.class);
		popMenu.add(copyAction.getMenuItem());

		//paste
		CsAction pasteAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(com.cattsoft.coolsql.system.menu.action.PasteMenuAction.class);
		popMenu.add(pasteAction.getMenuItem());

		popMenu.addSeparator();
		
		//select all
		CsAction selectAllAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(com.cattsoft.coolsql.system.menu.action.SelectAllMenuAction.class);
		popMenu.add(selectAllAction.getMenuItem());

		CsAction findAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(SQLEditorFindAction.class);
		popMenu.add(findAction.getMenuItem());
		
		popMenu.addSeparator();

		/**
		 * ��Ϣ��ʾ�˵���
		 */
		EditorPanel tCom = ((SqlPanel) this.getComponent()).getEditor();
		
		popMenu.add(((CsAction)tCom.getShowPromptPopAction()).getMenuItem());

		/**
		 * Retrive the sql automatically
		 */
		popMenu.add(autoSelectAction.getMenuItem());

		popMenu.add(sqlExecute.getMenuItem());

		popMenu.add(executeAsScript.getMenuItem());
		popMenu.addSeparator();

		//commitAction
		CsAction commitAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(CommitAction.class);
		popMenu.add(commitAction.getMenuItem());
		
		//rollbackAction
		CsAction rollbackAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(RollbackAction.class);
		popMenu.add(rollbackAction.getMenuItem());
		
		popMenu.addSeparator();
		
		popMenu.add(formatSQLAction.getMenuItem());

		popMenu.add(addSelectedTextAsFavoriteAction.getMenuItem());
		popMenu.addSeparator();

		// ������ݲ˵�
		export = new JideMenu(PublicResource
				.getString("sqlEditorView.popupmenu.export"));
		popMenu.add(export);

		// �����ı��˵�
		exportTxt = createMenuItem(PublicResource
				.getString("table.popup.exportTxt"), PublicResource
				.getIcon("table.popup.export.txticon"),
				new ExportDataOfDBAction(ExportData.EXPORT_TEXT));
		export.add(exportTxt);

		// ����excel�˵�
		exportExcel = createMenuItem(PublicResource
				.getString("table.popup.exportExcel"), PublicResource
				.getIcon("table.popup.export.excelicon"),
				new ExportDataOfDBAction(ExportData.EXPORT_EXCEL));
		export.add(exportExcel);
		isInit = true;
	}
	/**
	 * ��д�ı��༭����ķ���
	 */
	public BasePopupMenu itemCheck() {
		// super.itemCheck();
		if (!isInit)
			createPopMenu();

		// --------------

		return popMenu;
	}

	/**
	 * ��д�ı��༭����ķ���
	 */
	public BasePopupMenu getPopMenu() {

		return popMenu;
	}
	/**
	 * ������sqleditor������Ӧ��view��ͼ���Ҽ�˵����?�ر��ǵ����˵��Ĳ˵���Ŀ����Ե�У�顣
	 * 
	 * @author kenny liu
	 * 
	 * 2007-11-26 create
	 */
	private class SQLEditorPopmenuListener implements PopupMenuListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.event.PopupMenuListener#popupMenuCanceled(javax.swing.event.PopupMenuEvent)
		 */
		public void popupMenuCanceled(PopupMenuEvent e) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent)
		 */
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent)
		 */
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			itemCheck();
		}

	}
}
