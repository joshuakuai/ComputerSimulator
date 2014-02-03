package edu.gwu.cs6461.logic;

public class CPUController extends Thread {
	public IR IRobject = new IR();
	public RF RFtable = new RF();
	public XF XFtable = new XF();
	public Register SS = new Register();
	public Register PC = new Register();
	public Register MAR = new Register();
	public Register MBR = new Register();
	
	private ALU ALU = new ALU();
	private static final CPUController instance = new CPUController();
	private boolean suspendflag = false;

	// CPU holds a week reference of the memory but CPU doesn't own the memory
	private CPUController() {
		// Set register
		PC.setSize(13);
		SS.setSize(1);
		MAR.setSize(20);
		MBR.setSize(20);
	}

	// Singleton method
	public static CPUController shareInstance() {
		return instance;
	}

	// This method is deprecated but we'll still use it
	public void checkSingleStepModel() {
		if (SS.getData() == 1 && this.isAlive()) {
			this.Suspend();
		}
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
		Control cpuControl = new Control();

		// For the first time we begin the process, we'll check if the IR is
		// empty or not
		// TODO:If the IR is Empty then fetch the instruction from the
		// memory(Phase 2)
		if (IRobject.isEmpty()) {
			cpuControl.FetchIR(PC.getData(), IRobject);
		}

		cpuControl.Decode(IRobject);
		cpuControl.RunInstruction(IRobject, RFtable, XFtable,
				Memory.shareInstance(), ALU);

		// PC+1 then loop the whole process(Phase 2)
	}
}
