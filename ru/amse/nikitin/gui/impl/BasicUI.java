package ru.amse.nikitin.gui.impl;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.util.concurrent.TimeUnit;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ru.amse.nikitin.gui.Const;
import ru.amse.nikitin.activeobj.impl.Dispatcher;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.IGraphProduceStrategy;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.Radio;

/** 
 * Simple UI implementation
 * 
 * @author Pavel A. Nikitin
 *
 */
public class BasicUI {

	/** auxilliary frame creator */
	private static JFrame createMainFrame () {
		JFrame mainFrame = new JFrame ("Random sensnet test");
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(Const.frameWidth, Const.frameHeight);
		
		// LayoutManager layout = new GridLayout(2, 1, 1, 1);
		LayoutManager layout = new BorderLayout();
		mainFrame.setLayout(layout);
		
		return mainFrame;
	}
	
	/** UI frame creator */
	public static JFrame createUIFrame(Mot[] mots, IGraphProduceStrategy s, int bsIndex) {		
		JFrame appFrame = createMainFrame();
		JTextArea logTextArea = new JTextArea("", 11, 1);
		Dispatcher disp = Dispatcher.getInstance(); 
		disp.setMessageFilter(Radio.getInstance());
		DisplayComponent dispc = new DisplayComponent(disp, logTextArea);
		JScrollPane logPane = new JScrollPane(logTextArea);
		logPane.setWheelScrollingEnabled(true);
		
		/* if (centralized) {
			mots = RandomArea.getInstance().getArea(
				1024, 480, 30,
				ru.amse.nikitin.centralized.SendMotFactory.getInstance(),
				ru.amse.nikitin.centralized.MotFactory.getInstance(),
				ru.amse.nikitin.centralized.BsMotFactory.getInstance(),
				Const.bsPower
			);
		} else {
			mots = RandomArea.getInstance().getArea(
				1024, 480, 30,
				ru.amse.nikitin.aloha.SendMotFactory.getInstance(),
				ru.amse.nikitin.aloha.MotFactory.getInstance(),
				ru.amse.nikitin.aloha.BsMotFactory.getInstance(),
				RandomArea.commonMotPower
			);
		} */
		
		Action runAction  = ActionKit.createRunSimulationAction (dispc,
			500, TimeUnit.MILLISECONDS);
		Action stepAction = ActionKit.createStepSimulationAction(dispc);
		Action stopAction = ActionKit.createStopSimulationAction(dispc);
		
		appFrame.add(dispc); // add another panel to frame
		JButton runButton = new JButton(runAction);
		JButton stepButton = new JButton(stepAction);
		JButton stopButton = new JButton(stopAction);

	    JPanel opPanel = new JPanel();
	    opPanel.add(runButton);
        opPanel.add(stepButton);
        opPanel.add(stopButton);
        
        JPanel simPanel = new JPanel(new BorderLayout());
        simPanel.add(opPanel, BorderLayout.SOUTH);
        simPanel.add(dispc);
        
        appFrame.getContentPane().add(logPane, BorderLayout.SOUTH);
        appFrame.getContentPane().add(simPanel);
		
		for (int i = 0; i < mots.length; i++) {
			disp.addActiveObjectListener(mots[i]);
		}
		
		IGraph<Integer> g = s.produceGraph(mots);
		
		g.solvePaths(bsIndex);
		disp.setTopology(g);
		
		return appFrame;
	}
}
