/*
 * �������� 2006-12-15
 */
package com.cattsoft.coolsql.view.resultset;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.DefinableToolBar;
import com.cattsoft.coolsql.pub.component.IconButton;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.resultset.action.EditDataSetTableAction;
import com.cattsoft.coolsql.view.resultset.action.QueryAllRowsOfShortcutAction;

/**
 * @author liu_xlin �Ե������ڵ���ʽչʾ�����ͼ��ĳһҳ����Ϣ
 */
public class ShortcutDialog extends BaseDialog {

	private static final long serialVersionUID = 1L;

	/**
     * ͼ�갴ť����
     */
    private IconButton prePage = null; //ǰһҳ��ť

    private IconButton nextPage = null; //��һҳ��ť

    private IconButton queryAllPage = null; //��ѯ������

    private IconButton refresh = null; //ˢ����ݵĻ�ȡ

    /**
     * ��ݽ��չʾ�����
     */
    private DataSetPanel dataPane = null;

    private DataUpdateListener listener = null; //�����ݷ���仯�ļ�����

    public ShortcutDialog(DataSetPanel dataPane) {
        super(GUIUtil.getMainFrame(), false);
        this.dataPane = dataPane;
        initPane();
    }

    public void addDataUpdateListener() {
        dataPane.addPanelPropertyChangeListener(listener);
    }

    public void removeDataUpdateListener() {
        dataPane.removePanelPropertyChangeListener(listener);
    }

    protected void initPane() {
        JPanel pane = (JPanel) getContentPane();
        pane.setLayout(new BorderLayout());

        JToolBar toolBar = createTools();
        pane.add(toolBar, BorderLayout.NORTH);

        if (dataPane != null) {
            pane.add(dataPane, BorderLayout.CENTER);
            this.setTitle(PublicResource.getString("shortcutdialog.title")
                    + dataPane.getBookmark().getAliasName() + ")");
        }

        /**
         * �������
         */
        JPanel taskPane = new JPanel();
        taskPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        //���ذ�ť
        RenderButton returnTab = new RenderButton(PublicResource
                .getString("shortcutdialog.btn.returntab"));
        returnTab.setToolTipText(PublicResource
                .getString("shortcutdialog.btn.returntab.tip"));
        returnTab.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                closeDilaogAndReturn();
            }

        });
        taskPane.add(returnTab);

        //�رհ�ť
        RenderButton quit = new RenderButton(PublicResource
                .getString("shortcutdialog.btn.quit"));
        quit.setToolTipText(PublicResource
                .getString("shortcutdialog.btn.quit.tip"));
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }

        });
        taskPane.add(quit);

        pane.add(taskPane, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                //ѡ��رպ��Ƿ񷵻ؽ����ͼ��
                int result = JOptionPane.showConfirmDialog(ShortcutDialog.this,
                        PublicResource.getString("shortcutdialog.closeprompt"),
                        "confirm close", JOptionPane.YES_NO_CANCEL_OPTION);
                if (result == JOptionPane.YES_OPTION) //ѡ���ǣ��رմ��ڣ������ؽ��
                    closeDilaogAndReturn();
                else if (result == JOptionPane.NO_OPTION) //ѡ���ֻ�رմ���
                    closeDialog();
            }
        });

        GUIUtil.setFrameSizeToScreen(this,(float)7/8,(float)3/4);
        
        listener = new DataUpdateListener();
        if (dataPane != null)
        {
            checkButtonAvailable();
            addDataUpdateListener();           
        }
    }

    /**
     * �������߰�ť����������ͼ�е�ͼ�갴ť����
     * 
     * @return --���߰�ť������
     */
    private JToolBar createTools() {
        DefinableToolBar toolBar = new DefinableToolBar("shortcutFrameTools");
//        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
//        toolBar.setBorderPainted(true);
//        toolBar.setFloatable(true);
//        toolBar.setRollover(true);
//        toolBar.setMargin(new Insets(0,0,0,0));
        toolBar.setPreferredSize(new Dimension(200,30));
        
        prePage = toolBar
                .addIconButton(
                        PublicResource
                                .getIcon("resultView.iconbutton.previouspage.icon"),
                        new EditDataSetTableAction(ResultSetDataProcess.PREVIOUS,dataPane),
                        PublicResource
                                .getString("resultView.iconbutton.previouspage.tooltip")); //ǰһҳ��ݰ�ť
        nextPage = toolBar.addIconButton(PublicResource
                .getIcon("resultView.iconbutton.nextpage.icon"),
                new EditDataSetTableAction(ResultSetDataProcess.NEXT,dataPane),
                PublicResource
                        .getString("resultView.iconbutton.nextpage.tooltip")); //��һҳ��ݰ�ť

        queryAllPage = toolBar
                .addIconButton(
                        PublicResource
                                .getIcon("resultView.iconbutton.queryallpage.icon"),
                        new QueryAllRowsOfShortcutAction(dataPane),
                        PublicResource
                                .getString("resultView.iconbutton.queryallpage.tooltip")); //��ѯ������ݰ�ť

        refresh = toolBar.addIconButton(PublicResource
                .getIcon("resultView.iconbutton.refresh.icon"),
                new EditDataSetTableAction(ResultSetDataProcess.REFRESH,dataPane),
                PublicResource
                        .getString("resultView.iconbutton.refresh.tooltip")); //ˢ����ݰ�ť

        return toolBar;
    }

    /**
     * �رնԻ��򴰿�
     *  
     */
    public void closeDialog() {
        clearDialog();
        dataPane.setRemoving(true);
        if (dataPane != null) //����ɾ��ʱ,��ո�����
        {
            dataPane.removeComponent();
        }
        dataPane = null;
    }

    /**
     * �رմ��ڣ����ҽ���ݷ��ص������ͼ��
     *  
     */
    public void closeDilaogAndReturn() {
        clearDialog();
        dataPane.setVisible(true); //����������Ŀ�����
        dataPane.setRemoving(true); //�ָ�Ϊ����ɾ��״̬
        //��ӶԽ�����ļ���
        dataPane.addPanelPropertyChangeListener(ViewManage.getInstance().getResultView()
                .getResultSetListener());
        //����������ͼ��
        ViewManage.getInstance().getResultView().addTab(dataPane,
                dataPane.getBookmark().getAliasName(), dataPane.getSql());
        dataPane = null;
    }

    private void clearDialog() {
        removeDataUpdateListener();
        
        removeAll();
        dispose();

    }

    private void checkButtonAvailable() {
        if (!dataPane.isReady()) { //���������δ׼����,����ǰ����,�Լ���ѯ���С�ˢ�°�ť��Ϊ������
            GUIUtil.setComponentEnabled(false, prePage);
            GUIUtil.setComponentEnabled(false, nextPage);
            GUIUtil.setComponentEnabled(false, queryAllPage);
            GUIUtil.setComponentEnabled(false, refresh);
        } else {
            if (dataPane.getSqlResult().isResultSet()) { //���Ϊ��ѯ���͵Ľ��
                if (dataPane.hasPrevious()) //����ܹ���ǰ�������
                    GUIUtil.setComponentEnabled(true, prePage);
                else
                    GUIUtil.setComponentEnabled(false, prePage);

                if (dataPane.hasNext()) //�ܹ����������
                    GUIUtil.setComponentEnabled(true, nextPage);
                else
                    GUIUtil.setComponentEnabled(false, nextPage);

                GUIUtil.setComponentEnabled(true, refresh);//��ˢ�°�ť��Ϊ����
                GUIUtil.setComponentEnabled(true, queryAllPage); //ͬ��
            } else //���Ϊ���»���ɾ�����ͽ��,����ǰ,���,��ѯ�������,ˢ�µȰ�ť��Ϊ������
            {
                GUIUtil.setComponentEnabled(false, prePage);
                GUIUtil.setComponentEnabled(false, nextPage);
                GUIUtil.setComponentEnabled(false, queryAllPage);
                GUIUtil.setComponentEnabled(false, refresh);
            }
        }
    }

    private class DataUpdateListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("sqlResult")) //����������ִ�н����仯,�԰�ť�����Խ���У��
            {
                checkButtonAvailable();
            }
        }
    }
}
