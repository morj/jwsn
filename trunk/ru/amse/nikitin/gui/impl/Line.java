package ru.amse.nikitin.gui.impl;

import java.awt.Color;
import java.awt.Graphics;

import ru.amse.nikitin.gui.Const;
import ru.amse.nikitin.gui.IShape;
import ru.amse.nikitin.activeobj.IActiveObjectDesc;

/** 
 * GUI line shape
 * 
 * @author Pavel A. Nikitin
 *
 */
/* package-private */ class Line implements IShape {
	IActiveObjectDesc d1;
	IActiveObjectDesc d2;
	protected final Color color;
	protected final Color bkColor;
	
	protected static final int DX = Const.POINT_X_SIZE / 2;
	protected static final int DY = Const.POINT_Y_SIZE / 2;
	
	
	public Line(IActiveObjectDesc d1, IActiveObjectDesc d2, Color color, Color bkColor) {
		this.d1 = d1;
		this.d2 = d2;
		this.color = color;
		this.bkColor = bkColor;
	}
	
	protected void paint(Graphics g) {
		int dx = (d2.getX() - d1.getX());
		int dy = (d2.getY() - d1.getY());
		double len = Math.sqrt((double)(dx * dx + dy * dy));
		dx = (int)(Const.POINT_DIAMETER * dx / len);
		dy = (int)(Const.POINT_DIAMETER * dy / len);
		int x = d2.getX() - dx + DX;
		int y = d2.getY() - dy + DY;
		g.drawLine(d1.getX() + dx + DX, d1.getY() + dy + DY, x, y);
		g.drawLine(
			x, y,
			d2.getX() - 2*dx + (int)(Const.ARROW_SCALE_A * dy) + DX,
			d2.getY() - 2*dy - (int)(Const.ARROW_SCALE_B * dx) + DY
		);
		g.drawLine(
			x, y,
			d2.getX() - 2*dx - (int)(Const.ARROW_SCALE_A * dy) + DX,
			d2.getY() - 2*dy + (int)(Const.ARROW_SCALE_B * dx) + DY
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
