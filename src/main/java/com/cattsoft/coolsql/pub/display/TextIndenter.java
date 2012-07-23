
package com.cattsoft.coolsql.pub.display;

import javax.swing.text.BadLocationException;

import org.apache.log4j.Logger;

public class TextIndenter 
{
	private static final Logger logger=Logger.getLogger(TextIndenter.class);
	
	private JEditTextArea editor;
	
	public TextIndenter(JEditTextArea client)
	{
		this.editor = client;
	}
	
	public void indentSelection()
	{
		this.shiftSelection(true);
	}

	public void unIndentSelection()
	{
		this.shiftSelection(false);
	}
	
	private void shiftSelection(boolean indent)
	{
		int startline = editor.getSelectionStartLine();
		int realEndline = editor.getSelectionEndLine();
		int endline = realEndline;
		int tabSize = editor.getTabSize();
		StringBuffer buff = new StringBuffer(tabSize);
		for (int i=0; i < tabSize; i++) buff.append(' ');
		String spacer = buff.toString();

		int pos = editor.getSelectionEnd(endline) - editor.getLineStartOffset(endline);
		if (pos == 0) endline --;
		SyntaxDocument document = editor.getDocument();
		
		try
		{
			document.beginCompoundEdit();
			for (int line = startline; line <= endline; line ++)
			{
				String text = editor.getLineText(line);
				if (text == null || text.trim().length() == 0) continue;
				int lineStart = editor.getLineStartOffset(line);
				if (indent)
				{
					document.insertString(lineStart, spacer, null);
				}
				else
				{
					char c = text.charAt(0);
					if (c == '\t')
					{
						document.remove(lineStart, 1);
					}
					else
					{
						int numChars = 0;
						while (Character.isWhitespace(text.charAt(numChars)) && numChars < tabSize) numChars ++;
						document.remove(lineStart, numChars);
					}
				}
			}
		}
		catch (BadLocationException e)
		{
			logger.error( "Error when shifting selection", e);
		}
		finally
		{
			document.endCompoundEdit();
		}
	}
	
}
