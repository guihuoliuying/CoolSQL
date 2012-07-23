/*
 * Created on 2007-2-1
 */
package com.cattsoft.coolsql.view.resultset.action;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.cattsoft.coolsql.action.common.ActionCommand;
import com.cattsoft.coolsql.modifydatabase.UpdateRowDialog;
import com.cattsoft.coolsql.modifydatabase.insert.InsertDataDialog;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.sql.SQLResultSetResults;
import com.cattsoft.coolsql.sql.SQLResults;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.DataSetTable;

/**
 * @author liu_xlin
 * �޸���ݿ���ݣ���ִ������˲�������ݺ͸�����������ֲ�����������
 */
public class ModifyDataCommand implements ActionCommand {

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(ModifyDataCommand.class);
	
    private int operatorType;

    private DataSetPanel pane;

    private DataSetTable table;

    public ModifyDataCommand(DataSetPanel pane, int operatorType) {
        this.operatorType = operatorType;
        this.pane = pane;
        if (pane != null) {
            JComponent com = pane.getContent();
            if (com instanceof DataSetTable) {
//                com=(JComponent)((JScrollPane)com).getViewport().getView();
                table = com instanceof DataSetTable ? (DataSetTable) com : null;
            }
            
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coolsql.action.common.ActionCommand#exectue()
     */
    public void exectue() {
        if (pane == null)
        {
        	if(operatorType == ModifyDataAction.INSERT)
        	{
        		InsertDataDialog dialog = new InsertDataDialog(GUIUtil.getMainFrame(),false,null);
                dialog.setVisible(true);
                return;
        	}
        }

        Entity[] entities=null;
        SQLResults tmp = pane.getSqlResult();
        if (tmp != null || tmp.isResultSet()) {
            SQLResultSetResults result = (SQLResultSetResults) tmp;
            entities = result.getEntities();
        }
        if (operatorType == ModifyDataAction.UPDATE) {
            if (entities != null && entities.length == 1) {
                UpdateRowDialog dialog = new UpdateRowDialog(entities[0],
                        getRowDataMap(table), false);
                dialog.setVisible(true);
            }
        } else if (operatorType == ModifyDataAction.INSERT) {
        	Entity entity=null;
        	if(entities == null ||entities.length==0)
        	{
        		JOptionPane.showMessageDialog(GUIUtil.findLikelyOwnerWindow(),stringMgr.getString("resultset.action.insertdata.noentity"),"warning",JOptionPane.WARNING_MESSAGE );
            }else if(entities.length==1)
            {
            	entity=entities[0];
            }else
            {
            	JOptionPane.showMessageDialog(GUIUtil.findLikelyOwnerWindow(), stringMgr.getString("resultset.action.insertdata.toomanyentities"),"warning",JOptionPane.WARNING_MESSAGE );
            }
        	InsertDataDialog dialog = new InsertDataDialog(GUIUtil.getMainFrame(),false,entity);
            dialog.setVisible(true);
        }
    }

    /**
     * ��ȡ�����������ӳ�伯�϶���,�÷���ֻ�����ڸ�������ݡ�key:���� value:������ֵ
     * 
     * @param table
     *            --���Դ���󣺱�ؼ�����
     * @return --�����������֮���ӳ���������ؼ���ѡ���˶��л���û��ѡ���У�����һ���յ�ӳ�����
     */
    private Map<String,Object> getRowDataMap(DataSetTable table) {
        Map<String,Object> map = new HashMap<String,Object>();
        if (table.getSelectedRowCount() != 1)
            return map;

        int row = table.getSelectedRow();
        for (int i = 0; i < table.getColumnCount(); i++) {
            map.put(table.getColumnName(i).toUpperCase(), table.getValueAt(row,
                    i));
        }
        return map;
    }
}
