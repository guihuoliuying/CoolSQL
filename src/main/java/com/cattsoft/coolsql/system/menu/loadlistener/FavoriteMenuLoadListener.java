/**
 * 
 */
package com.cattsoft.coolsql.system.menu.loadlistener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.cattsoft.coolsql.action.bookmarkmenu.CopyCommand;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.favorite.FavoriteEvent;
import com.cattsoft.coolsql.system.favorite.FavoriteListener;
import com.cattsoft.coolsql.system.favorite.FavoriteManage;
import com.cattsoft.coolsql.system.menubuild.IMenuLoadListener;

/**
 * �ղؼ۲˵�����ʱִ�еĶ���
 * @author kenny liu
 *
 * 2007-11-3 create
 */
public class FavoriteMenuLoadListener implements IMenuLoadListener {
	private static final String FAVORITE_SQL="favorite_sql";
	private FavoriteItemAction action=new FavoriteItemAction();
	private boolean isChanged;
	public FavoriteMenuLoadListener()
	{
		FavoriteManage.getInstance().addFavoriteListener(new FavoriteChangeListener());
		Setting.getInstance().addPropertyChangeListener(new FavoriteMaxDisplaySizeChangeListener(),
				PropertyConstant.PROPERTY_FAVORITE_DISPLAY_MAXSIZE);
		isChanged=true;
	}
	/* (non-Javadoc)
	 * @see com.coolsql.system.menubuild.IMenuLoadListener#action()
	 */
	public void action(final JMenuItem item) {
		if(!(item instanceof JMenu))
			return;
		((JMenu)item).addMenuListener(new MenuListener()
		{

			public void menuCanceled(MenuEvent e) {
				
			}

			public void menuDeselected(MenuEvent e) {
				
			}

			public void menuSelected(MenuEvent e) {
				if(isChanged)
				{
					if(!(item instanceof JMenu))
					{
						return;
					}
					loadMostOfAllFavorites((JMenu)item);
					isChanged=false;
				}
				
			}
			
		}
		);
		if(FavoriteManage.getInstance().getFavoriteSize()==0)
		{
			JMenuItem noSQL=new JMenuItem("no sql");
			noSQL.setToolTipText("no sql has been Collected!");
			noSQL.setEnabled(false);
			item.add(noSQL);
		}else
		{
			
		}

	}
	protected void loadMostOfAllFavorites(JMenu menu)
	{
		int size=Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_FAVORITE_DISPLAY_MAXSIZE, 15);
		menu.removeAll();
		for(int i=0;i<size&&i<FavoriteManage.getInstance().getFavoriteSize();i++)
		{
			String value=(String)FavoriteManage.getInstance().getFavorite(i);
			JMenuItem item=new JMenuItem(getDisplayStr(value),null);
			item.addActionListener(action);
			item.putClientProperty(FAVORITE_SQL, value);
			item.setToolTipText(value);
			menu.add(item);
		}
		
	}
	private String getDisplayStr(String v)
	{
		String str=StringUtil.trim(v);
		if(str.length()>50)
		{
			str=str.substring(0,50)+"...";
		}
		return str;
	}
	private class FavoriteMaxDisplaySizeChangeListener implements PropertyChangeListener
	{

		/* (non-Javadoc)
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			isChanged=true;
		}
		
	}
	private class FavoriteChangeListener implements FavoriteListener
	{

		/* (non-Javadoc)
		 * @see com.coolsql.system.favorite.FavoriteListener#addFavorite(com.coolsql.system.favorite.FavoriteEvent)
		 */
		public void addFavorite(FavoriteEvent e) {
			isChanged=true;
			
		}

		/* (non-Javadoc)
		 * @see com.coolsql.system.favorite.FavoriteListener#deleteFavorite(com.coolsql.system.favorite.FavoriteEvent)
		 */
		public void deleteFavorite(FavoriteEvent e) {
			isChanged=true;
			
		}

		/* (non-Javadoc)
		 * @see com.coolsql.system.favorite.FavoriteListener#updateFavorite(com.coolsql.system.favorite.FavoriteEvent)
		 */
		public void updateFavorite(FavoriteEvent e) {
			isChanged=true;
			
		}
		
	}
	private class FavoriteItemAction implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e)
		{
			JMenuItem item=(JMenuItem)e.getSource();
			String value=(String)item.getClientProperty(FAVORITE_SQL);
			new CopyCommand(value).execute();
		}
	}
}
