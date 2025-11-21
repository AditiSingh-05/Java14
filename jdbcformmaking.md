

---

# ⭐ **1) JSP Login Validation (username + password) — if correct → welcome.jsp, else → error.jsp**

## 💙 **Formal Exam Answer**

A JSP page can take user input and validate it using JDBC.
If the record exists in the database, the user is forwarded to *welcome.jsp*.
Otherwise, the user is forwarded to *error.jsp*.

## 💙 **JSP Code: login.jsp**

```jsp
<form action="validate.jsp" method="post">
    Username: <input type="text" name="user"><br>
    Password: <input type="password" name="pass"><br>
    <input type="submit" value="Login">
</form>
```

## 💙 **JSP Code: validate.jsp (WITH JDBC)**

```jsp
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
```

---

# ⭐ **2) JSP code to retrieve data from database and show in table**

## 💙 **Formal Exam Answer**

A JSP page can fetch database records using JDBC and generate an HTML table.

## 💙 **Code**

```jsp
<%@ page import="java.sql.*" %>

<table border="1">
<tr><th>Name</th><th>Address</th><th>Mobile</th></tr>

<%
Class.forName("com.mysql.cj.jdbc.Driver");
Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/aia","root","admin"
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
```

---

# ⭐ **3) JSP + JDBC program: Enter Employee ID → Show name & department**

## 💙 **Formal Exam Answer**

The JSP reads the employee ID, queries the database using PreparedStatement, and prints the employee details.

## 💙 **Form Page: empForm.jsp**

```jsp
<form action="empDetails.jsp" method="post">
    Enter Employee ID: <input type="text" name="eid">
    <input type="submit" value="Search">
</form>
```

## 💙 **empDetails.jsp**

```jsp
<%@ page import="java.sql.*" %>

<%
int id = Integer.parseInt(request.getParameter("eid"));

Class.forName("com.mysql.cj.jdbc.Driver");
Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/aia","root","admin"
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
    out.println("No employee found.");
}
%>
```

---

# ⭐ **4) Explain cursor movement in ResultSet (next(), previous(), absolute())**

## 💙 **Formal Exam Answer**

A ResultSet cursor points to rows returned by a SQL query.
We can move this cursor using various methods:

1. **next()** → moves to next row
2. **previous()** → moves to previous row
3. **absolute(n)** → jumps to row number *n*

## 💙 **Example**

```java
ResultSet rs = stmt.executeQuery("SELECT * FROM employee");

rs.next();        // moves to row 1
rs.next();        // moves to row 2
rs.previous();    // back to row 1
rs.absolute(3);   // jumps to row 3
```

---

# ⭐ **5) Flow of Client → JSP → JDBC → Database → JSP (diagram)**

## 💙 **Formal Exam Answer**

The data flow for JSP–JDBC based applications:

1. Client fills form
2. Form sends data to JSP
3. JSP uses JDBC to connect to database
4. Database returns data
5. JSP displays result to client

## 💙 **Diagram**

```
Client (Browser)
        ↓
 HTML Form Submit
        ↓
      JSP Page
        ↓
   JDBC Connection
        ↓
    Database Query
        ↓
   ResultSet to JSP
        ↓
Final Output to Client
```

---

# ⭐ **6) JSP program to display all student records using Text Blocks + Records (Java 17)**

## 💙 **Formal Exam Answer**

Java Records are used to store data in compact form.
Text Blocks allow writing multi-line HTML easily.

## 💙 **Code**

```jsp
<%@ page import="java.sql.*, java.util.*" %>

<%
record Student(int id, String name, String address) {}

List<Student> list = new ArrayList<>();

Class.forName("com.mysql.cj.jdbc.Driver");
Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/aia","root","admin"
);

ResultSet rs = con.createStatement().executeQuery("SELECT * FROM student");

while(rs.next()) {
    list.add(new Student(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("address")
    ));
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
```

---

# ⭐ **7) Case Study: JSP–JDBC application for employee info**

## 💙 **Formal Exam Answer**

A JSP page takes employee ID from the user, queries the database, and displays name and department.

## 💙 **Form: empForm.jsp**

```jsp
<form action="showEmp.jsp" method="post">
    Employee ID: <input type="text" name="id">
    <input type="submit" value="Search">
</form>
```

## 💙 **Processing + Display: showEmp.jsp**

```jsp
<%@ page import="java.sql.*" %>

<%
int id = Integer.parseInt(request.getParameter("id"));

Class.forName("com.mysql.cj.jdbc.Driver");
Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/aia","root","admin"
);

PreparedStatement ps = con.prepareStatement(
    "SELECT name, dept FROM employee WHERE id=?"
);
ps.setInt(1, id);

ResultSet rs = ps.executeQuery();

if(rs.next()) {
    out.println("Employee Name: " + rs.getString("name") + "<br>");
    out.println("Department: " + rs.getString("dept"));
} else {
    out.println("Employee Not Found.");
}
%>
```

---

# 🌟 DONE.

