package org.example;
import java.util.HashSet;
import java.util.Scanner;

public class HashMap1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n  = sc.nextInt();
        HashSet<String> set = new HashSet<>();
        for(int i=0; i<n; i++){
            String product = sc.next();
            set.add(sc.next());
            System.out.println(set.size());
        }
    }
}
