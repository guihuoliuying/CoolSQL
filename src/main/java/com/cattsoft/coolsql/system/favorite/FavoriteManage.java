/**
 * 
 */
package com.cattsoft.coolsql.system.favorite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.event.EventListenerList;

import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.system.SystemConstant;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * �ղؼеĹ�����
 * @author kenny liu
 *
 * 2007-11-3 create
 */
public class FavoriteManage {

	public static final String CHANGED_STRUT="changed_strut";  //sql�ղؽṹ����仯�����
	private static FavoriteManage fm=null;
	private  List<Object> list=null;//�ղص�sql�б�
	
	private EventListenerList elList;//���ղص�sql�ṹ����仯�����ᷢ���仯֪ͨ
	private FavoriteManage(){
		
		if(list==null)
		{
			list=loadFavoriteSQL();
			if(elList==null)
				elList=new EventListenerList();
		}
	}
	/**
	 * return the size of favorites
	 * @return
	 */
	public int getFavoriteSize()
	{
		return list.size();
	}
	public Object getFavorite(int index)
	{
		return list.get(index);
	}
	public List getSQLList()
	{
		return (List)((ArrayList)list).clone();
	}
	public synchronized static FavoriteManage getInstance()
	{
		if(fm==null)
			fm=new FavoriteManage();
		return fm;
	}
	/**
	 * �ӱ����ļ��м����ղص�sql
	 * @return
	 */
	private List<Object> loadFavoriteSQL()
	{
		List<Object> tmp=new ArrayList<Object>();
		ObjectInputStream input=null;
		try {
			File f=new File(SystemConstant.favoriteSQLFilePath);
			if(!f.exists())
				return tmp;
			input=new ObjectInputStream(new FileInputStream(f));
			tmp=(List<Object>)input.readObject();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			LogProxy.errorMessage(PublicResource.getSystemString("system.favoritesql.load.error")+e);
		} catch (ClassNotFoundException e) {
			LogProxy.errorMessage(PublicResource.getSystemString("system.favoritesql.load.fileformaterror")+e);
		}finally
		{
			if(input!=null)
			{
				try {
					input.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return tmp;
	}
	/**
	 * �ղ�sql
	 * @param sql
	 */
	public void addSQL(String sql)
	{
		sql=StringUtil.trim(sql);
		if(sql.equals(""))
			return ;
		list.add(sql);
		
		FavoriteEvent e=new FavoriteEvent(this);
		fireChanged(FavoriteEvent.ACTION_TYPE_ADDED, e);
	}
	public void addSQLs(Collection sqls)
	{
		list.addAll(sqls);
		FavoriteEvent e=new FavoriteEvent(this);
		fireChanged(FavoriteEvent.ACTION_TYPE_ADDED, e);
	}
	/**
	 * ɾ����sql
	 * @param sql
	 */
	public void deleteSQL(String sql)
	{
		sql=StringUtil.trim(sql);
		if(sql.equals(""))
			return ;
		list.remove(sql);
		FavoriteEvent e=new FavoriteEvent(this);
		fireChanged(FavoriteEvent.ACTION_TYPE_DELETED, e);
	}
	/**
	 * ���sql�Ƿ��Ѿ�����
	 * @param sql
	 * @return true: exist,false :not exist
	 */
	public boolean isExist(String sql)
	{
		return getInstance().list.indexOf(StringUtil.trim(sql))>-1;
	}
	/**
	 * ��������ղص�sql
	 *
	 */
	public void clearAll()
	{
		getInstance().list.clear();
		FavoriteEvent e=new FavoriteEvent(this);
		fireChanged(FavoriteEvent.ACTION_TYPE_DELETED, e);
	}
	/**
	 * 
	 * @param type
	 */
	protected void fireChanged(int type,FavoriteEvent e) {
		Object[] list = elList.getListenerList();
		for (int i = list.length - 2; i >= 0; i--) {
			if (list[i] == FavoriteListener.class) {
				if(type==FavoriteEvent.ACTION_TYPE_ADDED)
				((FavoriteListener) list[i + 1])
						.addFavorite(e);
				else if(type==FavoriteEvent.ACTION_TYPE_DELETED)
					((FavoriteListener) list[i + 1])
					.deleteFavorite(e);
				else if(type==FavoriteEvent.ACTION_TYPE_UPDATED)
					((FavoriteListener) list[i + 1])
					.updateFavorite(e);
					
			}
		}
	}
	public void addFavoriteListener(FavoriteListener listener)
	{
		elList.add(FavoriteListener.class, listener);
	}
	public void removeFavoriteListener(FavoriteListener listener)
	{
		elList.remove(FavoriteListener.class,listener);
	}
}
