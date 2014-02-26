
package edu.gwu.cs6461.test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Set;

import edu.gwu.cs6461.sim.common.HardwarePart;
import edu.gwu.cs6461.sim.common.OpCode;
import edu.gwu.cs6461.sim.common.SimConstants;
import edu.gwu.cs6461.sim.util.Convertor;

public class TEST {

	
	void testOverBit(){
		int val = -8;
		String sVal = Integer.toBinaryString(val);
		sVal = Convertor.getBinFromInt(val, 20);
		
		System.out.println(Convertor.getSignedValFromBin(sVal, 20));
		
	}
	
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
		TEST tester = new TEST();
		
		
		System.out.println(Convertor.getSignedValFromBin("00000001001101110100", 20));
		
//		tester.testMap();
	}

	void testMap(){
		LinkedHashMap<Integer, String>link = new LinkedHashMap<>();
		
		link.put(10,"1");
		link.put(1,"1");
		link.put(5,"1");
		link.put(3,"1");
		System.out.println(link);
		
		String val = link.remove(1);
		link.put(1, val);

		System.out.println(link);
		Set<Integer>keys = link.keySet();
		System.out.println(keys);
		
		Integer[] array = keys.toArray(new Integer[0]);
		
		link.remove(array[0]);
		System.out.println(link +":"+link.size()) ;
		
	}
	
	void testmask(){
		
		int net = 4385;
		
		int iMask = net & 0b11111111111111111111; 
		System.out.println(iMask);
		System.out.println(Convertor.display(iMask));		
		System.out.println(Integer.toBinaryString(iMask));
		
	}
	
	
	
	void instrTest() {
		System.out.println("---------Multiply----");
		test2complementMlt_2();
//		System.out.println("---------Division----");
//		tester.testDVD();
//		System.out.println("---------subtraction----");
//		tester.test2complementSub();
//		System.out.println("---------Add-----------");
//		tester.test2complementAdd();
//		System.out.println("---------bit AND-----------");
//		tester.testANDOrOR("AND");
//		System.out.println("---------bit or -----------");
//		tester.testANDOrOR("OR");
//		System.out.println("--------Arithmetic Shift-----------");
//		tester.testShiftArith("left");
//		System.out.println("-----------");
//		tester.testShiftArith("right");
//		System.out.println("-------Rotate Left--------");
//		tester.testRRCLeft();
//		System.out.println("-------Rotate Right-------");
//		tester.testRRCRight();
//		System.out.println("-------Shift logical left-------");
//		tester.testLeftShiftLogical();
//		System.out.println("-------Shift logical right-------");
//		tester.testRightShiftLogical();
//		System.out.println("-------NOT-------");
//		tester.testNOT();
//		
	}
	
	void testTRR(){
		
	}
	
	void testDVD(){
		
		int opt1 =100;
		int opt2 = -32;

		if (opt2 == 0) {
			System.out.println("DIVZERO! ");	
		}
		
		int quotient = opt1 / opt2;
		int remainder = opt1 % opt2;

		System.out.println(opt1 +"/"+opt2 + " quotient:"+ quotient +" Remainder: "+ remainder);
		System.out.println("quotient:"+ Convertor.getBinFromInt(quotient, 20) 
				+" Remainder: "+ Convertor.getBinFromInt(remainder, 20));
	}
	
	void get2sRange(int width){
		
		BigInteger range = (BigInteger.valueOf(2l).pow(width-1));
		System.out.println( width + " bits range " + range.negate() +" -- "  + range.subtract(BigInteger.valueOf(1)));

	}
	
	/**
	 * 
	 * Reference <br>
	 * http://csc.columbusstate.edu/woolbright/Instructions/MULTREG.pdf
	 * 
	 */
	void test2complementMlt_2(){
		String val20_1 = "01111111111111111111";   //524287
		String valxx_2 = "11111111111111111101";   //-3
		valxx_2 = "11";   //3
		valxx_2 = "10";   //2
		valxx_2 = "10";   //-2
//		valxx_2 = "111110";
		valxx_2 = "0111111111";   //511
		valxx_2 = "01111111111111111111";  //524287
		
		System.out.println("Start ............");
		int width = val20_1.length();
		int widthOp2 = valxx_2.length();
		
		int opt1 = Convertor.getSignedValFromBin(val20_1, width); // get the twos value in given width
		int opt2 = Convertor.getSignedValFromBin(valxx_2, widthOp2);  // get the twos value in given width

		long result = (long)opt1 * (long)opt2;
		System.out.println(width +" bit: "+val20_1 +" x " +valxx_2+ "=" + Long.toBinaryString(result) );
		System.out.println("Dec: "+opt1 +" x " +opt2 + "=" + result);
		
		//get the low order bit
		long iMask = result & SimConstants.BIN_MASK_20;  // get the lower order bits
		String lowOrderBit = Convertor.getBinFromInt((int)iMask, SimConstants.WORD_SIZE);   //low order bit in String
		int lowBitVal  = Convertor.getSignedValFromBin(lowOrderBit, SimConstants.WORD_SIZE); //low order bit in value
		if (lowBitVal < 0) {
			lowBitVal = lowBitVal + SimConstants.WORD_UNSIGN_VALUE; //since there is high order bit, it is not real -ve number
		}

		//get the high order bit
		result >>= SimConstants.WORD_SIZE;          //singed shift to keep the sign bit
		iMask = result & SimConstants.BIN_MASK_20;
		String highOrderBit = Convertor.getBinFromInt((int)iMask, SimConstants.WORD_SIZE);  //high order bit in String
		long highBitVal = Convertor.getSignedValFromBin(highOrderBit, SimConstants.WORD_SIZE); //high order bit in value
		highBitVal *= SimConstants.WORD_UNSIGN_VALUE; //because 20bit higher, enlarge to reflect its value
		
		//get the result
		long finalResult = highBitVal + lowBitVal;
		System.out.println("low order bit: " + lowOrderBit + ","+ lowBitVal);
		System.out.println("high order bit: " + highOrderBit + ","+ highBitVal);
		System.out.println("the result:"+  finalResult );
		
		//check overflow		
		BigInteger range = (BigInteger.valueOf(2l).pow(SimConstants.WORD_SIZE-1));
		if (BigInteger.valueOf(finalResult).compareTo(range) >= 0 || 
				BigInteger.valueOf(finalResult).compareTo(range.negate()) < 0) {
			System.out.println("OVERFLOW! " + SimConstants.WORD_SIZE + " bits range " + range.negate() +" -- "  + range.subtract(BigInteger.valueOf(1)));
		}
		
	}	
	
	/**
	 * 
	 */
	@Deprecated
	void test2complementMlt(){
		String val13_1 = "01101";   //13
		String val13_2 = "11010";   //-6
		int width = val13_1.length();
		
		int opt1 = Convertor.getSignedValFromBin(val13_1, 5);
		int opt2 = Convertor.getSignedValFromBin(val13_2, 5);
		
		int result = opt1 * opt2;
		System.out.println(width +" bin: "+val13_1 +" x " +val13_2+ "=" + result);
		System.out.println("Dec: "+opt1 +" x " +opt2 + "=" + result);
		
		String rstBin  = Convertor.getBinFromInt(result, width);
		System.out.println(width +" res: "+rstBin);
		System.out.println(width +" res: "+rstBin +" high order bits: ????????");//need to ask
		System.out.println(width +" res: "+rstBin +" low order bits: ????????");
		
		
		BigInteger range = (BigInteger.valueOf(2l).pow(width-1));
		if (BigInteger.valueOf(result).compareTo(range) >= 0 || 
				BigInteger.valueOf(result).compareTo(range.negate()) < 0) {
			System.out.println("OVERFLOW! " + width + " bits range " + range.negate() +" -- "  + range.subtract(BigInteger.valueOf(1)));
		}
		
	}
	void test2complementAdd(){
		String val13_1 = "1111110011011";   //-101
		String val13_2 = "0000000001010";   //10
		int width = val13_1.length();
		
		int opt1 = Convertor.getSignedValFromBin(val13_1, 13);
		int opt2 = Convertor.getSignedValFromBin(val13_2, 13);
		
		int result = opt1 + opt2;
		System.out.println(width+" bin: "+val13_1 +"+" +val13_2+ "=" + result);
		System.out.println("Dec: "+opt1 +"+" +opt2 + "=" + result);
		System.out.println(width+" res: "+Convertor.getBinFromInt(result, 13));
	}
	
	void test2complementSub(){
		String val13_1 = "1111110011011";   //-101
		String val13_2 = "0000000001010";   //10
		int width = val13_1.length();
		
		int opt1 = Convertor.getSignedValFromBin(val13_1, 13);
		int opt2 = Convertor.getSignedValFromBin(val13_2, 13);
		
		int result = opt1 - opt2;
		System.out.println(width+" bin: "+val13_1 +"-" +val13_2+ "=" + result);
		System.out.println("Dec: "+opt1 +"-" +opt2 + "=" + result);
		System.out.println(width+" res: "+Convertor.getBinFromInt(result, 13));
	}
	
	void testANDOrOR(String operator){
		String val20;
		val20 = "10011110000011111111";
		String val13 = "0000011001100";
		
		int iVal20 = Integer.parseInt(val20,2);
		int iVal13 = Integer.parseInt(val13,2);
		
		int result;
		if ("OR".equals(operator)) {
			result = iVal20 | iVal13;
		} else 
			result = iVal20 & iVal13;
		

		System.out.println(val20);
		System.out.println(pad(val13,20," "));
		System.out.println(Convertor.getBinFromInt(result, 20));
		
	}
	void testRRCRight() {
		int shift = 8;
		String val20 = "10010000000100000010";                         //string in binary
		val20 = "10011110000011111111";

		StringBuffer s = new StringBuffer(val20);
		int lastPos = s.length();
		for (int i = 0; i < shift; i++) {
			char c = s.charAt(lastPos-1);
			s.insert(0,c);
			s.deleteCharAt(lastPos);
		}
		String result = s.toString();
		System.out.println("val20: " + val20);
		System.out.println("result:" + result );
	}
	
	void testRRCLeft(){
		int shift = 8;
		String val20 = "10010000000100000010";                         //string in binary
		val20 = "10011110000011111111";

		StringBuffer s = new StringBuffer(val20);
		for (int i = 0; i < shift; i++) {
			char c = s.charAt(0);
			s.append(c);
			s.deleteCharAt(0);
		}
		String result = s.toString();
		System.out.println("val20: " + val20);
		System.out.println("result:" + result );
	}
	
	void testShiftArith(String direction) {
		int shift = 8;
		String val20 = "10010000000100000010";                         //string in binary
		val20 = "10011110000011111111";

		int width = val20.length();
		
		int iVal = Integer.parseInt(val20,2);
		
		String signBit = val20.substring(0,1);
		int result;
		if ("right".equals(direction)) {
			result = iVal >> shift;
		} else
			result = iVal << shift;
			
		String res19 = Convertor.getBinFromInt(result, width-1);
		String res20 = signBit + res19;
		System.out.println("val20:              "+ val20);
		System.out.println("result " + pad(direction,5," ") +" shift: "+ res20);
	}
	
	void testLeftShiftLogical() {
		
		int shift = 8;
		String val20 = "10010000000100000010";                         //string in binary

		StringBuilder sb = new StringBuilder(val20);
		for (int i = 0; i < shift; i++) {
			sb.append("0");
			sb.deleteCharAt(0);
		}
		String result = sb.toString();
		System.out.println("val20: " + val20);
		System.out.println("result:" + result);
	}

	void testRightShiftLogical() {
		
		int shift = 8;
		String val20 = "10010000000100000010";                         //string in binary


		StringBuilder sb = new StringBuilder(val20);
		int lastPos = sb.length();
		for (int i = 0; i < shift; i++) {
			sb.insert(0,"0");
			sb.deleteCharAt(lastPos);
		}
		String result = sb.toString();
		System.out.println("val20: " + val20);
		System.out.println("result:" + result );
	}
	
	void testNOT(){
		
		String val13 = "1001000000010";                         //string in binary
		System.out.println(val13 +"=" + 
					Convertor.getSignedValFromBin(val13, 13));  //1001000000010=-3582 
		int iVal = Integer.parseInt(val13,2);   				//assign to 32 bit int
		int result13 = ~iVal;                                   //Do a 'NOT' or one's complement  
//		System.out.println("incorrect="+ result13);             //incorrect result -4611                   
		
		//toBinaryString always return unsigned 32 bits, we only take the bit we need
		String sVal = Integer.toBinaryString(result13).substring(32-13); 
		System.out.println(sVal +"="+Convertor.getSignedValFromBin(sVal, 13)); //0110111111101=3581
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 */
	void testConvert(){
		int val = 5028;
		String bin = Convertor.getBinFromInt(val, 13);
		System.out.println("in binary form:"+ bin);
		
		System.out.println("value form: "+Convertor.getSignedValFromBin(bin, 14));
		

		String []str = Convertor.bitToArray(bin);
		System.out.println("len:"+str.length+":"+Arrays.asList(str));
		
		System.out.println(Integer.parseInt(bin,2));
		
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
	private static String pad(String val, int length, String padder){
		if (val==null || "".equals(val) || val.length() >= length) {
			return val;
		}
		int padLen = length-val.length();
		for (int i = 0; i < padLen; i++) {
			val = padder + val;
		} 
		
		return val;
	}	
	
	void testHex(){
		int val = 0x100000;
		System.out.println(val);
		System.out.println(Integer.toBinaryString(val));
	}
	

}
