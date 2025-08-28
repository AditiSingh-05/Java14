package org.example;

public class MainBankException {
    public static void main(String[] args) throws BankException {
        BankExceptionFunction bankException = new BankExceptionFunction();
        try{
            bankException.validBalance(9000);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
