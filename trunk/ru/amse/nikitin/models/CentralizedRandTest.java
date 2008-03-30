package ru.amse.nikitin.models;

import ru.amse.nikitin.models.centralized.BsMotFactory;
import ru.amse.nikitin.models.centralized.GraphProduceStrategy;
import ru.amse.nikitin.models.centralized.MotFactory;
import ru.amse.nikitin.models.centralized.SendMotFactory;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.random.RandomArea;
import ru.amse.nikitin.simulator.IDispatcher;
import ru.amse.nikitin.simulator.impl.Dispatcher;
import ru.amse.nikitin.simulator.util.graph.IGraph;
import ru.amse.nikitin.ui.gui.impl.BasicUI;

public class CentralizedRandTest {

	public static void main(String[] args) {
		
		Mot[] mots = RandomArea.getInstance().getArea(
			Const.fieldX, Const.fieldY, 30,
			SendMotFactory.getInstance(),
			MotFactory.getInstance(),
			BsMotFactory.getInstance(),
			Const.bsPower
		);
		
		IDispatcher disp = Dispatcher.getInstance();
			
		IGraph<Integer> g = GraphProduceStrategy.getInstance().produceGraph(mots);
		g.solvePaths(mots.length - 1);
		disp.setTopology(g);
		
		BasicUI.createUI();
		
		for (int i = 0; i < mots.length; i++) {
			disp.addActiveObjectListener(mots[i]);
		}
	}

}
