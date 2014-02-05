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
 * 
 * @author Ahmed
 */
public class ALU extends Observable {

	private String Operation = "";
	private int Result = 0;
	private int RSignBit=0;
	public void Calculate(int Operand1, int Operand2, int Opcode, Register RES, Register CC) {
		
		String Op1="", Op2="";
		String Res="";
		int Op1SignBit=0, Op2SignBit=0;
		Op1=Integer.toBinaryString(Operand1);
		Op2=Integer.toBinaryString(Operand2);
		String underFlowcheck="0";
		int unSigned=0;
		if(Op1.length()==20){
			Op1SignBit=Integer.parseInt(Op1.substring(0,1));
			Operand1 = Integer.parseInt(Op1.substring(1),2);
		}
		if(Op2.length()==20){
			Op2SignBit= Integer.parseInt(Op2.substring(0,1));
			Operand2 = Integer.parseInt(Op2.substring(1),2);
		}
		System.out.println("OP1="+Op1);

		if (Opcode == 4 || Opcode == 6)
			Operation = "+";
		else if (Opcode == 5 || Opcode == 7){
			Operation = "-";
		}

		if (Operation.equals("+")) {
			Result = Operand1 + Operand2;
		//including sign bit in calculation		
			if(Op1SignBit==0 && Op2SignBit==0){				
				RSignBit=0;
				Result = Operand1 + Operand2;
				Result=setResultSignBit(Result);
			}
			else if(Op1SignBit==0 && Op2SignBit==1 && Operand1 > Operand2){				
				RSignBit=0;
				Result = Operand1 - Operand2;
				Result=setResultSignBit(Result);
			}
			else if(Op1SignBit==0 && Op2SignBit==1 && Operand1 < Operand2){				
				RSignBit=1;
				Result = Operand2 - Operand1;
				Result=setResultSignBit(Result);
			}
			else if(Op1SignBit==1 && Op2SignBit==0 && Operand1 > Operand2){				
				RSignBit=1;
				Result = Operand1 - Operand2;
				Result=setResultSignBit(Result);
			}
			else if(Op1SignBit==1 && Op2SignBit==0 && Operand1<Operand2){				
				RSignBit=0;
				Result = Operand2 - Operand1;
				Result=setResultSignBit(Result);
			}
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
		else if (Operation.equals("-")) {
		//including sign bit in calculation
			if(Op1SignBit==0 && Op2SignBit==0 && Operand1 > Operand2){
				RSignBit=0;
				Result = Operand1 - Operand2;
				Result=setResultSignBit(Result);
			}
			else if(Op1SignBit==0 && Op2SignBit==0 && Operand1 < Operand2){
				RSignBit=1;
				Result = Operand2-Operand1;
				Result=setResultSignBit(Result);
			}
			else if(Op1SignBit==0 && Op2SignBit==1){
				RSignBit=0;
				Result = Operand1 + Operand2;
				Result=setResultSignBit(Result);
			}
			else if(Op1SignBit==1 && Op2SignBit==0){
				RSignBit=1;
				Result = Operand1 + Operand2;
				Result=setResultSignBit(Result);
			}
			else if(Op1SignBit==1 && Op2SignBit==1 && Operand1 > Operand2){
				RSignBit=1;
				Result = Operand1 - Operand2;
				Result=setResultSignBit(Result);
			}
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
	public void setCC(int Value, Register CC) {
		CC.setData(Value);

		HardwareData hardwareData = new HardwareData();
		hardwareData.put("CC",
				 Integer.toString(Value));

		this.notifyObservers(hardwareData);
	}
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
