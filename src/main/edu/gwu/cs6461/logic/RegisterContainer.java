package edu.gwu.cs6461.logic;

import edu.gwu.cs6461.sim.bridge.Observer;
import edu.gwu.cs6461.sim.common.HardwarePart;

/**This class is like a data class and just as an container which will do nothing but contain the registers
 *@Revised   Jan 20, 2014 - 11:24:39 AM
 *@Revised   Mar 8, 2014 - 11:18:42 PM  
 */
public class RegisterContainer {
	/**Create registers that are shared between the different*/
	public IR IRobject = new IR();
	
	/**Program counter*/
	public Register PC = new Register(HardwarePart.PC.getBit(),HardwarePart.PC.getName());
	/**Condition code*/
	public Register CC = new Register(HardwarePart.CC.getBit(),HardwarePart.CC.getName());
	/**Machine fault register*/
	public Register MFR =  new Register(HardwarePart.MFR.getBit(),HardwarePart.MFR.getName());
	
	/**Debug register when set run program in single step mode */
	public Register SS = new Register(1,"SS");
	
	/**Debug register when set run program in single instruction mode */
	public Register SI = new Register(1,"SI");
	
	/**register file for general registers*/
	public RF RFtable = new RF();
	/**register file for index registers*/
	public XF XFtable = new XF();
	
	/**Memory address register*/
	public Register MAR = new Register(HardwarePart.MAR.getBit(),HardwarePart.MAR.getName());
	/**Memory data register*/
	public Register MDR = new Register(HardwarePart.MDR.getBit(),HardwarePart.MDR.getName());
	/**Machine status register*/
	public Register MSR = new Register(HardwarePart.MSR.getBit(),HardwarePart.MSR.getName());
	
	/** from ALU calculation :internal register  */
	public Register RES = new Register(HardwarePart.RES.getBit(),HardwarePart.RES.getName());
	/** from ALU calculation :internal register  */
	public Register RES2 = new Register(HardwarePart.RES.getBit(),"RES2");
	/** Class to hold multiplication result */
	public Multiply Multi = new Multiply(40,"Multi");
	
	/** define floating point register 0*/
	public Register FR0 = new Register(HardwarePart.FR0.getBit(),HardwarePart.FR0.getName());
	/** define floating point register 1*/
	public Register FR1 = new Register(HardwarePart.FR1.getBit(),HardwarePart.FR1.getName());
	
	/**Deregister Observer for all registers*/
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
    	FR0.clear();
    	FR1.clear();
	}
	
	/**Register Observer for all registers*/
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
    	FR0.register(obs);
    	FR1.register(obs);
	}
}
