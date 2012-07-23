/**
 * Create date:2008-5-21
 */
package com.cattsoft.coolsql.view.resultset.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingworker.SwingWorker;

import com.cattsoft.coolsql.action.framework.AutoCsAction;
import com.cattsoft.coolsql.adapters.dialect.DialectFactory;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.exportdata.Actionable;
import com.cattsoft.coolsql.pub.component.WaitDialog;
import com.cattsoft.coolsql.pub.component.WaitDialogManage;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.pub.util.SqlUtil;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.Database;
import com.cattsoft.coolsql.sql.SQLResultSetResults;
import com.cattsoft.coolsql.sql.model.DataType;
import com.cattsoft.coolsql.sql.model.Table;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.DataSetTable;
import com.cattsoft.coolsql.view.resultset.SortableTableModel;
import com.cattsoft.coolsql.view.resultset.DataSetPanel.UpdateKey;
import com.cattsoft.coolsql.view.resultset.SortableTableModel.SortModelModifyStruct;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-21 create
 */
public class SaveChangeToDBAction extends AutoCsAction {

	private static final long serialVersionUID = 1L;

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(SaveChangeToDBAction.class);
	
	private SaveChangeWorker worker;
	
	private WaitDialog dialog ;
	@Override
	public void executeAction(ActionEvent e)
	{
		Component com=ViewManage.getInstance().getResultView().getDisplayComponent();
		DataSetPanel panel;
		if(com instanceof DataSetPanel)
		{
			panel=(DataSetPanel)com;
		}else
			return;
		SQLResultSetResults queryResult=(SQLResultSetResults)panel.getSqlResult();
		Table tableEntity=(Table)queryResult.getEntities()[0];
		/**
		 * Check whether is autocommit
		 */
		Bookmark bookmark = tableEntity.getBookmark();
        boolean isAutoCommit = bookmark.isAutoCommit();
        if(!isAutoCommit)
    	{
    		boolean result=GUIUtil.getYesNo(stringMgr.getString("resultset.action.beforesave.confirmcommit"));
        	if(!result)
        	{
        		return;
        	}
    	}
		
		if(dialog!=null)
			dialog.dispose();
		dialog= WaitDialogManage
        .getInstance().register(Thread.currentThread(),GUIUtil.findLikelyOwnerWindow());;
        worker=new SaveChangeWorker(dialog);
		worker.execute();
		dialog.setVisible(true);
	}

	private String generateRowUpdateSQL(String qualifiedName,
			Database db,
			int rowIndex,
			List<SortModelModifyStruct> list,
			Map<Integer,DataType> colTypeMap,
			DataSetTable table,
			List<UpdateKey> keyList) throws UnifyException
	{
		StringBuilder sb=new StringBuilder("UPDATE ").append(qualifiedName).append(" SET ");
		SortableTableModel model=(SortableTableModel)table.getModel();
		
		 boolean isPostgreSQL=DialectFactory.isPostgreSQL(db.getDatabaseMetaData());
		for(SortModelModifyStruct struct:list)
		{
			int columnIndex=struct.getColumnIndex();
			Object value=struct.getValue();
			DataType dataType=getColumnDataType(db,table.getColumnModel(),columnIndex,colTypeMap);
			String name=model.getColumnName(columnIndex);
			
			if(dataType==null||isPostgreSQL)
			{
				int columnDataType=getColumnDataType(columnIndex,table.getColumnModel());
				sb.append(name).append("=").append(value==null?"null":SqlUtil.qualifyColumnValue(value.toString(),columnDataType)).append(",");
				continue;
			}
			
			String realValue = (value == null ? "null"
                    : (StringUtil.trim(dataType.getLiteralPrefix())
                            + value.toString()
                            + StringUtil.trim(dataType.getLiteralSuffix())));
			sb.append(name).append("=").append(realValue).append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		
		sb.append(" WHERE ");
		for(UpdateKey key:keyList)
		{
			Object value=model.getValueInRealModel(rowIndex, key.getColumnIndex());
			DataType dataType=getColumnDataType(db,table.getColumnModel(),key.getColumnIndex(),colTypeMap);
			
			if(dataType==null||isPostgreSQL)
			{
				int columnDataType=getColumnDataType(key.getColumnIndex(),table.getColumnModel());
				sb.append(key.getColumnName()).
				append(value==null?" is null ":("=" + SqlUtil.qualifyColumnValue(value.toString(),columnDataType))).append(" AND ");
				continue;
			}
			
			String realValue = (value == null ? " is null "
                    : "="+(StringUtil.trim(dataType.getLiteralPrefix())
                            + value.toString()
                            + StringUtil.trim(dataType.getLiteralSuffix())));
			sb.append(key.getColumnName()).append(realValue).append(" AND ");
		}
		return sb.substring(0, sb.length()-5);//Clear up AND
	}
	private DataType getColumnDataType(Database db ,TableColumnModel columnModel,int columnIndex,Map<Integer,DataType> colTypeMap) throws UnifyException
	{
		if(colTypeMap.containsKey(columnIndex))
		{
			return colTypeMap.get(columnIndex);
		}
		
		TableColumn tcol = columnModel.getColumn(columnIndex);
        Object value = tcol.getHeaderValue();
        
        String typeName;
        if (value != null) //��ͷ�ж���Ϊ��
        {
            if (value instanceof SQLResultSetResults.Column) //������Ϊ�����ж��󣬲�������ʾ��Ϣ
            {
                SQLResultSetResults.Column columnInfo = (SQLResultSetResults.Column) value;
                typeName=columnInfo.getType();
            }else
            	return null;
        }else
        	throw new UnifyException("Can't get information in the column:"+columnIndex);

        DataType dataType = db.getDataType(typeName); 
        colTypeMap.put(columnIndex, dataType);
        
        return dataType;
	}
	private int getColumnDataType(int columnIndex,TableColumnModel columnModel) throws UnifyException
	{
		TableColumn tcol = columnModel.getColumn(columnIndex);
        Object value = tcol.getHeaderValue();
        if (value != null) 
        {
            if (value instanceof SQLResultSetResults.Column) //������Ϊ�����ж��󣬲�������ʾ��Ϣ
            {
                SQLResultSetResults.Column columnInfo = (SQLResultSetResults.Column) value;
                return columnInfo.getSqlType();
            }else
            	throw new UnifyException("Can't get information in the column:"+columnIndex);
        }else
        	throw new UnifyException("Can't get information in the column:"+columnIndex);
	}
	private class SaveChangeWorker extends SwingWorker<Object, String>
	{
		private WaitDialog dialog;
		
		private DataSetPanel panel;
		
		private Map<Integer,List<SortModelModifyStruct>> modifyDataStruct;
		
		private Set<Integer>  shouldDeletedRows;
		private boolean isRun;
		
		private int processCount; //rows count that have been processed.
		
		private Map<String ,TempCellObject> processedCell;
		private Set<Integer> processedRow;
		
		private Connection con = null;
        private Statement sm = null;
		public SaveChangeWorker(final WaitDialog dialog)
		{
			this.dialog=dialog;
			isRun=true;
			Component com=ViewManage.getInstance().getResultView().getDisplayComponent();
			if(com instanceof DataSetPanel)
			{
				panel=(DataSetPanel)com;
			}else
				return;
			
			DataSetTable dsTable=(DataSetTable)((DataSetPanel)panel).getContent();
			SortableTableModel model=(SortableTableModel)dsTable.getModel();
			modifyDataStruct = model.getModifiedDataStructure();
			
			shouldDeletedRows = model.getShouldDeletedRows();
			
        	final Thread current=Thread.currentThread();
            dialog.setTaskLength(modifyDataStruct.size() + shouldDeletedRows.size()); //Set the length of task
            dialog.addQuitAction(new Actionable() {

				public void action() {
					WaitDialogManage.getInstance().disposeRegister(current);
					dialog.dispose();
					isRun = false;
				}
            	
            }
            );
			addPropertyChangeListener(
		            new PropertyChangeListener() {
		                public  void propertyChange(PropertyChangeEvent evt) {
		                    if ("progress".equals(evt.getPropertyName())) {
		                    	dialog.setProgressValue((Integer)evt.getNewValue());
		                    }
		                }
		            });
			processedCell=new HashMap<String,TempCellObject>();
			processedRow = new HashSet<Integer>();
			
			dialog.setPrompt(stringMgr.getString("resultset.action.savechange.processdialog.prompt"));
		}
		/* (non-Javadoc)
		 * @see org.jdesktop.swingworker.SwingWorker#doInBackground()
		 */
		@Override
		protected Object doInBackground() throws Exception {
			if(panel==null)
				return null;
			parseDataPanel();
			return null;
		}
		private void parseDataPanel()
		{
			if(panel==null)
				return;
			
			SQLResultSetResults queryResult=(SQLResultSetResults)panel.getSqlResult();
			Table tableEntity=(Table)queryResult.getEntities()[0];
			
			Map<Integer,DataType> colTypeMap = new HashMap<Integer,DataType>();
			Iterator<Integer> rows=modifyDataStruct.keySet().iterator();
			
			Bookmark bookmark = tableEntity.getBookmark();
	        boolean oldIsAutoCommit = bookmark.isAutoCommit();
	        
	        try {
	        	if(!oldIsAutoCommit)
	        	{
	        		bookmark.getConnection().commit();
	        	}
	            bookmark.setAutoCommit(false);
	        } catch (SQLException e3) {
	            LogProxy.SQLErrorReport(GUIUtil.findLikelyOwnerWindow(), e3);
	            dialog.dispose();
	            return;
	        } catch (UnifyException e3) {
	            LogProxy.errorReport(GUIUtil.findLikelyOwnerWindow(), e3);
	            dialog.dispose();
	            return;
	        }
	        
	        try {
	            con = bookmark.getConnection();
	            sm = con.createStatement();
	        } catch (UnifyException e) {
	            LogProxy.errorReport(GUIUtil.findLikelyOwnerWindow(), e);
	            return;
	        } catch (SQLException e) {
	            LogProxy.SQLErrorReport(GUIUtil.findLikelyOwnerWindow(), e);
	            return;
	        }
			
	        DataSetTable dsTable = (DataSetTable)((DataSetPanel)panel).getContent();
	        processCount = 0;
	        
	        try
	        {
				while(isRun && rows.hasNext())
				{
					boolean isSuccess = true;
					int index=rows.next();
					List<SortModelModifyStruct> list=modifyDataStruct.get(index);
					try
					{
						isSuccess = modifyTask(index, dsTable, tableEntity, colTypeMap);
						
					}catch(Exception e)
					{
						isSuccess=false;
						try
						{
							if(processError(e,dsTable,index,con))
							{
								continue;
							}else
							{
	//							con.commit();
								isRun=false;
							}
						}catch(Throwable e1)
						{
							LogProxy.errorReport(e1);
						}
					}finally
					{
						if(isSuccess) {
							processStateForModify(index,list,dsTable);//Save the information of cell that has been process successfully.
						}
						
						setProgress(++processCount);
					}
				}
				
				/**
				 * process deleting .
				 */
				rows = new HashSet<Integer>(shouldDeletedRows).iterator();
//				Integer[] deleteedshouldDeletedRows.toArray(new Integer[shouldDeletedRows.size()]);
				while(isRun && rows.hasNext())
				{
					boolean isSuccess = true;
					int index=rows.next();
					try
					{
						isSuccess = deleteTask(index, tableEntity, dsTable, colTypeMap);
						
					}catch(Exception e)
					{
						isSuccess=false;
						try
						{
							if(processError(e,dsTable,index,con))
							{
								continue;
							}else
							{
								isRun=false;
							}
						}catch(Throwable e1)
						{
							LogProxy.errorReport(e1);
						}
					}finally
					{
						if(isSuccess) {
							processStateForDelete(index, dsTable);
						}
						setProgress(++processCount);
					}
				}
	        }finally
	        {
	        	//Release all resource including deleting rows have been marked.
	        	processedCell.clear();
	        	if (processedRow.size() > 0) {
	        		SortableTableModel model = (SortableTableModel) dsTable.getModel();
	        		int[] deletedRows = new int[processedRow.size()];
	        		Integer[] rowsObj = processedRow.toArray(new Integer[processedRow.size()]);
	        		for (int i = 0; i < processedRow.size(); i++) {
	        			deletedRows[i] = rowsObj[i];
	        		}
	        		model.deleteRows(deletedRows);
	        		
	        		processedRow.clear();
	        	}
	        	modifyDataStruct.clear();
	        	dialog.dispose();
				try {
					con.commit();
					sm.close();
					bookmark.setAutoCommit(oldIsAutoCommit);
					
					if(!dsTable.hasModified() && isRun) {
						JOptionPane.showMessageDialog(GUIUtil.findLikelyOwnerWindow(), 
							stringMgr.getString("resultset.action.savechange.savesuccess"));
					}
				} catch (SQLException e) {
					LogProxy.errorReport(e);
				} catch (UnifyException e) {
					LogProxy.errorReport(e);
				}
	        }
		}
		/**
		 * Save the modification of updating into database.
		 * @param index the row index.
		 * @param dsTable the table modified by user.
		 * @param tableEntity the table entity to which result data belongs
		 * @param colTypeMap the map of column type cache.
		 * @return return true if processing successfully.
		 */
		private boolean modifyTask(int index, DataSetTable dsTable,
				Table tableEntity, Map<Integer, DataType> colTypeMap)
				throws Exception {
			List<SortModelModifyStruct> list = modifyDataStruct.get(index);
			if (list == null) {
				return true;
			}

			boolean isSuccess = true;
			SortableTableModel model = (SortableTableModel) dsTable.getModel();
			String updateSQL = generateRowUpdateSQL(tableEntity
					.getQualifiedName(), tableEntity.getBookmark()
					.getDbInfoProvider(), index, list, colTypeMap, dsTable,
					panel.getKeyList());
			LogProxy.getProxy().debug(updateSQL);
			int updateCount = sm.executeUpdate(updateSQL);

			if (updateCount == 0) {
				String message = stringMgr.getString(
						"resultset.action.savechange.norowupdate", model
								.getViewRow(index) + 1);
				JOptionPane.showMessageDialog(GUIUtil.findLikelyOwnerWindow(),
						message, "warning", JOptionPane.WARNING_MESSAGE);
				isSuccess = false;
			} else if (updateCount > 1) {
				String message = stringMgr.getString(
						"resultset.action.savechange.multiRowUpdated",
						new Object[]{updateCount, model.getViewRow(index) + 1});
				JOptionPane.showMessageDialog(GUIUtil.findLikelyOwnerWindow(),
						message, "warning", JOptionPane.WARNING_MESSAGE);
				isSuccess = false;
				
				processStateForModify(index, list, dsTable);
				
				if (!isContinue(dsTable, con, processCount + 1)) {
					isRun = false;
				}
			}
			return isSuccess;

		}
		private boolean deleteTask(int index, Table tableEntity,
				DataSetTable dsTable, Map<Integer, DataType> colTypeMap)
				throws Exception {
			String updateSQL = generateDeleteSQL(index, tableEntity, dsTable,
					colTypeMap);

			boolean isSuccess = true;
			LogProxy.getProxy().debug(updateSQL);
			int updateCount = sm.executeUpdate(updateSQL);

			SortableTableModel model = (SortableTableModel) dsTable.getModel();
			if (updateCount == 0) {
				String message = stringMgr.getString(
						"resultset.action.savechange.norowupdate", model
								.getViewRow(index) + 1);
				JOptionPane.showMessageDialog(GUIUtil.findLikelyOwnerWindow(),
						message, "warning", JOptionPane.WARNING_MESSAGE);
				isSuccess = false;
			} else if (updateCount > 1) {
				String message = stringMgr.getString(
						"resultset.action.savechange.multiRowUpdated",
						new Object[]{updateCount, model.getViewRow(index) + 1});
				JOptionPane.showMessageDialog(GUIUtil.findLikelyOwnerWindow(),
						message, "warning", JOptionPane.WARNING_MESSAGE);
				isSuccess = false;
				
				processStateForDelete(index, dsTable);
				
				if (!isContinue(dsTable, con, processCount + 1)) {
					isRun = false;
				}
			}
			return isSuccess;
		}
		private String generateDeleteSQL(int index, Table tableEntity,
				DataSetTable dsTable, Map<Integer, DataType> colTypeMap)
				throws Exception {
			StringBuilder deleteSQL = new StringBuilder("DELETE FROM ");
			deleteSQL.append(tableEntity.getQualifiedName());

			deleteSQL.append(" WHERE ");

			SortableTableModel model = (SortableTableModel) dsTable.getModel();
			boolean isPostgreSQL = DialectFactory.isPostgreSQL(tableEntity
					.getBookmark().getDbInfoProvider().getDatabaseMetaData());
			for (UpdateKey key : panel.getKeyList()) {
				Object value = model.getValueInRealModel(index, key
						.getColumnIndex());
				DataType dataType = getColumnDataType(tableEntity.getBookmark()
						.getDbInfoProvider(), dsTable.getColumnModel(), key
						.getColumnIndex(), colTypeMap);

				if (dataType == null || isPostgreSQL) {
					int columnDataType = getColumnDataType(
							key.getColumnIndex(), dsTable.getColumnModel());
					deleteSQL.append(key.getColumnName()).append(
							value == null ? " is null " : ("=" + SqlUtil
									.qualifyColumnValue(value.toString(),
											columnDataType))).append(" AND ");
					continue;
				}

				String realValue = (value == null ? " is null " : "="
						+ (StringUtil.trim(dataType.getLiteralPrefix())
								+ value.toString() + StringUtil.trim(dataType
								.getLiteralSuffix())));
				deleteSQL.append(key.getColumnName()).append(realValue).append(
						" AND ");
			}
			return deleteSQL.substring(0, deleteSQL.length() - 5);// Clear up
			// AND

		}
		private void processStateForModify(int row,
				List<SortModelModifyStruct> list, DataSetTable dsTable) {
			if (list == null) {
				return;
			}
			SortableTableModel model = (SortableTableModel) dsTable.getModel();
			for (SortModelModifyStruct struct : list) {
				String key = StringUtil.compose(row, struct.getColumnIndex());
				processedCell.put(key, new TempCellObject(model
						.getValueInRealModel(row, struct.getColumnIndex()),
						struct.getValue()));
				model.removeModify(key);
				model.setValueInRealModel(struct.getValue(), row, struct
						.getColumnIndex());
			}
			dsTable.repaint();
		}
		private void processStateForDelete(int modelRow, DataSetTable dsTable) {
			SortableTableModel model = (SortableTableModel) dsTable.getModel();
			model.cancelDeletedRow(modelRow);
			processedRow.add(modelRow);
		}
		private boolean processError(Exception e,DataSetTable dsTable,int modelRow,Connection con) throws SQLException
		{
			//Give a prompt to user for error
			SortableTableModel model=(SortableTableModel)dsTable.getModel();
			String message=stringMgr.getString("resultset.action.savechange.errorconfirm"+(model.getViewRow(modelRow)+1));
			LogProxy.errorReport(message,e);
			
			return isContinue(dsTable,con, processCount + 1);
		}
		private boolean isContinue(DataSetTable dsTable,Connection con, int processedCount) throws SQLException
		{
			SortableTableModel model=(SortableTableModel)dsTable.getModel();
			//Confirm whether quit or continue.
			boolean result=GUIUtil.getYesNo(stringMgr.getString("resultset.action.savechange.goonconfirm"));
			if(result)//go on
			{
				return true;
			}else  //quit
			{
				if (processedCell.size() > 0 || processedRow.size() >0) {
					result=GUIUtil.getYesNo(stringMgr.getString("resultset.action.savechange.rollbackconfirm", processedCount));
					if(result)
					{
						if (processedCell.size() > 0) {
							Iterator<String> it=processedCell.keySet().iterator();
							while(it.hasNext())
							{
								String key=it.next();
								TempCellObject cellObject=processedCell.get(key);
								model.modifyCell(key, cellObject.getNewValue());
								int[] cellIndex=StringUtil.resolveString(key);
								model.setValueInRealModel(cellObject.getOldValue(), cellIndex[0], cellIndex[1]);
							}
							processedCell.clear();
						}
						
						if (processedRow.size() > 0) {
							model.markDeletedRow(processedRow.toArray(new Integer[processedRow.size()]));
							processedRow.clear();
						}
						
						
						dsTable.repaint();
						con.rollback();
					}
				}
				return false;
			}
		}
	}
	private class TempCellObject
	{
		private Object newValue;
		private Object oldValue;
		
		public TempCellObject(Object newValue,Object oldValue)
		{
			this.newValue=newValue;
			this.oldValue=oldValue;
		}

		/**
		 * @return the newValue
		 */
		public Object getNewValue() {
			return this.newValue;
		}

		/**
		 * @param newValue the newValue to set
		 */
		public void setNewValue(Object newValue) {
			this.newValue = newValue;
		}

		/**
		 * @return the oldValue
		 */
		public Object getOldValue() {
			return this.oldValue;
		}

		/**
		 * @param oldValue the oldValue to set
		 */
		public void setOldValue(Object oldValue) {
			this.oldValue = oldValue;
		}
		
	}
}
