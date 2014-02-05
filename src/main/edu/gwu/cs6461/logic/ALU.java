/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gwu.cs6461.logic;

import java.util.Observable;

import edu.gwu.cs6461.sim.bridge.HardwareData;

/**
 * ALU to support arithmetic logics
 * Two functionalities available subtract and add
 */
public class ALU extends Observable {
	//type of operation + , -
	private String Operation = "";
	private int Result = 0;
	//final result sign bit
	private int RSignBit=0;
	public void Calculate(int Operand1, int Operand2, int Opcode, Register RES, Register CC) {
		
		String Op1="", Op2="";
		String Res="";
		//sign bits of operand1 and operand2
		int Op1SignBit=0, Op2SignBit=0;
		Op1=Integer.toBinaryString(Operand1);
		Op2=Integer.toBinaryString(Operand2);
		String underFlowcheck="0";
		int unSigned=0;
		//check if the size of the operands is 20bits, if it is that means its sign
		//bit is set other wise it is positive and leave it as it is
		if(Op1.length()==20){
			Op1SignBit=Integer.parseInt(Op1.substring(0,1));
			Operand1 = Integer.parseInt(Op1.substring(1),2);
		}
		if(Op2.length()==20){
			Op2SignBit= Integer.parseInt(Op2.substring(0,1));
			Operand2 = Integer.parseInt(Op2.substring(1),2);
		}

		//check the opcode to decide what arithmetic operation to be done
		if (Opcode == 4 || Opcode == 6)
			Operation = "+";
		else if (Opcode == 5 || Opcode == 7){
			Operation = "-";
		}
		//for instructions that require addition
		if (Operation.equals("+")) {
			Result = Operand1 + Operand2;
		//including sign bit in calculation	
			//if both operands are positive add them
			//final sign bit is postive
			if(Op1SignBit==0 && Op2SignBit==0){				
				RSignBit=0;
				Result = Operand1 + Operand2;
				Result=setResultSignBit(Result);
			}
			//if first operand1 is positive and second negative while first is bigger than second
			//than subtract first from second and final sign bit is positive
			else if(Op1SignBit==0 && Op2SignBit==1 && Operand1 > Operand2){				
				RSignBit=0;
				Result = Operand1 - Operand2;
				Result=setResultSignBit(Result);
			}
			//if first operand1 is positive and second negative while first is smaller than second
			//than subtract second from first and final sign bit is negative
			else if(Op1SignBit==0 && Op2SignBit==1 && Operand1 < Operand2){				
				RSignBit=1;
				Result = Operand2 - Operand1;
				Result=setResultSignBit(Result);
			}
			//if first operand1 is negative and second positive while first is bigger than second
			//than subtract first from second and final sign bit is negative
			else if(Op1SignBit==1 && Op2SignBit==0 && Operand1 > Operand2){				
				RSignBit=1;
				Result = Operand1 - Operand2;
				Result=setResultSignBit(Result);
			}
			//if first operand1 is negative and second positive while first is smaller than second
			//than subtract second from first and final sign bit is positive
			else if(Op1SignBit==1 && Op2SignBit==0 && Operand1<Operand2){				
				RSignBit=0;
				Result = Operand2 - Operand1;
				Result=setResultSignBit(Result);
			}
			//if first operand1 is negative and second negative
			//than add first to second and final sign bit is negative
			else if(Op1SignBit==1 && Op2SignBit==1){				
				RSignBit=1;
				Result = Operand1 + Operand2;
				Result=setResultSignBit(Result);
			}		
		//Check for underflow
			if(RSignBit==1&& unSigned<-524287){
				System.out.println("underFlow="+unSigned);
				setCC(2, CC);
			}
		//check for overflow
			else if(unSigned>524287)
				setCC(1,CC);
		//else proceed with program calculation is fine
			else
				setCC(0,CC);
		} 
		//for instructions that require subtraction
		else if (Operation.equals("-")) {
		//including sign bit in calculation
			//if first operand1 is positive and second positive while first is bigger than second
			//than subtract first from second and final sign bit is positive
			if(Op1SignBit==0 && Op2SignBit==0 && Operand1 > Operand2){
				RSignBit=0;
				Result = Operand1 - Operand2;
				Result=setResultSignBit(Result);
			}
			//if first operand1 is positive and second positive while first is smaller than second
			//than subtract second from first and final sign bit is negative
			else if(Op1SignBit==0 && Op2SignBit==0 && Operand1 < Operand2){
				RSignBit=1;
				Result = Operand2-Operand1;
				Result=setResultSignBit(Result);
			}
			//if first operand1 is positive and second negative
			//than add first to second and final sign bit is positive
			else if(Op1SignBit==0 && Op2SignBit==1){
				RSignBit=0;
				Result = Operand1 + Operand2;
				Result=setResultSignBit(Result);
			}
			//if first operand1 is negative and second positive
			//than add first to second and final sign bit is negative
			else if(Op1SignBit==1 && Op2SignBit==0){
				RSignBit=1;
				Result = Operand1 + Operand2;
				Result=setResultSignBit(Result);
			}
			//if first operand1 is negative and second negative while first is bigger than second
			//than subtract first from second and final sign bit is negative
			else if(Op1SignBit==1 && Op2SignBit==1 && Operand1 > Operand2){
				RSignBit=1;
				Result = Operand1 - Operand2;
				Result=setResultSignBit(Result);
			}
			//if first operand1 is negative and second negative while first is smaller than second
			//than subtract second from first and final sign bit is positive
			else if(Op1SignBit==1 && Op2SignBit==1 && Operand1<Operand2){
				RSignBit=0;
				Result = Operand2 - Operand1;
				Result=setResultSignBit(Result);
			}
			underFlowcheck=Integer.toBinaryString(Result);
			unSigned=Integer.parseInt(underFlowcheck.substring(1),2);
		//Check for underflow
			if(RSignBit==1&& unSigned<-524287){
				System.out.println("underFlow="+unSigned);
				setCC(2, CC);
			}
		//check for overflow
			else if(unSigned>524287)
				setCC(1,CC);
		//else proceed with program calculation is fine
			else
				setCC(0,CC);
		}
		/*TODO add multiple and divide
		else if (Operation.equals("*")) {
			Result = Operand1 * Operand2;
		} else if (Operation.equals("/")) {
			Result = Operand1 / Operand2;
		}
		 */
		RES.setData(Result);
	}
	//this updates the GUI value of the CC register
	public void setCC(int Value, Register CC) {
		CC.setData(Value);

		HardwareData hardwareData = new HardwareData();
		hardwareData.put("CC",
				 Integer.toString(Value));

		this.notifyObservers(hardwareData);
	}
	//in order to display the correct value in the GUI
	//the operands have to be padded with zeros if they are less than 20bits
	private int setResultSignBit(int Result){
		String res=Integer.toBinaryString(Result);
		String res20bit="";
		int length=res.length();
		int templength=0;
		if(length<20){
			templength=20-length;
			if(RSignBit==0){
				for(int i=0;i<templength;i++){
					res20bit+="0";
				}
				res20bit+=res;
			}
			else if(RSignBit==1){
				res20bit+="1";
				for(int i=0; i<templength-1;i++){
					res20bit+="0";
				}
				res20bit+=res;
			}
		}
		else
			return Integer.parseInt(res,2);
		System.out.println("res20bit="+res20bit);
		return Integer.parseInt(res20bit,2);
		
	}
}
