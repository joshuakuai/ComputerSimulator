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
public class RF {
    private int R0=0;
    private int R1=0;
    private int R2=0;
    private int R3=0;
    /*
    ******get functions
    */
    public int getR0(){
        return R0;
    }
    public int getR1(){
        return R1;
    }
    public int getR2(){
        return R2;
    }
    public int getR3(){
        return R3;
    }
    /*
    ******set functions
    */
    public void setR0(int R){
        R0=R;
    }
    public void setR1(int R){
        R1=R;
    }
    public void setR2(int R){
        R2=R;
    }
    public void setR3(int R){
        R3=R;
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
