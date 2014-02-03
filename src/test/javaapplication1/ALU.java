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
public class ALU {
   
    private String Operation="";
    private int Result=0;
       
    public void Calculate(int Operand1, int Operand2, int Opcode, Register RES){
        if(Opcode==4 || Opcode==6)
            Operation="+";
        else if(Opcode==5 || Opcode==7)
            Operation="-";
        switch (Operation) {
            case "+":
                Result =Operand1+Operand2;
                break;
            case "-":
                Result =Operand1-Operand2;
                break; 
            case "*":
                Result =Operand1*Operand2;
                break;
            case "/":
                Result =Operand1/Operand2;
                break;
        }      
        RES.setData(Result);
    }
    
}
