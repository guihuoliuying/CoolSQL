/*
 * �������� 2006-7-3
 *
 */
package com.cattsoft.coolsql.pub.editable;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

import com.cattsoft.coolsql.action.common.RedoAction;
import com.cattsoft.coolsql.action.common.TextCopyAction;
import com.cattsoft.coolsql.action.common.TextCutAction;
import com.cattsoft.coolsql.action.common.TextPasteAction;
import com.cattsoft.coolsql.action.common.TextSelectAllAction;
import com.cattsoft.coolsql.action.common.UndoAction;
import com.cattsoft.coolsql.pub.component.BaseMenuManage;
import com.cattsoft.coolsql.pub.component.BasePopupMenu;
import com.cattsoft.coolsql.pub.parse.PublicResource;


/**
 * @author liu_xlin ��ͨ�༭�����Ҽ�˵�
 */
public class TextMenuManage extends BaseMenuManage {

	private DefaultUndoManager undoManage = null;

	private JMenuItem copy = null;

	private JMenuItem cut = null;

	private JMenuItem paste = null;

	private JMenuItem selectAll = null;

	private JMenuItem redo = null;

	private JMenuItem undo = null;

	private Action undoAction=null;
	
	private Action redoAction;
	public TextMenuManage(JTextComponent com) {
		super(com);
		undoManage = new DefaultUndoManager();
		((JTextComponent)this.getComponent()).getDocument().addUndoableEditListener(undoManage);
		
		//��ʼ����ݼ�
		undoAction = new UndoAction(undoManage);
		bindKey("control Z", undoAction,false);
		
		redoAction = new RedoAction(undoManage);
		bindKey("control R", redoAction,false);
	}

	protected void createPopMenu() {
		popMenu = new BasePopupMenu();
		Action cutAction = new TextCutAction((JTextComponent)getComponent());
		cut = this.createMenuItem(PublicResource
				.getString("TextEditor.popmenu.cut"),
				PublicResource.getIcon("TextMenu.icon.cut"),
				cutAction);
		cut.setAccelerator(KeyStroke.getKeyStroke("control X"));
		popMenu.add(cut);
		
		Action copyAction = new TextCopyAction((JTextComponent)getComponent());
		copy = createMenuItem(PublicResource
				.getString("TextEditor.popmenu.copy"), PublicResource.getIcon("TextMenu.icon.copy"),
				copyAction);
		copy.setAccelerator(KeyStroke.getKeyStroke("control C"));
		popMenu.add(copy);

		Action pasteAction = new TextPasteAction((JTextComponent)getComponent());
		paste = createMenuItem(PublicResource
				.getString("TextEditor.popmenu.paste"),
				PublicResource.getIcon("TextMenu.icon.paste"),
				pasteAction);
		paste.setAccelerator(KeyStroke.getKeyStroke("control V"));
		popMenu.add(paste);

		popMenu.addSeparator();
		Action selectAllAction = new TextSelectAllAction((JTextComponent)getComponent());
		selectAll = createMenuItem(PublicResource
				.getString("TextEditor.popmenu.selectAll"), PublicResource.getIcon("popmenu.icon.blank"),
				selectAllAction);
		selectAll.setAccelerator(KeyStroke.getKeyStroke("control A"));
		popMenu.add(selectAll);

		popMenu.addSeparator();
		
		undo = createMenuItem(PublicResource
				.getString("TextEditor.popmenu.undo"), PublicResource.getIcon("TextMenu.icon.undo"), undoAction);
		popMenu.add(undo);
		undo.setAccelerator(KeyStroke.getKeyStroke("control Z"));

		
		redo = createMenuItem(PublicResource
				.getString("TextEditor.popmenu.redo"), PublicResource.getIcon("TextMenu.icon.redo"), redoAction);
		popMenu.add(redo);
		redo.setAccelerator(KeyStroke.getKeyStroke("control R"));
		
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see src.pub.display.BaseMenuManage#itemSet()
	 */
	public BasePopupMenu itemCheck() {
	    if(popMenu==null)
	        createPopMenu();
	    
		JTextComponent tCom=(JTextComponent)getComponent();
		if (!tCom.isEditable() || !tCom.isEnabled()) {
			if (cut.isEnabled()) {
				cut.setEnabled(false);
			}
			if (paste.isEnabled()) {
				paste.setEnabled(false);
			}
			if (undo.isEnabled()) {
				undo.setEnabled(false);
			}
			if (redo.isEnabled()) {
				redo.setEnabled(false);
			}
			if (tCom.getSelectedText() == null
					|| tCom.getSelectedText().trim().equals("")) {
				if (copy.isEnabled()) {
					copy.setEnabled(false);
				}
			} else {
				if (!copy.isEnabled()) {
					copy.setEnabled(true);
				}
			}
		} else {
			if (tCom.getSelectedText() == null
					||tCom.getSelectedText().trim().equals("")) {
				if (cut.isEnabled()) {
					cut.setEnabled(false);
				}
				if (copy.isEnabled()) {
					copy.setEnabled(false);
				}
			} else {
				if (!cut.isEnabled()) {
					cut.setEnabled(true);
				}
				if (!copy.isEnabled()) {
					copy.setEnabled(true);
				}
			}
			if (!paste.isEnabled()) {
				paste.setEnabled(true);
			}
			if (undoManage.canUndo()) {
				if (!undo.isEnabled()) {
					undo.setEnabled(true);
				}
			} else {
				if (undo.isEnabled()) {
					undo.setEnabled(false);
				}
			}
			if (undoManage.canRedo()) {
				if (!redo.isEnabled()) {
					redo.setEnabled(true);
				}
			} else {
				if (redo.isEnabled()) {
					redo.setEnabled(false);
				}
			}
		}
		return popMenu;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see src.pub.display.BaseMenuManage#getPopMenu()
	 */
	public BasePopupMenu getPopMenu() {

		return itemCheck();
	}
    /**
     * @return Returns the undoManage.
     */
    public DefaultUndoManager getUndoManage() {
        return undoManage;
    }
}
