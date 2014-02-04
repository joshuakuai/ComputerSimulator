package edu.gwu.cs6461.sim.ui;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.sim.util.PropertiesLoader;
import edu.gwu.cs6461.sim.util.PropertiesParser;

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
		PropertiesParser prop = PropertiesLoader.getPropertyInstance();

		int width = prop.getIntProperty("sim.gui.width",850);
		int heigh = prop.getIntProperty("sim.gui.heigh",700);
		String title = prop.getStringProperty("sim.gui.windowtitle");
		
		MainSimFrame mFrame = new MainSimFrame(title);
		mFrame.init();
		mFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mFrame.setSize(width, heigh);
		mFrame.setVisible(true);

	}

	public static void main(String[] args) {
		logger.debug("Launching computer simulator");

		new SimUILauncher().createControlPanel();

	}
}
