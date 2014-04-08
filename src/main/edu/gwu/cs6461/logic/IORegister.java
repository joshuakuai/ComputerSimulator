package edu.gwu.cs6461.logic;

/**
 * Internal register for IO devices
 * <BR>
 * It holds one character each time and wait for consume by CPU or Device 
 * @Revised   Jan 20, 2014 - 11:24:39 AM
 * */
public class IORegister extends Register {

	/**flag to indicate ifs data is available */
	private boolean isDataAvailable = false;
	
	/**Constructor to create with specific size and name*/
	public IORegister(int size, String name) {
		this(size,name, false);
	}
	/**Constructor to create with specific size and name, 
	 * and indicate the signed or unsigned
	 * */
	public IORegister(int size, String name, boolean signed) {
		super(size, name, signed);
	}

	public boolean isDataAvailable() {
		return isDataAvailable;
	}

	public void setDataAvailable(boolean isDataAvailable) {
		this.isDataAvailable = isDataAvailable;
	}
	@Override
	public int getData() {
		return super.getData();
	}
	@Override
	public void setData(int newData) {
		super.setData(newData);
	}
	
	@Override
	public String toString() {
		String d = super.toString(); 
		return "data:"+isDataAvailable+"," + d;
	}
}
