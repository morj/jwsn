package ru.amse.nikitin.ui.gui;

public interface ISettings {
	public static final String PROP_ON = "y";
	public static final String PROP_OFF = "n";
	void setProperty(String name, String value);
	String getProperty(String name);
	void addPropertyChangeListener(IPropertyChangeListener listener);
}
