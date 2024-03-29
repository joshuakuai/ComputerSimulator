package edu.gwu.cs6461.sim.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
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
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.logic.CPUController;
import edu.gwu.cs6461.logic.unit.MMU;
import edu.gwu.cs6461.sim.bridge.HardwareData;
import edu.gwu.cs6461.sim.bridge.Observer;
import edu.gwu.cs6461.sim.common.ConditionCode;
import edu.gwu.cs6461.sim.common.DeviceType;
import edu.gwu.cs6461.sim.common.HardwarePart;
import edu.gwu.cs6461.sim.common.MachineFault;
import edu.gwu.cs6461.sim.common.MachineStatus;
import edu.gwu.cs6461.sim.common.OpCode;
import edu.gwu.cs6461.sim.common.SimConstants;
import edu.gwu.cs6461.sim.exception.IOCmdException;
import edu.gwu.cs6461.sim.util.Convertor;
import edu.gwu.cs6461.sim.util.GriddedPanel;
import edu.gwu.cs6461.sim.util.PropertiesLoader;
import edu.gwu.cs6461.sim.util.PropertiesParser;
import edu.gwu.cs6461.sim.util.TextAreaAppender;

/**
 * 
 * Main GUI Frame
 * 
 * This is the first screen to be displayed when user starts the simulator.
 * 
 * This Frame class displays all the hardware parts for user to see the status
 * and data; it also displays the switches to allow user to input values,
 * control panel to allow user to Run and do the Single Step for the simulator,
 * IPL button to start the simulator, Terminate button to terminate or reset
 * simlator, memory area to show the content in simulator memory, console area
 * to show the system message.
 * 
 *
 * 
 * @author marcoyeung
 * @Revised   Apr 5, 2014 - 10:11:50 AM   
 */
public class MainSimFrame extends JFrame implements Observer {

	/**
	 * object to logging purpose
	 */
	private final static Logger logger = Logger.getLogger(MainSimFrame.class);
	/**logger for Console printer */
	private final static Logger simConsole = Logger.getLogger("simulator.console");

	/**
	 * lowest and higtest bit for switches
	 * support 20 bits
	 */
	private static final int HIGHESTBIT = 19;
	private static final int LOWESTBIT = 0;

	/**define switch radio buttons*/
	private JRadioButton[] radBinData = new JRadioButton[20];
	private JLabel[] lblBinPosInfo = new JLabel[20];
	private Dimension shortField = new Dimension(100, 50);

	
	/**defining Swing components for registers */
	private JLabel lblR0 = new JLabel(HardwarePart.R0.getName());
	private JLabel lblR1 = new JLabel(HardwarePart.R1.getName());
	private JLabel lblR2 = new JLabel(HardwarePart.R2.getName());
	private JLabel lblR3 = new JLabel(HardwarePart.R3.getName());

	private JTextField txtR0 = new JTextField(20);
	private JTextField txtR1 = new JTextField(20);
	private JTextField txtR2 = new JTextField(20);
	private JTextField txtR3 = new JTextField(20);

	private JLabel lblX1 = new JLabel(HardwarePart.X1.getName());
	private JLabel lblX2 = new JLabel(HardwarePart.X2.getName());
	private JLabel lblX3 = new JLabel(HardwarePart.X3.getName());

	private JTextField txtX1 = new JTextField(15);
	private JTextField txtX2 = new JTextField(15);
	private JTextField txtX3 = new JTextField(15);

	private JLabel lblMAR = new JLabel(HardwarePart.MAR.getName());
	private JLabel lblMBR = new JLabel(HardwarePart.MDR.getName());
	private JLabel lblMSR = new JLabel(HardwarePart.MSR.getName());
	private JLabel lblMFR = new JLabel(HardwarePart.MFR.getName());

	private JTextField txtMAR = new JTextField(13);
	private JTextField txtMBR = new JTextField(20);
	private JTextField txtMSR = new JTextField(20);
	private JTextField txtMFR = new JTextField(20);

	private JLabel lblCC = new JLabel(HardwarePart.CC.getName());
	private JLabel lblIR = new JLabel(HardwarePart.IR.getName());
	private JLabel lblPC = new JLabel(HardwarePart.PC.getName());

	private JTextField txtCC = new JTextField(10); // condition code //UNDERFLOW
													// or
	private JTextField txtIR = new JTextField(20); // current instruction
	private JTextField txtPC = new JTextField(13); // address of next instruction
	
	private JLabel lblFR0 = new JLabel(HardwarePart.FR0.getName());
	private JLabel lblFR1 = new JLabel(HardwarePart.FR1.getName());
	private JTextField txtFR0 = new JTextField(20);
	private JTextField txtFR1 = new JTextField(20);

	
	/**define Control panel button for simulator run */
	private JButton btnHalt = new JButton("Halt");
	private JButton btnRun = new JButton("Run");
	private JButton btnSingleInstr = new JButton("Single Instr.");
	private JButton btnSingleStep = new JButton("Single Step");

	/**define Simulator start up and terminate buttons */
	private JButton btnIPL = new JButton("IPL");
	private JButton btnTerminate = new JButton("Terminate");

	/**define switches panel buttons to reset and deposit instruction and data */
	private JComboBox<String> cboSwithOptions = new JComboBox<String>();
	private JButton btnReset = new JButton("Reset");
	private JButton btnDeposit = new JButton("Deposit");

	/** set memory */
	private JLabel lblMemAddress = new JLabel("Address:");
	private JTextField txtMemAdd = new JTextField(4);

	/**console printer swing component */
	private JTextArea txtConsoleText = new JTextArea();
	/**Swint list model for Memory display */
	private DefaultListModel<String> lstModel = new DefaultListModel<String>();
	private JList<String> lstMemory = new JList<String>(lstModel);

	/**IO input text field*/
	private JTextField txtIOInput = new JTextField();
	
	/**all supported instruction for user to select to assign switches buttons quickly*/
	private JList<String> lstHistCdms;
	/**history of IO commands user has input*/
	private DefaultListModel<String> lstModHistCdms= new DefaultListModel<>();
	/**all supported instructions, use to setup switches*/
	private JComboBox<String> cboAllInstrHelper = new JComboBox<String>();

	/**default register background color*/
	private static Color registerColor = new Color(238,238,238);
	/**
	 * The first switch, in ComboBox ,to be allow to edit by the tester this is
	 * will be default IR. Terminate and reset button will reset back to IR
	 */
	private int mainSwitchIdx = 0;
	
	/**initial PC; load from property file while starting up simulator*/
	private int instrStartingPos = 0;

	/**
	 * Business logic objects
	 */
	private CPUController cpuController = CPUController.shareInstance();
	
	/**activate / deactivate IN IODevice*/
	private boolean captureKeyEvent = false;

	/** Engineer console for internal simulator registers or hardwares */
	private EngConsoleFrame engConsoleFrame;
	
	
	private PropertiesParser prop;
	/**
	 * Constructor:init GUI component; register GUi component event listeners
	 */
	public MainSimFrame(String title, boolean resizable) {
		// setLayout(new MigLayout());

		setTitle(title);

		prop = PropertiesLoader.getPropertyInstance();
		instrStartingPos = prop.getIntProperty("sim.program.startingpoint",100);
				
		
		HardwarePart[] names = HardwarePart.values();
		List<String> tmp = new ArrayList<String>();

		int i = 0;
		for (HardwarePart n : names) {
			if (n.isEditable()) {
				tmp.add(n.getName());
				if (n == HardwarePart.MEMORY) {
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
		btnDeposit.addActionListener(btnAct);
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
		
		JMenuItem jmConsole = new JMenuItem("Engineer Console");
		jmConsole.setMnemonic('e');
		jmConsole.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				InputEvent.ALT_MASK));
		jmConsole.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				popupEngineerConsole();
			}
		});
		JMenu mView = new JMenu("Engineer");
		mView.setMnemonic('V');
		mView.add(jmConsole);
		
		
		JMenuBar menu = new JMenuBar();
		menu.add(mFile);
		menu.add(mView);
		
		setJMenuBar(menu);
		iExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				promptExit();
			}
		});

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				promptExit();
			}
		});
		setResizable(resizable);
		setRegisterEditable(false);

		setMemorySwitch(false);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TextAreaAppender.setTextArea(txtConsoleText);
			}
		});

	}
	
	/**message prompt for user to confirm exiting simulator*/
	private void promptExit() {
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

	/**
	 * Create and arrange the main window components ie all the registers are at
	 * the upper portion of window swithes and control panel are in the middle
	 * 
	 */
	public void init() {

		JPanel regSwPanel = new JPanel();
		JPanel regPanel = new JPanel();
		regPanel.setLayout(new BoxLayout(regPanel, BoxLayout.X_AXIS));

		//general + index + floating
		JPanel jpMainRegisters = new JPanel();
		jpMainRegisters.setLayout(new GridBagLayout());
		GridBagConstraints mc = new GridBagConstraints();
		mc.gridx = 0;
		mc.gridy = 0;
		jpMainRegisters.add(createGeneralRPanel(),mc);
		mc.gridx = 1;
		mc.gridy = 0;
		jpMainRegisters.add(createIndexRPanel(),mc);
		mc.gridx = 0;
		mc.gridy = 1;
		mc.gridwidth = 2;
		mc.anchor = GridBagConstraints.FIRST_LINE_START;
		jpMainRegisters.add(createFloatingPointRPanel(),mc);
		
		JPanel jpTmp;// = createGeneralRPanel();
//		regPanel.add(jpTmp);
//		jpTmp  = createIndexRPanel();
//		regPanel.add(jpTmp);
		regPanel.add(jpMainRegisters);
		
		jpTmp = createMiscRPanel();
		regPanel.add(jpTmp);

		regSwPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//top buttons area
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		regSwPanel.add(createMainCtrlPanel(), c);

		//registers area
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		regSwPanel.add(regPanel, c);

		//switches and control buttons panel
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

		add(regSwPanel, BorderLayout.NORTH);
		//add(createConsolePanel(), BorderLayout.SOUTH);

		// memory area
		JScrollPane sMem = new JScrollPane();
		sMem.getViewport().add(lstMemory);
		JPanel jmem = new JPanel(new BorderLayout());
		jmem.add(sMem, BorderLayout.CENTER);
		jmem.setBorder(new TitledBorder(new EtchedBorder(), "Memory"));
		//add(jmem, BorderLayout.CENTER);
		
		JSplitPane spMemConsoleIO = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				createConsolePanel(), createIOConsolePanel());
		spMemConsoleIO.setDividerSize(8);
		spMemConsoleIO.setDividerLocation(500);
		spMemConsoleIO.setContinuousLayout(true);
		spMemConsoleIO.resetKeyboardActions();
		spMemConsoleIO.setOneTouchExpandable(true);

		JSplitPane spMemConsole = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jmem,spMemConsoleIO);
		spMemConsole.setDividerLocation(300);
		spMemConsole.setDividerSize(8);
		spMemConsole.setContinuousLayout(true);
		spMemConsole.setOneTouchExpandable(true);
		
		add(spMemConsole, BorderLayout.CENTER);

		logger.debug(getLayout());

		resetSimulator(false);

		reSetCPUController();
		
	}

	/**create Register panel*/
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

	/**create IO Input Panel*/
	private JPanel createIOConsolePanel(){
		JPanel gConsole = new JPanel();
		gConsole.setLayout(new GridBagLayout());
		gConsole.setBorder(new TitledBorder(new EtchedBorder(), "IO Input"));

 		lstHistCdms = new JList<String>(lstModHistCdms);
		lstHistCdms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		txtIOInput.addKeyListener(new InputKeyEventHandler());
		txtIOInput.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String cmd = txtIOInput.getText();
				
				if (captureKeyEvent) {
					lstModHistCdms.insertElementAt(DeviceType.Keyboard + " " + cmd, 0);
					logger.info("Ignore keyboard 'Enter' since IO operation is in action");
					txtIOInput.setText("");
					return;
				}
				
		        lstModHistCdms.insertElementAt("Loading "+ cmd, 0);
		        lstHistCdms.setSelectedIndex(0);
		        txtIOInput.selectAll();
		        
		        try {
					performIOCommand(cmd);
				} catch (IOCmdException e1) {
					simConsole.error(cmd + " is not loaded "  + e1.getMessage());
				}
			}
		});
		txtIOInput.setFocusTraversalKeysEnabled(false);
		txtIOInput.getDocument().addDocumentListener(new TextFieldDocListener(txtIOInput));
	    
		
		JScrollPane sclHistCmdsPane = new JScrollPane(lstHistCdms);
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;

		c.fill = GridBagConstraints.HORIZONTAL;
		gConsole.add(txtIOInput, c);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		gConsole.add(sclHistCmdsPane, c);

		return gConsole;
	}
	
	/**create index panel*/
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

	/**create control panel*/
	private JPanel createControlPanel() {
		GriddedPanel gPanel = new GriddedPanel();
		gPanel.setBorder(new TitledBorder(new EtchedBorder(), "Control Panel"));

		gPanel.addFilledComponent(btnHalt, 0, 0);
		gPanel.addComponent(btnSingleInstr, 0, 1);
		gPanel.addFilledComponent(btnSingleStep, 1, 1);
		gPanel.addFilledComponent(btnRun, 1, 0);

		return gPanel;
	}

	/**create console panel*/
	private JPanel createConsolePanel() {

		txtConsoleText.setFont(new Font("consolas", Font.PLAIN, 13));
		txtConsoleText.setEditable(false);
		JScrollPane scroll = new JScrollPane(txtConsoleText);
		scroll.setPreferredSize(new Dimension(240, 80));

		JPanel gPanel = new JPanel();
		gPanel.setBorder(new TitledBorder(new EtchedBorder(), "Console Printer"));
		gPanel.setLayout(new BorderLayout());
		gPanel.add(scroll, BorderLayout.CENTER);

		return gPanel;

	}

	/**Create start up and terminate panel */
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
	
	private JPanel createFloatingPointRPanel() {
		GriddedPanel gPanel = new GriddedPanel();
		gPanel.setBorder(new TitledBorder(new EtchedBorder(), "Floating Point"));

		gPanel.addComponent(lblFR0, 0, 0);
		gPanel.addComponent(txtFR0, 0, 1);
		gPanel.addComponent(lblFR1, 1, 0);
		gPanel.addComponent(txtFR1, 1, 1);

		FlowLayout fl = new FlowLayout();
		JPanel wrap = new JPanel();
		wrap.setLayout(fl);
		wrap.add(gPanel);
		fl.setAlignment(FlowLayout.LEFT);
		return wrap;
	}

	/**Create general register panel*/
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

	/**Create Radio button Switch Panel*/
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

	/**Create Switch panel */
	private JPanel createSwitchPanel(int start, int end) {
		// Button only
		JPanel btnPanel = new JPanel();

		// TODO remove
		createInstrHelperComponent();
		btnPanel.add(cboAllInstrHelper);
		cboAllInstrHelper.addActionListener(new InstrHelperComboActionListener());
		cboAllInstrHelper.setVisible(false);

		btnPanel.add(lblMemAddress);
		btnPanel.add(txtMemAdd);
		btnPanel.add(cboSwithOptions);
		cboSwithOptions.addActionListener(new SwitchComboActionListener());
		btnPanel.add(btnReset);
		btnPanel.add(btnDeposit);
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

	/**
	 * button event listener for Run and Single step buttons
	 * 
	 * @author marcoyeung
	 * 
	 */
	private class SimActListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			Component parent = (Component) e.getSource();

			if (parent == btnRun) {
				beginCPUProcessWithModel(0);
			} else if (parent == btnSingleStep) {
				beginCPUProcessWithModel(1);
			} else if (parent == btnSingleInstr) {
				beginCPUProcessWithModel(2);
			} else if (parent == btnHalt) {
				if (cpuController.isAlive()) {
					cpuController.Suspend();
				}
			}
		}

	}

	/**
	 * signal the back-end logic to run the instruction till the end or run as
	 * single step mode
	 * 
	 * @param Model
	 *            , 0: Run 1: Single Step 2: Single Instruction
	 */
	private void beginCPUProcessWithModel(int model) {
		if (cpuController.isAlive() && cpuController.isSuspended()) {
			// For Single step model or single instruction
			// If this thread is alive and is suspended, the button will
			// resume the thread
			cpuController.Resume();

			// If the model is debug, then we set the SI or SS as 1 again to
			// stop
			// After next instruction
			setDebugModelRegisters(model);

			return;
		}

		// For all model
		if (!cpuController.isAlive() && !cpuController.isSuspended()) {
			// then if the the thread is dead and is not suspended,
			// recreate the cpucontroller thread and
			// set the debug model on and start the process
			cpuController.recreateCPUController(true);
			reSetCPUController();

			setDebugModelRegisters(model);

			cpuController.start();
		}
		// For other situation just ignore this touch event
	}

	/**
	 * Set the debug model registers to stop the program properly
	 * 
	 * @param model
	 */
	public void setDebugModelRegisters(int model) {
		if (model == 1) {
			cpuController.registerContainer.SS.setData(1);
			cpuController.registerContainer.SI.setData(0);
		} else if (model == 0) {
			cpuController.registerContainer.SS.setData(0);
			cpuController.registerContainer.SI.setData(0);
		} else if (model == 2) {
			cpuController.registerContainer.SS.setData(0);
			cpuController.registerContainer.SI.setData(1);
		}
	}

	/**
	 * 
	 * hardware data from back-end or switches are published to the
	 * corresponding UI components.
	 * 
	 * @param dest
	 * @param vals
	 */
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
		} else if (dName == HardwarePart.FR0) {
			txtFR0.setText(val);
		} else if (dName == HardwarePart.FR1) {
			txtFR1.setText(val);
		} else if (dName == HardwarePart.MAR) {
			txtMAR.setText(val);
		} else if (dName == HardwarePart.MDR) {
			txtMBR.setText(val);
		} else if (dName == HardwarePart.PC) {
			txtPC.setText(val);
		} else if (dName == HardwarePart.IR) {
			txtIR.setText(val);
		} else if (dName == HardwarePart.MSR) {
			MachineStatus sta = MachineStatus.fromCode(Integer.parseInt(val) );
			if (sta != MachineStatus.Unknown) {
				txtMSR.setText(sta.name());
				if (sta == MachineStatus.MachineFault) {
					txtMSR.setBackground(Color.RED);
				} else if (sta == MachineStatus.TrapInstruction) {
					txtMSR.setBackground(Color.YELLOW);
				} else 
					txtMSR.setBackground(registerColor);
			} else {
				txtMSR.setText(val);
				txtMSR.setBackground(registerColor);
			}
		} else if (dName == HardwarePart.MFR) {
			MachineFault fau = MachineFault.valueOf(Integer.parseInt(val) ); 
			if (fau != MachineFault.Unknown) {
				txtMFR.setText(fau.name());
			} else txtMFR.setText(val);
			
		} else if (dName == HardwarePart.MEMORY) {
			String key = vals[0];
			val = vals[1];
			String comment="";

			String newElementString = padSpace(key, 9) + val;
			if (vals.length == 3) {
				comment = vals[2];
				newElementString = padSpace(newElementString,35) +  comment;
			}

			// Check if the Element has contained the address
			for (int i = 0; i < lstModel.getSize(); i++) {
				String addString = lstModel.getElementAt(i);

				int spIdx = addString.indexOf(" ");
				if (spIdx > 0) {
					addString = addString.substring(0, spIdx);
				}
				if (addString.equals(key) ) { //further enhance in the next phase
					lstModel.setElementAt(newElementString, i);
					return;
				}
			}

			lstModel.addElement(newElementString);
		} else if (dName == HardwarePart.CC) {
			ConditionCode cc = ConditionCode.fromCode(Integer.valueOf(val));
			if (cc != ConditionCode.NOTEXIST && cc != ConditionCode.NORMAL)
				txtCC.setText(cc.name());
			else
				txtCC.setText("");
		}
	}

	/**padding space for aligning the string */
	private String padSpace(String key, int space) {

		if (key == null && "".equals(key)) {
			return "";
		}
		int sp = space - key.length();
		for (int i = 0; i <= sp; i++) {
			key += " ";
		}
		return key;
	}

	/**
	 * Data input through UI switches are passed to back-end simulator logic
	 * 
	 * @param dest
	 * @param val
	 */
	private void loadToLogicLayer(String dest, String val) {
		// This val will be binary code string
		Integer data = Integer.parseInt(val, 2);
		HardwarePart dName = HardwarePart.fromName(dest);

		if (dName == HardwarePart.MEMORY) {
			Integer addressLocation = Integer.parseInt(txtMemAdd.getText());
			cpuController.setMemData(addressLocation, val);
		} else if (dName == HardwarePart.R0) {
			cpuController.registerContainer.RFtable.setR0(data);
		} else if (dName == HardwarePart.R1) {
			cpuController.registerContainer.RFtable.setR1(data);
		} else if (dName == HardwarePart.R2) {
			cpuController.registerContainer.RFtable.setR2(data);
		} else if (dName == HardwarePart.R3) {
			cpuController.registerContainer.RFtable.setR3(data);
		} else if (dName == HardwarePart.X1) {
			cpuController.registerContainer.XFtable.setX1(data);
		} else if (dName == HardwarePart.X2) {
			cpuController.registerContainer.XFtable.setX2(data);
		} else if (dName == HardwarePart.X3) {
			cpuController.registerContainer.XFtable.setX3(data);
		} else if (dName == HardwarePart.MAR) {
			cpuController.registerContainer.MAR.setData(data);
		} else if (dName == HardwarePart.MDR) {
			cpuController.registerContainer.MDR.setData(data);
		} else if (dName == HardwarePart.PC) {
			cpuController.registerContainer.PC.setData(data);
		} else if (dName == HardwarePart.IR) {
			cpuController.registerContainer.IRobject.setIRstring(val);
		} else if (dName == HardwarePart.FR0) {
			cpuController.registerContainer.FR0.setData(data);
		} else if (dName == HardwarePart.FR1) {
			cpuController.registerContainer.FR1.setData(data);
		}
		
	}

	/**reinitialize the hardware components */
	private void reSetCPUController() {
		cpuController = CPUController.shareInstance();
		cpuController.clearObserver();
		cpuController.setRegisterObserver(MainSimFrame.this);
		cpuController.setMainFrame(MainSimFrame.this);
	}

	/**
	 * Button event listeners for Deposit, Reset , IPL, and Terminate buttons
	 * 
	 * @author marcoyeung
	 * 
	 */
	private class BtnActListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			Component parent = (Component) e.getSource();

			if (parent == btnDeposit) {
				String sel = (String) cboSwithOptions.getSelectedItem();

				HardwarePart rName = HardwarePart.fromName(sel);
				int numBit = rName.getBit();

				String res = "";
				// for (int i = 0; i < numBit; i++) {
				for (int i = HIGHESTBIT - numBit + 1; i <= HIGHESTBIT; i++) {
					res += radBinData[i].isSelected() ? "1" : "0";
				}
				logger.debug("switch value from UI: " + res);

				if (rName == HardwarePart.MEMORY
						&& txtMemAdd.getText().isEmpty()) {

					JOptionPane.showMessageDialog(MainSimFrame.this,
							"Please input the memory address.",
							"Missing Content", JOptionPane.ERROR_MESSAGE);
					return;
				}

				loadToLogicLayer(sel, res);

			} else if (parent == btnReset) {
				String sel = (String) cboSwithOptions.getSelectedItem();
				int numBit = HardwarePart.fromName(sel).getBit();

				clearSwitches(HIGHESTBIT - numBit + 1, 19);

			} else if (parent == btnIPL) {

				new Thread(new Runnable() {
					public void run() {
						try {							
							Thread.sleep(200);
							resetSimulator(true);
							simConsole.info("Simulator started....");

						} catch (Exception e) {
							logger.error("failed while initializing simulator.", e);
						}
					}
				}).start();
				simConsole.info("Simulator starting....");
				resetMainCtrlBtn(true);
				
				cpuController.recreateCPUController(false);
				reSetCPUController();
		
				// TODO:We need to define the initial PC value
				cpuController.registerContainer.PC.setData(instrStartingPos);

			} else if (parent == btnTerminate) {
				String ObjButtons[] = { "Yes", "No" };
				int promptResult = JOptionPane
						.showOptionDialog(
								MainSimFrame.this,
								"Are you sure you terminate simulator? All value will be reset.",
								"Termination", JOptionPane.DEFAULT_OPTION,
								JOptionPane.WARNING_MESSAGE, null, ObjButtons,
								ObjButtons[1]);
				if (promptResult == 0) {
					resetSimulator(false);
					txtConsoleText.setText("");
					simConsole.info("Simulator terminated....");
					resetMainCtrlBtn(false);
					
					//Clean up all data in memory and caches
					MMU.instance().clean();
					if (engConsoleFrame!=null) {
						engConsoleFrame.cleanUp();
					}
				}
			}

		}

	}
	
	/**Event handler for IO Input textbook */
	private void performIOCommand(String cmd) throws IOCmdException{
		
		if (cmd==null || "".equals(cmd)) {
			return;
		}
		
		PropertiesParser prop = PropertiesLoader.getPropertyInstance();
		String filePath = prop.getStringProperty("sim.programfilepath");
		filePath = filePath + File.separator+cmd;
		if (filePath != null && !"".equals(filePath)  && new File(filePath).exists()) {
			cpuController.loadFromFile(filePath);
			logger.debug("program file "+filePath+" is loaded");
			simConsole.info("program file "+cmd+" is loaded");
			cpuController.registerContainer.PC.setData(instrStartingPos);
			txtIOInput.setText("");
		} else {
			logger.debug("profile file is not loaded");
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

	/**adjust Switches component for user selection*/
	private void clearSwitches(int start, int end) {
		for (int i = start; i <= end; i++) {
			if (radBinData[i] != null) {
				radBinData[i].setSelected(false);
			}
		}

		String sel = (String) cboSwithOptions.getSelectedItem();

		if (HardwarePart.fromName(sel) == HardwarePart.MEMORY) {
			txtMemAdd.setText("");
		} else
			setMemorySwitch(false);
	}

	private void setSwitchesEditable(int start, int end, boolean b) {
		for (int i = start; i <= end; i++) {
			if (radBinData[i] != null) {
				radBinData[i].setEnabled(b);
			}
		}
	}

	private void setRegisterEditable(boolean b) {
		txtR0.setEditable(b);
		txtR1.setEditable(b);
		txtR2.setEditable(b);
		txtR3.setEditable(b);
		txtX1.setEditable(b);
		txtX2.setEditable(b);
		txtX3.setEditable(b);
		txtFR0.setEditable(b);
		txtFR1.setEditable(b);
		txtMAR.setEditable(b);
		txtMBR.setEditable(b);
		txtMSR.setEditable(b);
		txtMFR.setEditable(b);
		txtCC.setEditable(b);
		txtIR.setEditable(b);
		txtPC.setEditable(b);
		txtMFR.setEditable(b);

	}

	private void resetSimulator(boolean isStart) {
		txtR0.setText("");
		txtR1.setText("");
		txtR2.setText("");
		txtR3.setText("");
		txtX1.setText("");
		txtX2.setText("");
		txtX3.setText("");
		txtFR0.setText("");
		txtFR1.setText("");
		txtMAR.setText("");
		txtMBR.setText("");
		txtMSR.setText("");
		txtMFR.setText("");
		txtCC.setText("");
		txtIR.setText("");
		txtPC.setText("");
		txtMFR.setText("");
		cboSwithOptions.setSelectedIndex(mainSwitchIdx);
		clearSwitches(LOWESTBIT, HIGHESTBIT);
		setSwitchesEditable(LOWESTBIT, HIGHESTBIT, isStart);

		String sel = (String) cboSwithOptions.getSelectedItem();
		if (HardwarePart.fromName(sel) == HardwarePart.MEMORY) {
			setMemorySwitch(true);
		} else
			setMemorySwitch(false);

		cboSwithOptions.setEnabled(isStart);
		btnReset.setEnabled(isStart);
		btnDeposit.setEnabled(isStart);

		btnSingleStep.setEnabled(isStart);
		btnRun.setEnabled(isStart);
		btnSingleInstr.setEnabled(isStart);
		btnHalt.setEnabled(isStart);

		lstModel.clear();

		lstModel.addElement("Address   Content");
		lstModel.addElement("---------------------------------------");

		lstModHistCdms.clear();
		txtIOInput.setText("");
		txtIOInput.setEditable(isStart);
		
		cboAllInstrHelper.setEnabled(isStart);
		txtMemAdd.setEnabled(isStart);
		txtMSR.setBackground(registerColor);
		
		if (isStart) {
			cpuController.loadROM();
		}
		
		outHaldr = null;
		
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

	/**re-adjust the UI component display*/
	private void maskSwitches(int start, int end, boolean b) {

		for (int i = start; i <= end; i++) {
			if (radBinData[i] != null) {
				radBinData[i].setVisible(b);
				lblBinPosInfo[i].setVisible(b);
			}
		}

		String sel = (String) cboSwithOptions.getSelectedItem();
		if (HardwarePart.fromName(sel) == HardwarePart.MEMORY) {
			for (int i = 0; i < 20; i++) {
				lblBinPosInfo[i].setForeground(Color.BLACK);
			}
			lblBinPosInfo[6].setForeground(Color.BLUE);
			lblBinPosInfo[7].setForeground(Color.BLUE);
			lblBinPosInfo[8].setForeground(Color.GREEN);
			lblBinPosInfo[9].setForeground(Color.GREEN);
			lblBinPosInfo[10].setForeground(Color.GRAY);
			lblBinPosInfo[11].setForeground(Color.MAGENTA);

		} else if (HardwarePart.fromName(sel) == HardwarePart.FR0 || 
					HardwarePart.fromName(sel) == HardwarePart.FR1) {
				lblBinPosInfo[0].setForeground(Color.BLACK);
				for (int i = 1; i < 8; i++) {
					lblBinPosInfo[i].setForeground(Color.MAGENTA);
				}
				for (int i = 8; i < 20; i++) {
					lblBinPosInfo[i].setForeground(Color.BLUE);
				}
		} else {
			for (int i = 0; i < 20; i++) {
				lblBinPosInfo[i].setForeground(Color.BLACK);
			}
		}

	}

	/**Event handler to re-adjust switch radio button to reflect the number of bits 
	 * for different instructions*/
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

				String sel = (String)cboAllInstrHelper.getSelectedItem();
				if (reg == HardwarePart.MEMORY) {
					setMemorySwitch(true);
					cboAllInstrHelper.setVisible(true);
					
					if (OpCode.valueOf(sel) == OpCode.IN || OpCode.valueOf(sel) == OpCode.OUT)  {
						setSwithes4InOutInstr(false);
					}
					
				} else {
					setMemorySwitch(false);
					cboAllInstrHelper.setVisible(false);
					if (OpCode.valueOf(sel) == OpCode.IN || OpCode.valueOf(sel) == OpCode.OUT)  {
						setSwithes4InOutInstr(true);
					}
				}

			}
			logger.debug("selected :  " + selected);
		}
	}

//	private class txtCmdActionListener implements ActionListener

	/**
	 * Receive data from observable objects and publish to GUI components
	 * 
	 */
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
							String[] mVal = v.split("["+SimConstants.MSG_TO_GUI_DELIMITER +"]");
							if (mVal.length == 2) {
								loadToControl(k, mVal[0], mVal[1]);
							} else if(mVal.length >= 3){
								loadToControl(k, mVal[0], mVal[1], mVal[2]);
							}
						} else if (HardwarePart.fromName(k) == HardwarePart.OUTPUT) {
							
							if (outHaldr == null || !outHaldr.isAlive()) {
								outHaldr = new PrintOutHandler();
								logger.debug("output request from hardware :" + v + " creating handler.");
							}
							
						} else if (HardwarePart.fromName(k) == HardwarePart.INPUT) {
							logger.debug("input request from hardware:" + v + " activate the keyboard for input." );
							captureKeyEvent = true;
							txtIOInput.setBackground(Color.YELLOW);
//							txtIOInput.setText("");
						} else if (SimConstants.ECONSOLE_ALU_MSG.equals(k) || 
								SimConstants.ECONSOLE_DECODE_MSG.equals(k)) {
							
							
							if (engConsoleFrame!=null) {
								engConsoleFrame.pushData(k, v);
							}
							
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

	/***************************************/

	/**list all the supported instrctions in dropbox so that user can assign switch buttons easily */
	private void createInstrHelperComponent() {
		OpCode[] names = OpCode.values();
		List<String> tmp = new ArrayList<String>();

		int i = 0;
		for (OpCode n : names) {
			if (n.isEditable()) {
				tmp.add(n.name());
				i++;
			}
		}//

		String[] reg = tmp.toArray(new String[tmp.size()]);

		cboAllInstrHelper = new JComboBox<String>(reg);
	}

	/**Event handler for all support instruction; mainly to easily turn on/off switches */
	private class InstrHelperComboActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String selected = (String) ((JComboBox) e.getSource())
					.getSelectedItem();
			OpCode op = OpCode.valueOf(selected);
			String bin = op.getbStr();
			if (op != OpCode.NOTEXIST) {

				try {
					String bits[] = Convertor.bitToArray(bin.substring(0, 6));
					
					for (int i = 0; i < 6; i++) {
						if (bits[i].equals("1")) {
							radBinData[i].setSelected(true);
						} else {
							radBinData[i].setSelected(false);
						}
					}
					if (op == OpCode.IN || op == OpCode.OUT) {
						//TODO need a place to store this nu-used bit range?!
						//hard coded for now
						setSwithes4InOutInstr(false);
					} else  {
						setSwithes4InOutInstr(true);
					}
					
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			logger.debug("selected :  " + op);
		}

	}
	
	private void setSwithes4InOutInstr(boolean b) {
		setSwitchesEditable(8,9,b);
		setSwitchesEditable(10,15,b);
	}

	/**
	 * capture keyboard event for IN IO Instruction
	 * */
	private class InputKeyEventHandler implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			handleInput(e, "keyTyped");
		}

		@Override
		public void keyPressed(KeyEvent e) {}

		@Override
		public void keyReleased(KeyEvent e) {}
			
	}
	
	/**Capture keyboard event and send the keyboard value to hardware side 
	 * */
	private void handleInput(KeyEvent e, String keyStatus){
        if (!captureKeyEvent) {
			return;
		}
        //You should only rely on the key char if the event
        //is a key typed event.
        char c = e.getKeyChar();

        logger.debug("keyboard value to hardware: " + c + " val:"+ (int)c);
        
        cpuController.setInputDeviceData(DeviceType.Keyboard, (int)c);
        
        logger.debug("end of this capture keyboard cycle.");
        txtIOInput.setBackground(Color.WHITE);
        captureKeyEvent = false; //
    }	

	
	/***
	 * Handle OUT IO event from hardware so that message
	 * can be printed in console printer
	 * */
    private class PrintOutHandler implements Runnable {
    	
    	Thread self;
    	public PrintOutHandler() {
    		self = new Thread(this,"printerConsole");
    		self.start();
		}
    	private boolean isAlive() {
    		if (self!=null) {
				return self.isAlive();
			}
    		return false;
    	}
    	
		@Override
		public void run() {
			int val = 0;
			while (true) {
				val = cpuController.getOuputDeviceData();
				if (val == '\n') {
					break;
				}
				result = result + (char) val;
				logger.debug("chars received : " + result);
			}
			if (val == '\n') {
				simConsole.info(result);
				result = "";
			}

			// int val = cpuController.getOuputDeviceData();
			// simConsole.info((char)val);
		}
    } //
    /**holding OUT char for console printer*/
    private String result ="";
    private PrintOutHandler outHaldr=null;
    
    
    
    protected void clearConsole() {
    	engConsoleFrame=null;
    }
    
    /**
     * tips
     * http://stackoverflow.com/questions/309023/how-to-bring-a-window-to-the-front
     * */
    protected void popupEngineerConsole() {
    	if (engConsoleFrame==null) {
    		String title = prop.getStringProperty("sim.gui.engconsole.title");
    		
    		engConsoleFrame = new EngConsoleFrame(this,title);
    		engConsoleFrame.init();
    		engConsoleFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    		engConsoleFrame.setResizable(false);
    		engConsoleFrame.setVisible(true);
    	} else {
    		logger.debug("Eng. console already started");
    		if(engConsoleFrame.getState()!=Frame.NORMAL) { engConsoleFrame.setState(Frame.NORMAL); }
    		engConsoleFrame.toFront();
    		engConsoleFrame.requestFocus();
    	}
    	
    }
    
}
