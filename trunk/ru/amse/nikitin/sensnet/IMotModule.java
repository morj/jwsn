package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.graph.IGraph;

/** 
 * mot module
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IMotModule {
	/** upper and lower layer setter */
	void setNeghbours(IMotModule u, IMotModule l);
	/** message recieve */
	boolean recieveMessage(IPacket m);
	/** message send */
	boolean sendMessage(IPacket m);
	/** module init */
	void init(IGraph<Integer> topology);
}
