package edu.gwu.cs6461.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import edu.gwu.cs6461.sim.common.OpCode;



public class MicroToBinary {

	public static String binary="";
	public static void main(String[] args) throws Exception {

		new MicroToBinary().startUp();

	}
	private static void startUp() throws Exception {

		System.out.println(new File(".").getAbsoluteFile());


		BufferedReader in = new BufferedReader(new FileReader("bin/binary.txt"));
		String text = "";
		String input="";
		//		PrintWriter out = new PrintWriter(new FileWriter("bin.txt"));
		BufferedWriter out = new BufferedWriter(new FileWriter("bin.txt"));

		out.write("Hello ");
		out.newLine();
		out.write("world");
		out.newLine();

		while ((text = in.readLine()) !=null) {			
			// text = in.readLine();
			if(text.charAt(0)!='#'){
				System.out.println(text);
				input=text;
			}


			//String input="IN  r2,2			#comment something something";
			String comment = input.substring(input.indexOf('#'));
			input = input.replaceAll("\\s+","");

			String line=input.substring(0, 12);
			//System.out.println(input);
			//System.out.println(line);
			if(line.contains("EOP")){
				binary+="110111";
				binary+="000000000000";
			}
			else if(line.contains("LDR")){
				binary+="000001";
				normal(line);
			}		
			else if(line.contains("STR")){
				binary+="000010";
				normal(line);
			}	
			else if(line.contains("LDA")){
				binary+="000011";
				normal(line);
			}
			else if(line.contains("JZ")){
				binary+="001010";
				normal(line);
			}
			else if(line.contains("JNE")){
				binary+="001011";
				normal(line);
			}
			else if(line.contains("JMP")){
				binary+="001101";
				binary+="0000";
				JUMPing(line);
			}
			else if(line.contains("JSR")){
				binary+="001110";
				binary+="0000";
				JUMPing(line);			
			}
			else if(line.contains("RFS")){
				binary+="001111";
				binary+="00000000000000";			
			}
			else if(line.contains("SOB")){
				binary+="010000";
				normal(line);
			}
			else if(line.contains("JGE")){
				binary+="010001";
				normal(line);
			}
			else if(line.contains("AMR")){
				binary+="000100";
				normal(line);
			}
			else if(line.contains("SMR")){
				binary+="000101";
				normal(line);
			}
			else if(line.contains("AIR")){
				binary+="000110";
				normal(line);
			}
			else if(line.contains("SIR")){
				binary+="000111";
				normal(line);
			}
			////////////////////////////////////

			else if(line.contains("DVD")){
				binary+="010101";
				binary+=Register(line.substring(3, 5));
				binary+=Register(line.substring(6, 8));
				binary+="0000000000";
			}
			else if(line.contains("MLT")){
				binary+="010100";
				binary+=Register(line.substring(3, 5));
				binary+=Register(line.substring(6, 8));
				binary+="0000000000";
			}
			else if(line.contains("NOT")){
				binary+="011001";
				binary+=Register(line.substring(3, 5));		
				binary+="000000000000";

			}
			else if(line.contains("IN")){
				binary+="111101";
				binary+=Register(line.substring(3, 5));
				binary+="00000000";
				binary+="000";
				binary+=line.substring(5, 6);

			}
			else if(line.contains("OUT")){
				binary+="111110";
				binary+=Register(line.substring(3,5));
				binary+="00000000";
				binary+="000";
				binary+=line.substring(5, 6);
			}
			binary+="     "+comment;
			//System.out.println(binary);
			out.write(binary);
			out.newLine();
			out.flush();
			binary="";


		}
		//close file
		in.close();
		out.close();
	}

	//////////////////////////////////////////////
	////////////////////////////////////////////
	public static void normal(String line){
		int address=0;
		String temp="";
		binary+=Register(line.substring(3, 5));
		System.out.println(line.substring(3, 5));
		binary+="00";
		int i = line.indexOf(',');
		i++;
		while (line.charAt(i)!=73 && line.charAt(i)!='#'){			
			temp+= line.charAt(i);
			i++;
		}
		if(line.charAt(i)=='I'){
			binary+="1";
			binary+="0";
			address=Integer.parseInt(temp.substring(0, temp.indexOf(',')));
			binary+=Address(address);
		}
		else{
			binary+="0";
			binary+="0";		
			System.out.println(temp);
			address=Integer.parseInt(temp);
			binary+=Address(address);
		}

	}
	public static void JUMPing(String line){
		int address=0;
		String temp="";
		binary+=Register(line.substring(3, 5));
		System.out.println(line.substring(3, 5));
		binary+="00";
		int i = 3;

		while (line.charAt(i)!=73 && line.charAt(i)!='#'){			
			temp+= line.charAt(i);
			i++;
		}
		if(line.charAt(i)=='I'){
			binary+="1";
			binary+="0";
			address=Integer.parseInt(temp.substring(0, temp.indexOf(',')));
			binary+=Address(address);
		}
		else{
			binary+="0";
			binary+="0";		
			System.out.println(temp);
			address=Integer.parseInt(temp);
			binary+=Address(address);
		}

	}
	public static String Register(String Reg){
		String binary1="";
		if(Reg.equals("r0"))
			binary1="00";
		else if(Reg.equals("r1"))
			binary1="01";
		else if(Reg.equals("r2"))
			binary1="10";
		else if(Reg.equals("r3"))
			binary1="11";
		return binary1;
	}
	public void IndexRegister(String index){
		if(index=="x0")
			binary+="00";
		if(index=="x1")
			binary+="01";
		if(index=="x2")
			binary+="10";
		else if(index=="x3")
			binary+="11";
	}
	public void Indirect(String Ind){

	}
	public void Trace(String trace){

	}
	public static String Address(int address){
		String adr=Integer.toString(address, 2);
		String finalAddress="";
		if(adr.length()<8){
			int length =  8-adr.length();
			for(int i=0; i<length;i++){
				finalAddress+="0";
			}
			finalAddress+=adr;
		}
		else
			finalAddress=adr;
		return finalAddress;
	}
}
