/*
 * Created on 2007-1-18
 */
package com.cattsoft.coolsql.gui.property.database;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.cattsoft.coolsql.gui.property.PropertyPane;
import com.cattsoft.coolsql.pub.component.TextEditor;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.sql.ISQLDatabaseMetaData;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ʵ���������ʾ���
 */
public class EntityBaseInfo extends PropertyPane {

	private static final long serialVersionUID = 8554136544302520497L;

	private TextEditor entityName; // ʵ�����

	private TextEditor bookmark; // ʵ��������ǩ

	private JLabel catalogLabel;
	private TextEditor catalog;
	private JLabel schemaLabel;
	private TextEditor schema; // ʵ��ģʽ

	private TextEditor type; // ʵ�����ͣ�'TABLE','VIEW','SEQUENCE' or else type��
	
	private TextEditor remark; //The descrition of the entity.

//	private TextEditor dataCount; //count of table data
	private int displayLength;
	public EntityBaseInfo() {
		super();
		displayLength = 50;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.gui.property.PropertyPane#initContent()
	 */
	public JPanel initContent() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		pane.setLayout(new GridBagLayout());
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0;
		gbc.weighty = 0.1;
		gbc.insets = new Insets(2, 2, 2, 2);
		gbc.anchor = GridBagConstraints.EAST;

		// ʵ�����
		gbc.gridwidth = 1;
		pane.add(new JLabel(PublicResource
				.getSQLString("rowupdate.entitydisplay.entity"),
				SwingConstants.RIGHT), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridx++;
		entityName = new TextEditor(displayLength);
		entityName.setEditable(false);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1;
		pane.add(entityName, gbc);
		gbc.gridy++;
		gbc.gridx--;

		// ʵ��������ǩ
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		pane.add(new JLabel(PublicResource
				.getSQLString("rowupdate.entitydisplay.bookmark"),
				SwingConstants.RIGHT), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridx++;
		bookmark = new TextEditor(displayLength);
		bookmark.setEditable(false);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1;
		pane.add(bookmark, gbc);
		gbc.gridy++;
		gbc.gridx--;

		// ʵ������ģʽ
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		catalogLabel=new JLabel(PublicResource
				.getSQLString("rowupdate.entitydisplay.catalog"),SwingConstants.RIGHT);
		pane.add(catalogLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridx++;
		catalog = new TextEditor(displayLength);
		catalog.setEditable(false);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1;
		pane.add(catalog, gbc);
		gbc.gridy++;
		gbc.gridx--;

		// ʵ������ģʽ
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		schemaLabel=new JLabel(PublicResource
				.getSQLString("rowupdate.entitydisplay.schema"),
				SwingConstants.RIGHT);
		pane.add(schemaLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridx++;
		schema = new TextEditor(displayLength);
		schema.setEditable(false);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1;
		pane.add(schema, gbc);
		gbc.gridy++;
		gbc.gridx--;

		// ʵ������
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		pane.add(new JLabel(PublicResource
				.getSQLString("rowupdate.entitydisplay.entitytype"),
				SwingConstants.RIGHT), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridx++;
		type = new TextEditor(displayLength);
		type.setEditable(false);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1;
		pane.add(type, gbc);
		gbc.gridy++;
		gbc.gridx--;
		
		// remark
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		pane.add(new JLabel(PublicResource
				.getSQLString("rowupdate.entitydisplay.entitycomment"),
				SwingConstants.RIGHT), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridx++;
		remark = new TextEditor(displayLength);
		remark.setEditable(false);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1;
		pane.add(remark, gbc);
		
		JPanel temp = new JPanel();
		temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
		temp.add(pane);
		return pane;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.gui.property.PropertyInterface#set()
	 */
	public boolean set() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.gui.property.PropertyInterface#quit()
	 */
	public void cancel() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.gui.property.PropertyInterface#setData(java.lang.Object)
	 */
	public void setData(Object ob) {
		if (ob != null && ob instanceof Entity) {
			Entity en = (Entity) ob;

			try {
				ISQLDatabaseMetaData dbmd = en.getBookmark().getDbInfoProvider()
						.getDatabaseMetaData();

				entityName.setText(en.getName());
				remark.setText(en.getRemark());
				bookmark.setText(en.getBookmark().getAliasName());

				if (dbmd.supportsCatalogs()) {
					catalog.setText(en.getCatalog());
				} else
				{
					catalog.setVisible(false);
					catalogLabel.setVisible(false);
				}

				if (dbmd.supportsSchemas()) {
					schema.setText(en.getSchema());
				} else
				{
					schema.setVisible(false);
					schemaLabel.setVisible(false);
				}
				
//				Bookmark bm=en.getBookmark();
//				String sql=bm.getAdapter().getCountQuery(en.getQualifiedName());
//				SQLResults rs=bm.getDbInfoProvider().execute(bm.getConnection(), sql);
//				if(!rs.isResultSet())
//				{
//					dataCount.setText("0");
//					return ;
//				}
//				SQLResultSetResults result=(SQLResultSetResults)rs;
//				Row data=result.getRows()[0];
//				Object d1=data.get(1);
//				dataCount.setText(d1==null?"0":d1.toString());
			} catch (SQLException e) {
				LogProxy.SQLErrorReport(e);
			} catch (UnifyException e) {
				LogProxy.errorReport(this, e);
			}
			type.setText(en.getType());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.gui.property.PropertyInterface#apply()
	 */
	public void apply() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.gui.property.PropertyInterface#isNeedApply()
	 */
	public boolean isNeedApply() {
		return false;
	}

}
