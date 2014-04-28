package edu.gwu.cs6461.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import sun.io.Converters;
import edu.gwu.cs6461.sim.common.MachineFault;
import edu.gwu.cs6461.sim.util.Convertor;
import edu.gwu.cs6461.sim.util.FloatPoint;

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
	private void testFloatingUtil() {
		FloatPoint ee = new FloatPoint(0.315f);
		
		System.out.println("-------------------------------");
		System.out.println(ee.exponentBits());
		System.out.println(ee.mantissaBits());
		System.out.println(ee.biasedExponent());
		System.out.println(ee.floatValue());
		System.out.println(ee.signBit());
		System.out.println(ee.unbiasedExponent());
	}
	

	
	/**
	 * http://stackoverflow.com/questions/21797437/binary-to-decimal-java-converter
	 *  has bug
	 *  
	 *  @deprecated
	 * @param s
	 * @return
	 */
	public static String decimalToBinary(int n){
		StringBuilder sb = new StringBuilder();

		if (n==0) return "0";
		int d = 0;
		while (n > 0){
			d = n % 2;
			n /= 2;
			sb.append(d);
		}
		sb = sb.reverse();
		return sb.toString();
	}
	
	
	
	
	
	void doTestPart4() throws Exception {
	}

	
	public static void main(String[] args) throws Exception {
		new TEST2().doTestPart4();
	}	
	
	
	
	
	
	

	@Deprecated
	float bintoFloat(String str) {

		double output = 0f;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '1')
				output = output + (float)Math.pow(2, str.length() - 1 - i);

		}
		return (float)output;
	}
	
	/**
	 * http://www.java2novice.com/java-interview-programs/binary-to-decimal/
	 * @param binary
	 * @return
	 */
	private int getDecimalFromBinary(int binary){

		int decimal = 0;
		int power = 0;
		while(true){
			if(binary == 0){
				break;
			} else {
				int tmp = binary%10;
				decimal += tmp*Math.pow(2, power);
				binary = binary/10;
				power++;
			}
		}
		return decimal;
	}
	
	/**
	 * http://www.java-forums.org/advanced-java/11924-float-double-binary.html
	 * http://stackoverflow.com/questions/9320522/how-to-convert-32-bit-mantissa-ieee754-to-decimal-number-using-java
	 * Prentice Hall - Java Number Cruncher - The Java Programmer's Guide to Numerical Computing - 2002.chm
	 */
	void convertFloat2() {
		
		System.out.println(Convertor.getSignedValFromBin("10000101", 1));
		
        float f = -121.6875f;//-118.625f;
        f= 0.375f;  //0.317f from 64 bit intel assembly   0.375f from chm book
        f=41.275f;
        f=-4.75f;
        int intBits = Float.floatToIntBits(f);
        int rawIntBits = Float.floatToRawIntBits(f);
        System.out.printf("f = %f  intBits = %d  " +
                          "rawIntBits = %d%n", f, intBits, rawIntBits);
        
        float toFloat = Float.intBitsToFloat(intBits);
        System.out.printf("toFloat = %f%n", toFloat);
        
        /********************************************************************/
        String binaryForm = Convertor.getSignedBinFromInt(intBits, 32);
        System.out.println(binaryForm);
        
        char bits[] = Convertor.toCharBitArray(Float.floatToIntBits(f), 32);
        System.out.println(new String(bits));
        
        /********************************************************************/
        String theSign = binaryForm.substring(0,1);
        int theSignVal = theSign.equals("0") ? 1:-1;
        String theExponent = binaryForm.substring(1,9);
        int theExponentVal = Convertor.getSignedValFromBin(theExponent, 9);
        String theMantissa = binaryForm.substring(9,32);
        int theMantissaVal = Convertor.getSignedValFromBin(theMantissa, 24);
        System.out.printf("binarySign     = %s%nbinaryExponent = %s%n" +
                "binaryMantissa = %s%n", theSign + ", " + theSignVal,
                 theExponent + ", "+theExponentVal, theMantissa + ", "+theMantissaVal);
        /********************************************************************/
        
        int sign     = intBits & 0x80000000;
        int exponent = intBits & 0x7f800000;
        int exp = exponent>>23;
        int mantissa = intBits & 0x007fffff;
        int man = mantissa |= 0x00800000;
        
        System.out.println( man * Math.pow(2, exp-127) );
        System.out.printf("sign = %d  exponent = %d  mantissa = %d%n", sign, exponent, mantissa);
        
        String binarySign = Integer.toBinaryString(sign);
        String binaryExponent = Integer.toBinaryString(exponent);
        String binaryMantissa = Integer.toBinaryString(mantissa);
        System.out.printf("binarySign     = %s%nbinaryExponent = %s%n" +
                          "binaryMantissa = %s%n", binarySign,
                           binaryExponent, binaryMantissa);
	}
	
	void convertFloat(float val) {
		
		System.out.println(Convertor.getBinFromInt(118, 8));
		System.out.println(Convertor.getSignedValFromBin("11011010100000000000000", 23));
		
//		System.out.println(Float.floatToIntBits(val));
//		System.out.println(Integer.toBinaryString(Float.floatToIntBits(val)));
	}
	
	
	@Override
	public void close() throws Exception {
		
	}

}
