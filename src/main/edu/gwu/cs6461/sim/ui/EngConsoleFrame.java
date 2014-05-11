package edu.gwu.cs6461.sim.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.sim.common.ALUOperator;
import edu.gwu.cs6461.sim.common.DeviceType;
import edu.gwu.cs6461.sim.common.HardwarePart;
import edu.gwu.cs6461.sim.common.OpCode;
import edu.gwu.cs6461.sim.common.SimConstants;
import edu.gwu.cs6461.sim.util.GriddedPanel;

/**
 * 
 * 
 */
public class EngConsoleFrame extends JFrame {

	/**
	 * object to logging purpose
	 */
	private final static Logger logger = Logger.getLogger(EngConsoleFrame.class);

	
	private JLabel lblOpr1 = new JLabel("Operand 1");
	private JLabel lblOpr2 = new JLabel("Operand 2");
	private JLabel lblOptor = new JLabel("Operator");

	private JTextField txtOpr1 = new JTextField(20);
	private JTextField txtOpr2 = new JTextField(20);
	private JTextField txtOptor = new JTextField(10);
	
	private JLabel lblAddress = new JLabel("Base Address");
	private JLabel lblIndirect = new JLabel("Indirect Mode");
	private JLabel lblIndexReg = new JLabel("Index Register");
	
	private JTextField txtAddress = new JTextField(20);
	private JTextField txtIndirect = new JTextField(20);
	private JTextField txtIndexReg = new JTextField(20);
	
	
	private JLabel lblOpCode = new JLabel("OpCode");
	private JLabel lblRef1 = new JLabel("General Register 1");
	private JLabel lblRef2 = new JLabel("General Register 2");
	private JLabel lblIndReg = new JLabel("Index Register");
	private JLabel lblImmed = new JLabel("Immed value");
	private JLabel lblDeviceId = new JLabel("Device Id");
	private JLabel lblLeftRightMode = new JLabel("Shift Mode");
	private JLabel lblSRCount= new JLabel("Shift/Rotate Count");
	private JLabel lblLogicArithMode = new JLabel("Shift Type");
	

	private JTextField txtOpCode = new JTextField(20);
	private JTextField txtRef1 = new JTextField(20);
	private JTextField txtRef2 = new JTextField(20);
	private JTextField txtIndReg = new JTextField(20);
	private JTextField txtImmed = new JTextField(20);
	private JTextField txtDeviceId = new JTextField(20);
	private JTextField txtLeftRightMode = new JTextField(20);
	private JTextField txtSRCount= new JTextField(20);
	private JTextField txtLogicArithMode = new JTextField(20);
	
	private JButton btnClose  =new JButton("Close");

	private MainSimFrame mainFrame;
	private String title;

	public EngConsoleFrame(MainSimFrame mainFrame,String title) throws HeadlessException {
		super(title);
		setSize(390,550);
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
	private JPanel createDecodedPanel() {
		GriddedPanel gPanel = new GriddedPanel();
		gPanel.setBorder(new TitledBorder(new EtchedBorder(), "Decoded instruction"));

		gPanel.addComponent(lblOpCode, 0, 0);
		gPanel.addComponent(txtOpCode, 0, 1);
		gPanel.addComponent(lblRef1, 1, 0);
		gPanel.addComponent(txtRef1, 1, 1);
		gPanel.addComponent(lblRef2, 2, 0);
		gPanel.addComponent(txtRef2, 2, 1);
		gPanel.addComponent(lblIndReg, 3, 0);
		gPanel.addComponent(txtIndReg, 3, 1);
		gPanel.addComponent(lblImmed, 4, 0);
		gPanel.addComponent(txtImmed, 4, 1);
		gPanel.addComponent(lblDeviceId, 5, 0);
		gPanel.addComponent(txtDeviceId, 5, 1);
		gPanel.addComponent(lblLeftRightMode, 6, 0);
		gPanel.addComponent(txtLeftRightMode, 6, 1);
		gPanel.addComponent(lblLogicArithMode, 7, 0);
		gPanel.addComponent(txtLogicArithMode, 7, 1);
		gPanel.addComponent(lblSRCount, 8, 0);
		gPanel.addComponent(txtSRCount, 8, 1);

		FlowLayout fl = new FlowLayout();
		JPanel wrap = new JPanel();
		wrap.setLayout(fl);
		wrap.add(gPanel);
		fl.setAlignment(FlowLayout.LEFT);
		return wrap;

	}
	
	
	/**message prompt for user to confirm exiting simulator*/
	private void promptExit() {
		/*String ObjButtons[] = { "Yes", "No" };
		int PromptResult = JOptionPane.showOptionDialog(
				EngConsoleFrame.this, "Are you sure want to close this console ?",
				"Simualtor", JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, ObjButtons,
				ObjButtons[1]);
		if (PromptResult == 0) {
			mainFrame.clearConsole();
			this.dispose();
		}*/
		
		closeMe();
	}
	
	private void closeMe() {
		mainFrame.clearConsole();
		this.dispose();

	}
	
	private boolean isIndirectable(OpCode code) {
		if (code == OpCode.LDR || code == OpCode.STR || code == OpCode.LDA
				|| code == OpCode.LDX || code == OpCode.STX
				|| code == OpCode.JZ || code == OpCode.JNE
				|| code == OpCode.JCC || code == OpCode.JMP
				|| code == OpCode.JSR || code == OpCode.SOB
				|| code == OpCode.JGE || code == OpCode.AMR
				|| code == OpCode.SMR || code == OpCode.FADD
				|| code == OpCode.FSUB || code == OpCode.VADD
				|| code == OpCode.VSUB || code == OpCode.CNVRT
				|| code == OpCode.LDFR || code == OpCode.STFR) {
			return true;
		}
		
		return false;
	}
	private boolean isUsing2ndGR(OpCode code) {
		if (code == OpCode.MLT ||
				code == OpCode.DVD ||
				code == OpCode.TRR ||
				code == OpCode.AND ||
				code == OpCode.ORR
				) {
			return true;
		}
		return false;
	}
	
	private void updateInstrAddressUI(OpCode code,String... data) {
		if (code == OpCode.LDR || code == OpCode.STR || code == OpCode.LDR
				|| code == OpCode.LDX || code == OpCode.STX
				|| code == OpCode.JZ || code == OpCode.JNE
				|| code == OpCode.JCC || code == OpCode.JMP
				|| code == OpCode.JSR || code == OpCode.RFS
				|| code == OpCode.SOB || code == OpCode.JGE
				|| code == OpCode.AMR || code == OpCode.SMR
				|| code == OpCode.FADD|| code == OpCode.FSUB
				|| code == OpCode.VADD || code == OpCode.VSUB
				|| code == OpCode.CNVRT || code == OpCode.LDFR
				|| code == OpCode.STFR ) {
			txtAddress.setText(data[4]);
		} else 
			txtAddress.setText("0");
		
		if (isIndirectable(code)) {
			txtIndirect.setText(data[5].equals("1")? "Indirect":"Non-Indirect");
		} else {
			txtIndirect.setText("");
		}
		txtIndexReg.setText(ireg(data[3]));
	}
	private String ffReg(String reg){
		String name ="FR0";
		if ("0".equals(reg)) {
			name = "FR0";
		} else if ("1".equals(reg)) {
			name = "FR1";
		}
		return name;
	}
	private String reg(String Reg){
		String binary1="R0";
		if(Reg.equalsIgnoreCase("0"))
			binary1="R0";
		else if(Reg.equalsIgnoreCase("1"))
			binary1="R1";
		else if(Reg.equalsIgnoreCase("2"))
			binary1="R2";
		else if(Reg.equalsIgnoreCase("3"))
			binary1="R3";
		return binary1;
	}
	private String ireg(String index){
		String binary1="";
		if(index.equalsIgnoreCase("1"))
			binary1="X1";
		else if(index.equalsIgnoreCase("2"))
			binary1="X2";
		else if(index.equalsIgnoreCase("3"))
			binary1="X3";
		else 
			binary1="";
		return binary1;
	}	
	private void updateDecodedInstrUI(OpCode op, String... data) {
		txtOpCode.setText(op.name());
		
		if (op==OpCode.FADD ||
				op==OpCode.FSUB ||
				op==OpCode.VADD ||
				op==OpCode.VSUB ||
				op==OpCode.CNVRT||
				op==OpCode.LDFR||
				op==OpCode.STFR) {
			txtRef1.setText(ffReg(data[11]));
			lblRef1.setText("Floating Register");
		} else {	txtRef1.setText(reg(data[1]));
			lblRef1.setText("General Register 1");
		}
		
		if (isUsing2ndGR(op)) {
			txtRef2.setText(reg(data[2]));
		} else {
			txtRef2.setText("");
		}
		
		txtIndReg.setText(ireg(data[3]));
		if (op==OpCode.AIR || op==OpCode.SIR ||op==OpCode.RFS) {
			txtImmed.setText(data[6]);
		} else 
			txtImmed.setText("");
		
		if (op == OpCode.IN || op == OpCode.OUT) {
			int dId = Integer.parseInt(data[7]);
			txtDeviceId.setText( DeviceType.fromId(dId).name()  );
		} else {
			txtDeviceId.setText( "");
		}
		if (op == OpCode.SRC || op == OpCode.RRC) {
			txtLeftRightMode.setText( "1".equals(data[8]) ? "Left":"Right");
			txtLogicArithMode.setText("1".equals(data[9]) ? "Logical":"Arithmetic");		
			txtSRCount.setText(data[10]);
		} else {
			txtLeftRightMode.setText("");
			txtLogicArithMode.setText("");		
			txtSRCount.setText("");
		}
	}
	
	private void updateALUUI(String... data) {
		txtOpr1.setText(data[0]);
		txtOpr2.setText(data[1]);
		txtOptor.setText(ALUOperator.fromOpt(data[3]).name());
	}
	public void pushData(String type, String data) {
		
		try {

			logger.debug("type: "+ type + ","+ data);

			if (SimConstants.ECONSOLE_ALU_MSG.equals(type)) {
				String []entries = data.split("["+SimConstants.MSG_TO_GUI_DELIMITER+"]");
				updateALUUI(entries);

			} else if (SimConstants.ECONSOLE_DECODE_MSG.equals(type)) {
				String []entries = data.split("["+SimConstants.MSG_TO_GUI_DELIMITER+"]");
				int code = Integer.parseInt(entries[0]);
				OpCode op = OpCode.fromCode(code);

				updateInstrAddressUI(op, entries);
				updateDecodedInstrUI(op, entries);
				updateALUUI("0","0","0","");
			}
		} catch (Exception e) {
			logger.error("Failed to parse Engine console data." + data + ","+ type,e);
		}
	}
	
	
	/**creating and arranging UI components*/
	public void init() {
		JPanel pan = createEAPanel();
		
		GriddedPanel engPanel = new GriddedPanel();

		engPanel.addComponent(pan, 0, 0);
		
		pan = createDecodedPanel();
		engPanel.addComponent(pan, 1, 0);

		pan = createALUPanel();
		engPanel.addComponent(pan, 2, 0);
		
		engPanel.addFilledComponent(btnClose, 4, 0,GridBagConstraints.HORIZONTAL);
		
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				closeMe();
			}
		});
		
		add(engPanel, BorderLayout.NORTH);
		
		setComponentEditable(false);
	}

	
	protected void cleanUp(){
		txtAddress.setText("");
		txtIndirect.setText("");
		txtIndexReg.setText("");
		txtOpr1.setText("");
		txtOpr2.setText("");
		txtOptor.setText("");
		txtOpCode.setText("");
		txtRef1.setText("");
		txtRef2.setText("");
		txtIndReg.setText("");
		txtImmed.setText("");
		txtDeviceId.setText(""); 
		txtLeftRightMode.setText("");
		txtSRCount.setText("");
		txtLogicArithMode.setText("");

	}
	private void setComponentEditable(boolean b) {
		txtAddress.setEditable(b);
		txtAddress.setBackground(Color.YELLOW);
		txtIndirect.setEditable(b);
		txtIndirect.setBackground(Color.YELLOW);
		txtIndexReg.setEditable(b);
		txtIndexReg.setBackground(Color.YELLOW);
		txtOpr1.setEditable(b);
		txtOpr1.setBackground(Color.YELLOW);
		txtOpr2.setEditable(b);
		txtOpr2.setBackground(Color.YELLOW);
		txtOptor.setEditable(b);
		txtOptor.setBackground(Color.YELLOW);
		txtOpCode.setEditable(b);
		txtOpCode.setBackground(Color.YELLOW);
		txtRef1.setEditable(b);
		txtRef1.setBackground(Color.YELLOW);
		txtRef2.setEditable(b);
		txtRef2.setBackground(Color.YELLOW);
		txtIndReg.setEditable(b);
		txtIndReg.setBackground(Color.YELLOW);
		txtImmed.setEditable(b);
		txtImmed.setBackground(Color.YELLOW);
		txtDeviceId.setEditable(b); 
		txtDeviceId.setBackground(Color.YELLOW);
		txtLeftRightMode.setEditable(b);
		txtLeftRightMode.setBackground(Color.YELLOW);
		txtSRCount.setEditable(b);
		txtSRCount.setBackground(Color.YELLOW);
		txtLogicArithMode.setEditable(b);
		txtLogicArithMode.setBackground(Color.YELLOW);
	}
	
	
	/**create Register panel*/
	private JPanel createALUPanel() {
		GriddedPanel gPanel = new GriddedPanel();
		gPanel.setBorder(new TitledBorder(new EtchedBorder(), "ALU (Integer)"));
		gPanel.addComponent(lblOpr1, 0, 0);
		gPanel.addComponent(txtOpr1, 0, 1);
		gPanel.addComponent(lblOpr2, 1, 0);
		gPanel.addComponent(txtOpr2, 1, 1);
		gPanel.addComponent(lblOptor, 2, 0);
		gPanel.addComponent(txtOptor, 2, 1);

		// gPanel.setBorder(new
		// TitledBorder(BorderFactory.createLineBorder(Color.blue)));

		FlowLayout fl = new FlowLayout();
		JPanel wrap = new JPanel();
		wrap.setLayout(fl);
		wrap.add(gPanel);
		fl.setAlignment(FlowLayout.LEFT);
		return wrap;
	}
	/**create Register panel*/
	private JPanel createEAPanel() {
		GriddedPanel gPanel = new GriddedPanel();
		gPanel.setBorder(new TitledBorder(new EtchedBorder(), "Instruction Addressing"));
		gPanel.addComponent(lblAddress, 0, 0);
		gPanel.addComponent(txtAddress, 0, 1);
		gPanel.addComponent(lblIndirect, 1, 0);
		gPanel.addComponent(txtIndirect, 1, 1);
		gPanel.addComponent(lblIndexReg, 2, 0);
		gPanel.addComponent(txtIndexReg, 2, 1);

		// gPanel.setBorder(new
		// TitledBorder(BorderFactory.createLineBorder(Color.blue)));

		FlowLayout fl = new FlowLayout();
		JPanel wrap = new JPanel();
		wrap.setLayout(fl);
		wrap.add(gPanel);
		fl.setAlignment(FlowLayout.LEFT);
		return wrap;
	}
	
}
