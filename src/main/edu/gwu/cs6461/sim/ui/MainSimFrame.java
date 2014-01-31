package edu.gwu.cs6461.sim.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.sim.common.RegisterName;
import edu.gwu.cs6461.sim.util.GriddedPanel;
import edu.gwu.cs6461.sim.util.TextAreaAppender;

/**
 * 
 * @author marcoyeung
 * 
 */
public class MainSimFrame extends JFrame {
	
	private static final int HIGHESTBIT = 19;
	private static final int LOWESTBIT = 0;
	

	private final static Logger logger = Logger.getLogger(MainSimFrame.class);
	private final static Logger simConsole = Logger.getLogger("simulator.console");

	private JCheckBox[] chkBinData  = new JCheckBox[20]; 
	private JRadioButton[] radBinData  = new JRadioButton[20];
	private JLabel[] lblBinPosInfo = new JLabel[20];
	private Dimension shortField = new Dimension( 100, 50 );
	
	
	private JLabel lblR0 = new JLabel(RegisterName.R0.getVal());
	private JLabel lblR1 = new JLabel(RegisterName.R1.getVal());
	private JLabel lblR2 = new JLabel(RegisterName.R2.getVal());
	private JLabel lblR3 = new JLabel(RegisterName.R3.getVal());
	
	private JTextField txtR0 = new JTextField(20);
	private JTextField txtR1 = new JTextField(20);
	private JTextField txtR2 = new JTextField(20);
	private JTextField txtR3 = new JTextField(20);
	
	private JLabel lblX1 = new JLabel(RegisterName.X1.getVal());
	private JLabel lblX2 = new JLabel(RegisterName.X2.getVal());
	private JLabel lblX3 = new JLabel(RegisterName.X3.getVal());

	private JTextField txtX1 = new JTextField(15);
	private JTextField txtX2 = new JTextField(15);
	private JTextField txtX3 = new JTextField(15);

	private JLabel lblMAR = new JLabel(RegisterName.MAR.getVal());
	private JLabel lblMBR = new JLabel(RegisterName.MBR.getVal());
	private JLabel lblMSR = new JLabel(RegisterName.MSR.getVal());
	private JLabel lblMFR = new JLabel(RegisterName.MFR.getVal());
	
	private JTextField txtMAR = new JTextField(13);
	private JTextField txtMBR = new JTextField(20);
	private JTextField txtMSR = new JTextField(20);
	private JTextField txtMFR = new JTextField(20);
	
	private JLabel lblCC = new JLabel(RegisterName.CC.getVal());  
	private JLabel lblIR = new JLabel(RegisterName.IR.getVal());
	private JLabel lblPC = new JLabel(RegisterName.PC.getVal());

	private JTextField txtCC = new JTextField(4);    //condition code //UNDERFLOW or 
	private JTextField txtIR = new JTextField(20);  //current instruction 
	private JTextField txtPC = new JTextField(13);  //address of next instruction
	
	private JButton btnHalt = new JButton("Halt");
	private JButton btnRun = new JButton("Run");
	private JButton btnSingleInstr = new JButton("Single Instr.");
	private JButton btnSingleStep = new JButton("Single Step");
	
	private JButton btnIPL = new JButton("IPL");
	private JButton btnTerminate = new JButton("Terminate");
	private JButton btnExit = new JButton("Exit");
	
	private JComboBox<String> cboSwithOptions = new JComboBox<String>();
	private JButton btnReset = new JButton("Reset");
	private JButton btnLoad = new JButton("Load");
	
	private JLabel lblWinStatus = new JLabel(" "); 
	
	private JTextArea txtConsoleText = new JTextArea();
	private int mainSwitchIdx = 0;
	
	
	public MainSimFrame() {
		// setLayout(new MigLayout());

		RegisterName[] names = RegisterName.values();
		
		List<String> tmp = new ArrayList<String>();

		int i=0;
		for (RegisterName n : names) {
			if (n.isOperator()) {
				tmp.add(n.getVal());
				if (n == RegisterName.IR) {
					mainSwitchIdx = i;
				}
				i++;
			}
		}//

		String[] reg = tmp.toArray(new String[tmp.size()]);
		
		cboSwithOptions = new JComboBox<String>(reg);
		cboSwithOptions.setSelectedIndex(mainSwitchIdx);
		
		BtnActListener btnAct = new BtnActListener();
		
		btnReset.addActionListener(btnAct);
		btnLoad.addActionListener(btnAct);
		btnIPL.addActionListener(btnAct);
		btnTerminate.addActionListener(btnAct);
		
		btnTerminate.setEnabled(false);
		
		SimActListener simAct = new SimActListener();
		btnSingleStep.addActionListener(simAct);
		btnHalt.addActionListener(simAct);
		btnRun.addActionListener(simAct);
		btnSingleInstr.addActionListener(simAct);
		

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TextAreaAppender.setTextArea(txtConsoleText);
			}
		});
		
	}
	
	public void init(){
		
		JPanel regSwPanel = new JPanel();
		JPanel regPanel = new JPanel();
		regPanel.setLayout(new BoxLayout(regPanel, BoxLayout.X_AXIS));
//		regPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		regPanel.add(createGeneralRPanel());
		regPanel.add(createIndexRPanel());
		regPanel.add(createMiscRPanel());
		
		regSwPanel.setLayout(new GridBagLayout());
		GridBagConstraints c =new GridBagConstraints();
		
		c.gridx=0;
		c.gridy=0;
		c.gridwidth = 3;
		c.anchor =  GridBagConstraints.LAST_LINE_START;
		regSwPanel.add(createMainCtrlPanel(),c);
		
		c.gridx=0;
		c.gridy=1;
		c.gridwidth = 3;
		regSwPanel.add(regPanel,c);
		
		c.gridx=0;
		c.gridy=2;
		c.gridwidth = 2;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
//		c.anchor = GridBagConstraints.FIRST_LINE_END;
		regSwPanel.add(createSwitchPanel(0, 19),c);
		c.gridx=2;
		c.gridy=2;
		c.gridwidth = 1;
		c.weightx = 0;
		c.weighty = 1;
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
//		c.fill = GridBagConstraints.BOTH;
		regSwPanel.add(createControlPanel(), c);
		
//		c.gridx=0;
//		c.gridy=2;
//		c.gridwidth = 3;
//		regSwPanel.add(createConsolePanel(), c);
		
//		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(regSwPanel, BorderLayout.NORTH);
		add(createConsolePanel(), BorderLayout.CENTER);
		add(lblWinStatus,BorderLayout.SOUTH);
		

		logger.debug(getLayout());
	}

	
	private JPanel createMiscRPanel(){
		GriddedPanel gPanel = new GriddedPanel();
		gPanel.setBorder(new TitledBorder(new EtchedBorder(), "Registers"));
		gPanel.addComponent(lblMAR,0,0);
		gPanel.addComponent(txtMAR,0,1);
		gPanel.addComponent(lblMBR,1,0);
		gPanel.addComponent(txtMBR,1,1);
		gPanel.addComponent(lblMSR,2,0);
		gPanel.addComponent(txtMSR,2,1);
		gPanel.addComponent(lblMFR,3,0);
		gPanel.addComponent(txtMFR,3,1);
		gPanel.addComponent(lblCC,4,0);
		gPanel.addComponent(txtCC,4,1);
		gPanel.addComponent(lblIR,5,0);
		gPanel.addComponent(txtIR,5,1);
		gPanel.addComponent(lblPC,6,0);
		gPanel.addComponent(txtPC,6,1);
		
//		gPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.blue)));
		
		FlowLayout fl = new FlowLayout();
		JPanel wrap = new JPanel();
		wrap.setLayout(fl);
		wrap.add(gPanel);
		fl.setAlignment(FlowLayout.LEFT);
		return wrap;
	}
	private JPanel createIndexRPanel(){
		GriddedPanel gPanel = new GriddedPanel();
		gPanel.setBorder(new TitledBorder(new EtchedBorder(), "Index"));
		gPanel.addComponent(lblX1,0,0);
		gPanel.addComponent(txtX1,0,1);
		gPanel.addComponent(lblX2,1,0);
		gPanel.addComponent(txtX2,1,1);
		gPanel.addComponent(lblX3,2,0);
		gPanel.addComponent(txtX3,2,1);
//		gPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.blue)));
		
		FlowLayout fl = new FlowLayout();
		JPanel wrap = new JPanel();
		wrap.setLayout(fl);
		wrap.add(gPanel);
		fl.setAlignment(FlowLayout.LEFT);
		return wrap;
	}

	private JPanel createControlPanel(){
		GriddedPanel gPanel = new GriddedPanel();
		gPanel.setBorder(new TitledBorder(new EtchedBorder(), 
			      "Control Panel"));
		
		gPanel.addComponent(btnSingleStep,0,0);
		
		
		return gPanel;
	}
	
	private JPanel createConsolePanel(){
		
		txtConsoleText.setFont( new Font("consolas", Font.PLAIN, 13));
		txtConsoleText.setEditable(false);
		JScrollPane scroll = new JScrollPane( txtConsoleText );
//		scroll.setPreferredSize(new Dimension( 240, 180 ) );
		
		JPanel gPanel = new JPanel();
		gPanel.setBorder(new TitledBorder(new EtchedBorder(), "Console"));
		gPanel.setLayout(new BorderLayout());
		gPanel.add(scroll, BorderLayout.CENTER);
		
		return gPanel;
		
	}
	
	private JPanel createMainCtrlPanel(){
		
		JPanel btnPanel = new JPanel(new GridLayout(1,3,5,5));
		btnPanel.add(btnIPL);
		btnPanel.add(btnTerminate);
		btnPanel.add(btnExit);
		btnExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String ObjButtons[] = {"Yes","No"};
				int PromptResult = JOptionPane.showOptionDialog(MainSimFrame.this,
						"Are you sure you want to exit?", "Simualtor",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, ObjButtons,
						ObjButtons[1]);
				if (PromptResult == 0) {
					System.exit(0);
				}
			}
		});
		
		FlowLayout fl = new FlowLayout();
		JPanel wrap = new JPanel(fl);
		fl.setAlignment(FlowLayout.LEFT);
		wrap.add(btnPanel);
		
		return wrap;
	}
	private JPanel createGeneralRPanel(){
	    GriddedPanel gPanel = new GriddedPanel();
	    gPanel.setBorder(new TitledBorder(new EtchedBorder(), "General"));
	    
		gPanel.addComponent(lblR0,0,0);
		gPanel.addComponent(txtR0,0,1);
		gPanel.addComponent(lblR1,1,0);
		gPanel.addComponent(txtR1,1,1);
		gPanel.addComponent(lblR2,2,0);
		gPanel.addComponent(txtR2,2,1);
		gPanel.addComponent(lblR3,3,0);
		gPanel.addComponent(txtR3,3,1);
//		gPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.blue)));
		
		FlowLayout fl = new FlowLayout();
		JPanel wrap = new JPanel();
		wrap.setLayout(fl);
		wrap.add(gPanel);
		fl.setAlignment(FlowLayout.LEFT);
		return wrap;
		
	}

	
	private JPanel createBinPanel(int start, int end) {
		JPanel pBinPanel = new JPanel();
		
//		pBinPanel.setLayout(new GridBagLayout());
		pBinPanel.setLayout(new BoxLayout(pBinPanel,BoxLayout.X_AXIS));
		
		JPanel tmp = null;
		for (int i = start; i <= end; i++) {
			tmp = new JPanel();
			tmp.setLayout(new GridLayout(2,1,0,0));
			
			lblBinPosInfo[i] = new JLabel(String.valueOf(i));
			lblBinPosInfo[i].setHorizontalAlignment(SwingConstants.CENTER);
			lblBinPosInfo[i].setAlignmentY(RIGHT_ALIGNMENT);
//			lblBinPosInfo[i].setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.GREEN)));
			tmp.add(lblBinPosInfo[i]);
			
			radBinData[i] = new JRadioButton();
//			radBinData[i].setBorder(BorderFactory.createEmptyBorder());
			tmp.add(radBinData[i]);
//			tmp.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.blue)));
			pBinPanel.add(tmp);
		}
		tmp.setBorder(BorderFactory.createEmptyBorder());
		
//		pBinPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.blue)));
		
		return pBinPanel; 
	}

	private JPanel createSwitchPanel(int start, int end){
		//Button only
		JPanel btnPanel = new JPanel();
		
		btnPanel.add(cboSwithOptions);
		cboSwithOptions.addActionListener(new SwitchComboActionListener());
		btnPanel.add(btnReset);
		btnPanel.add(btnLoad);
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.RIGHT);
		btnPanel.setLayout(fl);
		//Button only 		
		
		JPanel wrapPanel = new JPanel();
		wrapPanel.setBorder(new TitledBorder(new EtchedBorder(), "Switches"));
//		wrapPanel.setLayout(new GridLayout(2,1,0,0));
		wrapPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c= new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		wrapPanel.add(createBinPanel(start, end),c);
		c.gridy = 1;
		wrapPanel.add(btnPanel,c);
		
		return wrapPanel;
	}
	

	private class SimActListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			Component parent = (Component) e.getSource();
			
			logger.debug(parent);
		}
		
	}
	
	private void loadToControl(String dest, String val){
		
		RegisterName dName = RegisterName.fromName(dest);
		
		if (dName == RegisterName.R0) {
			txtR0.setText(val);
		} else if (dName == RegisterName.R1) {
			txtR1.setText(val);
		} else if (dName == RegisterName.R2) {
			txtR2.setText(val);
		} else if (dName == RegisterName.R3) {
			txtR3.setText(val);
		} else if (dName == RegisterName.X1) {
			txtX1.setText(val);
		} else if (dName == RegisterName.X2) {
			txtX2.setText(val);
		} else if (dName == RegisterName.X3) {
			txtX3.setText(val);
		} else if (dName == RegisterName.MAR) {
			txtMAR.setText(val);
		} else if (dName == RegisterName.MBR) {
			txtMBR.setText(val);
		} else if (dName == RegisterName.PC) {
			txtPC.setText(val);
		} else if (dName == RegisterName.IR) {
			txtIR.setText(val);
		}
	}
	
	private class BtnActListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			Component parent = (Component) e.getSource();
			
			if (parent == btnLoad) {
				String sel = (String)cboSwithOptions.getSelectedItem();
				
				int numBit = RegisterName.fromName(sel).getBit();
				
				String res="";
				for (int i = 0; i < numBit; i++) {
					res += radBinData[i].isSelected()? "1":"0";
				}
				logger.debug("value got: " +res);
				loadToControl(sel, res);
				
			} else if (parent == btnReset) {
				String sel = (String)cboSwithOptions.getSelectedItem();
				int numBit = RegisterName.fromName(sel).getBit();
				
				resetSwitches(0,numBit-1,false);
			}else if (parent == btnIPL) {
				
					
			     /*   SwingUtilities.invokeLater(new Runnable() {
			            public void run() {
			                try {
			                	simConsole.info("Simulator starting....");
			                	resetSimulator();
								Thread.sleep(2000);
							} catch (Exception e) {
								e.printStackTrace();
							}
			            }
			        });*/
				simConsole.info("Simulator started....");
				resetMainCtrlBtn(true);
				
			}else if (parent == btnTerminate) {
				String ObjButtons[] = {"Yes","No"};
				int PromptResult = JOptionPane.showOptionDialog(MainSimFrame.this,
						"Are you sure you terminate simulator? All value will be reset.", "Termination",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, ObjButtons,
						ObjButtons[1]);
				if (PromptResult == 0) {
					resetSimulator();
					simConsole.info("Simulator terminated....");
					resetMainCtrlBtn(false);
					
				}
				
			}
			
			
			logger.debug(parent);
		}
		
	}
	
	private void resetMainCtrlBtn(boolean isStart){
		
		if (isStart) {
			btnTerminate.setEnabled(true);
			btnIPL.setEnabled(false);
		} else {
			btnTerminate.setEnabled(false);
			btnIPL.setEnabled(true);
		}
		 
	}
	
	private void resetSwitches(int start, int end,boolean b){
		for (int i = start; i <= end; i++) {
			if (radBinData[i]!=null) {
				radBinData[i].setSelected(b);
			}
		}
	}

	private void resetSimulator() {
		txtR0.setText("");
		txtR1.setText("");
		txtR2.setText("");
		txtR3.setText("");
		txtX1.setText("");
		txtX2.setText("");
		txtX3.setText("");
		txtMAR.setText("");
		txtMBR.setText("");
		txtMSR.setText("");
		txtMFR.setText("");
		txtCC.setText("");
		txtIR.setText("");
		txtPC.setText("");
		cboSwithOptions.setSelectedIndex(mainSwitchIdx);
		resetSwitches(LOWESTBIT,  HIGHESTBIT, false);
		
	}

	private void maskSwitches(int start, int end,boolean b){
		
		for (int i = start; i <= end; i++) {
			if (radBinData[i]!=null) {
				radBinData[i].setEnabled(b);
				lblBinPosInfo[i].setVisible(b);
			}
		}
	}
	
	private class SwitchComboActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String selected = (String)((JComboBox)e.getSource()).getSelectedItem();
			RegisterName reg =RegisterName.fromName(selected); 
			if (reg!= RegisterName.NOTEXIST) {
				int numBit = reg.getBit();
				maskSwitches(0, numBit-1, true);
				maskSwitches(numBit, 19, false);
			}
			
			logger.debug("selcted :  " + selected);
			
		}
		
	}
	
}

