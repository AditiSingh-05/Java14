package org.example;

import java.util.Scanner;
//customexception2
public class UserDefinedException2 {
    public void validate(int age) throws UDF {
        if(age<18) {
            throw new UDF("Age must be at least 18");
        }else{
            System.out.println("Valid age");
        }
    }
}
