/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gwu.cs6461.logic;

import edu.gwu.cs6461.sim.bridge.Observer;

/**
 *
 * @author Ahmed
 */
public class RF {
    private Register R0 = new Register();
    private Register R1 = new Register();
    private Register R2 = new Register();
    private Register R3 = new Register();
    
    public RF(){
    	R0.setName("R0");
    	R1.setName("R1");
    	R2.setName("R2");
    	R3.setName("R3");
    }
    
    public void setRegisterObserver(Observer obs){
    	R0.register(obs);
    	R1.register(obs);
    	R2.register(obs);
    	R3.register(obs);
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
