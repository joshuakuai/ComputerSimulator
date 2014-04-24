package edu.gwu.cs6461.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import edu.gwu.cs6461.sim.common.MachineFault;
import edu.gwu.cs6461.sim.util.Convertor;

public class TEST2 implements AutoCloseable {

	
	
	private void testArrayList() {
		List<Integer>list = new ArrayList<>();
		
		
	}
	
	private void testChar() {
		int val =13;
		
		if ((char)val== '\n') {
			System.out.println("yes");
		} else 
			System.out.println("'"+(char)val +"'");
		
		
		String name = "32094";
		int len = name.length();
		for (int i = 0; i < len; i++) {
			System.out.print ((int)name.charAt(i) + " ");
		}
		
	}
	
	/**
	 * Apress - Pro Java Programming, Second Edition (2005).pdf
	 */
	private void testMessage() {
		String msg = MessageFormat.format("this is for {2}  !!", "eh","22","33");
				System.out.println(msg);

	}
	
	private void testEnum() {
		System.out.println(MachineFault.valueOf(10));

	}
	
	private void testIO() throws Exception {
		BufferedReader in = new BufferedReader(new FileReader("D:/java_stuff/githome/ComputerSimulator/src/resources/sample.txt"));
		String text = "";
		String input="";
		
		while ( (text=in.readLine()) != null)  {
			if(text.charAt(0)!='#'){
				System.out.println(text);
			}
		}
		

	}
	
	private void testDivide() {
		int result = 3/2;
		System.out.println(result);
	}
	
	private void testShift() {
		int a = 80;
		System.out.println( a>>1);

	}

	private void testFloat() {
		System.out.println(Convertor.getSignedValFromBin("00001000001000101000", 20));
		System.out.println(Convertor.getSignedBinFromInt(-1, 20));
		System.out.println(Convertor.getSignedBinFromInt(63, 7));

//		System.out.println(Integer.toBinaryString(Float.floatToIntBits(11.12346f)));

	}
	/**
	 * http://stackoverflow.com/questions/756430/how-do-i-convert-a-decimal-fraction-to-binary-in-java
	 * @param number
	 * @return
	 */
	private static String convert(double number) {
	    int n = 10;  // constant?
	    BigDecimal bd = new BigDecimal(number);
	    BigDecimal mult = new BigDecimal(2).pow(n);
	    bd = bd.multiply(mult);
	    BigInteger bi = bd.toBigInteger();
	    StringBuilder str = new StringBuilder(bi.toString(2));
	    while (str.length() < n+1) {  // +1 for leading zero
	        str.insert(0, "0");
	    }
	    str.insert(str.length()-n, ".");
	    System.out.println(str);
	    return str.toString();
	}
	public static void main(String[] args) throws Exception {
		
//		new TEST2().testFloat();
		new TEST2().convert(21.472);
	}

	@Override
	public void close() throws Exception {
		
	}

}
