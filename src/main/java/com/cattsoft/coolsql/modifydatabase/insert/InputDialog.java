/*
 * Created on 2007-3-7
 */
package com.cattsoft.coolsql.modifydatabase.insert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.Inputer;
import com.cattsoft.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin ��Ϊ���������ֵ֮ǰ�����øöԻ���������Ҫ��ֵ��
 */
public class InputDialog extends BaseDialog {

	private static final long serialVersionUID = 1L;

	private JTextArea inputer;//�����ı���

    private JCheckBox selectnull; //����nullֵ

//    private JRadioButton inputNew; //�ֹ�����

    /**
     * ��Ҫ������ݵĽӿ�
     */
    private Inputer input = null;

    public InputDialog(JFrame frame, Inputer input) {
        super(frame, true);
        this.input = input;
        createGUI();
    }

    public InputDialog(JDialog frame, Inputer input) {
        super(frame, true);
        this.input = input;
        createGUI();
    }

    protected void createGUI() {
        setTitle(PublicResource.getUtilString("inputdialog.title"));
        JPanel main = (JPanel) getContentPane();
        main.setLayout(new BorderLayout());

        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
//        ButtonGroup group = new ButtonGroup();
        selectnull = new JCheckBox(PublicResource
                .getUtilString("inputdialog.radiobutton.setnull"));
//        inputNew = new JRadioButton();
        SetAsNullListener listener = new SetAsNullListener();
        selectnull.addItemListener(listener);
//        inputNew.addItemListener(listener);
//        group.add(selectnull);
//        group.add(inputNew);

        gbc.anchor = GridBagConstraints.EAST;
        pane.add(selectnull, gbc);        
//        gbc.gridx++;
        
//        pane.add(new JLabel(PublicResource
//                .getUtilString("inputdialog.radiobutton.setnull")));
//        gbc.gridx--;
        gbc.gridy++;

//        pane.add(inputNew, gbc);
//        gbc.gridx++;
//        pane.add(new JLabel(PublicResource
//                .getUtilString("inputdialog.radiobutton.input")),gbc);
//        gbc.gridx++;
//        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty=1;
        inputer = new JTextArea();
        GUIUtil.installDefaultTextPopMenu(inputer);
        gbc.fill = GridBagConstraints.BOTH;
        pane.add(new JScrollPane(inputer), gbc);

//        inputNew.setSelected(true);
        pane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        main.add(pane, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout());
        RenderButton okButton = new RenderButton(PublicResource
                .getUtilString("inputdialog.okbutton"));
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                finishInput();

                closeDialog();
            }

        });
        RenderButton quitButton = new RenderButton(PublicResource
                .getUtilString("inputdialog.quitbutton"));
        quitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }

        });
        buttonPane.add(okButton);
        buttonPane.add(quitButton);

        main.add(buttonPane, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(okButton);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }
        });
        setSize(400,330);
        toCenter();
    }
    public void setValue(Object t)
    {
    	if(t==null)
    	{
    		selectnull.setSelected(true);
    	}else
    	{
    		selectnull.setSelected(false);
    		inputer.setText(t.toString());
    	}
    	
    }
    /**
     * ���������,���ø÷������к������ݴ��ݴ���
     *  
     */
    private void finishInput() {
        if (selectnull.isSelected()) //���ѡ����null
        {
            input.setData(null);
        } else {
            String value = inputer.getText();
            input.setData(value);
        }
        input=null;
    }

    /**
     * �رմ���ʱ�����Ĵ���
     *  
     */
    private void closeDialog() {
        removeAll();
        dispose();
    }

    /**
     * Do something when the value of cell is set as NULL.
     * @author liu_xlin
     */
    private class SetAsNullListener implements ItemListener {

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
         */
        public void itemStateChanged(ItemEvent e) {
        	if(e.getStateChange() == ItemEvent.SELECTED)
        	{
        		inputer.setEnabled(false);
        	}else
        	{
        		inputer.setEnabled(true);
        		inputer.selectAll();
                inputer.requestFocusInWindow();
        	}
        	
//            if (e.getStateChange() == ItemEvent.SELECTED) { //���ָ���ѡ���Ժ�,������Ӧ�ĵ���
//                if (e.getSource() == selectnull) //��Ϊnull
//                {
//                    GUIUtil.setComponentEnabled(false, inputer);
//                } else {
//                    GUIUtil.setComponentEnabled(true, inputer);
//                    inputer.selectAll();
//                    inputer.requestFocus();
//                }
//            }

        }

    }
}
