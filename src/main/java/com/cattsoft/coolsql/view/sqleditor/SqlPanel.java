package com.cattsoft.coolsql.view.sqleditor;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.pub.display.DelimiterDefinition;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.IAdditionalPainter;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.ScriptCommandDefinition;
import com.cattsoft.coolsql.system.ActionCollection;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.action.TrackEntityAction;
import com.jidesoft.swing.Searchable;
import com.jidesoft.swing.SearchableBar;

/**
 *Wrap the sqleditor, providing some useful actions,such as formating, entity tracking, and popup menu .
 * @author kenny liu
 *
 * 2007-12-4 create
 */
public class SqlPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private EditorPanel editor=null; //Sql editor panel
	
	private boolean isInSqlPanel=false; //This variable indicates whether mouse is in sql panel or not.
	private TrackPainter tp=null; //Additional painter of TextAreaPainter,used to display the text tracked 
	
	
	/**
	 * Used to switch the cursor of textarea in different condition. 
	 */
	private Cursor handCursor=new Cursor(Cursor.HAND_CURSOR);
	private Cursor oldCursor=null;//the origianl cursor of textarea
	
	private ScriptParser sqlParser;
	
	private SearchableBar editorSearchBar;
	public SqlPanel()
	{
		editor=EditorPanel.createSqlEditorWithPrompt();
		editor.getPainter().addMouseListener(new EntityTrackMouseListener());
		this.setLayout(new BorderLayout());
		add(editor,BorderLayout.CENTER);
		
		editor.getPainter().addMouseMotionListener(new EntityTrackMouseMotionListener());
		tp=new TrackPainter();
		editor.getPainter().setAdditionalPainter(tp);
		
		editor.getDocument().addDocumentListener(new DocumentListener()
		{

			public void changedUpdate(DocumentEvent e) {
			}

			public void insertUpdate(DocumentEvent e) {
				updateActionEnable();
			}

			public void removeUpdate(DocumentEvent e) {		
				updateActionEnable();
			}
			private void updateActionEnable()
			{
				CsAction undoAction=Setting.getInstance().getShortcutManager().
				getActionByClass(com.cattsoft.coolsql.system.menu.action.UndoMenuAction.class);
				CsAction redoAction=Setting.getInstance().getShortcutManager().
				getActionByClass(com.cattsoft.coolsql.system.menu.action.RedoMenuAction.class);
				redoAction.setEnabled(editor.getDocument().canRedo());
				undoAction.setEnabled(editor.getDocument().canUndo());
			}
			
		}
		);
		Action promptAction=editor.getShowPromptPopAction();
		if(promptAction instanceof CsAction)
		{
			CsAction showPromptAciton=(CsAction)promptAction;
			showPromptAciton.setMenuText(PublicResource
					.getString("TextEditor.popmenu.showtip"));
			showPromptAciton.setIcon( PublicResource
				.getIcon("TextEditor.popmenu.showtip.icon"));
			Setting.getInstance().getShortcutManager().registerAction(showPromptAciton);
		}
		editor.addCaretListener(new CaretListener()
		{

			public void caretUpdate(CaretEvent e) {
				int offset=editor.getCaretPosition();
				int row=editor.getLineOfOffset(offset);
				int column=editor.getCaretPositionInLine(row);
				row++;
				column++;
				GUIUtil.updateSystemStatusBarForSQLEditor(row+":"+column);
			}
		}
		);
		sqlParser=new ScriptParser();
		Setting.getInstance().addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent evt) {
				sqlParser.getDelimiter().setDelimiter(Setting.getInstance().getProperty(
					PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_DELIMITER,
					DelimiterDefinition.STANDARD_DELIMITER.getDelimiter()));
			}
		}
		
		, PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_DELIMITER);
		
		//install find bar
		final Searchable searchable = new SqlEditorSearchable(editor);
        searchable.setRepeats(true);
        editorSearchBar=SearchableBar.install(searchable, KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK), new SearchableBar.Installer() {
        	boolean isBarVisible=false;
            public void openSearchBar(SearchableBar searchableBar) {
            	if(isBarVisible)
            		return;
            	
                add(searchableBar, BorderLayout.SOUTH);
//                searchable.installListeners();
                isBarVisible=true;
                invalidate();
                revalidate();
            }

            public void closeSearchBar(SearchableBar searchableBar) {
                remove(searchableBar);
                isBarVisible=false;
//                searchable.uninstallListeners();
                invalidate();
                revalidate();
            }
        });
        editorSearchBar.setVisibleButtons(SearchableBar.SHOW_CLOSE|
        		SearchableBar.SHOW_NAVIGATION|
        		SearchableBar.SHOW_MATCHCASE|
        		SearchableBar.SHOW_REPEATS|
        		SearchableBar.SHOW_STATUS);
	}
	public void clearTrack()
	{
		if(tp!=null)
		{
			tp.stopPaint();
		}
	}
	public void toFind()
	{
		editorSearchBar.getInstaller().openSearchBar(editorSearchBar);
		editorSearchBar.focusSearchField();
	}
	/**
	 * register a key listener that listen to releasing of ctrl key on mainframe
	 *
	 */
	public void registerCtrlListener()
	{
		Action action=new AbstractAction()
		{
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				if(isInSqlPanel)
				{
					tp.stopPaint();
					editor.getPainter().repaint();		
				}
			}
		}	
		;
		KeyStroke stroke=KeyStroke.getKeyStroke("released CONTROL");
//		stroke.isOnKeyRelease()
		JFrame frame=GUIUtil.getMainFrame();
		JPanel pane=(JPanel)frame.getContentPane();
		pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				stroke, "ctrl released");
		pane.getActionMap().put("ctrl released",action);
	}
	/**
	 * ��ݹ������λ�ã�ѡ����Ч��sql���
	 * sql��ȡ����:�Էֺŷָ�
	 * @throws BadLocationException
	 */
	public void autoSelect() throws BadLocationException
	{
		int curPosition=editor.getCaretPosition();
		sqlParser.setScript(editor.getText());
		int index=sqlParser.getCommandIndexAtCursorPos(curPosition);
		ScriptCommandDefinition scd=sqlParser.getCommandDefinition(index);
		sqlParser.done();
		if(scd==null)
			return;
		editor.setSelectionStart(scd.getStartPositionInScript());
		editor.setSelectionEnd(scd.getEndPositionInScript());
	}
	public List<String> getQueries() {
		String content=editor.getSelectedText();
		if(content==null||content.equals(""))
			return new ArrayList<String>();
		
		sqlParser.setScript(content);
		List<String> list=new ArrayList<String>(sqlParser.getSize());
		@SuppressWarnings("unchecked")
		Iterator it=sqlParser.getIterator();
		while(it.hasNext())
		{
			list.add((String)it.next());
		}
		sqlParser.done();
        return list;
    }
	/**
	 * ��ȡ�༭��������ĵ�����
	 * @return
	 */
	public Document getEditorDocument()
	{
		return editor.getDocument();
	}
	public EditorPanel getEditor()
	{
		return editor;
	}
	/**
	 * Ϊsql�������Ҽ�˵�
	 * @param pop
	 */
	public void setPopupMenu(JPopupMenu pop)
	{
		editor.setRightClickPopup(pop);
	}
    /**
     * ��ȡָ��λ�õ��ַ�
     */
    private String getPositionChar(int offset) {
		String str = "";
		str = editor.getText(offset, 1);

		return str;
	}
    /**
	 * �Ӹ��ƫ��λ����ǰ��������������ո񡢻س������С�tab�ȷ��ʱ���ص�ǰ��λ��
	 * 
	 * @param offset
	 *            --���ƫ����
	 * @return --��ǰ�����������ĵ�һ��whitespace��ŵ�λ��
	 */
    public int getWordStart(int offset)
    {
        String str = "";
        if (offset < 0)
            return 0;

        str = getPositionChar(offset);
        if(StringUtil.isSpaceString(str)||str.equals(","))
        {
            return offset+1;
        }else
            return getWordStart(offset-1);
    }
    public int getWordEnd(int offset)
    {
        String str = "";
        if (offset >= editor.getDocumentLength()-1)
            return offset;
        
        offset++;
        str = getPositionChar(offset);//ȡ��һ���ַ�
        if(StringUtil.isSpaceString(str)||str.equals(","))
        {
            return offset-1;
        }else
            return getWordEnd(offset+1);
    }
    /**
     * ��ȡ��ƫ��λ���µ������ַ����βλ��
     * @param offset --��ƫ��λ��
     * @return  --�����ַ����βλ��
     */
    public int[] getWordRange(int offset)
    {
        int[] range=new int[2];
        range[0]=getWordStart(offset);
        range[1]=getWordEnd(offset);
        
        return range;
    }
    /**
     * ��ȡ��λ���ϵ��ַ�ֵ
     * @param offset  --ƫ����
     * @return  --��λ���ϵ��ַ�ֵ
     */
    public String getStringOnOffset(int offset)
    {
        String before = getBackforwardString(offset-1);
        return before;
    }
    /**
     * �뷽��getBeforeBlankString���ƣ������ˡ�,����Ϊ�ָ���
     * @param offset
     * @return
     */
    private String getBackforwardString(int offset) {
        String str = "";
        if (offset < 0)
            return "";

        str = getPositionChar(offset);
        if (StringUtil.isSpaceString(str)||str.equals(","))
            return "";

        String r = getBackforwardString(offset - 1);
        return r + str;
    }
    private boolean isCanTrack=false;
    private class EntityTrackMouseListener extends MouseAdapter
    {
    	@Override
    	public void mouseClicked(MouseEvent e)
    	{
            if (isCanTrack&&SwingUtilities.isLeftMouseButton(e)&&e.isControlDown()) {
                
                final Action a = 
                	ActionCollection.getInstance().get(TrackEntityAction.class);
                GUIUtil.processOnSwingEventThread(new Runnable() {
                    public void run() {
                        a.actionPerformed(new ActionEvent(this, 1, "ViewObjectAtCursorInObjectTreeAction"));
                    }
                });
            }
    	}
    	@Override
    	public void mouseExited(MouseEvent e)
    	{
    		isInSqlPanel=false;
    		if(e.isControlDown())
    		{
    			tp.stopPaint();
    			editor.getPainter().repaint();
    		}
    	}
    	@Override
    	public void mouseEntered(MouseEvent e)
    	{
    		isInSqlPanel=true;
    	}
    }
    /**
     *Mousemotion listener that listens to mouse motion on textareapainter.
     *make a reuqest for repainting if ctrl key is typed. 
     * @author ��Т��(kenny liu)
     *
     * 2008-3-9 create
     */
    public class EntityTrackMouseMotionListener extends MouseMotionAdapter
    {
    	public void mouseMoved(MouseEvent e)
    	{
    		if(e.isControlDown())
    		{
    			int x=e.getX()-editor.getPainter().getGutterWidth();
    			int y=e.getY();
    			int offset=editor.xyToOffset(x, y);
    			isCanTrack=processMove(offset,e);
    			if(isCanTrack)
    			{
    				handCursor();
    				tp.startPaint();
    			}else
    			{
    				oldCursor();
    				tp.stopPaint();
    			}
    			editor.getPainter().repaint();
    		}else
        	{
        		tp.stopPaint();
        	}
    			
    	}
    	private boolean processMove(int offset,MouseEvent e)
    	{
    		Bookmark bookmark =BookmarkManage.getInstance().getDefaultBookmark();
    		String delimiter = Setting.getInstance().getDatabaseSetting(
    				bookmark.getAliasName()).getSQLStatementSeparator();
    		String text=editor.getText();
    		char delimiterChar = delimiter.charAt(0);
    		if(text.length()<=offset)
    		{
    			return false;
    		}
    		int lastIndexOfText = Math.max(0, text.length() - 1);
    		int beginPos = Math.min(offset, lastIndexOfText); // The Math.min is
    															// for the Caret at
    															// the end of the
    															// text
    		beginPos=editor.getLeftPositionOfWord(beginPos,delimiterChar);
    		int endPos=editor.getRightPositionOfWord(offset,delimiterChar);
    		
    		if(beginPos==endPos)
    		{
    			return false;
    		}
    		beginPos=Math.min(beginPos,text.length()-1);
    		
    		int line=editor.yToLine(e.getY());
    		int lineStartOffset=editor.getLineStartOffset(line);
    		
    		int gutterwidth=editor.getPainter().getGutterWidth();
    		if(text.charAt(beginPos)==' '&&beginPos!=offset)
    			beginPos++;
    		
    		int xS=editor.offsetToX(line, beginPos-lineStartOffset)+gutterwidth;
    		int xE=editor.offsetToX(line, endPos-lineStartOffset)+gutterwidth;
    		
    		int y=editor.lineToY(line+1)+2;
    		tp.setBound(xS, xE, y);
    		
    		tp.startPaint();
    		editor.getPainter().repaint();
    		
    		return true;
    	}
    }
	private void handCursor()
	{
		Cursor cursor=editor.getPainter().getCursor();
		if(cursor!=handCursor)
		{
			if(oldCursor==null)
				oldCursor=cursor;
			editor.getPainter().setCursor(handCursor);
		}
	}
	private void oldCursor()
	{
		Cursor cursor=editor.getPainter().getCursor();
		if(cursor==handCursor)
			editor.getPainter().setCursor(oldCursor);
	}
    /**
     * It will paint display needed additionally.
     * draw a line at the bottom of text tracked.
     * @author ��Т��(kenny liu)
     *
     * 2008-3-9 create
     */
    private class TrackPainter implements IAdditionalPainter
    {
    	private boolean isNeedPaint=false;
    	private int xS,xE,y;
//    	private int width,height;
		/* (non-Javadoc)
		 * @see com.coolsql.pub.display.IAdditionalPainter#paint(java.awt.Graphics)
		 */
		public void paint(Graphics g) {
			if(!isNeedPaint)
				return;
			if(xS==xE)
				return;
			g.drawLine(xS, y, xE, y);
		}
    	public void setBound(int xS,int xE,int y)
    	{
    		this.xS=xS;
    		this.xE=xE;
    		this.y=y;
    	}
    	public void startPaint()
    	{
    		if(!isNeedPaint)
    			isNeedPaint=true;
    	}
    	public void stopPaint()
    	{
    		if(isNeedPaint)
    			isNeedPaint=false;
    		if(editor.getPainter().getCursor()==handCursor)
				editor.getPainter().setCursor(oldCursor);
    	}
    }
}
