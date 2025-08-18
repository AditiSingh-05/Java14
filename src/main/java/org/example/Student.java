package org.example;

public class Student {
    int rollNo;
    String name;

    public Student(int rollNo, String name) {
        this.rollNo = rollNo;
        this.name = name;
    }

    void display(){
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
    }


    //copy constructor

    Student(Student s){
        this.rollNo = s.rollNo;
        this.name = s.name;
    }

}

