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
public class XF {
    private Register X1 = new Register();
    private Register X2 = new Register();
    private Register X3 = new Register();
    
    public XF(){
    	X1.setName("X1");
    	X1.setName("X2");
    	X1.setName("X3");
    }
    
    public void setRegisterObserver(Observer obs){
    	X1.register(obs);
    	X2.register(obs);
    	X3.register(obs);
    }
    
    public int getX1(){
        return X1.getData();
    }
    public int getX2(){
        return X2.getData();
    }
    public int getX3(){
        return X3.getData();
    }
    /*
    *****set functions
    */
    public void setX1(int X){
        X1.setData(X);
    }
    public void setX2(int X){
        X2.setData(X);
    }
    public void setX3(int X){
        X3.setData(X);
    }
    
    //***********************************//
    public void setSwitch(int XFI,int Data){      
        if (XFI==1)
            setX1(Data);
        else if (XFI==2)
            setX2(Data);
        else if (XFI==3)
            setX3(Data);
    }
    public int getSwitch(int XFI){      
        if (XFI==1)
           return getX1();
        else if (XFI==2)
           return getX2();
        else if (XFI==3)
           return getX3();
        return -1;
    }
}
