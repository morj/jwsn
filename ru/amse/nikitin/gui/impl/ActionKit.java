package ru.amse.nikitin.gui.impl;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import java.util.concurrent.TimeUnit;

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

	/* package-private */ RunSimulationAction(DisplayComponent component,
			long rate, TimeUnit units) {
		// super("Run");
		putValue(SHORT_DESCRIPTION, "Run simulation");
        putValue(SMALL_ICON, new ImageIcon("icons\\run.png"));
		this.component = component;
		this.rate = rate;
		this.units = units;
	}

	public void actionPerformed(ActionEvent e) {
		component.runSimulation(rate, units);
	}
}

class StopSimulationAction extends AbstractAction {
	private static final long serialVersionUID = 239;
	protected DisplayComponent component;

	/* package-private */ StopSimulationAction(DisplayComponent component) {
		// super("Stop");
		putValue(SHORT_DESCRIPTION, "Stop simulation");
        putValue(SMALL_ICON, new ImageIcon("icons\\stop.png"));
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
        putValue(SMALL_ICON, new ImageIcon("icons\\step.png"));
		this.component = component;
	}
	
	public void actionPerformed(ActionEvent e) {
		component.stepSimulation();
	}
}
