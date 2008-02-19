package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.impl.Mot;

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
