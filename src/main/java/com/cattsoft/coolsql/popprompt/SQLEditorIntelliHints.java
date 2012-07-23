/**
 * 
 */
package com.cattsoft.coolsql.popprompt;

import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.pub.editable.TextToken;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.Database;
import com.cattsoft.coolsql.sql.ISQLDatabaseMetaData;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.sqleditor.EditorPanel;
import com.cattsoft.coolsql.view.sqleditor.pop.BaseListCell;
import com.jidesoft.swing.DelegateAction;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-4-6 create
 */
public class SQLEditorIntelliHints extends AbstractEditorIntelliHints {
	
	private boolean _caseSensitive = false;
    private List<BaseListCell> _completionList;

    private int firstCaretPos;
    private int caretPos;
    private int contextStartPos;
//    private boolean isPopped=false;
    private Bookmark currentBookmark;
    
    /**
     * record keyword
     */
    private String catalog;
    private String schema;
    private String entity;
    
    private KeyStroke showActionKS;
    public SQLEditorIntelliHints(EditorPanel comp, List<BaseListCell> completionList) {
        super(comp);
        setCompletionList(completionList);
        initCommon();
    }

    public SQLEditorIntelliHints(EditorPanel comp, BaseListCell[] completionList) {
        super(comp);
        setCompletionList(completionList);
        initCommon();
    }
    @Override
    public KeyStroke getShowHintsKeyStroke() {
        
    	return showActionKS;
    }
    public void setShowHintsKeyStroke(KeyStroke ks)
    {
    	if(showActionKS==ks)
    		return;
    	DelegateAction.restoreAction(getTextComponent(), JComponent.WHEN_FOCUSED, getShowHintsKeyStroke());
		showActionKS=ks;
		DelegateAction.replaceAction(getTextComponent(), JComponent.WHEN_FOCUSED, getShowHintsKeyStroke(), getDefaultShowAction());
    }
    private void initCommon()
    {
    	setAutoPopup(false);
    	showActionKS=KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, KeyEvent.ALT_MASK);
    }
    /**
     * Gets the list of hints.
     *
     * @return the list of hints.
     */
    public List<BaseListCell> getCompletionList() {
        return _completionList;
    }

    /**
     * Sets a new list of hints.
     *
     * @param completionList
     */
    public void setCompletionList(List<BaseListCell> completionList) {
        _completionList = completionList;
    }

    /**
     * Sets a new list of hints.
     *
     * @param completionList
     */
    public void setCompletionList(BaseListCell[] completionList) {
        final BaseListCell[] list = completionList;
        _completionList = new AbstractList<BaseListCell>() {
            @Override
            public BaseListCell get(int index) {
                return list[index];
            }

            @Override
            public int size() {
                return list.length;
            }
        };
    }
    public void showHintsPopupForOutside() {
    	super.showHintsPopup(false);
    }
    public void hiddenPopup()
    {
    	super.hideHintsPopup();
    }
    public boolean updateHints(Object context) {
        if (context == null) {
            return false;
        }
        String s = context.toString();
        if (s.length() == 0) {
            return false;
        }

        List<BaseListCell> possibleStrings = new ArrayList<BaseListCell>();
        for (BaseListCell cell : getCompletionList()) {
            if(catalog!=null)
            {
            	if (!isCaseSensitive()) {
            		if(!catalog.equalsIgnoreCase(cell.getCatalog()))
	            		continue;
            	}else
            	{
	            	if(!catalog.equals(cell.getCatalog()))
	            		continue;
            	}
            }
            if(schema!=null)
            {
            	if (!isCaseSensitive()) {
	            	if(!schema.equalsIgnoreCase(cell.getSchema()))
	            		continue;
            	}else
            	{
            		if(!schema.equals(cell.getSchema()))
	            		continue;
            	}
            }

            int substringLen=entity==null?0:entity.length();
            if(substringLen>cell.getEntity().length())
            	continue;
            
            if (!isCaseSensitive()) {
                if (entity==null||entity.equalsIgnoreCase(cell.getEntity().substring(0, substringLen)))
                    possibleStrings.add(cell);
            }
            else
            {
            	if (entity==null||cell.getEntity().startsWith(entity))
            		possibleStrings.add(cell);
            }
        }
//        if(possibleStrings.size()==1)
//        {
//        	BaseListCell cell=possibleStrings.get(0);
//        	acceptHint(cell.getDisplayLabel());
//        	return false;
//        }
        setListData(possibleStrings);
        return possibleStrings.size() > 0;
    }
    @Override
    public void acceptHint(Object selected) {
    	
    	if(selected==null)
    		return;
    	((EditorPanel)getTextComponent()).setText(contextStartPos,caretPos,selected.toString());
    }
    @Override
    protected Object getContext() {
		int pos = getTextComponent().getCaretPosition()-1;
		caretPos = pos+1;
		if(!isHintsPopupVisible())
		{
			currentBookmark=BookmarkManage.getInstance().getDefaultBookmark();
			firstCaretPos=pos;
		}
		else
		{
			if(firstCaretPos>pos)
				return null;
		}
		if (pos == 0) {
			return null;
		} else {
			EditorPanel pane = (EditorPanel) getTextComponent();
			TextToken tt = pane.getTextTokenOnOffset(pos+1);
			if(!isHintsPopupVisible())
				contextStartPos = tt.getStart();
			String text = tt.getText();
			
			if(text==null)
				return null;
	        if(!parseKeyword(text))
	        	return null;
	        if(!isHintsPopupVisible())
	        {
	        	List<BaseListCell> allData=getDataList(catalog,schema,entity);
	        	if(allData==null||allData.size()==0)
	        		return null;
	        	setCompletionList(allData);
	        }
			return text;
		}
	}
    private boolean parseKeyword(String orignalStr)
    {
    	if(!currentBookmark.isConnected())
    		return false;
    	orignalStr=StringUtil.trim(orignalStr);
    	if(orignalStr.equals(""))
    		return false;
    	
    	ISQLDatabaseMetaData md;
		try {
			md = currentBookmark.getDbInfoProvider().getDatabaseMetaData();
		} catch (UnifyException e1) {
			LogProxy.errorLog(e1.getMessage(),e1);
			return false;
		}
    	String quoter;
    	try {
			quoter=md.getIdentifierQuoteString();
		} catch (SQLException e) {
			quoter=null;
		}
        if (quoter != null
	            && quoter.equals(" "))
	    {
        		quoter = null;
	    }
        
        boolean isSupportCatalog;
        boolean isSupportSchema;
		try {
			isSupportCatalog=md.supportsCatalogs();
			isSupportSchema=md.supportsSchemas();
		} catch (SQLException e) {
			LogProxy.errorLog(e.getMessage(),e);
			return false;
		}
        
		entity=null;
		schema=null;
		catalog=null;
		
    	String[] splits = orignalStr.split("\\.");
    	if(orignalStr.charAt(orignalStr.length()-1)=='.')
    	{
			orignalStr+=".";
			String[] tmp=splits;
			splits=new String[tmp.length+1];
			int i=0;
			for(;i<tmp.length;i++)
				splits[i]=tmp[i];
			splits[i]=null;
    	}
    	if(splits.length==1)
    	{
    		entity = removeQuotes(splits[0]);
            if(entity!=null&&quoter!=null)
            	entity=entity.replaceAll(quoter, "");
    	}else if(splits.length==2)
    	{
    		if(splits[1]!=null)
    			entity=removeQuotes(splits[1]).replaceAll(quoter, "");
    		if(isSupportSchema)
    		{
    			if(splits[0]!=null)
    				schema=removeQuotes(splits[0]).replaceAll(quoter, "");
    		}else if(isSupportCatalog)
    		{
    			if(splits[0]!=null)
    				catalog=removeQuotes(splits[0]).replaceAll(quoter, "");
    		}else
    		{
    			entity=removeQuotes(orignalStr).replaceAll(quoter, "");
    		}
    		
    	}else if(splits.length==3)
    	{
    		if(splits[2]!=null)
    			entity=removeQuotes(splits[2]).replaceAll(quoter, "");
    		schema=removeQuotes(splits[1]).replaceAll(quoter, "");
    		catalog=removeQuotes(splits[0]).replaceAll(quoter, "");
    	}else
    		return false;
    	return true;
    }
    private static String removeQuotes(String objectName)
    {
    	if(objectName==null||objectName.equals(""))
    		return objectName;
       String ret = objectName.trim();


       while(ret.startsWith("\"") || ret.startsWith("/"))
       {
          ret = ret.substring(1);
       }

       while(ret.endsWith("\"") || ret.endsWith("/"))
       {
           ret = ret.substring(0,ret.length()-1);
       }
       
       return ret;
    }
    /**
	 * Checks if it used case sensitive search. By defaul it's false.
	 * 
	 * @return if it's case sensitive.
	 */
    public boolean isCaseSensitive() {
        return _caseSensitive;
    }

    /**
     * Sets the case sensitive flag. By default, it's false meaning it's a case insensitive search.
     *
     * @param caseSensitive
     */
    public void setCaseSensitive(boolean caseSensitive) {
        _caseSensitive = caseSensitive;
    }

	/**
	 * @return the caretPos
	 */
	public int getCaretPos() {
		return this.caretPos;
	}

	/**
	 * @param caretPos the caretPos to set
	 */
	public void setCaretPos(int caretPos) {
		this.caretPos = caretPos;
	}

	/**
	 * @return the contextStartPos
	 */
	public int getContextStartPos() {
		return this.contextStartPos;
	}

	/**
	 * @param contextStartPos the contextStartPos to set
	 */
	public void setContextStartPos(int contextStartPos) {
		this.contextStartPos = contextStartPos;
	}
    private List<BaseListCell> getDataList(String catalog,String schema,String entity) {
        Bookmark bookmark = BookmarkManage.getInstance().getDefaultBookmark();
        Database db = null;
        List<BaseListCell> list = new ArrayList<BaseListCell>();
        try {
            db = bookmark.getDbInfoProvider();

            ISQLDatabaseMetaData sdmd = db.getDatabaseMetaData();
            if (sdmd.storesLowerCaseIdentifiers()) {
            	if (catalog != null) {
            		catalog = catalog.toLowerCase();
            	}
            	if (schema != null) {
            		schema = schema.toLowerCase();
            	}
            	if (entity != null) {
            		entity = entity.toLowerCase();
            	}
            } else if (sdmd.storesUpperCaseIdentifiers()) {
            	if (catalog != null) {
            		catalog = catalog.toUpperCase();
            	}
            	if (schema != null) {
            		schema = schema.toUpperCase();
            	}
            	if (entity != null) {
            		entity = entity.toUpperCase();
            	}
            }
            
			String[] promptTypes = getPromptTypes();
			List<Entity> entities = db.queryEntities(bookmark, bookmark
					.getConnection(), catalog, schema, entity == null
					? null
					: (entity + "%"), promptTypes);

			for (Entity en : entities) {
				BaseListCell cell = new BaseListCell(en.getCatalog(), en
						.getSchema(), en.getName(), en.getType());
				list.add(cell);
			}
			return list;
        } catch (UnifyException e) {
            LogProxy.errorReport(e);
            return null;
        } catch (SQLException e) {
            LogProxy.SQLErrorReport(e);
            return null;
        }

    }
    private String[] getPromptTypes()
    {
    	String str=Setting.getInstance().getProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_PROMPTTYPE,null);
//    			"TABLE");
    	if(str==null)
    		return null;
    	
    	String[] s=str.split(",");
    	if(s.length==0)
    		return null;
    	return s;
    }
}
