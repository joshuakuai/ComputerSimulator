package edu.gwu.cs6461.sim.mem;

public class SimpleMemory implements Memory<String> {

	private static final int MEM_SIZE = 2048;
	private String[] data;

	public SimpleMemory() {
		data = new String[MEM_SIZE];
		for (int i = 0; i < data.length; i++) {
			data[i] = "0";
		}
	}

	@Override
	public String get(int add) {
		if (!isValidAdd(add)) {
			return "-1";
		}
		return data[add];

	}

	@Override
	public void set(int add, String val) {
		if (isValidAdd(add)) {
			data[add] = val;
		}

	}

	private boolean isValidAdd(int add) {
		if (add >= 0 || add <= MEM_SIZE) {
			return true;
		}
		return false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleMemory sm = new SimpleMemory();
		sm.set(5, "afsl");

		System.out.println(sm);

	}


}
