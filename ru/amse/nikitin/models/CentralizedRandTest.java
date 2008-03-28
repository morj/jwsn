package ru.amse.nikitin.models;

import javax.swing.JFrame;

import ru.amse.nikitin.models.centralized.BsMotFactory;
import ru.amse.nikitin.models.centralized.GraphProduceStrategy;
import ru.amse.nikitin.models.centralized.MotFactory;
import ru.amse.nikitin.models.centralized.SendMotFactory;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.random.RandomArea;
import ru.amse.nikitin.ui.gui.impl.BasicUI;

public class CentralizedRandTest {

	public static void main(String[] args) {
		
		Mot[] mots = RandomArea.getInstance().getArea(
			Const.fieldX, Const.fieldY, 30,
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
