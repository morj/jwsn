package ru.amse.nikitin.ui.gui;

public interface ISettings {
	void setProperty(String name, String value);
	String getProperty(String name);
	void addPropertyChangeListener(IPropertyChangeListener listener);
}
