/*
 * �������� 2006-9-19
 */
package com.cattsoft.coolsql.sql.commonoperator;

import java.awt.Container;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.gui.property.database.ColumnProperty;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.search.QueryDBInfo;
import com.cattsoft.coolsql.sql.model.Column;
import com.cattsoft.coolsql.sql.model.Entity;

/**
 * @author liu_xlin
 *�����Բ鿴������
 */
public class ColumnPropertyOperator implements Operatable {

    /* �����Ի�ȡ
     * ������Ҫ�������ã�
     * 0����ǩ
     * 1������
     * 2:ģʽ��
     * 3��ʵ����
     * 4: ����
     * 5: ������
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object)
     */
    public void operate(Object arg) throws UnifyException, SQLException {
        if(arg==null)
            throw new UnifyException("no operate object!");
        if(!(arg instanceof List))
        {
            throw new UnifyException("operate object error! class:"+arg.getClass());
        }
        
        List<?> list=(List<?>)arg;
        if(list.size()!=6)
            throw new UnifyException("Getting column property failed! The number of arguments is illegal");

        Bookmark bookmark=null;
        String catalog=null;
        String schema=null;
        String entity=null;
        String column=null;
        Container con=null;
        try
        {
            bookmark=(Bookmark)list.get(0);
            catalog=(String)list.get(1);
            schema=(String)list.get(2);
            entity=(String)list.get(3);
            column=(String)list.get(4);
            con=(Container)list.get(5);
        }catch(ClassCastException e)
        {
            throw new UnifyException("class of argument is error");
        }
        list.clear(); //�����ݵ�Ӧ��
        if(bookmark!=null&&entity!=null&&column!=null) //���в�����Ϊ��
        {
            Column data=QueryDBInfo.queryColumnDetail(bookmark,catalog,schema,entity,column);
            if(data==null)
                throw new UnifyException("result is not unique");
            
            boolean isDialog=con instanceof JDialog?true:false;
            ColumnProperty cp=null;
            if(isDialog)
                cp=new ColumnProperty((JDialog)con,data,bookmark);
            else
                cp=new ColumnProperty((JFrame)con,data,bookmark);
            cp.initData(data);
            cp.setVisible(true);
        }else
        {
        	if(bookmark==null)
        		throw new IllegalArgumentException("can't get bookmark object");
        	if(entity==null)
        		throw new IllegalArgumentException("can't get entity object");
        	if(column==null)
        		throw new IllegalArgumentException("can't get column object");
        }
    }

    /* ����������
     * arg0:�����������Դ��ڵĸ�����
     * arg1���ж���(Column)
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object, java.lang.Object)
     */
    public void operate(Object arg0, Object arg1) throws UnifyException,
            SQLException {
        if(arg0==null||arg1==null)
            throw new UnifyException("no operate object!");
            
        if(!(arg0 instanceof Container))
        {
            throw new UnifyException("operate object(Container) error! class:"+arg0.getClass());
        }else if(!(arg1 instanceof Column))
        {
            throw new UnifyException("operate object(Column) error! class:"+arg0.getClass());
        }
        
        Container con=(Container)arg0;
        Column col=(Column)arg1;
        Entity entity=col.getParentEntity();
        List array=new ArrayList();
        array.add(entity.getBookmark());
        array.add(entity.getCatalog());
        array.add(entity.getSchema());
        array.add(entity.getName());
        array.add(col.getName());
        array.add(con);
        operate(array);
    }

}
