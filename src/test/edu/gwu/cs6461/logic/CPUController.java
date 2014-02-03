package edu.gwu.cs6461.logic;

public class CPUController extends Thread {
	private IR IRobject = new IR();
	private RF RFtable = new RF();
	private XF XFtable = new XF();
	private ALU ALU = new ALU();
	private Register SS = new Register();
	private Register PC = new Register();
	private static final CPUController instance = new CPUController();

	// CPU holds a week reference of the memory but CPU doesn't own the memory
	private CPUController() {
		// Set register
		PC.setSize(13);
		SS.setSize(1);
	}

	// Singleton method
	public static CPUController shareInstance() {
		return instance;
	}

	// This method is deprecated but we'll still use it
	public void checkSingleStepModel() {
		if (SS.getData() == 1) {
			this.suspend();
		}
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
