package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Student student1 = new Student(1,"Me");
        Student student2 = new Student(2,"You");
        student1.display();
        student2.display();


        Employee employee1 = new Employee(1,"John", 50000);
        Employee employee2 = new Employee(2,"Doe", 60000);
        employee1.display();
        employee2.display();
    }
}