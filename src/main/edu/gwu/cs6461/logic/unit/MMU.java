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
 * 
 * Cache write policy :  write-through caches with write-no-allocate policy 
 * 
 * @author marcoyeung
 *
 */
public class MMU {
	private static final Logger logger = Logger.getLogger(MMU.class);
	
	private static MMU instance = new MMU();
	private static MainMemory mainMem = MainMemory.getInstance();
	private static final CacheMapper cacheMap = SetAssociativeMapper.instance();

	private boolean enableCache = true;
	
	public static MMU instance() {
		return instance;
	}

	private MMU() {
		PropertiesParser prop = PropertiesLoader.getPropertyInstance();
		enableCache = prop.getBooleanProperty("sim.mem.cache.enabled", true);
	}
	
	
	/*****************************************************************/
	/*Main memory area
	/*****************************************************************/
	public void clearObserver() {
		mainMem.clear();
	}
	public void registerObserver(Observer srv) {
		mainMem.register(srv);
	}
	

	public void setData(int address, int data, int size) {
		String binary = Convertor.getSignedBinFromInt(data, 20);
		setData(address, binary, size);
	}

	private void setData(int address, String data, int size) {
		writeCache(MemoryType.DATA, address, data, size);
		mainMem.setData(address, data, size); //write through to main memory
	}

	public void setData(int address, int data) {
		setData(address, data, SimConstants.WORD_SIZE);
	}

	public void setData(int address, String data) {
		setData(address, data, SimConstants.WORD_SIZE);
	}

	public void setInstr(int address, String data) {
		writeCache(MemoryType.INSTR, address, data, SimConstants.WORD_SIZE);
		mainMem.setInstr(address, data); //write through to main memory
	}
	
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
	 * @return
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
	 * @return
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

	
	public void clean() {
		MainMemory.getInstance().init();
		Cache.getInstance().init();
	}


}
