package edu.gwu.cs6461.logic;
/**
 * This class holds most of the objects need to run the CPU. 
 * To run it first checks if the user clicked single mode or run which would set its debug
 * Register (SS), which is used as a condition to control the CPU thread by suspending it
 * after each microcode step is finished. Example after decode, effective address
 * ALU calculation etc.
 * 
 */
import static edu.gwu.cs6461.sim.common.SimConstants.FILE_COMMENT;
import static edu.gwu.cs6461.sim.common.SimConstants.FILE_DATA_HEAD;
import static edu.gwu.cs6461.sim.common.SimConstants.FILE_INSTRUCTION_HEAD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import edu.gwu.cs6461.logic.unit.MMU;
import edu.gwu.cs6461.logic.unit.MainMemory;
import edu.gwu.cs6461.sim.bridge.*;
import edu.gwu.cs6461.sim.common.HardwarePart;
import edu.gwu.cs6461.sim.ui.*;

import org.apache.log4j.Logger;

public class CPUController extends Thread {
	//Create registers that are shared between the different
	//cpu classes
	public IR IRobject = new IR();
	public Register PC = new Register(HardwarePart.PC.getBit(),HardwarePart.PC.getName());
	public Register CC = new Register(HardwarePart.CC.getBit(),HardwarePart.CC.getName());
	public Register MFR =  new Register(HardwarePart.MFR.getBit(),HardwarePart.MFR.getName());
	//Debug register when set run program in single step mode
	public Register SS = new Register(1,"SS");
	
	//create RF and XF tables
	public RF RFtable = new RF();
	public XF XFtable = new XF();
	
	public Control cpuControl = new Control();
	private static CPUController instance = new CPUController();
	
	private boolean suspendflag = false;
	private final static Logger logger = Logger.getLogger(CPUController.class);

	// Keep a weak reference of mainSimFrame
	private MainSimFrame mainFrame = null;
	private MMU mmu = MMU.instance();

	// CPU holds a weak reference of the memory but CPU doesn't own the memory
	private CPUController() {
		
	}

	//This recreates the cpu thread after an instruction is finished
	//and a new one is started
	public void recreateCPUController(boolean isReserveData) {
		CPUController tmpController = new CPUController();
		
		if(isReserveData){
			tmpController.IRobject = instance.IRobject;
			tmpController.RFtable = instance.RFtable;
			tmpController.XFtable = instance.XFtable;
			tmpController.SS = instance.SS;
			tmpController.PC = instance.PC;
			tmpController.cpuControl = instance.cpuControl;
			tmpController.CC=instance.CC;
			tmpController.MFR=instance.MFR;
		}
		
		instance = tmpController;
	}

	// Singleton method
	public static CPUController shareInstance() {
		return instance;
	}

	// Suspend is deprecated but we'll still use it
	public void checkSingleStepModel() {
		if (SS.getData() == 1 && this.isAlive()) {
			this.Suspend();
		}
	}

	public void setMemData(int address, String data){
		mmu.setData(address, data);
	}
	
	public void setMainFrame(MainSimFrame mf) {
		mainFrame = mf;
	}
	
	public void clearObserver(){
		PC.clear();
		IRobject.clear();
		RFtable.clearObserver();
		XFtable.clearObserver();
		cpuControl.clearObserver();
		CC.clear();
		MFR.clear();
//		Memory.shareInstance().clear();
		mmu.clearObserver();
	}

	public void setRegisterObserver(Observer obs) {
		PC.register(obs);
		IRobject.register(obs);
		RFtable.setRegisterObserver(obs);
		XFtable.setRegisterObserver(obs);
		cpuControl.setRegisterObserver(obs);
		CC.register(obs);
		MFR.register(obs);
//		Memory.shareInstance().register(obs);
		mmu.registerObserver(obs);
	}

	public void Suspend() {
		suspendflag = true;
		super.suspend();
	}

	public void Resume() {
		suspendflag = false;
		super.resume();
	}

	public boolean isSuspended() {
		return suspendflag;
	}

	
	
	
	public void run() {
		logger.debug("CPU thread begins.");

		// For the first time we begin the process, we'll check if the IR is
		// empty or not
		// TODO:If the IR is Empty then fetch the instruction from the
		// memory(Phase 2)
		if (IRobject.isEmpty()) {
			cpuControl.FetchIR(PC.getData(), IRobject);
		}

		cpuControl.Decode(IRobject);
		cpuControl.RunInstruction(IRobject, RFtable, XFtable, mmu, ALU.getInstance(),CC, PC, MFR);

		// PC+1 then loop the whole process(Phase 2)

		// Finish all instructions, show message to user to let it know
		mainFrame.showProgrameFinishDialog();

		logger.debug("CPU thread ends.");
	}


	/**
	 * load up program from file into memory
	 * @param fileName
	 */
	public void loadFromFile(String fileName) {
		FileInputStream fis = null;
		BufferedReader br = null;
		try {
			File f = new File(fileName);
			fis = new FileInputStream(f);
			br = new BufferedReader(new FileReader(f));
			
			String line;
			int instrPos = 100, dataPos = 150;
			boolean currData = true;
			boolean dheader = false,iheader = false;
			while ((line = br.readLine()) != null) {
				
				if (line.startsWith(FILE_COMMENT) || "".equals(line.trim())) {
					continue;
				}
				
				int cmIdx = line.indexOf(FILE_COMMENT);
				if (cmIdx> 1) {
					line = line.substring(0, cmIdx);
				}
				line = line.trim();
				
				if (line.startsWith(FILE_DATA_HEAD)) {
					int pos = line.indexOf(":");
					if (pos >1) {
						dataPos = Integer.parseInt(line.substring(pos+1));
						logger.debug("save " + FILE_DATA_HEAD +" from " + dataPos);
					}
					currData = true;
					dheader = true;
				} else {
					dheader = false;
				}
				if (line.startsWith(FILE_INSTRUCTION_HEAD)) {
					int pos = line.indexOf(":");
					if (pos >1) {
						instrPos = Integer.parseInt(line.substring(pos+1));
						logger.debug("save " + FILE_INSTRUCTION_HEAD +" from " + instrPos);
					}
					currData = false;
					iheader = true;
				} else {
					iheader = false;
				}
			
				if (currData && !dheader) {
					mmu.setData(dataPos++, line);
				} else if (!currData && !iheader) {
					mmu.setInstr(instrPos++, line);
				}
			}
			
		} catch (IOException e) {
			logger.error("failed to read file ",e);
		} finally{
			try {
				fis.close();
				br.close();
			} catch (Exception e) { }
		}
	}




}
