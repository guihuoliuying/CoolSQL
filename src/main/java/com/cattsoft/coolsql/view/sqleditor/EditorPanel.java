package com.cattsoft.coolsql.view.sqleditor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Set;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.popprompt.AbstractIntelliHints;
import com.cattsoft.coolsql.popprompt.SQLEditorIntelliHints;
import com.cattsoft.coolsql.pub.display.AnsiSQLTokenMarker;
import com.cattsoft.coolsql.pub.display.ClipboardSupport;
import com.cattsoft.coolsql.pub.display.DelimiterDefinition;
import com.cattsoft.coolsql.pub.display.JEditTextArea;
import com.cattsoft.coolsql.pub.display.SyntaxDocument;
import com.cattsoft.coolsql.pub.display.TextContainer;
import com.cattsoft.coolsql.pub.display.TokenMarker;
import com.cattsoft.coolsql.pub.editable.TextToken;
import com.cattsoft.coolsql.pub.util.SyntaxUtilities;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.sqleditor.pop.BaseListCell;

/**
 *  This class
 * implements SQL specific extensions to JEditTextArea class.
 * 
 * @author kenny liu
 */
public class EditorPanel
	extends JEditTextArea
	implements ClipboardSupport, PropertyChangeListener,
						 TextContainer, FormattableSql
{
//	private static final Logger logger=Logger.getLogger(EditorPanel.class);
	private static final long serialVersionUID = 1L;
	private static final Border DEFAULT_BORDER = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	private AnsiSQLTokenMarker sqlTokenMarker;
	private static final int SQL_EDITOR = 0;
	private static final int TEXT_EDITOR = 1;
	private int editorType;
	private String lastSearchCriteria;

//	private FormatSqlAction formatSql;
	
//	private ColumnSelectionAction columnSelection;
	private String fileEncoding;
	private Set dbFunctions = null;
	private Set dbDatatypes = null;
	private boolean isMySQL = false;
	private DelimiterDefinition alternateDelimiter;
	
	private Action showPromptPopAction;
	public static EditorPanel createSqlEditor()
	{
		AnsiSQLTokenMarker sql = new AnsiSQLTokenMarker();
		EditorPanel p = new EditorPanel(sql);
		p.editorType = SQL_EDITOR;
		p.sqlTokenMarker = sql;
		return p;
	}
	public static EditorPanel createSqlEditorWithPrompt()
	{
		AnsiSQLTokenMarker sql = new AnsiSQLTokenMarker();
		final EditorPanel p = new EditorPanel(sql)
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void processKeyEvent(KeyEvent evt) {
				processBodyKey(evt);
				super.processKeyEvent(evt);
			}
			private void processBodyKey(KeyEvent evt)
			{
				if(evt.getID()!=KeyEvent.KEY_PRESSED)
					return;
				KeyStroke keyStroke=KeyStroke.getKeyStroke(evt.getKeyCode(),evt.getModifiers());
				Object ob=getActionForKeyStroke(keyStroke);

				if(ob instanceof AbstractIntelliHints.LazyAction)
				{
					if(executeAction((AbstractIntelliHints.LazyAction)ob,evt.getSource(),String.valueOf(evt.getKeyChar())))
						evt.consume();
				}
			}
			public boolean executeAction(AbstractIntelliHints.LazyAction listener, Object source, String actionCommand)
			{
				// create event
				ActionEvent evt = new ActionEvent(source,ActionEvent.ACTION_PERFORMED,actionCommand);
				return listener.delegateActionPerformed(evt);
			}
		};
		p.editorType = SQL_EDITOR;
		p.sqlTokenMarker = sql;
	    /**
	     * design a prompt popup 
	     */
	    final SQLEditorIntelliHints intelliHints=new SQLEditorIntelliHints(p,(List<BaseListCell>)null);
        intelliHints.setCaseSensitive(false);
        p.getPainter().addMouseListener(new MouseAdapter()
        {
        	@Override
        	public void mousePressed(MouseEvent e)
        	{
        		if(!intelliHints.isHintsPopupVisible())
        			return;
        		intelliHints.hiddenPopup();
        	}
        }
        );
        final CsAction showPromptAction=new CsAction()
        {

			private static final long serialVersionUID = 1L;

			public void executeAction(ActionEvent e) {
				
				if(!intelliHints.isHintsPopupVisible())
				{
					intelliHints.showHintsPopupForOutside();
					p.requestFocusInWindow();
				}
			}
        	
        };
        showPromptAction.setAccelerator(intelliHints.getShowHintsKeyStroke());
        showPromptAction.setDefaultAccelerator(intelliHints.getShowHintsKeyStroke());
        showPromptAction.addPropertyChangeListener(new PropertyChangeListener()
    	{
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals(Action.ACCELERATOR_KEY))
				{
					Object tmpks=showPromptAction.getValue(Action.ACCELERATOR_KEY);
					if(tmpks==null||tmpks instanceof KeyStroke)
					{
						intelliHints.setShowHintsKeyStroke((KeyStroke)tmpks);
					}
				}
				
			}
    		
    	}
    	);
        p.setShowPromptPopAction(showPromptAction);
		return p;
	}
	public static EditorPanel createTextEditor()
	{
		EditorPanel p = new EditorPanel(null);
		p.editorType = TEXT_EDITOR;
		return p;
	}

	public EditorPanel(TokenMarker aMarker)
	{
		super();
		this.setDoubleBuffered(true);
		this.setFont(Setting.getInstance().getEditorFont());
		this.setBorder(DEFAULT_BORDER);

		this.getPainter().setStyles(SyntaxUtilities.getDefaultSyntaxStyles());

		this.setTabSize(Setting.getInstance().getEditorTabWidth());
		this.setCaretBlinkEnabled(true);

		if (aMarker != null) this.setTokenMarker(aMarker);

		this.setMaximumSize(null);
		this.setPreferredSize(null);
		this.setShowLineNumbers(Setting.getInstance().getShowLineNumbers());
/**
 * TODO:  this place should add and modify
 */
//		this.columnSelection = new ColumnSelectionAction(this);
//		this.matchBracket = new MatchBracketAction(this);
//		this.addKeyBinding(this.matchBracket.getAccelerator(),matchBracket);
//
//		this.commentAction = new CommentAction(this);
//		this.unCommentAction = new UnCommentAction(this);

//		this.setSelectionRectangular(false);
		Setting.getInstance().addPropertyChangeListener(this, 
				new String[]{PropertyConstant.PROPERTY_SHOW_LINE_NUMBERS, PropertyConstant.PROPERTY_EDITOR_TAB_WIDTH});
		this.setRightClickMovesCursor(Setting.getInstance().getRightClickMovesCursor());
//		new DropTarget(this, DnDConstants.ACTION_COPY, this);
	}

//	public void paint(Graphics g)
//	{
//		super.paint(g);
//		painter.repaint();
//	}
	public void setText(int start,int end,String str)
	{
		selectionStart=start;
		selectionEnd=end;
		setSelectedText(str);
	}
	public void disableSqlHighlight()
	{
		this.setTokenMarker(null);
	}
	
	public void enableSqlHighlight()
	{
		if (this.sqlTokenMarker == null)
		{
			this.sqlTokenMarker = new AnsiSQLTokenMarker();
		}
		this.setTokenMarker(this.sqlTokenMarker);
	}

	public AnsiSQLTokenMarker getSqlTokenMarker()
	{
		TokenMarker marker = this.getTokenMarker();
		if (marker instanceof AnsiSQLTokenMarker)
		{
			return (AnsiSQLTokenMarker)marker;
		}
		return null;
	}

//	protected FindAction getFindAction() { return this.replacer.getFindAction(); }
//	protected FindAgainAction getFindAgainAction() { return this.replacer.getFindAgainAction(); }
//	protected ReplaceAction getReplaceAction() { return this.replacer.getReplaceAction(); }
//
//	public SearchAndReplace getReplacer() { return this.replacer; }
	
//	public FileSaveAction getFileSaveAction() { return this.fileSave; }
//	public FileSaveAsAction getFileSaveAsAction() { return this.fileSaveAs; }
//	public FormatSqlAction getFormatSqlAction() { return this.formatSql; }
//	public FileReloadAction getReloadAction() { return this.fileReloadAction; }
//	public FileOpenAction getFileOpenAction() { return this.fileOpen; }

//	public void showFormatSql()
//	{
//		if (this.formatSql != null) return;
//		this.formatSql = new FormatSqlAction(this);
//		this.addKeyBinding(this.formatSql);
//		this.addPopupMenuItem(this.formatSql, true);
//	}
	void setShowPromptPopAction(Action action)
	{
		showPromptPopAction=action;
	}
	public Action getShowPromptPopAction()
	{
		return showPromptPopAction;
	}
	/**
	 * This method indicates whether editor panel can prompt user for useful information displayed in the pop window.
	 * Return true if has a prompt pop window, or return false.
	 */
	public boolean hasPromptPop()
	{
		return showPromptPopAction!=null;
	}
	public void setEditable(boolean editable)
	{
		super.setEditable(editable);
//		this.getReplaceAction().setEnabled(editable);
//		this.fileOpen.setEnabled(editable);
//		if (!editable)
//		{
//			Component[] c = this.popup.getComponents();
//			for (int i = 0; i < c.length; i++)
//			{
//				if (c[i] instanceof LazyMenuItem)
//				{
//					LazyMenuItem menu = (LazyMenuItem)c[i];
//					if (menu.getAction() == fileOpen)
//					{
//						popup.remove(c[i]);
//						return;
//					}
//				}
//			}
//		}
	}

	/**
	 * TODO:��ʽ��sql����ʱ����Ҫʹ��
	 */
	public void reformatSql()
	{
		TextFormatter f = new TextFormatter();
		f.formatSql(this, alternateDelimiter, dbFunctions, dbDatatypes, isMySQL ? "#" : "--");
	}

	/**
	 * 	Enable column selection for the next selection.
	 */
	public void enableColumnSelection()
	{
		this.setSelectionRectangular(true);
	}

	public void addPopupMenuItem(JMenuItem item, boolean withSeparator)
	{
		if (withSeparator)
		{
			this.popup.addSeparator();
		}
		this.popup.add(item);
//		this.addKeyBinding(anAction);
	}

	public void dispose()
	{
		this.clearUndoBuffer();
		this.popup.removeAll();
		this.popup = null;
		this.stopBlinkTimer();
		this.setDocument(new SyntaxDocument());
	}
	/**
	 * Return the selected statement of the editor. If no 
	 * text is selected, the whole text will be returned
	 */
	public String getSelectedStatement()
	{
		String text = this.getSelectedText();
		if (text == null || text.length() == 0)
			return this.getText();
		else
			return text;
	}
	/**
	 * get word on the offset.
	 * @param offset
	 * @return
	 */
	public String getWordOnOffset(int offset)
	{
		Bookmark bookmark =BookmarkManage.getInstance().getDefaultBookmark();
		String delimiter = Setting.getInstance().getDatabaseSetting(
				bookmark.getAliasName()).getSQLStatementSeparator();
		String text=getText();
		char delimiterChar = delimiter.charAt(0);
		int lastIndexOfText = Math.max(0, text.length() - 1);
		int beginPos = Math.min(offset, lastIndexOfText); // The Math.min is
															// for the Caret at
															// the end of the
															// text
		beginPos=getLeftPositionOfWord(beginPos,delimiterChar);
		int endPos=getRightPositionOfWord(offset,delimiterChar);
		
		return text.substring(beginPos, endPos).trim();
		
	}
	public TextToken getTextTokenOnOffset(int offset)
	{
		Bookmark bookmark =BookmarkManage.getInstance().getDefaultBookmark();
		String delimiter = Setting.getInstance().getDatabaseSetting(
				bookmark.getAliasName()).getSQLStatementSeparator();
		String text=getText();
		char delimiterChar = delimiter.charAt(0);
		
		int lastIndexOfText =getLineEndOffset(getLineOfOffset(offset))-2;
		int beginPos = Math.min(offset-1, lastIndexOfText); // The Math.min is
															// for the Caret at
															// the end of the
															// text
		beginPos=getLeftPositionOfWord(beginPos,delimiterChar);
//		int endPos=getRightPositionOfWordOfToken(offset,delimiterChar);
		//endPos+1>text.length()?endPos:(endPos+1)
		String tmpTxt=text.substring(beginPos, offset).trim();
		return new TextToken(beginPos,tmpTxt);
		
	}
//	private int getRightPositionOfWordOfToken(int endPos,char delimiterChar)
//	{
//		String text=getText();
//		if(endPos<0)
//			return 0;
//		while (endPos < text.length() ) {
//			char c=text.charAt(endPos);
//			if('(' == c || ')' == c || '\'' == c || c == delimiterChar
//					|| Character.isWhitespace(c) ||'.' == c)
//				break;
//			
//			++endPos;
//		}
//		return endPos;
//	}
	public int getLeftPositionOfWord(int beginPos,char delimiterChar)
	{
		String text=getText();
		if(beginPos>text.length())
			return text.length()-1;
		while (0 <= beginPos) {
			char c=text.charAt(beginPos);
			if( isParseStop(c, delimiterChar,false))
			{
				if(c=='\''||c==' '||c=='\n')
					beginPos++;
				break;
			}
			--beginPos;
		}
		return beginPos<0?0:beginPos;
	}
	public int getRightPositionOfWord(int endPos,char delimiterChar)
	{
//		int endPos = caretPos;
		String text=getText();
		if(endPos<0)
			return 0;
		while (endPos < text.length()
				&& false == isParseStop(text.charAt(endPos), delimiterChar,
						true)) {
			++endPos;
		}
		return endPos;
	}
	private boolean isParseStop(char c, char delimiterChar,
			boolean treatDotAsStop) {
		return '(' == c || ')' == c || '\'' == c || c == delimiterChar
				|| Character.isWhitespace(c) || (treatDotAsStop && '.' == c);

	}
//	public boolean closeFile(boolean clearText)
//	{
//    if (this.currentFile == null) return false;
//		this.currentFile = null;
//		if (clearText)
//		{
//			this.setCaretPosition(0);
//			this.setDocument(new SyntaxDocument());
//			this.clearUndoBuffer();
//		}
//		fireFilenameChanged(null);
//		this.resetModified();
//    return true;
//	}

//	protected void checkFileActions()
//	{
//		boolean hasFile = this.hasFileLoaded();
//		this.fileSave.setEnabled(hasFile);
//		this.fileReloadAction.setEnabled(hasFile);
//	}
	
//	public void fireFilenameChanged(String aNewName)
//	{
//		this.checkFileActions();
//		if (this.filenameChangeListeners == null) return;
//		Iterator<FilenameChangeListener> itr = filenameChangeListeners.iterator();
//		while (itr.hasNext())
//		{
//			FilenameChangeListener l = itr.next();
//			l.fileNameChanged(this, aNewName);
//		}
//	}

//	public void addFilenameChangeListener(FilenameChangeListener aListener)
//	{
//		if (aListener == null) return;
//		if (this.filenameChangeListeners == null) this.filenameChangeListeners = new LinkedList<FilenameChangeListener>();
//		this.filenameChangeListeners.add(aListener);
//	}

//	public void removeFilenameChangeListener(FilenameChangeListener aListener)
//	{
//		if (aListener == null) return;
//		if (this.filenameChangeListeners == null) return;
//		this.filenameChangeListeners.remove(aListener);
//	}
//
//	public boolean openFile()
//	{
//		boolean result = false;
//		YesNoCancelResult choice = this.canCloseFile();
//		if (choice == YesNoCancelResult.cancel)
//		{
//			this.requestFocusInWindow();
//			return false;
//		}
//
//		String lastDir = Settings.getInstance().getLastSqlDir();
//		JFileChooser fc = new JFileChooser(lastDir);
//		JComponent p = EncodingUtil.createEncodingPanel();
//		p.setBorder(new EmptyBorder(0, 5, 0, 0));
//		EncodingSelector selector = (EncodingSelector)p;
//		selector.setEncoding(Settings.getInstance().getDefaultFileEncoding());
//		fc.setAccessory(p);
//		
//		fc.addChoosableFileFilter(ExtensionFileFilter.getSqlFileFilter());
//		int answer = fc.showOpenDialog(SwingUtilities.getWindowAncestor(this));
//		if (answer == JFileChooser.APPROVE_OPTION)
//		{
//			String encoding = selector.getEncoding();
//			
//			result = readFile(fc.getSelectedFile(), encoding);
//			
//			WbSwingUtilities.repaintLater(this.getParent());
//			
//			lastDir = fc.getCurrentDirectory().getAbsolutePath();
//			Settings.getInstance().setLastSqlDir(lastDir);
//			Settings.getInstance().setDefaultFileEncoding(encoding);
//		}
//		return result;
//	}
//
//	public boolean reloadFile()
//	{
//		if (!this.hasFileLoaded()) return false;
//		if (this.currentFile == null) return false;
//
//		if (this.isModified())
//		{
//			String msg = ResourceMgr.getString("MsgConfirmUnsavedReload");
//			msg = StringUtil.replace(msg, "%filename%", this.getCurrentFileName());
//			boolean reload = WbSwingUtilities.getYesNo(this, msg);
//			if (!reload) return false;
//		}
//		boolean result = false;
//		int caret = this.getCaretPosition();
//		result = this.readFile(currentFile, fileEncoding);
//		if (result)
//		{
//			this.setCaretPosition(caret);
//		}
//		return result;
//	}


//	public boolean hasFileLoaded()
//	{
//		String file = this.getCurrentFileName();
//		return (file != null) && (file.length() > 0);
//	}
//
//	public int checkAndSaveFile()
//	{
//		if (!this.hasFileLoaded()) return JOptionPane.YES_OPTION;
//		int result = JOptionPane.YES_OPTION;
//
//		if (this.isModified())
//		{
//			String msg = ResourceMgr.getString("MsgConfirmUnsavedEditorFile");
//			msg = StringUtil.replace(msg, "%filename%", this.getCurrentFileName());
//			result = WbSwingUtilities.getYesNoCancel(this, msg);
//			if (result == JOptionPane.YES_OPTION)
//			{
//				this.saveCurrentFile();
//			}
//		}
//		return result;
//	}

//	public YesNoCancelResult canCloseFile()
//	{
//		if (!this.hasFileLoaded()) return YesNoCancelResult.yes;
//		if (!this.isModified()) return YesNoCancelResult.yes;
//		int choice = this.checkAndSaveFile();
//		if (choice == JOptionPane.YES_OPTION)
//		{
//			return YesNoCancelResult.yes;
//		}
//		else if (choice == JOptionPane.NO_OPTION)
//		{
//			return YesNoCancelResult.no;
//		}
//		else 
//		{
//			return YesNoCancelResult.cancel;
//		}
//	}
//
//	public boolean readFile(File aFile)
//	{
//		return this.readFile(aFile, null);
//	}

//	public boolean readFile(File aFile, String encoding)
//	{
//		if (aFile == null) return false;
//		if (!aFile.exists()) return false;
//		if (aFile.length() >= Integer.MAX_VALUE / 2)
//		{
//			WbSwingUtilities.showErrorMessageKey(this, "MsgFileTooBig");
//			return false;
//		}
//		
//		boolean result = false;
//		
//		BufferedReader reader = null;
//		SyntaxDocument doc = null;
//		
//		try
//		{
//			// try to free memory by releasing the current document
//			if(this.document != null)
//			{
//				this.document.removeDocumentListener(documentHandler);
//				this.document.dispose();
//			}			
//			System.gc();
//			System.runFinalization();
//			
//			String filename = aFile.getAbsolutePath();
//			File f = new File(filename);
//			try
//			{
//				if (StringUtil.isEmptyString(encoding)) encoding = EncodingUtil.getDefaultEncoding();
//				reader = EncodingUtil.createBufferedReader(f, encoding);
//			}
//			catch (UnsupportedEncodingException e)
//			{
//				LogMgr.error("EditorPanel.readFile()", "Unsupported encoding: " + encoding + " requested. Using UTF-8", e);
//				try
//				{
//					encoding = "UTF-8";
//					FileInputStream in = new FileInputStream(filename);
//					reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8192);
//				}
//				catch (Throwable ignore) {}
//			}
//
//			// Creating a SyntaxDocument with a filled GapContent
//			// does not seem to work, inserting the text has to
//			// go through the SyntaxDocument
//			// but we initiallize the GapContent in advance to avoid
//			// too many re-allocations of the internal buffer
//			GapContent  content = new GapContent((int)aFile.length() + 1500);
//			doc = new SyntaxDocument(content);
//			doc.suspendUndo();
//			
//			int pos = 0;
//			
//			final int numLines = 50;
//			StringBuilder lineBuffer = new StringBuilder(numLines * 100);
//			
//			// Inserting the text in chunks is much faster than 
//			// inserting it line by line. Optimal speed would probably
//			// when reading everything into a buffer, and then call insertString()
//			// only once, but that will cost too much memory (the memory footprint
//			// of the editor is already too high...)
//			int lines = FileUtil.readLines(reader, lineBuffer, numLines, "\n");
//			while (lines > 0)
//			{
//				doc.insertString(pos, lineBuffer.toString(), null);
//				pos += lineBuffer.length();
//				lineBuffer.setLength(0);
//				lines = FileUtil.readLines(reader, lineBuffer, numLines, "\n");
//			}
//			
//			doc.resumeUndo();
//			this.setDocument(doc);
//			
//			this.currentFile = aFile;
//			this.fileEncoding = encoding;
//			result = true;
////			this.fireFilenameChanged(filename);
//		}
//		catch (BadLocationException bl)
//		{
//			LogMgr.error("EditorPanel.readFile()", "Error reading file " + aFile.getAbsolutePath(), bl);
//		}
//		catch (IOException e)
//		{
//			LogMgr.error("EditorPanel.readFile()", "Error reading file " + aFile.getAbsolutePath(), e);
//		}
//		catch (OutOfMemoryError mem)
//		{
//			doc.dispose();
//			System.gc();
//			WbManager.getInstance().showOutOfMemoryError();
//			result = false;
//		}
//		finally
//		{
//			this.resetModified();
//			FileUtil.closeQuitely(reader);
//		}
//		return result;
//	}

//	public boolean saveCurrentFile()
//	{
//		boolean result = false;
//		try
//		{
//			if (this.currentFile != null)
//			{
//				this.saveFile(this.currentFile);
//				result = true;
//			}
//			else
//			{
//				result = this.saveFile();
//			}
//		}
//		catch (IOException e)
//		{
//			result = false;
//		}
//		return result;
//	}
//
//	public boolean saveFile()
//	{
//		boolean result = false;
//		String lastDir;
//		FileFilter ff = null;
//		if (this.editorType == SQL_EDITOR)
//		{
//			lastDir = Settings.getInstance().getLastSqlDir();
//			ff = ExtensionFileFilter.getSqlFileFilter();
//		}
//		else
//		{
//			lastDir = Settings.getInstance().getLastEditorDir();
//			ff = ExtensionFileFilter.getTextFileFilter();
//		}
//		JFileChooser fc = new JFileChooser(lastDir);
//		fc.setSelectedFile(this.currentFile);
//		fc.addChoosableFileFilter(ff);
//		JComponent p = EncodingUtil.createEncodingPanel();
//		p.setBorder(new EmptyBorder(0,5,0,0));
//		EncodingSelector selector = (EncodingSelector)p;
//		selector.setEncoding(this.fileEncoding);
//		fc.setAccessory(p);
//
//		int answer = fc.showSaveDialog(SwingUtilities.getWindowAncestor(this));
//		if (answer == JFileChooser.APPROVE_OPTION)
//		{
//			try
//			{
//				String encoding = selector.getEncoding();
//				this.saveFile(fc.getSelectedFile(), encoding, Settings.getInstance().getExternalEditorLineEnding());
//	      this.fireFilenameChanged(this.getCurrentFileName());
//				lastDir = fc.getCurrentDirectory().getAbsolutePath();
//				if (this.editorType == SQL_EDITOR)
//				{
//					Settings.getInstance().setLastSqlDir(lastDir);
//				}
//				else
//				{
//					Settings.getInstance().setLastEditorDir(lastDir);
//				}
//				result = true;
//			}
//			catch (IOException e)
//			{
//				WbSwingUtilities.showErrorMessage(this, ResourceMgr.getString("ErrSavingFile") + "\n" + ExceptionUtil.getDisplay(e));
//				result = false;
//			}
//		}
//		return result;
//	}
//
//	public void saveFile(File aFile)
//		throws IOException
//	{
//		this.saveFile(aFile, this.fileEncoding, Settings.getInstance().getExternalEditorLineEnding());
//	}
//
//	public void saveFile(File aFile, String encoding)
//		throws IOException
//	{
//		this.saveFile(aFile, encoding, Settings.getInstance().getExternalEditorLineEnding());
//	}
//	
//	public void saveFile(File aFile, String encoding, String lineEnding)
//		throws IOException
//	{
//		if (encoding == null)
//		{
//			encoding = Settings.getInstance().getDefaultFileEncoding();
//		}
//
//		try
//		{
//			String filename = aFile.getAbsolutePath();
//			int pos = filename.indexOf('.');
//			if (pos < 0)
//			{
//				filename = filename + ".sql";
//				aFile = new File(filename);
//			}
//			
//			Writer writer = EncodingUtil.createWriter(aFile, encoding, false);
//			
//			int count = this.getLineCount();
//			
//			for (int i=0; i < count; i++)
//			{
//				String line = this.getLineText(i);
//				int len = StringUtil.getRealLineLength(line);
//				if (len > 0) writer.write(line, 0, len);
//				writer.write(lineEnding);
//			}
//			writer.close();
//			this.currentFile = aFile;
//			this.fileEncoding = encoding;
//			this.resetModified();
//		}
//		catch (IOException e)
//		{
//			LogMgr.error("EditorPanel.saveFile()", "Error saving file", e);
//			throw e;
//		}
//	}
//
//	public File getCurrentFile() { return this.currentFile; }

//	public String getCurrentFileEncoding()
//	{
//		if (this.currentFile == null) return null;
//		return this.fileEncoding;
//	}
//	public String getCurrentFileName()
//	{
//		if (this.currentFile == null) return null;
//		return this.currentFile.getAbsolutePath();
//	}
//
//	public CommentAction getCommentAction() { return this.commentAction; }
//	public UnCommentAction getUnCommentAction() { return this.unCommentAction; }

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (PropertyConstant.PROPERTY_SHOW_LINE_NUMBERS.equals(evt.getPropertyName()))
		{
			this.setShowLineNumbers(Setting.getInstance().getShowLineNumbers());
		}
		else if (PropertyConstant.PROPERTY_EDITOR_TAB_WIDTH.equals(evt.getPropertyName()))
		{
			this.setTabSize(Setting.getInstance().getEditorTabWidth());
		}
		validate();
		repaint();
	}

//	public void dragEnter(java.awt.dnd.DropTargetDragEvent dropTargetDragEvent)
//	{
//		if (this.isEditable())
//		{
//			dropTargetDragEvent.acceptDrag (DnDConstants.ACTION_COPY);
//		}
//		else
//		{
//			dropTargetDragEvent.rejectDrag();
//		}
//	}
//
//	public void dragExit(java.awt.dnd.DropTargetEvent dropTargetEvent)
//	{
//	}
//
//	public void dragOver(java.awt.dnd.DropTargetDragEvent dropTargetDragEvent)
//	{
//	}
//
//	public void drop(java.awt.dnd.DropTargetDropEvent dropTargetDropEvent)
//	{
//		try
//		{
//			Transferable tr = dropTargetDropEvent.getTransferable();
//			if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
//			{
//				dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY);
//				java.util.List fileList = (java.util.List)tr.getTransferData(DataFlavor.javaFileListFlavor);
//				if (fileList != null && fileList.size() == 1)
//				{
//					File file = (File)fileList.get(0);
//					if (this.canCloseFile() != YesNoCancelResult.cancel)
//					{
//						this.readFile(file);
//						dropTargetDropEvent.getDropTargetContext().dropComplete(true);
//					}
//					else
//					{
//						dropTargetDropEvent.getDropTargetContext().dropComplete(false);
//					}
//				}
//				else
//				{
//					dropTargetDropEvent.getDropTargetContext().dropComplete(false);
//					final Window w = SwingUtilities.getWindowAncestor(this);
//					EventQueue.invokeLater(new Runnable()
//					{
//						public void run()
//						{
//							w.toFront();
//							w.requestFocus();
//							WbSwingUtilities.showErrorMessageKey(w, "ErrNoMultipleDrop");
//						}
//					});
//				}
//			}
//			else
//			{
//				dropTargetDropEvent.rejectDrop();
//			}
//		}
//		catch (IOException io)
//		{
//			io.printStackTrace();
//			dropTargetDropEvent.rejectDrop();
//		}
//		catch (UnsupportedFlavorException ufe)
//		{
//			ufe.printStackTrace();
//			dropTargetDropEvent.rejectDrop();
//		}
//	}
//
//
//	public void dropActionChanged(java.awt.dnd.DropTargetDragEvent dropTargetDragEvent)
//	{
//	}

}
