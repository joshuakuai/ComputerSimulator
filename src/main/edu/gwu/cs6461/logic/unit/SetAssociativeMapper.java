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

public class SetAssociativeMapper implements CacheMapper {
	private static final Logger logger = Logger.getLogger(SetAssociativeMapper.class);
	private Map<Integer, List<Integer>> memoryBlockView;
	
	/**8192 -> 1024 block, the tagbitsize + setbitsize */
	private int blockSizeInBit = 10;
	/**block size */
	private int wordInBlock = 8;
	
	/**Main memory singleton instance */
	private static MainMemory mainMem = MainMemory.getInstance();
	private static Cache cache = Cache.getInstance();
	
	private static final SetAssociativeMapper instance = new SetAssociativeMapper();  
	public static SetAssociativeMapper instance() {
		System.out.println("Set associative Mapping is been using.");
		return instance;
	}
	
	private SetAssociativeMapper() {
		PropertiesParser prop = PropertiesLoader.getPropertyInstance();
		wordInBlock = prop.getIntProperty("sim.mem.cache.wordinblock", 8);
		
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

	@Override
	public boolean isDataInCache(int address) {
		MemoryAddress add = new MemoryAddress(address);
		if (cache.getData(add, false) != null) {
			return true;
		}
		return false;
	}

	@Override
	public void writeCache(int address, Entry entry) {
		MemoryAddress add = new MemoryAddress(address);
		pushBlockToCache(add, entry);
	}

	@Override
	public Entry getDataFromCache(int address) throws MemoryException {
		MemoryAddress add = new MemoryAddress(address);
		logger.debug("lookup data at "+address+" in cache set:"+add.setField+",tag:"+add.tagField+",word:"+add.wordField);
		Entry request = cache.getData(add);
		if (request == null) {
			logger.debug("cache miss set:"+add.setField+",tag:"+add.tagField+",word:"+add.wordField);
			logger.debug("Retrieve block from main memory.");
			request = mainMem.getData(address);
			pushBlockToCache(add, null);
		} else {
			logger.debug("cache hit set:"+add.setField+",tag:"+add.tagField+",word:"+add.wordField);
		}
		return request;
	}

	private void pushBlockToCache(MemoryAddress add, Entry tobeWrite) {
		
		List<Integer>block = getMemBlockByAddress(add.getAddress());//construct block of memory entry
		List<Entry> bkData = new ArrayList<>(); 
		for (Integer word : block) {
			Entry ee = mainMem.getData(word);
			
			//detach from mainmem instance
			Entry ec = new Entry(ee.getType(),ee.getData(),ee.getAddress(),ee.getSize());
			if (tobeWrite != null && add.wordField == word) {
				ec = new Entry(tobeWrite.getType(),tobeWrite.getData(),tobeWrite.getAddress(),tobeWrite.getSize());
			}
			bkData.add(ec);
		}
		cache.pushDataToCache(add, bkData);
	}
	
	private List<Integer> getMemBlock(int blockNum) {
		return memoryBlockView.get(blockNum);
	}
	
	private List<Integer> getMemBlockByAddress(int address) {
		String str = Convertor.getBinFromInt(address, MEMORY_ADDRESS_SPACE);
		str = str.substring(0, blockSizeInBit);
		int blockNum= address/wordInBlock;
		return getMemBlock(blockNum);
	}
	
	@Override
	public void printCacheStatus() {
		cache.printCacheStatus();
	}

	protected class MemoryAddress{
		private int address;
		private int tagField = 0;
		private int setField = 0;
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
		public int getTagField() {
			return tagField;
		}
		public void setTagField(int tagField) {
			this.tagField = tagField;
		}
		public int getSetField() {
			return setField;
		}
		public void setSetField(int setField) {
			this.setField = setField;
		}
		public int getWordField() {
			return wordField;
		}
		public void setWordField(int wordField) {
			this.wordField = wordField;
		}
		public int getAddress() {
			return address;
		}
		
	}
}
