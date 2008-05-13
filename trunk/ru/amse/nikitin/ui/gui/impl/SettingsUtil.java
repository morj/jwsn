package ru.amse.nikitin.ui.gui.impl;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ru.amse.nikitin.ui.gui.IAdjustableComponent;
import ru.amse.nikitin.ui.gui.IPropertyChangeListener;
import ru.amse.nikitin.ui.gui.ISettings;
import ru.amse.nikitin.ui.gui.ISettingsUtil;

public class SettingsUtil implements ISettingsUtil {
	private static SettingsUtil instance = null;
	private final ISettings settings;
	private final Map<String, IAdjustableComponent> components = new HashMap<String, IAdjustableComponent>();
	private final Map<IAdjustableComponent, String> properties = new HashMap<IAdjustableComponent, String>();
	
	class MyCheckBox implements IAdjustableComponent {
		private final JCheckBox box;
		public MyCheckBox(JCheckBox box) {
			super();
			this.box = box;
		}
		public String itemStateChanged() {
			if (box.isSelected()) {
				return ISettings.PROP_ON;
			} else {
				return ISettings.PROP_OFF;
			}
		}
		public void propertyChanged(String newValue) {
			box.setSelected(newValue.equals(ISettings.PROP_ON));
		}
		public JComponent getComponent() {
			return box;
		}
		public void addChangeListener(ChangeListener l) {
			box.addChangeListener(l);
		}
	}
	
	class MySlider implements IAdjustableComponent {
		private final JSlider slider;
		public MySlider(JSlider slider) {
			super();
			this.slider = slider;
		}
		public JComponent getComponent() {
			return slider;
		}
		public String itemStateChanged() {
			return Integer.toString(slider.getValue());
		}
		public void propertyChanged(String newValue) {
			int state = Integer.parseInt(newValue);
			if((slider.getMinimum() <= state) && (state <= slider.getMaximum())) {
				slider.setValue(state);
			}
		}
		public void addChangeListener(ChangeListener l) {
			slider.addChangeListener(l);
		}
	}
	
	private IPropertyChangeListener settingsListener = new IPropertyChangeListener() {
		public void propertyChanged(String name, String newValue) {
			for(String title: components.keySet()) {
				if(title.equals(name)) {
					components.get(title).propertyChanged(newValue);
				}
			}
		}
	};
	
	private ChangeListener componentsListener = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			Object source = e.getSource();
			if (source instanceof JSlider) if(((JSlider)source).getValueIsAdjusting()) {
				return; // ignore slider middle-states
			}
			for(IAdjustableComponent component: properties.keySet()) {
				if(source == component.getComponent()) {
					settings.setProperty(properties.get(component), component.itemStateChanged());
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
		if (state) {
			settings.setProperty(title, ISettings.PROP_ON);
		} else {
			settings.setProperty(title, ISettings.PROP_OFF);
		}
		addAdjustableComponent(title, new MyCheckBox(box));
		return box;
	}

	public JCheckBox getCheckBox(String title) {
		JCheckBox box = new JCheckBox(
				title, ISettings.PROP_ON.equals(settings.getProperty(title))
		);
		box.setEnabled(false);
		addAdjustableComponent(title, new MyCheckBox(box));
		return box;
	}
	
	public JSlider getSlider (String title, int min, int max, int value) {
		JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);
		slider.setName(title);
		settings.setProperty(title, Integer.toString(value));
		addAdjustableComponent(title, new MySlider(slider));
		return slider;
	}

	public void addAdjustableComponent(String title, IAdjustableComponent component) {
		// settingsListener is added in SettingsUtil constructor
		component.addChangeListener(componentsListener);
		components.put(title, component);
		properties.put(component, title);
	}

}
