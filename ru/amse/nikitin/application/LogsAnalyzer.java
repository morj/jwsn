package ru.amse.nikitin.application;

import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class LogsAnalyzer {
	
	private static final Map <Integer, Integer> createTime
		= new HashMap <Integer, Integer>();

	public static void analyzeLog(BufferedReader input, PrintStream output) {
		try {
			int recvCount = 0;
			double timesSum = 0;
			Integer maxTime = 0;
			String s;
			while ((s =input.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(s);
				Integer time = Integer.parseInt(st.nextToken());
				st.nextToken();
				String t = st.nextToken();
				if (t.equals("allocated")) {
					// output.print("a ");
					Integer id = Integer.parseInt(st.nextToken());
					// output.println(id);
					createTime.put(id, time);
				}
				if (t.equals("hello")) {
					st.nextToken(); // skipping a word
					// output.print("h ");
					Integer id = Integer.parseInt(st.nextToken());
					// output.println(id);
					if (createTime.containsKey(id)) {
						recvCount++;
						
						Integer time_ = createTime.get(id);
						Integer deliveryTime = time - time_;
						
						/* if (deliveryTime < maxTime) {
							output.println("Less than max at " + time);
						}
						if (deliveryTime == maxTime) {
							output.println("Max at " + time);
						} */
						if (deliveryTime > maxTime) {
							maxTime = deliveryTime;
							// output.println("New max at " + time);
						}
						
						timesSum += deliveryTime;
					} else {
						output.println("Error");
					}
				}
			}
			
			output.println("Max time: " + maxTime);
			// output.println("Times sum: " + timesSum);
			output.println("Recv count: " + recvCount);
			output.println("Average time: " + (timesSum / recvCount));
		} catch (IOException ioe) {
			output.println("I/O Error");
		}
	}
	
	public static void main(String[] args) {
		try {
			System.out.println("Aloha:");
			analyzeLog(
				new BufferedReader(new FileReader(Const.alohaName)),
				System.out
			);
			System.out.println("Centralized:");
			analyzeLog(
				new BufferedReader(new FileReader(Const.centralizedName)),
				System.out
			);
		} catch (FileNotFoundException fnfe) {
			System.err.println("Output file not found!");
		}
	}

}
