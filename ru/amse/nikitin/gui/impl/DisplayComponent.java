package ru.amse.nikitin.gui.impl;

import java.util.*;
import java.util.concurrent.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

// import javax.swing.JToolTip;
import javax.swing.JTextArea;
import javax.swing.JComponent;

import ru.amse.nikitin.activeobj.IDispatcher;
import ru.amse.nikitin.activeobj.ILoggerListener;
import ru.amse.nikitin.activeobj.IDisplayListener;
import ru.amse.nikitin.activeobj.IActiveObjectDesc;
import ru.amse.nikitin.activeobj.impl.Logger;
import ru.amse.nikitin.gui.Const;
import ru.amse.nikitin.gui.IShape;

/**
 * @author Pavel A. Nikitin
 * GUI implementation
 *
 */
public class DisplayComponent extends JComponent {
	private static final long serialVersionUID = 239;
	protected final IDispatcher dispatcher;
	protected final DisplayListener displayListener = new DisplayListener();
	protected final Map<Integer, IActiveObjectDesc> descriptions
		= new HashMap<Integer, IActiveObjectDesc>();
	protected final BlockingQueue<IShape> shapes = new ArrayBlockingQueue<IShape>(10000);
	protected ScheduledExecutorService scheduler;
	protected final JTextArea logOutput;
	
	protected boolean firstRun = true;
	protected boolean isRunning = false;
	
	protected final Color bkColor = new Color (235, 235, 235); 
	
	/** a runnable, performing steps */
	class SimulationRunnable implements Runnable {
		public void run() {
			step();
		}
	}
	
	/** display listener */
	class DisplayListener implements IDisplayListener, ILoggerListener {
	
		public DisplayListener() {
		}
	
		public void objectAdded(int id, IActiveObjectDesc desc) {
			descriptions.put(id, desc);
			repaint();
		}
	
		public void descChanged(int id) {
			// TODO Auto-generated method stub
			
		}
		
		public void messageConflicted(int source, int dest) {
			IActiveObjectDesc d1 = descriptions.get(source);
			IActiveObjectDesc d2 = descriptions.get(dest);
			
			try {
				shapes.put(new Line(
					d1.getX(), d1.getY(),
					d2.getX(), d2.getY(),
					Color.RED,
					bkColor
				));
			} catch (InterruptedException ie) {
			}
		}
	
		public void messageRecieved(int source, int dest, int[] data) {
			StringBuilder b = new StringBuilder();
			for (int i = 0; i < data.length; i++) {
				b.append(data[i]);
				b.append(' ');
			}
			if (source != -1) {
				IActiveObjectDesc d1 = descriptions.get(source);
				IActiveObjectDesc d2 = descriptions.get(dest);
				try {
					shapes.put(new Line(
						d1.getX(), d1.getY(),
						d2.getX(), d2.getY(),
						Color.GREEN,
						bkColor
					));
				} catch (InterruptedException ie) {
				}
			}
		}
	
		public void messageAccepted(int dest) {
			// logMessage("accepted\n");
			IActiveObjectDesc d = descriptions.get(dest);
			try {
				shapes.put(new Frame(
					d.getX(), d.getY(),
					Color.BLUE, bkColor
				));
			} catch (InterruptedException ie) {
			}
		}
	
		public void notificationArrived(int id, String notification) {
			//	TODO Auto-generated method stub
			
		}
		
		public void stringLogged(String s) {
			logMessage(s);
		}
		
		public void stepStarted() {
			Graphics g = getGraphics();
			
			for (IShape s: shapes) {
				s.erase(g);
			}
			shapes.clear();
		}
		
		public void stepPerformed() {
			Graphics g = getGraphics();
			
			for (IShape s: shapes) {
				s.draw(g);
			}
			
			paintMots(g);
		}
	}
	
	/** icons painter */
	protected void paintMots (Graphics graphics) {
		Color prevColor = graphics.getColor();
		graphics.setColor(Color.BLACK);
		for (Map.Entry<Integer, IActiveObjectDesc> p: descriptions.entrySet()) {
			IActiveObjectDesc d = p.getValue();
			d.getImage().paintIcon(this, graphics, p.getValue().getX(), p.getValue().getY());
			graphics.drawString(
				d.getName(),
				d.getX() + Const.pointXSize, d.getY() + Const.pointYSize
			);
		}
		graphics.setColor(prevColor);
	}
	
	/** component painter */
	protected void paintComponent (Graphics graphics) {
		Color prevColor = graphics.getColor();
		graphics.setColor(bkColor);
		Rectangle r = graphics.getClipBounds();
		graphics.fillRect(0, 0, r.width, r.height);
		graphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1); // frame
		for (IShape s: shapes) {
			s.draw(graphics);
		}
		paintMots(graphics);
		graphics.setColor(prevColor);
	}
	
	/** logging method */
	protected void logMessage(String msg) {
		logOutput.append(msg + "\n");
	}
	
	/** simulation step */
	protected synchronized void step() {
		if (firstRun) {
			dispatcher.init();
			firstRun = false;
		} else {
			dispatcher.step();
		}
	}
	
	/** constructor */
	public DisplayComponent(IDispatcher d,  JTextArea logOutput) {
		dispatcher = d;
		this.logOutput = logOutput;
		Logger.getInstance().addListener(displayListener);
		d.addDisplayListener(displayListener);
	}
	
	/* public JToolTip createToolTip() {
		JToolTip j = new JToolTip();
		j.setToolTipText("sdfsdf");
		return j;
	} */
	
	/* package-private */ synchronized void runSimulation(long rate, TimeUnit u) {
		if (!isRunning) {
			isRunning = true;
			scheduler = Executors.newSingleThreadScheduledExecutor();
			scheduler.scheduleWithFixedDelay(new SimulationRunnable(),
				0, rate, u);
		}
	}
	
	/* package-private */ synchronized void stopSimulation() {
		if (isRunning) {
			scheduler.shutdown();
			isRunning = false;
		}
	}
	
	/* package-private */ synchronized void stepSimulation() {
		if (!isRunning) {
			isRunning = true;
			new Thread(new SimulationRunnable(), "Simulation Step").start();
			isRunning = false;
		}
	}
}
