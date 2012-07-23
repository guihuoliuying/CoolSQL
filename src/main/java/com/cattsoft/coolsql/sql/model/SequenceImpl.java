
package com.cattsoft.coolsql.sql.model;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.util.SqlUtil;

/**
 * A implement to sequence interface.
 * @author liu_xlin
 */
class SequenceImpl extends EntityImpl
    implements Sequence
{

    public SequenceImpl(Bookmark bookmark,String catalog, String schema, String name, String remark, boolean isSynonym)
    {
        super(bookmark,catalog, schema, name, SqlUtil.SEQUENCE, remark, isSynonym);
    }

    public void refresh()
    {
        
    }
    public Column[] getColumns()
    {
        return new Column[0];
    }

    public Column getColumn(String columnName)
    {
        return null;
    }
}
