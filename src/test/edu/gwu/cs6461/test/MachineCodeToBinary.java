package edu.gwu.cs6461.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import edu.gwu.cs6461.sim.common.OpCode;
import edu.gwu.cs6461.sim.common.SimConstants;
import edu.gwu.cs6461.sim.util.Convertor;

/**
 * 
 * 
 * @author marcoyeung
 * @Revised   Apr 11, 2014 - 11:13:34 AM  
 */
public class MachineCodeToBinary {

	private static String instrStr = "{0}{1}{2}{3}{4}{5}";
	private static String instrStrIO = "{0}{1}{2}{3}{4}";
	private static String instrStrDATA = "{0}";
	private static String instrStrTwo = "{0}{1}";
	private static String instrStrThree = "{0}{1}{2}";
	private static String instrStrSeven = "{0}{1}{2}{3}{4}{5}{6}";

	private static Map<String, OpCode>allOpCode =new HashMap<>();

	static {
		for (OpCode code: OpCode.values()) {
			allOpCode.put(code.name(), code);
		}
	}

	/**************************************************************************/
	//	Main
	/**************************************************************************/
	public static void main(String[] args) throws Exception {

		MachineCodeToBinary bin = new MachineCodeToBinary();
		bin.convertBinaryInstr();

	}
	
	
	/**
	 * 
	 * 
	 * */
	private void convertBinaryInstr(){
		String codePart="";
		try (BufferedReader br= new BufferedReader(
				new FileReader("bin/tester6-microcode.txt"));
				BufferedWriter bw = new BufferedWriter(
						new FileWriter("src/resources/tester6-binary.txt")); ) {

			int idx = 0;
			String tmp;
			String binInstr = "";
			while ((tmp = br.readLine()) != null) {
				if (tmp.startsWith(SimConstants.FILE_INSTRUCTION_HEAD) ||
						tmp.startsWith(SimConstants.FILE_DATA_HEAD+":") ||
						tmp.startsWith(SimConstants.FILE_COMMENT) ) {
					bw.write(tmp);
					bw.newLine();
					continue;
				}
				
				codePart = tmp;
				idx = codePart.indexOf(SimConstants.FILE_COMMENT);
				if (idx > 0) {
					codePart = codePart.substring(0, idx);
				}
				
				binInstr = doGetBinaryInstr(codePart);
				
				if ("".equals(binInstr)) {
					bw.write(tmp);
					System.out.println(tmp);
				} else {
					tmp=tmp.replaceFirst(SimConstants.FILE_COMMENT, " ");
					binInstr = binInstr + Convertor.padSpace(" ", 5)+SimConstants.FILE_COMMENT + tmp; 
					bw.write(binInstr);
					
					System.out.println(binInstr);
				}
				bw.newLine();
				bw.flush();
			}
			
		} catch (Exception e) {
			
			System.out.println("err: " + codePart);
			e.printStackTrace();
		}
		
		
//		for(String it: instrs) {
//			doGetBinaryInstr(it);
//		}
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
			microCode = it.substring(id).trim();
			operand = parseInstr(microCode);

			switch (oCode) {
			case TRAP:case HLT: case RFS:
				bInstr = MessageFormat.format(instrStrThree, 
						oCode.getbStr(),"000000",
						Convertor.getBinFromInt(Integer.parseInt(operand[0]), 8));
				break;
			case LDX:case STX: case JMP: case JSR:
				bInstr = MessageFormat.format(instrStr, oCode.getbStr(),
						"00",
						ireg(operand[0]),
						iort(operand[1],"i")
						,iort(operand[1],"t"),
						binMemAdd(operand[1], 8));

				break;
			case LDR: case STR:case LDA: case JGE:case JZ:case JNE:case SOB:
			case AMR: case SMR:
				bInstr = MessageFormat.format(instrStr, oCode.getbStr(), 
						reg(operand[0]),
						ireg(operand[1]),
						iort(operand[2],"i")
						,iort(operand[2],"t"),
						binMemAdd(operand[2], 8));
				break;
			case JCC:
				String t = Integer.toBinaryString(Integer.parseInt(operand[0]));
				bInstr = MessageFormat.format(instrStr, oCode.getbStr(), 
						t.substring(t.length()-2),
						ireg(operand[1]),
						iort(operand[2],"i")
						,iort(operand[2],"t"),
						binMemAdd(operand[2], 8));
				break;
			case SIR: case AIR:
				bInstr = MessageFormat.format(instrStrTwo, 
						oCode.getbStr(), reg(operand[0])+ "0000" +
						Convertor.getBinFromInt(Integer.parseInt(operand[1]), 8));
				break;
			case MLT: case DVD: case TRR:case AND: case ORR: 
				bInstr = MessageFormat.format(instrStrTwo, 
						oCode.getbStr(), reg(operand[0])+reg(operand[1])+ "00" +
						Convertor.padZero("0", 8));
				break;
			case NOT:
				bInstr = MessageFormat.format(instrStrTwo, 
						oCode.getbStr(), reg(operand[0])+"0000" +
						Convertor.padZero("0", 8));
				break;
			case SRC: case RRC:
				String aorl = operand[2].equalsIgnoreCase("l")?"1":"0";
				String lorr = operand[3].equalsIgnoreCase("l")?"1":"0";
				bInstr = MessageFormat.format(instrStrSeven, 
						oCode.getbStr(), reg(operand[0]),"00",aorl,lorr,"000",
						binMemAdd(operand[1], 5));
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
				
			case FADD:case FSUB:case VADD:case VSUB:case CNVRT:case LDFR:case STFR:
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
//			if (!bInstr.equals("")) {
//				System.out.println(bInstr + "      #" + it);	
//			}
		}

		return bInstr;
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
	private String[] parseInstr(String instr) {
		/*StringTokenizer st = new StringTokenizer(instr, ",");
		String[]res = new String[st.countTokens()];
		for (int j = 0; st.hasMoreTokens(); j++) {
			res[j] = st.nextToken();
		}*/

		String[] res2 = instr.split("[,]");

		return res2;
	}

}
