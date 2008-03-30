package ru.amse.nikitin.models;

import ru.amse.nikitin.models.aloha.GraphProduceStrategy;
import ru.amse.nikitin.protocols.app.BsApp;
import ru.amse.nikitin.protocols.app.EmptyApp;
import ru.amse.nikitin.protocols.app.SendApp;
import ru.amse.nikitin.protocols.mac.aloha.CommonMac;
import ru.amse.nikitin.protocols.routing.distributed.CommonNet;
import ru.amse.nikitin.protocols.routing.distributed.BsNet;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.random.RandomArea;
import ru.amse.nikitin.ui.gui.impl.BasicUI;
import ru.amse.nikitin.simulator.util.graph.IGraph;
import ru.amse.nikitin.simulator.impl.Dispatcher;
import ru.amse.nikitin.simulator.IDispatcher;

/* abstract class MotGenerator implements IMotGenerator {
	
	protected void connectDuplexGates(IGate a, IGate b) {
		a.setFrom(b);
		a.setTo(b);
		b.setTo(a);
		b.setFrom(a);
	}
	
	protected void declareDuplexGates(MotModule a, MotModule b) {
		IGate u = a.declareGate("upper");
		IGate l = b.declareGate("lower");
		connectDuplexGates(u, l);
	}


	protected void connectModules(Mot m, MotModule mac, MotModule net, MotModule app) {
		m.addModule("mac", mac);
		m.addModule("net", net);
		m.addModule("app", app);
		
		IGate gate = mac.declareGate("lower");
		
		IGate input = m.declareInputGate(WirelessPacket.class);
		input.setTo(gate);
		gate.setFrom(input);
		
		IGate output = m.getOutputGate();
		gate.setTo(output);
		output.setFrom(gate);
		
		declareDuplexGates(mac, net);
		declareDuplexGates(net, app);
	}
	
	public Mot generateMot(int x, int y, double power, double threshold) {
		return null;
	}
	
} /**/

abstract class MotGenerator implements IMotGenerator {
	
	protected void connectModules(Mot m, MotModule mac, MotModule net, MotModule app) {
		m.addModule("mac", mac);
		m.addModule("net", net);
		m.addModule("app", app);
		
		m.createTopology();
	}
	
	public Mot generateMot(int x, int y, double power, double threshold) {
		return null;
	}
	
} /**/

class BsMotGenerator extends MotGenerator {
	public Mot generateMot(int x, int y, double power, double threshold) {
		Mot m = new Mot(x, y, power, threshold);
		
		MotModule mac = new CommonMac(m);
		MotModule net = new BsNet(m);
		MotModule app = new BsApp(m); 
		
		connectModules(m, mac, net, app);
		
		return m;
	}
}

class SendMotGenerator extends MotGenerator {
	public Mot generateMot(int x, int y, double power, double threshold) {
		Mot m = new Mot(x, y, power, threshold);
		
		MotModule mac = new CommonMac(m);
		MotModule net = new CommonNet(m);
		MotModule app = new SendApp(m); 
		
		connectModules(m, mac, net, app);
		
		return m;
	}
}

class EmptyMotGenerator extends MotGenerator {
	public Mot generateMot(int x, int y, double power, double threshold) {
		Mot m = new Mot(x, y, power, threshold);
		
		MotModule mac = new CommonMac(m);
		MotModule net = new CommonNet(m);
		MotModule app = new EmptyApp(m); 
		
		connectModules(m, mac, net, app);
		
		return m;
	}
}

public class AlohaRandTest {

	public static void main(String[] args) {
		
		/* Mot[] mots = RandomArea.getInstance().getArea(
			Const.fieldX, Const.fieldY, 30,
			SendMotFactory.getInstance(),
			MotFactory.getInstance(),
			BsMotFactory.getInstance(),
			RandomArea.commonMotPower
		); */
		
		Mot[] mots = RandomArea.getInstance().getArea(
			Const.fieldX, Const.fieldY, 30,
			new SendMotGenerator(),
			new EmptyMotGenerator(),
			new BsMotGenerator(),
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