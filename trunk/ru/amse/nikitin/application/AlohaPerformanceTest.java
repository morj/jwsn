package ru.amse.nikitin.application;

import java.io.*;

import ru.amse.nikitin.activeobj.impl.Dispatcher;
import ru.amse.nikitin.activeobj.impl.Logger;
import ru.amse.nikitin.aloha.*;
import ru.amse.nikitin.cui.IConsoleUI;
import ru.amse.nikitin.cui.impl.BasicUI;
import ru.amse.nikitin.cui.impl.ConsoleUI;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.Radio;
import ru.amse.nikitin.sensnet.impl.RandomArea;

public class AlohaPerformanceTest {

	public static void main(String[] args) {
		Mot[] mots = RandomArea.getInstance().getArea(
			Const.fieldX, Const.fieldY, Const.motCount,
			SendMotFactory.getInstance(),
			MotFactory.getInstance(),
			BsMotFactory.getInstance(), RandomArea.commonMotPower 
		);
		
		Dispatcher disp = Dispatcher.getInstance(); 
		disp.setMessageFilter(Radio.getInstance());
		
		IConsoleUI dispc = new ConsoleUI(disp);
		
		for (int i = 0; i < mots.length; i++) {
			disp.addActiveObjectListener(mots[i]);
		}
		
		IGraph<Integer> g = GraphProduceStrategy.getInstance().produceGraph(mots);
		g.solvePaths(mots.length - 1);
		disp.setTopology(g);
		
		try {
			Logger.getInstance().setOutputStream(new PrintStream(new File(Const.alohaName)));
		} catch (FileNotFoundException fnfe) {
			System.err.println("Output file not found!");
		}
		
		dispc.runSimulation(Const.iterationsCount);
		
		BasicUI.cuiLoop(dispc);
	}
}
