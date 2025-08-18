package org.example;

public interface Printable {
    void print();
    void show();

    private void diaplay(){
        System.out.println("I am in interface private method");
    }

}
