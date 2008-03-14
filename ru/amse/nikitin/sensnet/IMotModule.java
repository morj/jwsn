package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.net.IModule;

/** 
 * mot module
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IMotModule extends IModule {
	void init(IGraph<Integer> topology);
}
