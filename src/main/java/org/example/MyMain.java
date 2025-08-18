package org.example;

public class MyMain {
    public static void main(String[] args) {
        SchoolStudent stud  = new SchoolStudent(5,"Huh");
        stud.give_exam();
        stud.attend_class();

        CollegeStudent studd = new CollegeStudent(6,"Huhh");
        studd.give_exam();
        studd.attend_class();
    }
}
