package ru.amse.nikitin.ui.cui.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ru.amse.nikitin.ui.cui.IConsoleUI;


/**
 * @author @author Pavel A. Nikitin
 * basic console ui loop controlling one console disp listener
 *
 */
public class BasicUI {
	public static void createUI(String[] args) {
		if(args.length > 0) {
			try {
				IConsoleUI dispc = new ConsoleUI();
				int i = Integer.parseInt(args[0]);
				dispc.runSimulation(i);
			} catch (NumberFormatException nfe){
				System.out.println("Null UI, usage: <name>.java <iterations count>");
			}
		} else {
			IConsoleUI dispc = new ConsoleUI();
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Console UI: enter command or type 'help'");
			try {
				while (true) {
					String s = in.readLine();
					
					if (s != null) {
						if ((s.equals("stop")) || (s.equals(""))) {
							dispc.stopSimulation();
						}
						if (s.equals("")) {
							break;
						}
						if (s.equals("run")) {
							System.out.print("enter iterations count: ");
							s = in.readLine();
							int i = Integer.parseInt(s);
							dispc.runSimulation(i);
						}
						if (s.equals("help")) {
							System.out.println("Commands:");
							System.out.println("run: runs simulation");
							System.out.println("stop: stops simulation");
						}
					}
				}
			} catch (IOException ioe) {
			}
		}
	}
}