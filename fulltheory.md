⭐ UNIT–2 — SHORT ANSWER QUESTIONS (MAX THEORY + PDF CONTENT INCLUDED)
1. Difference between Thread Class and Runnable Interface

📚 PDF Reference: Thread creation methods Pg 9–14

Full Theory Answer

In Java, a thread can be created in two ways — by extending the Thread class or by implementing the Runnable interface. Both approaches represent independent execution paths, but they differ in flexibility and design purpose.

When a class extends Thread, it becomes a specialized thread object. The programmer overrides the run() method and starts execution using start(). This approach limits inheritance because Java does not support multiple inheritance — a class cannot extend another class if it already extends Thread.

When a class implements Runnable, it only provides the task to be executed. The actual thread object is created separately by passing the Runnable instance to a Thread constructor. This approach is preferred in modern Java because it promotes loose coupling, supports reuse, and allows a class to extend some other parent class.

Exam Table
Thread Class	Runnable Interface
Use extends Thread	Use implements Runnable
Cannot extend another class	Can extend another class
Thread object is itself the task	Task and thread are separate
Slightly heavier	Lightweight and preferred
Direct call to run() inside thread	run() executed inside Thread object
2. Meaning of pausing a running thread using sleep()

📚 PDF Pg 15–16

Full Theory Answer

sleep() is a static method of the Thread class that is used to temporarily pause the execution of a thread for a specified number of milliseconds. During sleep, the thread moves from the Running → Timed Waiting state (as shown in the thread lifecycle diagram on PDF Pg 7) and gives the CPU to other threads.

The thread wakes up automatically after the sleep interval unless interrupted. sleep() does not release any lock held by the thread, meaning synchronization is not broken.

PDF Example Code
try { 
    Thread.sleep(500); 
} catch (InterruptedException e) {}

3. Use of yield() in Java threads and in switch expression

📚 Thread yield: Pg 11 + Switch yield: Pg 105

Thread yield() Theory

yield() is a hint to the scheduler indicating that the current thread is willing to pause temporarily so that other threads of equal priority may run. It does not guarantee a pause; it simply moves the current thread back to the Runnable state.

Purpose: improve fairness in multi-threaded execution.

yield in Switch Expressions

Java 13 introduced yield to return values from switch expressions.

PDF Pg 105 shows:

return switch(day) {
    case "Monday" -> { yield "Weekday"; }
    default -> { yield "Unknown"; }
};


Yield is used when the case body uses curly braces and needs to return a value.

4. Define Functional Interface (with two examples)

📚 PDF Pg 31–32

Full Theory Answer

A Functional Interface is an interface that contains exactly one abstract method, though it may have default or static methods. It serves as the foundation for Lambda expressions and Method References in Java.

Java provides the @FunctionalInterface annotation to ensure no more than one abstract method exists.

Examples

Built-in: Runnable

void run();


Custom:

@FunctionalInterface
interface Calculator {
    int calculate(int x);
}

5. What is Anonymous Inner Class? Give syntax.

📚 PDF Pg 88–90 (Diamond + anonymous class)

Theory

An Anonymous Inner Class is a class created without a name, usually to provide a one-time implementation of an interface or abstract class. It helps in writing short and concise code where creating a separate class is unnecessary.

Syntax
InterfaceName obj = new InterfaceName() {
    @Override
    public void method() { ... }
};

6. What is Lambda Expression? Give sample.

📚 PDF Pg 28–34

Theory

A Lambda Expression is a concise way to represent a function without a name. It is used to implement functional interfaces and supports functional programming in Java.

It eliminates boilerplate code of anonymous inner classes.

Basic Syntax
(parameters) -> expression

Example
(int a, int b) -> a + b

7. What are Method References?

📚 PDF Pg 37–45

Full Theory

Method References provide a compact way to refer to methods using ::. They make code more readable when a lambda simply calls an existing method.

Types

Static method reference – ClassName::staticMethod

Instance method reference – obj::method

Instance of arbitrary object – ClassName::instanceMethod

Constructor reference – ClassName::new

PDF Example
System.out::println;

8. Java 17 Features — Text Block, Record, Sealed Class

📚 Text Block Pg 112–116, Record Pg 117–121, Sealed Class Pg 122–126


a) Text Block

A Text Block is a multi-line string literal enclosed within triple quotes (""").
It makes writing long strings, HTML, SQL, JSON easier, without escaped characters.

Example
String msg = """
Hello
World
""";

b) Record

A Record is a special data-carrier class that automatically provides constructor, getters, equals(), hashCode(), and toString().

Example
public record Person(String name, int age) {}


Records are ideal for DTOs and immutable data.

c) Sealed Class

A Sealed Class restricts which classes can extend it using the permits keyword.

Example
sealed class Human permits Manish, Vartika {}


Child classes must be sealed, non-sealed, or final.

⭐ 1. Two Threads Depositing Money (Race Condition + synchronized)

📚 Thread & sync concept PDF Pg 9–14

FULL THEORY ANSWER (5–7 marks)

In multithreading, when multiple threads perform operations on a shared resource, inconsistent results may occur due to race conditions. A race condition happens because both threads try to update the shared variable simultaneously.

To avoid this, Java provides the synchronized keyword. A synchronized method ensures mutual exclusion, meaning only one thread can execute the method at a time while the other waits. Thus, it preserves data consistency.

In a bank account scenario, if two threads deposit money simultaneously without synchronization, the final balance may be incorrect. Using a synchronized deposit method ensures atomicity of update.

Program
class BankAccount {
    private int balance = 0;

    public synchronized void deposit(int amount) {
        balance += amount;
        System.out.println("Deposited: " + amount);
    }

    public int getBalance() { return balance; }
}

public class SyncDemo {
    public static void main(String[] args) throws Exception {
        BankAccount acc = new BankAccount();

        Thread t1 = new Thread(() -> {
            for(int i=0;i<3;i++) acc.deposit(100);
        });

        Thread t2 = new Thread(() -> {
            for(int i=0;i<3;i++) acc.deposit(200);
        });

        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("Final Balance = " + acc.getBalance());
    }
}

⭐ 2. Even–Odd Printing Alternately (Two Threads Coordination)
FULL THEORY ANSWER

This program demonstrates thread coordination using wait() and notify().
Two threads — one prints odd numbers, the other prints even numbers.
A shared object is used where threads alternate based on number parity.

wait() makes the thread release lock and wait.

notify() wakes the waiting thread.

synchronized blocks ensure coordination.

Program
class Printer {
    int num = 1, limit = 10;

    public synchronized void printOdd() {
        while(num <= limit) {
            if(num % 2 == 1) {
                System.out.println("Odd: " + num++);
                notify();
            } else {
                try { wait(); } catch(Exception e){}
            }
        }
    }

    public synchronized void printEven() {
        while(num <= limit) {
            if(num % 2 == 0) {
                System.out.println("Even: " + num++);
                notify();
            } else {
                try { wait(); } catch(Exception e){}
            }
        }
    }
}

public class OddEven {
    public static void main(String[] args) {
        Printer p = new Printer();
        new Thread(() -> p.printOdd()).start();
        new Thread(() -> p.printEven()).start();
    }
}

⭐ 3. Filter Even Numbers Using Stream API

📚 PDF Stream concepts Pg 46–53

FULL THEORY

Streams allow functional-style operations like filtering, mapping, reducing.
Filtering selects elements based on a condition.

Stream pipeline → source → intermediate ops → terminal op.

Program
import java.util.*;
import java.util.stream.*;

public class StreamEven {
    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(1,2,3,4,5,6,7,8);

        List<Integer> evens = nums.stream()
                                  .filter(n -> n % 2 == 0)
                                  .toList();

        System.out.println(evens);
    }
}

⭐ 4. Print Students List — Lambda + Method Reference

📚 Method References PDF Pg 37–45

FULL THEORY

lambda → inline implementation of functional interface

method reference → compact syntax using ::

Program
import java.util.*;

class Student {
    String name;
    Student(String n) { name = n; }
    public String toString() { return name; }
}

public class PrintStudents {
    public static void main(String[] args) {
        List<Student> list = Arrays.asList(
            new Student("Aditi"),
            new Student("Rohan"),
            new Student("Simran")
        );

        // Using Lambda
        list.forEach(s -> System.out.println(s));

        // Using Method Reference
        list.forEach(System.out::println);
    }
}

⭐ 5. Dependency Injection using Constructor Injection
FULL THEORY

Dependency Injection (DI) is a design pattern where dependency objects are provided from outside rather than created inside the class.
This improves testability, modularity, and loose coupling.

Constructor Injection is the most common DI form.

Program
class Service {
    public void serve() { System.out.println("Service running..."); }
}

class Client {
    Service service;

    Client(Service s) { this.service = s; }

    public void request() {
        service.serve();
    }
}

public class DITest {
    public static void main(String[] args) {
        Service s = new Service();
        Client c = new Client(s);
        c.request();
    }
}

⭐ 6. Functional Interface implemented using Lambda + Anonymous Class
FULL THEORY

Functional Interface → exactly 1 abstract method.
It can be implemented using:

Anonymous Inner Class

Lambda Expression

Program
@FunctionalInterface
interface Hello {
    void say();
}

public class FI_Demo {
    public static void main(String[] args) {

        // Lambda
        Hello h1 = () -> System.out.println("Hi from Lambda");
        h1.say();

        // Anonymous Class
        Hello h2 = new Hello() {
            public void say() {
                System.out.println("Hi from Anonymous Class");
            }
        };
        h2.say();
    }
}

⭐ 7. Write “Welcome” into file using try-with-resources
FULL THEORY

try-with-resources automatically closes opened resources like FileWriter.
PDF Pg 66–68 describes this feature.

Program
import java.io.*;

public class FileWrite {
    public static void main(String[] args) {
        try(FileWriter fw = new FileWriter("demo.txt")) {
            fw.write("Welcome");
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}

⭐ 8. Bank Account + Text Block + Record + Sealed Class
FULL THEORY

This question checks:

Threads (PDF Pg 9–14)

Text Blocks (Pg 112–116)

Records (Pg 117–121)

Sealed Classes (Pg 122–126)

Program
// Record (Java 17)
record Transaction(String type, int amount) {}

// Sealed Class
sealed class Account permits Savings {}

final class Savings extends Account {
    private int balance = 0;

    public synchronized void deposit(int amt) {
        balance += amt;
        System.out.println("Deposited " + amt);
    }

    public int getBalance() { return balance; }
}

public class Modern {
    public static void main(String[] args) throws Exception {

        String msg = """
                --- Deposit Operation Started ---
                """;
        System.out.println(msg);

        Savings s = new Savings();

        Thread t1 = new Thread(() -> s.deposit(100));
        Thread t2 = new Thread(() -> s.deposit(200));

        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("Final Balance: " + s.getBalance());
    }
}

⭐ UNIT–4 — SHORT ANSWER QUESTIONS (FULL THEORETICAL ANSWERS)
⭐ 1. What is JSP? How is it different from HTML?

📚 PDF Pg 35–38 (JSP Intro + Architecture)

Full Theory Answer (5 marks)

JSP (Java Server Pages) is a server-side technology used for creating dynamic web pages.
It allows embedding Java code inside HTML using JSP tags.
JSP pages are translated into Servlets by the JSP Translator and then executed by the web container. Since JSP runs on the server, it can access Java APIs, databases (via JDBC), JavaBeans, and application logic.

Difference from HTML

HTML is a client-side static markup language.
It cannot interact with a database or execute Java code.
It displays the same output for all users.

JSP is dynamic and runs on the server.
It can fetch data from a database, process user input, and generate different output for different users.

⭐ 2. Advantages of JSP over Servlets

📚 PDF Pg 35–38 + Scriptlet examples Pg 47–55

Full Theory Answer (5 marks)

Servlets contain HTML inside Java code, making them difficult to write and maintain. JSP was introduced to simplify web development by separating presentation (HTML) from business logic (Java).

Advantages

Easier to Write (HTML-like)
JSP mainly uses HTML with small Java snippets, unlike Servlets where HTML must be written inside Java strings.

Implicit Objects Available Automatically
Objects like request, response, session, out, pageContext are available without coding (PDF Pg 56–76) .

Automatic Servlet Conversion
JSP is automatically converted into a Servlet by JSP Translator (PDF Pg 36) .

Better Separation of Concerns
JSP handles UI; Servlets handle logic → supports MVC design.

Reusable Components
JSP includes directives, taglibs, and action tags to modularize code.

⭐ 3. Describe the JSP Life Cycle.

📚 PDF Pg 38–40

Full Theory Answer (5 marks)

JSP life cycle defines the process from page creation to destruction. According to the PDF:

1. Translation Phase

The JSP page is translated into a Servlet class by the JSP engine using JspPage and HttpJspPage interfaces.

2. Compilation Phase

The generated Servlet (.java file) is compiled into a .class file.

3. Initialization — jspInit()

Called once when the JSP is first loaded (similar to init() in Servlets). Used for initialization like opening DB connections.

4. Execution — _jspService()

Called for each request. All Java code inside JSP scriptlets, expressions, and HTML rendering happens here.

5. Destruction — jspDestroy()

Called before the JSP is removed from memory. Used for cleanup.

This is shown in the JSP lifecycle diagram on PDF Pg 38.

⭐ 4. Difference between Statement and PreparedStatement

📚 PDF Pg 31–34 (ResultSet + execute methods)

Full Theory Answer (5 marks)

Statement

Used for executing simple SQL queries.

Query compiled every time → slower.

Vulnerable to SQL injection.

Syntax:

Statement st = con.createStatement();
st.executeQuery("SELECT * FROM student");


PreparedStatement

Precompiled SQL statement with ? parameters.

Faster due to precompilation.

Safe from SQL Injection.

Ideal for Insert/Update/Delete operations.

Syntax:

PreparedStatement ps = con.prepareStatement("SELECT * FROM student WHERE id=?");
ps.setInt(1, 101);

⭐ 5. What is Implicit Object in JSP? Give any 4 examples.

📚 PDF Pg 56–76

Full Theory Answer (5 marks)

Implicit objects are predefined objects created automatically by the JSP container.
They give access to request data, response data, session, application context, output writer, etc.

Four commonly used implicit objects:

request (HttpServletRequest)
Used to get form data, parameters, headers.

response (HttpServletResponse)
Used to send redirects, add headers.

session (HttpSession)
Stores user-specific data across pages.

out (JspWriter)
Used to send output to the browser.

(Others: application, config, pageContext, page, exception)

⭐ 6. Purpose of JSP Directives (page, include, taglib)

📚 PDF Pg 77–87

JSP directives give instructions to the JSP engine on how to process the page.

⭐ a) page Directive

Used to define attributes that apply to the entire page (import, session, errorPage, contentType).

Example (PDF Pg 79):

<%@ page import="java.util.Date" %>

⭐ b) include Directive

Includes the content of another file at translation time.
Useful for static content like header, footer.

PDF Example (Pg 85):

<%@ include file="header.html" %>

⭐ c) taglib Directive

Declares a custom tag library (e.g., JSTL) for use inside JSP.

Example (PDF Pg 86):

<%@ taglib uri="http://www.javatpoint.com/tags" prefix="mytag" %>

⭐ 7. What are JDBC Drivers? Explain Types.

📚 PDF Pg 5–17

Full Theory Answer (5 marks)

A JDBC driver is a software component enabling Java applications to communicate with databases.
PDF lists 4 types:

1. JDBC–ODBC Bridge Driver (Type 1)

Uses ODBC driver.

Slower because JDBC calls converted to ODBC calls.

Requires ODBC installation. (PDF Pg 6–8)

2. Native-API Driver (Type 2)

Uses native DB client libraries.

Faster than Type 1.

Requires DB vendor library on client. (PDF Pg 9–11)

3. Network Protocol Driver (Type 3)

Uses middleware for communication.

Written entirely in Java.

Supports load balancing/logging. (PDF Pg 12–14)

4. Thin Driver (Type 4)

Pure Java driver (most used).

Converts JDBC calls directly to database-specific protocol.

No client-side installation required. (PDF Pg 15–17)

⭐ 8. Steps to Connect JSP with JDBC

📚 PDF Pg 19–24

Full Theory Answer (5 marks)

There are 5 standard steps in JDBC connection:

Register Driver

Class.forName("com.mysql.cj.jdbc.Driver");


Establish Connection

Connection con = DriverManager.getConnection(url,user,pass);


Create Statement / PreparedStatement

PreparedStatement ps = con.prepareStatement(sql);


Execute Query

ResultSet rs = ps.executeQuery();


Close Connection

con.close();


These steps are shown exactly in PDF Pg 19–24 with examples.

⭐ UNIT–4 — LONG ANSWER QUESTIONS (FULL THEORY + PDF CONTENT)
⭐ 1. JSP Login Validation Using JDBC (Forward: welcome.jsp / error.jsp)

📚 PDF Reference: JDBC Steps Pg 19–24 + JSP Implicit Objects Pg 56–76

FULL THEORY ANSWER (10 marks)

A JSP login system allows the user to enter username and password in a form.
When the JSP receives the request, it connects to the database using JDBC, checks whether the provided credentials exist, and then forwards the user to the appropriate page.

Concepts involved:

JSP form handling using request.getParameter()

JDBC connection using DriverManager

PreparedStatement to prevent SQL injection

RequestDispatcher for server-side forwarding

Flow (As per PDF lifecycle Pg 38):

Client → JSP → JDBC → Database → ResultSet → JSP response

Code Implementation (Exam Ready)
login.jsp
<form action="validate.jsp" method="post">
    Username: <input type="text" name="user"><br>
    Password: <input type="password" name="pass"><br>
    <input type="submit" value="Login">
</form>

validate.jsp
<%@ page import="java.sql.*" %>

<%
String u = request.getParameter("user");
String p = request.getParameter("pass");

Class.forName("com.mysql.cj.jdbc.Driver");
Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/aia","root","admin"
);

PreparedStatement ps = con.prepareStatement(
    "SELECT * FROM student WHERE mobile=? AND password=?"
);
ps.setString(1, u);
ps.setString(2, p);

ResultSet rs = ps.executeQuery();

if(rs.next()) {
    RequestDispatcher rd = request.getRequestDispatcher("welcome.jsp");
    rd.forward(request, response);
} else {
    RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
    rd.forward(request, response);
}
%>


This satisfies the typical exam question perfectly.

⭐ 2. JSP Code to Retrieve Data and Display in a Table

📚 PDF ResultSet Methods Pg 32–34

FULL THEORY ANSWER

To display database records inside a JSP, a JDBC connection is established and records are retrieved using ResultSet.
The JSP uses a combination of HTML + Java scriptlets to build a dynamic table.

Code
<%@ page import="java.sql.*" %>

<table border="1">
<tr><th>Name</th><th>Address</th><th>Mobile</th></tr>

<%
Class.forName("com.mysql.cj.jdbc.Driver");
Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/aia", "root", "admin"
);

Statement st = con.createStatement();
ResultSet rs = st.executeQuery("SELECT * FROM student");

while(rs.next()) {
%>
<tr>
<td><%= rs.getString("name") %></td>
<td><%= rs.getString("address") %></td>
<td><%= rs.getString("mobile") %></td>
</tr>
<%
}
%>
</table>

⭐ 3. JSP + JDBC: Enter Employee ID → Show Name & Department

📚 PDF PreparedStatement Pg 31–34

FULL THEORY ANSWER

This demonstrates:

Form handling

PreparedStatement

ResultSet cursor operations

Dynamic page output

Form: empForm.jsp
<form action="showEmp.jsp" method="post">
    Enter ID: <input type="text" name="id">
    <input type="submit" value="Search">
</form>

showEmp.jsp
<%@ page import="java.sql.*" %>

<%
int id = Integer.parseInt(request.getParameter("id"));

Class.forName("com.mysql.cj.jdbc.Driver");
Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/aia", "root", "admin"
);

PreparedStatement ps = con.prepareStatement(
    "SELECT name, dept FROM employee WHERE id=?"
);
ps.setInt(1, id);

ResultSet rs = ps.executeQuery();

if(rs.next()) {
    out.println("Name: " + rs.getString("name") + "<br>");
    out.println("Department: " + rs.getString("dept"));
} else {
    out.println("Employee Not Found");
}
%>

⭐ 4. Explain Cursor Movement in ResultSet

📚 PDF Pg 32–34

FULL THEORY ANSWER (Diagram + Explanation)

ResultSet maintains a cursor that points to rows returned by SQL queries. By default, the cursor starts before the first row.

Important Cursor Methods (PDF Pg 33):

next() → moves cursor to next row

previous() → moves to previous row

first() → moves to first row

last() → moves to last row

absolute(n) → jumps to row number n

Example
ResultSet rs = stmt.executeQuery("SELECT * FROM employee");

rs.next();     // row 1
rs.next();     // row 2
rs.previous(); // back to row 1
rs.absolute(5); // jump to row 5


These operations help move back and forth in the record set.

⭐ 5. Explain Flow of Data: Client → JSP → JDBC → DB → Response

📚 PDF Architecture Pg 36–38

FULL THEORY ANSWER (Diagram Included)

When a user interacts with a JSP-based web application, the data flow follows these steps:

1. Client (Browser)

User enters data in an HTML form and submits it.

2. JSP Page Receives Request

Form data is captured using request.getParameter().

3. JDBC Connection

JSP loads the JDBC driver, establishes a DB connection, creates statements, and executes queries.

4. Database Execution

MySQL/Oracle processes the query and returns the result.

5. JSP Processes Result

ResultSet data is rendered into dynamic HTML.

6. Response Sent to Client

JSP outputs final formatted HTML.

Flow Diagram (PDF Style)
Client (Browser)
        ↓
      JSP Page
        ↓
     JDBC API
        ↓
    Database
        ↓
   ResultSet → JSP
        ↓
  HTML Response

⭐ 6. JSP Program to Display Students Using Text Block + Record (Java 17)
FULL THEORY ANSWER

Record (PDF Pg 117–121) → simple immutable data class

Text Block (Pg 112–116) → multi-line string literal

JSP embeds Java → display table dynamically

Code
<%@ page import="java.sql.*, java.util.*" %>

<%
record Student(int id, String name, String address) {}

List<Student> list = new ArrayList<>();

Class.forName("com.mysql.cj.jdbc.Driver");
Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/aia","root","admin");

ResultSet rs = con.createStatement().executeQuery(
    "SELECT id, name, address FROM student");

while(rs.next()) {
    list.add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3)));
}

String table = """
    <table border='1'>
        <tr><th>ID</th><th>Name</th><th>Address</th></tr>
    """;

out.println(table);

for(Student s : list) {
    out.println("<tr><td>"+s.id()+"</td><td>"+s.name()+"</td><td>"+s.address()+"</td></tr>");
}

out.println("</table>");
%>

⭐ 7. Case Study: Employee JSP–JDBC Application
FULL THEORY ANSWER

This application demonstrates:

Input handling using JSP

JDBC Connection

PreparedStatement

Dynamic result display

Implicit objects (request, out)

Flow:

Employee ID → JSP → JDBC → Database → Name/Dept → JSP output

(Same code as Q3, but explained as an “application.”)

⭐ 8. Case Study: Bank Account with Two Threads + Modern Java Features
FULL THEORY ANSWER

This checks:

Thread synchronization

Text Blocks

Records

Sealed classes

Program theory:

synchronized ensures only one thread updates balance at a time.

record stores transaction info immutably.

sealed class restricts inheritance.

text block prints formatted output.

(Complete implementation already provided in previous message.)
