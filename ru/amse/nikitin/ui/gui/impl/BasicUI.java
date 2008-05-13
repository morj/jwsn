package ru.amse.nikitin.ui.gui.impl;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.util.concurrent.TimeUnit;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
// import javax.swing.UIManager; import javax.swing.UnsupportedLookAndFeelException;

import ru.amse.nikitin.sensnet.impl.SensingChannel;
import ru.amse.nikitin.sensnet.impl.Wireless;
import ru.amse.nikitin.simulator.impl.Dispatcher;
import ru.amse.nikitin.ui.gui.Const;
import ru.amse.nikitin.ui.gui.IPropertyChangeListener;

/** 
 * Simple UI implementation
 * 
 * @author Pavel A. Nikitin
 *
 */
public class BasicUI {

	/** auxilliary frame creator */
	protected static JFrame createMainFrame () {
		JFrame mainFrame = new JFrame ("Random sensnet test");
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(Const.FRAME_WIDTH, Const.FRAME_HEIGHT);
		
		// LayoutManager layout = new GridLayout(2, 1, 1, 1);
		LayoutManager layout = new BorderLayout();
		mainFrame.setLayout(layout);
		
		return mainFrame;
	}
	
	/** UI */
	public static void createUI() {
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
		disp.addMessageFilter(SensingChannel.getInstance());
		DisplayComponent dispc = new DisplayComponent(disp, logTextArea);
		
		JScrollPane logPane = new JScrollPane(logTextArea);
		logPane.setWheelScrollingEnabled(true);
		logPane.setBorder(BorderFactory.createTitledBorder("Log"));
		
		Settings.getInstance().addPropertyChangeListener(new IPropertyChangeListener () {
			public void propertyChanged(String name, String newValue) {
				System.out.println("name: " + name + ", value: " + newValue);
			}
        });

		// JPanel opPanel = new JPanel();
		JToolBar opPanel = new JToolBar("Actions");
		MessagesProgressBar m = new MessagesProgressBar(opPanel);
		Action runAction  = ActionKit.createRunSimulationAction (dispc,
				Const.SIM_RATE, TimeUnit.MILLISECONDS);
		Action stepAction = ActionKit.createStepSimulationAction(dispc);
		Action runnAction = ActionKit.createLongRunSimulationAction(dispc, m,
				Const.SIM_RATE, TimeUnit.MILLISECONDS);
		// Action stopAction = ActionKit.createStopSimulationAction(dispc);
		
		JPanel dispPanel = new JPanel(new BorderLayout());
        dispPanel.add(dispc);
        JScrollPane pane = new JScrollPane(dispPanel);
        pane.setBorder(BorderFactory.createTitledBorder("Simulated network"));
       
		JButton runButton = new JButton(runAction);
		JButton runnButton = new JButton(runnAction);
		JButton stepButton = new JButton(stepAction);
		// JButton stopButton = new JButton(stopAction);

	    opPanel.setLayout(new BoxLayout(opPanel, BoxLayout.X_AXIS));
	    opPanel.add(runButton);
	    opPanel.add(runnButton);
        opPanel.add(stepButton);
        opPanel.add(SettingsUtil.getInstance().getCheckBox("Grid", false));
        // opPanel.add(SettingsUtil.getInstance().getCheckBox("Running"));
        opPanel.add(SettingsUtil.getInstance().getCheckBox("Red arrows", true));
        opPanel.addSeparator();
        opPanel.add(m);
        // opPanel.add(stopButton);
        pane.setBorder(BorderFactory.createTitledBorder("Process control"));
 
        /* ToolBox tools = new ToolBox(dispc);
        JPanel aaaPanel = new JPanel();
        // aaaPanel.setBorder(new LineBorder(Color.DARK_GRAY));
        aaaPanel.setBorder(BorderFactory.createTitledBorder("Tools"));
        // aaaPanel.setBorder(BorderFactory.createTitledBorder("*"));
        aaaPanel.add(tools.getJList(), BorderLayout.NORTH); */
        
        JPanel simPanel = new JPanel(new BorderLayout());
        simPanel.add(opPanel, BorderLayout.NORTH);
        // simPanel.add(aaaPanel, BorderLayout.WEST);
        simPanel.add(pane);
        
        appFrame.add(logPane, BorderLayout.SOUTH);
        appFrame.add(simPanel);
		
		// for (int i = 0; i < mots.length; i++) {
		// 	disp.addActiveObjectListener(mots[i]);
		//}
		
		// IGraph<Integer> g = s.produceGraph(mots);
		// g.solvePaths(bsIndex);
		// disp.setTopology(g);
		
        appFrame.setVisible (true); // show frame
	}
}
