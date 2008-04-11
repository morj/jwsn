package ru.amse.nikitin.models;

import ru.amse.nikitin.models.aloha.BsMotFactory;
import ru.amse.nikitin.models.aloha.GraphProduceStrategy;
import ru.amse.nikitin.models.aloha.MotFactory;
import ru.amse.nikitin.models.aloha.SendMotFactory;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.random.RandomArea;
import ru.amse.nikitin.simulator.IDispatcher;
import ru.amse.nikitin.simulator.impl.Dispatcher;
import ru.amse.nikitin.simulator.util.graph.IGraph;
import ru.amse.nikitin.ui.gui.impl.BasicUI;

public class M2_AlohaRand {

	public static void main(String[] args) {
		
		Mot[] mots = RandomArea.getInstance().getArea(
			Const.fieldX, Const.fieldY, 30,
			SendMotFactory.getInstance(),
			MotFactory.getInstance(),
			BsMotFactory.getInstance(),
			RandomArea.commonMotPower
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
