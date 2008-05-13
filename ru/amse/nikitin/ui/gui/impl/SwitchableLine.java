package ru.amse.nikitin.ui.gui.impl;

import java.awt.Color;

import java.awt.Graphics;

import ru.amse.nikitin.simulator.IActiveObjectDesc;
import ru.amse.nikitin.ui.gui.IPropertyChangeListener;
import ru.amse.nikitin.ui.gui.ISettings;

/** display listener */ class SettingsListener implements IPropertyChangeListener {
	public void propertyChanged(String name, String newValue) {
		if (name.equals("Red arrows")) {
			SwitchableLine.showArrows = ISettings.PROP_ON.equals(newValue);
		}
	}
}

public class SwitchableLine extends Line {
	/* package-private */ static boolean showArrows = true;
	
	static {
		Settings.getInstance().addPropertyChangeListener(new SettingsListener());
	}

	public SwitchableLine(IActiveObjectDesc d1, IActiveObjectDesc d2, Color color, Color bkColor) {
		super(d1, d2, color, bkColor);
	}

	public void draw(Graphics g) {
		if (showArrows) {
			super.draw(g);
		}
	}
	
}
