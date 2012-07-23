package com.cattsoft.coolsql.sql.formater;

import javax.swing.text.BadLocationException;

import org.apache.log4j.Logger;

import com.cattsoft.coolsql.pub.display.JEditTextArea;
import com.cattsoft.coolsql.pub.display.SyntaxDocument;

public class TextCommenter 
{
	private static final Logger logger=Logger.getLogger(TextCommenter.class);
	private JEditTextArea editor;
	
	public TextCommenter(JEditTextArea client)
	{
		this.editor = client;
	}
	
	public void commentSelection()
	{
		String commentChar = editor.getCommentChar();
		boolean isCommented = this.isSelectionCommented(commentChar);
		// Comment Selection acts as a toggle.
		// if the complete selection is already commented
		// the comments will be removed.
		doComment(commentChar, !isCommented);
	}
	/**
	 * �༭�����ѡ�������Ƿ�ע��
	 * @return true:�Ѿ���ע�͹�,false:δ��ע��
	 */
	public boolean isSelectionCommented()
	{
		String commentChar = editor.getCommentChar();
		return this.isSelectionCommented(commentChar);
	}
	public void unCommentSelection()
	{
		doComment(editor.getCommentChar(), false);
	}
	
	private void doComment(String commentChar, boolean comment)
	{
		int startline = editor.getSelectionStartLine();
		int realEndline = editor.getSelectionEndLine();
		int endline = realEndline;

		if (commentChar == null) commentChar = "--";
		
		int cLength = commentChar.length();
		
//		int pos = editor.getSelectionEnd(endline) - editor.getLineStartOffset(endline);
//		if (pos == 0 && endline > 0) endline --;
		SyntaxDocument document = editor.getDocument();
		
		try
		{
			document.beginCompoundEdit();
			for (int line = startline; line <= endline; line ++)
			{
				String text = editor.getLineText(line);
				if (text == null || text.trim().length() == 0) continue;
				int lineStart = editor.getLineStartOffset(line);
				if (comment)
				{
					document.insertString(lineStart, commentChar, null);
				}
				else
				{
					int pos = text.indexOf(commentChar);
					if (pos > -1)
					{
						document.remove(lineStart, pos + cLength);
					}
				}
			}
		}
		catch (BadLocationException e)
		{
			logger.error("Error when processing comment", e);
		}
		finally
		{
			document.endCompoundEdit();
		}
	}

	protected boolean isSelectionCommented(String commentChar)
	{
		int startline = editor.getSelectionStartLine();
		int realEndline = editor.getSelectionEndLine();
		int endline = realEndline;
		if (commentChar == null) commentChar = "--";
		
//		int pos = editor.getSelectionEnd(endline) - editor.getLineStartOffset(endline);
//		if (pos == 0 && endline > 0) endline --;
		
		for (int line = startline; line <= endline; line ++)
		{
			String text = editor.getLineText(line);
			if (text == null || text.trim().length() == 0) continue;
			if (!text.startsWith(commentChar)) return false;
		}
		return true;
	}
	
}
