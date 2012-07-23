/*
 * Created on 2007-1-31
 */
package com.cattsoft.coolsql.action.common;

import com.cattsoft.coolsql.modifydatabase.insert.InsertDataDialog;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.sql.model.Entity;

/**
 * @author liu_xlin
 *̸�����ʵ����ݵĴ���
 */
public class InsertToEntityCommand implements ActionCommand {

    private Entity entity;  //Ҫ�������ݵ�ʵ��
    public InsertToEntityCommand()
    {
        this(null);
    }
    public InsertToEntityCommand(Entity entity)
    {
        this.entity=entity;
    }
    /* (non-Javadoc)
     * @see com.coolsql.action.common.ActionCommand#exectue()
     */
    public void exectue() {
        
        InsertDataDialog dialog=new InsertDataDialog(GUIUtil.getMainFrame(),false,entity);
        dialog.setVisible(true);
    }

}
