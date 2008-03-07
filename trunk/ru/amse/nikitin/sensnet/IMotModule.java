package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.graph.IGraph;

/** 
 * mot module
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IMotModule {
	/** message recieve */
	boolean recieveMessage(IPacket m);
	/** module init */
	void init(IGraph<Integer> topology);
	IGate declareGate(String name);
	IGate getGate(String name);
	void setArrivedOn(String arrivedOn);
}
