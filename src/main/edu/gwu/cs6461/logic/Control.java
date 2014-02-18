/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gwu.cs6461.logic;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.logic.unit.MMU;
import edu.gwu.cs6461.sim.bridge.Observer;
import edu.gwu.cs6461.sim.common.ALUFlags;
import edu.gwu.cs6461.sim.common.ALUOperator;
import edu.gwu.cs6461.sim.common.ConditionCode;
import edu.gwu.cs6461.sim.common.HardwarePart;
import edu.gwu.cs6461.sim.exception.MemoryException;

/**
 * This class has the main logic that calls the different classes needed to finish an instruction.
 * Each instruction is broken down and put in a function in this class.
 * To run the RunInstruction function is called and depending on the opcode from IR
 * it will choose which instruction function to run.
 *  
 * 
 */
public class Control {
	private final static Logger logger = Logger.getLogger(Control.class);
	//create registers shared by most instructions.
	public Register MAR = new Register(HardwarePart.MAR.getBit(),HardwarePart.MAR.getName());
	public Register MDR = new Register(HardwarePart.MDR.getBit(),HardwarePart.MDR.getName());
	//create result register this contains the final result

	
	//from ALU calculation
	@Deprecated private Register RES = new Register(HardwarePart.RES.getBit(),HardwarePart.RES.getName());
	
	public Control(){
	}
	//create observer for them to be updated to GUI
	public void setRegisterObserver(Observer obs){
		MAR.register(obs);
		MDR.register(obs);
	}
    public void clearObserver(){
    	MAR.clear();
    	MDR.clear();
    }
	

	//TODO (PART2) Fetch IR from memory
	public void FetchIR(int PC, IR IRobject) {

	}
	//this creates a new decode class object which breaks down the instruction
	//into the different parts opcode, r,x,address,[,I] (depending the instruction)
	public void Decode(IR IRobject) {
		Decode dec = new Decode();
		dec.decodeSwitch(IRobject);
	}
	/**this functions runs all instructions so all the objects created in the cpucontroller
     *class are passed here, this allows the function to share the appropriate objects with
	 *the instruction functions.
	 * @run depending on the opcode from IR the right instruction is called
	*/
	public void RunInstruction(IR IRobject, RF RFtable, XF XFtable, MMU Mem,
			ALU ALU, Register CC) {
		if (IRobject.getOpCode() == 1)
			LDR(IRobject, RFtable, XFtable, Mem, CC);
		else if (IRobject.getOpCode() == 2)
			STR(IRobject, RFtable, XFtable, Mem);
		else if (IRobject.getOpCode() == 3)
			LDA(IRobject, RFtable, XFtable, Mem);
		else if (IRobject.getOpCode() == 4)
			AMR(IRobject, RFtable, XFtable, Mem, ALU,CC);
		else if (IRobject.getOpCode() == 5)
			SMR(IRobject, RFtable, XFtable, Mem, ALU,CC);
		else if (IRobject.getOpCode() == 6)
			AIR(IRobject, RFtable, ALU,CC);
		else if (IRobject.getOpCode() == 7)
			SIR(IRobject, RFtable, ALU, CC);
		else if (IRobject.getOpCode() == 41)
			LDX(IRobject, XFtable, Mem);
		else if (IRobject.getOpCode() == 42)
			STX(IRobject, XFtable, Mem);
	}
	/** 
	 * @param IRobject IR object with decoded instruction
	 * @run	Load Register from memory instruction
	 */
	public void LDR(IR IRobject, RF RFtable, XF XFtable, MMU Mem, Register CC) {
		// TODO (part 2) create an adder class for effective address calculation
		if (IRobject.getXFI() != 0) {
			if (IRobject.getXFI() == 1) {
				
				//example to use ALU to calculate the address
				//TODO  to apply to all
				ALU.getInstance().setALU(IRobject.getAddress(), XFtable.getSwitch(IRobject.getXFI()),
						ALUOperator.Addition, MAR.getSize()).calculate();
				
				if (ALU.getInstance().getFlag() == ALUFlags.Normal) {
					MAR.setData(ALU.getInstance().getResult());
					CC.setData(ConditionCode.NORMAL.getCode());
				} else if (ALU.getInstance().getFlag() == ALUFlags.Overflow) {
					CC.setData(ConditionCode.OVERFLOW.getCode());
					//Exception handling  is needed
				}
				
				/*RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				 MAR.setData(RES.getData());
				*/
				
			} else if (IRobject.getXFI() == 2) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
				
				
			} else if (IRobject.getXFI() == 3) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
				
			}
		} else
			MAR.setData(IRobject.getAddress());
		//put the memory content of MAR address into MDR
		try {
			MDR.setData(Mem.getDataFromMem(MAR.getData()));
		} catch (MemoryException e) {
			logger.error("failed to get data from memory: " + e.getMessage(), e);
		}
		
		
		//if indirect bit is enabled put MDR in MAR and get the memory content 
		//of MAR address and put it in MDR
		if (IRobject.getIndirect() == 1) {
			MAR.setData(MDR.getData());
			try {
				MDR.setData(Mem.getDataFromMem(MAR.getData()));
			} catch (MemoryException e) {
				logger.error("failed to get data from memory: " + e.getMessage(), e);
			}
		}
		//Using the RFI index the setSwitch function decides in which General Register
		//to put the value of MDR in
		RFtable.setSwitch(IRobject.getRFI1(), MDR.getData());
	}
	/**
	 * 
	 * @param IRobject IR object with decoded instruction
	 * @param CC passed to ALU
	 * @run   Add memory to register
	 */
	public void AMR(IR IRobject, RF RFtable, XF XFtable, MMU Mem, ALU ALU, Register CC) {
		// TODO (part 2) create an adder class for effective address calculation
		if (IRobject.getXFI() != 0) {
			if (IRobject.getXFI() == 1) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			} else if (IRobject.getXFI() == 2) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			} else if (IRobject.getXFI() == 3) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			}
		} else
			MAR.setData(IRobject.getAddress());

		try {
			MDR.setData(Mem.getDataFromMem(MAR.getData()));
		} catch (MemoryException e) {
			logger.error("failed to get data from memory: " + e.getMessage(), e);
		}
		//if indirect bit is enabled put MDR in MAR and get the memory content 
		//of MAR address and put it in MDR
		if (IRobject.getIndirect() == 1) {
			MAR.setData(MDR.getData());
			try {
				MDR.setData(Mem.getDataFromMem(MAR.getData()));
			} catch (MemoryException e) {
				logger.error("failed to get data from memory: " + e.getMessage(), e);
			}
		}
		//send General Register as operand1 and MDR value(memory value)
		//as operand2 two to ALU.
		ALU.Calculate(RFtable.getSwitch(IRobject.getRFI1()), MDR.getData(),
				IRobject.getOpCode(), RES,CC);
		//if CC register is 0 it means the calculation was ok with no errors
		//so put the ALU result in the general Register selected
		if(CC.getData()==ConditionCode.NORMAL.getCode()){
			RFtable.setSwitch(IRobject.getRFI1(), RES.getData());
		}
	}
	/**
	 * @param IRobject IR object with decoded instruction
	 * @run store register to memory
	 */
	public void STR(IR IRobject, RF RFtable, XF XFtable, MMU Mem) {
		// TODO (part 2) create an adder class for effective address calculation
		if (IRobject.getXFI() != 0) {
			if (IRobject.getXFI() == 1) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			} else if (IRobject.getXFI() == 2) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			} else if (IRobject.getXFI() == 3) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			}
		} else
			MAR.setData(IRobject.getAddress());
		//if indirect is set put the memory value at MAR in MDR and then pass MDR value
		//to MAR then use MAR address to store General register into memory
		if (IRobject.getIndirect() == 1) {
			try {
				MDR.setData(Mem.getDataFromMem(MAR.getData()));
			} catch (MemoryException e) {
				logger.error("failed to get data from memory: " + e.getMessage(), e);
			}
			MAR.setData(MDR.getData());
			Mem.setData(MAR.getData(), RFtable.getSwitch(IRobject.getRFI1()));
		}else
			Mem.setData(MAR.getData(), RFtable.getSwitch(IRobject.getRFI1()));
	}
/**
 * @param IRobject IR object with decoded instruction
 * @param CC passed to ALU
 * @run	Subtract Memory from Register
 */
	public void SMR(IR IRobject, RF RFtable, XF XFtable, MMU Mem, ALU ALU, Register CC) {
		// TODO (part 2) create an adder class for effective address calculation
		if (IRobject.getXFI() != 0) {
			if (IRobject.getXFI() == 1) {
				RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
				
			} else if (IRobject.getXFI() == 2) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			} else if (IRobject.getXFI() == 3) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			}
		} else
			MAR.setData(IRobject.getAddress());

		try {
			MDR.setData(Mem.getDataFromMem(MAR.getData()));
		} catch (MemoryException e) {
			logger.error("failed to get data from memory: " + e.getMessage(), e);
		}

		if (IRobject.getIndirect() == 1) {
			MAR.setData(MDR.getData());
			try {
				MDR.setData(Mem.getDataFromMem(MAR.getData()));
			} catch (MemoryException e) {
				logger.error("failed to get data from memory: " + e.getMessage(), e);			}
		}
		//send General Register as operand1 and MDR value(memory value)
		//as operand2 two to ALU.
		ALU.Calculate(RFtable.getSwitch(IRobject.getRFI1()), MDR.getData(),
				IRobject.getOpCode(), RES,CC);
		//if CC register is 0 it means the calculation was ok with no errors
		//so put the ALU result in the general Register selected
		if(CC.getData()==ConditionCode.NORMAL.getCode()){
			RFtable.setSwitch(IRobject.getRFI1(), RES.getData());
		}
	}
/**
 * @param IRobject IR object with decoded instruction
 * @param CC passed to ALU
 * @run	Add Immediate to Register
 */
	public void AIR(IR IRobject, RF RFtable, ALU ALU, Register CC) {
		// if immed is zero no need to change register but the value is
		// reinserted into the register
		// that is because the single step mode only works when the set
		// functions are called
		if (IRobject.getImmed() == 0) {
			RFtable.setSwitch(IRobject.getRFI1(),
					RFtable.getSwitch(IRobject.getRFI1()));
		}
		// if the General Register is empty than just copy and paste the immediate
		// into the register(because the immediate for now is only positive values)
		if (RFtable.getSwitch(IRobject.getRFI1()) == 0) {
			RFtable.setSwitch(IRobject.getRFI1(), IRobject.getImmed());
		}
		// else add the register to the immediate and put back the value in the
		// register
		else {
			//send General Register selected as operand1 and immediate
			//as operand2 two to ALU.
			// ALU: add the register to the immediate and put back the value in the
			// register
			ALU.Calculate(RFtable.getSwitch(IRobject.getRFI1()),
					IRobject.getImmed(), IRobject.getOpCode(), RES, CC);
			//if CC register is 0 it means the calculation was ok with no errors
			//so put the ALU result in the general Register selected
			if(CC.getData()==ConditionCode.NORMAL.getCode()){
				RFtable.setSwitch(IRobject.getRFI1(), RES.getData());
			}
		}
	}
/**
 * @param IRobject IR object with decoded instruction
 * @param CC passed to ALU
 * @run	Subtract Immediate from Register
 */
	public void SIR(IR IRobject, RF RFtable, ALU ALU, Register CC) {
		// if immed is zero no need to change register but the value is
		// reinserted into the register
		// that is because the single step mode only works when the set
		// functions are called
		if (IRobject.getImmed() == 0) {
			RFtable.setSwitch(IRobject.getRFI1(),
					RFtable.getSwitch(IRobject.getRFI1()));
		}
		// if the General Register is empty the ALU is still called
		//because it has the ability to calculated 20bit negative values
		//immed*(-1) will sit the 32bit in java
		
		else {
			//send General Register selected as operand1 and immediate
			//as operand2 two to ALU.
			// ALU: subtract the register from the immediate and put back the value in the
			// register
			ALU.Calculate(RFtable.getSwitch(IRobject.getRFI1()),
					IRobject.getImmed(), IRobject.getOpCode(), RES, CC);
			//if CC register is 0 it means the calculation was ok with no errors
			//so put the ALU result in the general Register selected
			if(CC.getData()==ConditionCode.NORMAL.getCode()){
				RFtable.setSwitch(IRobject.getRFI1(), RES.getData());
			}
		}
		
	}
/**
 * @param IRobject IR object with decoded instruction
 * @run	Load Register with Address
 */
	public void LDA(IR IRobject, RF RFtable, XF XFtable, MMU Mem) {
		// TODO (part 2) create an adder class for effective address calculation
		if (IRobject.getXFI() != 0) {
			if (IRobject.getXFI() == 1) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			} else if (IRobject.getXFI() == 2) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			} else if (IRobject.getXFI() == 3) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			}
		} else {
			MAR.setData(IRobject.getAddress());
		}
				
		//if indirect is set than get the contents of memory at location MAR and pass it to MDR
		//than pass MDR value To MAR and pass MAR value to general register
		if (IRobject.getIndirect() == 1) {
			try {
				MDR.setData(Mem.getDataFromMem(MAR.getData()));
			} catch (MemoryException e) {
				logger.error("failed to get data from memory: " + e.getMessage(), e);
			}
			MAR.setData(MDR.getData());
			RFtable.setSwitch(IRobject.getRFI1(), MAR.getData());
		}
		else
			//if indirect is not set than put effective address into general register
			RFtable.setSwitch(IRobject.getRFI1(), MAR.getData());
		
	}
	/**
	 * @param IRobject IR object with decoded instruction
	 * @run	Load Index Register from Memory
	 */
	public void LDX(IR IRobject, XF XFtable, MMU Mem) {
		//get memory contents of address MAR and put it in MDR
		MAR.setData(IRobject.getAddress());
		try {
			//TODO what is it is loading from memory which has too large of value ???
			MDR.setData(Mem.getDataFromMem(MAR.getData()));
		} catch (MemoryException e) {
			logger.error("failed to get data from memory: " + e.getMessage(), e);
		}
		//if indirect is set than put MDR in MAR and get the memory contents of location MAR
		//and put them in MDR
		if (IRobject.getIndirect() == 1) {
			MAR.setData(MDR.getData());
			try {
				//TODO what is it is loading from memory which has too large of value ???
				MDR.setData(Mem.getDataFromMem(MAR.getData()));
			} catch (MemoryException e) {
				logger.error("failed to get data from memory: " + e.getMessage(), e);
			}
		}
		//if indirect is not set put original MDR in index register
		XFtable.setSwitch(IRobject.getXFI(), MDR.getData());
	}
	/**
	 * @param IRobject IR object with decoded instruction
	 * @run	Store Index Register to Memory
	 */
	public void STX(IR IRobject, XF XFtable, MMU Mem) {
		MAR.setData(IRobject.getAddress());
		//if indirect is not set than just put the index register content into memory
		//IR address location		

		int xfi = IRobject.getXFI();
		Mem.setData(MAR.getData(), XFtable.getSwitch(xfi),  XFtable.getSize(xfi));
		
		//if indirect is set than put IR address in MAR and than get the contents of the memory at MAR address
		//put that in MDR then put MDR value into MAR
		//then the index register value is insert into memory at indirect location MAR
		if (IRobject.getIndirect() == 1) {
			//MAR.setData(IRobject.getAddress());
			try {
				MDR.setData(Mem.getDataFromMem(MAR.getData()));
			} catch (MemoryException e) {
				logger.error("failed to get data from memory: " + e.getMessage(), e);
			}
			MAR.setData(MDR.getData());
			xfi = IRobject.getXFI();
			Mem.setData(MAR.getData(), XFtable.getSwitch(xfi), XFtable.getSize(xfi));
		}
	}
}
