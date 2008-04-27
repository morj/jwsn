package ru.amse.nikitin.models;

import javax.swing.ImageIcon;
import ru.amse.nikitin.models.aloha.GraphProduceStrategy;
import ru.amse.nikitin.net.IGate;
import ru.amse.nikitin.protocols.app.BsApp;
import ru.amse.nikitin.protocols.app.SensorApp;
import ru.amse.nikitin.protocols.mac.aloha.CommonMac;
import ru.amse.nikitin.protocols.routing.centralized.CommonNet;
import ru.amse.nikitin.protocols.routing.centralized.BsNet;
import ru.amse.nikitin.sensnet.impl.MonitoredPacket;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotGenerator;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.impl.MonitoredObject;
import ru.amse.nikitin.sensnet.random.RandomArea;
import ru.amse.nikitin.ui.gui.impl.BasicUI;
import ru.amse.nikitin.simulator.util.graph.IGraph;
import ru.amse.nikitin.simulator.impl.Dispatcher;
import ru.amse.nikitin.simulator.IDispatcher;
import ru.amse.nikitin.protocols.app.CarObject;
import ru.amse.nikitin.protocols.app.TemperatureObject;

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

class BsMotGenerator2 extends MotGenerator {
	public Mot generateMot(int x, int y, double power, double threshold) {
		Mot m = new Mot(x, y, power, threshold);
		
		MotModule mac = new CommonMac(m);
		MotModule net = new BsNet(m);
		MotModule app = new BsApp(m); 
		
		connectModules(m, mac, net, app);
		
		return m;
	}
}

class SendMotGenerator2 extends MotGenerator {
	public Mot generateMot(int x, int y, double power, double threshold) {
		Mot m = new Mot(x, y, power, threshold);
		
		MotModule mac = new CommonMac(m);
		MotModule net = new CommonNet(m);
		MotModule app = new SensorApp(m);
		
		connectModules(m, mac, net, app);
		
		return m;
	}
}

class EmptyMotGenerator2 extends MotGenerator {
	public Mot generateMot(int x, int y, double power, double threshold) {
		Mot m = new Mot(x, y, power, threshold);
		
		MotModule mac = new CommonMac(m);
		MotModule net = new CommonNet(m);
		MotModule app = new SensorApp(m);
		
		IGate sensingGate = m.declareInputGate(MonitoredPacket.class);
		IGate gate = app.declareGate("sensor");
		// sensingGate -> gate
		sensingGate.setTo(gate);
		gate.setFrom(sensingGate);
		
		connectModules(m, mac, net, app);
		
		return m;
	}
}

public class M4_CarsCentrAloha {

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
			new SendMotGenerator2(),
			new EmptyMotGenerator2(),
			new BsMotGenerator2(),
			Const.bsPower
		);
		
		MonitoredObject temp = new MonitoredObject(10, 10);
		temp.addModule("logic", new TemperatureObject(temp));
		temp.createTopology();
		temp.newDesc(new ImageIcon("icons\\bs.png"), "temperature", 10, 10);
		
		MonitoredObject car1 = new MonitoredObject(100, 100);
		car1.addModule("logic", new CarObject(car1));
		car1.createTopology();
		car1.newDesc(new ImageIcon("icons\\car.jpg"), "some car a", 100, 100);
		
		MonitoredObject car2 = new MonitoredObject(700, 100);
		car2.addModule("logic", new CarObject(car2));
		car2.createTopology();
		car2.newDesc(new ImageIcon("icons\\car.jpg"), "some car b", 500, 500);
		
		IDispatcher disp = Dispatcher.getInstance();
			
		IGraph<Integer> g = GraphProduceStrategy.getInstance().produceGraph(mots);
		g.solvePaths(mots.length - 1);
		disp.setTopology(g);
		
		BasicUI.createUI();
	
		for (int i = 0; i < mots.length; i++) {
			disp.addActiveObjectListener(mots[i]);
		}
		disp.addActiveObjectListener(temp);
		disp.addActiveObjectListener(car1);
		disp.addActiveObjectListener(car2);
	}
	
}
