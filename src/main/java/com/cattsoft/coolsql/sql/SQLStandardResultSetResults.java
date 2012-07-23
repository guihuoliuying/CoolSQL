package com.cattsoft.coolsql.sql;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;

public class SQLStandardResultSetResults extends SQLResultSetResults {

    protected SQLStandardResultSetResults(Bookmark bookmark, String query,
            Entity entity[], int numberOfRowsPerPage) {
        super(query, bookmark, entity);
        hasMore = false;
        start = 1;
        totalNumberOfRows = -1;
        fullMode = false;
        this.numberOfRowsPerPage = numberOfRowsPerPage;
        
        Setting.getInstance().addPropertyChangeListener(new PropertyChangeListener()
        {

			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getNewValue()==null)
					return;
				setMaxColumnWidth(Integer.parseInt(evt.getNewValue().toString()));
			}
        	
        }
        , PropertyConstant.PROPERTY_VIEW_RESULTSET_MAXCOLUMNWIDTH);
        
    }

    static SQLResultSetResults create(ResultSet set, Bookmark bookmark,
            String query, Entity[] entity, int numberOfRows) throws SQLException {
        SQLStandardResultSetResults results = new SQLStandardResultSetResults(
                bookmark, query, entity, numberOfRows);
        results.parseResultSet(set);
        if(entity==null)
            results.setEntitys(getEntitiesInResult(set,bookmark));
        return results;
    }

    /**
     * ������ݿ���ÿ���ֶε�ֵ��ֻȡǰ2048���ֽ�
     */
    protected void parseResultSet(ResultSet set) throws SQLException {
        int rowCount = 1;
        ResultSetMetaData metaData = set.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        ResultSetReader reader=new ResultSetReader(set);
        reader.setMaxColumnWith(maxColumnWidth);
        /**
         * ��ȡ����е���������Ϣ
         */
        List<SQLResultSetResults.Column> columns = new ArrayList<SQLResultSetResults.Column>();
        for (int i = 1; i <= columnCount; i++)
            columns.add(new SQLResultSetResults.Column(metaData
                    .getColumnName(i), metaData.getColumnTypeName(i), metaData
                    .getColumnDisplaySize(i),metaData.getColumnType(i),metaData.isAutoIncrement(i)));

        setColumns((SQLResultSetResults.Column[]) columns
                .toArray(new SQLResultSetResults.Column[columns.size()]));
        
        
        boolean exitEarly = false;
        int firstRow = fullMode ? 0 : start;
        int lastRow = fullMode ? Integer.MAX_VALUE : (start + numberOfRowsPerPage) - 1;
        List<SQLResultSetResults.Row> rowList = new ArrayList<SQLResultSetResults.Row>();
        while (true) {
        	Object[] rowData=reader.readRow();
        	if (rowData == null)
				break;
        	
            boolean disable = start < 1 || lastRow < 1;
            if (disable || rowCount >= firstRow && rowCount <= lastRow) {
//                List row = new ArrayList();
//                int i = 1;
                
//                /**
//                 * Parse row data
//                 */
//                for (int length = columns.size(); i <= length; i++) {
//                    Object value = null;
//                    row.add(value != null && !set.wasNull() ? ((Object) (value))
//                                    : "<NULL>");
//                }

                //������������ݶ���(SQLResultSetResults.Row)����������
                rowList.add(new SQLResultSetResults.Row(rowData));
            }
            rowCount++;
            
            //����������λ�ã��˳���ݽ���
            if (!disable && rowCount > lastRow) {
                exitEarly = true;
                break;
            }
        }
        if (exitEarly) {  //��������˳�������ݽ���
            hasMore = set.next();
        } else {   //��ɽ��Ľ�������ô�������������
            totalNumberOfRows = Math.max(0, rowCount - 1);
            hasMore = false;
        }
        
        //��������Ľ����ݱ���
        setRows((SQLResultSetResults.Row[]) rowList
                .toArray(new SQLResultSetResults.Row[rowList.size()]));
    }


    public int getTotalNumberOfRows() {
        return totalNumberOfRows;
    }

    public void nextPage(Connection connection) throws SQLException {
        if (hasNextPage()) {
            start += numberOfRowsPerPage;
            maxSizeOfResultset=start+numberOfRowsPerPage;
            super.refresh(connection);
        }
    }

    public void previousPage(Connection connection) throws SQLException {
        if (hasPreviousPage()) {
            start = Math.max(1, start - numberOfRowsPerPage);
            maxSizeOfResultset=start+numberOfRowsPerPage;
            super.refresh(connection);
        }
    }
    public void refresh(Connection connection) throws SQLException{
    	maxSizeOfResultset=start+numberOfRowsPerPage;
    	super.refresh(connection);
	}
    /**
     * return max size of next query
     */
    protected int getMaxSizeOfResultset(){
    	return fullMode ? 0 : maxSizeOfResultset;
    }
    public boolean hasNextPage() {
        return hasMore;
    }

    public boolean hasPreviousPage() {
        return start > 1;
    }

    public void setFullMode(boolean fullMode) {
        this.fullMode = fullMode;
    }

    public boolean isFullMode() {
        return fullMode;
    }

    public int getStart() {
        return getRowCount() != 0 ? fullMode ? 1 : start : 0;
    }

    public int getEnd() {
        return fullMode ? getRowCount() : (start + getRowCount()) - 1;
    }

    public int getLast() {
        return totalNumberOfRows;
    }

    public void setFilterSort(FilterSort filterSort) {
        super.setFilterSort(filterSort);
        start = 1;
    }
    public int getMaxColumnWidth()
    {
    	return maxColumnWidth;
    }
    public void setMaxColumnWidth(int w)
    {
    	this.maxColumnWidth=w;
    }
    
    private int maxColumnWidth=Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_MAXCOLUMNWIDTH, 2048);
    /**
     * ������Ƿ���Ȼ����û�н��������
     */
    private boolean hasMore;


    /**
     * ÿҳ�ļ�¼����
     */
    private int numberOfRowsPerPage;

    /**
     * ��ѯ�����ܼ�¼��
     */
    private int totalNumberOfRows;

    /**
     * �Ƿ�չʾ�����������
     */
    private boolean fullMode;
    
    /**
     * max size that current querying may fetch from database.
     */
    protected int maxSizeOfResultset=0;
}
