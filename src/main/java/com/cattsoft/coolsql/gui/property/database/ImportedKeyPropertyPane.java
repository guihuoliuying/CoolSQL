package com.cattsoft.coolsql.gui.property.database;

import java.sql.SQLException;

import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.sql.model.ForeignKey;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * the property pane that dislays importedkey information for entity
 * 
 * @author ��Т��
 * 
 * 2008-1-6 create
 */
public class ImportedKeyPropertyPane extends ForeignKeyPropertyPane {

	private static final long serialVersionUID = 3440989746716909009L;

	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.database.ForeignKeyPropertyPane#getForeignKeys(com.coolsql.sql.model.Entity)
	 */
	@Override
	protected ForeignKey[] getForeignKeys(Entity entity) {
		ForeignKey[] fks = null;
		try {
			fks = entity.getImportedKeys();
			
		} catch (UnifyException e) {
			LogProxy.errorReport(this, e);
		} catch (SQLException e) {
			LogProxy.SQLErrorReport(this, e);
		}
		return fks;
	}

	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.database.ForeignKeyPropertyPane#getForeignEntityInfo()
	 */
	@Override
	protected int[] getForeignEntityInfo() {
		
		return new int[]{0,1,2};
	}
}
