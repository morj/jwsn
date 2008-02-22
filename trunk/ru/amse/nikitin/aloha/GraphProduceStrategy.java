package ru.amse.nikitin.aloha;

import ru.amse.nikitin.activeobj.IActiveObjectDesc;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.graph.impl.Graph;
import ru.amse.nikitin.sensnet.IGraphProduceStrategy;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.random.RandomArea;

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
			double t1 = mots[i].getThreshold();
			for (int j = i + 1; j < mots.length; j++) {
				IActiveObjectDesc di = mots[i].getDesc();
				IActiveObjectDesc dj = mots[j].getDesc();
				
				double r = RandomArea.squaredDistance(
					di.getX(), di.getY(),
					dj.getX(), dj.getY()
				);
				
				double t2 = mots[j].getThreshold();
				double p = RandomArea.commonMotPower / r;
				
				if ((p >= t1) && (p >= t2)) {
					g.addNeighbour(i, j);
					g.addNeighbour(j, i);
				}
			}
		}
		
		return g;
	}
	
}
