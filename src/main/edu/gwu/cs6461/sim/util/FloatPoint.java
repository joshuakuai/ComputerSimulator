package edu.gwu.cs6461.sim.util;

import java.math.BigDecimal;

import edu.gwu.cs6461.sim.exception.FloatingPointException;


/**
 * 
 * @author marcoyeung
 * @Revised   Apr 26, 2014 - 1:35:17 PM  
 */
public class FloatPoint {
	public static final int FLOAT_SIGN_INDEX = 0;
	public static final int FLOAT_SIGN_SIZE = 1;
	public static final int FLOAT_EXPONENT_INDEX = 1;
	public static final int FLOAT_EXPONENT_SIZE = 7;
	public static final int FLOAT_EXPONENT_RESERVED = 128;
//	public static final int FLOAT_EXPONENT_BIAS = 63;
	public static final int FLOAT_EXPONENT_BIAS = (int)(Math.pow(2,FLOAT_EXPONENT_SIZE-1) -1);
	public static final int FLOAT_MANTISSA_INDEX = 8;
	public static final int FLOAT_MANTISSA_SIZE = 12;
	/**  ~1.8444492E19 */
	public static final float FLOAT_MAX_VALUE = (float) ((2- Math.pow(2,FLOAT_MANTISSA_SIZE*-1 )) * Math.pow(2, FLOAT_EXPONENT_BIAS) );

	/** sign bit as a string */
	private String signBit;
	/** exponent bits as a string */
	private String exponentBits;
	/** fraction bits as a string */
	private String fractionBits;
	/** implied bit as a string */
	private String impliedBit="1";

	/** biased exponent value */
	private int biased;
	/** fraction value */
	private long fraction;

	/** exponent bias */
	private int bias;

	/** float number value */
	private float floatValue;

	/** true if number value is zero */
	private boolean isZero;
	/** true if reserved exponent value */
	private boolean isReserved;
	/** true if denormalized number value */
	private boolean isDenormalized;


	public FloatPoint(float value) {
        // Convert the value to a character array of '0' and '1'.
//		String binVal = Convertor.getSignedBinFromInt(Float.floatToIntBits(value), 32);

		if (value > FLOAT_MAX_VALUE) {
			floatValue = Float.POSITIVE_INFINITY;
			return;
		} else if (value < (-1*FLOAT_MAX_VALUE) ) {
			floatValue = Float.NEGATIVE_INFINITY;
			return;
		}
		
		String binVal = floatToBits(value);

        floatValue = value;

        decompose(binVal,
                  FLOAT_EXPONENT_BIAS,  FLOAT_EXPONENT_RESERVED,
                  FLOAT_SIGN_INDEX,     FLOAT_SIGN_SIZE,
                  FLOAT_EXPONENT_INDEX, FLOAT_EXPONENT_SIZE,
                  FLOAT_MANTISSA_INDEX, FLOAT_MANTISSA_SIZE);
	}

	public FloatPoint(String sign, String biasedExp,String mantissa ) throws Exception {
        // Check the sign.
		if ((!"0".equals(sign)) && (!"1".equals(sign))) {
            throw new FloatingPointException("Invalid sign bit.");
        }

        FloatFraction fraction = new FloatFraction(mantissa);

        // Convert to the float value.
        floatValue = expNotationToFloat(sign, biasedExp, mantissa);
        
        String binVal = sign+biasedExp+mantissa;

        decompose(binVal,
                  FLOAT_EXPONENT_BIAS,  FLOAT_EXPONENT_RESERVED,
                  FLOAT_SIGN_INDEX,     FLOAT_SIGN_SIZE,
                  FLOAT_EXPONENT_INDEX, FLOAT_EXPONENT_SIZE,
                  FLOAT_MANTISSA_INDEX, FLOAT_MANTISSA_SIZE);
	}

	private void decompose(String bits,
			int bias,          int reserved,
			int signIndex,     int signSize,
			int exponentIndex, int exponentSize,
			int fractionIndex, int fractionSize)
	{
		this.bias = bias;

		// Extract the individual parts as strings of '0' and '1'.
		signBit =  bits.substring(FLOAT_SIGN_INDEX,FLOAT_SIGN_INDEX+FLOAT_SIGN_SIZE);
		exponentBits  =  bits.substring(FLOAT_EXPONENT_INDEX,FLOAT_EXPONENT_INDEX+FLOAT_EXPONENT_SIZE);
		fractionBits  =  bits.substring(FLOAT_MANTISSA_INDEX,FLOAT_MANTISSA_INDEX+FLOAT_MANTISSA_SIZE);

		try {
			biased   = Integer.parseInt(exponentBits, 2);
			fraction = Long.parseLong(fractionBits, 2);
		} catch (NumberFormatException ex) {}

		isZero         = (biased == 0) && (fraction == 0);   //CASE ZERO
		isDenormalized = (biased == 0) && (fraction != 0);   //CASE ?
		isReserved     = (biased == reserved);

		impliedBit = isDenormalized || isZero || isReserved ? "0" : "1";
		
	}
	
	private static abstract class Field {
		private StringBuilder field;

		private Field(int size, String bits) throws FloatingPointException {
            if (size <= 0) {
                throw new FloatingPointException("Invalid field size: " + field);
            }

            int length = bits.length();
            field = new StringBuilder(size);

            // String length matches field size.
            if (length == size) {
                field.append(bits);
                validate();
            }

            // String length < field size:  Pad with '0'.
            else if (length < size) {
                field.append(bits);
                validate();
                for (int i = length; i < size; ++i) field.append('0');
            }
            // String length > field size:  Truncate at the right end.
            else {
                field.append(bits.substring(0, size));
                validate();
            }
        }

		protected int toInt() throws FloatingPointException {
			try {
				return Integer.parseInt(field.toString(), 2);
			} catch (NumberFormatException ex) {
				throw new FloatingPointException("Invalid binary number format: "
						+ field.toString());
			}
		}

		private void validate() throws FloatingPointException {
			int length = field.length();

			for (int i = 0; i < length; ++i) {
				char bit = field.charAt(i);
				if ((bit != '0') && (bit != '1')) {
					throw new FloatingPointException("Invalid fraction bit string.");
				}
			}
		}
        public String toString() { return field.toString(); }
	}
	public static class FloatFraction extends Field {
        public FloatFraction(String bits) throws FloatingPointException
        {
            super(FLOAT_MANTISSA_SIZE, bits);
        }		
	}

	public static void validateFloatBiasedExponent(int biased)
			throws FloatingPointException {
		if ((biased < 0) || (biased > FLOAT_EXPONENT_RESERVED)) {
			throw new FloatingPointException(
					"The biased exponent value should be " + "0 through "
							+ FLOAT_EXPONENT_RESERVED + ".");
		}
	}
	public String exponentBits() { return exponentBits; }
	public int biasedExponent() { return biased; }
	public int unbiasedExponent() {
		return isDenormalized ? -bias + 1 : biased - bias;
	}
    public String signBit() { return signBit; }
    public String mantissaBits() { return fractionBits; }
	public String significandBits() {return impliedBit + "." + fractionBits; }
	public String exponentForm() {return signBit+exponentBits+fractionBits; }
    public boolean isZero() { return isZero; }
    public boolean isDenormalized() { return isDenormalized; }
//    public boolean isExponentReserved() { return isReserved; }
    public float floatValue() { return floatValue; }
	public static int toFloatBiasedExponent(int unbiased) {
		return unbiased + FLOAT_EXPONENT_BIAS;
	}
	public static int toFloatUnbiasedExponent(int biased) {
		return biased == 0 ? -FLOAT_EXPONENT_BIAS + 1 : biased- FLOAT_EXPONENT_BIAS;
	}
	
	
	
	
	
	
	
	/*************************************************************************/
	/*************************************************************************/

	/**
	 * 
	 * http://stackoverflow.com/questions/343584/how-do-i-get-whole-and-fractional-parts-from-double-in-jsp-java
	 * 
	 * Convert floating fraction portion to binary 
	 * */
	private String fractionToBinary(double n, int dp){
		StringBuilder sb = new StringBuilder();
		
		double ff;// = n - (long) n;
		ff = n%1;
				
		if (ff == 0) return "0";
		
		int d,i=0;
		double tmp = ff;
		while (tmp > 0) {
			if (i++ > dp) {
				break;
			}
			tmp = tmp * 2;
			d = (int)tmp;
			tmp = tmp - d;
			sb.append(d);
		}
		
		return sb.toString();
	}
	
	/**
	 * Floating point normalization to exponent notation
	 * */
	private int normalize(StringBuilder sb){ 
		int size = sb.length();
		int dPos = sb.indexOf(".");
		if (size == 1) return 0;

		String decPart=""; 
		StringBuilder fraPart=new StringBuilder();;
		if (dPos > 0) {
			decPart = sb.substring(0, dPos);
			fraPart = new StringBuilder(sb.substring(dPos + 1));
		} else {
			decPart = sb.toString();
		}

		int exp=0;
		if (!"0".equals(decPart.toString()) || dPos == -1) {
			exp = decPart.length() - 1;
			if (dPos>0) sb.deleteCharAt(dPos);
			sb.insert(1, ".");
			
		} else if (!"0".equals(fraPart.toString())) {
			int i = 0;
			while (i < size) {
				char c = fraPart.charAt(i);
				i++;

				if (c == '1') {
					exp = -i;
					fraPart.insert(i, ".");
					fraPart.replace(0, i-1, "");
					break;
				}
			}
			sb.replace(0, size, fraPart.toString());
			
		}//
		
		return exp;
	}
	
	/**
	 * binary represent of floating point in exponent and mantissa format
	 * */
	private String floatToBits(float ff) {

		int sign = 1;
		if (ff < 0) {
			sign = -1;
			ff *= -1;
		}
		String dinb=  Convertor.decimalToBinary((long)ff);
		String fdinb = fractionToBinary(ff, FLOAT_MANTISSA_SIZE);
		StringBuilder normDinb = new StringBuilder(dinb + "." + fdinb);
		int exp = normalize(normDinb);
		
//		System.out.println(ff*sign +" => "+ normDinb+ "x2^" + exp);

		String mantissa = normDinb.substring(2);  
		mantissa = Convertor.padZeroAfter(mantissa, FLOAT_MANTISSA_SIZE); 
		mantissa = mantissa.substring(0,FLOAT_MANTISSA_SIZE); //ensure the length
		
		int biasedExp = exp + FLOAT_EXPONENT_BIAS;
		String expBin = Convertor.decimalToBinary(biasedExp);
		expBin = Convertor.padZero(expBin,FLOAT_EXPONENT_SIZE); //ensure the length
		String signStr = (sign == -1 ? "1" : "0");
		String expNotation = signStr +expBin+mantissa;
//		String tmp = signStr +"|"+ expBin+"|"+mantissa;
//		System.out.println("Floating: " + tmp);
		
		return expNotation;
	}
	
	/**
	 * Convert Exponent notation number to floating point float number
	 * @throws FloatingPointException 
	 * */
	private float expNotationToFloat(String sign, String biasedExp, String mantissa) throws FloatingPointException {
		int bExp = Convertor.getIntFromBin(biasedExp);
		validateFloatBiasedExponent(bExp);
		
		int unbiasExp = bExp - FLOAT_EXPONENT_BIAS;
		
		StringBuilder denormal = new StringBuilder(mantissa);
		deNormalize(denormal, unbiasExp);
//		System.out.println("exp:"+unbiasExp+" denormalized:"+denormal.toString());
		
		String snorm = denormal.toString();
		float value = binFracToFloat(snorm);
		value *= ("1".equals(sign)? -1:1);
		return value;
	}
	
	/**
	 * convert fraction part of the floating binary to float decimal
	 * */
	private float binFracToFloat(String str) {
		
		double result = 0;
		int dPos = str.indexOf(".");
		if (dPos ==-1) {
			result = Convertor.getIntFromBin(str);
		} else {
			String decPart = str.substring(0, dPos);
			if (!decPart.equals("null") ) {
				result = Long.parseLong(decPart, 2);
//				result = Convertor.getIntFromBin(decPart);
			}
			String fraPart = str.substring(dPos + 1);
			
			for (int i = 0; i < fraPart.length(); i++) {
				if (fraPart.charAt(i) == '1')
					result = result + (float) Math.pow(2, (i + 1) * -1);
			}
		}
		
		return (float)result;
	}
	
	/**
	 * denormalize the exponent notation
	 * */
	private void deNormalize(StringBuilder sb, int exp){
//		String impliedBit = "1";
		
		if (exp > 0 ) {
			int space =exp-FLOAT_MANTISSA_SIZE; 
			if (space >0) {
				String a = "0";
				a = Convertor.padZeroAfter(a, space);
				sb.append(a);
			}
			sb.insert(exp, ".");
			sb.insert(0, impliedBit);
		} else if (exp == 0) {
			long a = Integer.valueOf(sb.toString());
			if (a == 0 && exp == 0) impliedBit ="0";   //CASE ZERO
			sb.insert(0, impliedBit + ".");
		} else {
			sb.insert(0,impliedBit);
			for (int i = 0; i < (exp*-1) -1; i++) {
				sb.insert(0, "0");
			}
			sb.insert(0,"0.");	
		}
		
	}	
	
}
