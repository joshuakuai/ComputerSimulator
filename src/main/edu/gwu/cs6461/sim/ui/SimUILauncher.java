package edu.gwu.cs6461.sim.ui;

import java.awt.Panel;

import javax.swing.JFrame;

import org.apache.log4j.Logger;


/**
 * 
 * @author marcoyeung
 *
 */
public class SimUILauncher {
	private final static Logger logger = Logger.getLogger(SimUILauncher.class);

	
	
	private Panel createControlPanel(){

		MainSimFrame  mFrame = new MainSimFrame();
		
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mFrame.setSize(500,400);
		mFrame.setVisible(true);
		
		
		
		return new Panel();
	}
	
	
	public static void main(String[] args) {
		logger.debug("Launching computer simulator");
		
		new SimUILauncher().createControlPanel();
		
		
		
	}
}
