/**
 * 
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.text.JTextComponent;

import com.cattsoft.coolsql.pub.display.PopMenuMouseListener;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.menubuild.IconResource;

/**
 * the default popup menu of text editor is defined in this class. All JTextComponents can install this popup menu.
 * @author ��Т��(kenny liu)
 * 
 * 2008-3-23 create
 */
public class TextComponentPopMenu {
	
	private JMenuItem copy = null;

	private JMenuItem paste = null;

	private JMenuItem cut = null;

	private JMenuItem selectAll = null;

	private JPopupMenu popMenu = null;
	
	private JTextComponent textComponent;
	public TextComponentPopMenu(JTextComponent textComponent)
	{
		this.textComponent=textComponent;
		if(textComponent!=null)
		{
			popMenu=createPopMenu();
			textComponent.addMouseListener(new TextMouseListener());
		}
	}
	protected JPopupMenu createPopMenu() {
		JPopupMenu pop = new BasePopupMenu();
		copy = new JMenuItem(PublicResource
				.getString("TextEditor.popmenu.copy"),IconResource.ICON_COPY);
		copy.addActionListener(new CopyAction());

		paste = new JMenuItem(PublicResource
				.getString("TextEditor.popmenu.paste"),IconResource.ICON_PASTE);
		paste.addActionListener(new PasteAction());

		cut = new JMenuItem(PublicResource.getString("TextEditor.popmenu.cut"),IconResource.ICON_CUT);
		cut.addActionListener(new CutAction());

		selectAll = new JMenuItem(PublicResource
				.getString("TextEditor.popmenu.selectAll"),IconResource.getBlankIcon());
		selectAll.addActionListener(new SelectAllAction());

		pop.add(cut);
		pop.add(copy);
		pop.add(paste);
		pop.add(new JSeparator());
		pop.add(selectAll);
		return pop;
	}

	/**
	 * �����Ҽ�˵�����Ч��
	 * 
	 */
	public void checkMenuEnable() {
		if (!textComponent.isEnabled()) {
			if (copy.isEnabled())
				copy.setEnabled(false);
			if (cut.isEnabled())
				cut.setEnabled(false);
			if (paste.isEnabled())
				paste.setEnabled(false);
			if (selectAll.isEnabled())
				selectAll.setEnabled(false);
			return;
		}
		if (textComponent.getSelectedText() == null || textComponent.getSelectedText().equals("")) {
			if (copy.isEnabled())
				copy.setEnabled(false);
			if (cut.isEnabled())
				cut.setEnabled(false);

		} else {
			if (!copy.isEnabled())
				copy.setEnabled(true);
			if (!cut.isEnabled())
				cut.setEnabled(true);

		}
		if (!textComponent.isEditable()) {
			if (paste.isEnabled())
				paste.setEnabled(false);
			if (cut.isEnabled())
				cut.setEnabled(false);
		} else {
			if (!paste.isEnabled())
				paste.setEnabled(true);
			// if (!cut.isEnabled())
			// cut.setEnabled(true);
		}
		if (!selectAll.isEnabled())
			selectAll.setEnabled(true);
	}

	protected class TextMouseListener extends PopMenuMouseListener {

		public void mouseReleased(MouseEvent e) {

			if (isPopupTrigger(e)) {
				checkMenuEnable();
				popMenu.show(textComponent, e.getX(), e.getY());
			}
		}
	}

	protected class CopyAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			textComponent.copy();
		}
	}

	protected class PasteAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e) {
			textComponent.paste();
		}
	}

	protected class CutAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e) {
			textComponent.cut();
		}
	}

	protected class SelectAllAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e) {
			textComponent.selectAll();
		}
	}
}
