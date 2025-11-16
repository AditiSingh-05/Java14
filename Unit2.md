# Java Unit 2 - Part 1: Multithreading and Thread Management

## 📘 Table of Contents
1. [Introduction to Multithreading](#introduction-to-multithreading)
2. [Thread Basics](#thread-basics)
3. [Thread Life Cycle](#thread-life-cycle)
4. [Creating Threads](#creating-threads)
5. [Thread Methods](#thread-methods)
6. [Thread Priority](#thread-priority)
7. [Thread Synchronization](#thread-synchronization)

---

## 1. Introduction to Multithreading

### What is Multithreading?
**Multithreading** is a Java feature that allows concurrent execution of two or more parts of a program to maximize CPU utilization.

**Key Concepts:**
- **Thread**: Smallest unit of a process that can be scheduled by the OS
- **Process**: A program in execution
- **Multitasking**: Running multiple tasks simultaneously

### Why Multithreading?
✅ **Non-blocking**: User isn't blocked; operations run independently  
✅ **Time-saving**: Multiple operations execute simultaneously  
✅ **Independent threads**: Exception in one thread doesn't affect others  
✅ **Shared memory**: Threads share common memory space efficiently

### Real-World Analogy
Think of a restaurant kitchen:
- **Single-threaded**: One chef does everything sequentially (cook → serve → clean)
- **Multi-threaded**: Multiple chefs work simultaneously (one cooks, one serves, one cleans)

---

## 2. Thread Basics

### What is a Thread?
A **thread** is a lightweight subprocess—the smallest unit of processing. Java provides built-in support through:
- `java.lang.Thread` class
- `java.lang.Runnable` interface

### Important Note
⚠️ **At any given time, only ONE thread executes on a single CPU core** (though it switches rapidly, creating the illusion of parallelism)

---

## 3. Thread Life Cycle

A thread in Java goes through several states during its lifetime:

### Thread States

| State | Description |
|-------|-------------|
| **New** | Thread is created but not started (`Thread t = new Thread()`) |
| **Runnable** | Thread is ready to run after calling `start()`, waiting for CPU |
| **Running** | Thread is currently executing (chosen by thread scheduler) |
| **Waiting/Timed Waiting** | Thread is waiting for another thread to perform an action or for a specified time |
| **Terminated (Dead)** | Thread has completed execution or stopped |

### State Transition Flow
```
New → start() → Runnable → (CPU allocation) → Running → (completion) → Terminated
                    ↓                              ↓
                    ← ← ← ← ← ← Waiting/Blocked ← ←
```

### Detailed State Explanation

**1. New State**
- Created using `new Thread()` but `start()` not yet called
- Thread object exists but not a thread of execution

**2. Runnable State**
- After calling `start()`, thread enters runnable state
- Thread scheduler hasn't picked it yet for execution
- Ready to run, waiting for CPU time

**3. Running State**
- Thread scheduler selects the thread from runnable pool
- Thread's `run()` method is executing

**4. Non-Runnable (Blocked/Waiting) State**
- Thread is alive but not eligible to run
- Reasons: `sleep()`, `wait()`, blocked on I/O, waiting for lock

**5. Terminated (Dead) State**
- Thread has completed its `run()` method
- Cannot be restarted

---

## 4. Creating Threads

There are **two main ways** to create threads in Java:

### Method 1: Extending Thread Class

```java
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread is running: " + Thread.currentThread().getName());
    }
    
    public static void main(String[] args) {
        MyThread t1 = new MyThread();
        t1.start(); // Starts the thread
    }
}
```

**How it works:**
1. Create a class that extends `Thread`
2. Override the `run()` method with your code
3. Create an instance and call `start()` (NOT `run()`)

### Method 2: Implementing Runnable Interface ⭐ (Preferred)

```java
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Thread is running: " + Thread.currentThread().getName());
    }
    
    public static void main(String[] args) {
        MyRunnable runnable = new MyRunnable();
        Thread t1 = new Thread(runnable);
        t1.start();
    }
}
```

**How it works:**
1. Create a class that implements `Runnable`
2. Override the `run()` method
3. Create a `Thread` object, passing your `Runnable` instance
4. Call `start()` on the Thread object

### Which Method to Use?
✅ **Use Runnable** (Method 2) because:
- Java doesn't support multiple inheritance; using Runnable allows your class to extend another class
- Better OOP design (composition over inheritance)
- Cleaner separation of task (Runnable) and execution mechanism (Thread)

---

## 5. Thread Methods

### Essential Thread Methods

| Method | Description |
|--------|-------------|
| `start()` | Starts thread execution; JVM calls `run()` |
| `run()` | Contains code to be executed by thread |
| `sleep(long ms)` | Pauses thread for specified milliseconds |
| `getName()` | Returns thread name |
| `setName(String name)` | Sets thread name |
| `getPriority()` | Returns thread priority (1-10) |
| `setPriority(int priority)` | Sets thread priority |
| `currentThread()` | Returns reference to currently executing thread |
| `isAlive()` | Checks if thread is alive |
| `yield()` | Pauses current thread to allow others to execute |
| `join()` | Waits for a thread to die |

### Common Methods Explained

#### sleep() Method
Pauses the thread for a specified time.

```java
class SleepExample extends Thread {
    public void run() {
        for (int i = 1; i <= 5; i++) {
            try {
                Thread.sleep(500); // Sleep for 500ms
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println(i);
        }
    }
    
    public static void main(String[] args) {
        SleepExample t1 = new SleepExample();
        SleepExample t2 = new SleepExample();
        t1.start();
        t2.start();
    }
}
```

**Output:** Numbers 1-5 printed by both threads with 500ms delay between each

#### Naming Threads

```java
class NamingExample extends Thread {
    public void run() {
        System.out.println("Running: " + Thread.currentThread().getName());
    }
    
    public static void main(String[] args) {
        NamingExample t1 = new NamingExample();
        NamingExample t2 = new NamingExample();
        
        System.out.println("Default name t1: " + t1.getName()); // Thread-0
        System.out.println("Default name t2: " + t2.getName()); // Thread-1
        
        t1.start();
        t2.start();
        
        t1.setName("Worker Thread");
        System.out.println("After renaming: " + t1.getName()); // Worker Thread
    }
}
```

---

## 6. Thread Priority

### What is Thread Priority?
Each thread has a **priority** (number between 1 and 10) that suggests to the thread scheduler which thread should be executed first.

⚠️ **Important**: Priority is just a hint; the JVM may ignore it.

### Priority Constants

```java
Thread.MIN_PRIORITY  = 1
Thread.NORM_PRIORITY = 5 (default)
Thread.MAX_PRIORITY  = 10
```

### Priority Example

```java
class PriorityExample extends Thread {
    public void run() {
        System.out.println("Thread: " + Thread.currentThread().getName());
        System.out.println("Priority: " + Thread.currentThread().getPriority());
    }
    
    public static void main(String[] args) {
        PriorityExample t1 = new PriorityExample();
        PriorityExample t2 = new PriorityExample();
        
        t1.setPriority(Thread.MIN_PRIORITY); // Priority 1
        t2.setPriority(Thread.MAX_PRIORITY); // Priority 10
        
        t1.start();
        t2.start();
    }
}
```

**Note**: Higher priority thread has a better chance of execution, but it's not guaranteed.

---

## 7. Thread Synchronization

### The Problem: Race Condition
When multiple threads access shared resources simultaneously, data inconsistency can occur.

**Example of Race Condition:**
```java
class Counter {
    private int count = 0;
    
    public void increment() {
        count++; // NOT atomic! (read → modify → write)
    }
    
    public int getCount() {
        return count;
    }
}
```

If two threads call `increment()` simultaneously:
1. Thread 1 reads count = 0
2. Thread 2 reads count = 0
3. Thread 1 writes count = 1
4. Thread 2 writes count = 1

**Expected:** count = 2  
**Actual:** count = 1 ❌

### Solution: Synchronization
Use the `synchronized` keyword to ensure only one thread accesses the method at a time.

```java
class Counter {
    private int count = 0;
    
    public synchronized void increment() {
        count++;
    }
    
    public int getCount() {
        return count;
    }
}
```

### Complete Synchronization Example

```java
class Counter {
    private int count = 0;
    
    public synchronized void increment() {
        count++;
    }
    
    public int getCount() {
        return count;
    }
}

public class SyncExample {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
        
        t1.start();
        t2.start();
        
        t1.join(); // Wait for t1 to finish
        t2.join(); // Wait for t2 to finish
        
        System.out.println("Final Count: " + counter.getCount()); // Output: 2000 ✅
    }
}
```

### Why Synchronization?
✅ Prevents **race conditions**  
✅ Ensures **data consistency**  
✅ Guarantees **thread safety**

### How Synchronization Works
- When a thread enters a synchronized method, it acquires a **lock** on the object
- Other threads must wait until the lock is released
- Only one thread can execute the synchronized method at a time

---

## 📌 Key Takeaways

1. **Multithreading** allows concurrent execution to maximize CPU usage
2. **Thread lifecycle**: New → Runnable → Running → Waiting → Terminated
3. **Two ways to create threads**: Extend Thread or Implement Runnable (prefer Runnable)
4. **start() vs run()**: Always call `start()`, not `run()` directly
5. **Thread priorities** are hints, not guarantees (1-10, default is 5)
6. **Synchronization** prevents race conditions when multiple threads access shared resources
7. **join()** makes one thread wait for another to complete

---

## 🎯 Interview Tips

**Q: Why use Runnable over Thread?**  
A: Java doesn't support multiple inheritance. Implementing Runnable allows your class to extend another class. It also follows better OOP design (composition over inheritance).

**Q: What's the difference between start() and run()?**  
A: `start()` creates a new thread and calls `run()` in that new thread. Calling `run()` directly executes it in the current thread (no multithreading).

**Q: What is a race condition?**  
A: When multiple threads access shared data simultaneously and the final result depends on thread scheduling timing, leading to unpredictable results.

**Q: What does synchronized keyword do?**  
A: It ensures only one thread can execute a synchronized method/block at a time on the same object, preventing race conditions.

---

## ⚠️ Common Mistakes

1. Calling `run()` instead of `start()` → No new thread is created
2. Not handling `InterruptedException` when using `sleep()`
3. Forgetting to synchronize shared resource access → Race conditions
4. Using `synchronized` unnecessarily → Performance overhead
5. Not using `join()` when you need to wait for thread completion

---

# Java Unit 2 - Part 2: Functional Programming Features

## 📘 Table of Contents
1. [Functional Interfaces](#functional-interfaces)
2. [Lambda Expressions](#lambda-expressions)
3. [Method References](#method-references)
4. [Stream API](#stream-api)
5. [Default Methods in Interfaces](#default-methods-in-interfaces)
6. [Static Methods in Interfaces](#static-methods-in-interfaces)
7. [forEach Loop](#foreach-loop)
8. [Try-With-Resources](#try-with-resources)

---

## 1. Functional Interfaces

### 🔬 Concept Overview
A **Functional Interface** is an interface that contains **exactly ONE abstract method**. It represents a single unit of behavior that can be implemented using lambda expressions or method references.

**Key Characteristics:**
- Only ONE abstract method (Single Abstract Method - SAM)
- Can have multiple default methods
- Can have multiple static methods
- Can have methods from Object class (equals, hashCode, toString)
- Optional `@FunctionalInterface` annotation (compiler enforces the rule)

### ⚙️ Why Functional Interfaces Exist

**Before Java 8 (Anonymous Inner Classes):**
```java
// Verbose and cluttered
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("Running");
    }
};
```

**After Java 8 (Lambda Expressions):**
```java
// Clean and concise
Runnable r = () -> System.out.println("Running");
```

Functional interfaces are the **bridge** between OOP and functional programming in Java.

### 🧩 Creating Custom Functional Interface

```java
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b); // Single abstract method
    
    // Default methods allowed
    default void printResult(int result) {
        System.out.println("Result: " + result);
    }
    
    // Static methods allowed
    static void info() {
        System.out.println("Calculator interface");
    }
}

// Usage with lambda
public class FunctionalInterfaceDemo {
    public static void main(String[] args) {
        // Lambda implementation
        Calculator add = (a, b) -> a + b;
        Calculator multiply = (a, b) -> a * b;
        
        System.out.println(add.calculate(5, 3));      // 8
        System.out.println(multiply.calculate(5, 3)); // 15
        
        add.printResult(add.calculate(10, 20));       // Result: 30
        Calculator.info();                             // Calculator interface
    }
}
```

### 📦 Built-in Functional Interfaces (java.util.function)

Java provides pre-built functional interfaces for common use cases:

| Interface | Method Signature | Purpose | Example |
|-----------|-----------------|---------|---------|
| **Predicate\<T>** | `boolean test(T t)` | Tests a condition | `Predicate<Integer> isEven = n -> n % 2 == 0;` |
| **Function<T, R>** | `R apply(T t)` | Transforms input to output | `Function<String, Integer> len = s -> s.length();` |
| **Consumer\<T>** | `void accept(T t)` | Performs action on input | `Consumer<String> print = System.out::println;` |
| **Supplier\<T>** | `T get()` | Supplies a value (no input) | `Supplier<Double> random = Math::random;` |
| **BiFunction<T, U, R>** | `R apply(T t, U u)` | Takes 2 inputs, returns 1 output | `BiFunction<Integer, Integer, Integer> add = (a,b) -> a+b;` |
| **UnaryOperator\<T>** | `T apply(T t)` | Special Function where input = output type | `UnaryOperator<Integer> square = n -> n * n;` |
| **BinaryOperator\<T>** | `T apply(T t1, T t2)` | Special BiFunction where all types same | `BinaryOperator<Integer> max = Math::max;` |

### 🧪 Built-in Functional Interfaces Example

```java
import java.util.function.*;

public class BuiltInFunctionalInterfacesDemo {
    public static void main(String[] args) {
        // 1. Predicate - Boolean test
        Predicate<Integer> isEven = n -> n % 2 == 0;
        System.out.println("Is 8 even? " + isEven.test(8));  // true
        System.out.println("Is 7 even? " + isEven.test(7));  // false
        
        // 2. Function - Transformation
        Function<String, Integer> stringLength = s -> s.length();
        System.out.println("Length of 'Hello': " + stringLength.apply("Hello")); // 5
        
        // 3. Consumer - Action without return
        Consumer<String> greet = name -> System.out.println("Hello, " + name);
        greet.accept("Aditi"); // Hello, Aditi
        
        // 4. Supplier - Supply value
        Supplier<Double> randomValue = () -> Math.random();
        System.out.println("Random: " + randomValue.get()); // Random number
        
        // 5. BiFunction - Two inputs, one output
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        System.out.println("5 + 3 = " + add.apply(5, 3)); // 8
        
        // 6. UnaryOperator - Transform same type
        UnaryOperator<Integer> square = n -> n * n;
        System.out.println("Square of 5: " + square.apply(5)); // 25
        
        // 7. BinaryOperator - Two same types to one
        BinaryOperator<Integer> max = (a, b) -> a > b ? a : b;
        System.out.println("Max(10, 20): " + max.apply(10, 20)); // 20
    }
}
```

### ⚠️ Common Mistakes

❌ **Multiple abstract methods**
```java
@FunctionalInterface
interface Wrong {
    void method1(); // OK
    void method2(); // ERROR! More than one abstract method
}
```

❌ **Forgetting @FunctionalInterface annotation** (not error, but bad practice)
```java
// Works but no compile-time safety
interface Calculator {
    int calculate(int a, int b);
}
```

✅ **Correct approach**
```java
@FunctionalInterface // Compiler ensures single abstract method
interface Calculator {
    int calculate(int a, int b);
}
```

### 💡 Pro Tips

1. **Always use @FunctionalInterface**: Provides compile-time safety
2. **Prefer built-in interfaces**: Don't create custom ones unnecessarily
3. **Method reference over lambda**: When possible, use method references for readability
4. **Chain functional interfaces**: Predicate has `and()`, `or()`, `negate()`

```java
Predicate<Integer> isEven = n -> n % 2 == 0;
Predicate<Integer> isPositive = n -> n > 0;
Predicate<Integer> isEvenAndPositive = isEven.and(isPositive);

System.out.println(isEvenAndPositive.test(4));  // true
System.out.println(isEvenAndPositive.test(-4)); // false
```

---

## 2. Lambda Expressions

### 🔬 Concept Overview
A **Lambda Expression** is an anonymous function (function without a name) that can be treated as a value. It provides a concise way to implement functional interfaces.

**Syntax:**
```
(parameters) -> expression
or
(parameters) -> { statements; }
```

### ⚙️ Lambda Expression Structure

```java
// Zero parameters
() -> System.out.println("No parameters");

// Single parameter (parentheses optional)
x -> x * x
(x) -> x * x  // Both are valid

// Multiple parameters
(x, y) -> x + y

// Multiple statements (requires curly braces)
(x, y) -> {
    int sum = x + y;
    return sum;
}

// With type declaration (usually inferred)
(int x, int y) -> x + y
```

### 🧩 Lambda Expression Types

#### 1. Zero Parameter Lambda
```java
@FunctionalInterface
interface Greeting {
    void sayHello();
}

public class LambdaDemo {
    public static void main(String[] args) {
        // Before Java 8
        Greeting greeting1 = new Greeting() {
            @Override
            public void sayHello() {
                System.out.println("Hello World");
            }
        };
        
        // With Lambda
        Greeting greeting2 = () -> System.out.println("Hello World");
        
        greeting2.sayHello(); // Hello World
    }
}
```

#### 2. Single Parameter Lambda
```java
@FunctionalInterface
interface NumericTest {
    boolean test(int n);
}

public class SingleParamLambda {
    public static void main(String[] args) {
        // Parentheses optional for single parameter
        NumericTest isEven = n -> n % 2 == 0;
        NumericTest isPositive = n -> n > 0;
        
        System.out.println("10 is even: " + isEven.test(10));     // true
        System.out.println("9 is even: " + isEven.test(9));       // false
        System.out.println("-5 is positive: " + isPositive.test(-5)); // false
    }
}
```

#### 3. Multiple Parameters Lambda
```java
@FunctionalInterface
interface Arithmetic {
    int operate(int a, int b);
}

public class MultipleParamLambda {
    public static void main(String[] args) {
        Arithmetic add = (a, b) -> a + b;
        Arithmetic subtract = (a, b) -> a - b;
        Arithmetic multiply = (a, b) -> a * b;
        Arithmetic divide = (a, b) -> {
            if (b == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return a / b;
        };
        
        System.out.println("10 + 5 = " + add.operate(10, 5));       // 15
        System.out.println("10 - 5 = " + subtract.operate(10, 5));  // 5
        System.out.println("10 * 5 = " + multiply.operate(10, 5));  // 50
        System.out.println("10 / 5 = " + divide.operate(10, 5));    // 2
    }
}
```

### 🔄 Lambda vs Anonymous Inner Class

| Feature | Lambda Expression | Anonymous Inner Class |
|---------|------------------|----------------------|
| **Syntax** | Concise | Verbose |
| **this keyword** | Refers to enclosing class | Refers to anonymous class itself |
| **Scope** | Does not create new scope | Creates new scope |
| **Interface type** | Only functional interfaces | Any interface/abstract class |
| **Variables** | Can access effectively final variables | Can access final variables |
| **Compilation** | Uses invokedynamic (faster) | Creates .class file (slower) |

```java
public class LambdaVsAnonymous {
    private String name = "Outer";
    
    public void demonstrate() {
        // Anonymous Inner Class
        Runnable r1 = new Runnable() {
            private String name = "Inner";
            
            @Override
            public void run() {
                System.out.println(this.name); // Inner (refers to anonymous class)
            }
        };
        
        // Lambda Expression
        Runnable r2 = () -> {
            System.out.println(this.name); // Outer (refers to enclosing class)
        };
        
        r1.run(); // Inner
        r2.run(); // Outer
    }
    
    public static void main(String[] args) {
        new LambdaVsAnonymous().demonstrate();
    }
}
```

### 💡 Pro Tips & Best Practices

#### 1. Keep Lambdas Short (Prefer Single Expression)
```java
// ✅ Good - Single expression
Function<Integer, Integer> square = n -> n * n;

// ❌ Avoid - Too complex for lambda
Function<Integer, String> complex = n -> {
    if (n < 0) return "Negative";
    else if (n == 0) return "Zero";
    else if (n < 10) return "Single digit";
    else if (n < 100) return "Double digit";
    else return "Large number";
};

// ✅ Better - Use method reference or separate method
Function<Integer, String> better = MyClass::classifyNumber;
```

#### 2. Use Method References When Possible
```java
// ❌ Lambda
list.forEach(item -> System.out.println(item));

// ✅ Method reference (more readable)
list.forEach(System.out::println);
```

#### 3. Effectively Final Variables
```java
public void demonstrateVariableCapture() {
    int multiplier = 10; // Effectively final
    
    // Lambda can access it
    Function<Integer, Integer> multiply = n -> n * multiplier;
    
    // ❌ This would cause error:
    // multiplier = 20; // Cannot modify captured variable
    
    System.out.println(multiply.apply(5)); // 50
}
```

### ⚠️ Common Mistakes

❌ **Modifying captured variables**
```java
int counter = 0;
list.forEach(item -> counter++); // ERROR: Variable must be final or effectively final
```

❌ **Ignoring return type**
```java
Function<Integer, Integer> wrong = n -> { n * n }; // ERROR: Missing return
Function<Integer, Integer> correct = n -> { return n * n; }; // OK
Function<Integer, Integer> better = n -> n * n; // Best (no braces needed)
```

❌ **Using lambda for complex logic**
```java
// ❌ Bad: Lambda too complex
Comparator<Student> comparator = (s1, s2) -> {
    if (s1.getGrade() != s2.getGrade()) {
        return s1.getGrade() - s2.getGrade();
    }
    if (!s1.getName().equals(s2.getName())) {
        return s1.getName().compareTo(s2.getName());
    }
    return s1.getRollNo() - s2.getRollNo();
};

// ✅ Good: Extract to method
Comparator<Student> comparator = this::compareStudents;
```

---

## 3. Method References

### 🔬 Concept Overview
**Method References** are shorthand notation of lambda expressions to call a method. They make code more readable and concise when the lambda expression only calls an existing method.

**Syntax:** `ClassName::methodName` or `objectName::methodName`

### ⚙️ Why Method References?

```java
// Lambda expression
list.forEach(item -> System.out.println(item));

// Method reference (cleaner)
list.forEach(System.out::println);
```

When a lambda only calls a method without additional logic, method references are preferred.

### 🧩 Types of Method References

| Type | Syntax | Example | Lambda Equivalent |
|------|--------|---------|------------------|
| **Static method** | `ClassName::staticMethod` | `Math::max` | `(a, b) -> Math.max(a, b)` |
| **Instance method of particular object** | `object::instanceMethod` | `str::length` | `() -> str.length()` |
| **Instance method of arbitrary object** | `ClassName::instanceMethod` | `String::toLowerCase` | `s -> s.toLowerCase()` |
| **Constructor** | `ClassName::new` | `ArrayList::new` | `() -> new ArrayList<>()` |

### 1️⃣ Reference to Static Method

```java
@FunctionalInterface
interface Converter {
    int convert(String s);
}

public class StaticMethodReference {
    // Static method
    public static int stringToInt(String s) {
        return Integer.parseInt(s);
    }
    
    public static void main(String[] args) {
        // Using lambda
        Converter lambda = s -> Integer.parseInt(s);
        
        // Using method reference (cleaner)
        Converter methodRef = Integer::parseInt;
        
        System.out.println(lambda.convert("123"));     // 123
        System.out.println(methodRef.convert("456"));  // 456
    }
}
```

**Built-in examples:**
```java
List<Integer> numbers = Arrays.asList(-3, 5, -1, 2);

// Lambda
numbers.stream().map(n -> Math.abs(n));

// Method reference
numbers.stream().map(Math::abs); // Cleaner!
```

### 2️⃣ Reference to Instance Method of Particular Object

```java
@FunctionalInterface
interface Printer {
    void print(String msg);
}

public class InstanceMethodReference {
    public void displayMessage(String msg) {
        System.out.println("Message: " + msg);
    }
    
    public static void main(String[] args) {
        InstanceMethodReference obj = new InstanceMethodReference();
        
        // Lambda
        Printer lambda = msg -> obj.displayMessage(msg);
        
        // Method reference
        Printer methodRef = obj::displayMessage;
        
        methodRef.print("Hello Method Reference!"); // Message: Hello Method Reference!
    }
}
```

**Real-world example:**
```java
class Logger {
    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
}

public class Demo {
    public static void main(String[] args) {
        Logger logger = new Logger();
        List<String> messages = Arrays.asList("Error", "Warning", "Info");
        
        // Using instance method reference
        messages.forEach(logger::log);
        // Output:
        // [LOG] Error
        // [LOG] Warning
        // [LOG] Info
    }
}
```

### 3️⃣ Reference to Instance Method of Arbitrary Object of a Particular Type

This is the **most confusing** type but very powerful!

```java
// Lambda: First parameter becomes the object on which method is called
Function<String, String> lambda = s -> s.toUpperCase();

// Method reference: String::toUpperCase
Function<String, String> methodRef = String::toUpperCase;

System.out.println(methodRef.apply("hello")); // HELLO
```

**More examples:**
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// Lambda
names.stream().map(name -> name.toUpperCase());

// Method reference (cleaner)
names.stream().map(String::toUpperCase);

// Sorting example
List<String> fruits = Arrays.asList("banana", "apple", "cherry");

// Lambda
fruits.sort((s1, s2) -> s1.compareToIgnoreCase(s2));

// Method reference
fruits.sort(String::compareToIgnoreCase);
```

**How it works internally:**
```java
// When you write:
String::toUpperCase

// Java translates it to:
(String s) -> s.toUpperCase()

// When you write:
String::compareToIgnoreCase

// Java translates it to:
(String s1, String s2) -> s1.compareToIgnoreCase(s2)
```

### 4️⃣ Reference to Constructor

```java
@FunctionalInterface
interface StudentFactory {
    Student create(String name, int age);
}

class Student {
    private String name;
    private int age;
    
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    @Override
    public String toString() {
        return "Student{name='" + name + "', age=" + age + "}";
    }
}

public class ConstructorReference {
    public static void main(String[] args) {
        // Lambda
        StudentFactory lambda = (name, age) -> new Student(name, age);
        
        // Constructor reference
        StudentFactory methodRef = Student::new;
        
        Student s1 = methodRef.create("Aditi", 21);
        System.out.println(s1); // Student{name='Aditi', age=21}
    }
}
```

**Real-world Stream example:**
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// Convert List<String> to List<Student>
// Lambda
List<Student> students1 = names.stream()
    .map(name -> new Student(name, 20))
    .collect(Collectors.toList());

// Constructor reference (but need to match constructor signature)
// Assuming Student has constructor: Student(String name)
List<Student> students2 = names.stream()
    .map(Student::new)
    .collect(Collectors.toList());
```

### 💡 Pro Tips & Best Practices

#### 1. Prefer Method References Over Lambdas (When Clear)
```java
// ✅ Clear and concise
list.forEach(System.out::println);

// ❌ Unnecessarily verbose
list.forEach(item -> System.out.println(item));
```

#### 2. Use Lambda When Adding Logic
```java
// ✅ Lambda is better here (has additional logic)
list.forEach(item -> System.out.println("Item: " + item));

// ❌ Cannot use method reference here
```

#### 3. Constructor References with Streams
```java
// Creating new objects from stream
List<Integer> ages = Arrays.asList(20, 21, 22);
List<Student> students = ages.stream()
    .map(Student::new) // Assumes Student(int age) constructor exists
    .collect(Collectors.toList());
```

### ⚠️ Common Mistakes

❌ **Wrong parameter count**
```java
BiFunction<Integer, Integer, Integer> add = Math::max; // OK (2 params)
Function<Integer, Integer> wrong = Math::max; // ERROR (needs 2 params)
```

❌ **Confusing arbitrary object method reference**
```java
List<String> list = Arrays.asList("a", "b", "c");

// ❌ Wrong
list.forEach(list::add); // ERROR

// ✅ Correct
list.forEach(System.out::println);
```

---

## 4. Stream API

### 🔬 Concept Overview
The **Stream API** (introduced in Java 8) provides a functional approach to process collections of objects. Streams represent a sequence of elements supporting sequential and parallel aggregate operations.

**Key Characteristics:**
- **Not a data structure**: Stream doesn't store data; it conveys elements from a source
- **Functional in nature**: Operations don't modify the source
- **Lazy evaluation**: Intermediate operations are not executed until terminal operation is called
- **Possibly unbounded**: Streams can be infinite
- **Consumable**: Elements are visited only once; need new stream to revisit

### ⚙️ Stream Pipeline

```
Source → Intermediate Operations (0 or more) → Terminal Operation
```

**Example:**
```java
list.stream()           // Source
    .filter(...)        // Intermediate
    .map(...)           // Intermediate
    .collect(...);      // Terminal
```

### 🧩 Creating Streams

```java
// 1. From Collection
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> stream1 = list.stream();

// 2. From Array
String[] array = {"a", "b", "c"};
Stream<String> stream2 = Arrays.stream(array);

// 3. Using Stream.of()
Stream<String> stream3 = Stream.of("a", "b", "c");

// 4. Using Stream.builder()
Stream<String> stream4 = Stream.<String>builder()
    .add("a")
    .add("b")
    .add("c")
    .build();

// 5. Infinite Stream
Stream<Integer> infiniteStream = Stream.iterate(0, n -> n + 2); // 0, 2, 4, 6...

// 6. Empty Stream
Stream<String> emptyStream = Stream.empty();
```

### 📊 Stream Operations

#### **Intermediate Operations** (Return Stream - Chainable)

| Operation | Description | Example |
|-----------|-------------|---------|
| `filter()` | Filters elements based on predicate | `.filter(n -> n > 0)` |
| `map()` | Transforms each element | `.map(String::toUpperCase)` |
| `flatMap()` | Flattens nested structures | `.flatMap(list -> list.stream())` |
| `distinct()` | Removes duplicates | `.distinct()` |
| `sorted()` | Sorts elements | `.sorted()` or `.sorted(Comparator)` |
| `peek()` | Performs action without changing stream | `.peek(System.out::println)` |
| `limit()` | Limits to first n elements | `.limit(5)` |
| `skip()` | Skips first n elements | `.skip(2)` |

#### **Terminal Operations** (Return Result - Ends Stream)

| Operation | Description | Example |
|-----------|-------------|---------|
| `collect()` | Collects elements into collection | `.collect(Collectors.toList())` |
| `forEach()` | Performs action on each element | `.forEach(System.out::println)` |
| `count()` | Returns count of elements | `.count()` |
| `reduce()` | Reduces to single value | `.reduce(0, (a,b) -> a + b)` |
| `anyMatch()` | Tests if any element matches | `.anyMatch(n -> n > 10)` |
| `allMatch()` | Tests if all elements match | `.allMatch(n -> n > 0)` |
| `noneMatch()` | Tests if no elements match | `.noneMatch(n -> n < 0)` |
| `findFirst()` | Returns first element | `.findFirst()` |
| `findAny()` | Returns any element | `.findAny()` |
| `min()/max()` | Returns min/max element | `.min(Comparator.naturalOrder())` |

### 🧪 Practical Stream Examples

#### Example 1: Filtering Collection
```java
class Product {
    int id;
    String name;
    float price;
    
    public Product(int id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

public class StreamFilteringExample {
    public static void main(String[] args) {
        List<Product> products = Arrays.asList(
            new Product(1, "HP Laptop", 25000f),
            new Product(2, "Dell Laptop", 30000f),
            new Product(3, "Lenovo Laptop", 28000f),
            new Product(4, "Sony Laptop", 28000f),
            new Product(5, "Apple Laptop", 90000f)
        );
        
        // Filter products with price > 30000
        List<Float> expensivePrices = products.stream()
            .filter(p -> p.price > 30000)
            .map(p -> p.price)
            .collect(Collectors.toList());
        
        System.out.println(expensivePrices); // [90000.0]
        
        // Filter and print names
        products.stream()
            .filter(p -> p.price == 30000)
            .forEach(p -> System.out.println(p.name)); // Dell Laptop
        
        // Count products under 30000
        long count = products.stream()
            .filter(p -> p.price < 30000)
            .count();
        
        System.out.println("Products under 30000: " + count); // 3
    }
}
```

#### Example 2: Stream Iteration
```java
public class StreamIterationExample {
    public static void main(String[] args) {
        // Generate and process infinite stream
        Stream.iterate(1, n -> n + 1)  // 1, 2, 3, 4, 5, 6...
            .filter(n -> n % 5 == 0)    // 5, 10, 15, 20, 25...
            .limit(5)                    // First 5 elements
            .forEach(System.out::println);
        
        // Output:
        // 5
        // 10
        // 15
        // 20
        // 25
    }
}
```

#### Example 3: Map and Reduce
```java
public class MapReduceExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        
        // Map: Square each number
        List<Integer> squares = numbers.stream()
            .map(n -> n * n)
            .collect(Collectors.toList());
        System.out.println(squares); // [1, 4, 9, 16, 25]
        
        // Reduce: Sum all numbers
        int sum = numbers.stream()
            .reduce(0, (a, b) -> a + b);
        System.out.println("Sum: " + sum); // 15
        
        // Reduce: Product of all numbers
        int product = numbers.stream()
            .reduce(1, (a, b) -> a * b);
        System.out.println("Product: " + product); // 120
        
        // Find max
        Optional<Integer> max = numbers.stream()
            .max(Integer::compareTo);
        max.ifPresent(m -> System.out.println("Max: " + m)); // 5
    }
}
```

#### Example 4: FlatMap
```java
public class FlatMapExample {
    public static void main(String[] args) {
        List<List<Integer>> nestedList = Arrays.asList(
            Arrays.asList(1, 2, 3),
            Arrays.asList(4, 5),
            Arrays.asList(6, 7, 8, 9)
        );
        
        // Flatten nested list
        List<Integer> flatList = nestedList.stream()
            .flatMap(list -> list.stream())
            .collect(Collectors.toList());
        
        System.out.println(flatList); // [1, 2, 3, 4, 5, 6, 7, 8, 9]
        
        // Real-world example: Get all words from sentences
        List<String> sentences = Arrays.asList(
            "Hello World",
            "Java Streams",
            "Functional Programming"
        );
        
        List<String> words = sentences.stream()
            .flatMap(sentence -> Arrays.stream(sentence.split(" ")))
            .collect(Collectors.toList());
        
        System.out.println(words); 
        // [Hello, World, Java, Streams, Functional, Programming]
    }
}
```

#### Example 5: Collectors
```java
public class CollectorsExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Alice");
        
        // Collect to List
        List<String> list = names.stream()
            .collect(Collectors.toList());
        
        // Collect to Set (removes duplicates)
        Set<String> set = names.stream()
            .collect(Collectors.toSet());
        System.out.println(set); // [Alice, Bob, Charlie, David]
        
        // Joining strings
        String joined = names.stream()
            .collect(Collectors.joining(", "));
        System.out.println(joined); // Alice, Bob, Charlie, David, Alice
        
        // Grouping by length
        Map<Integer, List<String>> groupedByLength = names.stream()
            .collect(Collectors.groupingBy(String::length));
        System.out.println(groupedByLength); 
        // {3=[Bob], 5=[Alice, David, Alice], 7=[Charlie]}
        
        // Counting
        Map<String, Long> nameCount = names.stream()
            .collect(Collectors.groupingBy(
                name -> name,
                Collectors.counting()
            ));
        System.out.println(nameCount); 
        // {Alice=2, Bob=1, Charlie=1, David=1}
    }
}
```

### 💡 Pro Tips & Best Practices

#### 1. Streams are Lazy
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// This doesn't execute anything!
Stream<Integer> stream = numbers.stream()
    .filter(n -> {
        System.out.println("Filtering: " + n);
        return n > 2;
    })
    .map(n -> {
        System.out.println("Mapping: " + n);
        return n * 2;
    });

// Only when terminal operation is called, pipeline executes
List<Integer> result = stream.collect(Collectors.toList());
// Output:
// Filtering: 1
// Filtering: 2
// Filtering: 3
// Mapping: 3
// Filtering: 4
// Mapping: 4
// Filtering: 5
// Mapping: 5
```

#### 2. Streams are Consumable (Can't Reuse)
```java
Stream<Integer> stream = Stream.of(1, 2, 3);
stream.forEach(System.out::println); // OK

stream.forEach(System.out::println); // ERROR: stream has already been operated upon or closed
```

#### 3. Prefer Method References
```java
// ❌ Verbose
list.stream().map(s -> s.toUpperCase()).forEach(s -> System.out.println(s));

// ✅ Clean
list.stream().map(String::toUpperCase).forEach(System.out::println);
```

#### 4. Use Parallel Streams for Large Data (Carefully)
```java
// Sequential (default)
list.stream().map(...).collect(...);

// Parallel (uses ForkJoinPool)
list.parallelStream().map(...).collect(...);

// Note: Only use for CPU-intensive operations on large datasets
// Overhead of parallelization can be more than benefit for small data
```

### ⚠️ Common Mistakes

❌ **Modifying source during stream operation**
```java
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
list.stream().forEach(n -> list.add(n * 2)); // ConcurrentModificationException
```

❌ **Reusing streams**
```java
Stream<Integer> stream = Stream.of(1, 2, 3);
stream.count(); // 3
stream.count(); // ERROR
```

❌ **Not storing terminal operation result**
```java
list.stream().filter(n -> n > 0); // Does nothing! (no terminal operation)
list.stream().filter(n -> n > 0).collect(Collectors.toList()); // ✅ Correct
```

---

## 5. Default Methods in Interfaces

### 🔬 Concept Overview
**Default methods** (introduced in Java 8) allow you to add new methods to interfaces with implementation without breaking existing classes that implement the interface.

**Syntax:**
```java
default returnType methodName() {
    // implementation
}
```

### ⚙️ Why Default Methods Exist

**Problem before Java 8:**
```java
interface Vehicle {
    void drive();
}

class Car implements Vehicle {
    @Override
    public void drive() {
        System.out.println("Car is driving");
    }
}

// If you add new method to Vehicle interface:
interface Vehicle {
    void drive();
    void honk(); // This breaks ALL implementing classes!
}
```

**Solution with Default Methods:**
```java
interface Vehicle {
    void drive();
    
    default void honk() {  // Default implementation
        System.out.println("Beep beep!");
    }
}

class Car implements Vehicle {
    @Override
    public void drive() {
        System.out.println("Car is driving");
    }
    // honk() is inherited automatically
}
```

### 🧪 Default Method Example

```java
interface Greetable {
    // Abstract method (must be implemented)
    void greet(String name);
    
    // Default method (optional to override)
    default void sayHello() {
        System.out.println("Hello! This is a default method");
    }
    
    default void sayGoodbye() {
        System.out.println("Goodbye!");
    }
}

class Person implements Greetable {
    @Override
    public void greet(String name) {
        System.out.println("Hello, " + name);
    }
    
    // Optionally override default method
    @Override
    public void sayHello() {
        System.out.println("Hi there! (Overridden default method)");
    }
    
    // sayGoodbye() is inherited as-is
}

public class DefaultMethodDemo {
    public static void main(String[] args) {
        Person person = new Person();
        person.greet("Aditi");    // Hello, Aditi (implemented method)
        person.sayHello();         // Hi there! (overridden default method)
        person.sayGoodbye();       // Goodbye! (inherited default method)
    }
}
```

### 🔄 Real-World Example: Backward Compatibility

**Scenario:** Java 8 added `forEach()` to `Iterable` interface without breaking millions of existing classes.

```java
// Java 8 added this to java.lang.Iterable:
public interface Iterable<T> {
    Iterator<T> iterator(); // Existing abstract method
    
    // New default method (doesn't break existing implementations!)
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }
}

// Now all collections automatically have forEach():
List<String> list = Arrays.asList("A", "B", "C");
list.forEach(System.out::println); // Works without modifying ArrayList class!
```

### ⚠️ Diamond Problem with Default Methods

```java
interface A {
    default void show() {
        System.out.println("A's show");
    }
}

interface B {
    default void show() {
        System.out.println("B's show");
    }
}

// ❌ This causes ambiguity
class C implements A, B {
    // Compiler error: class C inherits unrelated defaults for show() from types A and B
}

// ✅ Solution: Override the method
class C implements A, B {
    @Override
    public void show() {
        // Call specific interface method
        A.super.show();  // Calls A's implementation
        // or
        B.super.show();  // Calls B's implementation
        // or provide your own implementation
        System.out.println("C's show");
    }
}
```

### 💡 Pro Tips

1. **Use for evolution**: Add methods to interfaces without breaking implementations
2. **Not for multiple inheritance**: Don't design interfaces as classes with default methods
3. **Keep defaults simple**: Complex logic should be in implementation classes
4. **Document clearly**: Indicate when overriding is recommended

---

## 6. Static Methods in Interfaces

### 🔬 Concept Overview
**Static methods** in interfaces (Java 8+) belong to the interface itself, not to implementing classes. They provide utility functions related to the interface.

**Key Characteristics:**
- Cannot be overridden
- Called using interface name (not instance)
- Not inherited by implementing classes

### 🧪 Static Method Example

```java
interface MathUtils {
    // Static methods
    static int add(int a, int b) {
        return a + b;
    }
    
    static int subtract(int a, int b) {
        return a - b;
    }
    
    static int multiply(int a, int b) {
        return a * b;
    }
}

public class StaticMethodDemo {
    public static void main(String[] args) {
        // Call using interface name
        int sum = MathUtils.add(10, 5);
        int diff = MathUtils.subtract(10, 5);
        int product = MathUtils.multiply(10, 5);
        
        System.out.println("Sum: " + sum);         // 15
        System.out.println("Diff: " + diff);       // 5
        System.out.println("Product: " + product); // 50
    }
}
```

### 🔄 Scope of Static Methods

```java
interface PrintDemo {
    static void hello() {
        System.out.println("Called from Interface PrintDemo");
    }
}

class MyClass implements PrintDemo {
    // This is a completely different method (not override)
    static void hello() {
        System.out.println("Called from MyClass");
    }
}

public class StaticMethodScope {
    public static void main(String[] args) {
        PrintDemo.hello();  // Called from Interface PrintDemo
        MyClass.hello();    // Called from MyClass
        
        // Cannot call via instance
        // MyClass obj = new MyClass();
        // obj.hello(); // ❌ Not accessible via instance
    }
}
```

### 💡 Real-World Use Case: Factory Methods

```java
interface Connection {
    void connect();
    void disconnect();
    
    // Static factory method
    static Connection createConnection(String type) {
        if (type.equals("DATABASE")) {
            return new DatabaseConnection();
        } else if (type.equals("HTTP")) {
            return new HttpConnection();
        }
        throw new IllegalArgumentException("Unknown connection type");
    }
}

class DatabaseConnection implements Connection {
    @Override
    public void connect() {
        System.out.println("Database connected");
    }
    
    @Override
    public void disconnect() {
        System.out.println("Database disconnected");
    }
}

class HttpConnection implements Connection {
    @Override
    public void connect() {
        System.out.println("HTTP connected");
    }
    
    @Override
    public void disconnect() {
        System.out.println("HTTP disconnected");
    }
}

public class FactoryExample {
    public static void main(String[] args) {
        Connection dbConn = Connection.createConnection("DATABASE");
        dbConn.connect(); // Database connected
        
        Connection httpConn = Connection.createConnection("HTTP");
        httpConn.connect(); // HTTP connected
    }
}
```

---

## 7. forEach Loop

### 🔬 Concept Overview
`forEach()` is a default method added to `Iterable` interface in Java 8. It provides an internal iteration mechanism for collections.

**Syntax:**
```java
collection.forEach(action);
```

### 🧪 forEach Examples

```java
public class ForEachExample {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Java", "Python", "JavaScript");
        
        // Traditional for loop
        for (String lang : list) {
            System.out.println(lang);
        }
        
        // forEach with lambda
        list.forEach(lang -> System.out.println(lang));
        
        // forEach with method reference (cleanest)
        list.forEach(System.out::println);
        
        // forEach with complex logic
        list.forEach(lang -> {
            String upper = lang.toUpperCase();
            System.out.println("Language: " + upper);
        });
    }
}
```

### 🔄 forEach on Maps

```java
Map<String, Integer> map = new HashMap<>();
map.put("Alice", 25);
map.put("Bob", 30);
map.put("Charlie", 35);

// forEach on entries
map.forEach((name, age) -> {
    System.out.println(name + " is " + age + " years old");
});

// Output:
// Alice is 25 years old
// Bob is 30 years old
// Charlie is 35 years old
```

---

## 8. Try-With-Resources

### 🔬 Concept Overview
**Try-with-resources** (Java 7+) automatically closes resources that implement `AutoCloseable` or `Closeable` interface, eliminating the need for explicit `finally` blocks.

**Before Java 7:**
```java
BufferedReader br = null;
try {
    br = new BufferedReader(new FileReader("file.txt"));
    String line = br.readLine();
} catch (IOException e) {
    e.printStackTrace();
} finally {
    if (br != null) {
        try {
            br.close();  // Manual closing
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

**After Java 7 (Try-with-resources):**
```java
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    String line = br.readLine();
} catch (IOException e) {
    e.printStackTrace();
}
// BufferedReader is automatically closed
```

### 🧪 Try-With-Resources Examples

#### Multiple Resources
```java
try (
    FileInputStream input = new FileInputStream("input.txt");
    FileOutputStream output = new FileOutputStream("output.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output))
) {
    String line;
    while ((line = reader.readLine()) != null) {
        writer.write(line);
        writer.newLine();
    }
} catch (IOException e) {
    e.printStackTrace();
}
// All resources are automatically closed in reverse order
```

#### Custom AutoCloseable Resource
```java
class DatabaseConnection implements AutoCloseable {
    public DatabaseConnection() {
        System.out.println("Database connection opened");
    }
    
    public void executeQuery(String query) {
        System.out.println("Executing: " + query);
    }
    
    @Override
    public void close() {
        System.out.println("Database connection closed");
    }
}

public class TryWithResourcesExample {
    public static void main(String[] args) {
        try (DatabaseConnection conn = new DatabaseConnection()) {
            conn.executeQuery("SELECT * FROM users");
        } // close() is automatically called here
        
        // Output:
        // Database connection opened
        // Executing: SELECT * FROM users
        // Database connection closed
    }
}
```

### 💡 Pro Tips

1. **No finally needed**: Resources are closed automatically
2. **Exception handling**: If both try block and close() throw exceptions, try block exception is primary
3. **Multiple resources**: Closed in reverse order of declaration
4. **Java 9 enhancement**: Can use effectively final variables

```java
// Java 9+
BufferedReader br = new BufferedReader(new FileReader("file.txt"));
try (br) {  // Can use existing variable if effectively final
    String line = br.readLine();
}
```

---

## 📌 Key Takeaways - Part 2

1. **Functional Interfaces**: Single abstract method interfaces enabling lambda expressions
2. **Lambda Expressions**: Anonymous functions for concise code (parameters) -> expression
3. **Method References**: Shorthand for lambdas: `ClassName::methodName`
4. **Stream API**: Functional-style operations on collections (filter, map, collect)
5. **Default Methods**: Add methods to interfaces without breaking implementations
6. **Static Methods in Interfaces**: Utility methods belonging to interface itself
7. **forEach**: Internal iteration mechanism for collections
8. **Try-with-resources**: Automatic resource management with AutoCloseable

---

## 🎯 Interview Focus Points

**Q: Difference between Functional Interface and regular interface?**
A: Functional interface has exactly one abstract method (SAM), can be implemented with lambda expressions. Regular interfaces can have multiple abstract methods.

**Q: Can lambda access local variables?**
A: Yes, but they must be final or effectively final (not modified after initialization).

**Q: What's the difference between map() and flatMap()?**
A: `map()` transforms each element 1:1. `flatMap()` flattens nested structures (e.g., Stream<Stream<T>> to Stream<T>).

**Q: Why use default methods?**
A: Allows adding new methods to interfaces without breaking existing implementations (backward compatibility).

**Q: When to use method reference vs lambda?**
A: Use method reference when lambda only calls an existing method without additional logic. More readable and concise.

---
# Java Unit 2 - Part 3: Modern Java Features (Java 9-17)

## 📘 Table of Contents
1. [Type Annotations](#type-annotations)
2. [Repeating Annotations](#repeating-annotations)
3. [Java Module System (Java 9)](#java-module-system-java-9)
4. [Diamond Operator with Anonymous Classes](#diamond-operator-with-anonymous-classes)
5. [Local Variable Type Inference (var)](#local-variable-type-inference-var)
6. [Switch Expressions](#switch-expressions)
7. [Text Blocks (Java 15)](#text-blocks-java-15)
8. [Records (Java 16)](#records-java-16)
9. [Sealed Classes (Java 17)](#sealed-classes-java-17)

---

## 1. Type Annotations

### 🔬 Concept Overview
**Type Annotations** (Java 8+) allow annotations to be applied to any use of a type, not just declarations. Before Java 8, annotations were limited to declarations (classes, methods, fields). Now they can be used on type usages for enhanced type checking and compile-time safety.

**Where Type Annotations Can Be Applied:**
- Type casts
- Generic type arguments
- Array creation
- Exception throws clauses
- Implements/extends clauses
- Method return types
- Constructor invocations

### ⚙️ Detailed Mechanics

**Before Java 8 (Declaration Annotations Only):**
```java
@NonNull String name;  // ✅ OK - Declaration annotation
String name;           // No way to annotate the type itself
List<String> list;     // No way to annotate String type argument
```

**After Java 8 (Type Annotations):**
```java
@NonNull String name;              // Declaration annotation
List<@NonNull String> strings;     // Type argument annotation
@NonNull List<String> list;        // Both declaration and type
String @NonNull [] array;          // Array component type
void process() throws @Critical IOException { }  // Exception type
```

### 🧩 When/Where to Use

**Use Cases:**

1. **Null Safety Checking**
```java
@NonNull String getUserName() {
    return this.name;  // Compile error if name can be null
}

List<@NonNull String> names = new ArrayList<>();
names.add(null);  // Static analysis tools can catch this
```

2. **Concurrency Annotations**
```java
@Immutable
public class Configuration {
    private final @NonNull String appName;
    private final @Positive int port;
}
```

3. **SQL Injection Prevention**
```java
void executeQuery(@Untainted String query) {
    // Only accepts sanitized queries
}
```

4. **Unit Annotations**
```java
void setTemperature(@Celsius double temp) {
    // Type system ensures you don't mix Fahrenheit and Celsius
}
```

### 🧪 Complete Example with Custom Type Annotation

```java
import java.lang.annotation.*;

// 1. Define type annotation with @Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@interface NonNull { }

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@interface ReadOnly { }

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@interface NonNegative { }

// 2. Use type annotations
public class TypeAnnotationDemo {
    
    // Field with type annotation
    private @NonNull String username;
    
    // Generic type argument annotation
    private List<@NonNull String> emails;
    
    // Array with component type annotation
    private @NonNull String @NonNull [] roles;
    
    // Method return type annotation
    public @NonNull String getUsername() {
        return username;
    }
    
    // Parameter type annotation
    public void setAge(@NonNegative Integer age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
    }
    
    // Exception type annotation
    public void readFile() throws @Critical IOException {
        // File reading logic
    }
    
    // Type cast annotation
    public void processObject(Object obj) {
        String str = (@NonNull String) obj;
    }
    
    // Constructor invocation
    public void createList() {
        List<@ReadOnly String> readOnlyList = new ArrayList<>();
    }
    
    // Generic type with multiple annotations
    public <@NonNull T> void process(T item) {
        System.out.println(item);
    }
}
```

### 🔄 Type Annotations vs Declaration Annotations

| Aspect | Declaration Annotations | Type Annotations |
|--------|------------------------|------------------|
| **Where Applied** | Class, method, field declarations | Any type usage |
| **Target** | `@Target(ElementType.METHOD)` etc. | `@Target(ElementType.TYPE_USE)` |
| **Purpose** | Metadata for declarations | Type checking, validation |
| **Examples** | `@Override`, `@Deprecated` | `@NonNull`, `@Immutable` |
| **Compile-time checking** | Limited | Enhanced (with tools like Checker Framework) |

### ⚙️ Under the Hood: How Type Annotations Work

**Compile-Time:**
1. Java compiler stores type annotations in `.class` files
2. Stored in new bytecode attribute: `RuntimeVisibleTypeAnnotations`
3. Static analysis tools (Checker Framework, SpotBugs, Error Prone) read these annotations
4. These tools perform additional type checking beyond standard Java type system

**Runtime:**
```java
// Type annotations can be accessed via reflection
Method method = MyClass.class.getMethod("getUserName");
AnnotatedType returnType = method.getAnnotatedReturnType();
Annotation[] annotations = returnType.getAnnotations();

for (Annotation ann : annotations) {
    System.out.println("Annotation: " + ann);
}
```

**Memory/Performance:**
- **Zero runtime overhead** if annotations have `RetentionPolicy.SOURCE`
- Minimal overhead with `RetentionPolicy.CLASS` (stored in bytecode but not loaded)
- Small overhead with `RetentionPolicy.RUNTIME` (loaded into memory)

### 🧩 Real-World Usage: Checker Framework

**Checker Framework** is the most popular tool for leveraging type annotations:

```java
import org.checkerframework.checker.nullness.qual.*;

public class UserService {
    
    // Guarantees non-null return
    public @NonNull User getUserById(@NonNull String id) {
        User user = database.findUser(id);
        if (user == null) {
            // Checker Framework forces you to handle this
            throw new UserNotFoundException(id);
        }
        return user;  // ✅ Safe
    }
    
    // Allows nullable parameter
    public void updateEmail(@Nullable String newEmail) {
        if (newEmail != null) {  // Must check before use
            this.email = newEmail;
        }
    }
    
    // List with non-null elements
    public void processUsers(List<@NonNull User> users) {
        for (User user : users) {
            // No need to check for null, guaranteed by type system
            System.out.println(user.getName());
        }
    }
}
```

**Compile command with Checker Framework:**
```bash
javac -processor org.checkerframework.checker.nullness.NullnessChecker UserService.java
```

If you try to return null or pass null where `@NonNull` is expected, **compilation fails**.

### ⚠️ Common Mistakes

❌ **Confusing declaration and type annotations**
```java
// Declaration annotation (on variable declaration)
@NonNull String name;

// Type annotation (on type itself)
List<@NonNull String> names;

// Both (declaration + type)
@NonNull List<@NonNull String> names;
```

❌ **Using wrong @Target**
```java
@Target(ElementType.FIELD)  // ❌ Won't work on type usages
@interface NonNull { }

@Target(ElementType.TYPE_USE)  // ✅ Works on type usages
@interface NonNull { }
```

❌ **Expecting runtime enforcement without tools**
```java
public @NonNull String getName() {
    return null;  // Java won't prevent this without Checker Framework!
}
```

### 💡 Pro Tips & Professional Practices

1. **Use Static Analysis Tools**: Type annotations alone don't prevent bugs; integrate Checker Framework or similar tools in your build pipeline.

2. **Gradle Integration**:
```gradle
dependencies {
    implementation 'org.checkerframework:checker-qual:3.42.0'
    annotationProcessor 'org.checkerframework:checker:3.42.0'
}
```

3. **Maven Integration**:
```xml
<dependency>
    <groupId>org.checkerframework</groupId>
    <artifactId>checker-qual</artifactId>
    <version>3.42.0</version>
</dependency>
```

4. **Incremental Adoption**: Add `@NonNull` annotations gradually to critical code paths first (public APIs, database layer, DTOs).

5. **IDE Support**: IntelliJ IDEA and Eclipse have plugins for Checker Framework that show warnings in real-time.

6. **Combine with Kotlin Null Safety**: If building new systems, consider Kotlin which has null safety built into the type system.

### 💭 Interview Angle

**Q: What are Type Annotations and how do they differ from regular annotations?**

**A:** Type annotations (Java 8+) can be applied to any use of a type (generic type arguments, casts, array components, exception types), while regular declaration annotations are only applied to declarations (classes, methods, fields). Type annotations use `@Target(ElementType.TYPE_USE)` and enable enhanced compile-time checking through tools like Checker Framework.

**Follow-up: How do type annotations help prevent bugs?**

**A:** They enable pluggable type systems. For example, `@NonNull` annotations with Checker Framework add null safety checks at compile time, catching potential NullPointerExceptions before runtime. The Java compiler doesn't enforce these by default, but static analysis tools do.

**Follow-up: What's the performance impact?**

**A:** Zero runtime impact if using `RetentionPolicy.SOURCE` or `CLASS`. Minimal impact with `RUNTIME`. The checking happens at compile-time via static analysis tools, not at runtime.

---

## 2. Repeating Annotations

### 🔬 Concept Overview
**Repeating Annotations** (Java 8+) allow you to apply the same annotation multiple times to a single declaration. Before Java 8, you had to use container annotations with arrays, which was verbose and error-prone.

**Problem Before Java 8:**
```java
@Schedules({
    @Schedule(day = "Monday"),
    @Schedule(day = "Wednesday")
})
public void task() { }
```

**Solution in Java 8:**
```java
@Schedule(day = "Monday")
@Schedule(day = "Wednesday")
public void task() { }
```

### ⚙️ Detailed Mechanics: How Repeating Annotations Work

**Internally, repeating annotations still use a container**, but the compiler generates it automatically.

**Step-by-Step Creation:**

1. **Create the repeatable annotation** with `@Repeatable`
2. **Create a container annotation** to hold multiple instances
3. **Apply the annotation multiple times**
4. **Access via reflection**

### 🧪 Complete Implementation Example

#### Step 1: Define the Repeatable Annotation

```java
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Schedules.class)  // Points to container annotation
public @interface Schedule {
    String day();
    int hour() default 9;
}
```

**Key Points:**
- `@Repeatable(Schedules.class)` tells compiler which container to use
- `@Retention(RUNTIME)` makes it accessible via reflection
- `@Target(METHOD)` restricts usage to methods (can be TYPE, FIELD, etc.)

#### Step 2: Create Container Annotation

```java
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Schedules {
    Schedule[] value();  // Must be array of repeatable annotation
}
```

**Container Requirements:**
- Must have a `value()` method returning array of repeatable annotation
- Must have same `@Retention` and `@Target` as repeatable annotation
- Must be explicitly defined (compiler doesn't generate it)

#### Step 3: Use the Repeating Annotation

```java
public class TaskScheduler {
    
    @Schedule(day = "Monday", hour = 9)
    @Schedule(day = "Wednesday", hour = 14)
    @Schedule(day = "Friday", hour = 16)
    public void backupDatabase() {
        System.out.println("Backing up database...");
    }
    
    @Schedule(day = "Tuesday", hour = 10)
    public void generateReport() {
        System.out.println("Generating report...");
    }
}
```

#### Step 4: Access via Reflection

```java
import java.lang.reflect.Method;

public class AnnotationProcessor {
    public static void main(String[] args) throws Exception {
        // Get method reference
        Method method = TaskScheduler.class.getMethod("backupDatabase");
        
        // Method 1: Get individual annotations (Java 8+)
        Schedule[] schedules = method.getAnnotationsByType(Schedule.class);
        
        System.out.println("=== Individual Schedules ===");
        for (Schedule schedule : schedules) {
            System.out.println("Day: " + schedule.day() + ", Hour: " + schedule.hour());
        }
        
        // Method 2: Get container annotation (if checking directly)
        if (method.isAnnotationPresent(Schedules.class)) {
            Schedules container = method.getAnnotation(Schedules.class);
            System.out.println("\n=== Via Container ===");
            for (Schedule schedule : container.value()) {
                System.out.println("Day: " + schedule.day() + ", Hour: " + schedule.hour());
            }
        }
        
        // Method 3: Check for single annotation (for methods with only one)
        Method reportMethod = TaskScheduler.class.getMethod("generateReport");
        Schedule[] reportSchedules = reportMethod.getAnnotationsByType(Schedule.class);
        
        System.out.println("\n=== Report Schedules ===");
        for (Schedule schedule : reportSchedules) {
            System.out.println("Day: " + schedule.day() + ", Hour: " + schedule.hour());
        }
    }
}
```

**Output:**
```
=== Individual Schedules ===
Day: Monday, Hour: 9
Day: Wednesday, Hour: 14
Day: Friday, Hour: 16

=== Via Container ===
Day: Monday, Hour: 9
Day: Wednesday, Hour: 14
Day: Friday, Hour: 16

=== Report Schedules ===
Day: Tuesday, Hour: 10
```

### 🔄 Under the Hood: What the Compiler Does

**When you write:**
```java
@Schedule(day = "Monday")
@Schedule(day = "Wednesday")
public void task() { }
```

**The compiler generates (conceptually):**
```java
@Schedules({
    @Schedule(day = "Monday"),
    @Schedule(day = "Wednesday")
})
public void task() { }
```

**Bytecode Representation:**
The annotations are stored in the method's `RuntimeVisibleAnnotations` attribute in the `.class` file.

**Verification:**
```bash
# Compile the class
javac TaskScheduler.java

# View bytecode
javap -v TaskScheduler.class | grep -A 20 "RuntimeVisibleAnnotations"
```

### 🧩 Real-World Use Cases

#### 1. **Scheduled Tasks (Spring Framework)**

```java
@Component
public class ScheduledTasks {
    
    @Scheduled(cron = "0 0 9 * * MON")  // Monday 9 AM
    @Scheduled(cron = "0 0 9 * * WED")  // Wednesday 9 AM
    @Scheduled(cron = "0 0 9 * * FRI")  // Friday 9 AM
    public void sendWeeklyReport() {
        // Send report
    }
}
```

#### 2. **Security Roles (Custom Security Framework)**

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Roles.class)
public @interface RequireRole {
    String value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Roles {
    RequireRole[] value();
}

// Usage
@RestController
public class AdminController {
    
    @RequireRole("ADMIN")
    @RequireRole("SUPER_ADMIN")
    @GetMapping("/sensitive-data")
    public String getSensitiveData() {
        return "Sensitive data";
    }
}
```

#### 3. **Validation Constraints**

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(ValidEmails.class)
public @interface ValidEmail {
    String domain();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidEmails {
    ValidEmail[] value();
}

// Usage
public class User {
    
    @ValidEmail(domain = "company.com")
    @ValidEmail(domain = "subsidiary.com")
    private String email;
}
```

#### 4. **API Versioning**

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ApiVersions.class)
public @interface ApiVersion {
    String value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiVersions {
    ApiVersion[] value();
}

// Usage
@RestController
public class UserController {
    
    @ApiVersion("v1")
    @ApiVersion("v2")
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
}
```

### ⚙️ Performance Considerations

**Compile-Time:**
- Minimal overhead; compiler generates container annotation automatically
- No impact on compilation speed

**Runtime:**
- Annotations with `RUNTIME` retention are loaded into memory
- Reflection operations (`getAnnotationsByType()`) have negligible overhead for typical use cases
- Repeated annotations are stored as single container annotation in bytecode (no duplication)

**Memory Footprint:**
```java
// This:
@Schedule(day = "Mon")
@Schedule(day = "Wed")

// Takes same memory as:
@Schedules({
    @Schedule(day = "Mon"),
    @Schedule(day = "Wed")
})
```

### ⚠️ Common Mistakes

❌ **Forgetting to create container annotation**
```java
@Repeatable(Schedules.class)  // Compiler error if Schedules doesn't exist
public @interface Schedule {
    String day();
}
```

❌ **Mismatched @Retention or @Target**
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Schedules.class)
public @interface Schedule { }

@Retention(RetentionPolicy.SOURCE)  // ❌ Must match repeatable annotation
@Target(ElementType.TYPE)            // ❌ Must match repeatable annotation
public @interface Schedules {
    Schedule[] value();
}
```

❌ **Wrong container method signature**
```java
public @interface Schedules {
    List<Schedule> value();  // ❌ Must be array, not List
}

public @interface Schedules {
    Schedule[] schedules();  // ❌ Must be named 'value()'
}

public @interface Schedules {
    Schedule[] value();      // ✅ Correct
}
```

❌ **Using wrong reflection method**
```java
// ❌ This may not work with repeating annotations
Schedule schedule = method.getAnnotation(Schedule.class);

// ✅ This works correctly
Schedule[] schedules = method.getAnnotationsByType(Schedule.class);
```

### 💡 Pro Tips & Professional Practices

1. **Always use `getAnnotationsByType()` for repeating annotations**:
```java
// ✅ Works for both single and multiple annotations
Schedule[] schedules = method.getAnnotationsByType(Schedule.class);

// ❌ Only works if exactly one annotation present
Schedule schedule = method.getAnnotation(Schedule.class);
```

2. **Provide meaningful defaults** to reduce verbosity:
```java
@interface Schedule {
    String day();
    int hour() default 9;      // Sensible default
    int minute() default 0;    // Sensible default
    String timezone() default "UTC";
}
```

3. **Document container annotation** (even though users rarely see it):
```java
/**
 * Container annotation for {@link Schedule}.
 * Users should apply @Schedule multiple times instead of using this directly.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Schedules {
    Schedule[] value();
}
```

4. **Use in Spring Framework** (built-in support):
```java
@Scheduled(cron = "0 0 9 * * MON")
@Scheduled(cron = "0 0 9 * * FRI")
public void weeklyTask() { }
```

5. **Custom annotation processors** for build-time validation:
```java
// Create custom annotation processor
@SupportedAnnotationTypes("com.example.Schedule")
public class ScheduleProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, 
                          RoundEnvironment roundEnv) {
        // Validate schedules at compile time
        return true;
    }
}
```

### 📚 Extra Insights: Design Philosophy

**Why Repeating Annotations?**

Before Java 8, the only way to have multiple annotations of the same type was:
```java
@Schedules({@Schedule(...), @Schedule(...)})
```

This was:
- ❌ Verbose
- ❌ Less readable
- ❌ Inconsistent with how developers think ("just add another annotation")

Java 8 made it syntactic sugar:
```java
@Schedule(...)
@Schedule(...)
```

**But internally it's still the container pattern!** The compiler just handles it for you.

**Language Design Trade-off:**
- **Pro**: Cleaner syntax, better readability
- **Con**: Requires container annotation boilerplate
- **Decision**: Worth it for improved developer experience

### 💭 Interview Angle

**Q: How do repeating annotations work in Java?**

**A:** Repeating annotations (Java 8+) allow applying the same annotation multiple times to a declaration. You mark an annotation with `@Repeatable(ContainerClass.class)` and create a container annotation with a `value()` method returning an array of the repeatable annotation. The compiler automatically wraps multiple annotations in the container at compile time. You access them via reflection using `getAnnotationsByType()`.

**Follow-up: What happens under the hood when you use repeating annotations?**

**A:** When you write `@Schedule(day="Mon") @Schedule(day="Wed")`, the compiler generates bytecode equivalent to `@Schedules({@Schedule(day="Mon"), @Schedule(day="Wed")})`. The container pattern is still used internally; repeating annotations are syntactic sugar that improves readability.

**Follow-up: What are the requirements for a container annotation?**

**A:** The container must:
1. Have a `value()` method returning an array of the repeatable annotation
2. Have the same `@Retention` and `@Target` as the repeatable annotation
3. Be explicitly defined (not auto-generated by compiler)

**Follow-up: Why use `getAnnotationsByType()` instead of `getAnnotation()`?**

**A:** `getAnnotation()` only works if exactly one annotation is present. `getAnnotationsByType()` handles both single and multiple annotations correctly, automatically unwrapping the container if present. It's the recommended approach for repeating annotations.

---

## 3. Java Module System (Java 9)

### 🔬 Concept Overview
The **Java Platform Module System (JPMS)**, introduced in Java 9 as **Project Jigsaw**, is a major architectural change that organizes code into modules—self-contained units with explicit dependencies and encapsulation boundaries.

**Before Java 9:**
- **Classpath Hell**: Conflicting versions of libraries, unclear dependencies
- **No Encapsulation**: All public classes were accessible (can't hide internal APIs)
- **Monolithic JRE**: Entire Java runtime was one huge package (100+ MB)
- **Security Issues**: Reflection could access everything

**After Java 9:**
- **Module System**: Explicit dependencies via `module-info.java`
- **Strong Encapsulation**: Packages can be internal (not exported)
- **Modular JDK**: JDK split into ~90 modules (can create custom JRE with only needed modules)
- **Reliable Configuration**: Missing dependencies detected at compile-time/startup

### ⚙️ Detailed Mechanics: What is a Module?

A **module** is a collection of packages with:
1. **A unique name** (e.g., `com.example.myapp`)
2. **Explicit dependencies** (`requires` other modules)
3. **Exported packages** (`exports` to make packages accessible)
4. **Services** (optional: `provides` and `uses`)

**Module Descriptor (`module-info.java`):**
```java
module com.example.myapp {
    requires java.sql;              // This module needs java.sql
    requires transitive java.logging;  // Transitive: users of myapp get logging too
    
    exports com.example.myapp.api;  // Make this package public
    // com.example.myapp.internal stays private!
    
    opens com.example.myapp.model;  // Allow reflection (for frameworks like Hibernate)
    
    provides com.example.myapp.api.Service 
        with com.example.myapp.impl.ServiceImpl;  // Service Provider Interface
    
    uses com.example.myapp.api.Plugin;  // This module uses plugins
}
```

### 🧩 Module Directive Keywords

| Keyword | Purpose | Example |
|---------|---------|---------|
| `module` | Declares a module | `module com.myapp { }` |
| `requires` | Declares dependency on another module | `requires java.sql;` |
| `requires transitive` | Transitive dependency (users get it automatically) | `requires transitive java.logging;` |
| `exports` | Makes package accessible to other modules | `exports com.myapp.api;` |
| `exports ... to` | Exports package only to specific modules | `exports com.myapp.internal to com.myapp.test;` |
| `opens` | Allows deep reflection on package | `opens com.myapp.model;` |
| `opens ... to` | Opens package only to specific modules | `opens com.myapp.model to org.hibernate;` |
| `uses` | Declares module uses a service | `uses com.myapp.api.Service;` |
| `provides ... with` | Implements a service | `provides Service with ServiceImpl;` |

### 🧪 Step-by-Step: Creating a Java Module

#### Project Structure
```
my-module-project/
├── src/
│   └── com.javatpoint/
│       ├── module-info.java
│       └── com/
│           └── javatpoint/
│               └── Hello.java
└── mods/  (compiled output)
```

#### Step 1: Create Directory Structure
```bash
mkdir -p src/com.javatpoint/com/javatpoint
```

#### Step 2: Write Module Descriptor (`module-info.java`)
```java
// File: src/com.javatpoint/module-info.java
module com.javatpoint {
    // Module is self-contained; no dependencies needed for this example
    // exports com.javatpoint;  // Uncomment if you want to export the package
}
```

**Key Points:**
- Must be in root of module source directory
- Must be named exactly `module-info.java`
- Module name should follow reverse-domain naming convention

#### Step 3: Write Java Source Code
```java
// File: src/com.javatpoint/com/javatpoint/Hello.java
package com.javatpoint;

public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello from the Java module system!");
    }
}
```

#### Step 4: Compile the Module
```bash
javac -d mods/com.javatpoint \
      --module-source-path src \
      --module com.javatpoint
```

**Explanation:**
- `-d mods/com.javatpoint`: Output directory for compiled classes
- `--module-source-path src`: Root directory containing modules
- `--module com.javatpoint`: Specifies which module to compile

**Generated Structure:**
```
mods/
└── com.javatpoint/
    ├── module-info.class
    └── com/
        └── javatpoint/
            └── Hello.class
```

#### Step 5: Run the Module
```bash
java --module-path mods \
     --module com.javatpoint/com.javatpoint.Hello
```

**Explanation:**
- `--module-path mods`: Where to find modules (replaces old `-classpath`)
- `--module com.javatpoint/com.javatpoint.Hello`: `moduleName/mainClass`

**Output:**
```
Hello from the Java module system!
```

### 🧩 Multi-Module Project Example

#### Project Structure
```
multi-module-app/
├── src/
│   ├── com.example.api/        (API module)
│   │   ├── module-info.java
│   │   └── com/example/api/
│   │       └── Service.java
│   ├── com.example.impl/       (Implementation module)
│   │   ├── module-info.java
│   │   └── com/example/impl/
│   │       └── ServiceImpl.java
│   └── com.example.app/        (Application module)
│       ├── module-info.java
│       └── com/example/app/
│           └── Main.java
└── mods/
```

#### Module 1: API Module
```java
// File: src/com.example.api/module-info.java
module com.example.api {
    exports com.example.api;  // Make API accessible
}
```

```java
// File: src/com.example.api/com/example/api/Service.java
package com.example.api;

public interface Service {
    String getMessage();
}
```

#### Module 2: Implementation Module
```java
// File: src/com.example.impl/module-info.java
module com.example.impl {
    requires com.example.api;  // Depends on API module
    exports com.example.impl;
}
```

```java
// File: src/com.example.impl/com/example/impl/ServiceImpl.java
package com.example.impl;

import com.example.api.Service;

public class ServiceImpl implements Service {
    @Override
    public String getMessage() {
        return "Hello from ServiceImpl!";
    }
}
```

#### Module 3: Application Module
```java
// File: src/com.example.app/module-info.java
module com.example.app {
    requires com.example.api;
    requires com.example.impl;
}
```

```java
// File: src/com.example.app/com/example/app/Main.java
package com.example.app;

import com.example.api.Service;
import com.example.impl.ServiceImpl;

public class Main {
    public static void main(String[] args) {
        Service service = new ServiceImpl();
        System.out.println(service.getMessage());
    }
}
```

#### Compile All Modules
```bash
# Compile API module
javac -d mods/com.example.api \
      --module-source-path src \
      --module com.example.api

# Compile Implementation module
javac -d mods/com.example.impl \
      --module-source-path src \
      --module-path mods \
      --module com.example.impl

# Compile Application module
javac -d mods/com.example.app \
      --module-source-path src \
      --module-path mods \
      --module com.example.app
```

#### Run the Application
```bash
java --module-path mods \
     --module com.example.app/com.example.app.Main
```

**Output:**
```
Hello from ServiceImpl!
```

### ⚙️ Under the Hood: How Modules Work

**1. Module Resolution (Startup):**
```
1. JVM loads main module (com.example.app)
2. Reads module-info.class
3. Resolves dependencies recursively (com.example.api, com.example.impl)
4. Builds module graph
5. Fails fast if dependencies missing or cyclic dependencies found
```

**2. Strong Encapsulation:**
```java
// Module A
module com.moduleA {
    exports com.moduleA.api;      // Public
    // com.moduleA.internal         // Private (not exported)
}
```

**From Module B:**
```java
import com.moduleA.api.PublicClass;     // ✅ OK
import com.moduleA.internal.InternalClass;  // ❌ Compile error
```

**Even reflection is blocked:**
```java
Class<?> clazz = Class.forName("com.moduleA.internal.InternalClass");
// Throws IllegalAccessException (unless module "opens" the package)
```

**3. JDK Modularization:**

The JDK itself is split into modules:
```
java.base          (String, Object, System, etc.)
java.sql           (JDBC)
java.xml           (XML parsing)
java.logging       (Logging APIs)
java.desktop       (Swing, AWT)
...~90 modules total
```

**View JDK modules:**
```bash
java --list-modules
```

**Custom Runtime Image (jlink):**
```bash
jlink --module-path mods:$JAVA_HOME/jmods \
      --add-modules com.example.app \
      --output custom-jre \
      --launcher myapp=com.example.app/com.example.app.Main
```

This creates a **minimal JRE** (~30 MB) with only required modules!

### 🔄 Module Types

| Type | Description | Example Use Case |
|------|-------------|------------------|
| **Named Module** | Has `module-info.java` | Modern Java applications |
| **Automatic Module** | JAR on module path without `module-info.java` | Legacy libraries (name derived from JAR filename) |
| **Unnamed Module** | Code on classpath | Legacy applications |

**Compatibility Matrix:**

| Can Access → | Named Module | Automatic Module | Unnamed Module |
|--------------|--------------|------------------|----------------|
| **Named Module** | ✅ (if required/exported) | ✅ (if required) | ❌ |
| **Automatic Module** | ✅ (if exported) | ✅ | ✅ |
| **Unnamed Module** | ❌ (unless opened) | ✅ | ✅ |

### ⚠️ Common Mistakes

❌ **Forgetting to export packages**
```java
module com.example {
    // exports com.example.api;  // Forgot to export!
}

// Other modules can't access com.example.api classes
```

❌ **Circular dependencies**
```java
// Module A
module com.moduleA {
    requires com.moduleB;  // A depends on B
}

// Module B
module com.moduleB {
    requires com.moduleA;  // B depends on A → Compile error!
}
```

❌ **Split packages** (same package in multiple modules)
```java
// Module A
package com.example.util;  // ❌ 

// Module B
package com.example.util;  // ❌ Same package → Conflict!
```

❌ **Not using `requires transitive` for API modules**
```java
// API module
module com.example.api {
    requires java.sql;  // ❌ Users of this module won't get java.sql
}

// ✅ Correct
module com.example.api {
    requires transitive java.sql;  // Users automatically get java.sql
}
```

### 💡 Pro Tips & Professional Practices

1. **Start with monolithic unnamed module**, then refactor incrementally:
```java
// Phase 1: Convert to single module
module com.myapp {
    requires java.sql;
    requires java.logging;
}

// Phase 2: Split into API and implementation modules
// Phase 3: Create domain-specific modules
```

2. **Use `jdeps` to analyze dependencies before modularizing**:
```bash
jdeps --generate-module-info . myapp.jar
```

3. **For libraries, provide both modular JAR and module-info**:
```xml
<!-- Maven: Multi-release JAR with module-info for Java 9+ -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <release>8</release>
    </configuration>
    <executions>
        <execution>
            <id>compile-java9</id>
            <goals><goal>compile</goal></goals>
            <configuration>
                <release>9</release>
                <compileSourceRoots>
                    <compileSourceRoot>${project.basedir}/src/main/java9</compileSourceRoot>
                </compileSourceRoots>
            </configuration>
        </execution>
    </executions>
</plugin>
```

4. **Testing modules**: Use `opens` for test frameworks:
```java
module com.example.app {
    exports com.example.app.api;
    
    // Allow JUnit to access internal packages via reflection
    opens com.example.app.internal to org.junit.platform.commons;
}
```

5. **Spring Boot with modules** (Spring 5+ supports JPMS):
```java
module com.example.springapp {
    requires spring.context;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    
    opens com.example.springapp to spring.core;  // Allow reflection
    exports com.example.springapp.api;
}
```

### 📚 Extra Insights: Why JPMS Took So Long

**History:**
- **2005**: First discussions about Java modularization
- **2011**: Project Jigsaw formally started
- **2014**: Delayed from Java 8 to Java 9
- **2017**: Finally released in Java 9

**Why the delay?**
1. **Backward compatibility**: Had to work with millions of existing applications
2. **Ecosystem impact**: Maven, Gradle, IDEs all needed updates
3. **Reflection challenges**: Many frameworks relied on unrestricted reflection
4. **Internal JDK dependencies**: JDK itself had tangled dependencies that needed refactoring

**Result:**
- Huge undertaking (6000+ issues tracked)
- Completely rewrote JDK internals
- Most significant Java change since Java 5 (generics)

### 💭 Interview Angle

**Q: What is the Java Module System and why was it introduced?**

**A:** The Java Platform Module System (JPMS), introduced in Java 9, organizes code into modules—self-contained units with explicit dependencies declared in `module-info.java`. It solves classpath hell (conflicting library versions), enables strong encapsulation (internal packages can't be accessed even via reflection), and allows creating custom JREs with only needed modules (reduced from 100+ MB to ~30 MB).

**Follow-up: How does strong encapsulation work?**

**A:** A module must explicitly `export` packages to make them accessible to other modules. Non-exported packages are completely inaccessible, even via reflection (unless the module `opens` them). This is enforced by the JVM at runtime, not just compile-time. For example, JDK internals like `sun.misc.Unsafe` are no longer accessible by default.

**Follow-up: What's the difference between `requires` and `requires transitive`?**

**A:** `requires` declares a dependency for the current module only. `requires transitive` makes the dependency available to any module that requires your module. It's used when your exported API types depend on another module's types—users of your module automatically get the transitive dependency without explicitly declaring it.

**Follow-up: How do you migrate a legacy application to modules?**

**A:** 
1. Analyze dependencies with `jdeps --generate-module-info`
2. Start with automatic modules (JARs on module path without module-info)
3. Create unnamed module (everything on classpath still works)
4. Gradually add `module-info.java` to application modules
5. Use `--add-opens` and `--add-exports` JVM flags for framework compatibility during transition
6. Finally, modularize all components

**Follow-up: What happens if there's a circular dependency between modules?**

**A:** The module system rejects circular dependencies at compile-time or startup. If Module A `requires` Module B, and Module B `requires` Module A, compilation fails with an error. This is by design—circular dependencies indicate poor architecture. The solution is to extract common dependencies into a third module or refactor the design.

---

## 4. Diamond Operator with Anonymous Classes

### 🔬 Concept Overview
The **Diamond Operator (`<>`)** was introduced in Java 7 to eliminate redundant generic type parameters when creating objects. Java 9 extended diamond operator support to **anonymous inner classes**.

**Java 6 (Before Diamond Operator):**
```java
List<String> list = new ArrayList<String>();  // Redundant type
Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();  // Very verbose
```

**Java 7 (Diamond Operator for Regular Classes):**
```java
List<String> list = new ArrayList<>();  // Type inferred
Map<String, List<Integer>> map = new HashMap<>();  // Much cleaner
```

**Java 9 (Diamond Operator for Anonymous Classes):**
```java
// Before Java 9: ❌ Compile error
List<String> list = new ArrayList<String>() {
    @Override
    public boolean add(String e) {
        System.out.println("Adding: " + e);
        return super.add(e);
    }
};

// Java 9+: ✅ Works!
List<String> list = new ArrayList<>() {  // Diamond operator with anonymous class
    @Override
    public boolean add(String e) {
        System.out.println("Adding: " + e);
        return super.add(e);
    }
};
```

### ⚙️ Detailed Mechanics: Why This Matters

**Problem in Java 7-8:**

Anonymous classes couldn't use diamond operator because the compiler couldn't infer the type parameter in the context of class extension.

```java
// Java 7-8: Must specify type explicitly
abstract class Box<T> {
    abstract void put(T item);
}

Box<String> box = new Box<String>() {  // ❌ Can't use <>
    @Override
    void put(String item) {
        System.out.println(item);
    }
};
```

**Java 9 Solution:**

Enhanced type inference algorithm that can infer type parameters for anonymous classes.

```java
Box<String> box = new Box<>() {  // ✅ Diamond operator works!
    @Override
    void put(String item) {
        System.out.println(item);
    }
};
```

### 🧪 Complete Examples

#### Example 1: Anonymous ArrayList with Diamond Operator

```java
import java.util.ArrayList;
import java.util.List;

public class DiamondOperatorDemo {
    public static void main(String[] args) {
        // ❌ Java 7-8: Required explicit type
        List<String> oldWay = new ArrayList<String>() {
            @Override
            public boolean add(String e) {
                System.out.println("Adding: " + e);
                return super.add(e);
            }
        };
        
        // ✅ Java 9+: Diamond operator allowed
        List<String> newWay = new ArrayList<>() {
            @Override
            public boolean add(String e) {
                System.out.println("Adding: " + e);
                return super.add(e);
            }
        };
        
        newWay.add("Java");
        newWay.add("Kotlin");
        
        System.out.println("List: " + newWay);
    }
}
```

**Output:**
```
Adding: Java
Adding: Kotlin
List: [Java, Kotlin]
```

#### Example 2: Custom Generic Class with Anonymous Implementation

```java
abstract class Handler<T> {
    abstract void handle(T data);
}

public class GenericAnonymousClass {
    public static void main(String[] args) {
        // ❌ Java 8: Must specify type
        Handler<String> handler1 = new Handler<String>() {
            @Override
            void handle(String data) {
                System.out.println("Handling: " + data.toUpperCase());
            }
        };
        
        // ✅ Java 9+: Diamond operator
        Handler<String> handler2 = new Handler<>() {
            @Override
            void handle(String data) {
                System.out.println("Handling: " + data.toUpperCase());
            }
        };
        
        handler2.handle("hello");  // Output: Handling: HELLO
        
        // Multiple type parameters
        abstract class Pair<K, V> {
            abstract V get(K key);
        }
        
        Pair<String, Integer> pair = new Pair<>() {  // ✅ Java 9
            @Override
            Integer get(String key) {
                return key.length();
            }
        };
        
        System.out.println(pair.get("Java"));  // Output: 4
    }
}
```

#### Example 3: Real-World Scenario - Event Listeners

```java
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventHandlerExample {
    public static void main(String[] args) {
        JButton button = new JButton("Click Me");
        
        // ❌ Java 8: Redundant type (though ActionListener isn't generic)
        // But for generic interfaces:
        
        interface EventHandler<T> {
            void onEvent(T event);
        }
        
        // ✅ Java 9+: Clean with diamond operator
        EventHandler<String> handler = new EventHandler<>() {
            @Override
            public void onEvent(String event) {
                System.out.println("Event received: " + event);
            }
        };
        
        handler.onEvent("Button Clicked");
    }
}
```

### ⚙️ Under the Hood: Type Inference

**How the compiler infers types:**

```java
List<String> list = new ArrayList<>() {
    // ...
};
```

**Compiler's reasoning:**
1. Left-hand side declares `List<String>`
2. Right-hand side is anonymous subclass of `ArrayList`
3. `ArrayList` has type parameter `<E>`
4. Diamond operator means: "infer `<E>` from context"
5. Context requires `List<String>`, so `E = String`
6. Result: `ArrayList<String>`

**Bytecode:**
```java
// Compiled to (conceptually):
List<String> list = new ArrayList$1<String>();

// Where ArrayList$1 is generated anonymous class:
class ArrayList$1 extends ArrayList<String> {
    @Override
    public boolean add(String e) {
        // ...
    }
}
```

**Key Point:** The diamond operator is pure compile-time syntactic sugar. The bytecode is identical whether you use `<>` or explicit type.

### ⚠️ Common Mistakes

❌ **Using diamond operator with non-generic classes**
```java
Object obj = new Object<>() {};  // ❌ Compile error: Object is not generic
```

❌ **Expecting runtime type information**
```java
List<String> list = new ArrayList<>() {};
// ❌ Type erasure: Can't check instanceof List<String> at runtime
// ✅ Can check: instanceof List
```

❌ **Confusing with lambda expressions**
```java
// This is anonymous class with diamond operator
Comparator<String> comp1 = new Comparator<>() {
    @Override
    public int compare(String s1, String s2) {
        return s1.length() - s2.length();
    }
};

// This is lambda (preferred for functional interfaces)
Comparator<String> comp2 = (s1, s2) -> s1.length() - s2.length();
```

### 💡 Pro Tips & Professional Practices

1. **Prefer lambda expressions over anonymous classes** for functional interfaces:
```java
// ❌ Verbose (even with diamond operator)
Runnable r1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Running");
    }
};

// ✅ Preferred
Runnable r2 = () -> System.out.println("Running");
```

2. **Use anonymous classes with diamond operator when:**
- Implementing non-functional interface (multiple methods)
- Need to maintain state
- Overriding multiple methods
```java
abstract class Worker<T> {
    abstract T doWork();
    abstract void cleanup();
}

Worker<String> worker = new Worker<>() {  // Good use case
    @Override
    String doWork() {
        return "Work done";
    }
    
    @Override
    void cleanup() {
        System.out.println("Cleaning up");
    }
};
```

3. **IDE warnings**: Modern IDEs suggest replacing anonymous classes with lambdas when possible.

4. **Readability**: For complex generic types, diamond operator significantly improves readability:
```java
// ❌ Hard to read
Map<String, List<Pair<Integer, String>>> map1 = 
    new HashMap<String, List<Pair<Integer, String>>>() {};

// ✅ Much cleaner
Map<String, List<Pair<Integer, String>>> map2 = new HashMap<>() {};
```

### 💭 Interview Angle

**Q: What is the diamond operator and when was it extended to anonymous classes?**

**A:** The diamond operator (`<>`) was introduced in Java 7 to eliminate redundant generic type declarations, allowing the compiler to infer types from context (e.g., `List<String> list = new ArrayList<>()`). Java 9 extended this to anonymous inner classes, which previously required explicit type parameters. Now you can write `new ArrayList<>() { ... }` instead of `new ArrayList<String>() { ... }`.

**Follow-up: Why couldn't anonymous classes use diamond operator in Java 7-8?**

**A:** The Java 7 type inference algorithm couldn't handle the complexity of inferring type parameters in the context of anonymous class creation. Java 9 enhanced the inference algorithm to support this scenario, analyzing the target type on the left-hand side to infer the type parameter for the anonymous class.

**Follow-up: Does the diamond operator affect runtime performance?**

**A:** No, it's purely compile-time syntactic sugar. The compiler generates identical bytecode whether you use `<>` or explicit types. Type erasure removes all generic type information at runtime anyway.

**Follow-up: When would you use an anonymous class with diamond operator instead of a lambda?**

**A:** Use anonymous classes when:
1. Implementing an interface/class with multiple abstract methods (lambdas only work with functional interfaces)
2. Need to maintain state across method calls
3. Overriding non-abstract methods
4. Need to access instance variables or methods of the anonymous class itself

For functional interfaces (single abstract method), always prefer lambdas for conciseness.

---

## 5. Local Variable Type Inference (`var`)

### 🔬 Concept Overview
**Local Variable Type Inference** (`var` keyword, Java 10+) allows you to declare local variables without explicitly specifying their type. The compiler infers the type from the initializer expression.

**Before Java 10:**
```java
String message = "Hello";
ArrayList<String> list = new ArrayList<String>();
Map<String, List<Integer>> complexMap = new HashMap<String, List<Integer>>();
```

**Java 10+ with `var`:**
```java
var message = "Hello";  // Type inferred as String
var list = new ArrayList<String>();  // Type inferred as ArrayList<String>
var complexMap = new HashMap<String, List<Integer>>();  // Type inferred
```

**Key Characteristics:**
- **Only for local variables** (not fields, method parameters, or return types)
- **Must be initialized** at declaration
- **Type is inferred at compile-time** (not dynamic typing!)
- **Strongly typed** after inference (can't reassign different type)

### ⚙️ Detailed Mechanics: How `var` Works

**Compile-Time Type Inference:**

```java
var name = "John";  // Compiler infers: String name = "John";
```

**Under the hood:**
1. Compiler analyzes initializer expression (`"John"`)
2. Determines type (`String`)
3. Replaces `var` with inferred type in bytecode
4. Generates identical bytecode as explicit type

**Proof: Bytecode is Identical**

```java
// Source code:
String explicit = "Hello";
var inferred = "Hello";

// Both compile to same bytecode:
// LDC "Hello"
// ASTORE 1  (store in local variable 1)
```

**Key Point:** `var` is NOT dynamic typing (like JavaScript). It's **static typing with type inference**.

```java
var x = 10;      // Type: int (fixed at compile-time)
x = "Hello";     // ❌ Compile error: incompatible types
```

### 🧩 Where `var` Can Be Used

| Context | Allowed? | Example |
|---------|----------|---------|
| **Local variables** | ✅ Yes | `var name = "John";` |
| **For-loop index** | ✅ Yes | `for (var i = 0; i < 10; i++)` |
| **Enhanced for-loop** | ✅ Yes | `for (var item : list)` |
| **Try-with-resources** | ✅ Yes | `try (var br = new BufferedReader(...))` |
| **Static blocks** | ✅ Yes | `static { var x = 10; }` |
| **Instance initializer blocks** | ✅ Yes | `{ var x = 10; }` |
| **Lambda parameters** | ✅ Yes (Java 11+) | `(var x, var y) -> x + y` |
| **Method parameters** | ❌ No | `void method(var x) { }` // Error |
| **Method return types** | ❌ No | `var method() { }` // Error |
| **Field declarations** | ❌ No | `class C { var x = 10; }` // Error |
| **Constructor parameters** | ❌ No | `C(var x) { }` // Error |

### 🧪 Complete Examples

#### Example 1: Basic Local Variable Type Inference

```java
public class VarBasicsDemo {
    public static void main(String[] args) {
        // Primitive types
        var num = 10;              // int
        var decimal = 10.5;        // double
        var flag = true;           // boolean
        var character = 'A';       // char
        
        // Reference types
        var message = "Hello";     // String
        var list = new ArrayList<String>();  // ArrayList<String>
        var map = new HashMap<String, Integer>();  // HashMap<String, Integer>
        
        // Generic types
        var numbers = List.of(1, 2, 3);  // List<Integer> (immutable)
        
        // Type verification
        System.out.println("num type: " + ((Object)num).getClass().getName());
        System.out.println("message type: " + message.getClass().getName());
        System.out.println("list type: " + list.getClass().getName());
    }
}
```

**Output:**
```
num type: java.lang.Integer
message type: java.lang.String
list type: java.util.ArrayList
```

#### Example 2: Allowed Use Cases

```java
public class VarUseCases {
    public static void main(String[] args) {
        // 1. Local variable
        var name = "Alice";
        
        // 2. For-loop index
        for (var i = 0; i < 5; i++) {
            System.out.println(i);
        }
        
        // 3. Enhanced for-loop
        var numbers = List.of(1, 2, 3, 4, 5);
        for (var num : numbers) {
            System.out.println(num);
        }
        
        // 4. Try-with-resources
        try (var reader = new BufferedReader(new FileReader("file.txt"))) {
            var line = reader.readLine();
            System.out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // 5. Static block
        class Example {
            static {
                var x = 100;
                System.out.println("Static block: " + x);
            }
        }
        
        // 6. Instance initializer block
        class Another {
            {
                var y = 200;
                System.out.println("Instance initializer: " + y);
            }
        }
        
        // 7. Return value from method
        var result = calculateSum(10, 20);
        System.out.println("Result: " + result);
        
        // 8. Lambda parameters (Java 11+)
        BiFunction<Integer, Integer, Integer> add = (var a, var b) -> a + b;
        System.out.println("Sum: " + add.apply(5, 3));
    }
    
    static int calculateSum(int a, int b) {
        return a + b;
    }
}
```

#### Example 3: Complex Type Inference

```java
public class ComplexVarExample {
    public static void main(String[] args) {
        // Complex generic type
        var complexMap = new HashMap<String, List<Map<Integer, String>>>();
        
        // Without var (verbose):
        HashMap<String, List<Map<Integer, String>>> explicit = 
            new HashMap<String, List<Map<Integer, String>>>();
        
        // Array
        var array = new int[]{1, 2, 3};
        
        // Anonymous class
        var handler = new Object() {
            void handleEvent() {
                System.out.println("Event handled");
            }
        };
        handler.handleEvent();
        
        // Method chaining
        var result = "hello world"
            .toUpperCase()
            .replace("WORLD", "JAVA")
            .substring(0, 10);
        System.out.println(result);  // HELLO JAVA
    }
}
```

### ⚠️ Where `var` CANNOT Be Used

#### 1. Uninitialized Variable
```java
var x;  // ❌ Compile error: cannot infer type without initializer
x = 10;
```

#### 2. Initialized to `null`
```java
var x = null;  // ❌ Compile error: cannot infer type from null
```

#### 3. Lambda Expression Without Target Type
```java
var lambda = () -> System.out.println("Hello");  // ❌ Error: cannot infer
Runnable r = () -> System.out.println("Hello");  // ✅ OK
```

#### 4. Method Reference Without Target Type
```java
var ref = System.out::println;  // ❌ Error
Consumer<String> ref = System.out::println;  // ✅ OK
```

#### 5. Array Initializer Without Type
```java
var array = {1, 2, 3};  // ❌ Error
var array = new int[]{1, 2, 3};  // ✅ OK
```

#### 6. Fields (Class Members)
```java
class MyClass {
    var name = "John";  // ❌ Error: var not allowed for fields
}
```

#### 7. Method Parameters
```java
void method(var param) {  // ❌ Error
    // ...
}
```

#### 8. Method Return Type
```java
var method() {  // ❌ Error
    return 10;
}
```

#### 9. Constructor Parameters
```java
class MyClass {
    MyClass(var param) {  // ❌ Error
        // ...
    }
}
```

### ⚙️ Performance & Memory Implications

**Compile-Time:**
- **No overhead**: Type inference happens during compilation
- Slightly increases compilation time (negligible)

**Runtime:**
- **Zero overhead**: Bytecode is identical to explicit types
- No runtime type checking or dynamic dispatch

**Memory:**
- **Same memory footprint**: `var` and explicit type produce identical objects

**Benchmark:**
```java
// Both have identical performance:
String explicit = "Hello";
var inferred = "Hello";
```

### 💡 Pro Tips & Professional Practices

#### 1. Use `var` When Type is Obvious
```java
// ✅ Good: Type is clear from right-hand side
var name = "Alice";
var numbers = List.of(1, 2, 3);
var file = new File("data.txt");

// ❌ Bad: Type is not obvious
var result = calculate();  // What does calculate() return?
```

#### 2. Avoid `var` for Numeric Types (Ambiguity)
```java
// ❌ Ambiguous: int or long?
var count = 100;  // int (but easy to forget)

// ✅ Better: Be explicit
long count = 100L;
```

#### 3. Use `var` for Complex Generic Types
```java
// ❌ Verbose
Map<String, List<Pair<Integer, String>>> map1 = new HashMap<String, List<Pair<Integer, String>>>();

// ✅ Much cleaner
var map2 = new HashMap<String, List<Pair<Integer, String>>>();
```

#### 4. Diamond Operator with `var`
```java
// ❌ Loses type information
var list1 = new ArrayList<>();  // Type: ArrayList<Object>

// ✅ Preserves type
var list2 = new ArrayList<String>();  // Type: ArrayList<String>

// ✅ Alternative: Factory method
var list3 = List.of("a", "b");  // Type: List<String>
```

#### 5. Readable Variable Names with `var`
```java
// ❌ Bad: var makes unclear code worse
var d = getData();

// ✅ Good: Clear name compensates for var
var userData = getUserData();
```

#### 6. Avoid `var` in Public APIs (Internal Use Only)
```java
// ❌ Bad: Hard to understand API
