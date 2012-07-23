/*
 * �������� 2006-11-30
 */
package com.cattsoft.coolsql.sql.commonoperator;

import java.sql.SQLException;

import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.view.SqlEditorView;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.sqleditor.EditorUtil;

/**
 * @author liu_xlin
 *�����ִ�е�sql��ӵ�sql�༭����
 */
public class AddSqlToEditorOperator implements Operatable {

    /* ���� Javadoc��
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object)
     */
    public void operate(Object arg) throws UnifyException, SQLException {
        if(arg==null)
            throw new UnifyException("no argument");
        if(!(arg instanceof String))
            throw new UnifyException("class type of argument is incorrect:"+arg.getClass());

        String sql=(String)arg;
        SqlEditorView view=ViewManage.getInstance().getSqlEditor();
//        EditorDocument doc=(EditorDocument)view.getEditorPane().getDocument();
        PlainDocument doc=(PlainDocument)view.getEditorPane().getDocument();
        try {
            doc.insertString(view.getEditorPane().getCaretPosition(),sql,EditorUtil.NORMAL_SET);
        } catch (BadLocationException e) {
            throw new UnifyException(PublicResource.getSQLString("sql.addtoeditor.inserterror"));
        }
    }

    /* ���� Javadoc��
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object, java.lang.Object)
     */
    public void operate(Object arg0, Object arg1) throws UnifyException,
            SQLException {

    }

}
