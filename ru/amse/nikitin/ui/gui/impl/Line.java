package ru.amse.nikitin.ui.gui.impl;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;

import ru.amse.nikitin.simulator.IActiveObjectDesc;
import ru.amse.nikitin.ui.gui.Const;
import ru.amse.nikitin.ui.gui.IShape;

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

	public Color getColor() {
		return color;
	}
	
	public boolean contains(Point p) {
		/* double pX = p.x - d1.getX();
		double pY = p.y - d1.getY();
		double x = d2.getX() - d1.getX();
		double y = d2.getY() - d1.getY();
		
		// if ((pY == 0) || (y == 0)) return false;
		
		System.err.println(x * pY - y * pX);
		if ((x * pX >= 0) && (y * pX >= 0) && (Math.abs(x * pY - y * pX) < 0.1)) {
			return true; 
		} */
		
		return false;
	}
}
