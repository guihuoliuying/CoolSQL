/**
 * 
 */
package com.cattsoft.coolsql.system.close;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.SystemConstant;
import com.cattsoft.coolsql.system.Task;
import com.cattsoft.coolsql.system.favorite.FavoriteManage;

/**
 * @author kenny liu
 * 
 * 2007-11-7 create
 */
public class SaveFavoriteSQLTask implements Task {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.system.Task#execute()
	 */
	public void execute() {
		File f = new File(SystemConstant.favoriteSQLFilePath);
		if (!f.exists() && FavoriteManage.getInstance().getFavoriteSize() == 0)
			return;

		ObjectOutputStream out = null;
		try {

			out = new ObjectOutputStream(new FileOutputStream(f));
			out.writeObject(FavoriteManage.getInstance().getSQLList());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.system.Task#getDescribe()
	 */
	public String getDescribe() {

		return PublicResource
				.getString("system.closetask.savefavoritesql.describe");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.system.Task#getTaskLength()
	 */
	public int getTaskLength() {
		return 1;
	}

}
