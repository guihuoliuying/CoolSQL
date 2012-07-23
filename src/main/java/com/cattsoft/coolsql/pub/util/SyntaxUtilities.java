package com.cattsoft.coolsql.pub.util;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.text.Segment;
import javax.swing.text.TabExpander;
import javax.swing.text.Utilities;

import com.cattsoft.coolsql.pub.display.SyntaxStyle;
import com.cattsoft.coolsql.pub.display.Token;
import com.cattsoft.coolsql.pub.display.Token.CustomToken;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;

/**
 * Class with several utility functions used by jEdit's syntax colorizing
 * subsystem.
 *
 * @author kenny liu
 */
public class SyntaxUtilities
{
	/**
	 * Checks if a subregion of a <code>Segment</code> is equal to a
	 * string.
	 * @param ignoreCase True if case should be ignored, false otherwise
	 * @param text The segment
	 * @param offset The offset into the segment
	 * @param match The string to match
	 */
	public static boolean regionMatches(boolean ignoreCase, Segment text,
					    int offset, String match)
	{
		int length = offset + match.length();
		char[] textArray = text.array;
		if(length > text.offset + text.count)
			return false;
		for(int i = offset, j = 0; i < length; i++, j++)
		{
			char c1 = textArray[i];
			char c2 = match.charAt(j);
			if(ignoreCase)
			{
				c1 = Character.toUpperCase(c1);
				c2 = Character.toUpperCase(c2);
			}
			if(c1 != c2)
				return false;
		}
		return true;
	}
	
	/**
	 * Checks if a subregion of a <code>Segment</code> is equal to a
	 * character array.
	 * @param ignoreCase True if case should be ignored, false otherwise
	 * @param text The segment
	 * @param offset The offset into the segment
	 * @param match The character array to match
	 */
	public static boolean regionMatches(boolean ignoreCase, Segment text,
					    int offset, char[] match)
	{
		int length = offset + match.length;
		char[] textArray = text.array;
		if(length > text.offset + text.count)
			return false;
		for(int i = offset, j = 0; i < length; i++, j++)
		{
			char c1 = textArray[i];
			char c2 = match[j];
			if(ignoreCase)
			{
				c1 = Character.toUpperCase(c1);
				c2 = Character.toUpperCase(c2);
			}
			if(c1 != c2)
				return false;
		}
		return true;
	}
	private static final String[] colorPropertyNames=new String[Token.getIDCount()];
	static{
		colorPropertyNames[Token.COMMENT1]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_COLOR;
		colorPropertyNames[Token.COMMENT2]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_COLOR;
		colorPropertyNames[Token.KEYWORD1]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_COLOR;
		colorPropertyNames[Token.KEYWORD2]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_COLOR;
		colorPropertyNames[Token.KEYWORD3]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_COLOR;
		colorPropertyNames[Token.LITERAL1]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_COLOR;
		colorPropertyNames[Token.LITERAL2]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL2_COLOR;
		colorPropertyNames[Token.LABEL]=null;
		colorPropertyNames[Token.OPERATOR]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_COLOR;
		colorPropertyNames[Token.INVALID]=null;
		colorPropertyNames[Token.NUMBER]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_COLOR;
	}

	private static final String[] boldPropertyNames=new String[Token.getIDCount()];
	static{
		boldPropertyNames[Token.COMMENT1]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_ISBOLD;
		boldPropertyNames[Token.COMMENT2]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_ISBOLD;
		boldPropertyNames[Token.KEYWORD1]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_ISBOLD;
		boldPropertyNames[Token.KEYWORD2]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_ISBOLD;
		boldPropertyNames[Token.KEYWORD3]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_ISBOLD;
		boldPropertyNames[Token.LITERAL1]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_ISBOLD;
		boldPropertyNames[Token.LITERAL2]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL2_ISBOLD;
		boldPropertyNames[Token.LABEL]=null;
		boldPropertyNames[Token.OPERATOR]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_ISBOLD;
		boldPropertyNames[Token.INVALID]=null;
		boldPropertyNames[Token.NUMBER]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_ISBOLD;
	}
	private static final String[] italicPropertyNames=new String[Token.getIDCount()];
	static{
		italicPropertyNames[Token.COMMENT1]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_ISITALIC;
		italicPropertyNames[Token.COMMENT2]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_ISITALIC;
		italicPropertyNames[Token.KEYWORD1]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_ISITALIC;
		italicPropertyNames[Token.KEYWORD2]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_ISITALIC;
		italicPropertyNames[Token.KEYWORD3]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_ISITALIC;
		italicPropertyNames[Token.LITERAL1]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_ISITALIC;
		italicPropertyNames[Token.LITERAL2]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL2_ISITALIC;
		italicPropertyNames[Token.LABEL]=null;
		italicPropertyNames[Token.OPERATOR]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_ISITALIC;
		italicPropertyNames[Token.INVALID]=null;
		italicPropertyNames[Token.NUMBER]=PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_ISITALIC;
	}
	private static SyntaxStyle[] styles=null;
	
	/**
	 * Returns the default style table. This can be passed to the
	 * <code>setStyles()</code> method of <code>SyntaxDocument</code>
	 * to use the default syntax styles.
	 */
	public static SyntaxStyle[] getDefaultSyntaxStyles()
	{
		if(styles!=null)
			return styles;
		
		styles = new SyntaxStyle[Token.getIDCount()];

		Setting sett = Setting.getInstance();
		
		styles[Token.COMMENT1] = new SyntaxStyle(sett.getColorProperty(
				colorPropertyNames[Token.COMMENT1], Color.GRAY),
				sett.getBoolProperty(italicPropertyNames[Token.COMMENT1], true),
				sett.getBoolProperty(boldPropertyNames[Token.COMMENT1], false));
		styles[Token.COMMENT2] = new SyntaxStyle(sett.getColorProperty(
				colorPropertyNames[Token.COMMENT2], Color.GRAY),
				sett.getBoolProperty(italicPropertyNames[Token.COMMENT2], true),
				sett.getBoolProperty(boldPropertyNames[Token.COMMENT2], false));
		styles[Token.KEYWORD1] = new SyntaxStyle(sett.getColorProperty(
				colorPropertyNames[Token.KEYWORD1], Color.BLUE),
				sett.getBoolProperty(italicPropertyNames[Token.KEYWORD1], false),
				sett.getBoolProperty(boldPropertyNames[Token.KEYWORD1], false));
		styles[Token.KEYWORD2] = new SyntaxStyle(sett.getColorProperty(
				colorPropertyNames[Token.KEYWORD2], Color.MAGENTA),
				sett.getBoolProperty(italicPropertyNames[Token.KEYWORD2], false),
				sett.getBoolProperty(boldPropertyNames[Token.KEYWORD2], false));
		styles[Token.KEYWORD3] = new SyntaxStyle(sett.getColorProperty(
				colorPropertyNames[Token.KEYWORD3], new Color(0x009600)),
				sett.getBoolProperty(italicPropertyNames[Token.KEYWORD3], false),
				sett.getBoolProperty(boldPropertyNames[Token.KEYWORD3], false));
		styles[Token.LITERAL1] = new SyntaxStyle(sett.getColorProperty(
				colorPropertyNames[Token.LITERAL1], new Color(0x650099)),
				sett.getBoolProperty(italicPropertyNames[Token.LITERAL1], false),
				sett.getBoolProperty(boldPropertyNames[Token.LITERAL1], false));
		styles[Token.LITERAL2] = new SyntaxStyle(sett.getColorProperty(
				colorPropertyNames[Token.LITERAL2], new Color(0x650099)),
				sett.getBoolProperty(italicPropertyNames[Token.LITERAL2], false),
				sett.getBoolProperty(boldPropertyNames[Token.LITERAL2], true));
		styles[Token.LABEL] = new SyntaxStyle(sett.getColorProperty("coolsql.editor.color.label", new Color(0x990033)),false,true);
		styles[Token.OPERATOR] = new SyntaxStyle(sett.getColorProperty(
				colorPropertyNames[Token.OPERATOR], new Color(228,27,47)),
				sett.getBoolProperty(italicPropertyNames[Token.OPERATOR], false),
				sett.getBoolProperty(boldPropertyNames[Token.OPERATOR], false));
		styles[Token.INVALID] = new SyntaxStyle(sett.getColorProperty("coolsql.editor.color.invalid", Color.RED),false,true);
		styles[Token.NUMBER] = new SyntaxStyle(sett.getColorProperty(
				colorPropertyNames[Token.NUMBER], Color.RED),
				sett.getBoolProperty(italicPropertyNames[Token.NUMBER], false),
				sett.getBoolProperty(boldPropertyNames[Token.NUMBER], false));
		
		PropertyChangeListener listener=new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().endsWith("color"))
				{
					for(int i=0;i<colorPropertyNames.length;i++)
					{
						if(evt.getPropertyName().equals(colorPropertyNames[i]))
						{
							if(evt.getNewValue()==null)
								return;
							styles[i].setColor(Setting.getInstance().getColorProperty(colorPropertyNames[i], null));
							return;
						}
					}
				}else if(evt.getPropertyName().endsWith("isbold"))
				{
					for(int i=0;i<boldPropertyNames.length;i++)
					{
						if(evt.getPropertyName().equals(boldPropertyNames[i]))
						{
							if(evt.getNewValue()==null)
								return;
							styles[i].setBold(new Boolean((String)evt.getNewValue()).booleanValue());
							return;
						}
					}
				}else if(evt.getPropertyName().endsWith("isitalic"))
				{
					for(int i=0;i<italicPropertyNames.length;i++)
					{
						if(evt.getPropertyName().equals(italicPropertyNames[i]))
						{
							if(evt.getNewValue()==null)
								return;
							styles[i].setItalic(new Boolean((String)evt.getNewValue()).booleanValue());
							return;
						}
					}
				}
			}
			
		};
		
		sett.addPropertyChangeListener(listener, colorPropertyNames);
		sett.addPropertyChangeListener(listener, italicPropertyNames);
		sett.addPropertyChangeListener(listener, boldPropertyNames);
		
		CustomToken[] tokens=Token.getCustomTokens();
		for(int i=0;i<tokens.length;i++)
		{
			styles[tokens[i].getId()]=new SyntaxStyle(tokens[i].getColor(),false,false);
		}
		return styles;
	}

	
	/**
	 * Paints the specified line onto the graphics context. Note that this
	 * method munges the offset and count values of the segment.
	 * @param line The line segment
	 * @param tokens The token list for the line
	 * @param styles The syntax style list
	 * @param expander The tab expander used to determine tab stops. May
	 * be null
	 * @param gfx The graphics context
	 * @param x The x co-ordinate
	 * @param y The y co-ordinate
	 * @param addwidth Additional spacing to be added to the line width
	 * @return The x co-ordinate, plus the width of the painted string
	 */
	public static int paintSyntaxLine(Segment line, Token tokens,
		SyntaxStyle[] styles, TabExpander expander, Graphics gfx,
		int x, int y, int addwidth)
	{
		Font defaultFont = gfx.getFont();
		Color defaultColor = gfx.getColor();

		int offset = 0;
		while (true)
		{
			byte id = tokens.id;
			if(id == Token.END)
				break;

			int length = tokens.length;
			if(id == Token.NULL)
			{
//				if(!defaultColor.equals(gfx.getColor()))
					gfx.setColor(defaultColor);
//				if(!defaultFont.equals(gfx.getFont()))
					gfx.setFont(defaultFont);
			}
			else
			{
				styles[id].setGraphicsFlags(gfx,defaultFont);
			}
			line.count = length;
			x = Utilities.drawTabbedText(line,x,y,gfx,expander,addwidth);
			line.offset += length;
			offset += length;

			tokens = tokens.next;
		}

		return x;
	}

	// private members
	private SyntaxUtilities() {}
}
