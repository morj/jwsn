package ru.amse.nikitin.ui.gui.impl;

import java.awt.Color;
import java.awt.Graphics;

import ru.amse.nikitin.simulator.IActiveObjectDesc;
import ru.amse.nikitin.ui.gui.IPropertyChangeListener;
import ru.amse.nikitin.ui.gui.ISettings;

public class SwitchableLine extends Line {
	protected boolean showArrows = true;
	
	/** display listener */ class SettingsListener implements IPropertyChangeListener {
		public void propertyChanged(String name, String newValue) {
			if (name.equals("Red arrows")) {
				showArrows = ISettings.PROP_ON.equals(newValue);
			}
		}
	}

	public SwitchableLine(IActiveObjectDesc d1, IActiveObjectDesc d2, Color color, Color bkColor) {
		super(d1, d2, color, bkColor);
		Settings.getInstance().addPropertyChangeListener(new SettingsListener());
	}

	public void draw(Graphics g) {
		if (showArrows) {
			super.draw(g);
		}
	}
	
}
