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
import edu.gwu.cs6461.sim.common.SimConstants;
import edu.gwu.cs6461.sim.exception.MemoryException;
import edu.gwu.cs6461.sim.util.Convertor;

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
	private Register RES = new Register(HardwarePart.RES.getBit(),HardwarePart.RES.getName());
	private Register RES2 = new Register(HardwarePart.RES.getBit(),"RES2");
	private Multiply Multi = new Multiply(40,"Multi");
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
	public void RunInstruction(IR IRobject, RF RFtable, XF XFtable, MMU Mem, ALU ALU, Register CC, Register PC, Register MFR) 
	{
		int OpCode=Integer.parseInt(IRobject.getIRstring().substring(0, 6),2);
		if(OpCode ==1 || OpCode==2 || OpCode==3||OpCode==4||OpCode==5||OpCode==6||OpCode==7||OpCode==41||OpCode==42||OpCode==20
			||OpCode==21||OpCode==22||OpCode==23||OpCode==24||OpCode==25||OpCode==31||OpCode==32||OpCode==10
			||OpCode==11||OpCode==12||OpCode==13|OpCode==14||OpCode==15||OpCode==16||OpCode==17){
			
			Decode(IRobject);
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
			else if(OpCode==20)
				MLT(IRobject, RFtable,ALU,CC);
			else if(OpCode==21)
				DVD(IRobject,RFtable,ALU,CC);
			else if(OpCode==22)
				TRR(IRobject,RFtable,CC);
			else if(OpCode==23)
				AND(IRobject,RFtable);
			else if(OpCode==24)
				ORR(IRobject,RFtable);
			else if(OpCode==25)
				NOT(IRobject,RFtable);
			else if(OpCode==31)
				SRC(IRobject,RFtable);
			else if(OpCode==32)
				RRC(IRobject,RFtable);
			else if(OpCode==10)
				JZ(IRobject,RFtable,XFtable, Mem, PC);
			else if(OpCode==11)
				JNE(IRobject,RFtable,XFtable, Mem, PC);
			else if(OpCode==12)
				JCC(IRobject,XFtable, Mem, CC, PC);
			else if(OpCode==13)
				JMP(IRobject,XFtable, Mem, PC);
			else if(OpCode==14)
				JSR(IRobject,RFtable,XFtable, Mem, PC);
			else if(OpCode==15)
				RFS(IRobject,RFtable, PC);
			else if(OpCode==16)
				SOB(IRobject,RFtable,XFtable, Mem, PC);
			else if(OpCode==17)
				JGE(IRobject,RFtable,XFtable, Mem, PC);
		}
		else{
			MFR.setData(2);
		}
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
		ALU.Calculate(RFtable.getSwitch(IRobject.getRFI1()), MDR.getData(),	IRobject.getOpCode(), RES,CC,RES2, Multi);
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
				IRobject.getOpCode(), RES,CC,RES2, Multi);
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
					IRobject.getImmed(), IRobject.getOpCode(), RES, CC, RES2, Multi);
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
					IRobject.getImmed(), IRobject.getOpCode(), RES, CC, RES2,Multi);
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
				logger.error("failed to send data to memory: " + e.getMessage(), e);
			}
			MAR.setData(MDR.getData());
			xfi = IRobject.getXFI();
			Mem.setData(MAR.getData(), XFtable.getSwitch(xfi), XFtable.getSize(xfi));
		}
	}
	public void MLT(IR IRobject, RF RFtable, ALU ALU, Register CC){
		ALU.Calculate(RFtable.getSwitch(IRobject.getRFI1()), RFtable.getSwitch(IRobject.getRFI2()),IRobject.getOpCode(), RES, CC, RES2, Multi);
		logger.debug("MLT="+Multi.getResult());
		
		String result = Long.toString(Multi.getResult(),2);
		int upperhalf=0;
		int lowerhalf=0;
		if(CC.getData()==ConditionCode.OVERFLOW.getCode()){
			if (result.length()>20){
				upperhalf= Integer.parseInt(result.substring(0,result.length()-20),2);
				lowerhalf=Integer.parseInt(result.substring(result.length()-20),2);
				RFtable.setSwitch(IRobject.getRFI1(), upperhalf);
				RFtable.setSwitch(IRobject.getRFI1()+1, lowerhalf);
			}
		}else if (CC.getData()==ConditionCode.NORMAL.getCode()){			
				upperhalf= 0;
				lowerhalf=Integer.parseInt(result.substring(0),2);	
				RFtable.setSwitch(IRobject.getRFI1(), upperhalf);
				RFtable.setSwitch(IRobject.getRFI1()+1, lowerhalf);
		}
		logger.debug("HIGHbit="+upperhalf+"  LOWERbit="+lowerhalf);			
	}
	public void DVD(IR IRobject, RF RFtable, ALU ALU, Register CC){
		ALU.Calculate(RFtable.getSwitch(IRobject.getRFI1()), RFtable.getSwitch(IRobject.getRFI2()),IRobject.getOpCode(), RES, CC, RES2, Multi);
		if(CC.getData()==ConditionCode.NORMAL.getCode()){			
			RFtable.setSwitch(IRobject.getRFI1(), RES.getData());
			RFtable.setSwitch(IRobject.getRFI1()+1, RES2.getData());
		}		
	}
	public void TRR(IR IRobject, RF RFtable,Register CC){
		String op1 = "", op2 = "";
		op1= Convertor.getBinFromInt(RFtable.getSwitch(IRobject.getRFI1()), SimConstants.WORD_SIZE);
		op2= Convertor.getBinFromInt(RFtable.getSwitch(IRobject.getRFI2()), SimConstants.WORD_SIZE);		
		int opt1 = Convertor.getSignedValFromBin(op1, SimConstants.WORD_SIZE);
		int opt2 = Convertor.getSignedValFromBin(op2, SimConstants.WORD_SIZE);
		//
		if(opt1 == opt2){
			CC.setData(ConditionCode.EQUALORNOT.getCode());
		}
		else
			CC.setData(0);
	}
	public void AND(IR IRobject, RF RFtable){
		String op1 = "", op2 = "";
		op1= Convertor.getBinFromInt(RFtable.getSwitch(IRobject.getRFI1()), SimConstants.WORD_SIZE);
		op2= Convertor.getBinFromInt(RFtable.getSwitch(IRobject.getRFI2()), SimConstants.WORD_SIZE);		
		int opt1 = Convertor.getSignedValFromBin(op1, SimConstants.WORD_SIZE);
		int opt2 = Convertor.getSignedValFromBin(op2, SimConstants.WORD_SIZE);
		//
		RES.setData(opt1 & opt2);
		RFtable.setSwitch(IRobject.getRFI1(), RES.getData());
	}
	public void ORR(IR IRobject, RF RFtable){
		String op1 = "", op2 = "";
		op1= Convertor.getBinFromInt(RFtable.getSwitch(IRobject.getRFI1()), SimConstants.WORD_SIZE);
		op2= Convertor.getBinFromInt(RFtable.getSwitch(IRobject.getRFI2()), SimConstants.WORD_SIZE);		
		int opt1 = Convertor.getSignedValFromBin(op1, SimConstants.WORD_SIZE);
		int opt2 = Convertor.getSignedValFromBin(op2, SimConstants.WORD_SIZE);
		//
		RES.setData(opt1 | opt2);
		RFtable.setSwitch(IRobject.getRFI1(), RES.getData());
	}
	public void NOT(IR IRobject, RF RFtable){
		String op1 = "";
		op1= Convertor.getBinFromInt(RFtable.getSwitch(IRobject.getRFI1()), SimConstants.WORD_SIZE);		
		int opt1 = Convertor.getSignedValFromBin(op1, SimConstants.WORD_SIZE);
		//
		RES.setData(~opt1);
		RFtable.setSwitch(IRobject.getRFI1(), RES.getData());
	}
	public void SRC(IR IRobject, RF RFtable){
		int count=IRobject.getCount();
		int LogicArithmetic=IRobject.getLogicalorArithmetic();
		int LeftRight=IRobject.getLeftorRight();
		//
		if(count!=0){
			String op1 = "";
			op1= Convertor.getBinFromInt(RFtable.getSwitch(IRobject.getRFI1()), SimConstants.WORD_SIZE);		
			StringBuilder sb = new StringBuilder(op1);
			//
			if(LogicArithmetic==1){
				if(LeftRight==1)
				for (int i=0; i<count;i++){
					sb.append("0");
					sb.deleteCharAt(0);
					logger.debug("Left="+sb);
				}
				if(LeftRight==0){
					int lastPos = sb.length();
					for (int i=0; i<count;i++){
						sb.insert(0,"0");
						sb.deleteCharAt(lastPos);
						logger.debug("Right="+sb);
					}
				}
				RES.setData(Integer.parseInt(sb.toString(), 2));
			}
			else if (LogicArithmetic==0){
				int width = op1.length();
					
				int iVal = Integer.parseInt(op1,2);
					
				String signBit = op1.substring(0,1);
				int result=0;
					//
				if(LeftRight==1){
					result = iVal << count;
					logger.debug("left a="+result);
				}
				else if(LeftRight==0){
					result = iVal >> count;		
					logger.debug("Right a="+Integer.toString(result,2));
				}				
				String res19 = Convertor.getBinFromInt(result, width-1);
				String res20 = signBit + res19;
					
				RES.setData(Integer.parseInt(res20, 2));
			}
			RFtable.setSwitch(IRobject.getRFI1(), RES.getData());
		}
		//else if count is zero do nothing
	}
	public void RRC(IR IRobject, RF RFtable){
		int count=IRobject.getCount();
		int LeftRight=IRobject.getLeftorRight();
		//
		if(count!=0){
			String op1 = "";
			op1= Convertor.getBinFromInt(RFtable.getSwitch(IRobject.getRFI1()), SimConstants.WORD_SIZE);		
			StringBuffer s = new StringBuffer(op1);
			int lastPos = s.length();
			if(LeftRight==0){
				for (int i = 0; i < count; i++) {
					char c = s.charAt(lastPos-1);
					s.insert(0,c);
					s.deleteCharAt(lastPos);
				}
			logger.debug("Rot Right="+s.toString());
			}
			if(LeftRight==1){
				for (int i = 0; i < count; i++) {
					char c = s.charAt(0);
					s.append(c);
					s.deleteCharAt(0);
				}
			logger.debug("Rot Left="+s.toString());
			}
			//
			RES.setData(Integer.parseInt(s.toString(), 2));
			RFtable.setSwitch(IRobject.getRFI1(), RES.getData());
		}
	}
	/////////////////////////////////////////////
	//////////////////////////////////////////////
	public void JZ(IR IRobject,RF RFtable,XF XFtable, MMU Mem,Register PC ){
		if(RFtable.getSwitch(IRobject.getRFI1())==0){
			MAR.setData(IRobject.getAddress());
			if(XFtable.getSize(IRobject.getXFI())!=0){
				if (IRobject.getXFI() != 0) {
					if (IRobject.getXFI() == 1) {
						RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
						MAR.setData(RES.getData());
					} else if (IRobject.getXFI() == 2) {
						RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
						MAR.setData(RES.getData());
					} else if (IRobject.getXFI() == 3) {
						RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
						MAR.setData(RES.getData());
					}
				}				
			}
			//if indirect bit is enabled get the memory content 
			//of MAR address and put it in MDR then copy MDR into MAR
			if (IRobject.getIndirect() == 1) {			
				try {
					MDR.setData(Mem.getDataFromMem(MAR.getData()));
					MAR.setData(MDR.getData());
				} catch (MemoryException e){
					logger.error("failed to get data from memory: " + e.getMessage(), e);
				}			
			}		
			PC.setData(MAR.getData());
			logger.debug("PC JZ="+PC.getData());
		}
		//////////////////////////
		else
			PC.setData(PC.getData()+1);
	}
	//////
	public void JNE(IR IRobject,RF RFtable,XF XFtable, MMU Mem,Register PC ){
		if(RFtable.getSwitch(IRobject.getRFI1())!=0){
			MAR.setData(IRobject.getAddress());
			if(XFtable.getSize(IRobject.getXFI())!=0){
				if (IRobject.getXFI() != 0) {
					if (IRobject.getXFI() == 1) {
						RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
						MAR.setData(RES.getData());
					} else if (IRobject.getXFI() == 2) {
						RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
						MAR.setData(RES.getData());
					} else if (IRobject.getXFI() == 3) {
						RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
						MAR.setData(RES.getData());
					}
				}				
			}
			//if indirect bit is enabled get the memory content 
			//of MAR address and put it in MDR then copy MDR into MAR
			if (IRobject.getIndirect() == 1) {			
				try {
					MDR.setData(Mem.getDataFromMem(MAR.getData()));
					MAR.setData(MDR.getData());
				} catch (MemoryException e){
					logger.error("failed to get data from memory: " + e.getMessage(), e);
				}			
			}		
			PC.setData(MAR.getData());
			logger.debug("PC JNE="+PC.getData());
		}
		//////////////////////////
		else
			PC.setData(PC.getData()+1);
	}
	public void JCC(IR IRobject,XF XFtable, MMU Mem,Register CC,Register PC ){
		if(CC.getData()==1){
			MAR.setData(IRobject.getAddress());
			if(XFtable.getSize(IRobject.getXFI())!=0){
				if (IRobject.getXFI() != 0) {
					if (IRobject.getXFI() == 1) {
						RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
						MAR.setData(RES.getData());
					} else if (IRobject.getXFI() == 2) {
						RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
						MAR.setData(RES.getData());
					} else if (IRobject.getXFI() == 3) {
						RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
						MAR.setData(RES.getData());
					}
				}				
			}
			//if indirect bit is enabled get the memory content 
			//of MAR address and put it in MDR then copy MDR into MAR
			if (IRobject.getIndirect() == 1) {			
				try {
					MDR.setData(Mem.getDataFromMem(MAR.getData()));
					MAR.setData(MDR.getData());
				} catch (MemoryException e){
					logger.error("failed to get data from memory: " + e.getMessage(), e);
				}			
			}		
			PC.setData(MAR.getData());
			logger.debug("PC JCC="+PC.getData());
		}
		//////////////////////////
		else
			PC.setData(PC.getData()+1);
	}
	public void JMP(IR IRobject,XF XFtable, MMU Mem,Register PC ){		
		MAR.setData(IRobject.getAddress());
		if(XFtable.getSize(IRobject.getXFI())!=0){
			if (IRobject.getXFI() != 0) {
				if (IRobject.getXFI() == 1) {
					RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
					MAR.setData(RES.getData());
				} else if (IRobject.getXFI() == 2) {
					RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
					MAR.setData(RES.getData());
				} else if (IRobject.getXFI() == 3) {
					RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
					MAR.setData(RES.getData());
				}
			}				
		}
		//if indirect bit is enabled get the memory content 
		//of MAR address and put it in MDR then copy MDR into MAR
		if (IRobject.getIndirect() == 1) {			
			try {
				MDR.setData(Mem.getDataFromMem(MAR.getData()));
				MAR.setData(MDR.getData());
			} catch (MemoryException e){
				logger.error("failed to get data from memory: " + e.getMessage(), e);
			}			
		}		
		PC.setData(MAR.getData());
		logger.debug("PC JMP="+PC.getData());
		
	}
	public void JSR(IR IRobject,RF RFtable, XF XFtable, MMU Mem,Register PC ){
		//save PC+1 into R3
		RFtable.setR3(PC.getData()+1);
		
		MAR.setData(IRobject.getAddress());
		if(XFtable.getSize(IRobject.getXFI())!=0){
			if (IRobject.getXFI() != 0) {
				if (IRobject.getXFI() == 1) {
					RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
					MAR.setData(RES.getData());
				} else if (IRobject.getXFI() == 2) {
					RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
					MAR.setData(RES.getData());
				} else if (IRobject.getXFI() == 3) {
					RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
					MAR.setData(RES.getData());
				}
			}				
		}
		//if indirect bit is enabled get the memory content 
		//of MAR address and put it in MDR then copy MDR into MAR
		if (IRobject.getIndirect() == 1) {			
			try {
				MDR.setData(Mem.getDataFromMem(MAR.getData()));
				MAR.setData(MDR.getData());
			} catch (MemoryException e){
				logger.error("failed to get data from memory: " + e.getMessage(), e);
			}			
		}		
		PC.setData(MAR.getData());
		logger.debug("PC JMP="+PC.getData());
		
	}
	public void RFS(IR IRobject,RF RFtable,Register PC ){
		RFtable.setR0(IRobject.getImmed());
		PC.setData(RFtable.getR3());
	}
	public void SOB(IR IRobject,RF RFtable,XF XFtable, MMU Mem,Register PC ){
		//subtract 1 from the selected general register
		int RegValue=RFtable.getSwitch(IRobject.getRFI1()) - 1;
		RFtable.setSwitch(IRobject.getRFI1(), RegValue );
		//check if the selected general register is greater than zero
		//if true put effective address or content of effective address
		//in PC
		if(RFtable.getSwitch(IRobject.getRFI1())>0){
			MAR.setData(IRobject.getAddress());
			if(XFtable.getSize(IRobject.getXFI())!=0){
				if (IRobject.getXFI() != 0) {
					if (IRobject.getXFI() == 1) {
						RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
						MAR.setData(RES.getData());
					} else if (IRobject.getXFI() == 2) {
						RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
						MAR.setData(RES.getData());
					} else if (IRobject.getXFI() == 3) {
						RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
						MAR.setData(RES.getData());
					}
				}				
			}
			//if indirect bit is enabled get the memory content 
			//of MAR address and put it in MDR then copy MDR into MAR
			if (IRobject.getIndirect() == 1) {			
				try {
					MDR.setData(Mem.getDataFromMem(MAR.getData()));
					MAR.setData(MDR.getData());
				} catch (MemoryException e){
					logger.error("failed to get data from memory: " + e.getMessage(), e);
				}			
			}		
			PC.setData(MAR.getData());
			logger.debug("PC JGE="+PC.getData());
		}
		//Increment the PC by 1
		else
			PC.setData(PC.getData()+1);
	}
	public void JGE(IR IRobject,RF RFtable,XF XFtable, MMU Mem,Register PC ){
		if(RFtable.getSwitch(IRobject.getRFI1())>=0){
			MAR.setData(IRobject.getAddress());
			if(XFtable.getSize(IRobject.getXFI())!=0){
				if (IRobject.getXFI() != 0) {
					if (IRobject.getXFI() == 1) {
						RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
						MAR.setData(RES.getData());
					} else if (IRobject.getXFI() == 2) {
						RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
						MAR.setData(RES.getData());
					} else if (IRobject.getXFI() == 3) {
						RES.setData(IRobject.getAddress() + XFtable.getSwitch(IRobject.getXFI()));
						MAR.setData(RES.getData());
					}
				}				
			}
			//if indirect bit is enabled get the memory content 
			//of MAR address and put it in MDR then copy MDR into MAR
			if (IRobject.getIndirect() == 1) {			
				try {
					MDR.setData(Mem.getDataFromMem(MAR.getData()));
					MAR.setData(MDR.getData());
				} catch (MemoryException e){
					logger.error("failed to get data from memory: " + e.getMessage(), e);
				}			
			}		
			PC.setData(MAR.getData());
			logger.debug("PC JGE="+PC.getData());
		}
		//////////////////////////
		else
			PC.setData(PC.getData()+1);
	}
		
	
}
