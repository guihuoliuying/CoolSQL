/*
 * �������� 2006-12-5
 */
package com.cattsoft.coolsql.view.bookmarkview;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.bookmarkBean.BookmarkUpdateOfComboBoxListener;
import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.DateSelector;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.display.CheckModel;
import com.cattsoft.coolsql.pub.display.CommonDataTable;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.SQLArea;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.system.LoadData;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.resultset.ResultSetDataProcess;

/**
 * @author liu_xlin ��ʾ�����ִ�е�sql����ʷ��¼��
 */
public class RecentSQLDialog extends BaseDialog implements ItemListener {

	private static final long serialVersionUID = 1L;

	private static boolean isDisplayed = false;

    //��ǩѡ��ؼ�
    private JComboBox bookmarkSelect = null;

    //����ѡ��ؼ�
    private DateSelector dateSelector = null;

    //��Ϣ��ʾ��ؼ�
    private CommonDataTable table = null;

    //sql��ʾ����
    private SQLArea sqlArea = null;

    //�����Ӧ��ǩ�Ĳ�ѯ����
    //key:aliasname value:date(String)
    private Map lastQuery = null;

    //��sql�������ļ�����
    private HistorySQLAddListener sqlListener = null;

    private BookmarkUpdateOfComboBoxListener listener = null; //��ǩ���¼�����

    public RecentSQLDialog(Frame con) {
        this(con, false);
    }

    public RecentSQLDialog(Dialog con) {
        this(con, false);
    }

    public RecentSQLDialog(Dialog con, boolean isModel) {
        super(con, isModel);
        initPane();
    }

    public RecentSQLDialog(Frame con, boolean isModel) {
        super(con, isModel);
        initPane();
    }

    private void initPane() {
        setTitle(PublicResource.getSQLString("recentsql.dialog.title"));
        JPanel pane = (JPanel) this.getContentPane();
        pane.setLayout(new BorderLayout());

        //��ʼ��
        lastQuery = Collections.synchronizedMap(new HashMap());

        /**
         * ����ѡ�����
         */
        JPanel topPane = new JPanel();
        topPane.setLayout(new BoxLayout(topPane, BoxLayout.X_AXIS));
        //��ǩѡ��
        topPane.add(new JLabel(PublicResource
                .getSQLString("recentsql.dialog.bookmark.label")));
        bookmarkSelect = new JComboBox();
        bookmarkSelect.setPreferredSize(new Dimension(130, 25));
        bookmarkSelect.setEditable(false);
        topPane.add(bookmarkSelect);
        topPane.add(Box.createHorizontalStrut(15));

        /**
         * ��ӶԸ���ǩ�ļ���
         */
        listener = new BookmarkUpdateOfComboBoxListener(bookmarkSelect);
        BookmarkManage.getInstance().addBookmarkListener(listener);
        Bookmark[] bookmarks = (Bookmark[]) BookmarkManage.getInstance()
                .getBookmarks().toArray(new Bookmark[0]);
        for (int i = 0; i < bookmarks.length; i++) {
            bookmarks[i].addPropertyListener(listener);
        }
        //��һ�հ�ť
        RenderButton preBtn = new RenderButton(PublicResource
                .getSQLString("recentsql.dialog.prebtn"));
        preBtn.setToolTipText(PublicResource
                .getSQLString("recentsql.dialog.prebtn.tip"));
        preBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    Date date = dateSelector.getSelectedDate();
                    dateSelector.setSelectedItem(StringUtil
                            .getPreviousDay(date));
                } catch (ParseException e1) {
                    LogProxy.errorReport(RecentSQLDialog.this, e1);
                }

            }
        });
        topPane.add(preBtn);

        topPane.add(Box.createHorizontalStrut(10));

        //��һ�հ�ť
        RenderButton nextBtn = new RenderButton(PublicResource
                .getSQLString("recentsql.dialog.nextbtn"));
        nextBtn.setToolTipText(PublicResource
                .getSQLString("recentsql.dialog.nextbtn.tip"));
        nextBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    Date date = dateSelector.getSelectedDate();
                    dateSelector.setSelectedItem(StringUtil.getNextDay(date));
                } catch (ParseException e1) {
                    LogProxy.errorReport(RecentSQLDialog.this, e1);
                }

            }
        });
        topPane.add(nextBtn);
        //����ѡ��
        topPane.add(Box.createHorizontalStrut(15));
        topPane.add(new JLabel(PublicResource
                .getSQLString("recentsql.dialog.dateselect.label")));
        dateSelector = new DateSelector();
        dateSelector.setEditable(false);
        dateSelector.setPreferredSize(new Dimension(130, 25));
        dateSelector.addItemListener(this);
        topPane.add(dateSelector);

        pane.add(topPane, BorderLayout.NORTH);

        /**
         * ��ʼ�����ִ��sql�ı���ʾ�ؼ� ���ñ�ͷ��Ϣ
         */
        Vector header = new Vector();
        for (int i = 0; i < 5; i++) {
            header.add(PublicResource
                    .getSQLString("recentsql.dialog.table.header" + i));
        }
        table = new CommonDataTable(null, header) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            public String getToolTipText(MouseEvent event) {
                Point p = event.getPoint();
                int column = columnAtPoint(p);
                if (column == 2) //����ƶ���sql��λ��
                {
                    int row = rowAtPoint(p);
                    return getValueAt(row, column).toString();
                }
                return null;
            }
            public boolean isDisplayToolTipSelectMenu()
            {
            	return false;
            }
            //            /**
            //             * ��дJTree��getToolTipLocation()����
            //             * ��tipλ�����������
            //             */
            //            public Point getToolTipLocation(MouseEvent e)
            //            {
            //                return new Point(e.getX(),e.getY()+20);
            //            }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(View.getThemeColor());
        table.getSelectionModel().addListSelectionListener(
                new SqlSelectListener());
        adjustWidth();
        //������Բ鿴�˵���
        JMenuItem item = table.getPopMenuManage().addMenuItem(
                PublicResource.getSQLString("recentsql.dialog.queryselect"),
                new QueryMenuListener(),
                PublicResource.getIcon("TextMenu.icon.execute"), true);
        table.getPopMenuManage().addMenuCheck(new QueryMenuCheck(item));//��Ӹò˵���Ŀ��ù���

        table.setEnableToolTip(false); //������Ϣ��ʾ
        ToolTipManager.sharedInstance().registerComponent(table); //ע����Ϣ��ʾ

        sqlArea = new SQLArea();
        sqlArea.setMinimumSize(new Dimension(420, 200));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(table), new JScrollPane(sqlArea));
        splitPane.setDividerSize(4);
        splitPane.setDividerLocation(350);
        pane.add(splitPane, BorderLayout.CENTER);
        /**
         * �˳���ť���
         */
        JPanel quitPane = new JPanel();
        quitPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        RenderButton quitBtn = new RenderButton(PublicResource
                .getSQLString("recentsql.dialog.quitbtn.label"));
        quitBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }

        });
        quitPane.add(quitBtn);
        pane.add(quitPane, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }
        });
//        this.setSize(620, 550);
        GUIUtil.setFrameSizeToScreen(this,(float)7/8,(float)3/4);
        this.toCenter();
        loadAliasData();
        bookmarkSelect.addItemListener(this);

        //��ʼ����ǰѡ����ǩ�������µ�sql��Ϣ
        try {
            loadRecentSQLInfo();
        } catch (UnifyException e1) {
            LogProxy.errorReport(e1);
        }
        sqlListener = new HistorySQLAddListener();
        RecentSQLManage.getInstance().addPropertyChangeListener(sqlListener);
    }

    /**
     * �رնԻ���ʱ���ͷ������Դ
     *  
     */
    public void closeDialog() {
        /**
         * ��ȥ�Ը���ǩ�ļ���
         */
        Bookmark[] bookmarks = (Bookmark[]) BookmarkManage.getInstance()
                .getBookmarks().toArray(new Bookmark[0]);
        for (int i = 0; i < bookmarks.length; i++) {
            bookmarks[i].removePropertyListener(listener);
        }
        BookmarkManage.getInstance().removeBookmarkListener(listener);

        RecentSQLManage.getInstance().removePropertyChangeListener(sqlListener);

        sqlArea.dispose();//�ر�sql��ʾ����������Դ

        //ɾ��������
        this.removeAll();
        dispose();
        RecentSQLDialog.isDisplayed = false;
    }

    /**
     * ��д���෽��
     */
    public void setVisible(boolean isVisible) {
        if (RecentSQLDialog.isDisplayed) {
            this.removeAll();
            return;
        }
        RecentSQLDialog.isDisplayed = true;
        super.setVisible(isVisible);

    }

    /**
     * ��ȡ��ʷ��¼���ڵ���ʾ״̬
     * 
     * @return
     */
    public static boolean getDisplayState() {
        return RecentSQLDialog.isDisplayed;
    }

    /**
     * �����е���ݿ�������ӵ���ݿ�����ѡ��ؼ���
     *  
     */
    private void loadAliasData() {
//        DefaultComboBoxModel model = (DefaultComboBoxModel) bookmarkSelect
//                .getModel();
//        Set set = BookmarkManage.getInstance().getAliases();
//        Iterator it = set.iterator();
//        while (it.hasNext()) {
//            model.addElement(it.next());
//        }
//
//        //����ǩѡ�����ó�sql�༭��ͼ��ѡ�����ǩ
//        SqlEditorView view = ViewManage.getInstance().getSqlEditor();
//        Bookmark bookmark = (Bookmark) view.getDefaultBookMark();
//        model.setSelectedItem(bookmark.getAliasName());
        GUIUtil.loadBookmarksToComboBox(bookmarkSelect);
    }

    /**
     * ��������еĿ��
     *  
     */
    private void adjustWidth() {
        if (table == null)
            return;
        table.getColumnModel().getColumn(0).setPreferredWidth(50); //������ʾsql�еĿ��
        table.getColumnModel().getColumn(0).setWidth(50); //������ʾsql�еĿ��
        table.getColumnModel().getColumn(2).setPreferredWidth(300); //������ʾsql�еĿ��
        table.getColumnModel().getColumn(2).setWidth(300); //������ʾsql�еĿ��

        table.getColumnModel().getColumn(3).setWidth(120); //������ʾsql�еĿ��
        table.getColumnModel().getColumn(3).setPreferredWidth(120); //������ʾsql�еĿ��
    }

    /**
     * ��ݽ���ѡ�����ǩ�����ڣ����Ҷ�Ӧ��sql��Ϣ
     * 
     * @throws UnifyException
     *             --���鿴������ʽ����ȷ���׳��쳣
     */
    public void loadRecentSQLInfo() throws UnifyException {
        String bookmark = StringUtil.trim((String) bookmarkSelect
                .getSelectedItem());
        if (bookmark.equals(""))
            throw new UnifyException(PublicResource
                    .getSQLString("recentsql.dialog.load.nobookmarkselected"));

        String date = (String) dateSelector.getSelectedItem();
        Vector data = null; //�������
        if (date.equals(StringUtil.getCurrentDate())) //���Ϊ�������ڣ�ֱ�Ӵ��ڴ��л�ȡ��Ϣ
        {
            List tmpList = RecentSQLManage.getInstance().getRecentSQLList(
                    bookmark);
            data = new Vector();
            if (tmpList != null) { //����ִ�е�sql��Ϣ

                Iterator sqlIt = tmpList.iterator();
                int count = 0;
                while (sqlIt.hasNext()) {
                    count++;
                    Object sqlObject = sqlIt.next();

                    if (sqlObject instanceof RecentSQL) {
                        RecentSQL tmpSQL = (RecentSQL) sqlObject;
                        Vector tmpVector = tmpSQL.converToVector(count);
                        data.add(tmpVector);
                    }
                }
            }
        } else {
            try {
                data = LoadData.getInstance().loadRecentSQL(bookmark, date);
            } catch (Exception e) {
                throw new UnifyException(PublicResource
                        .getSQLString("recentsql.dialog.load.xmlerror")+e.getMessage());
            }
        }

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.setDataVector(data, GUIUtil.getColumnIdenfiers(table));
        adjustWidth();

    }

    /**
     * �����sql�����������¼��ʾ�ؼ���
     * 
     * @param sqlOb
     */
    private void addSQLToTable(RecentSQL sqlOb) {
        updateSeqNo();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Vector tmpVector = sqlOb.converToVector(1);
        model.insertRow(0, tmpVector);
    }

    /**
     * ����ؼ���û����ݵ�����м�һ
     *  
     */
    private void updateSeqNo() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            int seqNo = Integer.parseInt(model.getValueAt(i, 0).toString());//��ȡ���еĵ�һ�У����
            seqNo++;
            model.setValueAt(seqNo + "", i, 0);
        }
    }

    /*
     * ����ѯ������ģ����ø÷�������װ����Ϣ
     * 
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) { //���ָ���ѡ���Ժ�,������Ӧ�ĵ���
            try {
                loadRecentSQLInfo();
            } catch (UnifyException e1) {
                LogProxy.errorReport(this, e1);
            }
        }
    }

    /**
     * 
     * @author liu_xlin ���ִ��sql��Ϣ��ʾ��ؼ���ѡ�������
     */
    protected class SqlSelectListener implements ListSelectionListener {

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
         */
        public void valueChanged(ListSelectionEvent e) {
            javax.swing.ListSelectionModel model = (javax.swing.ListSelectionModel) e
                    .getSource();
            int row=model.getMinSelectionIndex();
            if (row == -1)
                return;
            if (!e.getValueIsAdjusting() && row < table.getRowCount()) {
                String sql = (String) table.getValueAt(row, 2); //��ȡsql
                sqlArea.setText(sql);
            }
        }

    }

    /**
     * 
     * @author liu_xlin �ü���������ִ���µ�sql�������ʷ������ʾ����Ϊ���죬������ǩһ�£���ô����ʷ��¼��������Ӧ���һ����¼
     */
    private class HistorySQLAddListener implements PropertyChangeListener {

        /*
         * ���� Javadoc��
         * 
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            if (name.equals("addsql")) {
                RecentSQL sqlObject = (RecentSQL) evt.getNewValue();
                String currentSelectBookmark = bookmarkSelect.getSelectedItem()
                        .toString();
                if (currentSelectBookmark.equals(sqlObject.bookmark()
                        .getAliasName())) //��ǩһ��
                {
                    String currentDate = StringUtil.getCurrentDate();
                    String selectDate = dateSelector.getSelectedItem()
                            .toString();
                    if (currentDate.equals(selectDate)) //����һ��
                        addSQLToTable(sqlObject);
                }
            }
        }

    }

    /**
     * 
     * @author liu_xlin ��ѯ�˵����Ƿ���õ�У����
     */
    private class QueryMenuCheck extends CheckModel {
        public QueryMenuCheck(JMenuItem item) {
            super(item);
        }

        /*
         * ���� Javadoc��
         * 
         * @see com.coolsql.data.display.MenuCheckable#check(java.awt.Component)
         */
        public void check(Component table) {
            if (!(table instanceof JTable))
                return;
            JTable tmpTable = (JTable) table;
            int[] selected = tmpTable.getSelectedRows();
            if (selected == null || selected.length == 0) //���δѡ�У��ò˵������
            {
                if (this.getMenu().isEnabled())
                    this.getMenu().setEnabled(false);
            } else {
                if (!this.getMenu().isEnabled())
                    this.getMenu().setEnabled(true);
            }

        }

    }

    /**
     * 
     * @author liu_xlin ִ�в˵��¼���������.��ѡ����н���ִ��
     */
    private class QueryMenuListener implements ActionListener {

        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            int selectedCount = table.getSelectedRowCount();
            if (selectedCount < 1)
                return;
            Operatable operator = null;
            try {
                operator = OperatorFactory
                        .getOperator(com.cattsoft.coolsql.sql.commonoperator.SQLResultProcessOperator.class);
            } catch (Exception e1) {
                LogProxy.internalError(e1);
                return;
            }
            int[] selectRows = table.getSelectedRows();
            for (int i = 0; i < selectedCount; i++) {
                String bookmark = (String) table.getValueAt(selectRows[i], 1); //��ȡ��ǩ����
                String sql = (String) table.getValueAt(selectRows[i], 2); //��ȡsql
                Bookmark bm = BookmarkManage.getInstance().get(
                        StringUtil.trim(bookmark));
                if (bm == null)
                    continue;
                List argList = new ArrayList();
                argList.add(bm);
                argList.add(sql);
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

    }
}
