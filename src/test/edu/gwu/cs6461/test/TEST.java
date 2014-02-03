
package edu.gwu.cs6461.test;

import java.math.BigInteger;

import edu.gwu.cs6461.sim.common.OpCode;
import edu.gwu.cs6461.sim.common.RegisterName;

public class TEST {

	
	void testBit(){
		
		int number = 54;
		
		char[] bins = Integer.toBinaryString(number).toCharArray();
		for (int i = 0; i < bins.length; i++) {
			System.out.print(bins[i] + ":");
		}
		
		String bin = "1000000001101110";
		int n = 16;
		
		Integer.parseInt(bin,2);
		
		Integer iRes = Integer.parseInt(bin, 2);
		System.out.println("IRes: " + iRes);
		if (bin.charAt(0) == '1' && bin.length() == n)
			iRes -= (1 << n);
		System.out.println(iRes);
		
		Long res = Long.parseLong(bin, 2);
		System.out.println(": "+res);
		if (bin.charAt(0) == '1' && bin.length() == n)
			res -= (1L << n);
		
		
		System.out.println(res);
	}
	
	void testEnum(){
		
		RegisterName register = RegisterName.IR;
		System.out.println(register);
		
		
		
		
		System.out.println(OpCode.LDR);
	}
	
	void maksing() {
		String instr= "00000111011001000000";
		
		
		
		
	}
	void testOverFlow (){
		
		System.out.println(BigInteger.valueOf(2L).pow(4 - 1));
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TEST().testOverFlow();
	}

}
