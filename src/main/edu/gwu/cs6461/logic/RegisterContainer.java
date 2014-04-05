package edu.gwu.cs6461.logic;

import edu.gwu.cs6461.sim.bridge.Observer;
import edu.gwu.cs6461.sim.common.HardwarePart;

//This class is like a data class and just as an container which will do nothing but contain the registers
public class RegisterContainer {
	//Create registers that are shared between the different
	//cpu classes
	public IR IRobject = new IR();
	public Register PC = new Register(HardwarePart.PC.getBit(),HardwarePart.PC.getName());
	public Register CC = new Register(HardwarePart.CC.getBit(),HardwarePart.CC.getName());
	public Register MFR =  new Register(HardwarePart.MFR.getBit(),HardwarePart.MFR.getName());
	
	//Debug register when set run program in single step mode
	public Register SS = new Register(1,"SS");
	
	//Debug register when set run program in single instruction mode
	public Register SI = new Register(1,"SI");
	
	//create RF and XF tables
	public RF RFtable = new RF();
	public XF XFtable = new XF();
	
	//create registers shared by most instructions.
	public Register MAR = new Register(HardwarePart.MAR.getBit(),HardwarePart.MAR.getName());
	public Register MDR = new Register(HardwarePart.MDR.getBit(),HardwarePart.MDR.getName());

	public Register MSR = new Register(HardwarePart.MSR.getBit(),HardwarePart.MSR.getName());
	
	//from ALU calculation
	public Register RES = new Register(HardwarePart.RES.getBit(),HardwarePart.RES.getName());
	public Register RES2 = new Register(HardwarePart.RES.getBit(),"RES2");
	public Multiply Multi = new Multiply(40,"Multi");
	
	void clearAllRegistersObserver(){
		PC.clear();
		IRobject.clear();
		RFtable.clearObserver();
		XFtable.clearObserver();
		CC.clear();
		MFR.clear();
    	MAR.clear();
    	MDR.clear();
    	MSR.clear();
	}
	
	void registerObserver(Observer obs){
		CC.register(obs);
		MFR.register(obs);
		PC.register(obs);
		IRobject.register(obs);
		RFtable.setRegisterObserver(obs);
		XFtable.setRegisterObserver(obs);
		MAR.register(obs);
		MDR.register(obs);
		MSR.register(obs);
	}
}
