package ru.amse.nikitin.ui.gui;

import javax.swing.JCheckBox;

public interface ISettingsUtil {
	public JCheckBox getCheckBox(String title);
	public JCheckBox getCheckBox(String title, boolean state);
}
