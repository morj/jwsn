package ru.amse.nikitin.models.hugetest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.Wireless;
import ru.amse.nikitin.simulator.impl.Dispatcher;
import ru.amse.nikitin.simulator.impl.Logger;
import ru.amse.nikitin.ui.cui.impl.BasicUI;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Dispatcher disp = Dispatcher.getInstance(); 
		disp.addMessageFilter(Wireless.getInstance());
		
		for (int i = 0; i < Const.count; i++) {
			disp.addActiveObjectListener(new Mot(0, 0, 0, 0, new Factory()));
		}
		
		try {
			Logger.getInstance().addOutputStream(new PrintStream(new File(Const.fileName)));
		} catch (FileNotFoundException fnfe) {
			System.err.println("Output file not found!");
		}
		
		BasicUI.createUI();
	}

}
