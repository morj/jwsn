package ru.amse.nikitin.centralized;

import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.Radio;
import ru.amse.nikitin.activeobj.impl.Dispatcher;
import ru.amse.nikitin.graph.*;
import ru.amse.nikitin.graph.impl.*;

public class Test {
	public static void main(String[] args) {
		System.out.println("Testing centralized algoritm!");
		
		Dispatcher disp = Dispatcher.getInstance(); 
		disp.setMessageFilter(Radio.getInstance());
		
		Mot a = new Mot( 0, 40, 100, 0, SendMotFactory.getInstance());
		Mot b = new Mot(10, 30, 100, 0, MotFactory.getInstance());
		Mot c = new Mot(30, 20, 100, 0, MotFactory.getInstance());
		Mot d = new Mot(50, 20, 100, 0, BsMotFactory.getInstance());
		Mot e = new Mot(24, 10,  50, 0, SendMotFactory.getInstance());
		
		disp.addActiveObjectListener(a);
		disp.addActiveObjectListener(b);
		disp.addActiveObjectListener(c);
		disp.addActiveObjectListener(d);
		disp.addActiveObjectListener(e);
		
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
		
		/* g.solvePaths(3);
		
		for (IVertex<Integer> v: g.getVertices()) {
			System.out.print(v.getData() + ": ");
			if (v.getPredecessor() != null) {
				System.out.println(v.getPredecessor().getData()
					+ "; w = " + v.getWeight());
			} else {
				System.out.println("none");
			}
		} /* debug */
		
		/* g.solveColors();
		
		for (IVertex<Integer> v: g.getVertices()) {
			System.out.println(v.getData() + ": " + v.getColor());
		}  /* debug */
		
		disp.setTopology(g);
		disp.init();
		
		for (int i = 0; i < 12; i++) {
			disp.step();
		} /**/
	}
}
