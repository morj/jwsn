package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.simulator.util.graph.IGraph;

/** 
 * Graph producing strategy
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IGraphProduceStrategy {
	/** producing method */
	public IGraph<Integer> produceGraph(Mot[] mots);
}
