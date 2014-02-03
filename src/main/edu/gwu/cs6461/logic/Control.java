/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gwu.cs6461.logic;

/**
 * 
 * @author Ahmed
 */
public class Control {
	private Register MAR = new Register();
	private Register MDR = new Register();
	private Register RES = new Register();

	// /TODO (PART2) Fetch IR from memory
	public void FetchIR(int PC, IR IRobject) {

	}

	public void Decode(IR IRobject) {
		Decode dec = new Decode();
		dec.decodeSwitch(IRobject);
	}

	public void RunInstruction(IR IRobject, RF RFtable, XF XFtable, Memory Mem,
			ALU ALU) {
		if (IRobject.getOpCode() == 1)
			LDR(IRobject, RFtable, XFtable, Mem);
		else if (IRobject.getOpCode() == 4)
			AMR(IRobject, RFtable, XFtable, Mem, ALU);
	}

	public void LDR(IR IRobject, RF RFtable, XF XFtable, Memory Mem) {
		// TODO (part 2) create an adder class with over flow detection
		// ************ for effective address calcution
		if (IRobject.getXFI() != 0) {
			if (IRobject.getXFI() == 1) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			} else if (IRobject.getXFI() == 2) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			} else if (IRobject.getXFI() == 3) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			}
		} else
			MAR.setData(IRobject.getAddress());
		MDR.setData(Mem.getMem(MAR.getData()));
		if (IRobject.getIndirect() == 1) {
			MAR.setData(MDR.getData());
			MDR.setData(Mem.getMem(MAR.getData()));
		}
		RFtable.setSwitch(IRobject.getRFI1(), MDR.getData());
	}

	public void AMR(IR IRobject, RF RFtable, XF XFtable, Memory Mem, ALU ALU) {
		if (IRobject.getXFI() != 0) {
			if (IRobject.getXFI() == 1) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			} else if (IRobject.getXFI() == 2) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			} else if (IRobject.getXFI() == 3) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			}
		} else
			MAR.setData(IRobject.getAddress());
		// ////
		MDR.setData(Mem.getMem(MAR.getData()));
		// ////
		if (IRobject.getIndirect() == 1) {
			MAR.setData(MDR.getData());
			MDR.setData(Mem.getMem(MAR.getData()));
		}
		// ////
		System.out.println(RES.getData());
		ALU.Calculate(RFtable.getSwitch(IRobject.getRFI1()), MDR.getData(),
				IRobject.getOpCode(), RES);
		RFtable.setSwitch(IRobject.getRFI1(), RES.getData());
		System.out.println(RES.getData());
	}

	// /WOrk in progess
	// ***********************$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
	public void STR(IR IRobject, RF RFtable, XF XFtable, Memory Mem) {
		// TODO (part 2) create an adder class with over flow detection
		// ************ for effective address calcution
		if (IRobject.getXFI() != 0) {
			if (IRobject.getXFI() == 1) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			} else if (IRobject.getXFI() == 2) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			} else if (IRobject.getXFI() == 3) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			}
		} else
			MAR.setData(IRobject.getAddress());

		if (IRobject.getIndirect() == 0) {
			Mem.setMem(MAR.getData(), RFtable.getSwitch(IRobject.getRFI1()));
		} else if (IRobject.getIndirect() == 1) {
			// MAR.setData(MDR.getData());
			MDR.setData(Mem.getMem(MAR.getData()));
			Mem.setMem(MDR.getData(), RFtable.getSwitch(IRobject.getRFI1()));
		}
	}

	// ////
	public void SMR(IR IRobject, RF RFtable, XF XFtable, Memory Mem, ALU ALU) {
		if (IRobject.getXFI() != 0) {
			if (IRobject.getXFI() == 1) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			} else if (IRobject.getXFI() == 2) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			} else if (IRobject.getXFI() == 3) {
				RES.setData(IRobject.getAddress()
						+ XFtable.getSwitch(IRobject.getXFI()));
				MAR.setData(RES.getData());
			}
		} else
			MAR.setData(IRobject.getAddress());
		// ////
		MDR.setData(Mem.getMem(MAR.getData()));
		// ////
		if (IRobject.getIndirect() == 1) {
			MAR.setData(MDR.getData());
			MDR.setData(Mem.getMem(MAR.getData()));
		}
		// ////
		System.out.println(RES.getData());
		ALU.Calculate(RFtable.getSwitch(IRobject.getRFI1()), MDR.getData(),
				IRobject.getOpCode(), RES);
		RFtable.setSwitch(IRobject.getRFI1(), RES.getData());
		System.out.println(RES.getData());
	}

	// //////
	public void AIR(IR IRobject, RF RFtable, ALU ALU) {
		// if immed is zero no need to change register but the value is
		// reinserted into
		// the register because the single step mode only works when the set
		// functions are called
		if (IRobject.getImmed() == 0) {
			RFtable.setSwitch(IRobject.getRFI1(),
					RFtable.getSwitch(IRobject.getRFI1()));
		}
		// if the General Register is empty than just copy and paste the immed
		// into the register
		if (RFtable.getSwitch(IRobject.getRFI1()) == 0) {
			RFtable.setSwitch(IRobject.getRFI1(), IRobject.getImmed());
		}
		// else add the register to the immed and put back the value in the
		// register
		else {
			ALU.Calculate(RFtable.getSwitch(IRobject.getRFI1()),
					IRobject.getImmed(), IRobject.getOpCode(), RES);
			RFtable.setSwitch(IRobject.getRFI1(), RES.getData());
		}
	}

	// /
	public void SIR(IR IRobject, RF RFtable, ALU ALU) {
		// if immed is zero no need to change register but the value is
		// reinserted into
		// the register because the single step mode only works when the set
		// functions are called
		if (IRobject.getImmed() == 0) {
			RFtable.setSwitch(IRobject.getRFI1(),
					RFtable.getSwitch(IRobject.getRFI1()));
		}
		// if the General Register is empty than just copy and paste the immed
		// into the register
		if (RFtable.getSwitch(IRobject.getRFI1()) == 0) {
			RFtable.setSwitch(IRobject.getRFI1(), IRobject.getImmed());
		}
		// else add the register to the immed and put back the value in the
		// register
		else {
			ALU.Calculate(RFtable.getSwitch(IRobject.getRFI1()),
					IRobject.getImmed(), IRobject.getOpCode(), RES);
			RFtable.setSwitch(IRobject.getRFI1(), RES.getData());
		}
	}

	// /////
	public void LDA(IR IRobject, RF RFtable, Memory Mem) {
		MAR.setData(IRobject.getAddress());
		if (IRobject.getIndirect() == 0) {
			RFtable.setSwitch(IRobject.getRFI1(), MAR.getData());
		}
		if (IRobject.getIndirect() == 1) {
			MDR.setData(Mem.getMem(MAR.getData()));
			RFtable.setSwitch(IRobject.getRFI1(), MDR.getData());
		}
	}

	// /////
	public void LDX(IR IRobject, XF XFtable, Memory Mem) {
		MAR.setData(IRobject.getAddress());
		MDR.setData(Mem.getMem(MAR.getData()));
		if (IRobject.getIndirect() == 1) {
			MAR.setData(MDR.getData());
			MDR.setData(Mem.getMem(MAR.getData()));
		}
		XFtable.setSwitch(IRobject.getXFI(), MDR.getData());
	}

	// /////
	public void STX(IR IRobject, XF XFtable, Memory Mem) {
		if (IRobject.getIndirect() == 0) {
			MAR.setData(IRobject.getAddress());
			Mem.setMem(MAR.getData(), XFtable.getSwitch(IRobject.getXFI()));
		} else if (IRobject.getIndirect() == 1) {
			MDR.setData(Mem.getMem(MAR.getData()));
			Mem.setMem(MDR.getData(), XFtable.getSwitch(IRobject.getXFI()));
		}
	}
}
