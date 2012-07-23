/**
 * Create date:2008-5-4
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.border.Border;

import com.cattsoft.coolsql.pub.display.GUIUtil;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-4 create
 */
public class ColorIconButton extends JButton {

	public static final String PROPERTY_ICON_COLOR="iconcolor";
	
	private static final long serialVersionUID = 1L;
	
	private static final Border raisedBorder=BorderFactory.createRaisedBevelBorder();
	private static final Border loweredBorder=BorderFactory.createLoweredBevelBorder();
	
	private Color iconColor;//the color of button icon.
	public ColorIconButton()
	{
		this(null);
	}
	public ColorIconButton(Color iconColor)
	{
		super();
		setMargin(new Insets(1, 1, 1, 1));
        this.setBorder(raisedBorder);
        this.setFocusable(false);
        this.setBackground(null);
        setOpaque(false);
		this.iconColor=iconColor;
		this.setPreferredSize(new Dimension(23, 23));
		setIcon(new ColorIcon());
		
		addMouseListener(new MouseAdapter()
		{
			private boolean isPressed=false;
			@Override
			public void mousePressed(MouseEvent e)
			{
				if(e.getButton()!=MouseEvent.BUTTON1)
					return;
				isPressed=true;
				setBorder(loweredBorder);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				isPressed=false;
				setBorder(raisedBorder);
			}
			public void mouseExited(MouseEvent e)
			{
				if(isPressed)
					setBorder(raisedBorder);
			}
			public void mouseEntered(MouseEvent e)
			{
				if(isPressed)
					setBorder(loweredBorder);
			}
		}
		);
		this.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				Color color=JColorChooser.showDialog(GUIUtil.findLikelyOwnerWindow(),
						"Selection color", getIconColor());
				if(color!=null)
				{
					setIconColor(color);
				}
			}
			
		}
		);
	}
	/**
	 * @return the iconColor
	 */
	public Color getIconColor() {
		return this.iconColor;
	}
	/**
	 * @param iconColor the iconColor to set
	 */
	public void setIconColor(Color iconColor) {
		Color old=this.iconColor;
		this.iconColor = iconColor;
//		this.setBorder(BorderFactory.createLineBorder(iconColor));
		repaint();
		firePropertyChange(PROPERTY_ICON_COLOR, old, this.iconColor);
	}
	private class ColorIcon implements Icon
	{

		public ColorIcon()
		{
		}
		/* (non-Javadoc)
		 * @see javax.swing.Icon#getIconHeight()
		 */
		public int getIconHeight() {
			return 23-4;
		}

		/* (non-Javadoc)
		 * @see javax.swing.Icon#getIconWidth()
		 */
		public int getIconWidth() {
			return 23-4;
		}

		/* (non-Javadoc)
		 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
		 */
		public void paintIcon(Component c, Graphics g, int x, int y) {
			if(iconColor==null)
				return;
			
			Color oldColor=g.getColor();
			g.setColor(iconColor);
			g.fillRect(x+1, y+1, getIconWidth()-3, getIconHeight()-3);
			g.setColor(oldColor);
		}
		
	}
}
