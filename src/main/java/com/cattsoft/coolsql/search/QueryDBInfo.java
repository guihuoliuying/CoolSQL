/*
 * �������� 2006-9-19
 */
package com.cattsoft.coolsql.search;

import java.sql.SQLException;
import java.util.List;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.display.TableCellObject;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.sql.model.Column;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ����������ݿ��ʵ�������Ϣ
 */
public class QueryDBInfo {

    private SearchInfoDialog visual = null;

    /**
     * ��ѯ���չʾ����
     */
    private SearchResultFrame resultFrame = null;

    public QueryDBInfo(SearchInfoDialog v) {
        visual = v;
    }

    /**
     * ���ò�ѯ����
     * 
     * @param frame
     */
    public void setResultFrame(SearchResultFrame frame) {
        resultFrame = frame;
    }

    /**
     * ��ѯ��Ϣ
     *  
     */
    public void query() {
        //����У����ǩ����Ч��
        Bookmark bookmark = visual.getSelectBookmark();
        if (bookmark == null)
            return;

        int type = visual.getQueryType();
        if (type == 0) // ʵ���ѯ
        {
            resultFrame.setProcessInfo(PublicResource
                    .getSQLString("searchinfo.result.processinfo.getfromdb"));//��ʾ���ڻ�ȡ���
            Entity[] result;
            try {
                result = queryEntityInfo();
            } catch (UnifyException e) {
                resultFrame.closeFrame();
                LogProxy.errorReport(resultFrame, e);
                return;
            } catch (SQLException e) {
                resultFrame.closeFrame();
                LogProxy.SQLErrorReport(resultFrame, e);
                return;
            }
            if (result == null || resultFrame == null) //���û�н�����û��չʾ���ڣ���������
                return;
            resultFrame.setCount(result.length);
            Object[] rowData = null;

            resultFrame.setProcessInfo(PublicResource
                    .getSQLString("searchinfo.result.processinfo.adddata")); //��ʾ���ڼ���
            for (int i = 0; i < result.length; i++) {
                rowData = new Object[4];
                String entityType = result[i].getType();
                
                rowData[0] = new TableCellObject(
                        result[i].getName(),
                        BookMarkPubInfo.getTableTypeIcon(entityType));
                rowData[1] = result[i].getCatalog();
                rowData[2] = result[i].getSchema();
                rowData[3] = result[i].getType();
                resultFrame.addRow(rowData); //���һ�����
            }
            resultFrame.setProcessInfo("");
            resultFrame.adjustGUI();
        } else if (type == 1) //�в�ѯ
        {
            resultFrame.setProcessInfo(PublicResource
                    .getSQLString("searchinfo.result.processinfo.getfromdb"));
            Column[] result;
            try {
                result = queryColumnInfo();
            } catch (SQLException e) {
                resultFrame.closeFrame();
                LogProxy.SQLErrorReport(resultFrame, e);
                return;
            } catch (UnifyException e) {
                resultFrame.closeFrame();
                LogProxy.errorReport(resultFrame, e);
                return;
            }
            if (result == null || resultFrame == null)
                return;
            resultFrame.setCount(result.length);
            Object[] rowData = null;

            resultFrame.setProcessInfo(PublicResource
                    .getSQLString("searchinfo.result.processinfo.adddata"));

            for (int i = 0; i < result.length; i++) {
                rowData = new Object[5];
                rowData[0] = new TableCellObject(
                        result[i].getName(),
                        BookMarkPubInfo.getIconList()[BookMarkPubInfo.NODE_COLUMN]); //����
                rowData[1] = result[i].getParentEntity().getName(); //����ʵ����
                rowData[2] = result[i].getParentEntity().getType(); //ʵ������
                rowData[3] = result[i].getParentEntity().getSchema(); //����ģʽ
                rowData[4] = result[i].getParentEntity().getCatalog(); //����ģʽ
                resultFrame.addRow(rowData); //���һ�����
            }
            resultFrame.setProcessInfo("");
            resultFrame.adjustGUI();
        }
    }

    /**
     * To query entity information match sepecified condition 
     */
    public Entity[] queryEntityInfo() throws UnifyException, SQLException {
        Bookmark bookmark = visual.getSelectBookmark();
        if (bookmark == null)
            return null;

        String catalogName= visual.getQueryCatalog();
        String schemaName = visual.getQuerySchema();

        String entityName = visual.getQueryEntity(); //ʵ��

        if(bookmark.getDbInfoProvider().getDatabaseMetaData().storesLowerCaseIdentifiers())
        {
        	if(catalogName!=null)
        		catalogName = catalogName.toLowerCase();
        	if(schemaName!=null)
        		schemaName = schemaName.toLowerCase();
        	if(entityName!=null)
        		entityName = entityName.toLowerCase();
        }else if(bookmark.getDbInfoProvider().getDatabaseMetaData().storesUpperCaseIdentifiers())
        {
        	if(catalogName!=null)
        		catalogName = catalogName.toUpperCase();
        	if(schemaName!=null)
        		schemaName = schemaName.toUpperCase();
        	if(entityName!=null)
        		entityName = entityName.toUpperCase();
        }
//        �����mysql��ݿ⣬����ѯ���������ݵ���Ϊ��д
//        if (catalogName != null&&!(bookmark.isMysql()))
//        	catalogName = catalogName.toUpperCase();
//        if (schemaName != null&&!(bookmark.isMysql()))
//            schemaName = schemaName.toUpperCase();
//        if (entityName != null&&!(bookmark.isMysql()))
//            entityName = entityName.toUpperCase();
        return queryEntityInfo(bookmark,catalogName, schemaName, entityName);
    }
    /**
     * ���ָ����ʵ��·������ʵ����Ϣ�Ĳ�ѯ
     * 
     * @param bookmark
     * @param schemaName
     * @param entityName
     * @return
     * @throws SQLException
     * @throws UnifyException
     */
    public static Entity[] queryEntityInfo(Bookmark bookmark,String catalog,
            String schemaName, String entityName) throws UnifyException,
            SQLException {

        List list = bookmark.getDbInfoProvider().queryEntities(bookmark,
                bookmark.getConnection(),catalog, schemaName, entityName, null);
        return (Entity[]) list.toArray(new Entity[list.size()]);
    }

    /**
     * ��ѯ����Ϣ
     * 
     * @return
     * @throws UnifyException
     * @throws SQLException
     */
    public Column[] queryColumnInfo() throws SQLException, UnifyException {
        Bookmark bookmark = visual.getSelectBookmark();
        if (bookmark == null)
            return null;

        String catalogName= visual.getQueryCatalog();
        String schemaName = visual.getQuerySchema(); //ģʽ��

        String entityName = visual.getQueryEntity(); //ʵ��

        String columnName = visual.getQueryColumn(); //����

        if (catalogName != null&&!(bookmark.isMysql()))
        	catalogName = catalogName.toUpperCase();
        if (schemaName != null&&!(bookmark.isMysql()))
            schemaName = schemaName.toUpperCase();
        if (entityName != null&&!(bookmark.isMysql()))
            entityName = entityName.toUpperCase();
        if (columnName != null&&!(bookmark.isMysql()))
            columnName = columnName.toUpperCase();
        return queryColumnInfo(bookmark,catalogName, schemaName, entityName, columnName);
    }

    /**
     * ��ѯ����Ϣ
     * 
     * @return
     * @throws UnifyException
     * @throws SQLException
     */
    public static Column[] queryColumnInfo(Bookmark bookmark,String catalogName,
            String schemaName, String entityName, String columnName)
            throws SQLException, UnifyException {
        List list = bookmark.getDbInfoProvider().queryColumns(bookmark,
                bookmark.getConnection(),catalogName, schemaName, entityName, columnName);
        return (Column[]) list.toArray(new Column[list.size()]); //ת��Ϊ����������

    }

    /**
     * ��ѯ�е���ϸ��Ϣ
     * 
     * @param bookmark
     * @param schemaName
     * @param entityName
     * @param columnName
     * @return
     * @throws SQLException
     * @throws UnifyException
     */
    public static Column queryColumnDetail(Bookmark bookmark,String catalogName,
            String schemaName, String entityName, String columnName)
            throws SQLException, UnifyException {
        Column column = bookmark.getDbInfoProvider().queryColumnDetail(bookmark,
                bookmark.getConnection(),catalogName, schemaName, entityName, columnName);
        return column;
    }
}
