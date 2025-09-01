package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ArrayList1 {
    public static void main(String[] args) {
        Scanner sc =  new Scanner(System.in);
        int n = sc.nextInt();
        ArrayList<String> products=  new ArrayList<>();
        for(int i =0;i<n;i++){
            products.add(sc.next());
        }
        System.out.println(products);
        Collections.sort(products);
        System.out.println(products);
        System.out.println(products.get(3));
    }
}
