

---

# ⭐ **1. Difference between Thread Class and Runnable Interface**

### **Exam Definition**

In Java, a thread can be created by either **extending the Thread class** or **implementing the Runnable interface**.

### **Difference (5-marks structured)**

| Thread Class                                                                              | Runnable Interface                                  |
| ----------------------------------------------------------------------------------------- | --------------------------------------------------- |
| Used by extending `Thread` class.                                                         | Used by implementing `Runnable` interface.          |
| Your class **cannot extend any other class** (Java doesn't support multiple inheritance). | Your class **can extend another class** (flexible). |
| run() method belongs to your thread object directly.                                      | run() method is passed to a Thread object.          |
| Slightly heavier approach.                                                                | More preferred in real applications.                |
| Syntax: `class A extends Thread`                                                          | Syntax: `class A implements Runnable`               |

### **PDF Link**

Thread creation methods are shown clearly on *page 9–14* of pdf .

---

# ⭐ **2. Meaning of Pausing a Running Thread using sleep()**

### **Exam Definition**

`sleep()` is a static method of the Thread class which temporarily pauses the execution of the currently running thread for a given number of milliseconds.

### **Explanation (5 marks)**

* Thread moves from **Running → Timed Waiting**.
* CPU is free to run other threads during sleep.
* After sleep time completes, thread again becomes Runnable.

### **Example (from your PDF Pg 15–16)**



```java
try { 
    Thread.sleep(500);       // pauses for 0.5 seconds
} catch (InterruptedException e) { }
```

This pauses the thread but does NOT release any lock it holds.

---

# ⭐ **3. What is the use of the yield() Method in Java and Switch Expression?**

### **Exam Definition**

`yield()` is a Thread class method that suggests the scheduler to **pause the current thread** and allow other threads of the same priority to run.

* It does NOT guarantee a pause.
* It is simply a hint to the CPU.

### **yield in Switch Expression**

As per Java 13+, `yield` is used to **return a value** from a switch expression.

Example (PDF Pg 105) :

```java
return switch(day) {
    case "Monday" -> {
         yield "Weekday";
    }
    default -> "Unknown";
};
```

---

# ⭐ **4. Define Functional Interface with Two Examples**

### **Exam Definition**

A Functional Interface is an interface that contains **exactly one abstract method**.
It may contain multiple default or static methods.

### **Examples**

1. **Runnable**

   ```java
   public interface Runnable { void run(); }
   ```

2. **Comparator**

   ```java
   int compare(T o1, T o2);
   ```

### **PDF Reference**

Functional interface info and examples are on *page 31–34* .

---

# ⭐ **5. What is an Anonymous Inner Class? Give Syntax.**

### **Exam Definition**

An Anonymous Inner Class is a class defined **inside an expression** without a name, usually to instantiate an interface or subclass immediately.

### **Syntax**

```java
Interface obj = new Interface() {
    public void method() { ... }
};
```

Used when a class is required only once.

---

# ⭐ **6. What is a Lambda Expression? Give One Example.**

### **Exam Definition**

Lambda Expression is an anonymous function introduced in Java 8 that provides a short way to implement functional interfaces.

### **Syntax**

```
(parameters) -> expression
```

### **Example**

```java
(a, b) -> a + b;
```

### **PDF Reference**

Lambda expressions described on *page 28–30* .

---

# ⭐ **7. What are Method References in Java?**

### **Exam Definition**

Method Reference is a shorthand syntax for calling an existing method using `::`.
It is used with functional interfaces.

### **Types**

1. Reference to static method
2. Reference to instance method
3. Reference to constructor
4. Reference to instance method of arbitrary object

### **Example**

```java
System.out::println;
```

### **PDF Reference**

Explained on *page 37–45* with examples. 

---

# ⭐ **8. Define: Text Block, Record, Sealed Class (Java 17 Features)**

These three are clearly explained in Unit-2 PDF pages *112–126* .

### **Text Block**

A text block is a **multi-line string literal** written using triple quotes `"""` (Java 15+).

Example:

```java
String msg = """
Hello, this is a text block.
""";
```

### **Record**

A Record is a **special class for storing data** without needing boilerplate code.
Automatically provides constructor, getters, equals(), hashCode().

Example:

```java
public record Student(String name, int age) {}
```

### **Sealed Class**

A Sealed Class restricts which other classes can inherit it.

Example:

```java
sealed class A permits B, C {}
```

Subclasses must be **final**, **sealed**, or **non-sealed**.


---

# ⭐ **1. Java program with two threads depositing money (using synchronized)**


## **Theory (5 marks)**

When multiple threads update the same bank account, race conditions can occur.
To prevent inconsistent results, `synchronized` is used so only one thread can execute the critical section at a time.

## **Program**

```java
class BankAccount {
    private int balance = 0;

    // synchronized ensures only one thread updates at a time
    public synchronized void deposit(int amount) {
        balance += amount;
    }

    public int getBalance() { return balance; }
}

public class DepositDemo {
    public static void main(String[] args) throws Exception {
        BankAccount acc = new BankAccount();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) acc.deposit(100);
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) acc.deposit(200);
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final Balance: " + acc.getBalance());
    }
}
```

---

# ⭐ **2. Print Even and Odd numbers alternately using two threads**

## **Theory (What to write)**

Two threads coordinate using a shared lock and synchronized block.
One thread prints even numbers, the other prints odd numbers. Both alternate using `wait()` and `notify()`.

## **Program**

```java
class NumberPrinter {
    private int num = 1;
    private final int limit = 10;

    public synchronized void printOdd() {
        while (num <= limit) {
            if (num % 2 == 1) {
                System.out.println("Odd: " + num++);
                notify();
            } else {
                try { wait(); } catch (Exception e) {}
            }
        }
    }

    public synchronized void printEven() {
        while (num <= limit) {
            if (num % 2 == 0) {
                System.out.println("Even: " + num++);
                notify();
            } else {
                try { wait(); } catch (Exception e) {}
            }
        }
    }
}

public class OddEvenDemo {
    public static void main(String[] args) {
        NumberPrinter np = new NumberPrinter();
        new Thread(() -> np.printOdd()).start();
        new Thread(() -> np.printEven()).start();
    }
}
```

---

# ⭐ **3. Filter even numbers using Stream API**

(PDF Pg 46–53 Stream examples)

## **Theory**

Stream API allows functional-style operations such as filtering, mapping, and reduction.

## **Program**

```java
import java.util.*;
import java.util.stream.*;

public class EvenFilter {
    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(1,2,3,4,5,6,7,8,9);

        List<Integer> evens = nums.stream()
                                  .filter(n -> n % 2 == 0)
                                  .collect(Collectors.toList());

        System.out.println(evens);
    }
}
```

---

# ⭐ **4. List of Students – Print using Lambda & Method Reference**

## **Program**

```java
import java.util.*;

class Student {
    String name;
    Student(String name) { this.name = name; }
    public String toString() { return name; }
}

public class StudentPrint {
    public static void main(String[] args) {
        List<Student> list = Arrays.asList(
            new Student("Aditi"), new Student("Riya"), new Student("Kabir")
        );

        System.out.println("Using Lambda:");
        list.forEach(s -> System.out.println(s));

        System.out.println("Using Method Reference:");
        list.forEach(System.out::println);
    }
}
```

---

# ⭐ **5. Dependency Injection using Constructor Injection**

## **Theory**

DI means giving an object its dependency from outside rather than creating dependency inside the class.
Constructor Injection is the most used method.

## **Program**

```java
class Service {
    public void work() { System.out.println("Service Working..."); }
}

class Client {
    private Service service;

    // Constructor Injection
    Client(Service service) {
        this.service = service;
    }

    public void process() {
        service.work();
    }
}

public class DIDemo {
    public static void main(String[] args) {
        Service s = new Service();
        Client c = new Client(s);
        c.process();
    }
}
```

---

# ⭐ **6. Functional Interface implemented using Lambda + Anonymous Class**

## **Theory**

Functional Interface = 1 abstract method.
It can be implemented using:

* Lambda Expression
* Anonymous Inner Class

## **Program**

```java
@FunctionalInterface
interface Greeting {
    void sayHello();
}

public class Demo {
    public static void main(String[] args) {

        // Lambda Expression
        Greeting g1 = () -> System.out.println("Hello from Lambda");
        g1.sayHello();

        // Anonymous Inner Class
        Greeting g2 = new Greeting() {
            public void sayHello() {
                System.out.println("Hello from Anonymous Class");
            }
        };
        g2.sayHello();
    }
}
```

---

# ⭐ **7. Write “Welcome” into a file using try-with-resources**

## **Theory**

try-with-resources automatically closes the resource.
(PDF Pg 66–68)

## **Program**

```java
import java.io.*;

public class FileDemo {
    public static void main(String[] args) {
        try (FileWriter fw = new FileWriter("welcome.txt")) {
            fw.write("Welcome");
            System.out.println("Written successfully!");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
```

---

# ⭐ **8. Bank Account with deposit threads + Text Block + Record + Sealed Class**

(This combines threads + modern Java features.)

## **Program**

```java
// Record for storing transaction info
record Transaction(String type, int amount) { }

// Sealed class for account types
sealed class Account permits SavingsAccount { }

final class SavingsAccount extends Account {
    private int balance = 0;

    public synchronized void deposit(int amount) {
        balance += amount;
        System.out.println("Deposited: " + amount);
    }

    public int getBalance() { return balance; }
}

public class ModernDemo {
    public static void main(String[] args) throws Exception {

        SavingsAccount acc = new SavingsAccount();

        // Text Block
        String msg = """
                --- Bank Deposit Started ---
                """;
        System.out.println(msg);

        Thread t1 = new Thread(() -> acc.deposit(100));
        Thread t2 = new Thread(() -> acc.deposit(200));

        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("Final Balance: " + acc.getBalance());
    }
}
```

---

# ⭐ DONE!


