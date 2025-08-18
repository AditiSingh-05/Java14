package org.example;

public abstract class Students {

    int roll_no;
    public String name;

    Students(int roll_no, String name) {
        this.roll_no = roll_no;
        this.name = name;
    }

    abstract void attend_class();
    abstract void give_exam();
    void register(){
        System.out.println("Name  is "+ name);
        System.out.println("Roll No is "+ roll_no);

    }
}
