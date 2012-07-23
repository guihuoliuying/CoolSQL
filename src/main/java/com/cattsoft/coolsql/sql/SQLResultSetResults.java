package com.cattsoft.coolsql.sql;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.util.SqlUtil;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.sql.model.EntityFactory;

/**
 * 
 * @author liu_xlin
 *  
 */
public abstract class SQLResultSetResults extends SQLResults {
    class ColumnArrayComparator implements Comparator {

        public int compare(Object arg0, Object arg1) {
            return compare((Column[]) arg0, (Column[]) arg1);
        }

        public int compare(Column columns0[], Column columns1[]) {
            if (columns0 == null && columns1 == null)
                return 0;
            if (columns0 == null)
                return -1;
            if (columns1 == null)
                return 1;
            if (columns0.length < columns1.length)
                return -1;
            if (columns0.length > columns1.length)
                return 1;
            int result = 0;
            int i = 0;
            for (int length = columns1 != null ? columns1.length : 0; result == 0
                    && i < length; i++)
                result = compare(columns0[i], columns1[i]);

            return result;
        }

        private int compare(Column column0, Column column1) {
            if (column0 == null && column1 == null)
                return 0;
            if (column0 == null)
                return -1;
            if (column1 == null)
                return 1;
            if (column0.getName() == null)
                return -1;
            if (column1.getName() == null)
                return 1;
            if (column0.getName() != null && column1.getName() != null
                    && column0.getName().compareTo(column1.getName()) != 0)
                return column0.getName().compareTo(column1.getName());
            if (column0.getType() == null)
                return -1;
            if (column1.getType() == null)
                return 1;
            if (column0.getType() != null && column1.getType() != null
                    && column0.getType().compareTo(column1.getType()) != 0)
                return column0.getType().compareTo(column1.getType());
            else
                return column0.getSize() - column1.getSize();
        }

        ColumnArrayComparator() {
        }
    }

    public class Row {

        public Object get(int columnNumber) {
            return columnNumber <= elements.size() && columnNumber > 0 ? elements
                    .get(columnNumber - 1)
                    : null;
        }

        public SQLResultSetResults getResultSet() {
            return SQLResultSetResults.this;
        }

        private final List<Object> elements;

        Row(List<Object> elements) {
            this.elements = elements;
        }
        Row(Object[] elements) {
        	if(elements==null)
        		this.elements=new ArrayList<Object>();
        	else
        		this.elements = Arrays.asList(elements);
        }
    }

    public class Column {

        public String getName() {
            return name;
        }

        public int getSize() {
            return size;
        }

        public String getType() {
            return type;
        }

        public int getSqlType() {
            return sqlType;
        }
        public boolean isAutoIncrement()
        {
        	return isAutoIncrement;
        }
        public String toString() {
            return name;
        }

        private final String name;

        private final String type;

        private final int size;

        private final int sqlType; //��ݿ��ж��������ֵ

        private final boolean isAutoIncrement;
        Column(String name, String type, int size, int sqlType,boolean isAutoIncrement) {
            this.name = name;
            this.type = type;
            this.size = size;
            this.sqlType = sqlType;
            this.isAutoIncrement=isAutoIncrement;
        }
    }

    public SQLResultSetResults(String query, Bookmark bookmark, Entity entity[]) {
        rows = Collections.synchronizedList(new ArrayList());
        columns = Collections.synchronizedList(new ArrayList());
        encoding = "";
        filterSort = null;
        propertyChangeSupport = new PropertyChangeSupport(this);
        super.setSql(query);
        this.bookmark = bookmark;
        this.entities = entity;
    }

    /**
     * ��ȡ��������漰����ʵ����Ϣ
     * @param set  --������
     * @param bookmark  --����Ӧ����ǩ
     * @return  --����е�ʵ����Ϣ
     * @throws SQLException
     */
    public static Entity[] getEntitiesInResult(ResultSet set, Bookmark bookmark)
            throws SQLException {
        ResultSetMetaData rsmd = set.getMetaData();
        
        int count = rsmd.getColumnCount();
        List list=new ArrayList();
        for (int i = 1; i <= count; i++) {
            Entity entity=EntityFactory.getInstance().create(bookmark,rsmd.getCatalogName(i),
                    StringUtil.trim(rsmd.getSchemaName(i)),StringUtil.trim(rsmd
                            .getTableName(i)), SqlUtil.TABLE, "", false);
            if(entity!=null&&!isContain(entity,list))
                list.add(entity);
        }
        return (Entity[])list.toArray(new Entity[list.size()]);
    }
    /**
     * ����ʵ���Ƿ��Ѿ������
     * @param entity  --��Ҫ�������ʵ�����
     * @param temp  --�Ѿ�����ӵ�ʵ���б�
     * @return  --�Ѿ�����ӣ����أ�true�����򷵻�false
     */
    private static boolean isContain(Entity entity,List temp)
    {
    	if(entity==null)
    		return false;
        for(int i=0;i<temp.size();i++)
        {
            Entity en=(Entity)temp.get(i);
            if(entity.equals(en))
                return true;
        }
        return false;
    }
    public String getColumnName(int columnNumber) {
        Column column = getColumn(columnNumber);
        return column != null ? column.getName() : "";
    }

    protected Column getColumn(int columnNumber) {
        return columnNumber > columns.size() ? null : (Column) columns
                .get(columnNumber - 1);
    }

    public Column[] getColumns() {
        return (Column[]) columns.toArray(new Column[columns.size()]);
    }

    protected void setColumns(Column columns[]) {
        Column original[] = getColumns();
        if ((new ColumnArrayComparator()).compare(original, columns) != 0) {
            this.columns.clear();
            this.columns.addAll(Arrays.asList(columns));
            propertyChangeSupport.firePropertyChange("columns", original,
                    columns);
        }
    }

    public Object getElement(int column, int row) {
        return ((Row) rows.get(row - 1)).get(column);
    }

    public int getColumnCount() {
        if (columns.size() > 0)
            return columns.size();
        else
            return 0;
    }

    public int getRowCount() {
        return rows.size();
    }

    public String[] getColumnNames() {
        List names = new ArrayList();
        Column column;
        for (Iterator i = columns.iterator(); i.hasNext(); names.add(column
                .getName()))
            column = (Column) i.next();

        return (String[]) names.toArray(new String[names.size()]);
    }

    public boolean isResultSet() {
        return true;
    }

    public Row[] getRows() {
        return (Row[]) rows.toArray(new Row[rows.size()]);
    }

    public String getFilteredQuery() {
        if (filterSort == null)
            return sql;
        else
            return sql + filterSort.toString();
    }

    public Bookmark getBookmark() {
        return bookmark;
    }

    public Entity[] getEntities() {
        return entities;
    }

    public boolean isMetaData() {
        return false;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected abstract void parseResultSet(ResultSet resultset)
            throws SQLException;

    /**
     * ���»�ȡ�����Ϣ��Ȼ����н���
     */
    public void refresh(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ConnectionUtil.addLongTimeStatement(Thread.currentThread(), statement);
        try {
        	statement.setMaxRows(getMaxSizeOfResultset());//this may accelerate the querying.
        	
        	long startTime = System.currentTimeMillis();
        	
            ResultSet resultSet = statement.executeQuery(getFilteredQuery());
            
            setCostTime(System.currentTimeMillis()-startTime);
            
            ConnectionUtil.removeLongTimeStatement(Thread.currentThread());
            try {
                parseResultSet(resultSet);
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
        }
    }

    protected void setRows(Row rows[]) {
        Row original[] = getRows();
        this.rows.clear();
        if (rows != null)
            this.rows.addAll(Arrays.asList(rows));
        propertyChangeSupport.firePropertyChange("rows", original, getRows());
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public FilterSort getFilterSort() {
        return filterSort;
    }

    public void setFilterSort(FilterSort filterSort) {
        this.filterSort = filterSort;
    }

    /**
     * ������Ϣת��Ϊһά������ʽ
     * 
     * @return
     */
    public Object[] getArrayDataOfColumn() {
        Object[] tmp = new Object[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            tmp[i] = columns.get(i);
        }
        return tmp;
    }

    /**
     * �������Ϣת��Ϊ��ά�������ʽ
     * 
     * @return
     */
    public Object[][] getArrayDataOfRow() {
        Object[][] tmp = new Object[rows.size()][columns.size()];
        for (int i = 0; i < rows.size(); i++) {
            Row rowData = (Row) rows.get(i);
            for (int j = 0; j < columns.size(); j++) {
                tmp[i][j] = rowData.get(j);
            }
        }
        return tmp;
    }

    /**
     * ������Ϣת��Ϊ������ʽ
     * 
     * @return
     */
    public Vector getVectorOfColumn() {
        Vector tmp = new Vector(columns.size());
        for (int i = 0; i < columns.size(); i++) {
            tmp.add(columns.get(i));
        }
        return tmp;
    }

    /**
     * �������Ϣת��Ϊ������ʽ
     * 
     * @return
     */
    public Vector getVectorDataOfRow() {
        Vector tmpData = new Vector(rows.size());
        for (int i = 0; i < rows.size(); i++) {
            Row rowData = (Row) rows.get(i);
            Vector tmpRow = new Vector(columns.size());
            for (int j = 0; j < columns.size(); j++) {
                tmpRow.add(rowData.get(j + 1));
            }
            tmpData.add(tmpRow);
        }
        return tmpData;
    }

    public void setEntitys(Entity[] ens) {
        this.entities = ens;
    }
    /**
     * Number value which query operation will fetch.
     * @return
     */
    protected int getMaxSizeOfResultset(){return 0;}
    /**
     * Query row data list
     */
    private List rows;

    /**
     * all columns information list
     */
    private List columns;

    /**
     * Bookmark 
     */
    private Bookmark bookmark;

    /**
     * ��������ʵ����󣬴��޸�������ֻ�ܱ��浥һʵ�壬���ڶ�ʵ���޷�ʵ��
     */
    protected Entity[] entities;

    /**
     * encoding for result string
     */
    private String encoding;

    /**
     * sql��������ʽ
     */
    private FilterSort filterSort;

    /**
     * ���Ըı��¼�������
     */
    private PropertyChangeSupport propertyChangeSupport;
    
    /**
     * The start offset of current page
     */
    protected int start;
}
