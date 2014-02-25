package edu.gwu.cs6461.logic.unit;

import static edu.gwu.cs6461.sim.common.SimConstants.FILE_COMMENT;
import static edu.gwu.cs6461.sim.common.SimConstants.FILE_DATA_HEAD;
import static edu.gwu.cs6461.sim.common.SimConstants.FILE_INSTRUCTION_HEAD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.omg.CORBA.portable.InputStream;

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

	
	

	/**
	 * main memory address to cache address
	 * 
	 */
	public static class AddressTranslationUnit {
	}

	
	/**
	 * 
	 * Address translation between Cache and Main Memory
	 * 
	 * @author marcoyeung
	 *
	 */
	public class MemoryTranslator {
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
	
	
	public static void _main(String[] args) throws MemoryException {
		String fileName = "src/resources/edu/gwu/cs6461/logic/unit/program1.txt";
		fileName = "bin/program1.txt";
		MMU mmu = MMU.instance();
		mmu.loadFromFile(fileName);
		
		Entry e = cacheMap.getDataFromCache(7398);
		e = cacheMap.getDataFromCache(7392);
		e = cacheMap.getDataFromCache(7138);
		e = cacheMap.getDataFromCache(6882);
		e = cacheMap.getDataFromCache(6375);
		e = cacheMap.getDataFromCache(6368);
//		e = cacheMap.getDataFromCache(6624);
		e = cacheMap.getDataFromCache(8167);
		
		cacheMap.getDataFromCache(6375);
		
		cacheMap.printCacheStatus();
		
//		for (int i = 0; i < 8192; i++) {
//			Entry e = cacheMap.getDataFromCache(i);
//			logger.debug("data:"+e);
//			
//			String str = Convertor.getBinFromInt(i, 13);
//			String first5  = str.substring(0, 5);
//			String second5 = str.substring(5, 10);
//			String last3   = str.substring(10, 13);
//			logger.debug(i +":"+str +":"+ first5 + ":"+ second5 +":"+last3);
//		}
		
	}

}
