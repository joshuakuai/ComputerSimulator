package edu.gwu.cs6461.sim.ui;

import java.awt.Color;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

/**
 * 
 * @author marcoyeung
 *
 */
public class MainSimFrame extends JFrame {
	
	private final static Logger logger = Logger.getLogger(MainSimFrame.class);

	private JPanel createPanel() {
		
//		JPanel panel = new JPanel(new MigLayout());

		MigLayout layout = new MigLayout(
			     new LC().fillX(),
				 new AC().align("right").gap("rel").grow(1).fill(),
				 new AC().gap("1"));

			JPanel panel = new JPanel(layout);

			panel.add(new JLabel("PC:"));
			panel.add(new JTextField(""),            new CC().wrap());
			panel.add(new JLabel("IR:"));
			panel.add(new JTextField(""));
	    
	    
//	    panel.add(new JScrollPane(new JTextArea(200,100)), "wrap,grow");
		
	    panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
		return panel;
	}
	
	public MainSimFrame() throws HeadlessException {
//		setLayout(new MigLayout());
		
//		add(new JLabel("Test")) ;
//		add(new JButton("Start"));
		
		add(createPanel());
		
		JCheckBox boldJCheckBox = new JCheckBox( "Bold" );
		
	}

	
		
}
