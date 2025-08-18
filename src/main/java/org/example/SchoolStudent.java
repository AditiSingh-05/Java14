package org.example;

public class SchoolStudent extends Students{

    SchoolStudent(int roll_no, String name) {
        super(roll_no, name);
    }

    @Override
    void attend_class() {
        System.out.println("Attending class for roll no: " + roll_no);
        System.out.println("Student name is: " + name);
    }

    @Override
    void give_exam() {
        System.out.println("Giving exam for roll no: " + roll_no);
        System.out.println("Student name is: " + name);
    }
}
