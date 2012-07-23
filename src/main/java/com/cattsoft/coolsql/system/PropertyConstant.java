/**
 * 
 */
package com.cattsoft.coolsql.system;

/**
 * Property name constant
 * 
 * @author kenny liu
 * 
 *         2007-12-13 create
 */
public class PropertyConstant {

	/**
	 * common property
	 */
	public static final String PROPERTY_EDITOR_TAB_WIDTH = "view.editor.tab.width";// no
																					// setting
	public static final String PROPERTY_SHOW_LINE_NUMBERS = "view.editor.showlinenumbers";// no
																							// setting

	public static final String PROPERTY_EDITORTABLE_SHORTDRAGTYPE = "editortable.shortdragtype";
	public static final String PROPERTY_EDITORTABLE_ERRORCELL_COLOR = "editortable.errorcell.color";

	public static final String PROPERTY_COMMONTABLE_ISDISPLAYLINENUMBER = "commontable.isdisplaylinenumber";
	public static final String PROPERTY_COMMONTABLE_DISPLAY_HORIZONTALLINE = "commontable.displayhorizontalline";
	public static final String PROPERTY_COMMONTABLE_DISPLAY_VERTICALLINE = "commontable.displayverticalline";
	// public static final String PROPERTY_LANGUAGE="general.language";
	public static final String PROPERTY_SYSTEM_FONT = "general.systemfont";

	public static final String PROPERTY_TEXTEDITOR_FONT = "general.texteditor.font";
	public static final String PROPERTY_COMMONTABLE_RESIZEROWHEIGHT = "commontable.isResizeRowHeight";
	public static final String PROPERTY_COMMONTABLE_ROWHEIGHT = "commontable.rowHeight";
	/**
	 * system property
	 */
	public static final String PROPERTY_SYSTEM_STATUSBAR_DISPLAY = "system.statusbar.isdisplay";
	public static final String PROPERTY_SYSTEM_HISTORYDAYS = "system.history.days"; // ok

	public static final String PROPERTY_SYSTEM_EXPORT_TEXT_ISDISPLAYHEAD = "system.export.text.isdisplayhead";
	public static final String PROPERTY_SYSTEM_EXPORT_TEXT_DELIMITER = "system.export.text.delimiter";
	public static final String PROPERTY_SYSTEM_EXPORT_HTML_PAGESIZE = "system.export.html.pagesize";
	public static final String PROPERTY_SYSTEM_EXPORT_EXCEL_ISDISPLAYHEAD = "system.export.excel.isdisplayhead";
	public static final String PROPERTY_SYSTEM_EXPORT_EXCEL_MAXROWSWRITE = "system.export.excel.maxrowswrite";
	public static final String PROPERTY_SYSTEM_EXPORT_EXCEL_HEADCOLOR = "system.export.excel.headcolor";

	/**
	 * favorite
	 */
	/**
	 * system menu displays parts of all favorites,but size of displayed
	 * favorite items will be limited by this property
	 */
	public static final String PROPERTY_FAVORITE_DISPLAY_MAXSIZE = "favorite.display.maxsize";

	/**
	 * view public property
	 */
	public static final String PROPERTY_VIEW_RESULTSET_ISDISPLAY = "view.resultset.isdisplay"; //
	public static final String PROPERTY_VIEW_LOG_ISDISPLAY = "view.log.isdisplay"; //
	public static final String PROPERTY_VIEW_SQLEDITOR_ISDISPLAY = "view.sqleditor.isdisplay"; //
	public static final String PROPERTY_VIEW_BOOKMARK_ISDISPLAY = "view.bookmark.isdisplay"; //

	public static final String PROPERTY_VIEW_PUBLIC_ISSHOWTABLEROWBORDER = "view.public.isshowtablerowborder"; //
	public static final String PROPERTY_VIEW_PUBLIC_TABLEROWBORDER_COLOR = "view.public.tablerowborder.color"; //
	/**
	 * bookmark view
	 */
	public static final String PROPERTY_VIEW_BOOKMARK_ISSHOWNODEBORDER = "view.bookmark.isshownodeborder"; //
	public static final String PROPERTY_VIEW_BOOKMARK_NODEBORDER_COLOR = "view.bookmark.nodeborder.color"; //
	public static final String PROPERTY_VIEW_BOOKMARK_CONNECT_TIMEOUT = "view.bookmark.connect.timeout"; // ok
	public static final String PROPERTY_VIEW_BOOKMARK_BEFORE_DISCONNECT = "view.bookmark.before.disconnect"; // ok
	public static final String PROPERTY_VIEW_BOOKMARK_DEFAULT_HIGHLIGHTCOLOR = "view.bookmark.default.highlightcolor"; // ok
	/**
	 * sqleditor view
	 */
	public static final String PROPERTY_VIEW_SQLEDITOR_SQL_DELIMITER = "view.sqleditor.sql.delimiter";// ok
	public static final String PROPERTY_VIEW_SQLEDITOR_SQL_HISTORYSIZE = "view.sqleditor.sql.historysize";// ok
	public static final String PROPERTY_VIEW_SQLEDITOR_ISSAVEEDITORCONTENT = "view.sqleditor.issaveeditorcontent";// ok
	public static final String PROPERTY_VIEW_SQLEDITOR_SELECTIONCOLOR = "view.sqleditor.selectioncolor";// ok
	public static final String PROPERTY_VIEW_SQLEDITOR_EDITOR_FONT = "view.sqleditor.font"; // ok

	public static final String PROPERTY_VIEW_SQLEDITOR_BEFORE_ENABLEAUTOCOMMIT = "view.sqleditor.before.enableautocommit"; // ok

	// highlight
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_COLOR = "view.sqleditor.highlight.comment1.color";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_ISBOLD = "view.sqleditor.highlight.comment1.isbold";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_ISITALIC = "view.sqleditor.highlight.comment1.isitalic";

	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_COLOR = "view.sqleditor.highlight.comment2.color";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_ISBOLD = "view.sqleditor.highlight.comment2.isbold";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_ISITALIC = "view.sqleditor.highlight.comment2.isitalic";

	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_COLOR = "view.sqleditor.highlight.keyword1.color";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_ISBOLD = "view.sqleditor.highlight.keyword1.isbold";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_ISITALIC = "view.sqleditor.highlight.keyword1.isitalic";

	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_COLOR = "view.sqleditor.highlight.keyword2.color";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_ISBOLD = "view.sqleditor.highlight.keyword2.isbold";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_ISITALIC = "view.sqleditor.highlight.keyword2.isitalic";

	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_COLOR = "view.sqleditor.highlight.keyword3.color";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_ISBOLD = "view.sqleditor.highlight.keyword3.isbold";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_ISITALIC = "view.sqleditor.highlight.keyword3.isitalic";

	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_COLOR = "view.sqleditor.highlight.literal1.color";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_ISBOLD = "view.sqleditor.highlight.literal1.isbold";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_ISITALIC = "view.sqleditor.highlight.literal1.isitalic";

	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL2_COLOR = "view.sqleditor.highlight.literal2.color";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL2_ISBOLD = "view.sqleditor.highlight.literal2.isbold";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL2_ISITALIC = "view.sqleditor.highlight.literal2.isitalic";

	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_COLOR = "view.sqleditor.highlight.operator.color";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_ISBOLD = "view.sqleditor.highlight.operator.isbold";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_ISITALIC = "view.sqleditor.highlight.operator.isitalic";

	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_COLOR = "view.sqleditor.highlight.number.color";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_ISBOLD = "view.sqleditor.highlight.number.isbold";
	public static final String PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_ISITALIC = "view.sqleditor.highlight.number.isitalic";

	// if count of sqls to be executed exceeds the threshold value,executing
	// will be considered as processing script
	// default sql statment size is 2
	public static final String PROPERTY_VIEW_SQLEDITOR_SCRIPT_SQLTHRESHOLD = "view.sqleditor.script.sqlthreshold";// ok
	// Currently sqlEditor provides a prompt popup, this property indicates what
	// type should be displayed.
	// All type is separated with comma, such as "TABLE,VIEW,SYSTEM"
	public static final String PROPERTY_VIEW_SQLEDITOR_PROMPTTYPE = "view.sqleditor.prompttype";
	/**
	 * resultset view
	 */
	public static final String PROPERTY_VIEW_RESULTSET_DISPLAYMETAINFO = "view.resultset.displaymetainfo";
	public static final String PROPERTY_VIEW_RESULTSET_NUMBEROFROWSPERPAGE = "view.resultset.datatable.numberOfRowsPerPage";
	public static final String PROPERTY_VIEW_RESULTSET_MAXCOLUMNWIDTH = "view.resultset.maxcolumnwidth";

	public static final String PROPERTY_VIEW_RESULTSET_MODIFIEDCELL_HIGHLIGHT = "view.resultset.modifiedcell.highlight";

	// The property relevant to result table in the resultset view.
	public static final String PROPERTY_VIEW_RESULTSET_DATASETTABLE_ISDIRECTMODIFY = "view.resultset.datasettable.isdirectmodify";
	public static final String PROPERTY_VIEW_RESULTSET_DATASETTABLE_ISSORTABLE = "view.resultset.datasettable.issortable";

	/** log view */
	public static final String PROPERTY_VIEW_LOG_MAXLOGTEXT = "view.log.maxlogtext";
	public static final String PROPERTY_VIEW_LOG_ISSAVETOFILE = "view.log.issavetofile";
	public static final String PROPERTY_VIEW_LOG_FILEPATH = "view.log.filepath";
	public static final String PROPERTY_VIEW_LOG_INFOCOLOR = "view.log.infocolor";
	public static final String PROPERTY_VIEW_LOG_WARNCOLOR = "view.log.warncolor";
	public static final String PROPERTY_VIEW_LOG_ERRORCOLOR = "view.log.errorcolor";
	public static final String PROPERTY_VIEW_LOG_LEVEL = "view.log.level";
	/**
	 * parameters relative data formater
	 */
	public static final String PROPERTY_COMMON_DATAFORMATER_SEP = "common.data.formater.sep";
	public static final String PROPERTY_COMMON_MESSAGEBUFFER_MAXSIZE = "common.data.messagebuffer.maxsize";
}
