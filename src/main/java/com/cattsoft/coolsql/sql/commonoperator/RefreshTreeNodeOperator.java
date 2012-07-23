/*
 * Created date: 2006-9-8
 */
package com.cattsoft.coolsql.sql.commonoperator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.sql.Database;
import com.cattsoft.coolsql.sql.ISQLDatabaseMetaData;
import com.cattsoft.coolsql.sql.model.Catalog;
import com.cattsoft.coolsql.sql.model.Schema;
import com.cattsoft.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.cattsoft.coolsql.view.bookmarkview.INodeFilter;
import com.cattsoft.coolsql.view.bookmarkview.model.CatalogNode;
import com.cattsoft.coolsql.view.bookmarkview.model.DefaultTreeNode;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.bookmarkview.model.SQLGroupNode;
import com.cattsoft.coolsql.view.bookmarkview.model.SchemaNode;

/**
 * Refresh bookmark treenode.
 * @author liu_xlin
 */
public class RefreshTreeNodeOperator implements Operatable {

    /* ���� Javadoc��
     * @see com.coolsql.sql.common.Operatable#operate(java.lang.Object)
     */
    public void operate(Object arg) throws UnifyException,SQLException{
       if(arg==null)
           throw new UnifyException("no operate object!");
       if(!(arg instanceof Bookmark))
       {
           throw new UnifyException("operate object error! class:"+arg.getClass());
       }
       Bookmark bookmark=(Bookmark)arg;
       Database db=bookmark.getDbInfoProvider();
       ISQLDatabaseMetaData dbmd=db.getDatabaseMetaData();
       //Get bookmark node by bookmark alias
       DefaultTreeNode node=BookmarkTreeUtil.getInstance().getBookMarkNodeByAlias(bookmark.getAliasName());
       node.setExpanded(true);
       INodeFilter filter=node.getNodeFilter();
       int childCount=node.getChildCount();
       if(childCount>0)  //Delete all children nodes except sqlgroup node if bookmark has connnected to database.
       {
           for(int i=childCount-1;i>0;i--)
           {
               node.remove(i);
           }
       }else //Create a new sqlgroup node if bookmark is connecting to database.
       {
           
           Identifier groupId=new SQLGroupNode("Recent SQL Statements",bookmark);
           DefaultTreeNode sqlGroup=new DefaultTreeNode(groupId);
           
//           XMLParse parser=XMLParse.getParser();
//           List list=parser.getRecentSQL(bookmark.getAliasName());
//            
//           for(int i=0;i<list.size();i++)
//           {
//               Identifier sqlId=new SQLNode((String)list.get(i),bookmark,null);
//               if(filter!=null&&!filter.filter(sqlId))
//               {
//            	   continue;
//               }
//            	   
//               DefaultTreeNode sqlNode=new DefaultTreeNode(sqlId);
//               sqlGroup.add(sqlNode);
//           }
           node.add(sqlGroup);
       }
       
       List<Identifier> ids=new ArrayList<Identifier>();
       if (dbmd.supportsCatalogs()) // Support catalog
		{
			String[] catalogs = dbmd.getCatalogs();
			for (int i = 0; i < catalogs.length; i++) {
				String catalog = catalogs[i];
				Identifier id = new CatalogNode(catalog, bookmark, new Catalog(
						catalogs[i]));
				if(filter==null||filter.filter(id))
					ids.add(id);
			}

		} else {  //Support schema
			Schema[] schemas = db.getSchemas(null);// Get all schemas

			for (int i = 0; i < schemas.length; i++) {
				String schemaName = schemas[i].getName();
				Identifier id = new SchemaNode(schemaName, bookmark, schemas[i]);
				if(filter==null||filter.filter(id))
					ids.add(id);
			}
		}
       
       if(!bookmark.isHasChildren())
           bookmark.setHasChildren(true);
       node.addChildren(ids);
       
    }

    /* ���� Javadoc��
     * @see com.coolsql.sql.common.Operatable#operate(java.lang.Object, java.lang.Object)
     */
    public void operate(Object arg0, Object arg1) {

    }

}
