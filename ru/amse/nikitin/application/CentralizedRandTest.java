package ru.amse.nikitin.application;

import javax.swing.JFrame;

import ru.amse.nikitin.gui.impl.BasicUI;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.random.RandomArea;
import ru.amse.nikitin.centralized.*;

public class CentralizedRandTest {

	public static void main(String[] args) {
		
		Mot[] mots = RandomArea.getInstance().getArea(
			1024, 480, 30,
			SendMotFactory.getInstance(),
			MotFactory.getInstance(),
			BsMotFactory.getInstance(),
			Const.bsPower
		);
		
		JFrame appFrame = BasicUI.createUIFrame(
			mots, GraphProduceStrategy.getInstance(),
			mots.length - 1
		);
		
		appFrame.setVisible (true); // show frame
	}

}
