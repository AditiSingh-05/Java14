package org.example;

public class Student1 {
    protected String address;

    public Student1(String address) {
        this.address = address;
    }

    public void showAddress() {
        System.out.println("Address: " + address);
    }
}
