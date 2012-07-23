/**
 * 
 */
package com.cattsoft.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.system.favorite.FavoriteSQLAdjustFrame;

/**
 * @author kenny liu
 *
 * 2007-11-5 create
 */
public class AdjustFavoriteAction extends CsAction {

	private static final long serialVersionUID = 1L;

	public AdjustFavoriteAction()
	{
		initMenuDefinitionById("AdjustFavoriteAction");
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void executeAction(ActionEvent e) {
		FavoriteSQLAdjustFrame frame=new FavoriteSQLAdjustFrame();
		frame.toCenter();
		frame.setVisible(true);

	}

}
