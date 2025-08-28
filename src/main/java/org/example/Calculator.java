package org.example;

import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        System.out.println("Calculator is running...");
        int a = 5;
        Scanner scanner = new Scanner(System.in);
        int b = scanner.nextInt();
        int c = a/b;
        try {
            int d = a/b;
        } catch (ArithmeticException e) {
            System.out.println("Error: Division by zero");
        }finally{
            System.out.println("Finally block executed");
        }
    }
}
