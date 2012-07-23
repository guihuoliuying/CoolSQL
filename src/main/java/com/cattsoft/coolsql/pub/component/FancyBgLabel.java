/*
    Copyright (C) 2005  Eduardo Jodas Samper

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

	author e-mail: eduardj@dev.java.net
 */
/**
 * 
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.image.MemoryImageSource;

import javax.swing.JPanel;

import com.cattsoft.coolsql.system.lookandfeel.SystemLookAndFeel;

public class FancyBgLabel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Image image;
	private Color start = SystemLookAndFeel.getInstance().getViewThemeColor();
	private Color end = Color.WHITE;

    public FancyBgLabel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout,isDoubleBuffered);
        initComponent();
    }

    /**
     * Create a new buffered JPanel with the specified layout manager
     *
     * @param layout  the LayoutManager to use
     */
    public FancyBgLabel(LayoutManager layout) {
        super(layout);
        initComponent();
    }

    /**
     * Creates a new <code>JPanel</code> with <code>FlowLayout</code>
     * and the specified buffering strategy.
     * If <code>isDoubleBuffered</code> is true, the <code>JPanel</code>
     * will use a double buffer.
     *
     * @param isDoubleBuffered  a boolean, true for double-buffering, which
     *        uses additional memory space to achieve fast, flicker-free 
     *        updates
     */
    public FancyBgLabel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        initComponent();
    }

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public FancyBgLabel() {
        super();
        initComponent();
    }
	public Color getStartColor() {
		return start;
	}
	public void setStartColor(Color color) {
		start = color;
	}
	public Color getEndColor() {
		return end;
	}
	public void setEndColor(Color color) {
		end = color;
	}
	public void setColors(Color start, Color end) {
		this.start = start;
		this.end = end;
	}

	private void initComponent() {
		setOpaque(false);
	}

	protected void paintComponent(Graphics g) {
		if ((image == null || image.getWidth(null) != getWidth() || image
				.getHeight(null) != getHeight())
				&& getWidth() > 0 && getHeight() > 0) {
			int x = getWidth();
			int y = getHeight();
			final int[] pixels = new int[x * y];
			final int green = end.getGreen() - start.getGreen();
			final int blue = end.getBlue() - start.getBlue();
			final int red = end.getRed() - start.getRed();
			for (int iCnt = 0; iCnt < x; iCnt++) {
				float factor = (float) iCnt / (float) x;
				int color = (255 << 24)
						| ((start.getRed() + (int) (red * factor)) << 16)
						| ((start.getGreen() + (int) (green * factor)) << 8)
						| (start.getBlue() + (int) (blue * factor));
				// color = start.getBlue();
				for (int jCnt = 0; jCnt < y; jCnt++) {
					pixels[jCnt * x + iCnt] = color;
					// pixels[iCnt*jCnt] = color;
					// pixels[iCnt*jCnt] = (255 << 24) | (red << 16) | blue;
				}
			}
			image = createImage(new MemoryImageSource(x, y, pixels, 0, x));
		}
		if (image != null)
			g.drawImage(image, 0, 0, null);
		super.paintComponent(g);
	}
}