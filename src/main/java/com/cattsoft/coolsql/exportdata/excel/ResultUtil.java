/*
 * �������� 2006-9-1
 *
 * 
 */
package com.cattsoft.coolsql.exportdata.excel;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.cattsoft.coolsql.sql.util.TypesHelper;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ����?����
 */
public class ResultUtil {

    /**
     * ��������Ƿ����Lob�ֶΡ�
     * 
     * @param set
     * @return 0:��lob�ֶ� 1:����󣨰���clob,blob��2:�ݲ���
     * @throws SQLException
     */
    private static int checkLobOfResult(ResultSet set) throws SQLException {
        ResultSetMetaData metaData = set.getMetaData();
        int colCount = metaData.getColumnCount();
        for (int i = 0; i < colCount; i++) {
            int type = metaData.getColumnType(i + 1);
            if (TypesHelper.isLob(type)) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * ��ȡ�����ܼ�¼�� 
     * 
     * @param set
     * @return �����ִ�����ô����-1
     */
    public static int countResultRow(ResultSet set) {
        int count = -1;
        try {
            if (checkLobOfResult(set) == 1) //������д��ڴ�����ֶΣ�����-1
                return -1;

            set.last();
            count = set.getRow();
            set.beforeFirst();
        } catch (Throwable e) {
            LogProxy.outputErrorLog(e);
            count = -1;
        }
        return count;
    }

    /**
     * ͨ������ȡExcel�������
     * 
     * @param set
     * @return
     * @throws ExcelProcessException
     */
    public static ExcelComponentSet getExcelSet(ResultSet set)
            throws ExcelProcessException {
        CellDefined[] defined = CellDefined.createInstanceOfResult(set);
        ExcelComponentSet setting = new ExcelComponentSet();
        setting.setDisplayHead(true);
        setting.setHeadDefined(defined);
        return setting;
    }
}
