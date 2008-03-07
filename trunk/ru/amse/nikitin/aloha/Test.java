package ru.amse.nikitin.aloha;

import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.Wireless;
import ru.amse.nikitin.activeobj.impl.Dispatcher;

public class Test {
	public static void main(String[] args) {
		System.out.println("Testing aloha algoritm!");
		
		Dispatcher disp = Dispatcher.getInstance(); 
		disp.addMessageFilter(Wireless.getInstance());
		
		Mot a = new Mot( 0, 40, 100, 0, SendMotFactory.getInstance());
		Mot b = new Mot(10, 30, 100, 0, MotFactory.getInstance());
		Mot c = new Mot(30, 20, 100, 0, MotFactory.getInstance());
		Mot d = new Mot(50, 20, 100, 0, BsMotFactory.getInstance());
		Mot e = new Mot(24, 10,  50, 0, SendMotFactory.getInstance());
		
		disp.addActiveObjectListener(a);
		disp.addActiveObjectListener(b);
		disp.addActiveObjectListener(c);
		disp.addActiveObjectListener(d);
		disp.addActiveObjectListener(e);
		
		disp.init();
		
		for (int i = 0; i < 14; i++) {
			disp.step();
		}
	}
}
