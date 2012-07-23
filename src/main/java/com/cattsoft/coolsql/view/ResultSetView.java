/*
 * �������� 2006-6-8
 */
package com.cattsoft.coolsql.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.action.framework.CheckCsAction;
import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.component.BasePopupMenu;
import com.cattsoft.coolsql.pub.component.IconButton;
import com.cattsoft.coolsql.pub.component.MyTabbedPane;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.PopMenuMouseListener;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.DataSetTable;
import com.cattsoft.coolsql.view.resultset.HeaderMouseListener;
import com.cattsoft.coolsql.view.resultset.ResultDisplayPopMenuManage;
import com.cattsoft.coolsql.view.resultset.TableHeaderMenuManage;
import com.cattsoft.coolsql.view.resultset.action.AddNewDataAction;
import com.cattsoft.coolsql.view.resultset.action.CancelDeleteRowsAction;
import com.cattsoft.coolsql.view.resultset.action.ChangeStatusOfDataSetPanelAction;
import com.cattsoft.coolsql.view.resultset.action.CopyAsSqlInsertAction;
import com.cattsoft.coolsql.view.resultset.action.DeleteRowsAction;
import com.cattsoft.coolsql.view.resultset.action.EditInDialogAction;
import com.cattsoft.coolsql.view.resultset.action.NextPageProcessAction;
import com.cattsoft.coolsql.view.resultset.action.PrePageProcessAction;
import com.cattsoft.coolsql.view.resultset.action.QueryAllRowsAction;
import com.cattsoft.coolsql.view.resultset.action.RefreshQueryAction;
import com.cattsoft.coolsql.view.resultset.action.RestoreCellAction;
import com.cattsoft.coolsql.view.resultset.action.SaveChangeToDBAction;
import com.cattsoft.coolsql.view.resultset.action.ShowColumnSelectDialogAction;
import com.cattsoft.coolsql.view.resultset.action.UpdateRowActioin;

/**
 * The view that display the result of executing including querying and updating operation.
 * Meanwhile this view supports modifying the result data and saving into database.
 * @author liu_xlin
 */
public class ResultSetView extends TabView {

	private static final long serialVersionUID = 1L;
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(ResultSetView.class);
	
	public static final String DataTable = "dataTable";

    /**
     * tab���������չʾ��ѯ������
     */
    private MyTabbedPane tab = null;

    private ResultDisplayPopMenuManage menuManage = null;

    private TableHeaderMenuManage headerMenu = null;

    /**
     * �˵�����
     */
    private DataTableMouseListener popMenuListener = null;

    //��ͷ������Ҽ����,���ڲ˵��ĵ���
    private HeaderMouseListener headerPopMenuListener = null;

    /**
     * ͼ�갴ť����
     */
    private CsAction prePageAction;

    private CsAction nextPageAction;

    private CsAction queryAllAction;
    
    private CsAction changeStatusAction;
    
    private CsAction restoreCellAction;
    
    private CsAction deleteRowAction;
    
    private CsAction cancelDeleteRowAction;
    
    private CsAction showColumnSelectDialogAction;
    
    private CsAction saveChangeToDBAction;


    private IconButton removeSelectedTab = null; //ɾ����ѡtabҳ

    private IconButton removeAllTab = null; //ɾ�����е�tabҳ


    private CsAction copyAsInsertSQLAction;
    private CsAction updateRowAction;
    private CsAction refreshQueryAction;
    
    private CsAction editInDialogAction;
    
    private ResultSetOfDataPaneListener dataPaneListener = null; //���������

    /**
     * ������¼tab�ؼ��ĸı��¼��Ƿ���ɾ���¼��������tab�¼�
     */
    private boolean isRemoveOrAdd = false;

    private DataSetTableSelectionListener dataSetTableSelectionListener;
    public ResultSetView() {
        super();
        this.setTopTextIcon(PublicResource.getIcon("resultsetView.icon"));
        
        createActions();
        tab = new MyTabbedPane(JTabbedPane.TOP) {
			private static final long serialVersionUID = 1L;

			/**
             * ��дremoveTabAt����������������ϵĽ����ݵļ����¼�
             */
        	@Override
            public void removeTabAt(int index) {
                /**
                 * ɾ��������¼�
                 */
                Component com = getSelectedComponent();

                /**
                 * ���������ļ�����
                 */
                if (com instanceof DataSetPanel) {
                    DataSetPanel dataPane = (DataSetPanel) com;

                    dataPane.removePanelPropertyChangeListener(dataPaneListener); //�ü����ǶԹ��߰�ť�����ԵĴ���,��������Ƴ���,�������ü���
                    if (dataPane.isRemoving()) { //����ɾ��
//                        clearMenuProperty(dataPane);
                        dataPane.removeComponent();
                    }
                }

                setRemoveOrAddFlag(true);

                super.removeTabAt(index);

                checkButtonAvailable();
            }
        };
        menuManage = new ResultDisplayPopMenuManage(tab); //�����ͼ���Ҽ���˵�
        headerMenu = new TableHeaderMenuManage(null); //���չʾ��ؼ���ͷ���Ҽ�˵�������

        popMenuListener = new DataTableMouseListener(); //���ڱ�ؼ��������
        headerPopMenuListener = new HeaderMouseListener(); //��ͷ�Ҽ���˵�����
        dataPaneListener = new ResultSetOfDataPaneListener();

        this.setContent(tab); //���Զ���tab�ؼ���Ϊ�����ͼ����Ҫ����
        tab.setHeadPopMenu(createTabHeadMenu());

        createIconButton(); //���ͼ�갴ť

        tab.addChangeListener(new TabChangeListener()); //���tab�仯�¼�

        dataSetTableSelectionListener=new DataSetTableSelectionListener();
        checkButtonAvailable();//��ʼ������һ�ΰ�ť������У��
    }

    protected void createActions() {
		prePageAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(PrePageProcessAction.class);
		nextPageAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(NextPageProcessAction.class);
		queryAllAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(QueryAllRowsAction.class);

		copyAsInsertSQLAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(CopyAsSqlInsertAction.class);
		updateRowAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(UpdateRowActioin.class);
		refreshQueryAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(RefreshQueryAction.class);
		changeStatusAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(ChangeStatusOfDataSetPanelAction.class);
		
		deleteRowAction = Setting.getInstance().getShortcutManager()
		.getActionByClass(DeleteRowsAction.class);
		
		cancelDeleteRowAction = Setting.getInstance().getShortcutManager()
		.getActionByClass(CancelDeleteRowsAction.class);
		
		restoreCellAction = Setting.getInstance().getShortcutManager()
		.getActionByClass(RestoreCellAction.class);
		
		showColumnSelectDialogAction = Setting.getInstance().getShortcutManager()
		.getActionByClass(ShowColumnSelectDialogAction.class);
		changeStatusAction.addPropertyChangeListener(new PropertyChangeListener()
		{

			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals(CheckCsAction.SELECT_STATUS)||evt.getPropertyName().equals("enabled"))
				{
					if(changeStatusAction.isEnabled()) {
						showColumnSelectDialogAction.setEnabled(((CheckCsAction)changeStatusAction).isSelected());
					} else {
						showColumnSelectDialogAction.setEnabled(false);
					}
					
					if (changeStatusAction.isEnabled() && ((CheckCsAction)changeStatusAction).isSelected()) {
						Component component = getDisplayComponent();
				    	if(component instanceof DataSetPanel) {
				    		DataSetPanel dsPanel=(DataSetPanel)component;
				    		validateDeleteButton(dsPanel);
				    	} else {
				    		disableDeleteAction();
				    	}
					} else {
						disableDeleteAction();
					}
				}
			}
			
		}
		);
		
		editInDialogAction= Setting.getInstance().getShortcutManager()
		.getActionByClass(EditInDialogAction.class);
		
		saveChangeToDBAction= Setting.getInstance().getShortcutManager()
		.getActionByClass(SaveChangeToDBAction.class);
	}
    /**
     * �������չʾtab�ؼ���ҳͷ�Ҽ�˵�
     * 
     * @return
     */
    private JPopupMenu createTabHeadMenu() {
        JPopupMenu menu = new BasePopupMenu();
        JMenuItem shut = new JMenuItem(PublicResource
                .getString("resultView.tab.headpopmenu.delete"));
        shut.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeCurrentTab();
            }

        });
        menu.add(shut);
        return menu;
    }
    public void installDataSetTableSelectionListener(DataSetTable table)
    {
    	if(table==null)
    		return;
    	
    	table.getSelectionModel().addListSelectionListener(dataSetTableSelectionListener);
    	table.getColumnModel().getSelectionModel().addListSelectionListener(dataSetTableSelectionListener);
    }
    /**
     * ����ͼ�갴ť
     *  
     */
    @SuppressWarnings("serial")
	private void createIconButton() {
    	
        addIconButton(prePageAction.getToolbarButton());
        addIconButton(nextPageAction.getToolbarButton());
        addIconButton(queryAllAction.getToolbarButton());

        addIconButton(refreshQueryAction.getToolbarButton());

        addIconButton(deleteRowAction.getToolbarButton());
        
        addIconButton(restoreCellAction.getToolbarButton());
        
        addIconButton(editInDialogAction.getToolbarButton());
        
        addIconButton(showColumnSelectDialogAction.getToolbarButton());
        
        addIconButton(saveChangeToDBAction.getToolbarButton());
        
        this.addIconButton(Setting.getInstance().getShortcutManager()
			.getActionByClass(AddNewDataAction.class).getToolbarButton());
        
        /**
         * ����ɾ��ѡ��ҳ���¼�����
         */
        Action removeTabListener = new PublicAction() {

            /**
             * ɾ��ѡ�е�tabҳ
             */
            public void actionPerformed(ActionEvent e) {

                removeCurrentTab();
            }

        };
        removeSelectedTab = this
                .addIconButton(
                        PublicResource
                                .getIcon("resultView.iconbutton.removeselected.icon"),
                        removeTabListener,
                        PublicResource
                                .getString("resultView.iconbutton.removeselected.tooltip")); //ɾ��ѡ��ҳ��ť

        KeyStroke stroke = KeyStroke.getKeyStroke("DELETE");
        tab.getInputMap().put(stroke, "DELETE");
        tab.getActionMap().put("DELETE", removeTabListener);

        /**
         * ����ɾ��ѡ��ҳ���¼�����
         */
        Action removeAllListener = new PublicAction() {

            public void actionPerformed(ActionEvent e) {

                removeAllTab();
            }

        };
        removeAllTab = this.addIconButton(PublicResource
                .getIcon("resultView.iconbutton.removeall.icon"),
                removeAllListener, PublicResource
                        .getString("resultView.iconbutton.removeall.tooltip")); //ɾ������ҳ��ť
    }

    /**
     * ɾ��ǰѡ�е�tabҳ
     *  
     */
    public void removeCurrentTab() {
        int index = tab.getSelectedIndex();
        Component com = tab.getSelectedComponent();
        if (com instanceof DataSetPanel) {
            ((DataSetPanel) com).setRemoving(true);
        }
        tab.remove(index);
    }

    /**
     * ɾ������tabҳ
     *  
     */
    public void removeAllTab() {
        for (int i = tab.getTabCount() - 1; i > -1; i--) {
            Component com = tab.getComponentAt(i);
            if (com instanceof DataSetPanel) {
                ((DataSetPanel) com).setRemoving(true);
            }
            tab.remove(i);
        }
//        tab.removeAll();//ɾ������tabҳ
    }

    /**
     * ��Ϊ��������ʾ�б���Ҽ�˵��п��ܱ������������������á� �÷����ڳ���ɾ��������ʱ����
     * 
     * @param dataTable -- Table component in the DataSetPane.
     */
    public void clearMenuProperty(JTable dataTable) {

        //���Ҽ�˵���������п��ܱ����˸���������ã���������˽�����ɾ��
        Component content = dataTable;
        if (content instanceof JScrollPane)
            content = ((JScrollPane) content).getViewport().getView();

        if (content == menuManage.getNoRenderMenu().getClientProperty(
        		DataTable)) {
            menuManage.getNoRenderMenu().putClientProperty(DataTable, null); //ɾ��������ݵ�����
        }
        if (content == menuManage.getComponent()) {
            menuManage.setComponent(null);
        }

    }

    /**
     * ��ָ���ı�ؼ��������Ҽ�������¼�
     * 
     * @param table
     */
    public void addListenerToTable(JTable table) {
        table.addMouseListener(popMenuListener);
    }

    /**
     * ɾ��ָ���ı�ؼ�������Ҽ�������¼�
     * 
     * @param table
     */
    public void removeListenerFromTable(JTable table) {
        table.removeMouseListener(popMenuListener);
    }

    /**
     * ��ӿɹر�tabҳ����Ҫ���sqlִ��ʱ��Ϣ��չʾ��������ӵ�ҳ����֮����н�����ʾ�ĵ���
     * 
     * @param BookMark
     * @return
     */
    public DataSetPanel addTab(Bookmark bookmark) {
        DataSetPanel pane = new DataSetPanel(bookmark);
        this.setRemoveOrAddFlag(true);
        getResultTab().addCloseTab(bookmark.getAliasName(), pane);
        pane.addPanelPropertyChangeListener(dataPaneListener);
        return pane;
    }

    /**
     * ��ӿɹر����,�����ӵ����ΪDataSetPanel���ͣ���ô�����Բ���:title,tip
     * 
     * @param com
     *            --����ӵ����
     * @param title
     *            --ҳ����
     * @param tip
     *            --��ʾ��Ϣ
     */
    public void addTab(JComponent com, String title, String tip) {
        if (com == null)
            return;
        if (com instanceof DataSetPanel) {
            DataSetPanel pane = (DataSetPanel) com;

            getResultTab().addCloseTab(pane.getBookmark().getAliasName(), pane,
                    pane.getSql());
        } else {
            getResultTab().addCloseTab(title, com, tip);
        }
    }

    /**
     * ��tab�����ɾ�������
     * 
     * @param com
     *            --��ɾ������
     */
    public synchronized void removeTab(JComponent com) {
        if (com == null)
            return;
        getResultTab().remove(com);
    }

    /**
     * ��ȡ��������ļ����¼�
     * 
     * @return
     */
    public ResultSetOfDataPaneListener getResultSetListener() {
        return dataPaneListener;
    }

    /**
     * ��ȡ�����tab�ؼ��е�����λ��
     * 
     * @param com
     * @return
     */
    public int getPositionByComponent(JComponent com) {
        return tab.indexOfComponent(com);
    }

    /**
     * ����tab�ؼ�ָ������ҳ�ı���
     * 
     * @param index
     * @param title
     */
    public void setTabTitle(int index, String title) {
        tab.setTitleAt(index, title);
    }

    /**
     * ѡ��ָ�������
     * 
     * @param com
     */
    public void setSelectedTab(JComponent com) {
        tab.setSelectedComponent(com);
    }
    /**
     * ��ȡtab����е�ǰ��ʾ�����
     * @return 
     */
    public JComponent getDisplayComponent()
    {
    	return (JComponent)tab.getSelectedComponent();
    }
    /*
     * ���� Javadoc��
     * 
     * @see src.view.View#getName()
     */
    public String getName() {

        return stringMgr.getString("view.resultset.title");
    }

    /*
     * ���� Javadoc��
     * 
     * @see src.view.Display#dispayInfo()
     */
    public void dispayInfo() {

    }

    /*
     * 
     * @see src.view.Display#popupMenu()
     */
    public void popupMenu(int x, int y) {
    }

    public MyTabbedPane getResultTab() {
        return tab;
    }

    /**
     * У�鰴ť�Ŀ�����
     *  
     */
    public void checkButtonAvailable() {
    	
    	GUIUtil.processOnSwingEventThread(new Runnable() {
			public void run() {
				updateStatus();
				/**
				 * ����tab�ؼ��Ƿ�������
				 */
				if (tab.getTabCount() < 1) // ���û��tabҳ,����Ӧ�İ�ť��Ϊ������
				{
					prePageAction.setEnabled(false);
					nextPageAction.setEnabled(false);
					GUIUtil.setComponentEnabled(false, removeSelectedTab);
					GUIUtil.setComponentEnabled(false, removeAllTab);
					queryAllAction.setEnabled(false);
					copyAsInsertSQLAction.setEnabled(false);
					restoreCellAction.setEnabled(false);
					editInDialogAction.setEnabled(false);
					updateRowAction.setEnabled(false);
					refreshQueryAction.setEnabled(false);
				} else {

					JComponent com = (JComponent) tab.getSelectedComponent();
					if (com instanceof DataSetPanel) { // ���ж�����ֻ����Խ����ݱ༭��ť
						DataSetPanel dataPane = (DataSetPanel) com;
						validateDeleteButton(dataPane);
						
						if (!dataPane.isReady()) { // ���������δ׼����,����ǰ����,�Լ���ѯ���С�ˢ�°�ť��Ϊ������
							prePageAction.setEnabled(false);
							nextPageAction.setEnabled(false);
							queryAllAction.setEnabled(false);
							// GUIUtil.setComponentEnabled(false, refresh);
							refreshQueryAction.setEnabled(false);
							copyAsInsertSQLAction.setEnabled(false);
							restoreCellAction.setEnabled(false);
							editInDialogAction.setEnabled(false);
							updateRowAction.setEnabled(false);
						} else {
							if (dataPane.getSqlResult().isResultSet()) { // ���Ϊ��ѯ���͵Ľ��
								prePageAction
										.setEnabled(dataPane.hasPrevious());

								nextPageAction.setEnabled(dataPane.hasNext());

								refreshQueryAction.setEnabled(true);
								queryAllAction.setEnabled(true);

								int selectedCount = getQueryResultAndSelected(dataPane);
								copyAsInsertSQLAction
										.setEnabled(selectedCount > 0);
								restoreCellAction
										.setEnabled((selectedCount > 0)
												&& isSelectedCellsModified());
								editInDialogAction
										.setEnabled(isQueryResultAndSelectedOneCell(dataPane));
								updateRowAction.setEnabled(selectedCount == 1);

							} else // ���Ϊ���»���ɾ�����ͽ��,����ǰ,���,��ѯ�������,ˢ�µȰ�ť��Ϊ������
							{
								prePageAction.setEnabled(false);
								nextPageAction.setEnabled(false);
								queryAllAction.setEnabled(false);
								refreshQueryAction.setEnabled(false);
								copyAsInsertSQLAction.setEnabled(false);
								restoreCellAction.setEnabled(false);
								editInDialogAction.setEnabled(false);
								updateRowAction.setEnabled(false);
							}
						}
					} else {
						prePageAction.setEnabled(false);
						nextPageAction.setEnabled(false);
						copyAsInsertSQLAction.setEnabled(false);
						restoreCellAction.setEnabled(false);
						editInDialogAction.setEnabled(false);
						updateRowAction.setEnabled(false);
					}
					GUIUtil.setComponentEnabled(true, removeSelectedTab);
					GUIUtil.setComponentEnabled(true, removeAllTab);
				}
			}
		});
    }
    private int getQueryResultAndSelected(DataSetPanel dataPane)
    {
    	JComponent content=dataPane.getContent();
    	if(!(content instanceof JTable))
    	{
    		return -1;
    	}
    	return ((JTable)content).getSelectedRowCount();
    }
    private boolean isQueryResultAndSelectedOneCell(DataSetPanel dataPane)
    {
    	JComponent content=dataPane.getContent();
    	if(!(content instanceof JTable))
    	{
    		return false;
    	}
    	return (((JTable)content).getSelectedRowCount())==1&&(((JTable)content).getSelectedColumnCount())==1;
    }
    /**
	 * ����tab�ؼ���ɾ���־
	 */
    private void setRemoveOrAddFlag(boolean isRemoveOrAdd) {
        this.isRemoveOrAdd = isRemoveOrAdd;
    }
    /**
     * This method is used to update the status of button associated with modifying result data.
     */
    protected void updateStatus()
    {
    	Component component=getDisplayComponent();
    	if(component instanceof DataSetPanel)
    	{
    		DataSetPanel dsPanel=(DataSetPanel)component;
    		if(dsPanel.isEditable())
    		{
    			changeStatusAction.setEnabled(true);
    			((ChangeStatusOfDataSetPanelAction)changeStatusAction).setSelected(dsPanel.isAllowEdit());
    		}else
    		{
    			changeStatusAction.setEnabled(false);
    		}
    		
    		saveChangeToDBAction.setEnabled(dsPanel.isAllowEdit()&&hasModified(dsPanel));
    	}else
    	{
    		changeStatusAction.setEnabled(false);
    		saveChangeToDBAction.setEnabled(false);
    	}
    	
    }
    /**
     * Validate whether deleteRowAction and cancelDeleteRowAction are enabled.
     */
    private void validateDeleteButton(DataSetPanel dataPane) {
//    	if (!dataPane.isAllowEdit()) {
//    		deleteRowAction.setEnabled(false);
//    		cancelDeleteRowAction.setEnabled(false);
//    		return;
//    	}
    	
    	JComponent content = dataPane.getContent();
    	if(!(content instanceof DataSetTable))
    	{
    		disableDeleteAction();
    	} else {
    		DataSetTable t = (DataSetTable) content;
    		int[] selectedRows = t.getSelectedRows();
    		if (selectedRows == null || selectedRows.length == 0) {
    			deleteRowAction.setEnabled(false);
        		cancelDeleteRowAction.setEnabled(false);
    		} else {
    			boolean checkDeleteRow = false;
    			boolean checkCancelDeleteRow = false;
	    		for (int row : selectedRows) {
	    			if (!t.isShouldDelete(row)) {
	    				checkDeleteRow = true;
	    			} else {
	    				checkCancelDeleteRow = true;
	    			}
	    			if (checkDeleteRow && checkCancelDeleteRow) {
	    				break;
	    			}
	    		}
	    		deleteRowAction.setEnabled(checkDeleteRow);
	    		cancelDeleteRowAction.setEnabled(checkCancelDeleteRow);
    		}
    	}
    }
    private void disableDeleteAction() {
    	deleteRowAction.setEnabled(false);
		cancelDeleteRowAction.setEnabled(false);
    }
    /**
     * Return true if some cells of dataset table in the dsPanel have been modified.
     * @return true if there are some changes, otherwise return false.
     */
    private boolean hasModified(DataSetPanel dsPanel)
    {
    	if(dsPanel==null)
    		return false;
    	
    	if(dsPanel.getContent() instanceof DataSetTable)
    	{
    		DataSetTable dsTable=(DataSetTable)dsPanel.getContent();
    		return dsTable.hasModified();
    	}else
    		return false;
    }
    /**
     * To check whether some of selected cells have been modified.
     * @return
     */
    protected boolean isSelectedCellsModified()
    {
       	Component component=getDisplayComponent();
    	if(component instanceof DataSetPanel)
    	{
    		DataSetPanel dsPanel=(DataSetPanel)component;
    		if(!(dsPanel.getContent() instanceof DataSetTable))
    		{
    			return false;
    		}
    		DataSetTable dsTable=(DataSetTable)dsPanel.getContent();
    		int[] selectedRows=dsTable.getSelectedRows();
    		int[] selectedColumns=dsTable.getSelectedColumns();
    		for(int i=0;i<selectedRows.length;i++)
    		{
    			for(int j=0;j<selectedColumns.length;j++)
    			{
    				if(dsTable.isModified(selectedRows[i], selectedColumns[j]))
    				{
    					return true;
    				}
    			}
    		}
    		return false;
    	}else
    	{
    		return false;
    	}
    	
    }
    /**
     * 
     * @author liu_xlin ���tab�ؼ���ҳɾ���Լ��ı����ѡ���¼��ļ�����
     */
    protected class TabChangeListener implements ChangeListener {

        /*
         * ���� Javadoc��
         * 
         * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
         */
        public void stateChanged(ChangeEvent e) {
            if (isRemoveOrAdd) //�����ɾ��ı䣬��������
            {
                setRemoveOrAddFlag(false);
                return;
            }
            checkButtonAvailable();
        }

    }

    /**
     * 
     * @author liu_xlin ����ؼ�����Ҽ���˵�����,����ݱ��������ֻ����Խ�������ʾ��ؼ�(DataSetTable)ʹ�õ�
     */
    private class DataTableMouseListener extends PopMenuMouseListener {
        public DataTableMouseListener() {

        }

        public void mouseReleased(MouseEvent e) {
            if (isPopupTrigger(e)) {
                menuManage.setComponent((JComponent) e.getSource()); //���²˵��������������

                /**
                 * �����¼�����ʱ���ܹ�����Ļ�ȡ���������������
                 */
                menuManage.getNoRenderMenu().putClientProperty(DataTable,
                        menuManage.getComponent());

                menuManage.getPopMenu().show((JComponent) e.getSource(),
                        e.getX(), e.getY());
            }
        }
    }

    private class ResultSetOfDataPaneListener implements PropertyChangeListener {

        /*
         * ���� Javadoc��
         * 
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("sqlResult")) //����������ִ�н����仯,�԰�ť�����Խ���У��
            {
                if (evt.getSource() == tab.getSelectedComponent())
                    checkButtonAvailable();
                	
            } else if (evt.getPropertyName().equals("sql")) //����tab����ʾ��Ϣ
            {
                if (evt.getOldValue().equals(evt.getNewValue()))
                    return;
                JComponent com = (JComponent) evt.getSource();
                int index = tab.indexOfComponent(com);
                tab.setToolTipTextAt(index, evt.getNewValue().toString());
            }
        }

    }
    private class DataSetTableSelectionListener implements ListSelectionListener{

		/* (non-Javadoc)
		 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
		 */
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel tableSelectionModel=(ListSelectionModel)e.getSource();
			JComponent com = (JComponent) tab.getSelectedComponent();
	        if (com instanceof DataSetPanel) { //���ж�����ֻ����Խ����ݱ༭��ť
	        	DataSetPanel dataPane = (DataSetPanel) com;
	        	JComponent content=dataPane.getContent();
	        	if(!(content instanceof DataSetTable))
	        		return;
	        
	        	DataSetTable table=(DataSetTable)content;
	        	//if table changed on selection is not in the current display tab, it'll do nothing.
	        	if(table.getSelectionModel()!=tableSelectionModel&&
	        			table.getColumnModel().getSelectionModel()!=tableSelectionModel)
	        		return ;
	        	
				copyAsInsertSQLAction.setEnabled(table.getSelectedRowCount()>0);
				restoreCellAction.setEnabled(table.getSelectedRowCount()>0&&isSelectedCellsModified());
				editInDialogAction.setEnabled(table.getSelectedColumnCount()==1&&table.getSelectedRowCount()==1);
				updateRowAction.setEnabled(table.getSelectedRowCount()==1);
				
				if (changeStatusAction.isEnabled() && ((CheckCsAction)changeStatusAction).isSelected()) {
					validateDeleteButton(dataPane);
				} else {
					disableDeleteAction();
				}
	        }else
	        {
	        	copyAsInsertSQLAction.setEnabled(false);
	        	restoreCellAction.setEnabled(false);
	        	editInDialogAction.setEnabled(false);
	        	updateRowAction.setEnabled(false);
	        }
		}
    	
    }

    public TableHeaderMenuManage getHeaderMenu() {
        return headerMenu;
    }

    public HeaderMouseListener getHeaderPopMenuListener() {
        return headerPopMenuListener;
    }
    @Override
    public String getTabViewTitle()
    {
    	return PublicResource.getString("resultView.tabtitle");
    }
    @Override
    public int getTabViewIndex()
    {
    	return 0;
    }
	/* (non-Javadoc)
	 * @see com.coolsql.view.View#doAfterMainFrame()
	 */
	@Override
	public void doAfterMainFrame() {
		
	}
}
