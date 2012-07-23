/*
 * �������� 2006-8-19
 */
package com.cattsoft.coolsql.search;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import org.apache.log4j.Logger;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.bookmarkBean.BookmarkUpdateOfComboBoxListener;
import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.ExtendComboBox;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.model.Schema;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ����ݿ���Ϣ�Ĳ��Ҵ��ڣ��ṩ��Ϣ��ģ���ѯ
 */
public class SearchInfoDialog extends BaseDialog implements ActionListener {

	private static final Logger logger=Logger.getLogger(SearchInfoDialog.class);
	
	private static final long serialVersionUID = 1L;

	private static SearchInfoDialog instance = null;

    /**
     * ����һ�β��ң��رմ��ں󣬽���ѯ�������棬�����Ժ����ʱ���Զ���ʾ����Ϊ��һ�εĲ�ѯ����
     */
    private static String oldAliasName = null; //��ǩ����

    private static String oldCatalogName=null;  //�������
    private static Object oldSchemaName = ""; //ģʽ��

    private static String oldEntityName = ""; //ʵ����

    private static String oldColumnName = ""; //����

    private static boolean isEntityQuery = true; //�Ƿ���ʵ���ѯ

    /**
     * ��ʹ�õĲ�ѯ�ؼ���
     */
    private static List<String> pastedEntity=null;  //��ʹ�õ�ʵ��ؼ���
    private static List<String> pastedColumn=null;  //��ʹ�õ��йؼ���
    private static int maxSavedEntity=15;   //ʵ��ؼ��ֵ���󱣴�����
    private static int maxSavedColumn=15;   //�йؼ��ֵ���󱣴�����
    /**
     * ��ݿ��ѡ��
     */
    private JComboBox db = null;

    /**
     * �����ѡ��
     */
    private ExtendComboBox catalog = null;
    private JLabel catalogLabel=null; //����ı�ǩ
    /**
     * ģʽ��ѡ��
     */
    private ExtendComboBox schema = null;
    private JLabel schemaLabel=null; //label of component that select schema;
    
    /**
     * ���������
     */
    private ExtendComboBox table = null;

    /**
     * ���������
     */
    private ExtendComboBox column = null;

    private ExtendComboBox.SearchModeSelect columnMode = null;//����Ϊȫ�ֱ��������ڽ����ѺõĿ���(ʹ���Ƿ����)

    private QueryInfoThread thread = null; //��ѯ�̣߳������ѯ���ڵĵ�������Ӱ�������ڵ�ִ��

    private BookmarkUpdateOfComboBoxListener listener = null;//��ǩ�������

    private BookmarkConnectedPropertyListener connectedListener=null;//listener bookmark'connect state
    static {
        pastedEntity=new ArrayList<String>();
        pastedColumn=new ArrayList<String>();
    }
    private SearchInfoDialog(JFrame main) {
        super(main, false);
        this.setTitle("query database information!");
        initCondition(main);

        listener = new BookmarkUpdateOfComboBoxListener(db);
        connectedListener=new BookmarkConnectedPropertyListener();
        BookmarkManage.getInstance().addBookmarkListener(listener);
        Bookmark[] bookmarks = (Bookmark[]) BookmarkManage.getInstance()
                .getBookmarks().toArray(new Bookmark[0]);
        for (int i = 0; i < bookmarks.length; i++) {
            bookmarks[i].addPropertyListener(listener);
            bookmarks[i].addPropertyListener(connectedListener);
        }

        //��ʼ����ѯ�̣߳����������߳�
        thread = new QueryInfoThread(new QueryDBInfo(this));
        thread.start();
    }

    public static synchronized SearchInfoDialog getInstance(JFrame main) {
        if (instance == null)
        {
            instance = new SearchInfoDialog(main);
        }
        return instance;
    }

    /**
     * ����ô����Ƿ��Ѿ�ʵ������ظ�����
     * 
     * @return
     */
    public static boolean checkInstanced() {
        return instance != null;
    }

    private void initCondition(JFrame parent) {
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel content = (JPanel) this.getContentPane();
        JPanel main = new JPanel();
        main.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1;
        main.add(new JLabel(PublicResource
                .getSQLString("searchinfo.entityselect.label")), gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel entitySelect = new JPanel();
        entitySelect.setLayout(new FlowLayout(FlowLayout.LEFT));
        JRadioButton radioTable = new JRadioButton("Entity");
        JRadioButton radioColumn = new JRadioButton("Column");
        RadioListener itemListener = new RadioListener();
        radioTable.addItemListener(itemListener);
        radioColumn.addItemListener(itemListener);

        ButtonGroup group = new ButtonGroup();
        group.add(radioTable);
        group.add(radioColumn);
        entitySelect.add(radioTable);
        entitySelect.add(radioColumn);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        main.add(entitySelect, gbc);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        main.add(new JLabel(PublicResource
                .getSQLString("searchinfo.databaseselect.label")), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        db = new JComboBox();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        main.add(db, gbc);

        //88888
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        catalogLabel=new JLabel(PublicResource
                .getSQLString("searchinfo.catalogselect.label"));
        main.add(catalogLabel, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        catalog = new ExtendComboBox();
        catalog.setEditable(true);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        main.add(catalog, gbc);
        //888888
        
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        schemaLabel=new JLabel(PublicResource
                .getSQLString("searchinfo.schemaselect.label"));
        main.add(schemaLabel, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        schema = new ExtendComboBox();
//        schema.addSubmitListener(this);
        schema.setEditable(true);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        main.add(schema, gbc);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        main.add(new JLabel(PublicResource
                .getSQLString("searchinfo.table.label")), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        table = new ExtendComboBox();
//        table.addSubmitListener(this);
        table.setEditable(true);
        main.add(table, gbc);
        gbc.gridx = 2;
        ExtendComboBox.SearchModeSelect tableMode = table.new SearchModeSelect();
        tableMode.setToolTipText(PublicResource
                .getSQLString("searchinfo.tablemodeprompt"));
        tableMode.setEditable(false);
        main.add(tableMode, gbc);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        main.add(new JLabel(PublicResource
                .getSQLString("searchinfo.column.label")), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        column = new ExtendComboBox();
//        column.addSubmitListener(this);
        column.setEditable(true);
        main.add(column, gbc);
        columnMode = column.new SearchModeSelect();
        columnMode.setEditable(false);
        columnMode.setToolTipText(PublicResource
                .getSQLString("searchinfo.columnmodeprompt"));
        gbc.gridx = 2;
        main.add(columnMode, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        border = BorderFactory.createTitledBorder(border, PublicResource
                .getSQLString("searchinfo.title"));
        main.setBorder(border);

        JPanel buttons = new JPanel();
        RenderButton query = new RenderButton(PublicResource
                .getSQLString("searchinfo.command.query"));
        query.addActionListener(this);
        RenderButton quit = new RenderButton(PublicResource
                .getSQLString("searchinfo.command.exit"));
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quitQuery();
            }
        });
        buttons.add(query);
        buttons.add(quit);

        /**
         * ����ϴβ�ѯ�����ͣ���������
         */
        if (isEntityQuery)
            radioTable.setSelected(true);
        else
            radioColumn.setSelected(true);

        content.add(main, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
        this.getRootPane().setDefaultButton(query);
        //        pack();
        this.setSize(350, 330);
        centerToFrame(parent);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quitQuery();
            }
        });
//        this.setResizable(false);
        this.setVisible(true);
        //��ѯ���ڳ�ʼ����ɺ󣬿�ʼװ�����
        loadAliasData();
    }

    /**
     * �����е���ݿ�������ӵ���ݿ�����ѡ��ؼ���
     *  
     */
    private void loadAliasData() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) db.getModel();
        Set<String> set = BookmarkManage.getInstance().getAliases();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            model.addElement(it.next());
        }
        String selectedBookmark = (String) model.getSelectedItem(); //ѡ�е���ǩ����
        loadCatalogData(selectedBookmark);

        db.addItemListener(new BookmarkItemListener());
        catalog.addItemListener(new CatalogItemListener());
        
        String selectedCcatalog=(String)catalog.getSelectedItem();
        loadSchemaData(selectedBookmark,selectedCcatalog);
        loadOldCondition();
    }

    /**
     * ���ó��ϴα���Ĳ�ѯ����
     *  
     */
    private void loadOldCondition() {
        if (db != null&&oldAliasName!=null) {
            db.setSelectedItem(oldAliasName);
        }else
        {
            
            Bookmark bookmark=BookmarkManage.getInstance().getDefaultBookmark();
            if(bookmark!=null)
                db.setSelectedItem(bookmark.getAliasName());
        }
        if (catalog != null&&catalog.isVisible()) {
        	catalog.setSelectedItem(oldCatalogName);
        }
        if (schema != null&&schema.isVisible()) {
            schema.setSelectedItem(oldSchemaName);
        }
        if (table != null) {
            table.setSelectedItem(oldEntityName);
        }
        if (column != null) {
            column.setSelectedItem(oldColumnName);
        }
        
        /**
         * ����ǰ��ѯ�Ĺؼ��ֽ��м���
         */
        for(int i=pastedEntity.size()-1;i>-1;i--)
        {
            table.addItem(pastedEntity.get(i));
        }
        for(int i=pastedColumn.size()-1;i>-1;i--)
        {
            column.addItem(pastedColumn.get(i));
        }
    }
    /**
     * ���÷���ѡ������Ƿ�Ӧ��
     * @param isVisible �Ƿ����true:���ӣ�false��������
     */
    private void setCatalog(boolean isVisible)
    {
    	if(!isVisible)
    	{
    		catalog.removeAllItems();
    		catalog.setVisible(false);
    		catalogLabel.setVisible(false);
    	}else
    	{
    		catalog.setVisible(true);
    		catalogLabel.setVisible(true);
    	}
    }
    /**
     * ���ط������
     * @param bookmarkAlias
     */
    protected void loadCatalogData(String bookmarkAlias)
    {
        if (catalog.getSelectedItem() != null)
            oldCatalogName = (String)catalog.getSelectedItem();//���ģʽ֮ǰ������ѡ���ֵ
        catalog.removeAllItems();
        
    	Bookmark bookmark=BookmarkManage.getInstance().get(bookmarkAlias);
    	if(bookmark==null||!bookmark.isConnected())
    		return ;
    	try {
    		boolean isSupported=bookmark.getDbInfoProvider().getDatabaseMetaData().supportsCatalogs();
			setCatalog(isSupported);
			if(!isSupported) //��֧�ַ��ֱ࣬�ӷ���
				return;
			
			String[] catalogs=bookmark.getDbInfoProvider().getDatabaseMetaData().getCatalogs();
			if(catalogs==null)
				return;
			
			for(String c:catalogs)
				catalog.addItem(c);
		} catch (SQLException e) {
			LogProxy.SQLErrorReport(e);
		} catch (UnifyException e) {
			LogProxy.errorReport(logger,e);
		}
    }
    /**
     * Set whether component that select schema is not visible.
     * @param isVisible true:is visible,false:is not visible
     */
    private void setSchema(boolean isVisible)
    {
    	if(!isVisible)
    	{
    		schema.removeAllItems();
    		
    	}
    	schema.setVisible(isVisible);
    	schemaLabel.setVisible(isVisible);
    }
    /**
     * װ����ݿ��Ӧ��ģʽ�б�
     * 
     * @param alias
     */
    protected void loadSchemaData(String alias,String selectedCatalog) {
        if (schema.getSelectedItem() != null)
            oldSchemaName = schema.getSelectedItem();//���ģʽ֮ǰ������ѡ���ֵ

        schema.removeAllItems();
        Bookmark tmp = BookmarkManage.getInstance().get(alias);
        if(tmp==null||!tmp.isConnected())
        	return ;
        
        try {
			boolean isSupportSchema=tmp.getDbInfoProvider().getDatabaseMetaData().supportsSchemas();
			setSchema(isSupportSchema);
			if(!isSupportSchema)  //not support schema ,so return directly
				return;
			
			Schema[] schemas=tmp.getDbInfoProvider().getSchemas(selectedCatalog);
			if(schemas==null)
				return;
			
			for(int i=0;i<schemas.length;i++)
			{
				schema.addItem(schemas[i]);
			}
		} catch (SQLException e) {
			LogProxy.SQLErrorReport(e);
		} catch (UnifyException e) {
			LogProxy.errorReport(logger, e);
		}
    }
    public void actionPerformed(ActionEvent e) {
        query();
    }

    /**
     * ��ȡ��ѯ�Ķ������� 0��ʵ�� 1������Ϣ
     * 
     * @return
     */
    public int getQueryType() {
        if (!column.isEnabled()) // ʵ���ѯ
            return 0;
        else
            //��ѯ����Ϣ
            return 1;
    }

    /**
     * ��ѯ��Ϣ
     *  
     */
    public void query() {
        Bookmark bookmark = getSelectBookmark();
        if (bookmark == null)
            return;

        if (!column.isEnabled()) // ʵ���ѯ
        {
            saveEntityKeyWord();

            SearchResultFrame resultFrame = new SearchOfEntityFrame(this,
                    bookmark);
            resultFrame.setKeyword(getQueryEntity());
            thread.setOperateWindow(resultFrame); //���²�������Ϊ��ѯ����
            thread.launch(); //�����߳�
            resultFrame.setVisible(true);

        } else //�в�ѯ
        {
            /**
             * ��Ҫ��ʵ����еĹؼ��ֶ�����
             */
            saveEntityKeyWord();
            saveColumnKeyWord();
            
            SearchResultFrame resultFrame = new SearchOfColumnFrame(this,
                    bookmark);
            resultFrame.setKeyword(getQueryColumn());
            thread.setOperateWindow(resultFrame); //���²�������Ϊ��ѯ����
            thread.launch(); //�����߳�
            resultFrame.setVisible(true);

        }
    }
    /**
     * ��ʵ��ؼ��ֱ���
     *
     */
    protected void saveEntityKeyWord()
    {
        Object ob=table.getSelectedItem();
        String tmp=ob==null?"":ob.toString();
        tmp=StringUtil.trim(tmp);
        if(tmp.equals(""))
            return;
        
        if(addQueryKeyWord(pastedEntity,tmp,maxSavedEntity))////����ǰ�ؼ��ֱ���
        {
            table.removeItem(tmp);
        }else
        {
            if(table.getItemCount()>=maxSavedEntity) //������󱣴���������ĩβԪ��ɾ��
            {
                table.removeItemAt(table.getItemCount()-1);
            }
        }
        table.insertItemAt(tmp,0);
        table.setSelectedItem(ob);
    }
    /**
     * ���йؼ��ֱ���
     *
     */
    protected void saveColumnKeyWord()
    {        
        Object ob=column.getSelectedItem();
        String tmp=ob==null?"":ob.toString();
        tmp=StringUtil.trim(tmp);
        if(tmp.equals(""))
            return;
        
        if(addQueryKeyWord(pastedColumn,tmp,maxSavedColumn))  //����ǰ�ؼ��ֱ���
        {    
            column.removeItem(tmp);           
        }else
        {
            if(column.getItemCount()>=maxSavedColumn)  //������󱣴���������ĩβԪ��ɾ��
            {
                column.removeItemAt(column.getItemCount()-1);
            }
        }
        column.insertItemAt(tmp,0);//���ؼ��ּ���������ĵ�һ��λ��
        column.setSelectedItem(ob);
    }
    /**
     * ����ѯ�ؼ��ֱ�������Ӧ�б���.������,������б�ĵ�һ��λ��;����Ѿ�����,���ùؼ����Ƶ���һ��λ��
     * @param list  --����ؼ��ֵ��б�
     * @param keyWord  --�ؼ���
     * @param limit --�б����󳤶�
     */
    private boolean addQueryKeyWord(List<String> list,String keyWord,int limit)
    {
        boolean isContained=false;
        if(list.contains(keyWord))  //���ùؼ����Ѿ�����,ɾ��֮
        {
           list.remove(keyWord);  
           isContained=true;
        }else 
        {
            if(list.size()>=limit ) //����б��Ѿ��ﵽ��󳤶�,����һ��λ�õ�Ԫ��ɾ��
                list.remove(0);
        }
        list.add(keyWord);  //���ؼ��������ĩβ
        return isContained;
    }
    /**
     * ��ȡ��������
     * @return
     */
    protected String getQueryCatalog()
    {
    	Object ob=catalog.getSelectedItem();
    	String catalogName=null;
    	 if(ob!=null)
         {
    		 catalogName= StringUtil.trim(ob.toString());
         }
         if ("".equals(catalogName))
        	 catalogName = null;

         return catalogName;
    }
    /**
     * ��ȡ��Ч��ģʽ���Ա�����ݵĲ���
     * 
     * @return
     */
    protected String getQuerySchema() {
    	Object ob=schema.getSelectedItem();//ģʽ
    	
        String schemaName = null;
        if(ob!=null)
        {
        	if(ob instanceof Schema)
        	{
        		Schema tmpSchema=(Schema)ob;
        		if(!tmpSchema.isDefault())  //����ģʽ��ȱʡģʽ����ģʽ������Ϊnull
        			schemaName=StringUtil.trim(tmpSchema.getName());
        	}else
        		schemaName=StringUtil.trim(ob.toString());
        }
        if ("".equals(schemaName)) //���û������ģʽ����ô��ģʽ��ֵΪnull
            schemaName = null;

        return schemaName;
    }

    /**
     * ��ȡʵ���ѯ�ֶ�
     * 
     * @return
     */
    protected String getQueryEntity() {
        String tmp=table.getSelectedItem()==null?"":table.getSelectedItem().toString();
        String entityName = StringUtil.trim(tmp);
        if (entityName.equals(""))
            return null;
        int mode = table.getQueryMode(); //��ȡʵ���ѯģʽ
        if (mode == 0) //��ȫ���,���ý��д���
        {

        } else if (mode == 1) //�Թؼ��ֿ�ͷ
        {
            entityName = entityName + "%";
        } else if (mode == 2) //�Թؼ��ֽ�β
        {
            entityName = "%" + entityName;
        } else if (mode == 3) //��ؼ���
        {
            entityName = "%" + entityName + "%";
        }
        return entityName;
    }

    /**
     * ��ȡ�в�ѯ�ֶ�
     * 
     * @return
     */
    protected String getQueryColumn() {
        String tmp=column.getSelectedItem()==null?"":column.getSelectedItem().toString();
        String columnName = StringUtil.trim(tmp);
        if (columnName.equals(""))
            return null;
        int mode = column.getQueryMode(); //��ȡʵ���ѯģʽ
        if (mode == 0) //��ȫ���,���ý��д���
        {

        } else if (mode == 1) //�Թؼ��ֿ�ͷ
        {
            columnName = columnName + "%";
        } else if (mode == 2) //�Թؼ��ֽ�β
        {
            columnName = "%" + columnName;
        } else if (mode == 3) {
            columnName = "%" + columnName + "%";
        }
        return columnName;
    }

    /**
     * ��ȡѡ�е���ǩ
     * 
     * @return ����Ҳ���������null
     */
    public Bookmark getSelectBookmark() {
        String aliasName = StringUtil.trim((String) db.getSelectedItem());
        if (aliasName.equals("")) {
            JOptionPane.showMessageDialog(this, PublicResource
                    .getSQLString("searchinfo.query.noalias"), "warning", 2);
            return null;
        }
        BookmarkManage bmm = BookmarkManage.getInstance();
        return bmm.get(aliasName);
    }

    /**
     * �˳���ѯ
     *  
     */
    public void quitQuery() {
        thread.dispose(); //�����ѯ�߳�

        /**
         * ��ȥ�Ը���ǩ�ļ���
         */
        Bookmark[] bookmarks = (Bookmark[]) BookmarkManage.getInstance()
                .getBookmarks().toArray(new Bookmark[0]);
        for (int i = 0; i < bookmarks.length; i++) {
            bookmarks[i].removePropertyListener(listener);
            bookmarks[i].removePropertyListener(connectedListener);
        }
        BookmarkManage.getInstance().removeBookmarkListener(listener);

        /**
         * ���汾�εĲ�ѯ����
         */
        oldAliasName = db.getSelectedItem() == null ? "" : db.getSelectedItem()
                .toString();
        oldCatalogName=catalog.getSelectedItem()==null?"":catalog.getSelectedItem().toString();
        oldSchemaName = schema.getSelectedItem() == null ? "" : schema
                .getSelectedItem();
        oldEntityName = table.getSelectedItem()==null?"":table.getSelectedItem().toString();
        oldColumnName = column.getSelectedItem()==null?"":column.getSelectedItem().toString();

        this.dispose();
        instance.dispose();
        instance = null;

    }

    /**
     * ����ָ��frame�м�
     * 
     * @param f
     */
    public void centerToFrame(JFrame f) {
        Rectangle rect = f.getBounds();
        this
                .setBounds((int) (rect.getX() + (rect.getWidth() - this
                        .getWidth()) / 2), (int) (rect.getY() + (rect
                        .getHeight() - this.getHeight()) / 2), this.getWidth(),
                        this.getHeight());
    }

    /**
     * 
     * @author liu_xlin ��ѯ�����ѡ��ļ���������ݲ�ѯ����Ĳ�ͬ������������ѡ������Ƿ���õĿ���
     */
    private class RadioListener implements ItemListener {

        /*
         * ���� Javadoc��
         * 
         * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
         */
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) { //���ָ���ѡ���Ժ�,������Ӧ�ĵ���
                JRadioButton bu = (JRadioButton) e.getSource();
                if (bu.getText().equals("Entity")) {
                    if (column.isEnabled()) {
                        column.setEnabled(false);
                    }
                    if (columnMode.isEnabled())
                        columnMode.setEnabled(false);
                    isEntityQuery = true;
                } else {
                    if (!column.isEnabled()) {
                        column.setEnabled(true);
                    }
                    if (!columnMode.isEnabled())
                        columnMode.setEnabled(true);

                    isEntityQuery = false;
                }

            }
        }
    }

    private class BookmarkItemListener implements ItemListener
    {

		/* (non-Javadoc)
		 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
		 */
		public void itemStateChanged(ItemEvent e) {
			
			if (e.getStateChange() == ItemEvent.SELECTED) { //���ָ���ѡ���Ժ�,������Ӧ�ĵ���
		        String selectedBookmark = (String) db.getSelectedItem(); //ѡ�е���ǩ����
		        loadCatalogData(selectedBookmark);
		        if(catalog.isVisible())
		        	catalog.setSelectedItem(oldCatalogName);
		        
		        String selectedCcatalog=(String)catalog.getSelectedItem();
		        loadSchemaData(selectedBookmark,selectedCcatalog);
	        }
		}
    	
    }
    private class CatalogItemListener implements ItemListener
    {
		/* (non-Javadoc)
		 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
		 */
		public void itemStateChanged(ItemEvent e) {
			
			if (e.getStateChange() == ItemEvent.SELECTED) { //���ָ���ѡ���Ժ�,������Ӧ�ĵ���
				 if(!schema.isVisible())  //if the schema selector is not visible,it means that current bookmark don't support schema.
		            	return;
	            String alias = (String) db.getSelectedItem();
	            String selectedCatalog=(String)catalog.getSelectedItem();
	           
	            loadSchemaData(alias,selectedCatalog);
	            schema.setSelectedItem(oldSchemaName);
	        }
		}
    	
    }
    private class BookmarkConnectedPropertyListener implements PropertyChangeListener
    {

		/* (non-Javadoc)
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			String name=evt.getPropertyName();
			if(name.equals(Bookmark.PROPERTY_CONNECTED))
			{
				Bookmark source=(Bookmark)evt.getSource();
				if(source.isConnected())
				{
					String selectedBookmark=(String)db.getSelectedItem();
					if(source.getAliasName().equals(selectedBookmark))
					{
						loadCatalogData(selectedBookmark);
				        if(catalog.isVisible())
				        	catalog.setSelectedItem(oldCatalogName);
				        	
				        String selectedCcatalog=(String)catalog.getSelectedItem();
				        loadSchemaData(selectedBookmark,selectedCcatalog);
				        if(schema.isVisible())
				        	schema.setSelectedItem(oldSchemaName);
					}
				}
			}
			
		}
    	
    }
}
