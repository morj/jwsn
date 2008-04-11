package ru.amse.nikitin.ui.gui.impl;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
// import java.awt.Rectangle;
// import java.awt.geom.Rectangle2D;
import java.awt.font.*;

import ru.amse.nikitin.ui.gui.IShape;

public class Notification implements IShape {
	protected final Color color;
	protected final Color bkColor;
	protected final int x;
	protected final int y;
	protected final String text;
	protected TextLayout tl; 

	public Notification(int x, int y, String text, Color color, Color bkColor) {
		this.x = x;
		this.y = y;
		this.text = text;
		this.color = color;
		this.bkColor = bkColor;
	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		FontRenderContext frc = g2.getFontRenderContext();
		Font f = new Font("Helvetica", Font.PLAIN, 12);
		tl = new TextLayout(text, f, frc);
		Color prevColor = g.getColor();
		// Rectangle2D rect = tl.getBounds(); g2.draw(rect);
		g.setColor(color);
		tl.draw(g2, x, y);
		g.setColor(prevColor);
	}

	public void erase(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		Color prevColor = g.getColor();
		g.setColor(bkColor);
		tl.draw(g2, x, y);
		g.setColor(prevColor);
	}

}
