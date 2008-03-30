package ru.amse.nikitin.models.conflicter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.LinkedList;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.simulator.util.xml.FieldParser;
import ru.amse.nikitin.protocols.app.BsApp;
import ru.amse.nikitin.protocols.mac.aloha.CommonMac;
import ru.amse.nikitin.protocols.rooting.aloha.CommonNet;

public class Test {
	public static double power = 10;
	public static double threshold = 0;

	public static void main() {
		Mot m1 = new Mot(100, 100, power, threshold);
		Mot m2 = new Mot(100, 100, power, threshold);
		Mot m3 = new Mot(100, 100, power, threshold);
		
		m1.addModule("mac", new CommonMac(m1));
		m1.addModule("net", new CommonNet(m1));
		m1.addModule("app", new SenderApp(m1));
		
		m2.addModule("mac", new CommonMac(m2));
		m2.addModule("net", new CommonNet(m2));
		m2.addModule("app", new SenderApp(m2));
		
		m2.addModule("mac", new CommonMac(m2));
		m2.addModule("net", new CommonNet(m2));
		m2.addModule("app", new BsApp(m2));
		
		m1.createTopology();
		m2.createTopology();
		m3.createTopology();
		
		List<Mot> mots = new LinkedList<Mot>();
		mots.add(m1); mots.add(m2); mots.add(m3);
		
		try {
			FieldParser.ReadStyles(new FileInputStream("library.xml"), mots);
		} catch (FileNotFoundException nf) {
			System.err.println("File not found!");
		}
	}
}
