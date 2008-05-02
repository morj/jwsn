package ru.amse.nikitin.ui.gui.impl;

import javax.swing.JComponent;
import javax.swing.JProgressBar;

public class MessagesProgressBar extends JProgressBar {
	private static final long serialVersionUID = 239;
	protected JComponent container;
	protected boolean added = false;
	protected LongRunSimulationAction a = null;
	
	public MessagesProgressBar(JComponent container) {
		super();
		this.container = container;
	}
	
	public void init() {
		if(!added) {
			container.add(this);
			added = true;
			container.revalidate();
		}
		setMinimum(0);
	}
	
	public void finish() {
		if (a != null) {
			a.resetBtn();
		}
		// container.remove(this);
		setValue(0);
		container.repaint();
	}
	
	/* package-private */ void setAction(LongRunSimulationAction a) {
		this.a = a;
	}
	
}
