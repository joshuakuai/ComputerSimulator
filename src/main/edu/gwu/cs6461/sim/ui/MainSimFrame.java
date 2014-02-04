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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.logic.CPUController;
import edu.gwu.cs6461.logic.Memory;
import edu.gwu.cs6461.sim.bridge.HardwareData;
import edu.gwu.cs6461.sim.bridge.Observer;
import edu.gwu.cs6461.sim.common.HardwarePart;
import edu.gwu.cs6461.sim.util.GriddedPanel;
import edu.gwu.cs6461.sim.util.TextAreaAppender;

/**
 * 
 * @author marcoyeung
 * 
 */
public class MainSimFrame extends JFrame implements Observer {

	private static final int HIGHESTBIT = 19;
	private static final int LOWESTBIT = 0;

	private final static Logger logger = Logger.getLogger(MainSimFrame.class);
	private final static Logger simConsole = Logger
			.getLogger("simulator.console");

	private JRadioButton[] radBinData = new JRadioButton[20];
	private JLabel[] lblBinPosInfo = new JLabel[20];
	private Dimension shortField = new Dimension(100, 50);

	private JLabel lblR0 = new JLabel(HardwarePart.R0.getVal());
	private JLabel lblR1 = new JLabel(HardwarePart.R1.getVal());
	private JLabel lblR2 = new JLabel(HardwarePart.R2.getVal());
	private JLabel lblR3 = new JLabel(HardwarePart.R3.getVal());

	private JTextField txtR0 = new JTextField(20);
	private JTextField txtR1 = new JTextField(20);
	private JTextField txtR2 = new JTextField(20);
	private JTextField txtR3 = new JTextField(20);

	private JLabel lblX1 = new JLabel(HardwarePart.X1.getVal());
	private JLabel lblX2 = new JLabel(HardwarePart.X2.getVal());
	private JLabel lblX3 = new JLabel(HardwarePart.X3.getVal());

	private JTextField txtX1 = new JTextField(15);
	private JTextField txtX2 = new JTextField(15);
	private JTextField txtX3 = new JTextField(15);

	private JLabel lblMAR = new JLabel(HardwarePart.MAR.getVal());
	private JLabel lblMBR = new JLabel(HardwarePart.MBR.getVal());
	private JLabel lblMSR = new JLabel(HardwarePart.MSR.getVal());
	private JLabel lblMFR = new JLabel(HardwarePart.MFR.getVal());

	private JTextField txtMAR = new JTextField(13);
	private JTextField txtMBR = new JTextField(20);
	private JTextField txtMSR = new JTextField(20);
	private JTextField txtMFR = new JTextField(20);

	private JLabel lblCC = new JLabel(HardwarePart.CC.getVal());
	private JLabel lblIR = new JLabel(HardwarePart.IR.getVal());
	private JLabel lblPC = new JLabel(HardwarePart.PC.getVal());

	private JTextField txtCC = new JTextField(4); // condition code //UNDERFLOW
													// or
	private JTextField txtIR = new JTextField(20); // current instruction
	private JTextField txtPC = new JTextField(13); // address of next
													// instruction

	private JButton btnHalt = new JButton("Halt");
	private JButton btnRun = new JButton("Run");
	private JButton btnSingleInstr = new JButton("Single Instr.");
	private JButton btnSingleStep = new JButton("Single Step");

	private JButton btnIPL = new JButton("IPL");
	private JButton btnTerminate = new JButton("Terminate");

	private JComboBox<String> cboSwithOptions = new JComboBox<String>();
	private JButton btnReset = new JButton("Reset");
	private JButton btnLoad = new JButton("Deposit");

	// set memory
	private JLabel lblMemAddress = new JLabel("Address:");
	private JTextField txtMemAdd = new JTextField(4);

	private JTextArea txtConsoleText = new JTextArea();
	private DefaultListModel<String> lstModel = new DefaultListModel<String>();
	private JList<String> lstMemory = new JList<String>(lstModel);

	/**
	 * The first switch, in ComboBox ,to be allow to edit by the tester this is
	 * will be default IR. Terminate and reset button will reset back to IR
	 */
	private int mainSwitchIdx = 0;

	/**
	 * Business logic objects
	 */
	private CPUController cpuController = CPUController.shareInstance();

	/**
	 * Constructor:init GUI component; register GUi component event listeners
	 */
	public MainSimFrame(String title) {
		// setLayout(new MigLayout());

		setTitle(title);

		HardwarePart[] names = HardwarePart.values();
		List<String> tmp = new ArrayList<String>();

		int i = 0;
		for (HardwarePart n : names) {
			if (n.isEditable()) {
				tmp.add(n.getVal());
				if (n == HardwarePart.IR) {
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

		lstMemory.setFont(new Font("consolas", Font.PLAIN, 13));

		JMenuItem iExit = new JMenuItem("Exit");
		iExit.setMnemonic('x');
		JMenu mFile = new JMenu("File");
		mFile.setMnemonic('F');
		mFile.add(iExit);
		iExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				InputEvent.ALT_MASK));
		JMenuBar menu = new JMenuBar();
		menu.add(mFile);
		setJMenuBar(menu);
		iExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String ObjButtons[] = { "Yes", "No" };
				int PromptResult = JOptionPane.showOptionDialog(
						MainSimFrame.this, "Are you sure you want to exit?",
						"Simualtor", JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, ObjButtons,
						ObjButtons[1]);
				if (PromptResult == 0) {
					System.exit(0);
				}
			}
		});

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				String ObjButtons[] = { "Yes", "No" };
				int PromptResult = JOptionPane.showOptionDialog(
						MainSimFrame.this, "Are you sure you want to exit?",
						"Simualtor", JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, ObjButtons,
						ObjButtons[1]);
				if (PromptResult == 0) {
					System.exit(0);
				}
			}

		});
		setResizable(false);
		setRegisterEditable(false);
		resetSimulator(false);

		setMemorySwitch(false);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TextAreaAppender.setTextArea(txtConsoleText);
			}
		});

	}

	/**
	 * Create and arrange the main window components ie all the registers are at
	 * the upper portion of window swithes and control panel are in the middle
	 * 
	 */
	public void init() {

		JPanel regSwPanel = new JPanel();
		JPanel regPanel = new JPanel();
		regPanel.setLayout(new BoxLayout(regPanel, BoxLayout.X_AXIS));
		// regPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		regPanel.add(createGeneralRPanel());
		regPanel.add(createIndexRPanel());
		regPanel.add(createMiscRPanel());

		regSwPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		regSwPanel.add(createMainCtrlPanel(), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		regSwPanel.add(regPanel, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		// c.anchor = GridBagConstraints.FIRST_LINE_END;
		regSwPanel.add(createSwitchPanel(0, 19), c);
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0;
		c.weighty = 1;
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		// c.fill = GridBagConstraints.BOTH;
		regSwPanel.add(createControlPanel(), c);

		// c.gridx=0;
		// c.gridy=2;
		// c.gridwidth = 3;
		// regSwPanel.add(createConsolePanel(), c);

		// setLayout(new FlowLayout(FlowLayout.LEFT));
		add(regSwPanel, BorderLayout.NORTH);
		add(createConsolePanel(), BorderLayout.SOUTH);
		// add(lblWinStatus,BorderLayout.SOUTH);

		// memory area
		JScrollPane sMem = new JScrollPane();
		sMem.getViewport().add(lstMemory);
		JPanel jmem = new JPanel(new BorderLayout());
		jmem.add(sMem, BorderLayout.CENTER);
		jmem.setBorder(new TitledBorder(new EtchedBorder(), "Memory"));
		add(jmem, BorderLayout.CENTER);

		logger.debug(getLayout());

		Memory.shareInstance().register(this);
		cpuController.setRegisterObserver(MainSimFrame.this);
		cpuController.setMainFrame(MainSimFrame.this);
	}

	private JPanel createMiscRPanel() {
		GriddedPanel gPanel = new GriddedPanel();
		gPanel.setBorder(new TitledBorder(new EtchedBorder(), "Registers"));
		gPanel.addComponent(lblMAR, 0, 0);
		gPanel.addComponent(txtMAR, 0, 1);
		gPanel.addComponent(lblMBR, 1, 0);
		gPanel.addComponent(txtMBR, 1, 1);
		gPanel.addComponent(lblMSR, 2, 0);
		gPanel.addComponent(txtMSR, 2, 1);
		gPanel.addComponent(lblMFR, 3, 0);
		gPanel.addComponent(txtMFR, 3, 1);
		gPanel.addComponent(lblCC, 4, 0);
		gPanel.addComponent(txtCC, 4, 1);
		gPanel.addComponent(lblIR, 5, 0);
		gPanel.addComponent(txtIR, 5, 1);
		gPanel.addComponent(lblPC, 6, 0);
		gPanel.addComponent(txtPC, 6, 1);

		// gPanel.setBorder(new
		// TitledBorder(BorderFactory.createLineBorder(Color.blue)));

		FlowLayout fl = new FlowLayout();
		JPanel wrap = new JPanel();
		wrap.setLayout(fl);
		wrap.add(gPanel);
		fl.setAlignment(FlowLayout.LEFT);
		return wrap;
	}

	private JPanel createIndexRPanel() {
		GriddedPanel gPanel = new GriddedPanel();
		gPanel.setBorder(new TitledBorder(new EtchedBorder(), "Index"));
		gPanel.addComponent(lblX1, 0, 0);
		gPanel.addComponent(txtX1, 0, 1);
		gPanel.addComponent(lblX2, 1, 0);
		gPanel.addComponent(txtX2, 1, 1);
		gPanel.addComponent(lblX3, 2, 0);
		gPanel.addComponent(txtX3, 2, 1);
		// gPanel.setBorder(new
		// TitledBorder(BorderFactory.createLineBorder(Color.blue)));

		FlowLayout fl = new FlowLayout();
		JPanel wrap = new JPanel();
		wrap.setLayout(fl);
		wrap.add(gPanel);
		fl.setAlignment(FlowLayout.LEFT);
		return wrap;
	}

	private JPanel createControlPanel() {
		GriddedPanel gPanel = new GriddedPanel();
		gPanel.setBorder(new TitledBorder(new EtchedBorder(), "Control Panel"));

		gPanel.addComponent(btnSingleStep, 0, 0);
		gPanel.addFilledComponent(btnRun, 1, 0);

		return gPanel;
	}

	private JPanel createConsolePanel() {

		txtConsoleText.setFont(new Font("consolas", Font.PLAIN, 13));
		txtConsoleText.setEditable(false);
		JScrollPane scroll = new JScrollPane(txtConsoleText);
		scroll.setPreferredSize(new Dimension(240, 80));

		JPanel gPanel = new JPanel();
		gPanel.setBorder(new TitledBorder(new EtchedBorder(), "Console"));
		gPanel.setLayout(new BorderLayout());
		gPanel.add(scroll, BorderLayout.CENTER);

		return gPanel;

	}

	private JPanel createMainCtrlPanel() {

		JPanel btnPanel = new JPanel(new GridLayout(1, 3, 5, 5));
		btnPanel.add(btnIPL);
		btnPanel.add(btnTerminate);

		FlowLayout fl = new FlowLayout();
		JPanel wrap = new JPanel(fl);
		fl.setAlignment(FlowLayout.LEFT);
		wrap.add(btnPanel);

		return wrap;
	}

	private JPanel createGeneralRPanel() {
		GriddedPanel gPanel = new GriddedPanel();
		gPanel.setBorder(new TitledBorder(new EtchedBorder(), "General"));

		gPanel.addComponent(lblR0, 0, 0);
		gPanel.addComponent(txtR0, 0, 1);
		gPanel.addComponent(lblR1, 1, 0);
		gPanel.addComponent(txtR1, 1, 1);
		gPanel.addComponent(lblR2, 2, 0);
		gPanel.addComponent(txtR2, 2, 1);
		gPanel.addComponent(lblR3, 3, 0);
		gPanel.addComponent(txtR3, 3, 1);
		// gPanel.setBorder(new
		// TitledBorder(BorderFactory.createLineBorder(Color.blue)));

		FlowLayout fl = new FlowLayout();
		JPanel wrap = new JPanel();
		wrap.setLayout(fl);
		wrap.add(gPanel);
		fl.setAlignment(FlowLayout.LEFT);
		return wrap;

	}

	private JPanel createBinPanel(int start, int end) {
		JPanel pBinPanel = new JPanel();

		pBinPanel.setLayout(new BoxLayout(pBinPanel, BoxLayout.X_AXIS));
		pBinPanel.setBorder(new TitledBorder(new EtchedBorder(), "Bits"));

		// TODO
		radBinData = new JRadioButton[end + 1];
		lblBinPosInfo = new JLabel[end + 1];

		JPanel tmp = null;
		for (int i = start; i <= end; i++) {
			tmp = new JPanel();
			tmp.setLayout(new GridLayout(2, 1, 0, 0));

			lblBinPosInfo[i] = new JLabel(String.valueOf(end - i));
			lblBinPosInfo[i].setHorizontalAlignment(SwingConstants.CENTER);
			lblBinPosInfo[i].setAlignmentY(RIGHT_ALIGNMENT);
			tmp.add(lblBinPosInfo[i]);

			radBinData[i] = new JRadioButton();
			tmp.add(radBinData[i]);
			pBinPanel.add(tmp);

		}
		tmp.setBorder(BorderFactory.createEmptyBorder());

		return pBinPanel;
	}

	private JPanel createSwitchPanel(int start, int end) {
		// Button only
		JPanel btnPanel = new JPanel();
		btnPanel.add(lblMemAddress);
		btnPanel.add(txtMemAdd);
		btnPanel.add(cboSwithOptions);
		cboSwithOptions.addActionListener(new SwitchComboActionListener());
		btnPanel.add(btnReset);
		btnPanel.add(btnLoad);
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.RIGHT);
		btnPanel.setLayout(fl);
		// Button only

		JPanel wrapPanel = new JPanel();
		wrapPanel.setBorder(new TitledBorder(new EtchedBorder(), "Switches"));
		// wrapPanel.setLayout(new GridLayout(2,1,0,0));
		wrapPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		wrapPanel.add(createBinPanel(start, end), c);
		c.gridy = 1;
		wrapPanel.add(btnPanel, c);

		return wrapPanel;
	}

	private class SimActListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			Component parent = (Component) e.getSource();

			if (parent == btnRun) {
				beginCPUProcessWithModel(0);
			} else if (parent == btnSingleStep) {
				beginCPUProcessWithModel(1);
			}
		}

	}

	// Model, 0: Run 1: Single Step
	private void beginCPUProcessWithModel(int model) {
		if (model == 1 && cpuController.isAlive()
				&& cpuController.isSuspended()) {
			// For Single step model
			// If this thread is alive and is suspended, the button will
			// resume the thread
			cpuController.Resume();
			return;
		}

		// For all model
		if (!cpuController.isAlive() && !cpuController.isSuspended()) {
			// then if the the thread is dead and is not suspended,
			// recreate the cpucontroller thread and
			// set the debug model on and start the process
			CPUController.recreateCPUController();
			cpuController = CPUController.shareInstance();
			cpuController.setRegisterObserver(MainSimFrame.this);
			cpuController.setMainFrame(MainSimFrame.this);

			if (model == 1) {
				cpuController.SS.setData(1);
			}else if(model == 0){
				cpuController.SS.setData(0);
			}

			cpuController.start();
		}
		// For other situation just ignore this touch event
	}

	private void loadToControl(String dest, String... vals) {

		HardwarePart dName = HardwarePart.fromName(dest);

		String val = "";
		if (dName != HardwarePart.MEMORY) {
			val = vals[0];
		}

		if (dName == HardwarePart.R0) {
			txtR0.setText(val);
		} else if (dName == HardwarePart.R1) {
			txtR1.setText(val);
		} else if (dName == HardwarePart.R2) {
			txtR2.setText(val);
		} else if (dName == HardwarePart.R3) {
			txtR3.setText(val);
		} else if (dName == HardwarePart.X1) {
			txtX1.setText(val);
		} else if (dName == HardwarePart.X2) {
			txtX2.setText(val);
		} else if (dName == HardwarePart.X3) {
			txtX3.setText(val);
		} else if (dName == HardwarePart.MAR) {
			txtMAR.setText(val);
		} else if (dName == HardwarePart.MBR) {
			txtMBR.setText(val);
		} else if (dName == HardwarePart.PC) {
			txtPC.setText(val);
		} else if (dName == HardwarePart.IR) {
			txtIR.setText(val);
		} else if (dName == HardwarePart.MEMORY) {
			String key = vals[0];
			val = vals[1];

			String newElementString = key + "\t " + val;

			// Check if the Element has contained the address
			for (int i = 0; i < lstModel.getSize(); i++) {
				String addString = lstModel.getElementAt(i);
				String[] mVal = addString.split("\t");

				if (mVal[0].equals(key)) {
					lstModel.setElementAt(newElementString, i);
					return;
				}
			}

			lstModel.addElement(newElementString);
		}
	}

	private String padSpace(String key, int space) {

		if (key == null && "".equals(key)) {
			return "";
		}
		int sp = space - key.length();
		for (int i = 0; i <= sp; i++) {
			key += " ";
		}
		logger.debug(":" + key + ':');
		return key;
	}

	private void loadToLogicLayer(String dest, String val) {

		// This val will be binary code string
		Integer data = Integer.parseInt(val, 2);

		HardwarePart dName = HardwarePart.fromName(dest);

		if (dName == HardwarePart.MEMORY) {
			Integer addressLocation = Integer.parseInt(txtMemAdd.getText());
			Memory.shareInstance().setMem(addressLocation, data);
		} else if (dName == HardwarePart.R0) {
			cpuController.RFtable.setR0(data);
		} else if (dName == HardwarePart.R1) {
			cpuController.RFtable.setR1(data);
		} else if (dName == HardwarePart.R2) {
			cpuController.RFtable.setR2(data);
		} else if (dName == HardwarePart.R3) {
			cpuController.RFtable.setR3(data);
		} else if (dName == HardwarePart.X1) {
			cpuController.XFtable.setX1(data);
		} else if (dName == HardwarePart.X2) {
			cpuController.XFtable.setX2(data);
		} else if (dName == HardwarePart.X3) {
			cpuController.XFtable.setX3(data);
		} else if (dName == HardwarePart.MAR) {
			cpuController.cpuControl.MAR.setData(data);
		} else if (dName == HardwarePart.MBR) {
			cpuController.cpuControl.MDR.setData(data);
		} else if (dName == HardwarePart.PC) {
			cpuController.PC.setData(data);
		} else if (dName == HardwarePart.IR) {
			cpuController.IRobject.seIRstring(val);
		}
	}

	private class BtnActListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			Component parent = (Component) e.getSource();

			if (parent == btnLoad) {
				String sel = (String) cboSwithOptions.getSelectedItem();

				HardwarePart rName = HardwarePart.fromName(sel);
				int numBit = rName.getBit();

				String res = "";
				// for (int i = 0; i < numBit; i++) {
				for (int i = HIGHESTBIT - numBit + 1; i <= HIGHESTBIT; i++) {
					res += radBinData[i].isSelected() ? "1" : "0";
				}
				logger.debug("value from UI: " + res);

				if (rName == HardwarePart.MEMORY && txtMemAdd.getText().isEmpty()) {

					JOptionPane.showMessageDialog(MainSimFrame.this,
							"Please input the memory address.",
							"Missing Content", JOptionPane.ERROR_MESSAGE);
					return;
				}

				loadToLogicLayer(sel, res);

			} else if (parent == btnReset) {
				String sel = (String) cboSwithOptions.getSelectedItem();
				int numBit = HardwarePart.fromName(sel).getBit();

				resetSwitches(HIGHESTBIT - numBit + 1, 19, false);

			} else if (parent == btnIPL) {

				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(1000);
							resetSimulator(true);
							simConsole.info("Simulator started....");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
				simConsole.info("Simulator starting....");
				resetMainCtrlBtn(true);

			} else if (parent == btnTerminate) {
				String ObjButtons[] = { "Yes", "No" };
				int PromptResult = JOptionPane
						.showOptionDialog(
								MainSimFrame.this,
								"Are you sure you terminate simulator? All value will be reset.",
								"Termination", JOptionPane.DEFAULT_OPTION,
								JOptionPane.WARNING_MESSAGE, null, ObjButtons,
								ObjButtons[1]);
				if (PromptResult == 0) {
					resetSimulator(false);
					simConsole.info("Simulator terminated....");
					resetMainCtrlBtn(false);

				}

			}

		}

	}

	private void setMemorySwitch(boolean b) {

		lblMemAddress.setVisible(b);
		txtMemAdd.setVisible(b);

	}

	private void resetMainCtrlBtn(boolean isStart) {

		if (isStart) {
			btnTerminate.setEnabled(true);
			btnIPL.setEnabled(false);
		} else {
			btnTerminate.setEnabled(false);
			btnIPL.setEnabled(true);
		}

	}

	private void resetSwitches(int start, int end, boolean b) {
		for (int i = start; i <= end; i++) {
			if (radBinData[i] != null) {
				radBinData[i].setSelected(b);
			}
		}
		String sel = (String) cboSwithOptions.getSelectedItem();
		if (HardwarePart.fromName(sel) == HardwarePart.MEMORY) {
			txtMemAdd.setText("");
		} else
			setMemorySwitch(false);

	}

	private void setRegisterEditable(boolean b) {
		txtR0.setEditable(b);
		txtR1.setEditable(b);
		txtR2.setEditable(b);
		txtR3.setEditable(b);
		txtX1.setEditable(b);
		txtX2.setEditable(b);
		txtX3.setEditable(b);
		txtMAR.setEditable(b);
		txtMBR.setEditable(b);
		txtMSR.setEditable(b);
		txtMFR.setEditable(b);
		txtCC.setEditable(b);
		txtIR.setEditable(b);
		txtPC.setEditable(b);

	}

	private void resetSimulator(boolean isStart) {
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
		resetSwitches(LOWESTBIT, HIGHESTBIT, false);
		String sel = (String) cboSwithOptions.getSelectedItem();
		if (HardwarePart.fromName(sel) == HardwarePart.MEMORY) {
			setMemorySwitch(true);
		} else
			setMemorySwitch(false);

		cboSwithOptions.setEnabled(isStart);
		btnReset.setEnabled(isStart);
		btnLoad.setEnabled(isStart);

		btnSingleStep.setEnabled(isStart);
		btnRun.setEnabled(isStart);

		lstModel.clear();

		lstModel.addElement("Address   Content");
		lstModel.addElement("---------------------------------------");

		// try {
		// lstModel.clear();
		// for (int j = 0; j < 2048; j++) {
		// lstModel.addElement(" ");
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// lstMemory = new JList<String>(lstModel);

	}

	private void maskSwitches(int start, int end, boolean b) {

		for (int i = start; i <= end; i++) {
			if (radBinData[i] != null) {
				radBinData[i].setVisible(b);
				lblBinPosInfo[i].setVisible(b);
			}
		}

	}

	private class SwitchComboActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String selected = (String) ((JComboBox) e.getSource())
					.getSelectedItem();
			HardwarePart reg = HardwarePart.fromName(selected);
			if (reg != HardwarePart.NOTEXIST) {
				int numBit = reg.getBit();

				maskSwitches(0, HIGHESTBIT - numBit, false);
				maskSwitches(HIGHESTBIT - numBit + 1, 19, true);

				if (reg == HardwarePart.MEMORY) {
					setMemorySwitch(true);
				} else {
					setMemorySwitch(false);
				}
			}
			logger.debug("selcted :  " + selected);
		}
	}

	@Override
	public void refreshData(HardwareData subject) {

		final HardwareData data = subject;

		Runnable b = new Runnable() {
			@Override
			public void run() {
				Map<String, String> d = data.getData();
				for (Map.Entry<String, String> entry : d.entrySet()) {
					String k = entry.getKey();
					String v = entry.getValue();
					try {
						if (HardwarePart.fromName(k) == HardwarePart.MEMORY) {
							// memory value passed in with 'address, content');
							// presume the content is passed in one by one
							String[] mVal = v.split(",");
							loadToControl(k, mVal[0], mVal[1]);
						} else {
							loadToControl(k, v);
						}
						logger.debug("data from hw:" + k + "," + v);
					} catch (Exception e) {
						logger.error("failed to publish data to GUI.", e);

					}

				}
			}
		};

		SwingUtilities.invokeLater(b);

	}

	public void showProgrameFinishDialog() {
		JOptionPane.showMessageDialog(this, "All instructions are executed.");
	}

}
