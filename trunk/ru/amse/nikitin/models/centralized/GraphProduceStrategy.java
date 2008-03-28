package ru.amse.nikitin.models.centralized;

import ru.amse.nikitin.sensnet.IGraphProduceStrategy;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.random.RandomArea;
import ru.amse.nikitin.simulator.IActiveObjectDesc;
import ru.amse.nikitin.simulator.util.graph.IGraph;
import ru.amse.nikitin.simulator.util.graph.impl.Graph;

public class GraphProduceStrategy implements IGraphProduceStrategy {
	private static GraphProduceStrategy instance = null;
	
	private GraphProduceStrategy() {};
	
	public static GraphProduceStrategy getInstance () {
		if (instance == null) {
			instance = new GraphProduceStrategy();
		}
		return instance;
	}

	public IGraph<Integer> produceGraph(Mot[] mots) {
		IGraph<Integer> g = new Graph<Integer>();
		
		for (int i = 0; i < mots.length; i++) {
			g.newVertex(new Integer(i));
		}
		
		for (int i = 0; i < mots.length; i++) {
			double t = mots[i].getThreshold();
			for (int j = 0; j < mots.length; j++) {
				if (i != j) {
					IActiveObjectDesc di = mots[i].getDesc();
					IActiveObjectDesc dj = mots[j].getDesc();
					
					double r = RandomArea.squaredDistance(
						di.getX(), di.getY(),
						dj.getX(), dj.getY()
					);
					
					if (RandomArea.commonMotPower / r >= t) {
						g.addNeighbour(i, j);
					}
				}
			}
		}
		
		return g;
	}

}
