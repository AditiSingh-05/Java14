package org.example;

import java.util.Scanner;

public class Array1 {
    public static void main(String[] args) {
        int[] arr = new int[6];
        Scanner sc = new Scanner(System.in);
        for(int i=0; i<6; i++){
            arr[i] = sc.nextInt();
        }
        for(int i=5; i>=0; i--){
            System.out.print(arr[i] + " ");
        }
    }
}
