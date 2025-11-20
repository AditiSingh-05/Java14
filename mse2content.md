# Java Complete Interview Preparation Guide - CS301L Unit 2
**Topics Covered:** Threads, Functional Programming (Java 8+), Modern Java Features (Java 9-17)

---

## 📚 Table of Contents
1. [Thread & Multithreading](#1-thread--multithreading)
2. [Functional Interfaces & Lambda Expressions](#2-functional-interfaces--lambda-expressions)
3. [Method References](#3-method-references)
4. [Stream API](#4-stream-api)
5. [Default & Static Methods in Interfaces](#5-default--static-methods-in-interfaces)
6. [forEach Method](#6-foreach-method)
7. [Try-with-resources](#7-try-with-resources)
8. [Type Annotations](#8-type-annotations)
9. [Repeating Annotations](#9-repeating-annotations)
10. [Java Module System (JPMS)](#10-java-module-system)
11. [Diamond Operator with Anonymous Classes](#11-diamond-operator-with-anonymous-classes)
12. [Local Variable Type Inference (var)](#12-local-variable-type-inference-var)
13. [Switch Expressions & yield](#13-switch-expressions--yield)
14. [Text Blocks](#14-text-blocks)
15. [Records](#15-records)
16. [Sealed Classes](#16-sealed-classes)
17. [Interview Questions & Solutions](#17-interview-questions--solutions)

---

## 1. Thread & Multithreading

### 🔬 Concept
**Thread**: Smallest unit of execution within a process. Java supports multithreading natively.

### ⚙️ Thread vs Runnable

| Aspect | Thread Class | Runnable Interface |
|--------|-------------|-------------------|
| **Inheritance** | Extends Thread | Implements Runnable |
| **Multiple Inheritance** | ❌ Cannot extend other classes | ✅ Can extend other classes |
| **Design** | Tight coupling | Loose coupling (preferred) |
| **Usage** | Override `run()` | Implement `run()`, pass to Thread |

**Thread Example:**
```java
class MyThread extends Thread {
    public void run() {
        System.out.println("Thread: " + getName());
    }
}
MyThread t = new MyThread();
t.start();
```

**Runnable Example (Preferred):**
```java
class MyTask implements Runnable {
    public void run() {
        System.out.println("Task running");
    }
}
Thread t = new Thread(new MyTask());
t.start();
```

### 🔄 Thread Life Cycle
1. **New** - Created but not started
2. **Runnable** - Ready to run, waiting for CPU
3. **Running** - Executing
4. **Blocked/Waiting** - Waiting for resource/notification
5. **Terminated** - Execution completed

### ⚙️ Thread Methods
- `start()` - Starts thread execution
- `sleep(ms)` - Pauses thread for specified time
- `yield()` - Hints scheduler to give CPU to other threads
- `join()` - Waits for thread to complete
- `setPriority(int)` - Sets priority (1-10, default 5)

### 🧩 Thread Priority
```java
Thread t = new Thread();
t.setPriority(Thread.MAX_PRIORITY); // 10
t.setPriority(Thread.NORM_PRIORITY); // 5
t.setPriority(Thread.MIN_PRIORITY); // 1
```

### ⚠️ Race Conditions & Synchronization
**Problem:** Multiple threads accessing shared data → inconsistent state.
```java
class Counter {
    private int count = 0;
    public void increment() { count++; } // NOT atomic!
}
```

**Solution:** Use `synchronized`
```java
public synchronized void increment() { count++; }
```

---

## 2. Functional Interfaces & Lambda Expressions

### 🔬 Functional Interface
Interface with **exactly ONE abstract method**. Can have default/static methods.

**Annotation:** `@FunctionalInterface` (optional but recommended)

### ⚙️ Built-in Functional Interfaces

| Interface | Method | Use Case |
|-----------|--------|----------|
| `Predicate<T>` | `boolean test(T t)` | Condition testing |
| `Function<T,R>` | `R apply(T t)` | Transformation |
| `Consumer<T>` | `void accept(T t)` | Action without return |
| `Supplier<T>` | `T get()` | Supply value |

**Example:**
```java
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b);
}

// Implementation via lambda
Calculator add = (a, b) -> a + b;
System.out.println(add.calculate(5, 3)); // 8
```

### 🔬 Lambda Expression
Anonymous function that implements a functional interface.

**Syntax:** `(parameters) -> expression` or `(parameters) -> { statements; }`

**Examples:**
```java
// Zero parameters
Runnable r = () -> System.out.println("Hello");

// Single parameter (parentheses optional)
Consumer<String> print = s -> System.out.println(s);

// Multiple parameters
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;

// Multiple statements
Consumer<String> process = s -> {
    String upper = s.toUpperCase();
    System.out.println(upper);
};
```

### 💡 Lambda vs Anonymous Class
```java
// Anonymous Inner Class
Runnable r1 = new Runnable() {
    private String name = "Inner";
    public void run() {
        System.out.println(this.name); // "Inner" (refers to anonymous class)
    }
};

// Lambda
private String name = "Outer";
Runnable r2 = () -> {
    System.out.println(this.name); // "Outer" (refers to enclosing class)
};
```

**Key Difference:** `this` in lambda refers to enclosing class, in anonymous class refers to itself.

---

## 3. Method References

### 🔬 Concept
Shorthand for lambda expressions that only call an existing method.

**Syntax:** `ClassName::methodName`

### ⚙️ Types

| Type | Syntax | Lambda Equivalent |
|------|--------|-------------------|
| Static | `ClassName::staticMethod` | `(args) -> ClassName.staticMethod(args)` |
| Instance (specific object) | `object::instanceMethod` | `(args) -> object.instanceMethod(args)` |
| Instance (arbitrary object) | `ClassName::instanceMethod` | `(obj, args) -> obj.instanceMethod(args)` |
| Constructor | `ClassName::new` | `(args) -> new ClassName(args)` |

**Examples:**
```java
// Static method reference
List<Integer> nums = Arrays.asList(1, -2, 3, -4);
nums.stream().map(Math::abs); // Same as: n -> Math.abs(n)

// Instance method reference (arbitrary object)
List<String> names = Arrays.asList("Alice", "Bob");
names.stream().map(String::toUpperCase); // Same as: s -> s.toUpperCase()

// Constructor reference
Supplier<ArrayList<String>> listSupplier = ArrayList::new;
ArrayList<String> list = listSupplier.get();
```

---

## 4. Stream API

### 🔬 Concept
Functional-style operations on collections. Streams are **not data structures**, they convey elements from a source.

### ⚙️ Stream Operations

**Intermediate (return Stream):** `filter()`, `map()`, `sorted()`, `distinct()`, `limit()`, `skip()`  
**Terminal (return result):** `collect()`, `forEach()`, `count()`, `reduce()`, `anyMatch()`, `findFirst()`

### 🧪 Examples
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

// Filter even numbers
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList()); // [2, 4, 6]

// Map and sum
int sum = numbers.stream()
    .map(n -> n * 2)
    .reduce(0, Integer::sum); // 42

// Count elements
long count = numbers.stream()
    .filter(n -> n > 3)
    .count(); // 3
```

### 💡 Characteristics
- **Lazy evaluation**: Intermediate operations don't execute until terminal operation
- **Once-consumable**: Cannot reuse stream after terminal operation
- **No side effects**: Prefer pure functions

---

## 5. Default & Static Methods in Interfaces

### 🔬 Default Methods (Java 8)
**Purpose:** Add methods to interfaces without breaking implementations.

```java
interface Vehicle {
    void drive(); // Abstract
    
    default void honk() { // Default implementation
        System.out.println("Beep!");
    }
}

class Car implements Vehicle {
    public void drive() { System.out.println("Driving"); }
    // honk() inherited automatically
}
```

### 🔬 Static Methods (Java 8)
**Purpose:** Utility methods related to interface.

```java
interface MathUtils {
    static int add(int a, int b) {
        return a + b;
    }
}

// Usage
int result = MathUtils.add(5, 3); // Call via interface name
```

**Key:** Static methods belong to interface, not implementing classes.

---

## 6. forEach Method

### 🔬 Concept
Default method in `Iterable` interface for internal iteration.

```java
List<String> list = Arrays.asList("A", "B", "C");

// Traditional
for (String s : list) {
    System.out.println(s);
}

// forEach
list.forEach(System.out::println);

// Map forEach
Map<String, Integer> map = Map.of("A", 1, "B", 2);
map.forEach((key, value) -> System.out.println(key + ": " + value));
```

---

## 7. Try-with-resources

### 🔬 Concept (Java 7+)
Automatically closes resources that implement `AutoCloseable` or `Closeable`.

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
        try { br.close(); } catch (IOException e) { }
    }
}
```

**With Try-with-resources:**
```java
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    String line = br.readLine();
} catch (IOException e) {
    e.printStackTrace();
}
// br.close() called automatically
```

### 💡 Multiple Resources
```java
try (FileInputStream in = new FileInputStream("in.txt");
     FileOutputStream out = new FileOutputStream("out.txt")) {
    // Use resources
} // Both closed in reverse order
```

---

## 8. Type Annotations

### 🔬 Concept (Java 8+)
Annotations applied to **any use of a type**, not just declarations.

**Where:** Type casts, generic type arguments, array components, exception types.

```java
@Target(ElementType.TYPE_USE)
@interface NonNull { }

// Usage
List<@NonNull String> names;
@NonNull String name = getValue();
String @NonNull [] array;
```

**Use Case:** Enhanced compile-time checking with tools like Checker Framework.

---

## 9. Repeating Annotations

### 🔬 Concept (Java 8+)
Apply same annotation multiple times.

**Before Java 8:**
```java
@Schedules({@Schedule(day="Mon"), @Schedule(day="Wed")})
```

**Java 8+:**
```java
@Repeatable(Schedules.class)
@interface Schedule {
    String day();
}

@interface Schedules {
    Schedule[] value();
}

// Usage
@Schedule(day="Mon")
@Schedule(day="Wed")
void task() { }
```

---

## 10. Java Module System

### 🔬 Concept (Java 9)
Organizes code into modules with explicit dependencies.

**module-info.java:**
```java
module com.example.myapp {
    requires java.sql;
    exports com.example.myapp.api;
    opens com.example.myapp.model; // For reflection
}
```

**Benefits:**
- Strong encapsulation
- Reliable configuration
- Modular JDK (smaller runtime images)

---

## 11. Diamond Operator with Anonymous Classes

### 🔬 Concept (Java 9+)
Use `<>` with anonymous classes.

**Java 7-8:** ❌ Not allowed
```java
List<String> list = new ArrayList<String>() { }; // Must specify type
```

**Java 9+:** ✅ Allowed
```java
List<String> list = new ArrayList<>() { }; // Type inferred
```

---

## 12. Local Variable Type Inference (var)

### 🔬 Concept (Java 10+)
Compiler infers type from initializer.

```java
var name = "Alice"; // String
var list = new ArrayList<String>(); // ArrayList<String>
var numbers = List.of(1, 2, 3); // List<Integer>

// NOT allowed
var x; // ❌ Must be initialized
var y = null; // ❌ Cannot infer from null
```

**Use:** Local variables only (not fields, parameters, return types).

---

## 13. Switch Expressions & yield

### 🔬 Concept (Java 12-14)
Switch can **return values**.

**Traditional:**
```java
String result;
switch (day) {
    case "MON": result = "Weekday"; break;
    case "SAT": result = "Weekend"; break;
    default: result = "Unknown"; break;
}
```

**Switch Expression:**
```java
String result = switch (day) {
    case "MON", "TUE", "WED", "THU", "FRI" -> "Weekday";
    case "SAT", "SUN" -> "Weekend";
    default -> "Unknown";
};
```

### ⚙️ yield Keyword (Java 13+)
Returns value from switch block.

**In Threads:** `Thread.yield()` hints scheduler to give CPU to other threads.

**In Switch:**
```java
String result = switch (x) {
    case 1 -> "One";
    case 2 -> {
        System.out.println("Processing");
        yield "Two"; // Return value from block
    }
    default -> "Other";
};
```

---

## 14. Text Blocks

### 🔬 Concept (Java 15)
Multi-line string literals with `"""`.

**Before:**
```java
String json = "{\n" +
              "  \"name\": \"Alice\",\n" +
              "  \"age\": 25\n" +
              "}";
```

**With Text Blocks:**
```java
String json = """
    {
      "name": "Alice",
      "age": 25
    }
    """;
```

**Benefits:** No escape sequences, preserves formatting.

---

## 15. Records

### 🔬 Concept (Java 16)
Immutable data carriers. Compact syntax for classes holding data.

**Before:**
```java
class Person {
    private final String name;
    private final int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    
    // equals(), hashCode(), toString()...
}
```

**With Record:**
```java
record Person(String name, int age) { }
```

**Auto-generated:**
- Constructor
- Getters: `name()`, `age()`
- `equals()`, `hashCode()`, `toString()`

**Usage:**
```java
Person p = new Person("Alice", 25);
System.out.println(p.name()); // Alice
System.out.println(p); // Person[name=Alice, age=25]
```

---

## 16. Sealed Classes

### 🔬 Concept (Java 17)
Restricts which classes can extend/implement.

```java
sealed interface Shape permits Circle, Rectangle { }

final class Circle implements Shape {
    double radius;
}

final class Rectangle implements Shape {
    double width, height;
}

// Other classes CANNOT implement Shape
```

**Child classes must be:** `final`, `sealed`, or `non-sealed`.

**Benefits:**
- Controlled inheritance
- Exhaustive pattern matching in switch (no default needed)

```java
double area = switch (shape) {
    case Circle c -> Math.PI * c.radius * c.radius;
    case Rectangle r -> r.width * r.height;
    // No default needed—compiler knows all types
};
```

---

## 17. Interview Questions & Solutions

### Q1: What is the difference between Thread class and Runnable interface?

**Answer:**

| Aspect | Thread Class | Runnable Interface |
|--------|--------------|-------------------|
| **Type** | Class | Interface |
| **Inheritance** | Extends Thread (single inheritance limit) | Implements Runnable (can extend other classes) |
| **Design** | Tight coupling (thread + task together) | Loose coupling (separates task from thread) |
| **Reusability** | Cannot reuse same instance for multiple threads | Same Runnable can be used by multiple threads |
| **Flexibility** | Less flexible | More flexible (preferred) |

**Code Example:**
```java
// Thread class
class MyThread extends Thread {
    public void run() { System.out.println("Thread running"); }
}
new MyThread().start();

// Runnable interface (Preferred)
class MyTask implements Runnable {
    public void run() { System.out.println("Task running"); }
}
new Thread(new MyTask()).start();
```

**Why Runnable is preferred:**
1. Java doesn't support multiple inheritance—implementing Runnable allows extending other classes
2. Better OOP design (composition over inheritance)
3. Works with ExecutorService and thread pools

---

### Q2: Explain the meaning of pausing a running thread using sleep() with a suitable example.

**Answer:**

`Thread.sleep(ms)` **pauses** the currently executing thread for specified milliseconds, moving it from **RUNNING** to **TIMED_WAITING** state. During sleep:
- Thread does **NOT** consume CPU
- Does **NOT** release locks (if holding any)
- Can be interrupted (throws `InterruptedException`)

**Example:**
```java
class SleepDemo extends Thread {
    public void run() {
        for (int i = 1; i <= 3; i++) {
            try {
                System.out.println(i + " - " + LocalDateTime.now());
                Thread.sleep(1000); // Pause for 1 second
            } catch (InterruptedException e) {
                System.out.println("Interrupted!");
            }
        }
    }
    
    public static void main(String[] args) {
        new SleepDemo().start();
    }
}
```

**Output:**
```
1 - 2025-01-16T10:00:00
2 - 2025-01-16T10:00:01
3 - 2025-01-16T10:00:02
```

**Use Cases:** Delays, polling intervals, animation timing.

---

### Q3: What is the use of the yield() method in switch expression and Java threads?

**Answer:**

#### **1. In Threads: `Thread.yield()`**
**Purpose:** Hints the scheduler to give CPU time to other threads of same priority.

```java
class YieldDemo extends Thread {
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println(Thread.currentThread().getName() + " - " + i);
            Thread.yield(); // Give chance to other threads
        }
    }
    
    public static void main(String[] args) {
        new YieldDemo().start();
        new YieldDemo().start();
    }
}
```

**Note:** `yield()` is a **hint**, not guaranteed. Scheduler may ignore it.

#### **2. In Switch Expressions: `yield` keyword**
**Purpose:** Returns a value from a switch block (Java 13+).

```java
String result = switch (day) {
    case "MON" -> "Monday";
    case "TUE" -> {
        System.out.println("Processing Tuesday");
        yield "Tuesday"; // Return value from block
    }
    default -> "Other";
};
```

**Key Difference:**
- `Thread.yield()`: Method for thread scheduling
- `yield` keyword: Returns value from switch expression

---

### Q4: Define Functional Interface with two examples.

**Answer:**

**Definition:** Interface with **exactly ONE abstract method**. Can have default/static methods.

**Annotation:** `@FunctionalInterface` (optional but recommended)

**Example 1: Custom Functional Interface**
```java
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b); // Single abstract method
}

// Usage with lambda
Calculator add = (a, b) -> a + b;
Calculator multiply = (a, b) -> a * b;

System.out.println(add.calculate(5, 3));      // 8
System.out.println(multiply.calculate(5, 3)); // 15
```

**Example 2: Built-in Functional Interface (Predicate)**
```java
import java.util.function.Predicate;

Predicate<Integer> isEven = n -> n % 2 == 0;

System.out.println(isEven.test(4)); // true
System.out.println(isEven.test(7)); // false
```

**Built-in Functional Interfaces:**
- `Predicate<T>`: `boolean test(T t)`
- `Function<T, R>`: `R apply(T t)`
- `Consumer<T>`: `void accept(T t)`
- `Supplier<T>`: `T get()`

---

### Q5: What is an Anonymous Inner Class? Give syntax.

**Answer:**

**Definition:** Class without a name, defined and instantiated in a single expression. Used for one-time implementations.

**Syntax:**
```java
InterfaceName reference = new InterfaceName() {
    // Override methods
    @Override
    public returnType methodName(parameters) {
        // Implementation
    }
};
```

**Example:**
```java
// Anonymous class implementing Runnable
Runnable task = new Runnable() {
    @Override
    public void run() {
        System.out.println("Task running");
    }
};

new Thread(task).start();
```

**With Abstract Class:**
```java
abstract class Animal {
    abstract void sound();
}

Animal dog = new Animal() {
    @Override
    void sound() {
        System.out.println("Bark");
    }
};

dog.sound(); // Bark
```

**Characteristics:**
- Can have its own fields and methods
- `this` refers to anonymous class instance
- Cannot have constructors (no name)
- Used once (not reusable)

---

### Q6: What is a Lambda Expression? Write a sample lambda expression.

**Answer:**

**Definition:** Anonymous function that provides implementation of a functional interface. Introduced in Java 8.

**Syntax:**
```
(parameters) -> expression
or
(parameters) -> { statements; }
```

**Components:**
- **Parameter list**: Can be empty `()`, single `x`, or multiple `(x, y)`
- **Arrow token**: `->`
- **Body**: Expression or block

**Sample Lambda Expressions:**

**1. Zero parameters:**
```java
Runnable r = () -> System.out.println("Hello Lambda");
r.run(); // Hello Lambda
```

**2. Single parameter:**
```java
Consumer<String> print = name -> System.out.println("Hello " + name);
print.accept("Alice"); // Hello Alice
```

**3. Multiple parameters:**
```java
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
System.out.println(add.apply(5, 3)); // 8
```

**4. Block with multiple statements:**
```java
BiFunction<Integer, Integer, Integer> max = (a, b) -> {
    if (a > b) return a;
    else return b;
};
System.out.println(max.apply(10, 20)); // 20
```

**5. With type declaration (optional):**
```java
BiFunction<Integer, Integer, Integer> multiply = (Integer a, Integer b) -> a * b;
```

---

### Q7: What are Method References in Java?

**Answer:**

**Definition:** Shorthand notation for lambda expressions that only call an existing method. Uses `::` operator.

**Syntax:** `ClassName::methodName` or `object::methodName`

**Types:**

| Type | Syntax | Lambda Equivalent | Example |
|------|--------|-------------------|---------|
| **Static method** | `ClassName::staticMethod` | `(args) -> ClassName.staticMethod(args)` | `Math::abs` |
| **Instance method (specific object)** | `object::instanceMethod` | `(args) -> object.instanceMethod(args)` | `System.out::println` |
| **Instance method (arbitrary object)** | `ClassName::instanceMethod` | `(obj, args) -> obj.instanceMethod(args)` | `String::toUpperCase` |
| **Constructor** | `ClassName::new` | `(args) -> new ClassName(args)` | `ArrayList::new` |

**Examples:**

**1. Static Method Reference:**
```java
List<Integer> numbers = Arrays.asList(1, -2, 3, -4);

// Lambda
numbers.stream().map(n -> Math.abs(n));

// Method reference
numbers.stream().map(Math::abs);
```

**2. Instance Method Reference (specific object):**
```java
// Lambda
numbers.forEach(n -> System.out.println(n));

// Method reference
numbers.forEach(System.out::println);
```

**3. Instance Method Reference (arbitrary object):**
```java
List<String> names = Arrays.asList("alice", "bob", "charlie");

// Lambda
names.stream().map(s -> s.toUpperCase());

// Method reference
names.stream().map(String::toUpperCase);
```

**4. Constructor Reference:**
```java
// Lambda
Supplier<ArrayList<String>> supplier = () -> new ArrayList<>();

// Constructor reference
Supplier<ArrayList<String>> supplier = ArrayList::new;
```

---

### Q8: Define the following (Java 17 features)

#### a) Text Block

**Definition:** Multi-line string literal using triple double-quotes (`"""`). Introduced in Java 15.

**Purpose:** Simplify writing multi-line strings (JSON, SQL, HTML) without escape sequences.

**Syntax:**
```java
String textBlock = """
    Line 1
    Line 2
    Line 3
    """;
```

**Example:**
```java
String json = """
    {
      "name": "Alice",
      "age": 25,
      "city": "New York"
    }
    """;

String sql = """
    SELECT id, name, email
    FROM users
    WHERE age > 18
    ORDER BY name
    """;
```

**Benefits:**
- No `\n` escape sequences needed
- Preserves formatting and indentation
- More readable

---

#### b) Record

**Definition:** Special class for immutable data carriers. Introduced in Java 16.

**Purpose:** Compact syntax for classes that only hold data (like DTOs, value objects).

**Syntax:**
```java
record RecordName(Type field1, Type field2, ...) { }
```

**Example:**
```java
record Person(String name, int age) { }

// Usage
Person p = new Person("Alice", 25);
System.out.println(p.name());  // Alice
System.out.println(p.age());   // 25
System.out.println(p);         // Person[name=Alice, age=25]
```

**Auto-generated:**
- Constructor: `Person(String name, int age)`
- Getters: `name()`, `age()` (not `getName()`, `getAge()`)
- `equals()`, `hashCode()`, `toString()`

**Characteristics:**
- Immutable (fields are `final`)
- Cannot extend other classes (implicitly extends `Record`)
- Can implement interfaces

**Custom Methods:**
```java
record Rectangle(double width, double height) {
    // Custom method
    double area() {
        return width * height;
    }
    
    // Compact constructor (validation)
    public Rectangle {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
    }
}
```

---

#### c) Sealed Class

**Definition:** Class/interface that restricts which classes can extend/implement it. Introduced in Java 17.

**Purpose:** Controlled inheritance for domain modeling and exhaustive pattern matching.

**Syntax:**
```java
sealed class/interface Name permits SubClass1, SubClass2, ... { }
```

**Example:**
```java
sealed interface Shape permits Circle, Rectangle, Triangle { }

final class Circle implements Shape {
    double radius;
}

final class Rectangle implements Shape {
    double width, height;
}

non-sealed class Triangle implements Shape {
    double base, height;
}

// Other classes CANNOT implement Shape
```

**Child Classes Must Be:**
- `final` - Cannot be extended further
- `sealed` - Can be extended by permitted subclasses
- `non-sealed` - Open for extension

**Benefits:**

1. **Controlled Inheritance:**
```java
// Only Circle, Rectangle, Triangle can implement Shape
// Prevents unexpected implementations
```

2. **Exhaustive Pattern Matching:**
```java
double calculateArea(Shape shape) {
    return switch (shape) {
        case Circle c -> Math.PI * c.radius * c.radius;
        case Rectangle r -> r.width * r.height;
        case Triangle t -> 0.5 * t.base * t.height;
        // No default needed—compiler knows all types!
    };
}
```

---

## Programming Questions & Solutions

### P1: Write a Java program using two threads where both deposit money into the same account object. Use synchronized to prevent race conditions.

```java
class BankAccount {
    private int balance = 0;
    
    // Synchronized method prevents race condition
    public synchronized void deposit(int amount) {
        System.out.println(Thread.currentThread().getName() + 
                          " depositing " + amount);
        balance += amount;
        System.out.println(Thread.currentThread().getName() + 
                          " new balance: " + balance);
    }
    
    public int getBalance() {
        return balance;
    }
}

class DepositTask implements Runnable {
    private BankAccount account;
    private int amount;
    
    public DepositTask(BankAccount account, int amount) {
        this.account = account;
        this.amount = amount;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            account.deposit(amount);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class SynchronizedBankDemo {
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount();
        
        // Two threads depositing to same account
        Thread t1 = new Thread(new DepositTask(account, 100), "Thread-1");
        Thread t2 = new Thread(new DepositTask(account, 200), "Thread-2");
        
        t1.start();
        t2.start();
        
        t1.join(); // Wait for t1 to finish
        t2.join(); // Wait for t2 to finish
        
        System.out.println("\nFinal Balance: " + account.getBalance());
        // Expected: 900 (3*100 + 3*200)
    }
}
```

**Output:**
```
Thread-1 depositing 100
Thread-1 new balance: 100
Thread-2 depositing 200
Thread-2 new balance: 300
Thread-1 depositing 100
Thread-1 new balance: 400
Thread-2 depositing 200
Thread-2 new balance: 600
Thread-1 depositing 100
Thread-1 new balance: 700
Thread-2 depositing 200
Thread-2 new balance: 900

Final Balance: 900
```

**Why synchronized is needed:**
Without `synchronized`, both threads could read the same balance value, increment it, and write back—causing lost updates (race condition).

---

### P2: Write a program to print even and odd numbers alternately using two threads.

```java
class NumberPrinter {
    private int number = 1;
    private final int max = 20;
    
    // Print odd numbers
    public synchronized void printOdd() {
        while (number <= max) {
            while (number % 2 == 0) { // Wait if number is even
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (number <= max) {
                System.out.println(Thread.currentThread().getName() + 
                                  ": " + number);
                number++;
                notify(); // Wake up even thread
            }
        }
    }
    
    // Print even numbers
    public synchronized void printEven() {
        while (number <= max) {
            while (number % 2 != 0) { // Wait if number is odd
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (number <= max) {
                System.out.println(Thread.currentThread().getName() + 
                                  ": " + number);
                number++;
                notify(); // Wake up odd thread
            }
        }
    }
}

public class AlternateOddEven {
    public static void main(String[] args) {
        NumberPrinter printer = new NumberPrinter();
        
        Thread oddThread = new Thread(() -> printer.printOdd(), "ODD");
        Thread evenThread = new Thread(() -> printer.printEven(), "EVEN");
        
        oddThread.start();
        evenThread.start();
    }
}
```

**Output:**
```
ODD: 1
EVEN: 2
ODD: 3
EVEN: 4
ODD: 5
EVEN: 6
...
ODD: 19
EVEN: 20
```

**How it works:**
- `wait()`: Thread waits until notified
- `notify()`: Wakes up one waiting thread
- Odd thread waits when number is even, Even thread waits when number is odd

---

### P3: Write a Java program to filter even numbers from a list using Stream API.

```java
import java.util.*;
import java.util.stream.*;

public class FilterEvenNumbers {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Filter even numbers using Stream API
        List<Integer> evenNumbers = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        
        System.out.println("Original List: " + numbers);
        System.out.println("Even Numbers: " + evenNumbers);
        
        // Alternative: Print directly
        System.out.print("Even Numbers (printed): ");
        numbers.stream()
            .filter(n -> n % 2 == 0)
            .forEach(n -> System.out.print(n + " "));
    }
}
```

**Output:**
```
Original List: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
Even Numbers: [2, 4, 6, 8, 10]
Even Numbers (printed): 2 4 6 8 10 
```

**Explanation:**
- `stream()`: Creates stream from list
- `filter(n -> n % 2 == 0)`: Keeps only even numbers
- `collect(Collectors.toList())`: Collects results into new list

---

### P4: "List of students" – demonstrate two ways to print them using Lambda Expression and Method Reference

```java
import java.util.*;

class Student {
    private String name;
    private int rollNo;
    
    public Student(String name, int rollNo) {
        this.name = name;
        this.rollNo = rollNo;
    }
    
    @Override
    public String toString() {
        return "Student{name='" + name + "', rollNo=" + rollNo + "}";
    }
}

public class PrintStudents {
    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
            new Student("Alice", 101),
            new Student("Bob", 102),
            new Student("Charlie", 103),
            new Student("Diana", 104)
        );
        
        System.out.println("=== Using Lambda Expression ===");
        students.forEach(student -> System.out.println(student));
        
        System.out.println("\n=== Using Method Reference ===");
        students.forEach(System.out::println);
    }
}
```

**Output:**
```
=== Using Lambda Expression ===
Student{name='Alice', rollNo=101}
Student{name='Bob', rollNo=102}
Student{name='Charlie', rollNo=103}
Student{name='Diana', rollNo=104}

=== Using Method Reference ===
Student{name='Alice', rollNo=101}
Student{name='Bob', rollNo=102}
Student{name='Charlie', rollNo=103}
Student{name='Diana', rollNo=104}
```

**Explanation:**
- **Lambda:** `student -> System.out.println(student)` - explicitly passes parameter
- **Method Reference:** `System.out::println` - shorthand, cleaner syntax

---

### P5: Write a program to implement Dependency Injection (DI) using constructor injection.

```java
// Service interface
interface MessageService {
    void sendMessage(String message);
}

// SMS service implementation
class SMSService implements MessageService {
    @Override
    public void sendMessage(String message) {
        System.out.println("SMS sent: " + message);
    }
}

// Email service implementation
class EmailService implements MessageService {
    @Override
    public void sendMessage(String message) {
        System.out.println("Email sent: " + message);
    }
}

// Client class with constructor injection
class NotificationService {
    private MessageService messageService;
    
    // Constructor Injection
    public NotificationService(MessageService messageService) {
        this.messageService = messageService;
    }
    
    public void notifyUser(String message) {
        messageService.sendMessage(message);
    }
}

public class DependencyInjectionDemo {
    public static void main(String[] args) {
        // Inject SMS service
        MessageService smsService = new SMSService();
        NotificationService notification1 = new NotificationService(smsService);
        notification1.notifyUser("Hello via SMS");
        
        System.out.println();
        
        // Inject Email service
        MessageService emailService = new EmailService();
        NotificationService notification2 = new NotificationService(emailService);
        notification2.notifyUser("Hello via Email");
    }
}
```

**Output:**
```
SMS sent: Hello via SMS

Email sent: Hello via Email
```

**Benefits of DI:**
1. **Loose coupling**: `NotificationService` doesn't depend on concrete implementations
2. **Testability**: Easy to inject mock services for testing
3. **Flexibility**: Can switch implementations without changing client code

---

### P6: Demonstrate how to create a Functional Interface, implement it using both Lambda Expression and Anonymous Class

```java
// Custom Functional Interface
@FunctionalInterface
interface MathOperation {
    int operate(int a, int b);
}

public class FunctionalInterfaceDemo {
    public static void main(String[] args) {
        // Implementation using Lambda Expression
        System.out.println("=== Lambda Expression ===");
        MathOperation addition = (a, b) -> a + b;
        MathOperation subtraction = (a, b) -> a - b;
        MathOperation multiplication = (a, b) -> a * b;
        
        System.out.println("Addition: " + addition.operate(10, 5));
        System.out.println("Subtraction: " + subtraction.operate(10, 5));
        System.out.println("Multiplication: " + multiplication.operate(10, 5));
        
        // Implementation using Anonymous Class
        System.out.println("\n=== Anonymous Class ===");
        MathOperation division = new MathOperation() {
            @Override
            public int operate(int a, int b) {
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
            }
        };
        
        MathOperation modulus = new MathOperation() {
            @Override
            public int operate(int a, int b) {
                return a % b;
            }
        };
        
        System.out.println("Division: " + division.operate(10, 5));
        System.out.println("Modulus: " + modulus.operate(10, 3));
    }
}
```

**Output:**
```
=== Lambda Expression ===
Addition: 15
Subtraction: 5
Multiplication: 50

=== Anonymous Class ===
Division: 2
Modulus: 1
```

**Comparison:**

| Aspect | Lambda Expression | Anonymous Class |
|--------|------------------|----------------|
| Syntax | Concise: `(a, b) -> a + b` | Verbose: `new Interface() { }` |
| `this` keyword | Refers to enclosing class | Refers to anonymous class |
| Fields/Methods | Cannot have | Can have private fields/methods |
| Use case | Simple, stateless implementations | Complex logic, need state |

---

### P7: Write a program to display "Welcome" into a file using try-with-resources.

```java
import java.io.*;

public class TryWithResourcesDemo {
    public static void main(String[] args) {
        String fileName = "welcome.txt";
        String message = "Welcome to Java Try-with-resources!";
        
        // Try-with-resources (Java 7+)
        try (FileWriter writer = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            
            bufferedWriter.write(message);
            System.out.println("Message written to file: " + fileName);
            
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
        // Resources (FileWriter, BufferedWriter) automatically closed here
        
        // Read and display the file content
        System.out.println("\nReading from file:");
        try (FileReader reader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Message written to file: welcome.txt

Reading from file:
Welcome to Java Try-with-resources!
```

**Benefits of try-with-resources:**
1. **Automatic resource management**: No need for explicit `close()` calls
2. **Cleaner code**: No finally block needed
3. **Exception handling**: Suppressed exceptions properly handled
4. **Multiple resources**: Can declare multiple resources separated by semicolons

**Before Java 7 (old way):**
```java
FileWriter writer = null;
try {
    writer = new FileWriter("file.txt");
    writer.write("Hello");
} catch (IOException e) {
    e.printStackTrace();
} finally {
    if (writer != null) {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

---

## 🎯 Quick Reference Summary

### Thread Concepts
- **Thread vs Runnable**: Prefer Runnable for flexibility
- **sleep()**: Pauses thread, doesn't release locks
- **yield()**: Hints scheduler, not guaranteed
- **synchronized**: Prevents race conditions

### Functional Programming
- **Functional Interface**: One abstract method
- **Lambda**: `(params) -> expression`
- **Method Reference**: `ClassName::method`
- **Stream**: `filter()`, `map()`, `collect()`

### Modern Java Features
- **var** (Java 10): Type inference for local variables
- **Switch expressions** (Java 14): Returns value
- **yield**: Returns from switch block
- **Text Blocks** (Java 15): `""" multi-line """`
- **Records** (Java 16): `record Person(String name, int age) {}`
- **Sealed Classes** (Java 17): Controlled inheritance

### Best Practices
1. Always use `@FunctionalInterface` annotation
2. Prefer method references over lambdas when possible
3. Use try-with-resources for resource management
4. Synchronize shared mutable data
5. Use records for immutable data carriers
6. Leverage sealed classes for domain modeling

---

**Interview Preparation Tips:**
1. Understand **why** a feature exists, not just how to use it
2. Practice **explaining** concepts (as if teaching)
3. Know **trade-offs** between approaches
4. Write code **from scratch** without IDE hints
5. Review **thread safety** and **synchronization** thoroughly
6. Master **Stream API** operations and chaining
7. Understand **lambda** vs **anonymous class** differences

---

**End of Guide**
