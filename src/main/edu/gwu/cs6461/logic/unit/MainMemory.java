package edu.gwu.cs6461.logic.unit;

import static edu.gwu.cs6461.sim.common.SimConstants.MEMORY_ADDRESS_LIMIT;
import static edu.gwu.cs6461.sim.common.SimConstants.MEMORY_ADDRESS_SPACE;
import static edu.gwu.cs6461.sim.common.SimConstants.WORD_SIZE;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.logic.CPUController;
import edu.gwu.cs6461.sim.bridge.HardwareData;
import edu.gwu.cs6461.sim.bridge.Observable;
import edu.gwu.cs6461.sim.common.HardwarePart;
import edu.gwu.cs6461.sim.common.MemoryType;
import edu.gwu.cs6461.sim.util.Convertor;
import edu.gwu.cs6461.sim.util.PropertiesLoader;
import edu.gwu.cs6461.sim.util.PropertiesParser;


/**
 * 
 * MainMemory class to simulate the Main Memory in this simulator
 * <BR><BR>
 * By default, the Main Memory is divided into 8 banks, but it can be configurated through
 * simulator property file. 
 * 
 * <Br><BR>
 * TreeMap is used to store the Data in Main Memory so that memory data are stored in order.
 * 
 * <BR><BR>
 * Nested class MainMemory.Entry is used as the data structure for every data stored in memory.
 * This is to store data in a well organized manner. For instance, the type of data, the size of the data, and the 
 * address of the data can be easily access.
 * 
 * 
 * 
 * @author marcoyeung
 *
 */
public class MainMemory extends Observable{
	
	/**logger to log message to log file */
	private static final Logger logger = Logger.getLogger(MainMemory.class);
	
	/**Data structure, Map, to store data in Main Memory */
	private Map<Integer,Map<Integer,Entry>> contentInBank;
	
	/**Singleton object for Main memory.  The only instance of in this simulator */
	//should be a map to simulator banking
	private static MainMemory memory = new MainMemory();
	
	/**number of bank, default is 8. The value could be set in simulator property  */
	private int numOfbank = 8;
	
	/** method to return the singleton Main Memory object.  The only way to get the 
	 singleton Main Memory object. */
	public static MainMemory getInstance(){ 
		return memory;
	}
	
	/** Constructor is private so that the outside client cannot create a separated 
	 * instance for Main Memory */
	private MainMemory() {
		PropertiesParser prop = PropertiesLoader.getPropertyInstance();
		numOfbank = prop.getIntProperty("sim.mem.numberofbank", 8);
		logger.debug("numOfBank in main memory :"+ numOfbank);
		
		init();
	}
	
	/**
	 * Build the memory or clean it up 
	 */
	public void init(){
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
	 * The nested class to represent the data in Main Memory cell.
	 * <BR><BR>
	 * Type is used to distinguish the type of the data
	 * 	<BR><BR><PRe>
	 *    INSTR for instruction data
	 *    DATA for the value data
	 *    size for actual data size, e.g.: if the data is index register, the size could be smaller than word size
	 *    </PRe> 
	 *  <BR>
	 */
	protected static class Entry {
		private int address=0;
		
		/** UNDEF is the memory cell is not in use; INSTR for instruction, DATA for value data  */
		private MemoryType type = MemoryType.INSTR;// instruction or data
		private String data = "";// in binary form
		private int size = WORD_SIZE; //data may use 13 bit as the size
		protected Entry(MemoryType type, String data, int address, int size) {
			this.type =type;
			this.address = address;
			this.data = data;
			this.size = size;
		}
		protected Entry(MemoryType type, String data, int address) {
			this(type, data, address, WORD_SIZE);
		}
		
		//The following are the getters and setters for this nested class
		protected Entry(){}
		public void setType(MemoryType type){this.type = type;}
		public MemoryType getType() {return type;}
		public void setData(String data) {this.data = data;	}
		public String getData() {return data;}
		@Override public String toString() {return type +"," + data+"@"+address;}
		public int getAddress() {return address;}
		public void setAddress(int address) {this.address = address;}
		public void setSize(int size) {this.size = size;}
		public int getSize() {return size;}
		
		/** true if the Entry is data */
		public boolean isData(){
			if (type == MemoryType.DATA) { return true;
			} return false;
		}
		/** true if the Entry is instruction */
		public boolean isInstr(){
			if (type == MemoryType.INSTR) {return true;
			} return false;
		}
	}

	/** return a iterator so that the caller can use to loop through the memory content one by one
	 * <BR> Only the memory cell has data, ie the type is not UNDEF ,will be returned.
	 *  */
	public java.util.Iterator<Entry> iterator(){
		return new ContentIterator();
	}
	
	/** inner class for the memory content iterator */
	private class ContentIterator implements Iterator<Entry>{
		private int currAddress =0;
		@Override
		/**return true if there is still Memory entry that is not been gone through.*/
		public boolean hasNext() {
			return currAddress < MEMORY_ADDRESS_LIMIT;
		}

		@Override
		/**return the next Memory entry*/
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
		/**remove is not implemented*/
		public void remove() {}
	}
	
	/***
	 * Set instruction data into memory.<BR>
	 * By default, the size is the word size. e.g. 20 bits <BR>
	 * the update will be published to observer if there is any
	 *
	 * @param address      memory address in int
	 * @param data         the data in binary string form
	 * @param comment      the comment which will be displayed in front-end GUI
	 */
	public void setInstr(int address, String data, String comment) {
		int bank = address % numOfbank;
		
		if (contentInBank.get(bank) != null) {
			Entry d =contentInBank.get(bank).get(address);
			d.setData(data);
			d.setType(MemoryType.INSTR);
			logger.info("instruction memory updated, bank:" + bank +", address:"+ address);
			
			/**publish the update event to observer*/
			publishUpdate(address, data,comment);
			
		} else{
			logger.error("invalid instruction memory address " + address + " in bank " + bank);
		}
		
	}

	/**
	 * Set instrcution data into memory.<BR>
	 * An overloaded method.  this method do not set comment to front-end GUI
	 * @see setInstr(int address, String data, String comment)
	 */
	public void setInstr(int address, String data) {
		setInstr(address, data,"");
	}

	/***
	 * Construct and send the memory update to observer, if there is any.
	 * <BR>
	 * For instance, GUI is one of the observers in this simulator.  It listens to the memory update 
	 * event so that memory data could be displayed on the front-end control.
	 */
	private void publishUpdate(int address, String data, String comment){
		HardwareData hardwareData = new HardwareData();
		if (comment!=null && !"".equals(comment.trim())) {
			hardwareData.put(HardwarePart.MEMORY.getName(),
					Integer.toString(address) + "," + data +","+comment);
		} else {
			hardwareData.put(HardwarePart.MEMORY.getName(),
					Integer.toString(address) + "," + data);
		}

		this.notifyObservers(hardwareData);
		
		/**Test if single Step mode is on. If yes, the simulator will pause here.*/
		CPUController.shareInstance().checkSingleStepModel();
	}

	/**
	 * Set the value data into main memory. <BR>
	 * 
	 * @param address   address in main memory
	 * @param data      data in binary string form. mostly in two's complement form.
	 * @param size      the size of the data so that correct data in decimal form could be calculated.
	 * @param comment   the comment string that will be sent to registered observers, if there is any.
	 */
	public void setData(int address, String data, int size, String comment) {
		int bank = address % numOfbank;
		
		if (contentInBank.get(bank) != null) {
			Entry d =contentInBank.get(bank).get(address);
			d.setData(data);
			d.setType(MemoryType.DATA);
			d.setSize(size);
			logger.info("data memory update, bank:" + bank +", address:"+ address);
			publishUpdate(address, data,comment);
		} else{
			logger.error("invalid data memory address " + address + " in bank " + bank);
		}
	}
	
	/**
	 * @see  setData(int address, String data, int size, String comment)
	 * 
	 * @param address   address in main memory
	 * @param data      data in binary string form. mostly in two's complement form.
	 * @param size      the size of the data so that correct data in decimal form could be calculated.
	 */
	public void setData(int address, String data, int size) {
		setData(address, data, size, "");
	}

	/**
	 * @see  setData(int address, String data, int size, String comment)
	 * 
	 * @param address   address in main memory
	 * @param data      data in binary string form. mostly in two's complement form.
	 */
	public void setData(int address, String data) {
		setData(address, data, WORD_SIZE);
	}
	
	/**
	 * Return the main memory cell data as MainMemory.Entry
	 *  
	 * @param address   address in main memory
	 * @return   MainMemory.Entry will be returned; otherwise, null will be returned.
	 */
	public Entry getData(int address){
		int bank = address % numOfbank;
		if (contentInBank.get(bank) != null) {
			Entry d =contentInBank.get(bank).get(address);
			logger.debug("memory "+d.type+" retrieval, bank:" + bank +", address:"+ address);
			return d;
		}
		return null;
	}
	
	/**
	 * Output main memory content into logfile for information purpose.
	 * <BR>
	 * Only the memory cell that is defined will be printed.
	 */
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
	

/*	private Address translateAddress(int address){
		return new Address(address);
	}
	private static class Address{
		private int msb=0;
		private int lsb=0;
		private Address(int address) {
			String str = Convertor.getBinFromInt(address, MEMORY_ADDRESS_SPACE);
			
			String part1 = str.substring(0, 10);
			String part2 = str.substring(10, 13);
			
			msb = Integer.parseInt(part1);
			lsb = Integer.parseInt(part2);
		}
		public int getMsb() {
			return msb;
		}
		public int getLsb() {
			return lsb;
		}
		
	}*/
	
}
