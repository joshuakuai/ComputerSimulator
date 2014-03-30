package edu.gwu.cs6461.test;

import java.util.ArrayList;
import java.util.List;

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
	
	
	public static void main(String[] args) {
		System.out.println(Convertor.getSignedValFromBin("11110100000000000000", 20));
			new TEST2().testChar();
	}

	@Override
	public void close() throws Exception {
		
	}

}
