package edu.gwu.cs6461.test;

import edu.gwu.cs6461.sim.util.FloatPoint;

public class TEST_FP {

	public TEST_FP() {
//		System.out.println(Convertor.padZero(dinb, 12));
//		System.out.println(Convertor.padZeroAfter(dinb, 12));
//		System.out.println(Math.pow(2, -5));
		
		float ff = -2.02f;//0.1015625f;//2.625f;// -4.75f;//10.6875f; // 3.15f;//0.40625f;//39887.5625f;//
		
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
		
		System.out.println("-------------------------------");		
		
		
		try {
			
			fp = new FloatPoint(fp.signBit(),fp.exponentBits(),fp.mantissaBits());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
		float value = fp.floatValue();
		System.out.println(value);

	}

	public static void main(String[] args) {
		new TEST_FP();
	}

}
