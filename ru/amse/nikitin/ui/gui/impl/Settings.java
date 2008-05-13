package ru.amse.nikitin.ui.gui.impl;

import ru.amse.nikitin.ui.gui.IPropertyChangeListener;
import ru.amse.nikitin.ui.gui.ISettings;

// import java.awt.TextArea;
// import javax.swing.JFrame;

import java.util.*;

public class Settings implements ISettings {
	private static Settings instance = null;
	
	private final Map<String, String> params = new HashMap<String, String>();
	private final List<IPropertyChangeListener> listeners
		= new LinkedList<IPropertyChangeListener>();
	
	// private final JFrame mainFrame;
	// private final static String RETURN_ACTION = "return-done";
	
	private Settings() {
		// mainFrame = new JFrame ("Settings");
		// mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	public static ISettings getInstance() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}

	public void setProperty(String name, String value) {
		/* if(!params.containsKey(value)) {
			TextArea input = new TextArea();
			mainFrame.add(input);
		} */
		String oldValue = params.get(name);
		if(!value.equals(oldValue)) {
			params.put(name, value);
			for(IPropertyChangeListener l: listeners) {
				l.propertyChanged(name, value);
			}
		}
	}

	public String getProperty(String name) {
		return params.get(name);
	}

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		listeners.add(listener);
	}

}
