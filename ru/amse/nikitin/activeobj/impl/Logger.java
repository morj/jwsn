package ru.amse.nikitin.activeobj.impl;

import java.io.PrintStream;
import java.util.List;
import java.util.LinkedList;
import ru.amse.nikitin.activeobj.ELogMsgType;
import ru.amse.nikitin.activeobj.ILoggerListener;
import ru.amse.nikitin.activeobj.IMessage;

/**
 * @author Pavel A. Nikitin
 * Logger
 *
 */
public class Logger {
	/** singleton instance */
	private static Logger instance = null;
	/** output stream */
	protected PrintStream outs = System.out;
	/** listeners */
	protected List<ILoggerListener> listeners = new LinkedList<ILoggerListener>();
	
	/** hidden constructor */
	private Logger() {
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
	public void setOutputStream(PrintStream outs) {
		this.outs = outs;
	}
	
	/** compound message logging */
	public void logMessage(ELogMsgType type, String pack, IMessage msg) {
		String logMessage = Dispatcher.getInstance().getTime()
		+ " " + type + ": " + pack + " - " + msg + " ";
		for (ILoggerListener l: listeners) {
			l.stringLogged(logMessage);
		}
		outs.println(logMessage);
	}
	
	/** less sophisticated message logging */
	public void logMessage(ELogMsgType type, String pack) {
		String logMessage = Dispatcher.getInstance().getTime()
		+ " " + type + ": " + pack;
		for (ILoggerListener l: listeners) {
			l.stringLogged(logMessage);
		}
		outs.println(logMessage);
	}
}
