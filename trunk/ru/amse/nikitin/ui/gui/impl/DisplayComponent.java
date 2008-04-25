package ru.amse.nikitin.ui.gui.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.event.MouseInputListener;

import ru.amse.nikitin.simulator.IActiveObjectDesc;
import ru.amse.nikitin.simulator.IDispatcher;
import ru.amse.nikitin.simulator.IDisplayListener;
import ru.amse.nikitin.simulator.ILoggerListener;
import ru.amse.nikitin.simulator.impl.Logger;
import ru.amse.nikitin.ui.gui.Const;
import ru.amse.nikitin.ui.gui.IDisplayComponent;
import ru.amse.nikitin.ui.gui.IShape;
import ru.amse.nikitin.ui.gui.ITool;

class ToolTip {
    Rectangle rect;
    String text;
    ToolTip(Rectangle r, String t) {
        rect = r;
        text = t;
     }
 }

/**
 * @author Pavel A. Nikitin
 * GUI implementation
 *
 */
public class DisplayComponent extends JComponent implements IDisplayComponent {
	private static final long serialVersionUID = 239;
	protected final IDispatcher dispatcher;
	protected final DisplayListener displayListener = new DisplayListener();
	protected final Map<Integer, IActiveObjectDesc> descriptions
		= new HashMap<Integer, IActiveObjectDesc>();
	protected final BlockingQueue<IShape> shapes = new ArrayBlockingQueue<IShape>(10000);
	protected ScheduledExecutorService scheduler;
	protected final JTextArea logOutput;
	protected final Map<Integer, ToolTip> tips
		= new HashMap<Integer, ToolTip>();
	protected MessagesProgressBar progressBar = null;
	protected int progress = 0;
	
	protected boolean firstRun = true;
	protected boolean isRunning = false;
	
	protected ITool mouseTool;
	
	/** a runnable, performing steps */
	class SimulationRunnable implements Runnable {
		public void run() {
			try {
				if (progressBar != null) {
					progress++;
					progressBar.setValue(progress);
					if(progress == progressBar.getMaximum()) {
						scheduler.shutdown();
						killProgressBar();
						isRunning = false;
					}
				}
				step();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/** display listener */
	class DisplayListener implements IDisplayListener, ILoggerListener {
	
		public DisplayListener() {
		}
	
		public void objectAdded(int id, IActiveObjectDesc desc) {
			descriptions.put(id, desc);
			addToolTip(id, desc);
			repaint();
		}
	
		public void descChanged(int id, int oldX, int oldY) {
			IActiveObjectDesc desc = descriptions.get(id);
			tips.remove(id);
			addToolTip(id, desc);
			Graphics g = getGraphics();
			Color prevColor = g.getColor();
			g.setColor(Const.BK_COLOR);
			g.fillRect(oldX, oldY, Const.POINT_X_SIZE, Const.POINT_Y_SIZE);
			g.setColor(prevColor);
		}

		private void addToolTip(int id, IActiveObjectDesc desc) {
			tips.put(id, new ToolTip(new Rectangle(
					desc.getX(),
					desc.getY(),
					Const.POINT_X_SIZE,
					Const.POINT_Y_SIZE
			), desc.getName()));
		}
		
		public void messageConflicted(int source, int dest) {
			IActiveObjectDesc d1 = descriptions.get(source);
			IActiveObjectDesc d2 = descriptions.get(dest);
			
			try {
				shapes.put(new Line(
					d1, d2,
					Color.RED,
					Const.BK_COLOR
				));
			} catch (InterruptedException ie) {
			}
		}
	
		public void messageRecieved(int source, int dest, Object data) {
			/* StringBuilder b = new StringBuilder();
			for (int i = 0; i < data.length; i++) {
				b.append(data[i]);
				b.append(' ');
			} */
			if (source != -1) {
				IActiveObjectDesc d1 = descriptions.get(source);
				IActiveObjectDesc d2 = descriptions.get(dest);
				try {
					shapes.put(new Line(
						d1, d2,
						Color.GREEN,
						Const.BK_COLOR
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
					d, Color.BLUE, Const.BK_COLOR
				));
			} catch (InterruptedException ie) {
			}
		}
	
		public void notificationArrived(int id, String notification) {
			IActiveObjectDesc d = descriptions.get(id);
			try {
				shapes.put(new Notification(
					d.getX(), d.getY(), notification, Color.BLACK, Const.BK_COLOR
				));
			} catch (InterruptedException ie) {
			}
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
	
	class MyMouseListener implements MouseInputListener {
		
		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseMoved(MouseEvent arg0) {}
		
		public void mousePressed(MouseEvent arg0) {
			IActiveObjectDesc affectedDesc = null;
			for (IActiveObjectDesc d: descriptions.values()) {
				if (
					(Math.abs(arg0.getX() - d.getX() - Const.POINT_X_SIZE / 2)
						< Const.POINT_X_SIZE) &&
					(Math.abs(arg0.getY() - d.getY() - Const.POINT_Y_SIZE / 2)
						< Const.POINT_Y_SIZE)
				) {
					affectedDesc = d;
					// System.err.println("Hit!");
				}
			}
			mouseTool.mousePressed(arg0, affectedDesc);
		}
		
		public void mouseDragged(MouseEvent arg0) {
			// System.err.println("Moved!");
			mouseTool.mouseMoved(arg0);
			repaint();
		}
		
		public void mouseReleased(MouseEvent arg0) {
			mouseTool.mouseReleased(arg0);
		}
		
		public void mouseExited(MouseEvent arg0) {
			mouseTool.mouseReleased(arg0);
		}
		
	}
	
	protected void killProgressBar() {
		progressBar.finish();
		progressBar = null;
	}
	
	/** icons painter */
	protected void paintMots (Graphics graphics) {
		Color prevColor = graphics.getColor();
		graphics.setColor(Color.BLACK);
		for (Map.Entry<Integer, IActiveObjectDesc> p: descriptions.entrySet()) {
			IActiveObjectDesc d = p.getValue();
			d.getImage().paintIcon(this, graphics, p.getValue().getX(), p.getValue().getY());
			/* graphics.drawString(
				d.getName(),
				d.getX() + Const.POINT_X_SIZE, d.getY() + Const.POINT_Y_SIZE
			); */
		}
		graphics.setColor(prevColor);
	}
	
	/** component painter */
	protected void paintComponent (Graphics graphics) {
		Color prevColor = graphics.getColor();
		graphics.setColor(Const.BK_COLOR);
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
		logOutput.append("\n" + msg);
		logOutput.setCaretPosition(logOutput.getText().length());
		// logOutput.s
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
		try {
			Calendar c = Calendar.getInstance(); 
			String s = c.get(Calendar.DAY_OF_YEAR) + " - " +
			           c.get(Calendar.HOUR_OF_DAY) + "_" +
			           c.get(Calendar.MINUTE) + "_" +
			           c.get(Calendar.SECOND) + ".log";
			logOutput.append("Saving log to: " + s);
			PrintStream p = new PrintStream(new File(s));;
			Logger.getInstance().addOutputStream(p);
		} catch (FileNotFoundException fnfe) {
			logMessage("Output file not found!");
		}
		d.addDisplayListener(displayListener);
		// enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		MouseInputListener listener = new MyMouseListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);
		
		setToolTipText("outline");
		setPreferredSize(new Dimension (1024, 768));
		setBackground(Const.BK_COLOR);
	}
	
	/* public JToolTip createToolTip() {
		JToolTip j = new JToolTip();
		j.setToolTipText("sdfsdf");
		return j;
	} */
	
	public String getToolTipText(MouseEvent e) {	    
		// get mouse position
		Point p = e.getPoint();

        // see if it's in any of the custom tooltip
        // bounding boxes
        for (ToolTip t: tips.values()) {
        	if (t.rect.contains(p)) {
            	return t.text;
            }
        }
        // System.err.println("----");
        Line l;
        for (IShape s: shapes) {
        	if (s instanceof Line) {
        		l = (Line)s;
        		if (l.contains(p)) {
        			return "> " + l.getColor();
        		}
        	}
        }
        // if not, return default
        // return super.getToolTipText();
        return null;
	}
	
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
			if (progressBar != null) {
				killProgressBar();
			}
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
	
	/* package-private */ synchronized void stepSimulation(MessagesProgressBar progressBar,
			long rate, TimeUnit units) {
		if (!isRunning) {
			this.progressBar = progressBar;
			this.progress = 0;
			isRunning = true;
			scheduler = Executors.newSingleThreadScheduledExecutor();
			scheduler.scheduleWithFixedDelay(new SimulationRunnable(),
				0, rate, units);
		}
	}

	/**
	 * @param mouseTool The mouseTool to set.
	 */
	public void setMouseTool(ITool mouseTool) {
		// System.err.println(mouseTool.getClass().getSimpleName());
		this.mouseTool = mouseTool;
	}
	
	public synchronized boolean isRunning() {
		return isRunning;
	}
	
}
