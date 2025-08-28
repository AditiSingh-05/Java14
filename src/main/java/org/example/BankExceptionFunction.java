package org.example;

public class BankExceptionFunction {
    public BankExceptionFunction() {


    }

    public void validBalance(int balance) throws BankException{
        if(balance<5000){
            throw new BankException("Balance must be at least 5000");
        }else{
            System.out.println("Valid balance");
        }
    }

}
