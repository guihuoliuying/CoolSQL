package com.cattsoft.coolsql.sql.model;

import java.sql.SQLException;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.util.SqlUtil;
/**
 * A implement to view interface.
 * @author liu_xlin
 */
public class ViewImpl extends EntityImpl
    implements View
{

    public ViewImpl(Bookmark bookmark,String catalog,  String schema, String name, String remark, boolean isSynonym)
    {
        super(bookmark,catalog, schema, name, SqlUtil.VIEW, remark, isSynonym);
    }

    public Integer getSize()
    {
        Integer size = null;
        try
        {
            size = new Integer(getBookmark().getDbInfoProvider().getSize(getBookmark(), getBookmark().getConnection(), getQualifiedName(), getBookmark().getAdapter()));
        }
        catch(SQLException sqlexception) { }
        catch(UnifyException connectionexception) { }
        return size;
    }
}
