/*
 * �������� 2006-12-1
 */
package com.cattsoft.coolsql.gui.property.database;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.TextEditor;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.view.bookmarkview.RecentSQL;

/**
 * @author liu_xlin ���ִ�е�sql����ӵ����ؼ��к󣬶�ѡ�е�sql�ڵ����������Ϣ�Ĳ鿴
 */
public class SQLProperty extends BaseDialog {
    /**
     * ��ִ�е�sql����
     */
    private RecentSQL sqlData = null;

    private TextEditor bookmark = null; //������ǩ

    private TextEditor sql = null; //sql���

    private TextEditor costTime = null; //����ʱ��

    private TextEditor executeTime = null; //ִ��ʱ��

    public SQLProperty() {
        this(GUIUtil.getMainFrame(),null);
    }
    public SQLProperty(RecentSQL sqlData) {
        this(GUIUtil.getMainFrame(),sqlData);
    }
    public SQLProperty(JDialog owner) {
        this(owner,null);

    }
    public SQLProperty(JDialog owner,RecentSQL sqlData) {
        super(owner);
        this.sqlData=sqlData;
        setTitle(PublicResource.getString("recentsql.property.title"));
        init();

    }
    public SQLProperty(JFrame owner) {
        this(owner,null);

    }
    public SQLProperty(JFrame owner,RecentSQL sqlData) {
        super(owner);
        this.sqlData=sqlData;
        setTitle(PublicResource.getString("recentsql.property.title"));
        init();

    }

    private void init() {
        JPanel mainPane = (JPanel) this.getContentPane();
        mainPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(2, 2, 2, 2);

        //������ǩ
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1;
        mainPane.add(new JLabel(PublicResource
                .getString("recentsql.property.bookmark")), gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        bookmark = new TextEditor();
        bookmark.setEditable(false);
        bookmark.setTooltipEnable(true);
        mainPane.add(bookmark, gbc);

        //sql���
        gbc.gridx=0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        mainPane.add(new JLabel(PublicResource
                .getString("recentsql.property.sql")), gbc);
        gbc.gridx++;
        gbc.weightx =1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        sql = new TextEditor();
        sql.setEditable(false);
        sql.setTooltipEnable(true);
        mainPane.add(sql, gbc);

        //��ʼʱ��
        gbc.gridx=0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        mainPane.add(new JLabel(PublicResource
                .getString("recentsql.property.time")), gbc);
        gbc.gridx++;
        gbc.weightx =1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        executeTime = new TextEditor();
        executeTime.setEditable(false);
        executeTime.setTooltipEnable(true);
        mainPane.add(executeTime, gbc);

        //����ʱ��
        gbc.gridx=0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        mainPane.add(new JLabel(PublicResource
                .getString("recentsql.property.costtime")), gbc);
        gbc.gridx++;
        gbc.weightx =1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        costTime = new TextEditor();
        costTime.setEditable(false);
        costTime.setTooltipEnable(true);
        mainPane.add(costTime, gbc);

        this.addWindowListener(new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    {
                        dispose();
                    }
                }
        );       
        setSize(320, 170);
        toCenter();
        loadData(this.getSqlData());
    }
    public void dispose()
    {
        this.removeAll();
        this.sqlData=null;
        super.dispose();
    }
    /**
     * ��ͼ�ν����Ѿ���ʼ���Ļ���װ�����
     * @param sqlData
     */
    public void loadData(RecentSQL sqlData) {
        if (sqlData == null)
            return;

        if (bookmark != null)
            bookmark.setText(sqlData.bookmark().getAliasName());       
        
        if (sql != null)
        {
            sql.setText(sqlData.getSql());
            this.setTitle(PublicResource.getString("recentsql.property.title")+"("+sqlData.getSql()+")");
        }
        if (costTime != null) {
            costTime.setText(String
                    .valueOf(sqlData.getCostTime())
                    + PublicResource.getSQLString("sql.execute.time.unit"));
        }
        if (executeTime != null) {
            Date tmp=new Date(sqlData.getTime());
            executeTime.setText(StringUtil.transDate(tmp));
        }
    }
    public RecentSQL getSqlData() {
        return sqlData;
    }
    public void setSqlData(RecentSQL sqlData) {
        this.sqlData = sqlData;
    }
}
