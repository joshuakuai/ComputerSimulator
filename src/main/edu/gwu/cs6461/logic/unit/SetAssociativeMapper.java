/**
 * Has all the logic for Memory
 */
package edu.gwu.cs6461.logic.unit;

import static edu.gwu.cs6461.sim.common.SimConstants.MEMORY_ADDRESS_LIMIT;
import static edu.gwu.cs6461.sim.common.SimConstants.MEMORY_ADDRESS_SPACE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.logic.unit.MainMemory.Entry;
import edu.gwu.cs6461.sim.exception.MemoryException;
import edu.gwu.cs6461.sim.util.Convertor;
import edu.gwu.cs6461.sim.util.PropertiesLoader;
import edu.gwu.cs6461.sim.util.PropertiesParser;


/**
 * 
 * Set Associative Mapper implementation.
 * <BR><BR>
 * Please refer to Cache design document.
 * 
 * @author marcoyeung
 *
 */
public class SetAssociativeMapper implements CacheMapper {
	
	/** Logger to log the message to log file	 */
	private static final Logger logger = Logger.getLogger(SetAssociativeMapper.class);
	
	/**
	 * This holds the memory address in block view. The actually data in Main memory are not touched. 
	 */
	private Map<Integer, List<Integer>> memoryBlockView;
	
	/**Total number of block in this simulator in bit form
	 * 8192 of memory is put into 1024 block, which use 10 bits to represent 
	 * This is used to parse the memory address so that correct block for a memory address could be located. */
	private int blockSizeInBit = 10;
	
	/**block size: number of word in a block */
	private int wordInBlock = 8;
	
	/**Main memory singleton instance */
	private static MainMemory mainMem = MainMemory.getInstance();
	
	/**Cache singleton instance */
	private static Cache cache = Cache.getInstance();
	
	/**Singleton object for this set associative mapper class	 */
	private static final SetAssociativeMapper instance = new SetAssociativeMapper();
	
	/**Return the singleton set associative class object to the caller*/
	public static SetAssociativeMapper instance() {
		System.out.println("Set associative Mapping is been using.");
		return instance;
	}
	
	/**
	 * Private constructor to prevent others from creating instance for this class: only one instance is allowed to create
	 */
	private SetAssociativeMapper() {
		PropertiesParser prop = PropertiesLoader.getPropertyInstance();
		wordInBlock = prop.getIntProperty("sim.mem.cache.wordinblock", 8);
		
		//TreeMap as the data structure to memory block view so that memory address are in order 
		this.memoryBlockView = new TreeMap<Integer, List<Integer>>(); //for ordering
		int blockCnt = -1;
		for (int i = 0; i < MEMORY_ADDRESS_LIMIT; i++) {
			if (i % 8 == 0)
				blockCnt += 1;

			if (memoryBlockView.get(blockCnt) == null) {
				List<Integer> m = new ArrayList<Integer>();
				memoryBlockView.put(blockCnt, m);
			}
			List<Integer> mem = memoryBlockView.get(blockCnt);
			mem.add(i);
		}
		logger.debug("total block: " + blockCnt);
	}

	/**
	 * @see CacheMapper.isDataInCache(int address)
	 */
	@Override
	public boolean isDataInCache(int address) {
		MemoryAddress add = new MemoryAddress(address);
		if (cache.getData(add, false) != null) {
			return true;
		}
		return false;
	}

	/**
	 * @see  CacheMapper.writeCache(int address, Entry entry)
	 */
	@Override
	public void writeCache(int address, Entry entry) {
		MemoryAddress add = new MemoryAddress(address);
		//TODO only need to update the individual cache entry
		pushBlockToCache(add, entry);
	}

	/**
	 * @see  #CacheMapper.getDataFromCache(int address)
	 */
	@Override
	public Entry getDataFromCache(int address) throws MemoryException {
		MemoryAddress add = new MemoryAddress(address);
		logger.debug("lookup data at address "+address+" in cache set:"+add.setField+",tag:"+add.tagField+",word:"+add.wordField);
		Entry request = cache.getData(add);
		if (request == null) {
			logger.debug("cache miss set:"+add.setField+",tag:"+add.tagField+",word:"+add.wordField);
			logger.debug("Load up from main memory.");
			request = mainMem.getData(address);
			logger.debug("Retrieve the whole block from main memory.");
			pushBlockToCache(add, null);
		} else {
			logger.debug("cache hit set:"+add.setField+",tag:"+add.tagField+",word:"+add.wordField);
		}
		return request;
	}

	/***
	 * Construct and send the memory update to observer, if there is any.
	 * <BR>
	 * For instance, GUI is one of the observers in this simulator.  It listens to the memory update 
	 * event so that memory data could be displayed on the front-end control.
	 */
	private void pushBlockToCache(MemoryAddress add, Entry tobeWrite) {
		
		List<Integer>block = getMemBlockByAddress(add.getAddress());//construct block of memory entry
		List<Entry> bkData = new ArrayList<>(); 
		for (Integer b : block) {
			Entry ee = mainMem.getData(b);
			
			//detach from mainmem instance
			Entry ec = new Entry(ee.getType(),ee.getData(),ee.getAddress(),ee.getSize());
			/*if (tobeWrite != null && add.wordField == word) {*/
			if (tobeWrite != null && add.wordField == (b%wordInBlock)) {
				ec = new Entry(tobeWrite.getType(),tobeWrite.getData(),tobeWrite.getAddress(),tobeWrite.getSize());
			}
			bkData.add(ec);
		}
		cache.pushDataToCache(add, bkData);
	}
	
	/**
	 * Return a list of memory addresses that are in the same block
	 * 
	 * @param blockNum
	 * @return  list of memory addresses that are in the same block
	 */
	private List<Integer> getMemBlock(int blockNum) {
		return memoryBlockView.get(blockNum);
	}
	
	/**
	 * Parse the memory address so that a list of memory addresses that are in the same block could be return.
	 * 
	 * @param address  Main memory address
	 * @return   list of memory addresses that are in the same block
	 */
	private List<Integer> getMemBlockByAddress(int address) {
		String str = Convertor.getBinFromInt(address, MEMORY_ADDRESS_SPACE);
		str = str.substring(0, blockSizeInBit);
		int blockNum= address/wordInBlock;
		return getMemBlock(blockNum);
	}
	
	/**
	 * Print the cache content for information purpose
	 */
	@Override
	public void printCacheStatus() {
		cache.printCacheStatus();
	}

	/**
	 * Inner class to parse the Main memory address into Set associative form
	 * 
	 * @author marcoyeung
	 */
	protected class MemoryAddress{
		/** main memory address		 */
		private int address;

		/**tag portion after parse the main memory address  */
		private int tagField = 0;
		/**set portion after parse the main memory address  */
		private int setField = 0;
		/**word portion after parse the main memory address  */
		private int wordField = 0;
		
		private MemoryAddress(int address) {
			String str = Convertor.getBinFromInt(address, MEMORY_ADDRESS_SPACE);
			
			String first5  = str.substring(0, 5);
			String second5 = str.substring(5, 10);
			String last3   = str.substring(10, 13);

			this.address = address;
			this.tagField = Integer.parseInt(first5,2);
			this.setField = Integer.parseInt(second5,2);
			this.wordField = Integer.parseInt(last3,2);
		}
		
		/**
		 * Return the tag field in decimal form. Used to locate the line in cache
		 * @return  tag value
		 */
		public int getTagField() {
			return tagField;
		}
		/**
		 * Return the Set field in decimal form. Used to locate the Set in cache
		 * @return  set value
		 */
		public int getSetField() {
			return setField;
		}

		/**
		 * Return the word field in decimal form. Used to locate the word in a line in cache
		 * @return   word value
		 */
		public int getWordField() {
			return wordField;
		}
		
		/**
		 * returns the main memory address.
		 * @return   main memory address
		 */
		public int getAddress() {
			return address;
		}
		
	}
}
