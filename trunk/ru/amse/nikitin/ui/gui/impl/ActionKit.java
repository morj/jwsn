package ru.amse.nikitin.ui.gui.impl;

import java.awt.event.ActionEvent;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import ru.amse.nikitin.ui.gui.Const;
import ru.amse.nikitin.ui.gui.ISettings;

/** 
 * GUI actions Kit
 * 
 * @author Pavel A. Nikitin
 *
 */
public class ActionKit {

	/** run simulation */
	public static RunSimulationAction createRunSimulationAction
			(DisplayComponent component) {
		return new RunSimulationAction(component);
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
	
	/** run simulation for some time */
	public static LongRunSimulationAction createLongRunSimulationAction
			(DisplayComponent component, MessagesProgressBar m) {
		return new LongRunSimulationAction(component, m);
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
	protected TimeUnit units = TimeUnit.MILLISECONDS;
	protected boolean running = false; 

	/* package-private */ RunSimulationAction(DisplayComponent component) {
		// super("Run");
		putValue(SHORT_DESCRIPTION, "Run simulation");
		putValue(SMALL_ICON, Util.getInstance().createImageIcon("icon_run.png"));
		this.component = component;
	}

	public void actionPerformed(ActionEvent e) {
		if (running) {
			Settings.getInstance().setProperty("Running", ISettings.PROP_OFF);
			component.stopSimulation();
			putValue(SMALL_ICON, Util.getInstance().createImageIcon("icon_run.png"));
			running = false;
		} else {
			Settings.getInstance().setProperty("Running", ISettings.PROP_ON);
			if(!component.isRunning()) {
				String s = Settings.getInstance().getProperty("Speed");
				int rate = Const.MAX_RATE - Integer.parseInt(s) + 1;
				component.runSimulation(rate, units);
				putValue(SMALL_ICON, Util.getInstance().createImageIcon("icon_paused.png"));
				running = true;
			}
		}
	}
}

class StopSimulationAction extends AbstractAction {
	private static final long serialVersionUID = 239;
	protected DisplayComponent component;

	/* package-private */ StopSimulationAction(DisplayComponent component) {
		// super("Stop");
		putValue(SHORT_DESCRIPTION, "Stop simulation");
        putValue(SMALL_ICON, Util.getInstance().createImageIcon("icon_paused.png"));
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
        putValue(SMALL_ICON, Util.getInstance().createImageIcon("icon_step.png"));
		this.component = component;
	}
	
	public void actionPerformed(ActionEvent e) {
		component.stepSimulation();
	}
}

class LongRunSimulationAction extends AbstractAction {
	private static final long serialVersionUID = 239;
	protected DisplayComponent component;
	protected final TimeUnit units = TimeUnit.MILLISECONDS;
	protected MessagesProgressBar m;
	protected boolean running = false;

	/* package-private */ LongRunSimulationAction(DisplayComponent component,
			MessagesProgressBar m) {
		// super("Step");
		putValue(SHORT_DESCRIPTION, "Run simulation for given steps amount");
        putValue(SMALL_ICON, Util.getInstance().createImageIcon("icon_step_n.png"));
		this.component = component;
		this.m = m;
		m.setAction(this);
	}
	
	public synchronized void actionPerformed(ActionEvent e) {
		if (running) {
			component.stopSimulation();
			resetBtn();
		} else {
			if(!component.isRunning()) {
				try {
					int steps = Integer.parseInt(JOptionPane.showInputDialog(null,
						"Number of steps"));
					m.setMaximum(steps);
					m.init();
					String s = Settings.getInstance().getProperty("Speed");
					int rate = Const.MAX_RATE - Integer.parseInt(s) + 1;
					component.stepSimulation(m, rate, units);
					putValue(SMALL_ICON, Util.getInstance().createImageIcon("icon_stop.png"));
					running = true;
				} catch (NumberFormatException nfe) {
				}
			}
		}
	}
	
	/* package-private */ synchronized void resetBtn() {
		putValue(SMALL_ICON, Util.getInstance().createImageIcon("icon_step_n.png"));
		running = false;
	}
}
