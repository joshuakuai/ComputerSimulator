package edu.gwu.cs6461.logic.unit;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.logic.unit.MainMemory.Entry;
import edu.gwu.cs6461.sim.bridge.Observer;
import edu.gwu.cs6461.sim.common.MemoryType;
import edu.gwu.cs6461.sim.common.SimConstants;
import edu.gwu.cs6461.sim.exception.MemoryException;
import edu.gwu.cs6461.sim.util.Convertor;
import edu.gwu.cs6461.sim.util.PropertiesLoader;
import edu.gwu.cs6461.sim.util.PropertiesParser;


/**
 * Memory Management Unit (MMU)
 * <BR>
 * Main Memory and Cache controller class
 * <BR><BR>
 * 
 * This class holds the instances for Cache, Main Memory and Memory Mapper. It acts and the central control point for memory 
 * management in this simulator.
 * <BR>
 * <PRE>
 * Memory Mapping:  Set associative mapping is used
 * Main Memory :   Interleaved Memory is used
 * Cache :  one level of cache is used.
 * <BR>
 * Cache Replacement : Least Recently Used (LRU) is used.
 * Cache write policy :  write-through caches with write-no-allocate policy 
 * </PRE>
 * @author marcoyeung
 *
 */
public class MMU {
	
	/**logger to log message to log file*/
	private static final Logger logger = Logger.getLogger(MMU.class);
	
	/**The singleton object for MMU*/
	private static MMU instance = new MMU();
	/**singleton object for MainMemory*/
	private static MainMemory mainMem = MainMemory.getInstance();
	/**Singleton object for CacheMapper--Set associative mapper*/
	private static final CacheMapper cacheMap = SetAssociativeMapper.instance();

	/**True to enable cache mechanism; otherwise, data will not be put/read into/from cache*/
	private boolean enableCache = true;
	
	/**Return the singlteon instance for MMU*/
	public static MMU instance() {
		return instance;
	}

	/**Private constructor to control the number of MMU instance can be created*/
	private MMU() {
		PropertiesParser prop = PropertiesLoader.getPropertyInstance();
		enableCache = prop.getBooleanProperty("sim.mem.cache.enabled", true);
	}
	
	
	/*****************************************************************/
	/*Main memory area
	/*****************************************************************/
	/**purge any observer objects that are currently listening to memory update event*/
	public void clearObserver() {
		mainMem.clear();
	}
	/**Register new observer to listen to memory event */
	public void registerObserver(Observer srv) {
		mainMem.register(srv);
	}

	/**
	 * Set data into memory, if cache is enabled, the data will also be put/update into cache
	 * 
	 * @param address   Main memory address
	 * @param data      data in decimal form 
	 * @param size      size of the data in twos complement form
	 */
	public void setData(int address, int data, int size) {
		String binary = Convertor.getSignedBinFromInt(data, 20);
		setData(address, binary, size,"");
	}

	/**
	 * Set data into memory, if cache is enabled, the data will also be put/update into cache
	 * @param address   Main memory address
	 * @param data      data in String binary form
	 * @param size      size of the data in twos complement form
	 * @param comment   comment will be published to front-end GUI
	 */
	private void setData(int address, String data, int size, String comment) {
		writeCache(MemoryType.DATA, address, data, size);
		mainMem.setData(address, data, size, comment); //write through to main memory
	}

	/**
	 * Set data into memory, if cache is enabled, the data will also be put/update into cache
	 * 
	 * @param address   Main memory address
	 * @param data      data in decimal form 
	 */
	public void setData(int address, int data) {
		setData(address, data, SimConstants.WORD_SIZE);
	}

	/**
	 * Set data into memory, if cache is enabled, the data will also be put/update into cache
	 * 
	 * @param address   Main memory address
	 * @param data      data in String binary form
	 */
	public void setData(int address, String data) {
		setData(address, data, SimConstants.WORD_SIZE,"");
	}

	/**
	 * Set data into memory, if cache is enabled, the data will also be put/update into cache
	 * 
	 * @param address   Main memory address
	 * @param data      data in String binary form
	 * @param comment   comment will be published to front-end GUI
	 */
	public void setData(int address, String data, String comment) {
		setData(address, data, SimConstants.WORD_SIZE,comment);
	}

	/**
	 * Set the instruction to memory, , if cache is enabled, the instruction will also be put/update into cache
	 * @param address   Main memory address
	 * @param data      Instruction in String binary form
	 * @param comment   comment will be published to front-end GUI
	 */
	public void setInstr(int address, String data, String comment) {
		writeCache(MemoryType.INSTR, address, data, SimConstants.WORD_SIZE);
		mainMem.setInstr(address, data, comment); // write through to main memory
	}
	/**
	 * Set the instruction to memory, , if cache is enabled, the instruction will also be put/update into cache 
	 * @param address   Main memory address
	 * @param data      Instruction in String binary form
	 */
	public void setInstr(int address, String data) {
		setInstr(address, data, "");
	}
	
	/***
	 * Write the data into cache if cache is enabled.
	 * 
	 * @param type     Data type
	 * @param address   Main Memory address
	 * @param data     data in string binary form
	 * @param size     size of the data
	 */
	private void writeCache(MemoryType type,int address, String data, int size){
		
		if (enableCache) {
			boolean isInCache = false;
			isInCache = cacheMap.isDataInCache(address);
			if (isInCache) { //update cache: write-no-allocate policy
				logger.info("data in cache, update it at address: " + address);
				Entry en = new Entry(type, data, address,size);
				cacheMap.writeCache(address, en);
			}
		}
	}
	
	/** 
	 * return a signed value from memory 
	 * 
	 * @param address
	 * @return signed value from memory
	 * @throws MemoryException 
	 */
	public Integer getDataFromMem(int address) throws MemoryException {
		Entry entry;
		if (enableCache) {
			entry = cacheMap.getDataFromCache(address);
		} else {
			entry = mainMem.getData(address);
		}
		
		if (entry != null) {
			if (!entry.isData()) {
				logger.warn("instruction is retrieved for data use.");
			}
			int size = entry.getSize();
			String da = entry.getData();
			int len = da.length();
			
			int subStringStartPoint = len - size;
			if(subStringStartPoint < 0){
				subStringStartPoint = 0;
			}
			da = da.substring(subStringStartPoint);
			int val = Convertor.getSignedValFromBin(da, size);
			return val;
		}
		throw new MemoryException("Address not found in memory: " + address);
	}
	/**
	 * return the instruction in binary string
	 * 
	 * @param address
	 * @return    instruction in binary string
	 * @throws MemoryException 
	 */
	public String getInstrFromMem(int address) throws MemoryException{
		Entry entry;
		if (enableCache) {
			entry = cacheMap.getDataFromCache(address);
		} else {
			entry = mainMem.getData(address);
		}
		
		if (entry != null) {
			if (entry.isData()) {
				logger.warn("data is retrieved for data use.");
			}
			String da = entry.getData();
			return da;
		} 
		throw new MemoryException("Address not found in memory: " + address);
	}

	/***
	 * purge all the data in MainMemory and cache
	 */
	public void clean() {
		MainMemory.getInstance().init();
		Cache.getInstance().init();
	}


}
