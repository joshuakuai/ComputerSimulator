
package edu.gwu.cs6461.test;

import java.math.BigInteger;
import java.util.StringTokenizer;

import edu.gwu.cs6461.sim.common.HardwarePart;
import edu.gwu.cs6461.sim.common.OpCode;
import edu.gwu.cs6461.sim.util.Convertor;

public class TEST {

	/**
	 * 
	 * 
	 * http://stackoverflow.com/questions/15837899/java-twos-complement-binary-to-integer
	 * 
	 * 
	 * short res = (short)Integer.parseInt("1111111111001110", 2);
System.out.println(res);
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new TEST().testConvert();
	}
	
	
	void testConvert(){
		int val = 101;
		
		String bin = Convertor.getSignedBinFromInt(val, 8);
		
		System.out.println(bin);
		
		
	}
	
	void testSplit(){
		String bin = "1|1|0|0|0|0|0|0|000000001000";
		bin = "11000000000000001000";
		
		String bits[] = new String[bin.length()];
		for (int i = 0; i < bin.length(); i++) {
			bits[i]= bin.substring(i, i+1);
		}

		for (int i = 0; i < bits.length; i++) {
			System.out.println(bits[i]);
		}
		System.out.println(bits.length);
		
	}
	
	
	void testBit(){
		
		int number = 54;
		
//		char[] bins = Integer.toBinaryString(number).toCharArray();
//		for (int i = 0; i < bins.length; i++) {
//			System.out.print(bins[i] + ":");
//		}
		
		String bin = "11000000000000001000";
		
		System.out.println(Convertor.getSignedValFromBin(bin,20));
		
		
		bin = "01110000000000000000";
		int n = 20;
		System.out.println(Convertor.getSignedBinFromInt(6, n));
		
		
		Long res = Long.parseLong(bin, 2);
		System.out.println(": " + res);
		if (bin.charAt(0) == '1' && bin.length() == n)
			res -= (1L << n);
		
		System.out.println(res);
	}
	void testOverFlow (){
		
		
		String binVal = "11111000000000000000";
		int numOfBit = 20;
		System.out.println(binVal + ":"+Convertor.getSignedValFromBin(binVal, numOfBit));
		
		int iVal = -6;
		String binV = Convertor.getSignedBinFromInt(iVal, numOfBit);
		System.out.println(iVal + " in binary form " + binV);;
		
		int immed = -524288;
		immed -= 1; // overflow
		
		BigInteger rangeVal = BigInteger.valueOf(2L).pow(numOfBit- 1);
		if (BigInteger.valueOf(immed).compareTo(rangeVal)>=0 ||
				BigInteger.valueOf(immed).compareTo(rangeVal.negate()) < 0	) {
			System.out.println("set to overflow: " + immed);
		} else {
			System.out.println("still in range: " + immed);
		}
		
	}
	
	
	
	
	void testEnum(){
		
		HardwarePart register = HardwarePart.IR;
		System.out.println(register);
		
		
		
		
		System.out.println(OpCode.LDR);
	}
	
	void maksing() {
		String instr= "00000111011001000000";
		
		
		
		
	}
	
	void testHex(){
		int val = 0x100000;
		System.out.println(val);
		System.out.println(Integer.toBinaryString(val));
	}
	

}
