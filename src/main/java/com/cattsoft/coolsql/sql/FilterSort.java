package com.cattsoft.coolsql.sql;

import java.util.ArrayList;
/**
 * @author liu_xlin
 */
public class FilterSort
{

    public FilterSort()
    {
        filterList = new ArrayList();
        sortList = new ArrayList();
        ascDescList = new ArrayList();
    }
    /**
     * ��ӹ�������
     */
    public void addFilter(String column, String operator, String value, boolean isString)
    {
        FilterRow row = new FilterRow();
        row.column = column;
        row.operator = operator;
        row.value = value;
        row.isString = isString;
        filterList.add(row);
    }

    public void clearFilters()
    {
        filterList.clear();
    }

    public void addSort(String column, String ascDesc)
    {
        int ind = sortList.indexOf(column);
        if(ind < 0)
        {
            sortList.add(column);
            ascDescList.add(ascDesc);
            return;
        }
        if(ind < ascDescList.size())
            ascDescList.remove(ind);
        ascDescList.add(ind, ascDesc);
    }

    public void removeSort(String column)
    {
        int ind = sortList.indexOf(column);
        if(ind < 0)
        {
            return;
        } else
        {
            sortList.remove(ind);
            ascDescList.remove(ind);
            return;
        }
    }
    /**
     * ������������ʽ��ϳ�sqlԪ��
     */
    public String toString()
    {
        StringBuffer text = new StringBuffer();
        if(filterList.size() > 0)
        {
            text.append(" WHERE ");
            for(int i = 0; i < filterList.size(); i++)
            {
                FilterRow row = (FilterRow)filterList.get(i);
                text.append(row.column);
                text.append(" ");
                text.append(row.operator);
                text.append(" ");
                if(row.isString)
                    text.append(escape(row.value));
                else
                    text.append(row.value);
                text.append(" ");
                if(i < filterList.size() - 1)
                    text.append("AND ");
            }

        }
        if(sortList.size() > 0)
        {
            text.append(" ORDER BY ");
            for(int i = 0; i < sortList.size(); i++)
            {
                String value = (String)sortList.get(i);
                text.append(value);
                text.append(" ");
                text.append(ascDescList.get(i));
                if(i < sortList.size() - 1)
                    text.append(", ");
            }

        }
        return text.toString();
    }
    /**
     * �����ı��ֶΣ��ֶε�ֵǰ����ϴ����
     * @param original
     * @return
     */
    public static String escape(String original)
    {
        return '\'' + original + '\'';
    }
    
    /**
     * �����������
     */
    private ArrayList filterList;
    /**
     * ���������ֶ� (group by)
     */
    private ArrayList sortList;
    /**
     * ��������ʽ ���������
     */
    private ArrayList ascDescList;
}
