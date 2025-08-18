package org.example;

public class Employee {
    int emp_id;
    static String emp_name;
    double emp_salary;


    public Employee(int emp_id, String emp_name, double emp_salary) {
        this.emp_id = emp_id;
        this.emp_name = emp_name;
        this.emp_salary = emp_salary;
    }

    public void display(){
        System.out.println("Employee ID: " + emp_id);
        System.out.println("Employee Name: " + emp_name);
        System.out.println("Employee Salary: " + emp_salary);
    }

    public static void empinfo() {
        System.out.println(emp_name);
    }
}
