/**
 * CoolSQL 2009 copyright.
 * You must confirm to the GPL license if want to reuse and redistribute the codes.
 * 2009-12-22
 */
package com.cattsoft.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import javax.swing.text.BadLocationException;

import com.cattsoft.coolsql.action.framework.Auto2CsAction;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.sqleditor.EditorPanel;

/**
 * @author Kenny Liu
 *
 * 2009-12-22 create
 */
@SuppressWarnings("serial")
public class DeleteSelectedLinesAction extends Auto2CsAction {
	
	public DeleteSelectedLinesAction() {
		super();
	}

	@Override
    public void executeAction(ActionEvent e) {
		EditorPanel pane = ViewManage.getInstance().getSqlEditor()
		.getEditorPane();
		
		int startLine = pane.getSelectionStartLine();
		int endLine = pane.getSelectionEndLine();
		
		int lineCount = pane.getLineCount();
		
		int endOffset = 0;
		int startOffset = pane.getLineStartOffset(startLine);
		if (lineCount - 1 == endLine) {
			endOffset = pane.getLineEndOffset(endLine) - 1;
		} else {
			endOffset = pane.getLineEndOffset(endLine);
		}
		
		try {
			int length = endOffset - startOffset;
			pane.getDocument().remove(startOffset, length);
		} catch (BadLocationException e1) {
			LogProxy.errorLog(e1.getMessage(), e1);
		}
    }
}
