package ru.amse.nikitin.activeobj;

import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.activeobj.impl.Time;
import ru.amse.nikitin.activeobj.impl.MessageInitData;

/**
 * @author Pavel A. Nikitin
 * Dispatcher
 *
 */
public interface IDispatcher {
	/** active object listener adder */
	void addActiveObjectListener(IActiveObject obj);
	/** ui listener adder */
	void addDisplayListener(IDisplayListener l);
	/** message scheduling */
	void scheduleMessage(IMessage m, Time t);
	/** simulation init */
	void init();
	/** simulation step */
	void step();
	/** allocate message for active object */
	MessageInitData getMessageInitData();
	void assignMessage(IActiveObject owner, IMessage m);
	/** send message */
	boolean sendMessage(IMessage m);
	/** topology setter */
	void setTopology(IGraph<Integer> graph);
	/** topology getter */
	IGraph<Integer> getTopology();
	/** add msg filter */
	void addMessageFilter(IMessageFilter m);
	int getListenersCount();
}
