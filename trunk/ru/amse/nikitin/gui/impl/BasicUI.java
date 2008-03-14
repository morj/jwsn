package ru.amse.nikitin.gui.impl;

import javax.swing.BoxLayout;
import java.awt.Font;
import java.awt.BorderLayout;
// import java.awt.Color;
import java.awt.LayoutManager;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import ru.amse.nikitin.gui.Const;
import ru.amse.nikitin.activeobj.impl.Dispatcher;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.IGraphProduceStrategy;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.Wireless;

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
		mainFrame.setSize(Const.FRAME_WIDTH, Const.FRAME_HEIGHT);
		
		// LayoutManager layout = new GridLayout(2, 1, 1, 1);
		LayoutManager layout = new BorderLayout();
		mainFrame.setLayout(layout);
		
		return mainFrame;
	}
	
	/** UI frame creator */
	public static JFrame createUIFrame(Mot[] mots, IGraphProduceStrategy s, int bsIndex) {
		/* try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException cnfe) {
		} catch (InstantiationException ie) {
		} catch (UnsupportedLookAndFeelException ulafe) {
		} catch (IllegalAccessException iae) {
		} /**/
		
		JFrame appFrame = createMainFrame();
		JTextArea logTextArea = new JTextArea(11, 1);
		logTextArea.setFont(new Font("Arial", Font.PLAIN, 11));
		Dispatcher disp = Dispatcher.getInstance(); 
		disp.addMessageFilter(Wireless.getInstance());
		DisplayComponent dispc = new DisplayComponent(disp, logTextArea);
		
		JScrollPane logPane = new JScrollPane(logTextArea);
		logPane.setWheelScrollingEnabled(true);
		logPane.setBorder(BorderFactory.createTitledBorder("Log"));
		
		Action runAction  = ActionKit.createRunSimulationAction (dispc,
			500, TimeUnit.MILLISECONDS);
		Action stepAction = ActionKit.createStepSimulationAction(dispc);
		// Action stopAction = ActionKit.createStopSimulationAction(dispc);
		
		JPanel dispPanel = new JPanel(new BorderLayout());
        dispPanel.add(dispc);
        dispPanel.setBorder(BorderFactory.createTitledBorder("Simulated network"));
       
		JButton runButton = new JButton(runAction);
		JButton stepButton = new JButton(stepAction);
		// JButton stopButton = new JButton(stopAction);

	    JPanel opPanel = new JPanel();
	    opPanel.setLayout(new BoxLayout(opPanel, BoxLayout.X_AXIS));
	    opPanel.add(runButton);
        opPanel.add(stepButton);
        // opPanel.add(stopButton);
        opPanel.setBorder(BorderFactory.createTitledBorder("Process control"));
 
        ToolBox tools = new ToolBox(dispc);
        JPanel aaaPanel = new JPanel();
        // aaaPanel.setBorder(new LineBorder(Color.DARK_GRAY));
        aaaPanel.setBorder(BorderFactory.createTitledBorder("Tools"));
        // aaaPanel.setBorder(BorderFactory.createTitledBorder("*"));
        aaaPanel.add(tools.getJList(), BorderLayout.NORTH);
        
        JPanel simPanel = new JPanel(new BorderLayout());
        simPanel.add(opPanel, BorderLayout.NORTH);
        simPanel.add(aaaPanel, BorderLayout.WEST);
        simPanel.add(dispPanel);
        
        appFrame.add(logPane, BorderLayout.SOUTH);
        appFrame.add(simPanel);
		
		for (int i = 0; i < mots.length; i++) {
			disp.addActiveObjectListener(mots[i]);
		}
		
		IGraph<Integer> g = s.produceGraph(mots);
		
		g.solvePaths(bsIndex);
		disp.setTopology(g);
		
		return appFrame;
	}
}
