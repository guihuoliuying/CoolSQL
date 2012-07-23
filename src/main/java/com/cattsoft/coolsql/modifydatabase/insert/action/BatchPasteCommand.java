/*
 * Created on 2007-1-30
 */
package com.cattsoft.coolsql.modifydatabase.insert.action;

import java.awt.Cursor;

import com.cattsoft.coolsql.action.common.ActionCommand;
import com.cattsoft.coolsql.modifydatabase.insert.EditorTable;
import com.cattsoft.coolsql.modifydatabase.insert.EditorTableModel;

/**
 * @author liu_xlin
 *�¼��������ɸýӿڽ�����Ӧ�߼�����
 */
public class BatchPasteCommand implements ActionCommand{

    private EditorTable table=null;
    public BatchPasteCommand(EditorTable table)
    {
        this.table=table;
    }
    /* (non-Javadoc)
     * @see com.coolsql.action.common.ActionCommand#exectue()
     */
    public void exectue() {
        if(table==null)
            return;
        try {
            table.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            EditorTableModel model = (EditorTableModel) table
                    .getModel();
            model.batchPaste(table.getSelectedRow(), table
                    .getSelectedColumn(),table);
        } finally {
            table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        
    }
    
}
