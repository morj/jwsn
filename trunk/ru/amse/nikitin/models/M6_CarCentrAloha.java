package ru.amse.nikitin.models;

import ru.amse.nikitin.models.aloha.GraphProduceStrategy;
import ru.amse.nikitin.net.IGate;
import ru.amse.nikitin.protocols.app.BsApp;
import ru.amse.nikitin.protocols.app.CarObject;
import ru.amse.nikitin.protocols.app.SensorApp;
import ru.amse.nikitin.protocols.app.TemperatureObject;
import ru.amse.nikitin.protocols.mac.aloha.CommonMac;
import ru.amse.nikitin.protocols.routing.centralized.BsNet;
import ru.amse.nikitin.protocols.routing.centralized.CommonNet;
import ru.amse.nikitin.sensnet.impl.MonitoredObject;
import ru.amse.nikitin.sensnet.impl.MonitoredPacket;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotGenerator;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.random.RandomArea;
import ru.amse.nikitin.simulator.IDispatcher;
import ru.amse.nikitin.simulator.impl.Dispatcher;
import ru.amse.nikitin.simulator.util.graph.IGraph;
import ru.amse.nikitin.ui.gui.impl.BasicUI;
import ru.amse.nikitin.ui.gui.impl.Util;


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
		MotModule app = new SensorApp(m);
		
		connectModules(m, mac, net, app);
		
		return m;
	}
}

class EmptyMotGenerator extends MotGenerator {
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

public class M6_CarCentrAloha {

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
			Const.bsPower
		);
		
		MonitoredObject temp = new MonitoredObject(10, 10);
		temp.addModule("logic", new TemperatureObject(temp));
		temp.createTopology();
		temp.newDesc(Util.getInstance().createImageIcon("thermo.png"), "temperature", 10, 10);
		
		MonitoredObject car = new MonitoredObject(100, 100);
		car.addModule("logic", new CarObject(car));
		car.createTopology();
		car.newDesc(Util.getInstance().createImageIcon("noicon.png"), "some car", 100, 100);
		
		IDispatcher disp = Dispatcher.getInstance();
			
		IGraph<Integer> g = GraphProduceStrategy.getInstance().produceGraph(mots);
		g.solvePaths(mots.length - 1);
		disp.setTopology(g);
		
		BasicUI.createUI();
	
		for (int i = 0; i < mots.length; i++) {
			disp.addActiveObjectListener(mots[i]);
		}
		disp.addActiveObjectListener(temp);
		disp.addActiveObjectListener(car);
	}
	
}
