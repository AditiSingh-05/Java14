package org.example;

public class MyInterface implements Printable,Showable{
    public void print(){
        System.out.println("I am in print method");
    }
    public void show(){
        System.out.println("I am in show method");
    }
}
