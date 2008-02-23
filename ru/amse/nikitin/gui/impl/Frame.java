package ru.amse.nikitin.gui.impl;

import java.awt.Color;
import java.awt.Graphics;

import ru.amse.nikitin.activeobj.IActiveObjectDesc;
import ru.amse.nikitin.gui.Const;
import ru.amse.nikitin.gui.IShape;

/** 
 * GUI frame shape
 * 
 * @author Pavel A. Nikitin
 *
 */
/* package-private */ class Frame implements IShape {
	IActiveObjectDesc d1;
	protected final Color color;
	protected final Color bkColor;
	
	protected static final int DX = Const.POINT_X_SIZE + 2;
	protected static final int DY = Const.POINT_Y_SIZE + 2;
	
	public Frame(IActiveObjectDesc d1, Color color, Color bkColor) {
		this.d1 = d1;
		this.color = color;
		this.bkColor = bkColor;
	}

	public void draw(Graphics g) {
		Color prevColor = g.getColor();
		g.setColor(color);
		g.drawRect(d1.getX() - 1, d1.getY() - 1, DX - 1, DY - 1);
		g.setColor(prevColor);
	}

	public void erase(Graphics g) {
		Color prevColor = g.getColor();
		g.setColor(bkColor);
		g.drawRect(d1.getX() - 1, d1.getY() - 1, DX - 1, DY - 1);
		g.setColor(prevColor);
	}
}
