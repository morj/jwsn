package ru.amse.nikitin.ui.gui;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

public interface IAdjustableComponent {
	public String itemStateChanged();
	public void propertyChanged(String newValue);
	public JComponent getComponent();
	public void addChangeListener(ChangeListener l);
}
