package edu.gwu.cs6461.sim.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import edu.gwu.cs6461.sim.util.GriddedPanel;

public class EngConsoleFrame extends JFrame {


	private MainSimFrame mainFrame;
	private String title;

	public EngConsoleFrame(MainSimFrame mainFrame,String title) throws HeadlessException {
		super(title);
		setSize(400,300);
		this.mainFrame = mainFrame;
		this.title = title;
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				promptExit();
			}
		});
		
		
		Dimension di = mainFrame.getSize();
		di.getWidth();
		Dimension myDi = getSize();
		setLocation((int)(di.getWidth()-myDi.getWidth()), 0);
		
	}
	
	/**Create general register panel*/
	private JPanel createGeneralRPanel() {
		GriddedPanel gPanel = new GriddedPanel();
		gPanel.setBorder(new TitledBorder(new EtchedBorder(), "General"));
//
//		gPanel.addComponent(lblR0, 0, 0);
//		gPanel.addComponent(txtR0, 0, 1);
//		gPanel.addComponent(lblR1, 1, 0);
//		gPanel.addComponent(txtR1, 1, 1);
//		gPanel.addComponent(lblR2, 2, 0);
//		gPanel.addComponent(txtR2, 2, 1);
//		gPanel.addComponent(lblR3, 3, 0);
//		gPanel.addComponent(txtR3, 3, 1);

		FlowLayout fl = new FlowLayout();
		JPanel wrap = new JPanel();
		wrap.setLayout(fl);
		wrap.add(gPanel);
		fl.setAlignment(FlowLayout.LEFT);
		return wrap;

	}
	
	
	/**message prompt for user to confirm exiting simulator*/
	private void promptExit() {
		String ObjButtons[] = { "Yes", "No" };
		int PromptResult = JOptionPane.showOptionDialog(
				EngConsoleFrame.this, "Are you sure want to close this console ?",
				"Simualtor", JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, ObjButtons,
				ObjButtons[1]);
		if (PromptResult == 0) {
			mainFrame.clearConsole();
			this.dispose();
		}
	}
	
	
}
