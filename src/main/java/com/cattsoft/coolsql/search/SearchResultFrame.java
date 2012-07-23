/*
 * �������� 2006-8-19
 */
package com.cattsoft.coolsql.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.component.TextEditor;
import com.cattsoft.coolsql.pub.display.CheckModel;
import com.cattsoft.coolsql.pub.display.CommonDataTable;
import com.cattsoft.coolsql.pub.display.CommonTableModel;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.TableMenu;
import com.cattsoft.coolsql.pub.display.TableScrollPane;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ��ݿ���ҽ��չʾ����
 */
public abstract class SearchResultFrame extends BaseDialog {
	private JLabel kwLabel;  //�ؼ��ֱ�ǩ
	private TextEditor kwText; //��ʾ�ؼ������ݵ��ı���
    private JLabel l; //��ǩ��

    private JLabel processInfo;//��ݵĻ�ȡ���

    private TextEditor count = null; //��ѯ�������

    private CommonDataTable table = null;

    private Bookmark bookmark = null;

    public SearchResultFrame(JFrame con, Bookmark bookmark) {
        super(con, false);
        this.bookmark = bookmark;
        init();
    }

    public SearchResultFrame(JDialog con, Bookmark bookmark) {
        super(con, false);
        this.bookmark = bookmark;
        init();
    }

    public void init() {
        l = new JLabel("");
        kwLabel=new JLabel(PublicResource
                .getSQLString("searchinfo.result.kw.label"));
        JPanel content = (JPanel) this.getContentPane();

        //��ʼ����ʾ��Ϣ��� ��������а�����ǩ��Ϣ�ͽ��������Ϣ
        JPanel topPane = new JPanel();
        topPane.setLayout(new BoxLayout(topPane, BoxLayout.X_AXIS));
        topPane.add(kwLabel);
        kwText=new TextEditor(15);
        kwText.setEditable(false);
        kwText.setBorder(null);
        topPane.add(kwText);
        
        topPane.add(l);
        topPane
                .add(new JLabel("     "
                        + PublicResource
                                .getSQLString("searchinfo.result.count.label")));
        count = new TextEditor(15);
        count.setEditable(false);
        count.setBorder(null);
        topPane.add(count);

        content.setLayout(new BorderLayout());
        content.add(topPane, BorderLayout.NORTH);
        table = initContent();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        ((TableMenu)table.getPopMenuManage()).setExportable(false); //����ʾ��ݵ����˵�
        ((TableMenu)table.getPopMenuManage()).setPrintable(false); //����ʾ��ӡ�˵���

        //������Բ鿴�˵���
        JMenuItem item = table
                .getPopMenuManage()
                .addMenuItem(
                        PublicResource
                                .getSQLString("searchinfo.result.menu.moreproertylable"),
                        new MoreProperty(),
                        PublicResource
                                .getSQLIcon("searchinfo.result.menu.moreproertyicon"),
                        false);
        table.getPopMenuManage().addMenuCheck(new DetailMenuCheck(item));//��Ӹò˵���Ŀ��ù���
        /**
         * ���չ����Ӧ�ڵ�Ĳ˵���
         */
        ActionListener redirectAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    Identifier id = getSelectObject();
                    BookmarkTreeUtil util = BookmarkTreeUtil.getInstance();

                    util.selectNode(id,true);
                } catch (UnifyException e1) {
                    LogProxy.errorReport(e1);
                }
            }

        };
        item = table.getPopMenuManage().addMenuItem(
                PublicResource
                        .getSQLString("searchinfo.result.menu.redirectlabel"),
                redirectAction,
                PublicResource
                        .getSQLIcon("searchinfo.result.menu.redirecticon"),
                false);
        table.getPopMenuManage().addMenuCheck(new DetailMenuCheck(item));//��Ӹò˵���Ŀ��ù���
        content.add(new TableScrollPane(table), BorderLayout.CENTER);

        //��ӹرհ�ť
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout());
        RenderButton shutDown = new RenderButton(PublicResource
                .getSQLString("searchinfo.result.dialog.button.close"));
        shutDown.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                closeFrame();
            }

        });
        buttonPane.add(shutDown);
        processInfo = new JLabel("");
        buttonPane.add(processInfo); //��ӽ����Ϣ
        content.add(buttonPane, BorderLayout.SOUTH);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeFrame();
            }
        });

        setSize(getDefaultSize());
        centerToOwner();
    }

    public void closeFrame() {
        this.removeAll();
        this.dispose();
    }

    /**
     * ������Ա�����ʽչʾ
     */
    protected abstract CommonDataTable initContent();

    /**
     * ��ѡ�е�ʵ���������Ϣת��ΪIdentifier����
     * 
     * @return
     */
    protected abstract Identifier getSelectObject() throws UnifyException;

    /**
     * ����ȱʡ�Ĵ��ڴ�С
     * 
     * @return
     */
    private Dimension getDefaultSize() {
        Dimension d = new Dimension(400, 250);
        return d;
    }

    /**
     * ��ѯ��������
     * 
     * @param txt
     */
    public void setPrompt(String txt) {
        if (l != null)
            l.setText("  "+txt);
    }
    /**
     * ���ùؼ���
     * @param str
     */
    public void setKeyword(String str)
    {
    	if(str==null||str.trim().equals(""))
    		return;
    	kwText.setForeground(Color.blue);
    	kwText.setText(str);
    }
    /**
     * ��ȡ������ݽ����Ϣ
     * 
     * @param txt
     */
    public void setProcessInfo(String txt) {
        processInfo.setText(txt);
        processInfo.updateUI();
    }

    /**
     * ���ò�ѯ�������
     * 
     * @param count
     */
    public void setCount(int count) {
        this.count.setText(String.valueOf(count));
    }

    /**
     * ʹ��ǰ���ھ���
     *  
     */
    public void centerToOwner() {
        GUIUtil.centerFrameToFrame(this.getOwner(), this);
    }

    /**
     * @return ���� table��
     */
    public CommonDataTable getTable() {
        return table;
    }

    /**
     * ֪ͨ��ݱ?��ݽṹ����仯
     *  
     */
    public void notifyTable() {
        CommonTableModel model = (CommonTableModel) table.getModel();
        model.fireTableStructureChanged();
    }

    /**
     * ���һ�����
     * 
     * @param row
     */
    public abstract void addRow(Object[] row);
    
    /**
     * ���µ����û�����
     *
     */
    public abstract void adjustGUI();

    //    /**
    //     * ���һ����ݣ����ǲ��ı���չʾ
    //     * @param row
    //     */
    //    public abstract void addRowNoFire(Object[] row);
    /**
     * �÷������ڲ鿴��ѯ����е���������ϸ��Ϣ
     *  
     */
    public abstract void detailInfo() throws UnifyException, SQLException;

    protected class MoreProperty implements ActionListener {

        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            try {
                detailInfo();
            } catch (UnifyException e1) {
                LogProxy.errorReport(e1);
            } catch (SQLException e1) {
                LogProxy.SQLErrorReport(e1);
            }
        }

    }

    /**
     * 
     * @author liu_xlin ����ϸ�˵���У��
     */
    private class DetailMenuCheck extends CheckModel {
        public DetailMenuCheck(JMenuItem item) {
            super(item);
        }

        /*
         * ���� Javadoc��
         * 
         * @see com.coolsql.data.display.MenuCheckable#check(javax.swing.JTable,
         *      javax.swing.JMenuItem)
         */
        public void check(Component table) {
            if (!(table instanceof JTable))
                return;
            JTable tmpTable = (JTable) table;
            int[] selected = tmpTable.getSelectedRows();
            if (selected == null || selected.length != 1) //���δѡ�У�����ѡ�ж��У��ò˵������
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
     * @return ���� bookmark��
     */
    public Bookmark getBookmark() {
        return bookmark;
    }

    /**
     * @param bookmark
     *            Ҫ���õ� bookmark��
     */
    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }
}
