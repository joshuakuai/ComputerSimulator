package edu.gwu.cs6461.sim.util;

import org.apache.log4j.Logger;

/**
 * Utility class for data conversion
 * 
 * @author marcoyeung
 * 
 */
public class Convertor {
	/**logger to log message to file*/
	private final static Logger logger = Logger.getLogger(Convertor.class);

	/**
	 * Given a binary two's complement form, the method returns the signed
	 * integer value
	 * 
	 * eg  <br>
	 * 	String binVal = "01111000000000000000"; <br>
	 *	int numOfBit = 20;       <br>
	 *	
	 *	output: 01111000000000000000:491520
	 *		<br><br> 
     *
	 *	String binVal = "11111000000000000000";<br>
	 *	int numOfBit = 20;<br>
	 *	output: 11111000000000000000:-32768
	 *	
	 * 
	 * @param val
	 *            two's complement in binary bit form
	 * @param numOfBit
	 *            number of bit to represent the value
	 * @return signed int that reflect the value in val
	 */
	public static int getSignedValFromBin(String binVal, int numOfBit) {
		if (binVal == null || "".equals(binVal)) {
			return 0;
		}
		try {

			Integer iRes = Integer.parseInt(binVal, 2);
			if (binVal.charAt(0) == '1' && binVal.length() == numOfBit)
				iRes -= (1 << numOfBit);

			return iRes;
		} catch (Exception e) {
			logger.error("failed to complete the conversion", e);
		}

		return 0;
	}

	/**
	 * Return two's complement value in String form with the length specified
	 * 
	 * @param iVal
	 *            int value (+ve or -ve)
	 * @param numOfBit
	 *            since int is 32 bit, numOfBit specify the required number of bit.
	 * 
	 * @return return the two's int value in String form
	 */
	public static String getSignedBinFromInt(int iVal, int numOfBit) {

		String binV = Integer.toBinaryString(iVal);
		int len = binV.length();

		String resBin = binV;
		if (numOfBit >= len) {
			return padZero(resBin,numOfBit);
		}

		resBin = binV.substring(len - numOfBit);

		return resBin;
	}
	
	/**padding Zero in-front of string for alignment purpose*/
	public static String padZero(String val, int length){
		if (val==null || "".equals(val) || val.length() >= length) {
			return val;
		}
		int padLen = length-val.length();
		for (int i = 0; i < padLen; i++) {
			val = "0" + val;
		} 
		
		return val;
	}

	/**padding space for aligning the string */
	public static String padSpace(String key, int space) {

		if (key == null && "".equals(key)) {
			return "";
		}
		int sp = space - key.length();
		for (int i = 0; i <= sp; i++) {
			key += " ";
		}
		return key;
	}
	
	
	public static String getBinFromInt(int iVal, int numOfBit) {
		return getSignedBinFromInt(iVal, numOfBit);
		
	}
	
	/**split the bit into array */
	public static String[] bitToArray(String bit) {
		String[] rst = { "" };
		if (bit == null || "".equals(bit.trim())) {
			return rst;
		}

		String bits[] = new String[bit.length()];
		for (int i = 0; i < bit.length(); i++) {
			bits[i] = bit.substring(i, i + 1);
		}
		return bits;

	}
	
	/**Convert the int to binary without consider complement*/
	public static String  display(int value) {
		String str="",tmp;
		int displayMask = 1 << 31;
		for (int bit = 1; bit <= 32; bit++) {
			str += (value & displayMask) == 0 ? "0" : "1";

			value <<= 1; 

		} // end for
		
		return str;
	} // end method display	
	
}
