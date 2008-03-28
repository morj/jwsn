package ru.amse.nikitin.models;

import javax.swing.*;

import ru.amse.nikitin.models.aloha.*;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.IGraphProduceStrategy;
import ru.amse.nikitin.simulator.util.graph.IGraph;
import ru.amse.nikitin.simulator.util.graph.impl.Graph;
import ru.amse.nikitin.ui.gui.impl.BasicUI;

class MyGraphProduceStrategy implements IGraphProduceStrategy {
	private static MyGraphProduceStrategy instance = null;
	
	private MyGraphProduceStrategy() {};
	
	public static MyGraphProduceStrategy getInstance () {
		if (instance == null) {
			instance = new MyGraphProduceStrategy();
		}
		return instance;
	}

	public IGraph<Integer> produceGraph(Mot[] mots) {
		IGraph<Integer> g = new Graph<Integer>();
		
		for (int i = 0; i < 5; i++) {
			g.newVertex(new Integer(i));
		}
		
		g.addNeighbour(0, 1);
		g.addNeighbour(1, 0);
		g.addNeighbour(1, 2);
		g.addNeighbour(2, 1);
		g.addNeighbour(1, 4);
		g.addNeighbour(4, 1);
		g.addNeighbour(2, 3);
		g.addNeighbour(3, 2);
		g.addNeighbour(2, 4);
		g.addNeighbour(4, 2);
		
		return g;
	}
}

public class GUITest {
	
	public static void main(String[] args) {
		Mot[] mots = new Mot[5];
		
		mots[0] = new Mot( 0, 40, 100, 0, SendMotFactory.getInstance());
		mots[1] = new Mot(10, 30, 100, 0, MotFactory.getInstance());
		mots[2] = new Mot(30, 20, 100, 0, MotFactory.getInstance());
		mots[3] = new Mot(50, 20, 100, 0, BsMotFactory.getInstance());
		mots[4] = new Mot(24, 10,  50, 0, SendMotFactory.getInstance());
		
		mots[0].newDesc(new ImageIcon("icons\\terminal_vs.gif"), "Send 1", 125, 400);
		mots[1].newDesc(new ImageIcon("icons\\palm2_vs.gif"), "Simple 1", 225, 300);
		mots[2].newDesc(new ImageIcon("icons\\palm2_vs.gif"), "Simple 2", 425, 200);
		mots[3].newDesc(new ImageIcon("icons\\laptop_vs.gif"), "BS", 525, 300);
		mots[4].newDesc(new ImageIcon("icons\\terminal_vs.gif"), "Send 2", 365, 10);
		
		JFrame appFrame = BasicUI.createUIFrame(
				mots, MyGraphProduceStrategy.getInstance(), 3
			);
		
		appFrame.setVisible (true); // show frame
	}
}
