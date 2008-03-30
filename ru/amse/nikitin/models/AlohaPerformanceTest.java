package ru.amse.nikitin.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import ru.amse.nikitin.models.aloha.BsMotFactory;
import ru.amse.nikitin.models.aloha.GraphProduceStrategy;
import ru.amse.nikitin.models.aloha.MotFactory;
import ru.amse.nikitin.models.aloha.SendMotFactory;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.Wireless;
import ru.amse.nikitin.sensnet.random.RandomArea;
import ru.amse.nikitin.simulator.impl.Dispatcher;
import ru.amse.nikitin.simulator.impl.Logger;
import ru.amse.nikitin.simulator.util.graph.IGraph;
import ru.amse.nikitin.ui.cui.impl.BasicUI;

public class AlohaPerformanceTest {

	public static void main(String[] args) {
		Mot[] mots = RandomArea.getInstance().getArea(
			Const.fieldX, Const.fieldY, Const.motCount,
			SendMotFactory.getInstance(),
			MotFactory.getInstance(),
			BsMotFactory.getInstance(), RandomArea.commonMotPower 
		);
		
		Dispatcher disp = Dispatcher.getInstance(); 
		disp.addMessageFilter(Wireless.getInstance());
		
		for (int i = 0; i < mots.length; i++) {
			disp.addActiveObjectListener(mots[i]);
		}
		
		IGraph<Integer> g = GraphProduceStrategy.getInstance().produceGraph(mots);
		g.solvePaths(mots.length - 1);
		disp.setTopology(g);
		
		try {
			Logger.getInstance().addOutputStream(new PrintStream(new File(Const.alohaName)));
		} catch (FileNotFoundException fnfe) {
			System.err.println("Output file not found!");
		}
		
		BasicUI.createUI();
	}
}
