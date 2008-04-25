package ru.amse.nikitin.ui.gui.impl;

import javax.swing.JComponent;
import javax.swing.JProgressBar;

public class MessagesProgressBar extends JProgressBar {
	private static final long serialVersionUID = 239;
	protected JComponent container;
	
	public MessagesProgressBar(JComponent container) {
		super();
		this.container = container;
	}
	
	public void init() {
		setMinimum(0);
		container.add(this);
		container.revalidate();
	}
	
	public void finish() {
		container.remove(this);
		container.repaint();
	}
	
}
