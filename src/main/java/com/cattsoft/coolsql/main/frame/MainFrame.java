/*
 * �������� 2006-6-8
 *
 */
package com.cattsoft.coolsql.main.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;

import com.cattsoft.coolsql.pub.component.BaseFrame;
import com.cattsoft.coolsql.pub.component.MainFrameStatusBar;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.CloseProgressDialog;
import com.cattsoft.coolsql.system.DoOnClosingSystem;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.PropertyManage;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.SystemThread;
import com.cattsoft.coolsql.view.TabView;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ���������
 */
public class MainFrame extends BaseFrame {

	private static final long serialVersionUID = -9055062945934379296L;

	/**
     * split1=bookmarkview+split2
     */
    private JSplitPane split1 = null;

    /**
     * split2=sqlEditor+JTabbedPane
     */
    private JSplitPane split2 = null;

    private ViewManage vm = null;

    public MainFrame() {
        super();
        setTitle(PublicResource.getString("mainFrame.title"));//���ô��ڵı���
        
        vm = ViewManage.getInstance();
        split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false);
        split1.setDividerSize(3);
        split1.setOneTouchExpandable(false);
        View tmpView = vm.getBookmarkView();
        tmpView.setTopText(PublicResource.getString("bookmarkView.title"));
        tmpView.setTopTextIcon(PublicResource.getIcon("bookmarkView.icon"));
        split1.setTopComponent(tmpView); //�����ǩ��ͼ

        split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false);
        split2.setDividerSize(3);
        split2.setOneTouchExpandable(false);

        
        JTabbedPane tab =TabView.getTabPane();
        tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tab.setTabPlacement(JTabbedPane.BOTTOM);
        TabView resultView = vm.getResultView();
        resultView.setTopText(PublicResource.getString("resultView.title"));
        tab.addTab(resultView.getTabViewTitle(),resultView.getTabViewIcon(), resultView); //��ӽ����ͼ
        tab.setToolTipTextAt(tab.indexOfComponent(resultView), resultView.getTabViewTip());
        
        TabView logView = vm.getLogView();
        logView.setTopText(PublicResource.getString("logView.title"));
        tab.addTab(logView.getTabViewTitle(),logView.getTabViewIcon(), logView); //�����־��ͼ
        tab.setToolTipTextAt(tab.indexOfComponent(logView), logView.getTabViewTip());
        
        tmpView = vm.getSqlEditor();
        tmpView.setTopText(PublicResource.getString("sqlEditorView.title"));
        split2.setTopComponent( tmpView); //���sql�༭��ͼ
        split2.setBottomComponent(tab);

        split1.setRightComponent(split2);
        split1.resetToPreferredSizes();

        JPanel pane = (JPanel) this.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(split1, BorderLayout.CENTER);
        
        if(Setting.getInstance().getBoolProperty(PropertyConstant.PROPERTY_SYSTEM_STATUSBAR_DISPLAY, true))
        {
        	MainFrameStatusBar statusBar=new MainFrameStatusBar();
    		pane.add(statusBar,BorderLayout.SOUTH);
        }
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeSystem();
            }
        });
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(d);
        try {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        } catch (Throwable e) {
        }
        int a = (int) d.getWidth() * 2 / 10;
        int b = (int) d.getHeight() / 3;
        split1.setDividerLocation(a);
        split2.setDividerLocation(b);
        split1.setContinuousLayout(false);
        split2.setContinuousLayout(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setToolTip();
    }
    /**
     * ������ʾ��Ϣ�ĳ�ʼ��ʱ��͵ȴ�ʱ��
     *  
     */
    private void setToolTip() {
        ToolTipManager toolTipManage = ToolTipManager.sharedInstance();
        toolTipManage.setInitialDelay(100); //���õ���ʱ��Ϊ0.1��
        toolTipManage.setDismissDelay(100000); //�ȴ�ʱ��100��
    }

    /**
     * ��ȡjdk�汾
     * 
     * @return
     */
    public String getJDKVersion() {
        return System.getProperty("java.version	");
    }

    /**
     * ��ϵͳ�ر�
     *  
     */
    public void closeSystem() {
        int result = JOptionPane.showConfirmDialog(this, PublicResource
                .getString("system.closetask.confirm"), "close confirm",
                JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION)
            return;

        PropertyManage.saveLaunchSetting();
        DoOnClosingSystem closer = DoOnClosingSystem.getInstance();
        CloseProgressDialog progress = closer.getProgressDialog();
        SystemThread st=new SystemThread(progress);
        try {
            closer.close();
            progress.toCenter();
            progress.setVisible(true);
            progress.toFront();
            st.start();
        } catch (Exception e1) {
            LogProxy.outputErrorLog(e1);
        } finally {
//            progress.dispose();
//            System.exit(0);
        }
    }
}
