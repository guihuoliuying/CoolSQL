/*
 * �������� 2006-6-8
 *
 */
package com.cattsoft.coolsql.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.text.BadLocationException;

import com.cattsoft.coolsql.action.common.SQLHistoryAction;
import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.action.sqleditormenu.AutoSelectAction;
import com.cattsoft.coolsql.bookmarkBean.BookmarkEvent;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkListener;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.bookmarkBean.DefaultBookmarkChangeEvent;
import com.cattsoft.coolsql.bookmarkBean.DefaultBookmarkChangeListener;
import com.cattsoft.coolsql.pub.component.BaseMenuManage;
import com.cattsoft.coolsql.pub.component.SplitButton;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.mouseEventProcess.PopupAction;
import com.cattsoft.coolsql.view.sqleditor.EditorPanel;
import com.cattsoft.coolsql.view.sqleditor.FormatSQLAction;
import com.cattsoft.coolsql.view.sqleditor.SqlEditorMenuManage;
import com.cattsoft.coolsql.view.sqleditor.SqlPanel;
import com.cattsoft.coolsql.view.sqleditor.action.CommitAction;
import com.cattsoft.coolsql.view.sqleditor.action.EditorViewSqlExecuteListener;
import com.cattsoft.coolsql.view.sqleditor.action.EnableAutoCommitAction;
import com.cattsoft.coolsql.view.sqleditor.action.RollbackAction;
import com.cattsoft.coolsql.view.sqleditor.pop.SelectedListener;

/**
 * @author liu_xlin sql���༭���
 */
public class SqlEditorView extends View {
	private static final long serialVersionUID = 1L;

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(SqlEditorView.class);
	/**
     * sql�༭���
     */
    private SqlPanel pane = null;

    /**
     * �˵�������
     */
    private BaseMenuManage menuManage = null;

    /**
     * ��ϰ�ť,����ִ��sql
     */
    private SplitButton runBtn = null;

    private BookmarkChangedListener listener = null;
    
    
	/**
	 * Action variant
	 */
	private CsAction commitAction;
	private CsAction rollbackAction;
    private CsAction enableAutoCommitAction;
	
    public SqlEditorView() {
        super();
        this.setTopTextIcon(PublicResource.getIcon("sqlEditorView.icon"));
        pane = new SqlPanel();
//        JScrollPane scroll = new JScrollPane(pane);
        this.add(pane, BorderLayout.CENTER);
        
        menuManage = new SqlEditorMenuManage(pane);
        pane.setPopupMenu(menuManage.getPopMenu());  //����Ҽ�˵�
        
        pane.getEditor().addMouseListener(new PopupAction(this));

        
        createIconButton();
        
    }
    /**
     * ����ͼ�갴ť
     *  
     */
    protected void createIconButton() {
        runBtn = new SplitButton(PublicResource
                .getIcon("sqlEditorView.iconbutton.run"));
        runBtn.addAction(new EditorViewSqlExecuteListener(this));
        runBtn.setToolTipText(PublicResource
                .getString("sqlEditorView.iconbutton.tooltip.run"));
        runBtn.addPropertyChangeListener(new BookmarkSelectListener());
        this.addComponentOnBar(runBtn); //sqlִ�а�ť
        //ͬʱ��Ҫ���Ѵ��ڵ���ǩ���м���
        listenBookmarks();

        //commitAction
        addIconButton(commitAction.getToolbarButton());
        
        //rollbackAction
        addIconButton(rollbackAction.getToolbarButton());
        
        
        //�鿴sqlִ�е���ʷ��¼
        this.addIconButton(PublicResource
                .getIcon("sqlEditorView.iconbutton.history"),
                new SQLHistoryAction(), PublicResource
                        .getString("sqlEditorView.iconbutton.history.tip"));

        this.addIconButton( Setting.getInstance().getShortcutManager()
				.getActionByClass(AutoSelectAction.class).getToolbarButton());
        this.addIconButton(Setting.getInstance().getShortcutManager()
				.getActionByClass(FormatSQLAction.class).getToolbarButton());
        //���sql�༭��ͼ������
        Action clearAction=new AbstractAction()
        {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				pane.getEditor().setText("");			
			}
        	
        };
        this.addIconButton(PublicResource
                .getIcon("logView.popmenu.icon.clearall"),
                clearAction, PublicResource
                        .getString("sqlEditorView.iconbutton.tooltip.clear")); //������б༭����
    }
    /**
     * �����Ѿ����ڵ���ǩ��ͬʱҲ������ǩ������
     *  
     */
    private void listenBookmarks() {
        listener = new BookmarkChangedListener();
        BookmarkManage manage = BookmarkManage.getInstance();
        manage.addBookmarkListener(listener); //����ǩ��������Ӽ�����
        manage.addDefaultBookmarkListener(new DefaultBookmarkChangeListener()
        {

			public void defaultChanged(DefaultBookmarkChangeEvent e) {
				 try {
                     runBtn.changeDefaultItem(e.getNewValue(), false);
                 } catch (UnifyException e1) {
                     LogProxy.errorReport(e1);
                 }
			}
        	
        }
        );
        Collection set = manage.getBookmarks();
        Iterator it = set.iterator();
        while (it.hasNext()) //Ϊÿ����ǩ��Ӽ�����
        {
            Bookmark bookmark = (Bookmark) it.next();
            bookmark.addPropertyListener(listener);
        }
    }

    /**
     * ��sql�༭��ͼ�и��µ�ǰѡ�����ǩ����
     * 
     * @param bookmark
     *            --��ǩ����
     */
    public void updateSelectedBookmark(Bookmark bookmark) {
        if (bookmark == null)
            return;
        String title = PublicResource.getString("sqlEditorView.title");
        title += "("
                + PublicResource
                        .getString("sqlEditorView.title.currentbookmark")
                + bookmark.getAliasName() + ")";
        this.setTopText(title);
    }
	protected void checkAutocommit(final Bookmark bookmark)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				if (bookmark != null)
				{
					// if autocommit is enabled, then rollback and commit will
					// be disabled
					boolean flag = bookmark.isConnected()&&!bookmark.isAutoCommit();
					commitAction.setEnabled(flag);
					rollbackAction.setEnabled(flag);
					
					((EnableAutoCommitAction)enableAutoCommitAction).setSelected(bookmark.isAutoCommit());
				}
			}
		});
	}
    /**
     * ��ѡ�еı༭���ݽ��н�������ѡ�����ݲ��Ϊ������sql��䣬�Ա���ִ��
     * 
     * @return
     */
    public List<String> getQueries() {
        return pane.getQueries();
    }

    /**
     * ��ȡ�༭����
     * 
     * @return
     */
    public String getEditorContent() {
        return pane.getEditor().getText();
    }

    /**
     * ���ñ༭������ı�����
     * 
     * @param content
     */
    public void setEditorContent(String content) {
        pane.getEditor().setText(content);
    }

    /**
     * ��ȡѡ�е��ı���Ϣ
     * 
     * @return
     */
    public String getSelectedText() {
        return StringUtil.trim(pane.getEditor().getSelectedText());
    }
    /**
     * ����ȱʡ��ǩ
     * @param bookmark  --�����õ�Ĭ����ǩ
     */
    public void setDefaultBookmark(Bookmark bookmark)
    {
        try {
            runBtn.changeDefaultItem(bookmark,false);
        } catch (UnifyException e) {
            LogProxy.errorReport(e);
        }
    }

    /*
     * ���� Javadoc��
     * 
     * @see src.view.View#getName()
     */
    public String getName() {
        return stringMgr.getString("view.sqleditor.title");
    }

    /*
     * ���� Javadoc��
     * 
     * @see src.view.Display#dispayInfo()
     */
    public void dispayInfo() {

    }

    /*
     * ���� Javadoc��
     * 
     * @see src.view.Display#popupMenu()
     */
    public void popupMenu(int x, int y) {

//        menuManage.getPopMenu().show(pane, x, y);
    }

    public EditorPanel getEditorPane() {
        return pane.getEditor();
    }
    public void autoSelect()
    {
    	try {
			pane.autoSelect();
		} catch (BadLocationException e) {
			LogProxy.errorLog("",e);
		}
    }
    /**
     * Find specified text in the sql editor
     */
    public void toFind()
    {
    	pane.toFind();
    }
    /**
     * 
     * @author liu_xlin ѡ�������ֵ�󣬽�ѡ�е�ֵ�ڱ༭������ʾ
     */
    private class EntitySelectListener implements SelectedListener {

        /*
         * (non-Javadoc)
         * 
         * @see com.coolsql.view.sqleditor.pop.SelectedListener#selected(java.lang.Object)
         */
        public void selected(Object value) {
			int[] range = pane
					.getWordRange(pane.getEditor().getCaretPosition() - 1);
			if (range != null && range.length == 2) {
				int len = range[1] - range[0];
				if (len < 1)
					len = 0;
				else
					len++;
				pane.getEditor().setSelectionStart(range[0]);
				pane.getEditor().setSelectionEnd((range[1]));
				pane.getEditor().setSelectedText(value.toString());
			}
		}

    }

    /**
	 * 
	 * @author liu_xlin ��ǩѡ���������ʵʱ����sql�༭��ͼ�ı�����Ϣ
	 */
    private class BookmarkSelectListener implements PropertyChangeListener {

        /*
         * ���� Javadoc��
         * 
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("selectedData")) //��ǩ���ѡ��ı���
            {
                Object newOb = evt.getNewValue();
                if (newOb != null && newOb instanceof Bookmark) {
                    updateSelectedBookmark((Bookmark) newOb);
                    checkAutocommit((Bookmark) newOb);
                }
            }

        }

    }

    /**
     * 
     * @author liu_xlin ����ǩ����������ǩ�ļ���
     */
    private class BookmarkChangedListener implements BookmarkListener,
            PropertyChangeListener {

        /**
         * ������ǩʱ������ϰ�ť��Ӳ˵���
         * 
         * @see com.coolsql.bookmarkBean.BookmarkListener#bookmarkAdded(com.coolsql.bookmarkBean.BookmarkEvent)
         */
        public void bookmarkAdded(BookmarkEvent e) {
            Bookmark bookmark = (Bookmark) e.getBookmark();
            bookmark.addPropertyListener(this);
            try {
                runBtn
                        .addDataItem(bookmark.getAliasName(), BookMarkPubInfo.
                        		getBookmarkIcon(bookmark.isConnected()),
                                bookmark);
            } catch (UnifyException e1) {
                LogProxy.messageReport(e1.getMessage(), 0);
            }

        }

        /**
         * ��ǩɾ������Ӧ�Ĳ˵�ɾ��
         * 
         * @see com.coolsql.bookmarkBean.BookmarkListener#bookmarkDeleted(com.coolsql.bookmarkBean.BookmarkEvent)
         */
        public void bookmarkDeleted(BookmarkEvent e) {
            Bookmark bookmark = (Bookmark) e.getBookmark();
            bookmark.removePropertyListener(this);
            runBtn.deleteDataItem(bookmark);
        }

        /*
         * ���� Javadoc��
         * 
         * @see com.coolsql.bookmarkBean.BookMarkListener#bookMarkUpdated(com.coolsql.bookmarkBean.BookMarkEvent)
         */
        public void bookMarkUpdated(BookmarkEvent e) {
        }

        /**
         * ������ϰ�ť�Ĳ˵���ǩ
         * 
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            if (name.equals("aliasName")) //�����ǩ�����˱���仯��������ʵ��Ĵ���
            {
                runBtn.updateItemByData(evt.getSource(), (String) evt
                        .getNewValue(), null);
            } else if (name.equals(Bookmark.PROPERTY_CONNECTED)) //�����ǩ���������¼���������ϰ�ť��ȱʡ�˵���
            {
                boolean newValue = ((Boolean) evt.getNewValue()).booleanValue();

                runBtn.updateItemByData(evt.getSource(), 
                		((Bookmark)evt.getSource()).getAliasName(), BookMarkPubInfo.getBookmarkIcon(newValue));
                if(evt.getSource()==runBtn.getSelectData())
                {
                	checkAutocommit((Bookmark)evt.getSource());
                }
            }else if (name.equals(Bookmark.PROPERTY_AUTOCOMMIT)) 
            {
            	checkAutocommit((Bookmark)evt.getSource());
            }
        }

    }
    public void clearTrackEntity()
    {
    	pane.clearTrack();
    }
    /* (non-Javadoc)
     * @see com.coolsql.view.View#createActions()
     */
    protected void createActions() {
		commitAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(CommitAction.class);
		rollbackAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(RollbackAction.class);
		enableAutoCommitAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(EnableAutoCommitAction.class);
    }

	/* (non-Javadoc)
	 * @see com.coolsql.view.View#doAfterMainFrame()
	 */
	@Override
	public void doAfterMainFrame() {
		pane.registerCtrlListener();
		
	}
}
