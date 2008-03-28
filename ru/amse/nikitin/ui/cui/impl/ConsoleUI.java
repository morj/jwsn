package ru.amse.nikitin.ui.cui.impl;

import java.util.concurrent.*;

import ru.amse.nikitin.simulator.IActiveObjectDesc;
import ru.amse.nikitin.simulator.IDispatcher;
import ru.amse.nikitin.simulator.IDisplayListener;
import ru.amse.nikitin.ui.cui.IConsoleUI;

/**
 * @author Pavel A. Nikitin
 * console UI implementation
 *
 */
public class ConsoleUI implements IConsoleUI {
	/** hidden internal UI listener */
	protected final DisplayListener displayListener = new DisplayListener();
	/** dispatcher the class works with */
	protected final IDispatcher dispatcher;
	
	/** statistics */
	protected int currSteps = 1;
	/** statistics */
	protected int perfSteps = 0;
	/** statistics */
	protected int notifySteps = 1;
	
	/** if the simulation just started (calls init) */
	protected boolean firstRun = true;
	/** if the simulation is already running (calls step) */
	protected boolean isRunning = false;
	
	/** concurrency blocking */
	protected Semaphore sema = new Semaphore(0);
	
	/** a runnable, performing steps */
	class SimulationRunnable implements Runnable {
		protected int count;
		public SimulationRunnable(int count) {
			this.count = count;
		}
		public void run() {
			int i = 0;
			while ((i < count) && sema.tryAcquire()) {
				step();
				sema.release();
				i++;
			}
			isRunning = false;
			System.out.println("performing steps stopped");
		}
	}

	/** display listener */
	class DisplayListener implements IDisplayListener {

		public DisplayListener() {
		}

		public void objectAdded(int id, IActiveObjectDesc desc) {
		}

		public void descChanged(int id) {
		}
		
		public void messageConflicted(int source, int dest) {
			
		}

		public void messageRecieved(int source, int dest, Object data) {
		}

		public void messageAccepted(int dest) {
		}

		public void notificationArrived(int id, String notification) {
		}

		public void stepStarted() {
		}

		public void stepPerformed() {
			perfSteps++;
			if (perfSteps % notifySteps == 0) {
				int percent = perfSteps * 100 / currSteps;
				System.out.println(percent + "% completed");
			}
		}

	}

	/** constructor */
	public ConsoleUI(IDispatcher d) {
		dispatcher = d;
		d.addDisplayListener(displayListener);
	}
	
	/** run action */
	public synchronized void runSimulation(int steps) {
		if (!isRunning) {
			sema.release();
			perfSteps = 0;
			currSteps = steps;
			notifySteps = steps / 20;
			isRunning = true;
			new Thread(new SimulationRunnable(steps)).start();
		} else {
			System.out.println("damn");
		}
	}
	
	/** stop action */
	public synchronized void stopSimulation() {
		if (isRunning) {
			System.out.println("stopping...");
			try {
				sema.acquire();
				System.out.println("cleared");
			} catch (InterruptedException ie) {
			}
		}
	}

	/** step action */
	protected synchronized void step() {
		if (firstRun) {
			dispatcher.init();
			firstRun = false;
		} else {
			dispatcher.step();
		}
	}
}
