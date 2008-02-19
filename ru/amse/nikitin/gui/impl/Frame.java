package ru.amse.nikitin.gui.impl;

import java.awt.Color;
import java.awt.Graphics;

import ru.amse.nikitin.gui.Const;
import ru.amse.nikitin.gui.IShape;

/** 
 * GUI frame shape
 * 
 * @author Pavel A. Nikitin
 *
 */
/* package-private */ class Frame implements IShape {
	protected final int x;
	protected final int y;
	protected final Color color;
	protected final Color bkColor;
	
	protected static final int dx = Const.pointXSize + 2;
	protected static final int dy = Const.pointYSize + 2;
	
	public Frame(int x, int y, Color color, Color bkColor) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.bkColor = bkColor;
	}

	public void draw(Graphics g) {
		Color prevColor = g.getColor();
		g.setColor(color);
		g.drawRect(x - 1, y - 1, dx - 1, dy - 1);
		g.setColor(prevColor);
	}

	public void erase(Graphics g) {
		Color prevColor = g.getColor();
		g.setColor(bkColor);
		g.drawRect(x - 1, y - 1, dx - 1, dy - 1);
		g.setColor(prevColor);
	}
}
