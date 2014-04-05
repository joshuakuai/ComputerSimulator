package edu.gwu.cs6461.sim.common;

public enum MachineStatus {
	Normal(0), TrapInstruction(1), MachineFault(2), Unknown(-1);

	private final int statusCode;

	private MachineStatus(int statusCode) {
		this.statusCode = statusCode;
	}

	public static MachineStatus fromCode(int code) {

		for (MachineStatus value : MachineStatus.values()) {
			if (value.statusCode == code) {
				return value;
			}
		}
		return Unknown;

	}
	public int getCode(){
		return statusCode;
	}

}
