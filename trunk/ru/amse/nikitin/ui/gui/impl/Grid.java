package ru.amse.nikitin.ui.gui.impl;

import java.awt.Color;
import java.awt.Graphics;

import ru.amse.nikitin.ui.gui.IPropertyChangeListener;
import ru.amse.nikitin.ui.gui.ISettings;
import ru.amse.nikitin.ui.gui.IShape;

public class Grid implements IShape {
	/* package-private */ static boolean showGrid = false;
	public static final int step = 10;
	private final int h;
	private final int w;
	protected final Color color;
	protected final Color bkColor;
	
	/** display listener */ static class SettingsListener implements IPropertyChangeListener {
		public void propertyChanged(String name, String newValue) {
			if (name.equals("Grid")) {
				showGrid = ISettings.PROP_ON.equals(newValue);
			}
		}
	}
	
	static {
		Settings.getInstance().addPropertyChangeListener(new SettingsListener());
	}
	
	public Grid(int h, int w, Color color, Color bkColor) {
		this.h = w;
		this.w = h;
		this.color = color;
		this.bkColor = bkColor;
	}

	public void draw(Graphics g) {
		if (showGrid) {
			Color prevColor = g.getColor();
			g.setColor(color);
			int x = 0;
			while(x < w) {
				g.drawLine(x, 0, x, h);
				x += step;
			}
			int y = 0;
			while(y < h) {
				g.drawLine(0, y, w, y);
				y += step;
			}
			g.setColor(prevColor);
		}
	}
	
	public void erase(Graphics g) {
		/* if (showGrid) {
			Color prevColor = g.getColor();
			g.setColor(bkColor);
			g.fillRect(0, 0, w, h);
			g.setColor(prevColor);
		} */
	}
	
}
