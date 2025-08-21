package org.example;

public class CollegeStudent1 extends Student1{
    public int roll_no;
    public String name;
    public CollegeStudent1(int roll_no, String name, String address) {
        super(address);
        this.roll_no = roll_no;
        this.name = name;
    }
    public void showRollNo() {
        System.out.println("Roll No: " + roll_no);
    }
}
