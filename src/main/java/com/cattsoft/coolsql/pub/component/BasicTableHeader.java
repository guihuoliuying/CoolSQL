/*
 * �������� 2006-12-7
 */
package com.cattsoft.coolsql.pub.component;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import com.cattsoft.coolsql.view.resultset.EditableTableHeaderUI;

/**
 * @author liu_xlin
 *
 */
public class BasicTableHeader extends JTableHeader {

	private static final long serialVersionUID = 1L;
	public BasicTableHeader()
    {
        super();
        this.setUI(new EditableTableHeaderUI());
    }
    public BasicTableHeader(TableColumnModel columnModel)
    {
        super(columnModel);
        this.setUI(new EditableTableHeaderUI());
    }
}
