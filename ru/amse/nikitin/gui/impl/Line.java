package ru.amse.nikitin.gui.impl;

import java.awt.Color;
import java.awt.Graphics;

import ru.amse.nikitin.gui.Const;
import ru.amse.nikitin.gui.IShape;

/** 
 * GUI line shape
 * 
 * @author Pavel A. Nikitin
 *
 */
/* package-private */ class Line implements IShape {
	protected final int x1;
	protected final int x2;
	protected final int y1;
	protected final int y2;
	protected final Color color;
	protected final Color bkColor;
	
	protected static final int dx = Const.pointXSize / 2;
	protected static final int dy = Const.pointYSize / 2;
	
	
	public Line(int x1, int y1, int x2, int y2, Color color, Color bkColor) {
		this.x1 = x1 + dx;
		this.x2 = x2 + dx;
		this.y1 = y1 + dy;
		this.y2 = y2 + dy;
		this.color = color;
		this.bkColor = bkColor;
	}
	
	protected void paint(Graphics g) {
		int dx = (x2 - x1);
		int dy = (y2 - y1);
		double len = Math.sqrt((double)(dx * dx + dy * dy));
		dx = (int)(Const.pointDiameter * dx / len);
		dy = (int)(Const.pointDiameter * dy / len);
		int x = x2 - dx;
		int y = y2 - dy;
		g.drawLine(x1 + dx, y1 + dy, x, y);
		g.drawLine(
			x, y,
			x2 - 2*dx + (int)(Const.arrowScaleA * dy),
			y2 - 2*dy - (int)(Const.arrowScaleB * dx)
		);
		g.drawLine(
			x, y,
			x2 - 2*dx - (int)(Const.arrowScaleA * dy),
			y2 - 2*dy + (int)(Const.arrowScaleB * dx)
		);
	}
	
	public void draw(Graphics g) {
		Color prevColor = g.getColor();
		g.setColor(color);
		paint(g);
		g.setColor(prevColor);
	}

	public void erase(Graphics g) {
		Color prevColor = g.getColor();
		g.setColor(bkColor);
		paint(g);
		g.setColor(prevColor);
	}
}
