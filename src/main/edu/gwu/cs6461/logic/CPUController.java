package edu.gwu.cs6461.logic;

import edu.gwu.cs6461.sim.bridge.*;
import edu.gwu.cs6461.sim.ui.*;

import org.apache.log4j.Logger;

public class CPUController extends Thread {
	public IR IRobject = new IR();
	public RF RFtable = new RF();
	public XF XFtable = new XF();
	public Register SS = new Register();
	public Register PC = new Register();
	public Register CC = new Register();
	public Control cpuControl = new Control();
	public ALU ALU = new ALU();
	public static CPUController instance = new CPUController();
	
	private boolean suspendflag = false;
	private final static Logger logger = Logger.getLogger(MainSimFrame.class);

	// Keep a weak reference of mainSimFrame
	private MainSimFrame mainFrame = null;

	// CPU holds a week reference of the memory but CPU doesn't own the memory
	private CPUController() {
		// Set register size
		PC.setSize(13);
		SS.setSize(1);
		CC.setSize(4);

		// Set register name
		SS.setName("SS");
		PC.setName("PC");
		CC.setName("CC");
	}

	//In java a thread can't excrete twice so we have to recreate it after done.
	public static void recreateCPUController(boolean isReserveData) {
		CPUController tmpController = new CPUController();
		
		if(isReserveData){
			tmpController.IRobject = instance.IRobject;
			tmpController.RFtable = instance.RFtable;
			tmpController.XFtable = instance.XFtable;
			tmpController.SS = instance.SS;
			tmpController.PC = instance.PC;
			tmpController.cpuControl = instance.cpuControl;
tmpController.CC=instance.CC;
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

	public void setMainFrame(MainSimFrame mf) {
		mainFrame = mf;
	}

	public void setRegisterObserver(Observer obs) {
		PC.register(obs);
		IRobject.register(obs);
		RFtable.setRegisterObserver(obs);
		XFtable.setRegisterObserver(obs);
		cpuControl.setRegisterObserver(obs);
		CC.register(obs);
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
		cpuControl.RunInstruction(IRobject, RFtable, XFtable,
				Memory.shareInstance(), ALU,CC);

		// PC+1 then loop the whole process(Phase 2)

		// Finish all instructions, show message to user to let it know
		mainFrame.showProgrameFinishDialog();

		logger.debug("CPU thread ends.");
	}
}
