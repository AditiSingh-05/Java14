package org.example;

import java.util.Scanner;
//myexception2
public class UserDefinedException {
    public static void main(String[] args) throws InvalidAgeException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter age:");
        int age = sc.nextInt();
        if(age<18) {
            throw new InvalidAgeException("Age must be at least 18");
        }else{
            System.out.println("Valid age");
        }
    }
}
