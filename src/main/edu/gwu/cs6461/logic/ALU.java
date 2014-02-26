/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gwu.cs6461.logic;

import java.util.Observable;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.sim.bridge.HardwareData;
import edu.gwu.cs6461.sim.common.ALUFlags;
import edu.gwu.cs6461.sim.common.ALUOperator;
import edu.gwu.cs6461.sim.common.ConditionCode;
import edu.gwu.cs6461.sim.common.HardwarePart;
import edu.gwu.cs6461.sim.common.OpCode;
import edu.gwu.cs6461.sim.common.SimConstants;
import edu.gwu.cs6461.sim.util.Convertor;

/**
 * ALU to support arithmetic logics
 * Two functionalities available subtract and add
 */
public class ALU extends Observable {
	private final static Logger logger = Logger.getLogger(ALU.class);
	
	//final result sign bit
	private int RSignBit=0;
	
	private ALUFlags currFlag = ALUFlags.NotReady; 
	private static ALU INSTANCE = new ALU();
	private Register result = new Register(HardwarePart.RES.getBit(),HardwarePart.RES.getName());
	
	
	//type of operation + , -
	private ALUOperator operation = null;
	private Integer operand1 = null;
	private Integer operand2 = null;
	private int bitSize = 0;
	protected ALU(){
		reset();
	}
	public static ALU getInstance(){
		return INSTANCE;
	}
	
	public void reset( ) {
		operation = null;
		operand1 = null;
		operand2 = null;
		bitSize =0;
		currFlag = ALUFlags.Normal;
		result = new Register(HardwarePart.RES.getBit(),HardwarePart.RES.getName());
	}
	public void setOperation(ALUOperator operation) {
		this.operation = operation;
	}
	public void setOperand1(int operand1) {
		this.operand1 = operand1;
	}
	public void setOperand2(int operand2) {
		this.operand2 = operand2;
	}
	
	public void setBitSize(int bitSize) {
		this.bitSize = bitSize;
	}
	public ALU setALU(int operand1,int operand2, ALUOperator operation, int bitSize){
		setOperand1(operand1);
		setOperand2(operand2);
		setOperation(operation);
		setBitSize(bitSize);
		return this;
	}
	
	/**
	 * need to be enhanced to support more complex result
	 * 
	 * @return
	 */
	public int getResult (){
		return result.getData();
	}
	
	public ALUFlags getFlag(){
		return currFlag;
	}
	
	
	/**
	 * perform arithmetical operation
	 * the ALU status will be set to currFlag
	 * the result will be set at the result register.
	 * 
	 */
	public void calculate() {
		if (operand1 == null ||operand2 == null || 
				operation == null || bitSize<=0) {
			currFlag = ALUFlags.NotReady;
		}
		
		String op1 = "", op2 = "";
		op1= Convertor.getBinFromInt(operand1, bitSize);
		op2= Convertor.getBinFromInt(operand2, bitSize);
		
		int opt1 = Convertor.getSignedValFromBin(op1, bitSize);
		int opt2 = Convertor.getSignedValFromBin(op2, bitSize);
		
		logger.debug("opt1:"+ op1 +"="+opt1+",opt2:"+ op2+"="+opt2);
		
		int ret=0; 
		if (operation == ALUOperator.Addition) {
			ret = opt1+opt2;
		} else if (operation == ALUOperator.Subtraction) {
			ret = opt1-opt2;
		}
		if (ret > SimConstants.WORD_MAX_VALUE || ret < SimConstants.WORD_MIN_VALUE) {
			currFlag = ALUFlags.Overflow;
		} else {
			currFlag = ALUFlags.Normal;
		}
		result.setData(ret);
	}
	
	
	@Deprecated
	public void Calculate(int operand1, int operand2, int opcode, Register RES, Register CC, Register RES2, Multiply Multi) {
		Calculate(operand1, operand2, SimConstants.WORD_SIZE, opcode, RES, CC, RES2, Multi);
	}
	@Deprecated
	public void Calculate(int operand1, int operand2, int bitSize, int opcode, Register RES, Register CC, Register RES2, Multiply Multi) {
		String op1="", op2="";
		op1= Convertor.getBinFromInt(operand1, bitSize);;
		op2= Convertor.getBinFromInt(operand2, bitSize);;
		
		int opt1 = Convertor.getSignedValFromBin(op1, bitSize);
		int opt2 = Convertor.getSignedValFromBin(op2, bitSize);
		
		logger.debug("opt1:"+ op1 +"="+opt1+",opt2:"+ op2+"="+opt2);
		
		//check the opcode to decide what arithmetic operation to be done
		if (OpCode.fromCode(opcode) == OpCode.AMR || OpCode.fromCode(opcode) == OpCode.AIR)
			operation = ALUOperator.Addition;
		else if (OpCode.fromCode(opcode) == OpCode.SMR || OpCode.fromCode(opcode) == OpCode.SIR)
			operation = ALUOperator.Subtraction;
		else if (OpCode.fromCode(opcode) == OpCode.MLT)
			operation = ALUOperator.Multiply;
		else if (OpCode.fromCode(opcode) == OpCode.DVD)
			operation = ALUOperator.Division;
				
		int ret=0; 
		if ("+".equals(operation.getOpt())) {
			ret = opt1+opt2;
		} else if ("-".equals(operation.getOpt())) {
			ret = opt1-opt2;
		}

		if (ret > SimConstants.WORD_MAX_VALUE || ret < SimConstants.WORD_MIN_VALUE) {
			setCC(ConditionCode.OVERFLOW.getCode(),CC);			
		}else if (ret > SimConstants.WORD_MAX_VALUE || ret < SimConstants.WORD_MIN_VALUE) {
			setCC(ConditionCode.UNDERFLOW.getCode(),CC);			
		} 		
		else {
			setCC(ConditionCode.NORMAL.getCode(),CC);
		}
		//set RES for subtract, add, multiply
		RES.setData(ret);
		
		if("/".equals(operation.getOpt())){
			if(opt2!=0){
				RES.setData(opt1/opt2);
				RES2.setData(opt1%opt2);
			}
			else
				setCC(ConditionCode.DIVZERO.getCode(),CC);
		}
		if ("*".equals(operation.getOpt())){
			multiply(opt1, opt2, CC,Multi);
		}
		logger.debug("opt1:"+ opt1+",opt2:"+ opt2 + ",ret:"+ret);	
	}
	public void multiply(int opt1, int opt2, Register CC, Multiply Multi){
		long resultMLT=0;		
		
		resultMLT=1L*(long)opt1*(long)opt2;
		logger.debug("Res MLT="+resultMLT);		
		
		if (resultMLT > SimConstants.TWOWORD_MAX_VALUE ) {
			setCC(ConditionCode.OVERFLOW.getCode(),CC);			
		} else if( resultMLT < SimConstants.TWOWORD_MIN_VALUE){
			setCC(ConditionCode.UNDERFLOW.getCode(),CC);
		}
		else {
			setCC(ConditionCode.NORMAL.getCode(),CC);
		}
		
		Multi.setResult(resultMLT);
	}
	public void setCC(int Value, Register CC) {
		CC.setData(Value);

		HardwareData hardwareData = new HardwareData();
		hardwareData.put("CC", Integer.toString(Value));

		this.notifyObservers(hardwareData);
	}
}
