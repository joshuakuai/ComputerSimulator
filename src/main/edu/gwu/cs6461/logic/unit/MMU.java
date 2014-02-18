package edu.gwu.cs6461.logic.unit;

import java.util.Map;
import java.util.TreeMap;

import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener;

import edu.gwu.cs6461.logic.unit.MainMemory.Entry;
import edu.gwu.cs6461.sim.bridge.Observer;
import edu.gwu.cs6461.sim.exception.MemoryException;
import edu.gwu.cs6461.sim.util.Convertor;

public class MMU {
	
	private static MMU instance = new MMU();
	
	private Map<Integer, Integer[]> memoryBlockView;
	public static MMU instance() {
		return instance;
	}

	private MMU() {
		MainMemory.getInstance();
		this.memoryBlockView = new TreeMap<Integer, Integer[]>();

	}
	
	/*****************************************************************/
	/*Main memory area
	/*****************************************************************/
	
	public void clearObserver() {
		MainMemory.getInstance().clear();
	}
	public void registerObserver(Observer srv) {
		MainMemory.getInstance().register(srv);
	}
	
	public void setInstr(int address, String data) {
		MainMemory.getInstance().setInstr(address, data);
	}
	
	public void setData(int address, int data, int size) {
		String binary = Convertor.getSignedBinFromInt(data, 20);
		MainMemory.getInstance().setData(address, binary,size);
	}

	public void setData(int address, String data, int size) {
		MainMemory.getInstance().setData(address, data,size);
	}
	public void setData(int address, int data) {
		String binary = Integer.toBinaryString(data);
		MainMemory.getInstance().setData(address, binary);
	}
	public void setData(int address, String data) {
		MainMemory.getInstance().setData(address, data);
	}
	
	/** 
	 * return a signed value from memory 
	 * 
	 * @param address
	 * @return
	 * @throws MemoryException 
	 */
	public Integer getDataFromMem(int address) throws MemoryException {
		Entry entry = MainMemory.getInstance().getData(address);
		if (entry != null && entry.isData()) {
			int size = entry.getSize();
			String da = entry.getData();
			int len = da.length();
			da = da.substring(len - size);
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
		Entry entry = MainMemory.getInstance().getData(address);
		if (entry != null && entry.isData()) {
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

	
	public static class MemoryAddresser {

	}
}
