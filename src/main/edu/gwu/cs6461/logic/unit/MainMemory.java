package edu.gwu.cs6461.logic.unit;

import static edu.gwu.cs6461.sim.common.SimConstants.FILE_COMMENT;
import static edu.gwu.cs6461.sim.common.SimConstants.FILE_DATA_HEAD;
import static edu.gwu.cs6461.sim.common.SimConstants.FILE_INSTRUCTION_HEAD;
import static edu.gwu.cs6461.sim.common.SimConstants.MEMORY_ADDRESS_LIMIT;
import static edu.gwu.cs6461.sim.common.SimConstants.WORD_SIZE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.logic.CPUController;
import edu.gwu.cs6461.sim.bridge.HardwareData;
import edu.gwu.cs6461.sim.bridge.Observable;
import edu.gwu.cs6461.sim.common.HardwarePart;
import edu.gwu.cs6461.sim.common.MemoryType;
import edu.gwu.cs6461.sim.util.PropertiesLoader;
import edu.gwu.cs6461.sim.util.PropertiesParser;

public class MainMemory extends Observable{
	
	private static final Logger logger = Logger.getLogger(MainMemory.class);
	
	private Map<Integer,Map<Integer,Entry>> contentInBank;
	
	//should be a map to simulator banking
	private static MainMemory memory = new MainMemory();
	
	private int numOfbank = 8;

	public static MainMemory getInstance(){ 
		return memory;
	}
	
	private MainMemory() {
		PropertiesParser prop = PropertiesLoader.getPropertyInstance();
		numOfbank = prop.getIntProperty("sim.mem.numberofbank", 8);
		logger.debug("numOfBank :"+ numOfbank);
		
		contentInBank = new TreeMap<>();
		int bank=0;
		for (int i = 0; i < MEMORY_ADDRESS_LIMIT; i++) {
			bank = i % numOfbank;
			
			if (contentInBank.get(bank) == null) {
				Map<Integer,Entry>con = new TreeMap<>();
				con.put(i, new Entry(MemoryType.UNDEF,"0", i));
				contentInBank.put(bank, con);
			} else {
				contentInBank.get(bank).put(i, new Entry(MemoryType.UNDEF,"0", i));	
			}
		}
	}

	/**
	 * the memory content.
	 * the type to distinguish the memory type 
	 *
	 */
	protected static class Entry {
		private int address=0;
		private MemoryType type = MemoryType.INSTR;// instruction or data
		private String data = "";// in binary form
		private int size = 0; //data may use 13 bit as the size
		private Entry(MemoryType type, String data, int address, int size) {
			this.type =type;
			this.address = address;
			this.data = data;
			this.size = size;
		}
		private Entry(MemoryType type, String data, int address) {
			this(type, data, address,WORD_SIZE);
		}
		public void setType(MemoryType type){this.type = type;}
		public MemoryType getType() {return type;}
		public void setData(String data) {this.data = data;	}
		public String getData() {return data;}
		@Override public String toString() {return type +"," + data+"@"+address;}
		public int getAddress() {return address;}
		public void setAddress(int address) {this.address = address;}
		public void setSize(int size) {this.size = size;}
		public int getSize() {return size;}
		public boolean isData(){
			if (type == MemoryType.DATA) {
				return true;
			} return false;
		}
		public boolean isInstr(){
			if (type == MemoryType.INSTR) {
				return true;
			} return false;
		}
	}

	public java.util.Iterator<Entry> iterator(){
		return new ContentIterator();
	}
	
	private class ContentIterator implements Iterator<Entry>{
		private int currAddress =0;
		@Override
		public boolean hasNext() {
			return currAddress < MEMORY_ADDRESS_LIMIT;
		}

		@Override
		public Entry next() {
			
			int bank =0;
			do {
				bank = currAddress % numOfbank;
				Map<Integer, Entry> value = contentInBank.get(bank);
				Entry data = value.get(currAddress);

				++currAddress;
				if (!(data.type == MemoryType.UNDEF)) {
	    			return data;
	    		}
			} while (currAddress < MEMORY_ADDRESS_LIMIT);
			
			return null;
		}

		@Override
		public void remove() {}
	}
	
	public void setInstr(int address, String data) {
		int bank = address % numOfbank;
		
		if (contentInBank.get(bank) != null) {
			Entry d =contentInBank.get(bank).get(address);
			d.setData(data);
			d.setType(MemoryType.INSTR);
			logger.info("instr memory update, bank:" + bank +", address:"+ address);
			
			publishUpdate(address, data);
			
		} else{
			logger.error("invalid instr memory address " + address + " in bank " + bank);
		}
		
	}

	private void publishUpdate(int address, String data){
		HardwareData hardwareData = new HardwareData();
		hardwareData.put(HardwarePart.MEMORY.getName(),
				Integer.toString(address) + "," + data);

		this.notifyObservers(hardwareData);
		
		CPUController.shareInstance().checkSingleStepModel();
	}
	
	public void setData(int address, String data, int size) {
		int bank = address % numOfbank;
		
		if (contentInBank.get(bank) != null) {
			Entry d =contentInBank.get(bank).get(address);
			d.setData(data);
			d.setType(MemoryType.DATA);
			d.setSize(size);
			logger.info("data memory update, bank:" + bank +", address:"+ address);
			publishUpdate(address, data);
		} else{
			logger.error("invalid data memory address " + address + " in bank " + bank);
		}
	}
	public void setData(int address, String data) {
		setData(address, data, WORD_SIZE);
	}
	
	public Entry getData(int address){
		int bank = address % numOfbank;
		if (contentInBank.get(bank) != null) {
			Entry d =contentInBank.get(bank).get(address);
			logger.info("memory "+d.type+" retrieval, bank:" + bank +", address:"+ address);
			return d;
		}
		return null;
	}
	
	public void printMemory(){
		
		int bank =0;
		for (int i = 0; i < MEMORY_ADDRESS_LIMIT; i++) {
			bank = i % numOfbank;
			
			Map<Integer,Entry> value  = contentInBank.get(bank);
			Entry data = value.get(i);

    		if (!(data.type == MemoryType.UNDEF) ) {
    			logger.debug("In bank "+bank+" at "+ i + " " + data.type + " " + data.data);
    		}
			
		}
		
//        for (Map.Entry<Integer, Map<Integer,Entry>> entry : contentInBank.entrySet()) {
//        	bank = entry.getKey();
//        	Map<Integer,Entry> value  = entry.getValue();
//
//        	for(Map.Entry<Integer,Entry> e: value.entrySet()){
//        		int address = e.getKey();
//        		Entry data  =e.getValue();
//        		if (!(data.type == MemoryType.UNDEF) ) {
//        			logger.debug("In bank "+bank+" at "+ address + " " + data.type + " " + data.data);
//        		}
//        	}
//        }
	}
	
	
	@Deprecated
	private void loadFromFile(String fileName) {
		FileInputStream fis = null;
		BufferedReader br = null;
		try {
			File f = new File(fileName);
			fis = new FileInputStream(f);
			br = new BufferedReader(new FileReader(f));
			
			String line;
			int instrPos = 100, dataPos = 150;
			boolean currData = true;
			boolean dheader = false,iheader = false;
			while ((line = br.readLine()) != null) {
				
				if (line.startsWith(FILE_COMMENT) || "".equals(line.trim())) {
					continue;
				}
				
				int cmIdx = line.indexOf(FILE_COMMENT);
				if (cmIdx> 1) {
					line = line.substring(0, cmIdx);
				}
				line = line.trim();
				
				if (line.startsWith(FILE_DATA_HEAD)) {
					int pos = line.indexOf(":");
					if (pos >1) {
						dataPos = Integer.parseInt(line.substring(pos+1));
						logger.debug("save " + FILE_DATA_HEAD +" from " + dataPos);
					}
					currData = true;
					dheader = true;
				} else {
					dheader = false;
				}
				if (line.startsWith(FILE_INSTRUCTION_HEAD)) {
					int pos = line.indexOf(":");
					if (pos >1) {
						instrPos = Integer.parseInt(line.substring(pos+1));
						logger.debug("save " + FILE_INSTRUCTION_HEAD +" from " + instrPos);
					}
					currData = false;
					iheader = true;
				} else {
					iheader = false;
				}
			
				if (currData && !dheader) {
					setData(dataPos++, line);
				} else if (!currData && !iheader) {
					setInstr(instrPos++, line);
				}
			}
			
		} catch (IOException e) {
			logger.error("failed to read file ",e);
		} finally{
			try {
				fis.close();
				br.close();
			} catch (Exception e) { }
		}
	}
	
	public static void main(String[] args) {
		String fileName = "D:/java_stuff/githome/ComputerSimulator/src/main/edu/gwu/cs6461/logic/unit/program1.txt";
		fileName = "bin/program1.txt";
		MainMemory.getInstance().loadFromFile(fileName);
		
		Iterator<Entry> iter = MainMemory.getInstance().iterator();
		
		while (iter.hasNext()) {
			Entry string = iter.next();
			
			System.out.println(string);
			
		}
		
	}
}
