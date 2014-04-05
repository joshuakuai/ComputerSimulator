package edu.gwu.cs6461.test;

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
	
	public static void main(String[] args) {
		System.out.println(Convertor.getSignedBinFromInt(6, 20));
		System.out.println(Convertor.getSignedValFromBin("00000000000011001000",20));
		new TEST2().testEnum();
	}

	@Override
	public void close() throws Exception {
		
	}

}
