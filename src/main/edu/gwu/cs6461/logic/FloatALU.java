package edu.gwu.cs6461.logic;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.sim.bridge.Observable;
import edu.gwu.cs6461.sim.util.FloatPoint;


/***
 * 
 * Class for floating point data manipulation
 * 
 * @author marcoyeung
 *
 */
public class FloatALU extends Observable{
	public FloatALU(){
		
	}
	/**logger to log message to file*/
	private final static Logger logger = Logger.getLogger(Register.class);
		
	/**
	 * Convert binary data to decimal floating point number 
	 * */
	public float BinaryToFloat(int Data){
		String bin = Integer.toBinaryString(Data); 
		FloatPoint fp= new FloatPoint(0.0f);
		float value = 0.0f;
		if(bin.length()>20)
			bin= bin.substring(bin.length()-20, bin.length());		
		else{
			int len = bin.length();
			while(len<20){
				bin = "0"+bin;
				len++;
			}
		}		
		try {
			fp = new FloatPoint(bin.substring(0,1),bin.substring(1,8),bin.substring(8,20));		
		} catch (Exception e) {			
			e.printStackTrace();
		}
		value = fp.floatValue();
		
		return value;
	}
	public int FloatToBinary(float Data){
		FloatPoint fp = new FloatPoint(Data);
		
		String binary = fp.signBit() + fp.exponentBits()+fp.mantissaBits();
		int bin = Integer.valueOf(binary, 2);
		System.out.println("sign bit="+fp.signBit());
		System.out.println("exponent="+fp.exponentBits());
		System.out.println("mantissa="+fp.mantissaBits());
		return bin;
	}
	public int ConvertToFixedNumber(int DATA){
		float temp= BinaryToFloat(DATA);
		int fixedNum =(int)temp;
		return fixedNum;
	}
	
	public float ADD(float op1, float op2){
		float result = op1+op2;
		return result;
	}
	public float SUB(float op1, float op2){
		float result = op1-op2;
		return result;
	}

}
