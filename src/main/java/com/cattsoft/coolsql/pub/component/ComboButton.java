/*
 * �������� 2006-9-25
 *
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.ComboBoxEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxButton;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import javax.swing.plaf.metal.MetalComboBoxUI;

import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.sun.java.swing.plaf.windows.WindowsButtonUI;

/**
 * @author liu_xlin �������ѡ�����Եİ�ť,ͨ��ѡ���������,Ȼ�������в���
 */
public class ComboButton extends JComboBox {
	private static final long serialVersionUID = 1L;

	private static final String DATAKEY = "dataKey";

    /**
     * ��߰�ť
     */
    private JButton leftBtn = null;

    /**
     * �ұ߰�ť
     */
    private JButton rightBtn = null;

    /**
     * ����¼�������
     */
    private MouseProcessListener listener = null;

    //��ť�߿�
    private ThreeDBorder raiseBorder = new ThreeDBorder(ThreeDBorder.RAISE);

    private ThreeDBorder lowBorder = new ThreeDBorder(ThreeDBorder.LOWER);

    /**
     * �����˵�
     */
    private SplitePopupMenu popMenu = null;

    /**
     * ��߰�ť���¼������
     */
    private Vector actions = null;

    /**
     * �������Ǹÿؼ��ĺ������,ÿ��ѡ��һ���˵����,���¸�ֵ
     */
    private Object selectedData = null;

    private JMenuItem defaultItem = null;

    private MenuItemListener menuListener = null;

    /**
     * �˵��ķָ���
     */
    private JSeparator separator = null;

    public ComboButton(String label, Icon icon) {
        super();
        separator = new JSeparator();
        listener = new MouseProcessListener();
        popMenu = new SplitePopupMenu();
        defaultItem = new JMenuItem();
        menuListener = new MenuItemListener();
        defaultItem.addActionListener(menuListener);
        actions = new Vector();
        createButtons(label, icon);
        leftBtn.setFocusable(false);
        leftBtn.setBorder(null);
        leftBtn.addMouseListener(listener);
        rightBtn.setFocusable(false);
        rightBtn.setPreferredSize(new Dimension(13, 1));
        rightBtn.setBorder(null);
        rightBtn.addMouseListener(listener);
        
        this.setEditable(true);
        setUI(new SpliteButtonUI());
        setSize(80,30);
//        this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        
    }
    /**
     * ��ȡ��Ӧ�����Ĳ˵�ͼ��
     * 
     * @param data
     * @return
     */
    public Icon getItemIconByData(Object data) {
        JMenuItem item = this.getItemByData(data);
        if (item == null)
            return null;
        else
            return item.getIcon();
    }

    /**
     * ��ȡ��Ӧ�����ı�ǩ
     * 
     * @param data
     * @return
     */
    public String getLabelByData(Object data) {
        JMenuItem item = this.getItemByData(data);
        if (item == null)
            return null;
        else
            return item.getText();
    }

    /**
     * ������ݲ˵���
     * 
     * @param data
     * @param newLabel
     * @param newIcon
     */
    public void updateItemByData(Object data, String newLabel, Icon newIcon) {
        JMenuItem item = this.getItemByData(data);
        if (item == null)
            return;
        String label = StringUtil.trim(newLabel);
        if (!label.equals("")) {
            item.setText(label);
            if (data == defaultItem.getClientProperty(DATAKEY))
                defaultItem.setText(label);
        }

        if (newIcon != null) {
            item.setIcon(newIcon);
            if (data == defaultItem.getClientProperty(DATAKEY))
                defaultItem.setIcon(newIcon);
        }
    }

    /**
     * ��ӵ����˵���
     * 
     * @param item
     * @throws UnifyException
     */
    public synchronized void addDataItem(String label, Icon icon, Object data)
            throws UnifyException {
        if (data == null)
            throw new IllegalArgumentException("argument:data is null!");
        if (checkExist(data)) {
            throw new UnifyException(PublicResource
                    .getString("sqlEditorView.combobutton.datarepeat"));
        }
        JMenuItem item = new JMenuItem(label, icon);
        item.putClientProperty(DATAKEY, data);
        item.addActionListener(menuListener);
        popMenu.add(item);

        addDefaultItem();
    }

    /**
     * У���Ӧ����ݶ����Ƿ��Ѿ�����
     * 
     * @param data
     * @return
     */
    public boolean checkExist(Object data) {
        if (getItemByData(data) != null)
            return true;
        else
            return false;
    }

    /**
     * �ı�ȱʡ�˵��ͨ������ݶ���data���������ҵ���Ӧ�Ĳ˵��Ȼ�����ȱʡ�˵������ʾ�Լ���ݶ���
     * 
     * @param data
     *            --��ݶ��󣬱����ڲ˵�����
     * @param isAdd
     *            --��û����ʾȱʡ�˵�������£��Ƿ����ȱʡ�˵�  true:�ڲ����ڵ���������ӣ�false�������ڵ�����²����
     * @throws UnifyException
     *             --����Ҳ�����Ӧ��data�����׳����쳣
     */
    public void changeDefaultItem(Object data, boolean isAdd)
            throws UnifyException {
        JMenuItem item = getItemByData(data);
        if (item == null) {
            throw new UnifyException("data don't exist!");
        }

        /**
         * �ж���ȱʡ�˵��Ƿ��Ѿ���ӣ����û�У�������õĲ���isAdd�����ж��Ƿ����
         */
        JMenuItem tmpItem = (JMenuItem) popMenu.getComponent(0);
        if (tmpItem != defaultItem) //û�����ȱʡ�˵���
        {
            if (isAdd) {
                popMenu.insert(defaultItem, 0);
                popMenu.insert(separator, 1);
            } else
                return;
        }

        /**
         * ����ݶ���data��Ӧ�Ĳ˵������Ϊȱʡ�˵�
         */
        updateDefaultItemData(item);
    }

    /**
     * ͨ����ݶ��������Ҳ˵���
     * 
     * @param data
     * @return
     */
    private JMenuItem getItemByData(Object data) {
        for (int i = 0; i < popMenu.getComponentCount(); i++) {
            Component com = popMenu.getComponent(i);
            if (com instanceof JMenuItem && com != defaultItem) { //���ָ��ߺ�ȱʡ�˵�����
                JMenuItem tmpItem = (JMenuItem) com;
                if (data == tmpItem.getClientProperty(DATAKEY)) //�ҵ���Ӧ�˵���
                    return tmpItem;
            }
        }
        return null;
    }

    /**
     * �����ݶ�����ɾ��˵���
     * 
     * @param ob
     */
    public synchronized void deleteDataItem(Object ob) {
        for (int i = 0; i < popMenu.getComponentCount(); i++) {
            Component com = popMenu.getComponent(i);
            if (com instanceof JMenuItem && com != defaultItem) { //���ָ��ߺ�ȱʡ�˵�����
                JMenuItem tmpItem = (JMenuItem) com;
                if (tmpItem.getClientProperty(DATAKEY) == ob) {
                    tmpItem.removeActionListener(menuListener);
                    popMenu.remove(tmpItem);
                    if (selectedData == ob)
                        selectedData = null;

                    Component firstComponent = popMenu.getComponentCount() > 0 ? popMenu
                            .getComponent(0)
                            : null;
                    if (firstComponent != null) {
                        if (firstComponent == defaultItem) { //���ȱʡ�˵����Ѿ����

                            if (ob == defaultItem.getClientProperty(DATAKEY)) //�����ȱʡ�˵���һ�£������¸���ȱʡ�˵�����Ϣ
                            {
                                updateDefaultItemData((JMenuItem) popMenu
                                        .getComponent(2)); //����Ϊ��һ���˵�����Ϣ(��ȥȱʡ�˵��ͷָ���)
                            }
                            if (popMenu.getComponentCount() == 3) //ֻʣ��һ����Ч�˵�
                            {
                                popMenu.remove(defaultItem); //ȥ��ȱʡ�˵�
                                popMenu.remove(separator); //ȥ��ָ���
                                
                                Object oldObject=this.selectedData;
                                selectedData = ((JMenuItem) popMenu
                                        .getComponent(0))
                                        .getClientProperty(DATAKEY);//����ΪΨһ�˵����ֵ
                                this.firePropertyChange("selectedData",oldObject,selectedData);
                            }
                        }
                    } else //û�в˵���
                    {
                        selectedData = null; //������ݵ�����
                    }
                    return;
                }

            }
        }
    }

    /**
     * ��Ӳ˵����,���˵����������,����˵�������Ĭ�ϲ˵���
     *  
     */
    private void addDefaultItem() {
        if (popMenu.getComponentCount() > 1) //���˵������1
        {
            JMenuItem tmpItem = (JMenuItem) popMenu.getComponent(0);
            if (tmpItem != defaultItem) //û�����ȱʡ�˵���
            {
                popMenu.insert(defaultItem, 0);
                popMenu.insert(separator, 1);
                updateDefaultItemData(tmpItem);
            }
        }
    }

    /**
     * ȥ��ȱʡ�˵���
     *  
     */
    private void removeDefaultItem() {
        popMenu.remove(0); //ȥ��ȱʡ�˵�
        popMenu.remove(1); //ȥ��ָ���
    }

    /**
     * ����ȱʡ�˵������
     *  
     */
    private void updateDefaultItemData(JMenuItem selectedItem) {
        JMenuItem tmpItem = (JMenuItem) popMenu.getComponent(0);
        if (tmpItem == defaultItem) //�Ѿ������ȱʡ�˵���
        {
            if (selectedItem.getClientProperty(DATAKEY) != defaultItem
                    .getClientProperty(DATAKEY)) //���ȱʡ������һ���˵���(��ȥȱʡ�˵��ͷָ���)��Ӧ����ݲ����,����ȱʡ�˵������
            {
                defaultItem.setText(selectedItem.getText());
                defaultItem.setIcon(selectedItem.getIcon());
                Object ob = selectedItem.getClientProperty(DATAKEY);
                defaultItem.putClientProperty(DATAKEY, ob);
                
                Object oldObject=this.selectedData;
                selectedData = ob;
                this.firePropertyChange("selectedData",oldObject,selectedData);
            }
        }
    }
    /**
     * ��ʼ���������ߵİ�ť
     *  
     */
    private void createButtons(String label, Icon icon) {
        int preferSize = 0;
        if (leftBtn == null) {
            leftBtn = new JButton() {
                public void setUI(ButtonUI ui) {
                    if (ui instanceof WindowsButtonUI) {
                        ui = new BasicButtonUI();
                    }
                    super.setUI(ui);
                }
                public Dimension getPreferredSize() {
                    Dimension pref = super.getPreferredSize();
                    pref.height += 4;
                    return pref;
                }
                public Dimension getMinimumSize() {
                    Dimension min = super.getMinimumSize();
                    min.height += 4;
                    return min;
                }
            };
            if (label != null && !label.trim().equals("")) {
                leftBtn.setText(label);
                preferSize += leftBtn.getFontMetrics(leftBtn.getFont())
                        .stringWidth("abc"); //��ӡ�abc�����Ա����ð�ť���һ��
            }
            if (icon != null) {
                leftBtn.setIcon(icon);
                preferSize += icon.getIconWidth()
                        + leftBtn.getFontMetrics(leftBtn.getFont())
                                .stringWidth("ab");
            }
            leftBtn.setMargin(new Insets(0, 0, 0, 0));
            leftBtn.setBackground(BasePanel.getThemeColor());
            leftBtn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    leftBtn.setBorder(null);
                    rightBtn.setBorder(null);
//                    ComboButton.this.getSize();
                    runAction(e);
                }

            });
        }
        if (rightBtn == null) {
            rightBtn = new JButton() {
                public void setUI(ButtonUI ui) {
                    if (ui instanceof WindowsButtonUI) {
                        ui = new BasicButtonUI();
                    }
                    super.setUI(ui);
                }

                /**
                 * �ұ߰�ť��ͼ����ʾ
                 */
                public void paint(Graphics g) {
                    super.paint(g);
                    Polygon p = new Polygon();
                    int w = getWidth();
                    int y = (getHeight() - 4) / 2;
                    int x = (w - 6) / 2;
                    if (isSelected()) {
                        x += 1;
                    }
                    p.addPoint(x, y);
                    p.addPoint(x + 3, y + 3);
                    p.addPoint(x + 6, y);
                    g.fillPolygon(p);
                    g.drawPolygon(p);
                }
            };
            rightBtn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    rightBtn.setBorder(null);
                    if (popMenu.getComponentCount() > 0) {

                        int x = (int) (ComboButton.this.getWidth() - popMenu
                                .getPreferredSize().getWidth());
                        popMenu.show(ComboButton.this, x, ComboButton.this
                                .getHeight());
                    }
                }

            });
//            rightBtn.setUI(new BasicButtonUI());
            rightBtn.setMargin(new Insets(0, 0, 0, 0));
            rightBtn.setBackground(BasePanel.getThemeColor());
//            rightBtn.setPreferredSize(new Dimension(8,21));
//            rightBtn.setMaximumSize(new Dimension(8,21));
//            rightBtn.setSize(new Dimension(8,21));
            preferSize += rightBtn.getPreferredSize().getWidth();
            this.setPreferredSize(new Dimension(preferSize, 21)); //���øð�ť�ĺ��ʴ�С
        }
    }

    /**
     * ִ�а�ť�¼�����
     * 
     * @param e
     */
    protected void runAction(ActionEvent e) {
        if (selectedData == null && popMenu.getComponentCount() == 1)//�����˵�ֻ��һ��,��ôĬ��ѡȡ�������
        {
            Object oldObject=this.selectedData;
            selectedData = ((JMenuItem) popMenu.getComponent(0))
                    .getClientProperty(DATAKEY);//ȡ��һ��
            this.firePropertyChange("selectedData",oldObject,selectedData);
        }
        for (int i = 0; i < actions.size(); i++) {
            SplitBtnListener action = (SplitBtnListener) actions.elementAt(i);
            action.action(e, selectedData);
        }
    }

    /**
     * ���ص�ǰ��ϰ�ťĬ��ѡ�������
     * 
     * @return
     */
    public Object getSelectData() {
        if (selectedData == null && popMenu.getComponentCount() == 1)//�����˵�ֻ��һ��,��ôĬ��ѡȡ�������
        {
            Object oldObject=this.selectedData;
            selectedData = ((JMenuItem) popMenu.getComponent(0))
                    .getClientProperty(DATAKEY);//ȡ��һ��
            this.firePropertyChange("selectedData",oldObject,selectedData);
        }
        return selectedData;
    }

    /**
     * �����߰�ť�¼�����
     * 
     * @param action
     */
    public void addAction(SplitBtnListener action) {
        actions.add(action);
    }

    protected class MouseProcessListener implements MouseListener {

        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
         */
        public void mouseClicked(MouseEvent e) {

        }

        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
         */
        public void mousePressed(MouseEvent e) {
            if (e.getModifiers() == InputEvent.BUTTON3_MASK
                    || e.getModifiers() == InputEvent.BUTTON2_MASK) //������껬�ֺ��Ҽ�
                return;
            if (e.getSource() == leftBtn) //��߰�ť
            {
                leftBtn.setBorder(lowBorder);
                rightBtn.setBorder(lowBorder);
            } else //�ұ߰�ť
            {
                rightBtn.setBorder(lowBorder);

            }
        }

        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
         */
        public void mouseReleased(MouseEvent e) {
            if (e.getModifiers() == InputEvent.BUTTON3_MASK
                    || e.getModifiers() == InputEvent.BUTTON2_MASK)//������껬�ֺ��Ҽ�
                return;
            if (popMenu.getComponentCount() > 0) {
                leftBtn.setBorder(null);
                rightBtn.setBorder(null);
            } else {
                leftBtn.setBorder(raiseBorder);
                rightBtn.setBorder(raiseBorder);
            }

        }

        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
         */
        public void mouseEntered(MouseEvent e) {
            if (!ComboButton.this.popMenu.isShowing()) //���˵�û�е�������ʾ͹���߿�
            {
                leftBtn.setBorder(raiseBorder);
                rightBtn.setBorder(raiseBorder);
            }
        }

        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
         */
        public void mouseExited(MouseEvent e) {
            leftBtn.setBorder(null);
            rightBtn.setBorder(null);
        }

    }

    /**
     * 
     * @author liu_xlin �˵�����¼�����
     */
    private class MenuItemListener implements ActionListener {
        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() != defaultItem) //��ͨ�˵���
            {
                JMenuItem item = (JMenuItem) e.getSource();
                updateDefaultItemData(item);//����ȱʡ�˵���

            }
            runAction(e); //ͬʱִ����ӵ��¼�����
        }

    }
    private class SpliteButtonUI extends MetalComboBoxUI
    {
        protected ComboBoxEditor createEditor() {        
            return new LeftButtonEditor();
        }
        /**
         * �÷���������д�����Ҳ����κ����飬�����²˵��޷�����
         */
        public void setPopupVisible( JComboBox c, boolean v )
        {
        	
        }
        protected ComboPopup createPopup() {
        	if(popMenu==null)
        		popMenu=new SplitePopupMenu();
        	
            return popMenu;
        }
        protected Dimension getDefaultSize() {
        	return new Dimension(100,30);
        }
        protected JButton createArrowButton() {
            boolean iconOnly = (comboBox.isEditable());
            JButton button = new MetalComboBoxButton( comboBox,
                                                      new MetalComboBoxIcon(){
                /**
                 * Created a stub to satisfy the interface.
                 */
                public int getIconWidth() { return 6; }

                /**
                 * Created a stub to satisfy the interface.
                 */
                public int getIconHeight()  { return 4; }
            },
                                                      iconOnly,
                                                      currentValuePane,
                                                      listBox );
            rightBtn.setMargin( new Insets( 0, 1, 1, 3 ) );
//            if (MetalLookAndFeel.usingOcean()) {
//                // Disabled rollover effect.
//                button.putClientProperty(MetalBorders.NO_BUTTON_ROLLOVER,
//                                         Boolean.TRUE);
//            }
//            updateButtonForOcean(button);
            
            return button;
        }
    }
    /**
     * ���µ����˸ÿؼ���������з��򣨴������󣩣������Ҫ���༭�������ΪrightBtn��
     * @author kenny liu
     *
     * 2007-11-17 create
     */
    private class LeftButtonEditor implements ComboBoxEditor,javax.swing.plaf.UIResource
    {

		/* (non-Javadoc)
		 * @see javax.swing.ComboBoxEditor#addActionListener(java.awt.event.ActionListener)
		 */
		public void addActionListener(ActionListener l) {
//			rightBtn.addActionListener(l);
			
		}

		/* (non-Javadoc)
		 * @see javax.swing.ComboBoxEditor#getEditorComponent()
		 */
		public Component getEditorComponent() {
			return leftBtn;
		}

		/* (non-Javadoc)
		 * @see javax.swing.ComboBoxEditor#getItem()
		 */
		public Object getItem() {
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.swing.ComboBoxEditor#removeActionListener(java.awt.event.ActionListener)
		 */
		public void removeActionListener(ActionListener l) {
//			rightBtn.removeActionListener(l);
			
		}

		/* (non-Javadoc)
		 * @see javax.swing.ComboBoxEditor#selectAll()
		 */
		public void selectAll() {
		}

		/* (non-Javadoc)
		 * @see javax.swing.ComboBoxEditor#setItem(java.lang.Object)
		 */
		public void setItem(Object anObject) {
		}
    	
    }
    /**
     * ����ѡ��˵��ࡣ
     * @author kenny liu
     *
     * 2007-11-17 create
     */
    private class SplitePopupMenu extends BasePopupMenu implements ComboPopup
    {
		/* (non-Javadoc)
		 * @see javax.swing.plaf.basic.ComboPopup#getKeyListener()
		 */
		public KeyListener getKeyListener() {
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.swing.plaf.basic.ComboPopup#getList()
		 */
		public JList getList() {
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.swing.plaf.basic.ComboPopup#getMouseListener()
		 */
		public MouseListener getMouseListener() {
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.swing.plaf.basic.ComboPopup#getMouseMotionListener()
		 */
		public MouseMotionListener getMouseMotionListener() {
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.swing.plaf.basic.ComboPopup#uninstallingUI()
		 */
		public void uninstallingUI() {
		}
    	
    }
}
