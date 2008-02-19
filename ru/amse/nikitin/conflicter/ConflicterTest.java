package ru.amse.nikitin.conflicter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import ru.amse.nikitin.aloha.BsMotFactory;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.graph.impl.Graph;
import ru.amse.nikitin.gui.impl.BasicUI;
import ru.amse.nikitin.sensnet.IGraphProduceStrategy;
import ru.amse.nikitin.sensnet.impl.Mot;

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
		
		for (int i = 0; i < 3; i++) {
			g.newVertex(new Integer(i));
		}
		
		g.addNeighbour(0, 1);
		g.addNeighbour(1, 0);
		g.addNeighbour(1, 2);
		g.addNeighbour(2, 1);
		g.addNeighbour(0, 2);
		g.addNeighbour(2, 0);
		
		return g;
	}
}

public class ConflicterTest {
	
	public static void main(String[] args) {
		Mot[] mots = new Mot[3];
		
		mots[0] = new Mot( 10, 10, 100, 0, SenderMotFactory.getInstance());
		mots[1] = new Mot(10, 100, 100, 0, BsMotFactory.getInstance());		
		mots[2] = new Mot(50, 50, 100, 0, ConflicterMotFactory.getInstance());
		
		mots[0].newDesc(new ImageIcon("icons\\palm2_vs.gif"), "Sender (0)", 10, 10);
		mots[2].newDesc(new ImageIcon("icons\\palm2_vs.gif"), "Conflicter (2)", 50, 50);
		mots[1].newDesc(new ImageIcon("icons\\laptop_vs.gif"), "Bs (1)", 10, 100);
		
		JFrame appFrame = BasicUI.createUIFrame(
			mots, MyGraphProduceStrategy.getInstance(), 1
		);
		
		appFrame.setVisible (true); // show frame
	}
}
