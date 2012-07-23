/**
 * 
 */
package com.cattsoft.coolsql.pub.display;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

import com.cattsoft.coolsql.pub.component.DisplayPanel;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;

/**
 * this class is a border that designed for table cell,and used to display the
 * border of one row of table
 * 
 * @author ��Т��
 * 
 * 2008-1-9 create
 */
public class TableCellHighlightBorder extends AbstractBorder {
	private static final long serialVersionUID = 1L;
	private final static int CELL_TYPE_LEFT = -1; // top left cell
	private final static int CELL_TYPE_CENTER = 0; // center cell
	private final static int CELL_TYPE_RIGHT = 1; // right cell

	private static TableCellHighlightBorder leftCellBorder = null;
	private static TableCellHighlightBorder centerCellBorder = null;
	private static TableCellHighlightBorder rightCellBorder = null;

	private int cellType;
	private Color borderColor;
	public static TableCellHighlightBorder createLeftCellBorder() {
		if (leftCellBorder == null)
			leftCellBorder = new TableCellHighlightBorder(
					CELL_TYPE_LEFT,
					Setting
							.getInstance()
							.getColorProperty(
									PropertyConstant.PROPERTY_VIEW_PUBLIC_TABLEROWBORDER_COLOR,
									Color.blue));
		return leftCellBorder;
	}
	public static TableCellHighlightBorder createCenterCellBorder() {
		if (centerCellBorder == null)
			centerCellBorder = new TableCellHighlightBorder(
					CELL_TYPE_CENTER,
					Setting
							.getInstance()
							.getColorProperty(
									PropertyConstant.PROPERTY_VIEW_PUBLIC_TABLEROWBORDER_COLOR,
									Color.blue));
		return centerCellBorder;
	}
	public static TableCellHighlightBorder createRightCellBorder() {
		if (rightCellBorder == null)
			rightCellBorder = new TableCellHighlightBorder(
					CELL_TYPE_RIGHT,
					Setting
							.getInstance()
							.getColorProperty(
									PropertyConstant.PROPERTY_VIEW_PUBLIC_TABLEROWBORDER_COLOR,
									Color.blue));
		return rightCellBorder;
	}
	public TableCellHighlightBorder(int cellType, Color borderColor) {
		if (cellType < -1 || cellType > 1)
			throw new IllegalArgumentException(
					"celltype argument value is not valid,error type:"
							+ cellType);
		this.cellType = cellType;
		if (borderColor == null)
			this.borderColor = DisplayPanel.getThemeColor();
		else
			this.borderColor = borderColor;
	}
	/**
	 * draw border for component:c
	 */
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		g.setColor(borderColor);
		g.drawLine(x, y, width - 1, 0);
		g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
		// g.setColor(type==RAISE?Color.gray:Color.white);
		if (cellType == CELL_TYPE_LEFT)
			g.drawLine(x, y, x, y + height - 1);
		if (cellType == CELL_TYPE_RIGHT)
			g.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
	}
	/**
	 * Returns the insets of the border.
	 * 
	 * @param c
	 *            the component for which this border insets value applies
	 */
	public Insets getBorderInsets(Component c) {
		return new Insets(1, 1, 1, 1);
	}

	/**
	 * Reinitialize the insets parameter with this Border's current Insets.
	 * 
	 * @param c
	 *            the component for which this border insets value applies
	 * @param insets
	 *            the object to be reinitialized
	 */
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.left = insets.top = insets.right = insets.bottom = 1;
		return insets;
	}

}
