/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gwu.cs6461.logic;

import edu.gwu.cs6461.sim.bridge.Observer;
import edu.gwu.cs6461.sim.common.HardwarePart;

/**
 *Register File table class
 *creates all the General registers and updates them
 * @Revised   Jan 20, 2014 - 11:24:39 AM  
 */
public class RF {
	/**create general register with signed value enabled*/
    private Register R0 = new Register(HardwarePart.R0.getBit(),HardwarePart.R0.getName(), true);
    /**create general register with signed value enabled*/
    private Register R1 = new Register(HardwarePart.R1.getBit(),HardwarePart.R1.getName(),true);
    /**create general register with signed value enabled*/
    private Register R2 = new Register(HardwarePart.R2.getBit(),HardwarePart.R2.getName(),true);
    /**create general register with signed value enabled*/
    private Register R3 = new Register(HardwarePart.R3.getBit(),HardwarePart.R3.getName(),true);
    
    public RF(){
    }
    /**updates values to GUI */
    public void setRegisterObserver(Observer obs){
    	R0.register(obs);
    	R1.register(obs);
    	R2.register(obs);
    	R3.register(obs);
    }
    public void clearObserver(){
    	R0.clear();
    	R1.clear();
    	R2.clear();
    	R3.clear();
    }
    /*
    ******get functions
    */
    public int getR0(){
        return R0.getData();
    }
    public int getR1(){
        return R1.getData();
    }
    public int getR2(){
        return R2.getData();
    }
    public int getR3(){
        return R3.getData();
    }
    /*
    ******set functions
    */
    public void setR0(int R){
        R0.setData(R);
    }
    public void setR1(int R){
        R1.setData(R);
    }
    public void setR2(int R){
        R2.setData(R);
    }
    public void setR3(int R){
        R3.setData(R);
    }
    /** takes the index (RFI) of the register and puts the passed value(Data) into it */
    public void setSwitch(int RFI,int Data){
        if (RFI==0)
            setR0(Data);
        else if (RFI==1)
            setR1(Data);
        else if (RFI==2)
            setR2(Data);
        else if (RFI==3)
            setR3(Data);
    }
    /**takes the index (RFI) and returns the value of that register */
    public int getSwitch(int RFI){
        if (RFI==0)
           return getR0();
        else if (RFI==1)
           return getR1();
        else if (RFI==2)
           return getR2();
        else if (RFI==3)
           return getR3();
        return -1;
    }
}
