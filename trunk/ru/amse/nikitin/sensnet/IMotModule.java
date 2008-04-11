package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.net.IModule;
import ru.amse.nikitin.simulator.util.graph.IGraph;

/** 
 * Mot Module interface.
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IMotModule extends IModule {
	/** extended init */
	void init(IGraph<Integer> topology);
}
