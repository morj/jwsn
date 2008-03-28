package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.net.IModule;
import ru.amse.nikitin.simulator.util.graph.IGraph;

/** 
 * mot module
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IMotModule extends IModule {
	void init(IGraph<Integer> topology);
}
