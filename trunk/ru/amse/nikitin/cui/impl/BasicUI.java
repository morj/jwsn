package ru.amse.nikitin.cui.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ru.amse.nikitin.cui.IConsoleUI;


/**
 * @author @author Pavel A. Nikitin
 * basic console ui loop controlling one console disp listener
 *
 */
public class BasicUI {
	public static void cuiLoop(IConsoleUI dispc) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
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
				}
			}
		} catch (IOException ioe) {
		}
	}
}