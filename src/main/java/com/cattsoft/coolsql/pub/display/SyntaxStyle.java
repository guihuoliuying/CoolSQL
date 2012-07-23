/*
 * SyntaxStyle.java - A simple text style class
 * Copyright (C) 1999 Slava Pestov
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */
package com.cattsoft.coolsql.pub.display;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A simple text style class. It can specify the color, italic flag,
 * and bold flag of a run of text.
 * @author Slava Pestov
 * @version $Id: SyntaxStyle.java,v 1.1 2010/08/03 14:09:19 xiaolin Exp $
 */
public class SyntaxStyle
{
	/**
	 * Creates a new SyntaxStyle.
	 * @param color The text color
	 * @param italic True if the text should be italics
	 * @param bold True if the text should be bold
	 */
	public SyntaxStyle(Color color, boolean italic, boolean bold)
	{
		this.color = color;
		this.italic = italic;
		this.bold = bold;
		
		pcs=new PropertyChangeSupport(this);
	}

	/**
	 * Returns the color specified in this style.
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * Returns true if no font styles are enabled.
	 */
	public boolean isPlain()
	{
		return !(bold || italic);
	}

	/**
	 * Returns true if italics is enabled for this style.
	 */
	public boolean isItalic()
	{
		return italic;
	}

	/**
	 * Returns true if boldface is enabled for this style.
	 */
	public boolean isBold()
	{
		return bold;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		Color oldValue=this.color;
		this.color = color;
		pcs.firePropertyChange("color", oldValue, this.color);
	}

	/**
	 * @param italic the italic to set
	 */
	public void setItalic(boolean italic) {
		lastFont=null;
		boolean oldValue=this.italic;
		this.italic = italic;
		pcs.firePropertyChange("italic", oldValue, this.italic);
	}

	/**
	 * @param bold the bold to set
	 */
	public void setBold(boolean bold) {
		lastFont=null;
		boolean oldValue=this.bold;
		this.bold = bold;
		pcs.firePropertyChange("bold", oldValue, this.bold);
	}
	/**
	 * Returns the specified font, but with the style's bold and
	 * italic flags applied.
	 */
	public Font getStyledFont(Font font)
	{
		if(font == null)
			throw new NullPointerException("font param must not be null");
		if(font.equals(lastFont))
			return lastStyledFont;
		lastFont = font;
		lastStyledFont = new Font(font.getFamily(),
			(bold ? Font.BOLD : 0)
			| (italic ? Font.ITALIC : 0),
			font.getSize());
		return lastStyledFont;
	}

	/**
	 * Returns the font metrics for the styled font.
	 */
	public FontMetrics getFontMetrics(Font font)
	{
		if(font == null)
			throw new NullPointerException("font param must not be null");
		if(font.equals(lastFont) && fontMetrics != null)
			return fontMetrics;
		lastFont = font;
		lastStyledFont = new Font(font.getFamily(), 
															(bold ? Font.BOLD : 0) | (italic ? Font.ITALIC : 0),
															font.getSize());
		fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(lastStyledFont);
		return fontMetrics;
	}

	/**
	 * Sets the foreground color and font of the specified graphics
	 * context to that specified in this style.
	 * @param gfx The graphics context
	 * @param font The font to add the styles to
	 */
	public void setGraphicsFlags(Graphics gfx, Font font)
	{
		Font _font = getStyledFont(font);
		gfx.setFont(_font);
		gfx.setColor(color);
	}
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		if(listener!=null)
			pcs.addPropertyChangeListener(listener);
	}
	public void removePropertyListener(PropertyChangeListener listener)
	{
		pcs.removePropertyChangeListener(listener);
	}
	protected void firePropertyChange(String name,boolean old)
	{
		
	}
	/**
	 * Returns a string representation of this object.
	 */
	public String toString()
	{
		return getClass().getName() + "[color=" + color + (italic ? ",italic" : "") + (bold ? ",bold" : "") + "]";
	}

	// private members
	private Color color;
	private boolean italic;
	private boolean bold;
	private Font lastFont;
	private Font lastStyledFont;
	private FontMetrics fontMetrics;
	
	private PropertyChangeSupport pcs;
}
