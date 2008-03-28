package ru.amse.nikitin.simulator;

import ru.amse.nikitin.simulator.impl.MessageInitData;
import ru.amse.nikitin.simulator.impl.Time;
import ru.amse.nikitin.simulator.util.graph.IGraph;

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
