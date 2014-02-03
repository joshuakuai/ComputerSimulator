/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;

/**
 *
 * @author Ahmed
 */
public class XF {
    private int X1=0;
    private int X2=0;
    private int X3=0;
    
    public int getX1(){
        return X1;
    }
    public int getX2(){
        return X2;
    }
    public int getX3(){
        return X3;
    }
    /*
    *****set functions
    */
    public void setX1(int X){
        X1=X;
    }
    public void setX2(int X){
        X2=X;
    }
    public void setX3(int X){
        X3=X;
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
