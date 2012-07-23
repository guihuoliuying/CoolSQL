
package com.cattsoft.coolsql.view.sqleditor;

import java.util.Set;

import org.apache.log4j.Logger;

import com.cattsoft.coolsql.pub.display.DelimiterDefinition;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.system.Setting;

public class TextFormatter 
{
	private static final Logger logger=Logger.getLogger(TextFormatter.class);
	public TextFormatter()
	{
	}
	
	public void formatSql(EditorPanel editor, DelimiterDefinition alternateDelimiter, Set dbFunctions, Set dbDatatypes, String lineComment)
	{
		String sql = editor.getSelectedStatement();
		ScriptParser parser = new ScriptParser();
		parser.setAlternateDelimiter(alternateDelimiter);
		parser.setReturnStartingWhitespace(true);
		parser.setAlternateLineComment(lineComment);
		parser.setScript(sql);
		
		String delimit = parser.getDelimiterString();

		int count = parser.getSize();
		if (count < 1) return;

		StringBuilder newSql = new StringBuilder(sql.length() + 100);

//		String end = Settings.getInstance().getInternalEditorLineEnding();
		
		for (int i=0; i < count; i++)
		{
			String command = parser.getCommand(i);

			// no need to format "empty" strings
			if (StringUtil.isEmptyString(command) || StringUtil.isWhitespace(command))
			{
				newSql.append(command);
				continue;
			}
			
			SqlFormatter f = new SqlFormatter(command, Setting.getInstance().getFormatterMaxSubselectLength());
			f.setDBFunctions(dbFunctions);
			f.setDbDataTypes(dbDatatypes);
			int cols = Setting.getInstance().getFormatterMaxColumnsInSelect();
			f.setMaxColumnsPerSelect(cols);
			
			try
			{
				String formattedSql = f.getFormattedSql().toString();
				newSql.append(formattedSql);
				if (!command.trim().endsWith(delimit))
				{
					newSql.append(delimit);
				}
			}
			catch (Exception e)
			{
				logger.error( "Error when formatting SQL", e);
			}
		}

		if (newSql.length() == 0) return;

		if (editor.isTextSelected())
		{
			editor.setSelectedText(newSql.toString());
		}
		else
		{
			editor.setText(newSql.toString());
		}
	}
}
