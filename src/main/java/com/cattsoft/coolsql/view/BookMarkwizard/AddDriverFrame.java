/*
 * �������� 2006-6-2
 */
package com.cattsoft.coolsql.view.BookMarkwizard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.cattsoft.coolsql.action.common.OkButtonAction;
import com.cattsoft.coolsql.gui.SelectExtraFileDialog;
import com.cattsoft.coolsql.pub.component.CommonFrame;
import com.cattsoft.coolsql.pub.component.FileSelectDialog;
import com.cattsoft.coolsql.pub.component.ListCellRender;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.display.FileSelectFilter;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.loadlib.LoadJar;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.PropertyManage;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.mouseEventProcess.AddDriverListMouse;

/**
 * @author liu_xlin ѡ������򴰿�
 */
public class AddDriverFrame extends CommonFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JList jarList;

    private DefaultListModel model;

    private JButton addJar;
    private JButton selectJar;

    private Map<String,String> map = new HashMap<String,String>();

    public AddDriverFrame(JDialog f, View view) {
        super(f, true, view);
        initGUI(f);
    }
    public AddDriverFrame(JFrame f, View view) {
        super(f, true, view);
        initGUI(f);
    }
    private void initGUI(Window window)
    {
    	this.setDescribeText(PublicResource.getString("bookmark.adddriver"));
        this.enableOkButton(new OkButtonAction(this), true);

        this.setSize(450, 350);
        this.centerToFrame(window);
        setTitle(PublicResource.getString("bookmark.adddrivertitle"));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    public JComponent initDialog() {
        JPanel main = new JPanel();

        main.setLayout(new BorderLayout());

        JPanel jarSelect = new JPanel();
        jarSelect.setBackground(View.getThemeColor());
        jarSelect.setLayout(new BorderLayout());
        jarList = new JList();
        model = new DefaultListModel();
        jarList.setCellRenderer(new ListCellRender());
        jarList.setModel(model);

        JScrollPane scroll = new JScrollPane(jarList);
        JLabel listLabel = new JLabel(PublicResource
                .getString("driverlist.label"));
        jarSelect.add("North", listLabel);
        jarSelect.add("Center", scroll);

//        JPanel buttons = new JPanel();
        Box bPanel = Box.createHorizontalBox();
		Box buttons = Box.createVerticalBox();

		Dimension min = new Dimension(120, 24);
		Dimension max = new Dimension(160, 24);
        addJar = new RenderButton(PublicResource.getString("buttonLabel.addfile"));
        addJar.setMaximumSize(max);
        addJar.setMinimumSize(min);
        selectJar= new RenderButton(PublicResource.getString("buttonLabel.selectfile"));
        selectJar.setMaximumSize(max);
        selectJar.setMinimumSize(min);
        
        buttons.add(Box.createVerticalStrut(2));
        buttons.add(addJar);
        buttons.add(Box.createVerticalStrut(2));
        addJar.addActionListener(this);
        buttons.add(selectJar);
        selectJar.addActionListener(this);
        bPanel.add(Box.createHorizontalStrut(15));
        bPanel.add(buttons);
        bPanel.add(Box.createHorizontalStrut(5));
        
        main.add("Center", jarSelect);
        main.add("East", buttons);
        jarList.addMouseListener(new AddDriverListMouse(jarList));

        return main;
    }

    public Dimension getContentSize() {
        return new Dimension(200, 150);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == addJar) {
            FileSelectDialog chooser = new FileSelectDialog();
            FileSelectFilter jarFilter = new FileSelectFilter(".jar",
                    "jarFile(*.jar)");
            chooser.addChoosableFileFilter(jarFilter);
            chooser.addChoosableFileFilter(new FileSelectFilter(".zip",
                    "zipFile(*.zip)"));
            jarFilter = new FileSelectFilter(new String[]{".jar",".zip"},
            "resourceFile(*.jar,*.zip)");
            chooser.setFileFilter(jarFilter);
            chooser
                    .setCurrentDirectory(new File(
                    		PropertyManage.getSystemProperty().getSelectFile_addDriver()));
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            chooser.setMultiSelectionEnabled(true);
            int select = chooser.showOpenDialog(this);
            if (select == JFileChooser.APPROVE_OPTION) {

                File[] tmp = chooser.getSelectedFiles();
                if (tmp == null) {
                    return;
                }
                PropertyManage.getSystemProperty().setSelectFile_addDriver(tmp[0].getParent());

                Map<String,List<String>> tmpData=null;

                try {
                	tmpData = LoadJar.getInstance().getDriverNameToFile(tmp);
                } catch (UnifyException e1) {
                    LogProxy.errorReport(this,e1);
                    return;
                }
                Iterator<String> it=tmpData.keySet().iterator();
                while(it.hasNext())
                {
                	String filePath=(String)it.next();
                	List<String> drivers=(List<String>)tmpData.get(filePath);
                	
                	LoadJar.getInstance().addExtraFile(filePath);
                	
                    addFile(drivers);
                    for(int i=0;i<drivers.size();i++)
                    {
//                    if (!map.containsKey(driver[i]))
                        map.put(drivers.get(i), filePath);
                    }
                }
            }
        }else if(e.getSource()==selectJar)
        {
        	File[] selectedFiles=SelectExtraFileDialog.selectExtraFile();
        	if(selectedFiles==null)
        		return;
        	
        	 Map<String,List<String>> tmpData=null;

             try {
             	tmpData = LoadJar.getInstance().getDriverNameToFile(selectedFiles);
             } catch (UnifyException e1) {
                 LogProxy.errorReport(this,e1);
                 return;
             }
             Iterator<String> it=tmpData.keySet().iterator();
             while(it.hasNext())
             {
             	String filePath=(String)it.next();
             	List<String> drivers=(List<String>)tmpData.get(filePath);
             	
             	LoadJar.getInstance().addExtraFile(filePath);
             	
                 addFile(drivers);
                 for(int i=0;i<drivers.size();i++)
                 {
                     map.put(drivers.get(i), filePath);
                 }
             }
        }

    }

    /**
     * ���б���������������
     * 
     * @param f
     */
    protected void addFile(String f) {
        if (isExist(f))
            return;
        JLabel l = new JLabel();
        l.setText(f);
        l.setIcon(PublicResource.getIcon("driver.Icon"));
        l.setOpaque(true);
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.LEFT));
        JCheckBox box = new JCheckBox();
        box.setOpaque(false);
        p.add(box);
        p.add(l);
        model.addElement(p);
    }
    protected void addFile(List<String> list) {
        if(list==null)
        	return;
        for(int i=0;i<list.size();i++)
        {
        	addFile(list.get(i));
        }
    }
    /**
     * �ж��б����Ƿ��Ѵ���ָ��������
     * 
     * @param className
     * @return
     */
    protected boolean isExist(String className) {
        int len = model.getSize();
        for (int i = 0; i < len; i++) {
            JPanel p = (JPanel) model.getElementAt(i);
            JLabel l = (JLabel) p.getComponent(1);
            if (l.getText().equals(className)) {
                return true;
            }
        }
        return false;
    }

    public void shutDialogProcess(Object ob) {
        CommonFrame owner = (CommonFrame) this.getParent();
        filterSelectDriver();
        owner.preButtonProcess(map);
        map.clear();
        map = null;
        this.dispose();
    }

    /**
     * �������б���δ��ѡ�������
     * 
     * @return
     */
    public void filterSelectDriver() {
        int len = model.getSize();
        if (len < 1)
            return;

        for (int i = 0; i < len; i++) {
            JPanel p = (JPanel) model.getElementAt(i);
            JCheckBox b = (JCheckBox) p.getComponent(0);
            if (!b.isSelected()) {
                JLabel l = (JLabel) p.getComponent(1);
                map.remove(l.getText());
            }
        }
    }

    /**
     * @return ���� model��
     */
    public DefaultListModel getModel() {
        return model;
    }

    /**
     * @param model
     *            Ҫ���õ� model��
     */
    public void setModel(DefaultListModel model) {
        this.model = model;
        if (jarList != null) {
            jarList.setModel(model);
        }
    }

    /**
     * ������һ������һ����ȡ��ť����ʾ
     */
    public boolean displayPreButton() {
        return false;
    }

    public boolean displayNextButton() {
        return false;
    }

    protected boolean displayQuitButton() {
        return false;
    }
}
