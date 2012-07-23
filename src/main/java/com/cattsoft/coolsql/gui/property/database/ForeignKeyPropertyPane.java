/**
 * 
 */
package com.cattsoft.coolsql.gui.property.database;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.gui.property.PropertyPane;
import com.cattsoft.coolsql.pub.component.BaseMenuManage;
import com.cattsoft.coolsql.pub.display.BaseTable;
import com.cattsoft.coolsql.pub.display.CommonDataTable;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.MenuCheckable;
import com.cattsoft.coolsql.pub.display.TableCellObject;
import com.cattsoft.coolsql.pub.display.TableScrollPane;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.SqlUtil;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.sql.model.EntityFactory;
import com.cattsoft.coolsql.sql.model.ForeignKey;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.bookmarkview.model.TableNode;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * Property panel that displays foreignkeys information of entity.
 * 
 * @author xiaolin
 * 
 * 2008-1-7 create
 */
public abstract class ForeignKeyPropertyPane extends PropertyPane {
	
	private static final long serialVersionUID = -353599144965512411L;
	private JPanel content;
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.gui.property.PropertyPane#initContent()
	 */
	@Override
	public JPanel initContent() {
		content = new JPanel();
		content.setLayout(new BorderLayout());
		return content;
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
	 * @see com.coolsql.gui.property.PropertyInterface#set()
	 */
	public boolean set() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.gui.property.PropertyInterface#setData(java.lang.Object)
	 */
	public void setData(Object ob) {
		if (ob == null)
			return;
		if (ob instanceof Entity) {
			Entity en = (Entity) ob;
			if (en.getType().equals(SqlUtil.SEQUENCE))
				return;

			ForeignKey[] fks = getForeignKeys(en);
			if (fks == null)
				return;

			// ��ʼ����ͷ��Ϣ
			int colums = 12;
			String[] header = new String[colums];
			for (int i = 0; i < header.length; i++) {
				header[i] = PublicResource
						.getSQLString("sql.propertyset.entity.foreignkey" + i);
			}

			int tableRow = 0;
			for (int i = 0; i < fks.length; i++) {
				tableRow += fks[i].getNumberOfColumns();
			}
			TableCellObject[][] data = new TableCellObject[tableRow][colums];

			tableRow = 0;
			for (int i = 0; i < fks.length; i++) {

				// BookMarkPubInfo.getIconList()[type]
				int columnCount = fks[i].getNumberOfColumns();
				for (int j = 0; j < columnCount; j++) {
					data[tableRow][0] = new TableCellObject(fks[i]
							.getLocalEntityCatalog(), j == 0 ? fks[i]
							.getLocalEntityCatalog() : null, null);
					data[tableRow][1] = new TableCellObject(fks[i]
							.getLocalEntitySchema(), j == 0 ? fks[i]
							.getLocalEntitySchema() : null, null);
					data[tableRow][2] = new TableCellObject(fks[i]
							.getLocalEntityName(), j == 0 ? fks[i]
							.getLocalEntityName() : null, BookMarkPubInfo
							.getIconList()[BookMarkPubInfo.NODE_TABLE]);
					data[tableRow][3] = new TableCellObject(fks[i]
							.getLocalColumnName(j), BookMarkPubInfo
							.getIconList()[BookMarkPubInfo.NODE_COLUMN]);

					data[tableRow][4] = new TableCellObject(fks[i]
							.getForeignEntityCatalog(), j == 0 ? fks[i]
							.getForeignEntityCatalog() : null, null);
					data[tableRow][5] = new TableCellObject(fks[i]
							.getForeignEntitySchema(), j == 0 ? fks[i]
							.getForeignEntitySchema() : null, null);
					data[tableRow][6] = new TableCellObject(fks[i]
							.getForeignEntityName(), j == 0 ? fks[i]
							.getForeignEntityName() : null, BookMarkPubInfo
							.getIconList()[BookMarkPubInfo.NODE_TABLE]);
					data[tableRow][7] = new TableCellObject(fks[i]
							.getForeignColumnName(j), BookMarkPubInfo
							.getIconList()[BookMarkPubInfo.NODE_COLUMN]);

					data[tableRow][8] = new TableCellObject(""
							+ fks[i].getUpdateRule(), j == 0 ? ("" + fks[i]
							.getUpdateRule()) : null, null);
					data[tableRow][9] = new TableCellObject(""
							+ fks[i].getDeleteRule(), j == 0 ? ("" + fks[i]
							.getDeleteRule()) : null, null);
					data[tableRow][10] = new TableCellObject(
							fks[i].getPkName(), j == 0
									? fks[i].getPkName()
									: null, null);
					data[tableRow][11] = new TableCellObject(
							fks[i].getFkName(), j == 0
									? fks[i].getFkName()
									: null, null);

					tableRow++;
				}
			}

			int[] render = {2, 3, 6, 7}; // define column that need be
											// rendered with icon
			CommonDataTable table = new CommonDataTable(data, header, render) {
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.adjustPerfectWidth();

			content.add(new TableScrollPane(table), BorderLayout.CENTER);
			/**
			 * add menuitem used to view information of foreign entity
			 */
			BaseMenuManage popMenu = table.getPopMenuManage();
			final JMenuItem viewForeignTableItem = popMenu
					.addMenuItem(
							PublicResource
									.getSQLString("sql.propertyset.entity.foreignkey.popmenu.viewforeignentity"),
							new ViewForeignEntity(en.getBookmark(), table),
							null, true);
			popMenu.addMenuCheck(new MenuCheckable() {

				public void check(Component com) {
					if (com instanceof CommonDataTable) {
						CommonDataTable table = (CommonDataTable) com;
						GUIUtil.setComponentEnabled(
								table.getSelectedRowCount() == 1,
								viewForeignTableItem);
					} else
						GUIUtil
								.setComponentEnabled(false,
										viewForeignTableItem);

				}

			});
		}

	}
	/**
	 * Get foreign keys ,this method must be implemented by subclass.
	 * 
	 * @param entity
	 * @return
	 */
	protected abstract ForeignKey[] getForeignKeys(Entity entity);
	
	/**
	 * Get column indexes of foreign entity in the table ,this information includes catalog,schema,name of foreign entity.
	 * @return  0:catalog,1:schema,2:name
	 */
	protected abstract int[] getForeignEntityInfo();
	/**
	 * This class is a actionListener that used to view foreign table.
	 * 
	 * @author xiaolin
	 * 
	 * 2008-1-7 create
	 */
	private class ViewForeignEntity implements ActionListener {
		private Bookmark bookmark;
		private BaseTable table;
		public ViewForeignEntity(Bookmark bookmark, BaseTable table) {
			this.bookmark = bookmark;
			this.table = table;
		}
		public void actionPerformed(ActionEvent e) {
			if (table == null || table.getSelectedRowCount() != 1)
				return;

			int[] info=getForeignEntityInfo();
			int selectedRow = table.getSelectedRow();
			final String catalog = ((TableCellObject) table.getValueAt(
					selectedRow, info[0])).getValue(); // fk_table_catalog
			final String schema = ((TableCellObject) table.getValueAt(
					selectedRow, info[1])).getValue(); // fk_table_schema
			final String simpleName = ((TableCellObject) table.getValueAt(
					selectedRow, info[2])).getValue();// fk_table_tablename
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {

					Entity entity = EntityFactory.getInstance().create(
							bookmark, catalog, schema, simpleName,
							SqlUtil.TABLE, "", false);

					String className = PublicResource
							.getSQLString("propertypane.popmenu.viewforeignentity.classname");
					Operatable operator = null;
					try {
						operator = OperatorFactory.getOperator(className);
					} catch (Exception e) {
						LogProxy.errorReport(ForeignKeyPropertyPane.this, e);
						return;
					}
					Identifier id = new TableNode(simpleName, bookmark, entity);

					List<Identifier> args = new ArrayList<Identifier>();
					args.add(id);
					try {
						operator.operate(args);
					} catch (UnifyException e) {
						LogProxy.errorReport(e);
					} catch (SQLException e) {
						LogProxy.SQLErrorReport(e);
					}

				}
			});
		}
	}
}
