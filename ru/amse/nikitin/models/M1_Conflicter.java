package ru.amse.nikitin.models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.LinkedList;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.simulator.IDispatcher;
import ru.amse.nikitin.simulator.impl.Dispatcher;
import ru.amse.nikitin.simulator.util.graph.IGraph;
import ru.amse.nikitin.simulator.util.graph.impl.Graph;
import ru.amse.nikitin.simulator.util.xml.FieldParser;
import ru.amse.nikitin.ui.gui.impl.BasicUI;
import ru.amse.nikitin.models.conflicter.ConflicterApp;
import ru.amse.nikitin.models.conflicter.SenderApp;
import ru.amse.nikitin.protocols.app.BsApp;
import ru.amse.nikitin.protocols.mac.aloha.CommonMac;
import ru.amse.nikitin.protocols.routing.direct.Net;

public class M1_Conflicter {
	public static double power = 10;
	public static double threshold = 0;

	public static void main(String[] args) {
		Mot m1 = new Mot(101, 100, power, threshold);
		Mot m2 = new Mot(101, 200, 100 * power, threshold);
		Mot m3 = new Mot(201, 100, power, threshold);
		
		m1.addModule("mac", new CommonMac(m1));
		m1.addModule("net", new Net(m1));
		m1.addModule("app", new SenderApp(m1));
		
		m2.addModule("mac", new CommonMac(m2));
		m2.addModule("net", new Net(m2));
		m2.addModule("app", new ConflicterApp(m2));
		
		m3.addModule("mac", new CommonMac(m3));
		m3.addModule("net", new Net(m3));
		m3.addModule("app", new BsApp(m3));
		
		m1.createTopology();
		m2.createTopology();
		m3.createTopology();
		
		List<Mot> mots = new LinkedList<Mot>();
		mots.add(m1); mots.add(m2); mots.add(m3);
		
		try {
			FieldParser.ReadStyles(new FileInputStream("Descriptions.xml"), mots);
		} catch (FileNotFoundException nf) {
			System.err.println("File not found!");
		}
		
		IDispatcher disp = Dispatcher.getInstance();
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
		
		g.solvePaths(2);
		disp.setTopology(g);
		
		BasicUI.createUI();
		
		disp.addActiveObjectListener(m1);
		disp.addActiveObjectListener(m2);
		disp.addActiveObjectListener(m3);
	}
}
