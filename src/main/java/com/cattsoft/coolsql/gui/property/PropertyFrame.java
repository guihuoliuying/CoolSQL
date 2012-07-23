/*
 * �������� 2006-9-10
 */
package com.cattsoft.coolsql.gui.property;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * Property frame used to display some system information or setting information.
 * @author liu_xlin
 */
public class PropertyFrame extends BaseDialog {

	private static final long serialVersionUID = 1L;

	private JTree tree = null;

    private JPanel cardPane = null;

    private NodeKey defaultNode;  //default card node,this card will be displayed before propertyframe is shown
    /**
     * key=node object(DefaultMutableTreeNode.getUserObject()) value=property panel class object(Class)
     */
    private Map<NodeKey, Object> map = new HashMap<NodeKey, Object>();

    /**
     * Ӧ�ð�ť
     */
    private RenderButton applybtn = null;

    public PropertyFrame(JFrame frame) {
        super(frame, "", true);
        init();
    }

    public PropertyFrame(JDialog dialog) {
        super(dialog, "", true);
        init();
    }

    public PropertyFrame(JFrame frame, String title) {
        super(frame, title, true);
        init();
    }

    public PropertyFrame(JFrame frame, String title, boolean isModel) {
        super(frame, title, isModel);
        init();
    }

    public PropertyFrame(JDialog dialog, String title) {
        super(dialog, title, true);
        init();
    }

    public PropertyFrame(JDialog dialog, String title, boolean isModel) {
        super(dialog, title, isModel);
        init();
    }

    @SuppressWarnings("serial")
	public void init() {
        cardPane = new JPanel();

        JPanel content = (JPanel) this.getContentPane(); //�����
        JPanel btnPane = new JPanel(); //��ť���
        btnPane.setLayout(new FlowLayout());
        
		double[][] model = new double[][]{{160, TableLayout.FILL},
				{
			TableLayout.FILL}};
        JPanel main = new JPanel(new TableLayout(model)); //�����������

        //��ʼ�����������
        NodeKey rootNode = getRootData();
        rootNode.setDisplayName("root");
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootNode); //��ڵ�
        tree = new JTree(root);
        tree.setRowHeight(20);
        tree.setShowsRootHandles(true);
        tree.addTreeSelectionListener(new PropertyTreeSelect(this));
        tree.setCellRenderer(new PropertyTreeRender());
        JScrollPane pane = new JScrollPane(tree);


        PropertyPane rootPane = new PropertyPane()
        {

            public JPanel initContent() {
                return new JPanel();
            }

            public boolean set() {
                return true;
            }

            public void cancel() {
                
            }

            public void setData(Object ob) {
                
            }

            public void apply() {
                
            }

            public boolean isNeedApply() {
                return false;
            }
            
        }
        ;
        rootPane.add(new JLabel(""), "0, 0");
        map.put(rootNode, rootPane);

        //��ʼ�����Բ쿴���
        cardPane = new JPanel();
        cardPane.setLayout(new CardLayout());
        cardPane.add(rootNode.getName(), new JScrollPane(rootPane)); //��ʼ����Ƭ���

        main.add(pane, "0, 0");
        
        main.add(cardPane, "1, 0");

        RenderButton okbtn = new RenderButton(PublicResource.getString("propertyframe.button.ok"));
        RenderButton quitbtn = new RenderButton(PublicResource.getString("propertyframe.button.quit"));

        okbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setting();
            }
        });
        quitbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });

        btnPane.add(okbtn);
        btnPane.add(quitbtn);

        applybtn = new RenderButton(PublicResource.getString("propertyframe.button.apply"));
            applybtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    applyCurrentSet();
                }
            });
            btnPane.add(applybtn);

        JPanel tmpPane = new JPanel();
        tmpPane.setLayout(new BorderLayout());
        tmpPane.add(new JSeparator(), BorderLayout.NORTH);
        tmpPane.add(btnPane, BorderLayout.CENTER);
        tmpPane.setPreferredSize(new Dimension(550, 50));

        this.getRootPane().setDefaultButton(okbtn);
        content.add(main, BorderLayout.CENTER);
        content.add(tmpPane, BorderLayout.SOUTH);
        setSize(GUIUtil.getScaleScreenDimension(.6));

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
        toCenterOfOwner();
    }
    /**
     * ��ȡ��Ƭ���Ŀ�Ƭ������
     * 
     * @return
     */
    private CardLayout getCardLayout() {
        return (CardLayout) cardPane.getLayout();
    }

    /**
     * ��ʾ��Ƭ
     * 
     * @param key
     */
    public void showCard(NodeKey key) {
        String str = key.getName();
        Object card = map.get(key);
        if (card instanceof PropertyData) //δ��ʼ��
        {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            PropertyPane p;
            PropertyData cardData = (PropertyData) card;
            try {
                //��ʼ���������
                Class<?> tmp = cardData.getKey();
                p = (PropertyPane) (tmp.newInstance());
                p.setData(cardData.getData());
            } catch (InstantiationException e) {
                LogProxy.internalError(e);
                return;
            } catch (IllegalAccessException e) {
                LogProxy.internalError(e);
                return;
            } finally {
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            p.setLabel(key.getDisplayName());
            
            cardPane.add(str, p);
            map.put(key, p); //����ֵ�滻Ϊ������
        }
        getCardLayout().show(cardPane, str);
        PropertyPane property=(PropertyPane)map.get(key);
        if(property==null)
        	return;
        
        if(property.isNeedApply())
        {
        	showApplyButton(property.isChanged());
        }
        if(applybtn.isVisible()!=property.isNeedApply())
        {
           applybtn.setVisible(property.isNeedApply());
           this.validate();
        }
    }

    /**
     * Enable apply button visible, and set whether apply button is enabled according to isEnable.
     */
    public void showApplyButton(boolean isEnable)
    {
    	if(!applybtn.isVisible())
    	{
    		applybtn.setVisible(true);
    	}
    	if(isEnable!=applybtn.isEnabled())
    	{
    		applybtn.setEnabled(isEnable);
    	}
    }
    public void hiddenApplyButton()
    {
    	applybtn.setVisible(false);
    }
    /**
     * Get current component displayed in the card container.
     */
    public Component getCurrentComponent()
    {
    	int ncomponents = cardPane.getComponentCount();
        for (int i = 0 ; i < ncomponents ; i++) {
            Component comp = cardPane.getComponent(i);
            if (comp.isVisible()) {
                return comp;
            }
        }
        return null;
    }
    /**
     *@see {#addCard(key,pane,null)};
     */
    public void addCard(NodeKey key,Class<?> pane) throws UnifyException
    {
    	this.addCard(key,pane,null);
    }
    /**
     * Add a card panel to frame.
     * @param key --the data object of navigate tree node
     * @param pane--the class object of property panel .
     * @param data -- the data object needed when property panel is initializing.
     */
    public void addCard(NodeKey key, Class<?> pane, Object data)
            throws UnifyException {
        if (checkKey(key))
            throw new UnifyException(PublicResource
                    .getSQLString("sql.propertyset.keyrepeat")
                    + ",keyname=" + key.getName());
        map.put(key, new PropertyData(pane, data));
        addNodeByKey(key);

    }

    /**
     * ��ӽڵ�
     * 
     * @param key
     */
    public void addNodeByKey(NodeKey key) {
        NodeKey proKey = key.getParent();
        if(proKey==null)
        {
        	proKey=(NodeKey)getRootNode().getUserObject();
        	key.setParent(proKey);
        }
        if (proKey.equals(getRootNode().getUserObject())) //ֱ���ڸ�ڵ�����ӽڵ�
        {
            addNode(key, getRootNode());
            //չ����ڵ�

            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) model
                    .getRoot();
            if (root.getChildCount() > 0) {
                TreePath path = new TreePath(model.getPathToRoot(root));
                if (!tree.isExpanded(path)) {
                    tree.expandPath(path);
                    //ѡ���ڵ�
                    TreePath tmpPath = new TreePath(model.getPathToRoot(root
                            .getChildAt(0)));
                    tree.setSelectionPath(tmpPath);
                }
            }
            return;
        }
        DefaultMutableTreeNode parentNode = getNodeByKey(proKey);
        if(parentNode==null)
        	throw new RuntimeException("invalid node key:"+key.getDisplayName()+",can't find the parent of key ");
        addNode(key, parentNode);
    }

    /**
     * ��ݽڵ���ݻ�ȡ�ڵ����
     * 
     * @param key
     * @return ����Ҳ���,����null
     */
    @SuppressWarnings("unchecked")
	public DefaultMutableTreeNode getNodeByKey(NodeKey key) {
        Enumeration en = getRootNode().depthFirstEnumeration();
        while (en.hasMoreElements()) {
            DefaultMutableTreeNode ob = (DefaultMutableTreeNode) en
                    .nextElement();
            if (key.equals(ob.getUserObject())) {
                return ob;
            }
        }
        return null;
    }

    /**
     * ��ӽڵ�
     * 
     * @param node
     */
    public void addNode(NodeKey node, DefaultMutableTreeNode parent) {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.insertNodeInto(new DefaultMutableTreeNode(node), parent,
        		parent.getChildCount());
    }

    /**
     * У��ؼ�ڵ��Ƿ��ظ�
     * 
     * @param key
     * @return
     */
    public boolean checkKey(NodeKey key) {
        Set<NodeKey> keys = map.keySet();
        Iterator<NodeKey> it = keys.iterator();
        while (it.hasNext()) {
            if (key.equals(it.next()))
                return true;
        }
        return false;
    }

    /**
     * Save all properties
     *  
     */
    private void setting() {

        for (Iterator<Object> it = map.values().iterator(); it.hasNext();) {
            Object ob = it.next();
            if (ob instanceof PropertyPane) {
            	PropertyPane pi = (PropertyPane) ob;
            	if(!pi.isNeedApply()||!pi.isChanged())
            		continue;
                if (!pi.set())
                {
                	getCardLayout().show(cardPane, getKeyByPanel(pi).getName());
                    return;
                }
                if(pi.isNeedListenToChild())
                	pi.reset();
            }
        }
        closeFrame();
    }
    private NodeKey getKeyByPanel(PropertyPane pane) {
		Iterator<NodeKey> it = map.keySet().iterator();
		while (it.hasNext()) {
			NodeKey nk = (NodeKey) it.next();
			if (map.get(nk) == pane) {
				return nk;
			}
		}
		return null;
	}
    /**
     * �˳��������ô��ڣ����Ķ����
     *  
     */
    public void quit() {
        for (Iterator<Object> it = map.values().iterator(); it.hasNext();) {
            Object ob = it.next();
            if (ob instanceof PropertyInterface) {
                PropertyInterface pi = (PropertyInterface) ob;
                pi.cancel();
            }
        }
        closeFrame();
    }

    public void applyCurrentSet() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                .getLastSelectedPathComponent();
        Object ob = map.get(node.getUserObject());
        if (ob instanceof PropertyPane)
        {
        	PropertyPane pp=(PropertyPane)ob;
            pp.apply();
            if(pp.isNeedListenToChild())
            	pp.reset();
        }
    }

    /**
     * Release all resource before closing frame.
     */
    public void closeFrame() {
    	for (Iterator<Object> it = map.values().iterator(); it.hasNext();) {
            Object ob = it.next();
            if (ob instanceof PropertyPane) {
            	PropertyPane pi = (PropertyPane) ob;
            	try {
					pi.doOnClose();
				} catch (Exception e) {
					LogProxy.errorReport(e);
				}
            }
        }
        map.clear();
        removeAll();
        dispose();
    }
    /**
     * ��ȡ��ڵ�
     * 
     * @return
     */
    public DefaultMutableTreeNode getRootNode() {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        return root;
    }

    /**
     * ���ڸ������м�
     *  
     */
    public void toCenterOfOwner() {
        Window f = this.getOwner();
        Rectangle rect = f.getBounds();
        this
                .setBounds((int) (rect.getX() + (rect.getWidth() - this
                        .getWidth()) / 2), (int) (rect.getY() + (rect
                        .getHeight() - this.getHeight()) / 2), this.getWidth(),
                        this.getHeight());
    }
    /**
     * set the default card with parameter:key
     * @param key
     */
    public void setDefaultCard(NodeKey key)
    {
    	defaultNode=key;
    }
    @Override
    public void setVisible(boolean isVisible) {
		if (defaultNode != null) {
			DefaultMutableTreeNode node = getNodeByKey(defaultNode);
			if (node == null)
				return;
			TreePath path = new TreePath(node.getPath());
			tree.expandPath(path);
			tree.setSelectionPath(path);
		}
		super.setVisible(isVisible);
	}
    /**
	 * ��ȡ�ڵ����
	 * 
	 * @return
	 */
    public static NodeKey getRootData() {
        String rootStr = "root";
        NodeKey rootNode = new NodeKey(rootStr, (Icon) null, null);
        return rootNode;
    }

    /**
     * �������������Ϣ���������������(key)������ʼ����Ҫ�����(data)
     * @author liu_xlin 
     */
    private class PropertyData {
        private Class<?> key;

        private Object data;

        public PropertyData(Class<?> key, Object data) {
            this.key = key;
            this.data = data;
        }

        /**
         */
        public Object getData() {
            return data;
        }

        /**
         * @param data
         */
        public void setData(Object data) {
            this.data = data;
        }

        /**
         */
        public Class<?> getKey() {
            return key;
        }

        /**
         * @param key
         */
        public void setKey(Class<?> key) {
            this.key = key;
        }
    }
}
