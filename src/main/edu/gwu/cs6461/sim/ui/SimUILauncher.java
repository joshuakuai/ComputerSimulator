package edu.gwu.cs6461.sim.ui;

import javax.swing.JFrame;
import org.apache.log4j.Logger;

/**
 * 
 * @author marcoyeung
 * 
 */
public class SimUILauncher {
	private final static Logger logger = Logger.getLogger(SimUILauncher.class);
	private final static Logger simConsole = Logger
			.getLogger("simulator.console");

	private void createControlPanel() {

		MainSimFrame mFrame = new MainSimFrame();

		mFrame.init();
		mFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mFrame.setSize(800, 700);
		mFrame.setVisible(true);

	}

	public static void main(String[] args) {
		logger.debug("Launching computer simulator");

		new SimUILauncher().createControlPanel();

	}
}
