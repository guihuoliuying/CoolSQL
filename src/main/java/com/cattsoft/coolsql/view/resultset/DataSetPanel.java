/*
 * �������� 2006-7-20
 *
 */
package com.cattsoft.coolsql.view.resultset;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.component.AnimateLabel;
import com.cattsoft.coolsql.pub.component.MyTabbedPane;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.TableScrollPane;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.pub.util.SqlUtil;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.ConnectionUtil;
import com.cattsoft.coolsql.sql.ISQLDatabaseMetaData;
import com.cattsoft.coolsql.sql.SQLResultSetResults;
import com.cattsoft.coolsql.sql.SQLResults;
import com.cattsoft.coolsql.sql.SQLStandardResultSetResults;
import com.cattsoft.coolsql.sql.SQLResultSetResults.Column;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.sql.model.PrimaryKey;
import com.cattsoft.coolsql.sql.model.Table;
import com.cattsoft.coolsql.sql.model.View;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.ResultSetView;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ���չʾҳ�棬������ݺ����״̬
 */
public class DataSetPanel extends JPanel {
	
	public static final String PROPERTY_STATE="state";//Indicate whether the panel is editing.
	
	private static final long serialVersionUID = 1L;

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(DataSetPanel.class);
	/**
     * ִ��sql�������߳�
     */
    private Thread thread = null;

    /**
     * ���״̬����չʾ�˽��ļ�¼���
     */
    private DataSetStatusPanel status = null;

    /**
     * ��Ӧ����ǩ
     */
    private Bookmark bookmark = null;

    /**
     * sqlִ�н��
     */
    private SQLResults sqlResult = null;

    /**
     * ������Ҫ���ݵ���壨��ݱ���߻�ȡ��ݵĽ����Ϣ��
     */
    private JPanel pane = null;
    /**
     * �������(pane)�е����(�����JScrollPane������������������е��������)
     */
    private JComponent content=null; //

    protected PropertyChangeSupport pcs = null;

    /**
     * ����ǩ�ļ�����
     */
    private BookMarkPropertyListener bookmarkListener = null;

    private AnimateLabel prompt = null; //�����ʾ

    private boolean isReady = false; //����Ƿ�׼������
    
    /**
     * �жϸ�����Ƿ�Ҫ����ɾ����ڸ�������ƶ�,�ñ�־��Ҫ�ڽ�չʾ�����ݵı�ؼ�������ݴ���ʱʹ��,��ʱ�øñ�־ֵΪfalse
     *  true:����ɾ��  false:���ǳ���ɾ��
     */
    private boolean isRemoving=true; 

    /**
     * �ñ�־�ֶ�������ּ���Ҫ�������������������Ƿ��Ƕ�����ʾ�����animation label��
     */
    private boolean isAddPrompt=false;
    
    /**
     * This variant indicates whether all columns in execution include the primary keys of table when the type of execution is QUERY,
     * the value of this variant will be true if including, and it'll be false if not including or table has no primary keys.
     * 
     *  Note:This variant is used to cache value, so don't access it directly.
     */
    private Boolean isIncludePrimaryKey=null;
    
    /**column name list*/
    private List<UpdateKey> keyList;
    
    /**This variant controls the status of data table if data table can be edited.*/
    private Boolean isAllowEdit=null;
    
    /**
     * This variant indicates whether application record the action of user, doing thing according to record later.
     * The value of variant will be set as false if the status of data table changes.
     */
//    private boolean isRecordUserSelection=false;
    /**
     * Record the times of modifying. This variant indicate whether it's first time to modify data.
     */
    private boolean isFirstTime = true;
    
    private PromptableTableCellEditor cellEditor;
    
    private Boolean isEditable=null;
    public DataSetPanel(Bookmark bookmark) {
        this(bookmark, null, 0, 0,-1, "", null);
    }

    public DataSetPanel(Bookmark bookmark, Thread thread) {
        this(bookmark, null, 0, 0,-1, "", thread);
    }

    public DataSetPanel(Bookmark bookmark, JTable table, Thread thread) {
        this(bookmark, table, 0, 0,-1, "", thread);
    }

    public DataSetPanel(Bookmark bookmark, JTable table, int start, int end,int costTime,
            String sql, Thread thread) {

        this.thread = thread;
        this.bookmark = bookmark;
        this.setLayout(new BorderLayout());
        
        pane = new JPanel();
        pane.setLayout(new BorderLayout());

        status = new DataSetStatusPanel();
        if (table != null) {
            status.setRowCount(table.getRowCount());
            status.setRangeStart(start);
            status.setRangeEnd(end);
            status.updateRowRange();
            status.setCostTime(costTime);
            if (sql != null)
                status.setSql(sql);
            pane.add(new JScrollPane(table), BorderLayout.CENTER);
        }
        add(pane, BorderLayout.CENTER);
        if(Setting.getInstance().getBoolProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_DISPLAYMETAINFO,true))
        {
        	add(status, BorderLayout.SOUTH);
        }

        pcs = new PropertyChangeSupport(this);

        bookmarkListener = new BookMarkPropertyListener();

        addListenerToBookmark();
        
        addPanelPropertyChangeListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("sqlResult")) {
					if (isEditable()) {
						setAllowEdit(Setting
								.getInstance()
								.getBoolProperty(
										PropertyConstant.PROPERTY_VIEW_RESULTSET_DATASETTABLE_ISDIRECTMODIFY,
										true));
					} else {
						updateStatusIcon(false);
					}
				}
			}
        	
        });
    }

    /**
     * ������ʾ��ȱ�ǩ
     *  
     */
    public void setPromptContent() {
        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        prompt = new AnimateLabel("", SwingConstants.CENTER);        
        pane.add(prompt, gbc);
        prompt.start();
        
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy++;
        gbc.weightx = 0;
        gbc.weighty = 0;
        RenderButton quit = new RenderButton(PublicResource
                .getString("resultView.datapane.quit"));
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (thread != null) {
                    cancelExecute();
                    /**
                     * ɾ��ǰҳ
                     */
                    ResultSetView view = ViewManage.getInstance()
                            .getResultView(); //��ȡ�����ͼ
                    view.removeTab(DataSetPanel.this);
                }
            }

        });
        pane.add(quit, gbc);
        isAddPrompt=true;
        setContent(pane);
        isAddPrompt=false;
    }

    /**
     * Cancel executing.
     *
     */
    private void cancelExecute()
    {
        if(isReady)
            return;
            
        if (thread != null) {
            try {
                ConnectionUtil.quitLongTimeStatement(thread);
            } catch (SQLException e1) {
                LogProxy.SQLErrorReport(e1);
            }
        }
    }
    /**
     * Change the prompt information
     */
    public void setPromptText(String txt) {
        if (prompt != null)
            prompt.setText(txt);
    }

    /**
     * Add listener to bookmark.
     *  
     */
    public void addListenerToBookmark() {
        if (bookmark != null)
        {
        	bookmark.removePropertyListener(bookmarkListener); //Avoid adding listener repeatedly
            bookmark.addPropertyListener(bookmarkListener);
        }
    }

    /**
     *removeListener
     *  
     */
    public void removeListenerFromBookmark() {
        if (bookmark != null)
            bookmark.removePropertyListener(bookmarkListener);
    }

    /**
     * Retrive whether it has next page to be displayed.
     */
    public boolean hasNext() {
        if (sqlResult == null)
            return false;
        if (!sqlResult.isResultSet())
            return false;
        if (sqlResult instanceof SQLStandardResultSetResults) {
            SQLStandardResultSetResults queryResult = (SQLStandardResultSetResults) sqlResult;
            return queryResult.hasNextPage()&&!queryResult.isFullMode();
        } else
            return false;
    }

    /**
     * Retrive whether it has previous page to be displayed.
     */
    public boolean hasPrevious() {
        if (sqlResult == null)
            return false;
        if (!sqlResult.isResultSet())
            return false;
        if (sqlResult instanceof SQLStandardResultSetResults) {
            SQLStandardResultSetResults queryResult = (SQLStandardResultSetResults) sqlResult;
            return queryResult.hasPreviousPage()&&!queryResult.isFullMode();
        } else
            return false;
    }

    /**
     * Set the content of panel.
     * the type of content may be dataset table and label.
     */
    public void setContent(JComponent com) {
        /**
         * ���ǰ������Ӷ�����ʾ���,���Ҷ�����ǩ��Ϊnull,ֹͣ����.
         */
        if(!isAddPrompt&&prompt!=null)
        {
            prompt.stop();
            prompt=null;
        }
        //If current content object is  JTable type, then clear previous DataTable before updating the content value.
        if(content instanceof JTable)
        {
	        ViewManage.getInstance().getResultView().clearMenuProperty((JTable)getContent());
        }
        if (pane.getComponentCount() > 0)
            pane.removeAll();
        pane.add(new TableScrollPane(com), BorderLayout.CENTER);
        pane.validate();
        content=com;
    }
    /**
     * �����������ӱ�ؼ�
     */
    public void addTableToContent(JTable table) {
		setContent(table);

		if (table instanceof DataSetTable) {
			DataSetTable dsTable = (DataSetTable) getContent();
			dsTable.setEditable(isAllowEdit());
			if (dsTable.getModel() instanceof SortableTableModel) {
				((SortableTableModel) dsTable.getModel())
						.addTableModelModifyListener(new TableModelModifyListener() {

							public void dataChanged(TableModelModifyEvent e) {
								MyTabbedPane dataTab = ViewManage.getInstance()
										.getResultView().getResultTab();
								int index = dataTab
										.indexOfComponent(DataSetPanel.this);
								if (index < 0)
									return;

								dataTab.setTabState(index, e.hasModified());
								ViewManage.getInstance().getResultView()
										.checkButtonAvailable();
							}

						});
			}
		}
		// ��ӱ�ͷ��������
		table.getTableHeader().addMouseListener(
				ViewManage.getInstance().getResultView()
						.getHeaderPopMenuListener());

		ViewManage.getInstance().getResultView().addListenerToTable(table);
	}

    /**
     * ɾ�������������,ͬʱɾ�����ǩ�ļ���
     *  
     */
    public void removeComponent() {
        if(prompt!=null)
            prompt.stop();
        removeListenerFromBookmark();
        cancelExecute();

        JComponent com = this.getContent();
        if (com == null)
            return;
        if (com instanceof JScrollPane) {
            Component tmp = ((JScrollPane) com).getViewport().getView();
            if (tmp instanceof JTable) //�������������Ǳ�ؼ�,��ôɾ��������¼�
            {
                //����ؼ�����Ҽ�ļ���
                ViewManage.getInstance().getResultView()
                        .removeListenerFromTable((JTable) tmp);
                
                //����ͷ�ؼ�������Ҽ����
                ((JTable) tmp).getTableHeader().removeMouseListener(
                        ViewManage.getInstance().getResultView()
                                .getHeaderPopMenuListener());
            }
        }
        remove(com); //ɾ�������������
        
        if(getContent() instanceof JTable)
        {
	        //�������ط��Ա����������
	        ViewManage.getInstance().getResultView().clearMenuProperty((JTable)getContent());
        }
    }
    private boolean isIncludePrimaryKey()
    {
    	if(isIncludePrimaryKey==null)
    	{
    		if(!isEditable())
    		{
    			isIncludePrimaryKey=false;
    		}else
    		{
    			SQLResultSetResults queryResult=(SQLResultSetResults)sqlResult;
    			if(queryResult.getEntities()[0] instanceof View)
    			{
    				isIncludePrimaryKey=false;
    				return isIncludePrimaryKey;
    			}
    			Table tableEntity=(Table)queryResult.getEntities()[0];
    			try {
					PrimaryKey pk=tableEntity.getPrimaryKey();
					if(pk==null||pk.getNumberOfColumns()==0)
						isIncludePrimaryKey=false;
					else //Table has primary keys
					{
						String[] columns=queryResult.getColumnNames();
						List<String> columnNames=Arrays.asList(columns);
						int keyCount=pk.getNumberOfColumns();
						for(int i=0;i<keyCount;i++)
						{
							String key=pk.getColumn(i);
							if(!columnNames.contains(key))
							{
								isIncludePrimaryKey=false;
								return isIncludePrimaryKey;
							}
						}
						isIncludePrimaryKey=true;
					}
				} catch (SQLException e) {
					LogProxy.SQLErrorReport(e);
					isIncludePrimaryKey=false;
				} catch (UnifyException e) {
					LogProxy.errorReport(e);
					isIncludePrimaryKey=false;
				}
    		}
    	}
    	return isIncludePrimaryKey;
    }
    /**
     * Return true if only execution is query type , and associate with one entity.
     */
    public boolean isEditable()
    {
    	if(isEditable!=null)
    		return isEditable;
    	
    	if(sqlResult!=null&&sqlResult instanceof SQLResultSetResults)
    	{
    		SQLResultSetResults queryResult=(SQLResultSetResults)sqlResult;
    		//It must have only one entity!
    		//but some jdbc driver will return zero entity such as oracle ,
    		//so it will continue checking if the count of entities is zero!
    		if( queryResult.getEntities().length>1)
    		{
    			isEditable=false;
    			return isEditable;
    		}
    		
    		Entity entity=null;
    		if(queryResult.getEntities().length==1)
    		{
    			entity=queryResult.getEntities()[0];
	    		if(!(entity instanceof Table)&&!(entity instanceof View)) //It must be a TABLE or VIEW type
	    		{
	    			isEditable=false;
	    			return isEditable;
	    		}
    		}
    		ISQLDatabaseMetaData metaData;
			try {
				metaData = bookmark.getDbInfoProvider().getDatabaseMetaData();
			} catch (UnifyException e2) {
				isEditable=false;
    			return isEditable;
			}
    		/**
    		 * Jdbc driver may get alias of entity from method:ResultSetMetaData.getTableName(int),
    		 * so application will check by method:SqlUtil.getTables(String). 
    		 */
    		String sql=queryResult.getSql();
    		boolean isJdbcOdbc=bookmark.getDriver().isJdbcOdbc();
			if(isJdbcOdbc)
			{
				sql=sql.replaceAll("`", "\"");
			}
			
    		List<String> tables=SqlUtil.getTables(sql);
    		if(tables==null||tables.size()!=1)
    		{
    			isEditable=false;
    			return isEditable;
    		}
    		
    		String utilTable=tables.get(0);
    		if(isJdbcOdbc)
    		{
    			utilTable=utilTable.replaceAll("\"", "");
    		}
    		if(entity==null)
    		{
    			try {
					entity=SqlUtil.getTableObject(bookmark,utilTable);
					queryResult.setEntitys(new Entity[]{entity});
					isEditable=true;
				} catch (Exception e) {
					isEditable=false;
				}
    			return isEditable;
    		}
    		
    		//Retrive whether table name is identical with name in the metadata.
    		int index=utilTable.lastIndexOf(".");
    		if(index!=-1)
    		{
    			utilTable=utilTable.substring(index+1);
    		}
    		String identifierQuoteString;
			try {
				identifierQuoteString =StringUtil.trim(metaData.getIdentifierQuoteString());
			} catch (Exception e) {
				isEditable=false;
				return isEditable;
			}
    		if (identifierQuoteString.length()>0)
  	        {
  	        	utilTable=utilTable.replaceAll(identifierQuoteString, "");
  	        }
    		utilTable=utilTable.trim().toLowerCase();
    		String resultTable=entity.getName().toLowerCase();
    		
    		isEditable= utilTable.equals(resultTable);
    		return isEditable;
    	}else
    	{
    		isEditable=false;
			return isEditable;
    	}
    }
    /**
     * ������Ա仯�����¼�
     */
    public void addPanelPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * ɾ�����Ա仯����
     */
    public void removePanelPropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * ������ݸ����¼�
     */
    public void firePanelPropertyUpdate(String propertyName, Object oldValue,
            Object newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * ����ҳ�������
     * 
     * @return
     */
    public JComponent getContent() {
    	return content;
    }

    /**
     * @return
     */
    public int getRangeEnd() {
        return status.getRangeEnd();
    }

    /**
     * @return
     */
    public int getRangeStart() {
        return status.getRangeStart();
    }

    /**
     * @return
     */
    public int getRowCount() {
        return status.getRowCount();
    }

    /**
     * @return
     */
    public String getSql() {
        return status.getSql();
    }

    /**
     * @param row
     */
    public void setRangeEnd(int row) {
        status.setRangeEnd(row);
    }

    /**
     * @param row
     */
    public void setRangeStart(int row) {
        status.setRangeStart(row);
    }
    public void setCostTime(long time)
    {
        status.setCostTime(time);
    }
    /**
     * @param count
     */
    public void setRowCount(int count) {
        status.setRowCount(count);
    }

    /**
     * @param sql
     */
    public void setSql(String sql) {
        status.setSql(sql);
    }

    /**
     *  
     */
    public void updateRowRange() {
        status.updateRowRange();
    }

    /**
     * @return ���� sqlResult��
     */
    public SQLResults getSqlResult() {
        return sqlResult;
    }

    /**
     * ����ǲ�ѯ�Ľ����ݣ���ȡǰһҳ�����
     * 
     * @throws UnifyException
     * @throws SQLException
     *  
     */
    public void previousPage() throws SQLException, UnifyException {
        if (sqlResult.isResultSet()) {
            ((SQLStandardResultSetResults) sqlResult).previousPage(bookmark
                    .getConnection());
        }
    }

    /**
     * ����ǲ�ѯ�Ľ����ݣ���ȡ��һҳ�����
     *  
     */
    public void nextPage() throws SQLException, UnifyException {
        if (sqlResult.isResultSet()) {
            ((SQLStandardResultSetResults) sqlResult).nextPage(bookmark
                    .getConnection());
        }
    }

    /**
     * ����ǲ�ѯ�Ľ����ݣ�ˢ�����
     */
    public void refreshPage() throws SQLException, UnifyException {
        if (sqlResult.isResultSet()) {

            ((SQLStandardResultSetResults) sqlResult).refresh(bookmark
                    .getConnection());
        }
    }

    /**
     * This method is only invoked by process thread, and the object relevant to executing will not be changed including columns.
     *  So it only modifies the "allowedit" status,
     */
    void setSqlResult(SQLResults sqlResult) {
        SQLResults oldValue = this.sqlResult;
        this.sqlResult = sqlResult;

        updateResultInfo(sqlResult);
        String oldSql = this.getSql();
        setSql(sqlResult.getSql());
        firePanelPropertyUpdate("sql", oldSql, sqlResult.getSql());

        setReady(true); //����״̬Ϊ�Ѿ���

        if(!sqlResult.isResultSet()) {
        	status.setStatusIcon(null);
        }
        firePanelPropertyUpdate("sqlResult", oldValue, sqlResult);
    }

    /**
     * �������仯������״̬��Ϣ
     * 
     * @param queryData
     */
    protected void updateResultInfo(SQLResults sqlResult) {
        if (!(sqlResult instanceof SQLStandardResultSetResults))
            return;
        SQLStandardResultSetResults queryData = (SQLStandardResultSetResults) sqlResult;
        setRangeStart(queryData.getStart()); //������ʼ��
        setRangeEnd(queryData.getEnd()); //���ý���λ��
        setCostTime(queryData.getCostTime());
        updateRowRange();
        int total = queryData.getTotalNumberOfRows();
        setRowCount(total); //����������
        
    }

    /**
     * @return ���� bookmark��
     */
    public Bookmark getBookmark() {
        return bookmark;
    }

    /**
     * 
     * @author liu_xlin �����ǩ������仯����չʾ����tab�ؼ�ҳ��ҳ�������
     */
    private class BookMarkPropertyListener implements PropertyChangeListener {

        /*
         * ���� Javadoc��
         * 
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("aliasName")) {
                if (!evt.getOldValue().equals(evt.getNewValue())) {
                    ResultSetView view = ViewManage.getInstance()
                            .getResultView(); //��ȡ�����ͼ

                    //���µ�ǰ���������Ӧ��ҳ����
                    view.setTabTitle(view
                            .getPositionByComponent(DataSetPanel.this), evt
                            .getNewValue().toString());
                }
            }
        }

    }

    /**
     * @return ���� isReady��
     */
    public boolean isReady() {
        return isReady;
    }

    /**
     * @param isReady
     *            Ҫ���õ� isReady��
     */
    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    /**
     * ����ִ��sql��������ִ�н����߳�
     * 
     * @return
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * ����ִ��sql��������ִ�н����̶߳���
     * 
     * @param th
     */
    public void setCurrentThread(Thread th) {
        thread = th;
    }

	/**
	 * @return the keyList
	 */
	public List<UpdateKey> getKeyList() {
		return this.keyList;
	}
    /**
     * ��ָ�����������ת��Ϊ���ж���
     * 
     * @param data
     * @return
     */
    public static ColumnDisplayDefinition[] getHeaderDefinition(Object[] data) {
        if (data == null)
            return null;
        ColumnDisplayDefinition[] columns = new ColumnDisplayDefinition[data.length];
        for (int i = 0; i < data.length; i++) {
            Column colData = (Column) data[i];
            String displayName = colData.getName();
            int size = colData.getSize();
            int displayLength = displayName.length() + 3;
            columns[i] = new ColumnDisplayDefinition(
                    displayLength > size ? displayLength : size, displayName,
                    data[i]);
        }
        return columns;
    }
    public boolean isRemoving() {
        return isRemoving;
    }
    public void setRemoving(boolean isRemoving) {
        this.isRemoving = isRemoving;
    }

	/**
	 * @return the isAllowEdit
	 */
	public boolean isAllowEdit() {
		if(isAllowEdit==null)
			return false;
		return this.isAllowEdit;
	}

	/**
	 * @param isAllowEdit the isAllowEdit to set
	 */
	public void setAllowEdit(boolean isAllowEdit) {
		Boolean oldValue=this.isAllowEdit;
		this.isAllowEdit = isAllowEdit;
		if(oldValue!=null&&oldValue==this.isAllowEdit)
			return;

		updateStatusIcon(isAllowEdit);
		
		if(isAllowEdit)
		{
			boolean isIncludePk=isIncludePrimaryKey();
			if(keyList==null)
				keyList=new ArrayList<UpdateKey>();
			
			if(isIncludePk&&keyList.size()==0)//It occurs in the initialization.
			{
				SQLResultSetResults queryResult=(SQLResultSetResults)sqlResult;
				try {
					String[] columns=queryResult.getColumnNames();
					List<String> columnNames=Arrays.asList(columns);
					PrimaryKey pk=((Table)queryResult.getEntities()[0]).getPrimaryKey();
					for(int i=0;i<pk.getNumberOfColumns();i++)
					{
						String kName=pk.getColumn(i);
						keyList.add(new UpdateKey(kName,columnNames.indexOf(kName)));
					}
				} catch (SQLException e) {
					LogProxy.SQLErrorReport(e);
					this.isAllowEdit=false;
					return;
				} catch (UnifyException e) {
					LogProxy.errorReport(e);
					this.isAllowEdit=false;
					return;
				}
			}
			
			DataSetTable dsTable=(DataSetTable)getContent();
			//Do work below at last step
			dsTable=(DataSetTable)getContent();
			dsTable.setEditable(true);
			
    		if(this.cellEditor==null)
    		{
    			//The cell editor of all columns of DataSetTable is same each other,
    			//so get the cell editor of the first column as realeditor.
    			this.cellEditor=new PromptableTableCellEditor(dsTable.getCellEditor(0,0));
    		}else
    		{
    			TableCellEditor tce=dsTable.getCellEditor(0,0);
    			if(tce!=this.cellEditor)
    				this.cellEditor.setRealEditor(tce);
    		}
    		
    		ButtonTableHeader tableHeader=(ButtonTableHeader)dsTable.getTableHeader();
    		
    		/**
    		 * Change the cell editor of all columns, adding a checking work to the cell editor.
    		 */
    		TableColumnModel tcm=dsTable.getColumnModel();
    		List<String> keyColumnNames=createTempKeyNameList();
    		for(int i=0;i<dsTable.getColumnCount();i++)
    		{
    			//First set the editor of table column.
    			TableColumn tc=tcm.getColumn(i);
    			tc.setCellEditor(this.cellEditor);
    			
    			//Then set the color of column header
    			if(keyColumnNames.contains(dsTable.getColumnName(i)))
    			{
    				tableHeader.setHeaderColor(i,Color.red);
    			}else
    			{
    				tableHeader.setHeaderColor(i,null);
    			}
    		}
    		
    		tableHeader.repaint();
			
		}else
		{
			if(keyList==null)
				keyList=new ArrayList<UpdateKey>();
			
			//Do work below at last step
			if(!(getContent() instanceof DataSetTable))
				return ;
			DataSetTable dsTable=(DataSetTable)getContent();
			if(dsTable.getCellEditor()!=null)
				dsTable.getCellEditor().cancelCellEditing();
			dsTable.setEditable(false);
			
			dsTable.restoreAll();
			
			ButtonTableHeader tableHeader=(ButtonTableHeader)dsTable.getTableHeader();
			tableHeader.clearHeaderColor();

			//This operation should be performed when pressed the switch menu.
		}
		firePanelPropertyUpdate(PROPERTY_STATE,oldValue,this.isAllowEdit);
	}
	public boolean isFirstTime() {
		return isFirstTime;
	}
    /**
     * ͨ�����������������ȡ���Ӧ�Ľ��������
     * @param com  --�������
     * @return  --��������壬������������ڽ�������壬����null
     */
    public static DataSetPanel getDataPaneByContent(JComponent com)
    {
        return (DataSetPanel)GUIUtil.getUpParent(com,DataSetPanel.class);
    }
    public void popupColumnsSelectionDialog()
    {
    	if(!(getContent() instanceof DataSetTable))
    	{
    		return ;
    	}
    	DataSetTable dsTable=(DataSetTable)getContent();
    	String[] columns=new String[dsTable.getColumnCount()];
    	Boolean[] isSelected=new Boolean[dsTable.getColumnCount()];
    	List<String> keyColumnNames=createTempKeyNameList();
    	for(int i=0;i<dsTable.getColumnCount();i++)
    	{
    		columns[i]=dsTable.getColumnName(i);
    		isSelected[i]=keyColumnNames.contains(columns[i]);
    	}
    	SelectColumnsDialog dialog=new SelectColumnsDialog(columns,isSelected);
    	dialog.setVisible(true);
    	if(dialog.isDoSelection())
    	{
    		String[] selectedColumns=dialog.getSelectedColumns();
    		keyList.clear();
    		
    		keyColumnNames=new ArrayList<String>();
    		for(String str:selectedColumns)
    		{
    			keyColumnNames.add(str);
    		}
    		ButtonTableHeader tableHeader=(ButtonTableHeader)dsTable.getTableHeader();
    		for(int i=0;i<columns.length;i++)
    		{
    			if(keyColumnNames.contains(columns[i]))
    			{
    				tableHeader.setHeaderColor(i,Color.red);
    				UpdateKey key=new UpdateKey(columns[i],dsTable.convertColumnIndexToModel(i));
    				keyList.add(key);
    			}else
    			{
    				tableHeader.setHeaderColor(i,null);
    			}
    		}
    		tableHeader.repaint();
    	}
    	
    }
    private void updateStatusIcon(boolean isAllowEdit)
    {
		if(isEditable())
		{
			if(isAllowEdit)
			{
				status.setStatusIcon(DataSetStatusPanel.STATUS_ICON_EDITABLE);
			}else
			{
				status.setStatusIcon(DataSetStatusPanel.STATUS_ICON_DISABLE_EDITED);
			}
		}else
		{
			status.setStatusIcon(DataSetStatusPanel.STATUS_ICON_DISPLAY);
		}
    }
    private List<String> createTempKeyNameList()
    {
    	List<String> tempNames=new ArrayList<String>();
    	for(UpdateKey key:keyList)
    	{
    		tempNames.add(key.getColumnName());
    	}
    	return tempNames;
    }
    class PromptableTableCellEditor implements TableCellEditor
    {
    	private static final long serialVersionUID = 1L;
    	
		
    	private TableCellEditor realEditor;
		/**
		 * @param checkBox
		 */
		public PromptableTableCellEditor(TableCellEditor tableCelleditor) {
			this.realEditor=tableCelleditor;
		}
		void setRealEditor(TableCellEditor tableCelleditor)
		{
			this.realEditor=tableCelleditor;
		}
		 public boolean isCellEditable(EventObject anEvent) {
			boolean isCellEditable = realEditor.isCellEditable(anEvent);
			if (isCellEditable == true) {
				try {
					if (!isIncludePrimaryKey() && keyList.size() == 0
							&& isFirstTime) {
						boolean result = GUIUtil
								.getYesNo(
										GUIUtil.findLikelyOwnerWindow(),
										stringMgr
												.getString("resultset.instantmodify.firstprompt.message"));
						if (result) {
							popupColumnsSelectionDialog();
						}
					}
				} finally {
					isFirstTime = false;
				}
			}
			return isCellEditable;

		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable,
		 *      java.lang.Object, boolean, int, int)
		 */
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			return realEditor.getTableCellEditorComponent(table, value, isSelected, row, column);
		}
		/* (non-Javadoc)
		 * @see javax.swing.CellEditor#addCellEditorListener(javax.swing.event.CellEditorListener)
		 */
		public void addCellEditorListener(CellEditorListener l) {
			realEditor.addCellEditorListener(l);
		}
		/* (non-Javadoc)
		 * @see javax.swing.CellEditor#cancelCellEditing()
		 */
		public void cancelCellEditing() {
			realEditor.cancelCellEditing();
		}
		/* (non-Javadoc)
		 * @see javax.swing.CellEditor#getCellEditorValue()
		 */
		public Object getCellEditorValue() {
			return realEditor.getCellEditorValue();
		}
		/* (non-Javadoc)
		 * @see javax.swing.CellEditor#removeCellEditorListener(javax.swing.event.CellEditorListener)
		 */
		public void removeCellEditorListener(CellEditorListener l) {
			realEditor.removeCellEditorListener(l);
		}
		/* (non-Javadoc)
		 * @see javax.swing.CellEditor#shouldSelectCell(java.util.EventObject)
		 */
		public boolean shouldSelectCell(EventObject anEvent) {
			return realEditor.shouldSelectCell(anEvent);
		}
		/* (non-Javadoc)
		 * @see javax.swing.CellEditor#stopCellEditing()
		 */
		public boolean stopCellEditing() {
			return realEditor.stopCellEditing();
		}
    	
    }
    public class UpdateKey implements Serializable
    {
		private static final long serialVersionUID = 1L;

		private String columnName;
		private int columnIndex=-1;
		public UpdateKey(String columnName,int columnIndex)
    	{
    		this.columnIndex=columnIndex;
    		this.columnName=columnName;
    	}
		/**
		 * @return the columnName
		 */
		public String getColumnName() {
			return this.columnName;
		}
		/**
		 * @param columnName the columnName to set
		 */
		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}
		/**
		 * @return the columnIndex
		 */
		public int getColumnIndex() {
			return this.columnIndex;
		}
		/**
		 * @param columnIndex the columnIndex to set
		 */
		public void setColumnIndex(int columnIndex) {
			this.columnIndex = columnIndex;
		}
		@Override
		public boolean equals(Object ob)
		{
			if(ob==null)
				return false;
			if(!(ob instanceof UpdateKey))
				return false;
			
			UpdateKey that=(UpdateKey)ob;
			if(columnName==null)
			{
				if(that.columnName!=null)
					return false;
			}else
			{
				if(!columnName.equals(that.columnName))
					return false;
			}
			return columnIndex==that.columnIndex;
		}
    }
}
