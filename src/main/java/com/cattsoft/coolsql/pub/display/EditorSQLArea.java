/*
 * Created on 2007-1-25
 */
package com.cattsoft.coolsql.pub.display;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.resultset.ResultSetDataProcess;
import com.cattsoft.coolsql.view.sqleditor.ScriptParser;

/**
 * @author liu_xlin �ɱ༭sql��ʾ���
 */
public class EditorSQLArea extends JPanel {

    private JCheckBoxMenuItem wrapItem = null; //�Ƿ���

    private JMenuItem runItem = null; //ִ��sql

    private SQLArea sqlArea = null; //sql��ʾ����

    private StatusBar statueBar = null; //״̬��

    private Bookmark bookmark = null;

    public EditorSQLArea(Bookmark bookmark) {
        super();
        this.bookmark = bookmark;
        sqlArea = new SQLArea();
        sqlArea.setWrap(false);
        
        initPanel();
        addMenu();
    }

    protected void initPanel() {
        this.setLayout(new BorderLayout());
        add(new JScrollPane(sqlArea), BorderLayout.CENTER);
        statueBar = new StatusBar();
        statueBar.setMinimumSize(new Dimension(30, 50));
        add(statueBar, BorderLayout.SOUTH);
    }

    /**
     * �����˵�
     *  
     */
    protected void addMenu() {

        //�Ƿ��Զ����в˵���
        wrapItem = sqlArea.insertCheckBoxMenuItem(PublicResource
                .getString("logView.popmenu.iswrap"), null, null);
        ActionListener wrapListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                sqlArea.setWrap(wrapItem.getState());
            }

        };
        wrapItem.addActionListener(wrapListener);
        sqlArea.getAreaMenuManage().addMenuCheck(new MenuCheckable() {

            public void check(Component table) {
                wrapItem.setSelected(sqlArea.isWrap());

            }

        });

        if (isExecuteSQL()) {  //�����Ҫ���ִ��sql�Ĺ��ܲ˵���
            //ִ��sql�˵���

            ActionListener runListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (bookmark == null) {
                        LogProxy
                                .errorMessage(
                                        EditorSQLArea.this,
                                        PublicResource
                                                .getSQLString("sql.execute.nobookmark"));
                        return;
                    }
                    if (!bookmark.isConnected()) {
                        LogProxy.errorMessage(PublicResource
                                .getSQLString("database.notconnected")
                                + bookmark.getAliasName());
                        return;
                    }
                    ScriptParser sqlParser=new ScriptParser(sqlArea.getSelectedText());
//                    List list = SQLParser.parse(sqlArea.getSelectedText());

                    if (sqlParser.getSize() < 1) {
                        LogProxy.message(PublicResource
                                .getSQLString("sql.execute.nosql"),
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (bookmark == null) {
                        LogProxy.message(PublicResource
                                .getSQLString("sql.execute.nobookmark"),
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    Operatable operator = null;
                    try {
                        operator = OperatorFactory
                                .getOperator(com.cattsoft.coolsql.sql.commonoperator.SQLResultProcessOperator.class);
                    } catch (Exception e1) {
                        LogProxy.internalError(e1);
                        return;
                    }
                    for (int i = 0; i <sqlParser.getSize(); i++) {
                        List argList = new ArrayList();
                        argList.add(bookmark);
                        argList.add(sqlParser.getCommand(i));
                        argList.add(new Integer(ResultSetDataProcess.EXECUTE));//����sql��ִ�д������ͣ��״�ִ��
                        try {
                            operator.operate(argList);
                        } catch (UnifyException e2) {
                            LogProxy.errorReport(e2);
                        } catch (SQLException e2) {
                            LogProxy.SQLErrorReport(e2);
                        }
                    }

                }

            };
            runItem = sqlArea.insertMenuItem(PublicResource
                    .getSQLString("sqlarea.popmenu.execute.label"),
                    PublicResource.getIcon("TextMenu.icon.execute"),
                    runListener);
            sqlArea.getAreaMenuManage().addMenuCheck(new MenuCheckable() {

                public void check(Component table) {
                    String txt = StringUtil.trim(sqlArea.getSelectedText());
                    if (txt.equals(""))
                        GUIUtil.setComponentEnabled(false, runItem);
                    else
                        GUIUtil.setComponentEnabled(true, runItem);
                }

            });
        }
    }

    /**
     *  
     */
    public void dispose() {
        sqlArea.dispose();
    }

    /**
     * ����״̬����Ϣ
     * 
     * @param info
     */
    public void setStatueInfo(String info) {
        statueBar.setText(info);
    }

    /**
     * ���ĵ�ģ���������ı�����
     * 
     * @param txt
     *            --׷�ӵ��ı�����
     * @throws BadLocationException
     */
    public void append(String txt) throws BadLocationException {
        sqlArea.append(txt);
    }

    /**
     * @param t
     */
    public void setText(String t) {
        sqlArea.setText(t);
    }
    /**
     * �Ƿ���и�������
     * @return  --true���������� false��������
     */
    public boolean isHighlight()
    {
        return sqlArea.isHighlight();
    }
    /**
     * �����Ƿ���������ĵ������е�����
     * @param isHighlight true:�������� false:��������
     */
    public void setHighlight(boolean isHighlight)
    {
        sqlArea.setHighlight(isHighlight);
    }
    /**
     * @return Returns the bookmark.
     */
    public Bookmark getBookmark() {
        return bookmark;
    }

    /**
     * @param bookmark
     *            The bookmark to set.
     */
    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }

    /**
     * ���塰ִ��sql���˵����Ƿ���ʾ
     * 
     * @return true����ʾ false:����ʾ
     */
    public boolean isExecuteSQL() {
        return true;
    }
}
