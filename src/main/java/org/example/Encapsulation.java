package org.example;

public class Encapsulation {
    public int rollNo;
    public String name;
    private String marks;
    private String address;

    Encapsulation(int rollNo, String name, String marks, String address) {
        this.rollNo = rollNo;
        this.name = name;
        this.marks = marks;
        this.address = address;
    }

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
