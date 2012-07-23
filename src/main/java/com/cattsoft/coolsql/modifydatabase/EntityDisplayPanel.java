/*
 * Created on 2007-1-17
 */
package com.cattsoft.coolsql.modifydatabase;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.cattsoft.coolsql.exportdata.Actionable;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.component.TextEditor;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.Inputer;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.sql.ISQLDatabaseMetaData;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ʵ����Ϣ��ʾ��壬����ʵ��������ǩ��ģʽ��Ϣ
 */
public class EntityDisplayPanel extends JPanel implements Inputer {

	private static final long serialVersionUID = 3126339103327334525L;

	/**
     * ��ǰ��ѡ�е�ʵ�����
     */
    private Entity entityObject = null;

    private TextEditor bookmark = null; //��ǩ

    private JLabel catalogLabel=null;// label for catalog text component
    private TextEditor catalog=null;  //the component that dispays catalog name
    
    private JLabel schemaLabel; //label for schema text component
    private TextEditor schema = null; //ģʽ

    private TextEditor entity = null; //ʵ��

    /**
     * ʵ��ı�ʱ��֪ͨ�����ڸü����е�����Actionable�ӿ�
     */
    private List<Actionable> entityChangeActions = null;

    public EntityDisplayPanel() {
        this(null);
    }

    public EntityDisplayPanel(Entity entityObject) {
        super();
        this.entityObject = entityObject;
        entityChangeActions = new ArrayList<Actionable>();
        createGUI();
        displayEntityInfo(entityObject);
    }

    private void createGUI() {

        //        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        setLayout(new FlowLayout(FlowLayout.LEFT));

        //��ǩ
        bookmark = new TextEditor(10);
        add(new JLabel(PublicResource
                .getSQLString("rowupdate.entitydisplay.bookmark")));
        bookmark.setEditable(false);
        add(bookmark);

        add(Box.createHorizontalStrut(6));

        //catalog 
        catalog = new TextEditor(10);
        catalogLabel=new JLabel(PublicResource
                .getSQLString("rowupdate.entitydisplay.catalog"));
        add(catalogLabel);
        catalog.setEditable(false);
        add(catalog);
        add(Box.createHorizontalStrut(6));
        
        //ģʽ
        schema = new TextEditor(10);
        schemaLabel=new JLabel(PublicResource
                .getSQLString("rowupdate.entitydisplay.schema"));
        add(schemaLabel);
        schema.setEditable(false);
        add(schema);

        add(Box.createHorizontalStrut(6));

        //ʵ��
        entity = new TextEditor(10);
        add(new JLabel(PublicResource
                .getSQLString("rowupdate.entitydisplay.entity")));
        entity.setEditable(false);
        add(entity);

        add(Box.createHorizontalStrut(6));

        if (isDisplayChangeBtn()) {
            RenderButton btn = new RenderButton(PublicResource
                    .getSQLString("rowupdate.entitydisplay.selectentity"));
            btn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    selectEntity();
                }

            });
            add(btn);
        }

        setMinimumSize(new Dimension(600, 50));
    }

    /**
     * ���ʵ��ı�ļ���
     * 
     * @param action
     *            --ʵ��仯ʱ����Ҫ֪ͨ�Ľӿ�
     */
    public void addEntityChangeListener(Actionable action) {
        entityChangeActions.add(action);
    }

    /**
     * ɾ��ʵ��ı����
     * 
     * @param action
     *            --��Ҫ��ɾ��ļ���ӿ�
     */
    public void removeEntityChangeListener(Actionable action) {
        entityChangeActions.remove(action);
    }

    private void notifyActions() {
        for (int i = 0; i < entityChangeActions.size(); i++) {
            Actionable action = (Actionable) entityChangeActions.get(i);
            action.action();
        }
    }

    /**
     * ��ʾʵ����Ϣ
     * 
     * @param en
     *            --��Ҫ����ʾ��ʵ�����
     */
    public void displayEntityInfo(Entity en) {
        setEntityInfo(en);
    }

    /**
     * ��ȡʵ�����
     * 
     * @return
     */
    public Entity getEntity() {
        return entityObject;
    }

    /**
     * ����ʵ�����Ϣ�ֶε���ʾֵ
     * 
     * @param bookmarkStr
     *            --��ǩ����
     * @param catalogStr --catalog name
     * @param schemaStr
     *            --ģʽ��
     * @param entityNameStr
     *            --ʵ�����
     */
    private void setEntityInfo(String bookmarkStr,String catalogStr, String schemaStr,
            String entityNameStr) {
    	
        bookmark.setText(bookmarkStr);
        catalog.setText(catalogStr);
        schema.setText(schemaStr);
        entity.setText(entityNameStr);
    }
    /**
     * set entity property ,display information on components ,such as catalog,schema and so on.
     * And it will set the visibility of catalog component and schema component
     * @param en -- entity object that will be display.
     */
    public void setEntityInfo(Entity en)
    {
    	if(en==null)
    		return;
    	setEntityInfo(en.getBookmark().getAliasName(),en.getCatalog(),en.getSchema(),en.getName());
    	try {
			ISQLDatabaseMetaData metaData=en.getBookmark().getDbInfoProvider().getDatabaseMetaData();
			boolean isSupportCatalog=metaData.supportsCatalogs();
			catalogLabel.setVisible(isSupportCatalog);
			catalog.setVisible(isSupportCatalog);
			
			boolean isSupportSchema=metaData.supportsSchemas();
			schemaLabel.setVisible(isSupportSchema);
			schema.setVisible(isSupportSchema);
		} catch (UnifyException e) {
			LogProxy.errorMessage(this,e.getMessage());
		} catch (SQLException e) {
			LogProxy.SQLErrorReport(e);
		}
    	
    }
    /**
     * ѡ��ʵ��
     *  
     */
    protected void selectEntity() {
        EntitySelectDialog entitySelector = null;
        Container con = GUIUtil.getUpParent(this, javax.swing.JDialog.class);
        if (con instanceof javax.swing.JDialog)
            entitySelector = new EntitySelectDialog((javax.swing.JDialog) con,
                    entityObject, this);
        else
            entitySelector = new EntitySelectDialog(entityObject, this);

        entitySelector.setVisible(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coolsql.data.display.Inputer#setData(java.lang.Object)
     */
    public void setData(Object value) {
        boolean isNeedNotify = true; //
        if (value instanceof Entity) {
            isNeedNotify = isNeedNotify((Entity) value);
            entityObject = (Entity) value;
        }
        if (isNeedNotify) //���ʵ�����ֵ����ı�
        {
            displayEntityInfo(entityObject);
            notifyActions();
        }
    }

    /**
     * �÷���������ʵ����°�ť����ʾ���
     * 
     * @return --���
     */
    public boolean isDisplayChangeBtn() {
        return true;
    }

    /**
     * �����ʵ���Ƿ��뵱ǰʵ�������ͬ�������ͬ����false�����ͬ����true���÷��������ж��Ƿ���Ҫ���½�����Ϣ�Լ�֪ͨ�����¼�
     * 
     * @param newValue
     *            --����ʵ�����
     * @return --true:���ǰʵ�����ͬ�ڸ�ʵ�壨newValue�������򷵻�false
     */
    private boolean isNeedNotify(Entity newValue) {
        if (newValue == null) {
            if (newValue == entityObject)
                return false;
            else
                return true;
        } else if (entityObject == null) {
            return true;
        } else {
//            if (newValue.getBookmark().getAliasName().equals(
//                    entityObject.getBookmark().getAliasName())
//                    && newValue.getSchema().equals(entityObject.getSchema())
//                    && newValue.getName().equals(entityObject.getName()))
//                return false;
//            else
//                return true;
        	return !entityObject.equals(newValue);
        }
    }
}
