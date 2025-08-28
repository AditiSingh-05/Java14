package org.example;
//customexception3
public class MyException {
    public static void main(String[] args) {
        UserDefinedException2 udf = new UserDefinedException2();
        try {
            udf.validate(13);
        } catch (UDF e) {
            System.out.println(e);
        }
    }
}
