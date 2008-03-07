package ru.amse.nikitin.hugetest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import ru.amse.nikitin.activeobj.impl.Dispatcher;
import ru.amse.nikitin.activeobj.impl.Logger;
import ru.amse.nikitin.cui.IConsoleUI;
import ru.amse.nikitin.cui.impl.BasicUI;
import ru.amse.nikitin.cui.impl.ConsoleUI;
import ru.amse.nikitin.sensnet.impl.Wireless;
import ru.amse.nikitin.sensnet.impl.Mot;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Dispatcher disp = Dispatcher.getInstance(); 
		disp.addMessageFilter(Wireless.getInstance());
		
		IConsoleUI dispc = new ConsoleUI(disp);
		
		for (int i = 0; i < Const.count; i++) {
			disp.addActiveObjectListener(new Mot(0, 0, 0, 0, new Factory()));
		}
		
		try {
			Logger.getInstance().setOutputStream(new PrintStream(new File(Const.fileName)));
		} catch (FileNotFoundException fnfe) {
			System.err.println("Output file not found!");
		}
		
		dispc.runSimulation(Const.iterationsCount);
		
		BasicUI.cuiLoop(dispc);
	}

}
