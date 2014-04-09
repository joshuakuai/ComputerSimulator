package edu.gwu.cs6461.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import edu.gwu.cs6461.sim.common.OpCode;
import edu.gwu.cs6461.sim.util.Convertor;

public class MicroToBinary {

	private static String instrStr = "{0}{1}{2}{3}{4}{5}";
	private static String instrStrIO = "{0}{1}{2}{3}{4}";
	private static String instrStrDATA = "{0}";
	private static String instrStrTwo = "{0}{1}";

	/*			"JGE R1,X1,255i",
			"LDR R1,X1,200i",
			"STR R1,X1,200i",
			"LDX X1,200",
			"STX X1,200i",*/
	private String[] instrs = { 
			"LDX X3,101",    	//load memory advance to x3
			"LDR R0,X1,101", 	//testing
			"LDR R3,X1,102",  	//load number of character to get in R3
			"IN R1,0",       	//Perform IN
			"OUT R1,1",  		//Perform OUT
			"STR R1,X3,105",	//store IN character into Memory
			"SIR R3,1",			//Decrease counter by 1
			"AIR R0,1",			//increase counter by 1
			"STR R0,X0,101",    //
			"STR R3,X0,102",    //
			"JGE R3,X0,51",		//jump to IN to get another character
			"EOP ",
			"DATA 105,100",
			"DATA 0,101",
			"DATA 3,102"  //
			
	};
	private static Map<String, OpCode>allOpCode =new HashMap<>();

	static {
		for (OpCode code: OpCode.values()) {
			allOpCode.put(code.name(), code);
		}
	}

	private String[] parseInstr(String instr) {
		/*StringTokenizer st = new StringTokenizer(instr, ",");
		String[]res = new String[st.countTokens()];
		for (int j = 0; st.hasMoreTokens(); j++) {
			res[j] = st.nextToken();
		}*/

		String[] res2 = instr.split("[,]");

		return res2;
	}

	private void convertBinaryInstr(){
		for(String it: instrs) {
			doGetBinaryInstr(it);
		}
	}
	private String doGetBinaryInstr(String it) {

		String code = "";
		String microCode ="";
		String[] operand;
		OpCode oCode =null;
		String bInstr = "";
		int id = it.indexOf(" ");
		if (id > 0) {
			code = it.substring(0,id);
			oCode = allOpCode.get(code);
			microCode = it.substring(id);
			operand = parseInstr(microCode.trim());

			switch (oCode) {
			case SIR: case AIR:
				bInstr = MessageFormat.format(instrStrTwo, 
						oCode.getbStr(),reg(operand[0])+ "0000" +
						Convertor.getBinFromInt(Integer.parseInt(operand[1]), 8));
				break;
			case EOP:
				bInstr = MessageFormat.format(instrStrDATA, 
						oCode.getbStr()+ Convertor.padZero("0", 14));
				break;
			case DATA:
				bInstr = MessageFormat.format(instrStrDATA, 
						Convertor.getBinFromInt(Integer.parseInt(operand[0]), 20));
				break;
			case IN:case OUT:
				bInstr = MessageFormat.format(instrStrIO, oCode.getbStr(),
						reg(operand[0]),
						"00",
						"000000",
						binMemAdd(operand[1], 4));
				break;
			case LDX:case STX:
				bInstr = MessageFormat.format(instrStr, oCode.getbStr(),
						"00",
						ireg(operand[0]),
						iort(operand[1],"i")
						,iort(operand[1],"t"),
						binMemAdd(operand[1], 8));

				break;
			case LDR: case STR:case LDA:
			case JGE:
				bInstr = MessageFormat.format(instrStr, oCode.getbStr(), 
						reg(operand[0]),
						ireg(operand[1]),
						iort(operand[2],"i")
						,iort(operand[2],"t"),
						binMemAdd(operand[2], 8));
				break;
			default:
				break;
			}
			if (!bInstr.equals("")) {
				System.out.println(bInstr + "      #" + it);	
			}
			bInstr="";
		}

		return "";
	}


	public static void main(String[] args) throws Exception {

		MicroToBinary bin = new MicroToBinary();
		bin.convertBinaryInstr();

	}


	private String binMemAdd(String add, int len) {
		String[] parts = add.split("[it]");

		int address = Integer.parseInt(parts[0]);
		String memBin = Convertor.getBinFromInt(address, len+1);
		memBin=memBin.substring(memBin.length()-len);
		return memBin;

	}
	private String iort(String add, String key) {
		int id = add.indexOf(key);
		if (id >0) {
			return "1";
		}
		return "0";
	}
	private String reg(String Reg){
		String binary1="00";
		if(Reg.equalsIgnoreCase("r0"))
			binary1="00";
		else if(Reg.equalsIgnoreCase("r1"))
			binary1="01";
		else if(Reg.equalsIgnoreCase("r2"))
			binary1="10";
		else if(Reg.equalsIgnoreCase("r3"))
			binary1="11";
		return binary1;
	}
	private String ireg(String index){
		String binary1="00";
		if(index.equalsIgnoreCase("x0"))
			binary1="00";
		if(index.equalsIgnoreCase("x1"))
			binary1="01";
		if(index.equalsIgnoreCase("x2"))
			binary1="10";
		else if(index.equalsIgnoreCase("x3"))
			binary1="11";
		return binary1;
	}

}
