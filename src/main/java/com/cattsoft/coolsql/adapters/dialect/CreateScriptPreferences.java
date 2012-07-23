package com.cattsoft.coolsql.adapters.dialect;

import java.sql.DatabaseMetaData;

/**
 * A simple object to store user preferences regarding contraint info.
 * When we are generating a create script, we want to take into account the 
 * user's preferences.  However, when showing the source for constraints, we 
 * want to show exactly what is in the database.  
 * 
 * @author manningr
 */
public class CreateScriptPreferences {
    
    /*
    public static final int NO_ACTION = 0;
    
    public static final int CASCADE_DELETE = 1;
    
    public static final int SET_DEFAULT = 2;
    
    public static final int SET_NULL = 3;
    */
    
    private int deleteAction = DatabaseMetaData.importedKeyNoAction;
    
    private int updateAction = DatabaseMetaData.importedKeyNoAction;
    
    /**
     * whether or not to override the delete referential action for FK defs.
     */
    private boolean deleteRefAction = false;
    
    /**
     * whether or not to override the update referential action for FK defs.
     */
    private boolean updateRefAction = false;
    
    private boolean constraintsAtEnd=true;  //TODO:ͨ��ϵͳ�������ã�Ĭ��true
    
    private boolean includeExternalReferences=true;//TODO:ͨ��ϵͳ�������ã�Ĭ��true
    
    private boolean qualifyTableNames;
    
    public void setDeleteRefAction(boolean deleteRefAction) {
        this.deleteRefAction = deleteRefAction;
    }

    public boolean isDeleteRefAction() {
        return deleteRefAction;
    }

    public void setDeleteAction(int action) {
        this.deleteAction = action;
    }

    public int getDeleteAction() {
        return deleteAction;
    }

    public void setUpdateAction(int updateAction) {
        this.updateAction = updateAction;
    }

    public int getUpdateAction() {
        return updateAction;
    }

    public void setUpdateRefAction(boolean updateRefAction) {
        this.updateRefAction = updateRefAction;
    }

    public boolean isUpdateRefAction() {
        return updateRefAction;
    }

    public String getRefActionByType(int type) {
        switch (type) {
            case DatabaseMetaData.importedKeyNoAction:
                return "NO ACTION";
            case DatabaseMetaData.importedKeyCascade:
                return "CASCADE";
            case DatabaseMetaData.importedKeySetDefault:
                return "SET DEFAULT";
            case DatabaseMetaData.importedKeySetNull:
                return "SET NULL";
            default:
                return "NO ACTION";
        }
    }

    /**
     * @param constraintsAtEnd the constraintsAtEnd to set
     */
    public void setConstraintsAtEnd(boolean constraintsAtEnd) {
        this.constraintsAtEnd = constraintsAtEnd;
    }

    /**
     * @return the constraintsAtEnd
     */
    public boolean isConstraintsAtEnd() {
        return constraintsAtEnd;
    }

    /**
     * @param includeExternalReferences the includeExternalReferences to set
     */
    public void setIncludeExternalReferences(boolean includeExternalReferences) {
        this.includeExternalReferences = includeExternalReferences;
    }

    /**
     * @return the includeExternalReferences
     */
    public boolean isIncludeExternalReferences() {
        return includeExternalReferences;
    }

    /**
     * @param qualifyTableNames the qualifyTableNames to set
     */
    public void setQualifyTableNames(boolean qualifyTableNames) {
        this.qualifyTableNames = qualifyTableNames;
    }

    /**
     * @return the qualifyTableNames
     */
    public boolean isQualifyTableNames() {
        return qualifyTableNames;
    }
    
}
