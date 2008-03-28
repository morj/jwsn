package ru.amse.nikitin.simulator.impl;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import ru.amse.nikitin.simulator.ELogMsgType;
import ru.amse.nikitin.simulator.ILoggerListener;
import ru.amse.nikitin.simulator.IMessage;

/**
 * @author Pavel A. Nikitin
 * Logger
 *
 */
public class Logger {
	/** singleton instance */
	private static Logger instance = null;
	/** output stream */
	protected List<PrintStream> outs = new LinkedList<PrintStream>();
	/** listeners */
	protected List<ILoggerListener> listeners = new LinkedList<ILoggerListener>();
	
	/** hidden constructor */
	private Logger() {
		outs.add(System.out);
	}
	
	/** singleton instance getter */
	public static Logger getInstance () {
		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}
	
	/** listener adder */
	public void addListener(ILoggerListener l) {
		listeners.add(l);
	}
	
	/** outputstream setter */
	public void addOutputStream(PrintStream p) {
		outs.add(p);
	}
	
	/** compound message logging */
	public void logMessage(ELogMsgType type, String pack, IMessage msg) {
		String logMessage = Dispatcher.getInstance().getTime()
		+ " " + type + ": " + pack + " - " + msg + " ";
		for (ILoggerListener l: listeners) {
			l.stringLogged(logMessage);
		}
		for (PrintStream p: outs) {
			p.println(logMessage);	
		}
	}
	
	/** less sophisticated message logging */
	public void logMessage(ELogMsgType type, String pack) {
		String logMessage = Dispatcher.getInstance().getTime()
		+ " " + type + ": " + pack;
		for (ILoggerListener l: listeners) {
			l.stringLogged(logMessage);
		}
		for (PrintStream p: outs) {
			p.println(logMessage);	
		}
	}
}
