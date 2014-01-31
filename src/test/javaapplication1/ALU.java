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
    private int Operand1=0;
    private int Operand2=0;
    private String Operation="";
    private int Result=0;
    
    public void setOperand1(String Operand){
        Operand1 = Integer.parseInt(Operand);
    }
    public void setOperand2(String Operand){
        Operand2 = Integer.parseInt(Operand);
    }
    public void setOperation(String OperationNew){
        Operation = OperationNew;
    }
    public String Calculate(){
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
        
        return Integer.toString(Result);
    }
    
}
