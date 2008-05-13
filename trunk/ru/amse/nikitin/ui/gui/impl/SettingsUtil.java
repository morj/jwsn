package ru.amse.nikitin.ui.gui.impl;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;

import ru.amse.nikitin.ui.gui.IPropertyChangeListener;
import ru.amse.nikitin.ui.gui.ISettings;
import ru.amse.nikitin.ui.gui.ISettingsUtil;

public class SettingsUtil implements ISettingsUtil {
	private static SettingsUtil instance = null;
	private final ISettings settings;
	private final Map<String, JCheckBox> boxes = new HashMap<String, JCheckBox>();
	private final Map<JCheckBox, String> props = new HashMap<JCheckBox, String>();
	
	private IPropertyChangeListener settingsListener = new IPropertyChangeListener() {
		public void propertyChanged(String name, String newValue) {
			for(String title: boxes.keySet()) {
				if(title.equals(name)) {
					JCheckBox box = boxes.get(title);
					box.setSelected(newValue.equals(ISettings.PROP_ON));
				}
			}
		}
	};
	
	private ItemListener boxesListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			Object source = e.getItemSelectable();
			for(JCheckBox box: props.keySet()) {
				if(source == box) {
					String title = props.get(box);
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						settings.setProperty(title, ISettings.PROP_OFF);
					} else {
						settings.setProperty(title, ISettings.PROP_ON);
					}
				}
			}
		}
	};
	
	private SettingsUtil() {
		settings = Settings.getInstance();
		settings.addPropertyChangeListener(settingsListener);
	}
	
	public static ISettingsUtil getInstance() {
		if (instance == null) {
			instance = new SettingsUtil();
		}
		return instance;
	}

	public JCheckBox getCheckBox(String title, boolean state) {
		JCheckBox box = new JCheckBox(title, state);
		box.addItemListener(boxesListener);
		// settingsListener is added in constructor
		if (state) {
			settings.setProperty(title, ISettings.PROP_ON);
		} else {
			settings.setProperty(title, ISettings.PROP_OFF);
		}
		boxes.put(title, box);
		props.put(box, title);
		return box;
	}

	public JCheckBox getCheckBox(String title) {
		JCheckBox box = new JCheckBox(
				title, ISettings.PROP_ON.equals(settings.getProperty(title))
		);
		box.setEnabled(false);
		box.addItemListener(boxesListener);
		// settingsListener is added in constructor
		boxes.put(title, box);
		props.put(box, title);
		return box;
	}
	
	
}
