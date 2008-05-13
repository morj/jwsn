package ru.amse.nikitin.ui.gui;

import javax.swing.JCheckBox;
import javax.swing.JSlider;

public interface ISettingsUtil {
	JCheckBox getCheckBox(String title);
	JCheckBox getCheckBox(String title, boolean state);
	JSlider getSlider(String title, int min, int max, int value);
	void addAdjustableComponent(String title, IAdjustableComponent component);
}
