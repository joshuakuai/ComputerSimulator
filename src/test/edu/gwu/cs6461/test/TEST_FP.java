package edu.gwu.cs6461.test;

import edu.gwu.cs6461.sim.util.FloatPoint;

public class TEST_FP {

	public TEST_FP() {
		
	}
	void testConversion() {
//		System.out.println(Convertor.padZero(dinb, 12));
//		System.out.println(Convertor.padZeroAfter(dinb, 12));
//		System.out.println(Math.pow(2, -5));
		
		float ff =39887.5625f;//2.625f;//-0.1015625f;//78979f;//-4.75f;//10.6875f; //3.15f;//0.40625f;  //   
//		ff=Float.parseFloat("1.84444E+19");
//		ff= Float.parseFloat("9.2222461E18");
//		ff=0f;
		FloatPoint fp = new FloatPoint(ff);
		System.out.println("-------------------------------");
		System.out.println(ff);
		System.out.println(fp.exponentForm());
		System.out.println("sign bit="+fp.signBit());
		System.out.println("exponent="+fp.exponentBits());
		System.out.println("mantissa="+fp.mantissaBits());
		System.out.println(fp.unbiasedExponent());
		System.out.println(fp.biasedExponent());
		System.out.println(fp.floatValue());
		System.out.println("20bit float binary="+fp.signBit()+fp.exponentBits()+fp.mantissaBits());
		System.out.println("-------------------------------");		
	
		try {

			
			fp = new FloatPoint(fp.signBit(),fp.exponentBits(),fp.mantissaBits());
//			fp = new FloatPoint("0","1111111","111111111111");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		float value = fp.floatValue();
		System.out.println(value);

	}

	void testInfinity(){ 
		float test =Float.parseFloat("-3.40282E38");
		System.out.println("tset: "+ test);
		
		
		float test2 =Float.NEGATIVE_INFINITY;
		System.out.println("tset: "+ test2);
		
		int low = Integer.MIN_VALUE;
		int underflow = low - 1;
		System.out.println("und:  " + underflow + " vs " + Integer.MIN_VALUE);
		
	}
	
	public static void main(String[] args) {
		new TEST_FP().testConversion();;
	}

}
