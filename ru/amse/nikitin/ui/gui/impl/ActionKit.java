package ru.amse.nikitin.ui.gui.impl;

import java.awt.event.ActionEvent;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

/** 
 * GUI actions Kit
 * 
 * @author Pavel A. Nikitin
 *
 */
public class ActionKit {

	/** run simulation */
	public static RunSimulationAction createRunSimulationAction
			(DisplayComponent component, long rate, TimeUnit units) {
		return new RunSimulationAction(component, rate, units);
	}
	
	/** stop simulation */
	public static StopSimulationAction createStopSimulationAction
			(DisplayComponent component) {
		return new StopSimulationAction(component);
	}
	
	/** step simulation */
	public static StepSimulationAction createStepSimulationAction
			(DisplayComponent component) {
		return new StepSimulationAction(component);
	}
}

/** 
 * GUI action
 * 
 * @author Pavel A. Nikitin
 *
 */
class RunSimulationAction extends AbstractAction {
	private static final long serialVersionUID = 239;
	protected DisplayComponent component;
	protected long rate;
	protected TimeUnit units;
	protected boolean running = false; 

	/* package-private */ RunSimulationAction(DisplayComponent component,
			long rate, TimeUnit units) {
		// super("Run");
		putValue(SHORT_DESCRIPTION, "Run simulation");
        putValue(SMALL_ICON, new ImageIcon("icons\\icon_run.png"));
		this.component = component;
		this.rate = rate;
		this.units = units;
	}

	public void actionPerformed(ActionEvent e) {
		if (running) {
			component.stopSimulation();
			putValue(SMALL_ICON, new ImageIcon("icons\\icon_run.png"));
		} else {
			component.runSimulation(rate, units);
			putValue(SMALL_ICON, new ImageIcon("icons\\icon_paused.png"));
		}
		running = !running;
	}
}

class StopSimulationAction extends AbstractAction {
	private static final long serialVersionUID = 239;
	protected DisplayComponent component;

	/* package-private */ StopSimulationAction(DisplayComponent component) {
		// super("Stop");
		putValue(SHORT_DESCRIPTION, "Stop simulation");
        putValue(SMALL_ICON, new ImageIcon("icons\\icon_paused.png"));
		this.component = component;
	}
	
	public void actionPerformed(ActionEvent e) {
		component.stopSimulation();
	}
}

class StepSimulationAction extends AbstractAction {
	private static final long serialVersionUID = 239;
	protected DisplayComponent component;

	/* package-private */ StepSimulationAction(DisplayComponent component) {
		// super("Step");
		putValue(SHORT_DESCRIPTION, "Step simulation");
        putValue(SMALL_ICON, new ImageIcon("icons\\icon_step.png"));
		this.component = component;
	}
	
	public void actionPerformed(ActionEvent e) {
		component.stepSimulation();
	}
}
