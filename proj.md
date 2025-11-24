# 📋 Task Management System - Complete Guide

## Table of Contents
1. [What This Project Is](#what-this-project-is)
2. [Technologies Used](#technologies-used)
3. [Architecture Overview](#architecture-overview)
4. [Database Setup (MySQL)](#database-setup-mysql)
5. [How JDBC Works](#how-jdbc-works)
6. [How JSP Works](#how-jsp-works)
7. [How Servlets Work](#how-servlets-work)
8. [Project Structure Explained](#project-structure-explained)
9. [How Everything Works Together](#how-everything-works-together)
10. [Setup & Installation](#setup--installation)
11. [How to Use](#how-to-use)

---

## What This Project Is

This is a **web-based Task Management System** where users can:
- Register and create an account
- Login to their account
- Create tasks with title, description, deadline, and priority
- View all their tasks
- Update task details and status
- Delete tasks
- Logout

Think of it like a simple version of Trello or Todoist.

---

## Technologies Used

### 1. **Java** (Backend Programming Language)
- The core programming language for business logic
- Handles user authentication, task operations, database interactions

### 2. **JSP (JavaServer Pages)** (Frontend)
- Creates dynamic web pages (HTML + Java code)
- Like PHP - you can mix HTML with Java code
- Gets compiled into servlets by the server

### 3. **Servlets** (Request Handlers)
- Java classes that handle HTTP requests (GET/POST)
- Process form submissions, perform actions, redirect users
- Act as controllers in MVC pattern

### 4. **JDBC (Java Database Connectivity)** (Database Communication)
- API to connect Java applications with databases
- Allows you to execute SQL queries from Java code

### 5. **MySQL** (Database)
- Stores all data (users, tasks)
- Relational database with tables and relationships

### 6. **HTML/CSS** (User Interface)
- HTML: Structure of web pages
- CSS: Styling and design

### 7. **Maven** (Build Tool)
- Manages dependencies (libraries)
- Builds and packages the project into a WAR file

### 8. **Apache Tomcat** (Web Server)
- Runs the Java web application
- Handles HTTP requests and responses

---

## Architecture Overview

This project follows the **MVC (Model-View-Controller)** pattern:

```
┌─────────────┐
│   Browser   │  ← User interacts here
└──────┬──────┘
       │ HTTP Request (form submit, link click)
       ↓
┌─────────────┐
│  SERVLET    │  ← Controller (handles requests)
│ (Controller)│     - LoginServlet, AddTaskServlet, etc.
└──────┬──────┘
       │ Calls methods
       ↓
┌─────────────┐
│    DAO      │  ← Model (business logic + data access)
│   (Model)   │     - UserDAO, TaskDAO
└──────┬──────┘
       │ SQL Queries via JDBC
       ↓
┌─────────────┐
│   MySQL     │  ← Database (stores data)
│ (Database)  │
└──────┬──────┘
       │ Returns data
       ↓
┌─────────────┐
│    JSP      │  ← View (presents data to user)
│   (View)    │     - home.jsp, login.jsp, etc.
└─────────────┘
```

---

## Database Setup (MySQL)

### What is MySQL?
MySQL is a **relational database** - it stores data in tables with rows and columns (like Excel sheets).

### Database Structure

Your project uses a database called `taskdb` with 2 tables:

#### Table 1: `users`
Stores user account information.

```sql
CREATE DATABASE taskdb;
USE taskdb;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);
```

**Columns:**
- `id`: Unique identifier for each user (auto-generated)
- `name`: User's full name
- `email`: User's email (must be unique)
- `password`: User's password (stored as plain text - NOT secure for production!)

#### Table 2: `tasks`
Stores tasks created by users.

```sql
CREATE TABLE tasks (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    deadline DATE NOT NULL,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

**Columns:**
- `id`: Unique identifier for each task
- `user_id`: ID of the user who owns this task (links to users table)
- `title`: Task title
- `description`: Task description (optional)
- `deadline`: When the task is due
- `priority`: High, Medium, or Low
- `status`: Pending, In Progress, or Completed

**Relationship:** Each task belongs to one user (one-to-many relationship)

### How to Set Up the Database

```sql
-- Step 1: Open MySQL Command Line or MySQL Workbench

-- Step 2: Create the database
CREATE DATABASE taskdb;

-- Step 3: Use the database
USE taskdb;

-- Step 4: Create users table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

-- Step 5: Create tasks table
CREATE TABLE tasks (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    deadline DATE NOT NULL,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

### Update Database Credentials

Edit `src/main/java/dao/DBConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/taskdb";
private static final String USER = "root";           // Your MySQL username
private static final String PASS = "Aditi321";       // Your MySQL password
```

---

## How JDBC Works

**JDBC (Java Database Connectivity)** is like a bridge between Java and databases.

### The JDBC Process

```
Java Code → JDBC Driver → Database → Returns Results → Java Code
```

### Step-by-Step Example

Let's say you want to add a user to the database:

```java
// 1. LOAD THE DRIVER (tells Java how to talk to MySQL)
//Class.forName("com.mysql.cj.jdbc.Driver");

// 2. ESTABLISH CONNECTION (connect to the database)
Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/taskdb",  // Database URL
    "root",                                 // Username
    "password"                              // Password
);

// 3. CREATE SQL STATEMENT
String sql = "INSERT INTO users(name, email, password) VALUES(?, ?, ?)";

// 4. PREPARE STATEMENT (? are placeholders for values)
PreparedStatement ps = con.prepareStatement(sql);
//ps.setString(1, "John Doe");        // Replace first ?
//ps.setString(2, "john@email.com");  // Replace second ?
//ps.setString(3, "password123");     // Replace third ?

// 5. EXECUTE THE QUERY
//ps.executeUpdate();  // For INSERT, UPDATE, DELETE
// OR
ResultSet rs = ps.executeQuery();  // For SELECT queries

// 6. CLOSE RESOURCES (important to prevent memory leaks)
//ps.close();
//con.close();
```

### JDBC Components

1. **DriverManager**: Manages database drivers
2. **Connection**: Represents a connection to the database
3. **PreparedStatement**: A precompiled SQL statement (prevents SQL injection)
4. **ResultSet**: Holds data returned from SELECT queries
5. **executeUpdate()**: For INSERT, UPDATE, DELETE (returns number of rows affected)
6. **executeQuery()**: For SELECT (returns ResultSet)

### In This Project: `DBConnection.java`

```java
public class DBConnection {
    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/taskdb";
    private static final String USER = "root";
    private static final String PASS = "Aditi321";

    // Load MySQL driver when class is loaded
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to get a connection
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
```

**What it does:**
- Loads the MySQL driver once when the application starts
- Provides a `getConnection()` method that other classes can use
- Centralizes database connection logic

---

## How JSP Works

**JSP (JavaServer Pages)** lets you create dynamic web pages by mixing HTML with Java code.

### JSP Lifecycle

```
1. Browser requests page.jsp
2. Tomcat converts JSP to Servlet (first time only)
3. Servlet executes Java code
4. Generates HTML output
5. Sends HTML to browser
```

### JSP Syntax

#### 1. **Scriptlets** `<% ... %>` - Execute Java code
```jsp
<%
    String username = "John";
    int count = 5;
%>
```

#### 2. **Expressions** `<%= ... %>` - Print values
```jsp
<p>Welcome, <%= username %></p>
<!-- Outputs: <p>Welcome, John</p> -->
```

#### 3. **Directives** `<%@ ... %>` - Import classes, set page properties
```jsp
<%@ page import="java.util.*, dao.TaskDAO" %>
<%@ page contentType="text/html;charset=UTF-8" %>
```

### Example: login.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <!-- Java code to check for errors -->
    <% if (request.getAttribute("error") != null) { %>
        <div class="error">⚠️ <%= request.getAttribute("error") %></div>
    <% } %>

    <!-- HTML form that submits to LoginServlet -->
    <form action="login" method="post">
        <input name="email" type="email" required>
        <input name="password" type="password" required>
        <button type="submit">Login</button>
    </form>
</body>
</html>
```

**What happens:**
1. Browser requests `login.jsp`
2. JSP checks if there's an error message
3. Displays error if it exists
4. Shows login form
5. When user submits, data goes to `/login` (LoginServlet)

### Example: home.jsp (Dynamic Content)

```jsp
<%@ page import="dao.TaskDAO, java.util.*" %>
<%
    // Check if user is logged in
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("login.jsp");  // Redirect to login
        return;
    }
    
    // Get user's tasks from database
    Integer userId = (Integer) session.getAttribute("userId");
    List<Map<String, String>> tasks = new TaskDAO().getTasks(userId);
%>
<!DOCTYPE html>
<html>
<body>
    <h1>Your Tasks</h1>
    
    <!-- Loop through tasks and display -->
    <% for (Map<String, String> task : tasks) { %>
        <div>
            <h3><%= task.get("title") %></h3>
            <p><%= task.get("description") %></p>
            <p>Deadline: <%= task.get("deadline") %></p>
        </div>
    <% } %>
</body>
</html>
```

**What happens:**
1. JSP checks if user is logged in (checks session)
2. Fetches user's tasks from database using TaskDAO
3. Loops through tasks and generates HTML for each
4. Sends final HTML to browser

### JSP Built-in Objects

- `request`: HTTP request data (form parameters, headers)
- `response`: HTTP response (redirect, set cookies)
- `session`: Store user-specific data across requests
- `out`: Print to the page (rarely used, <%= %> is preferred)

---

## How Servlets Work

**Servlets** are Java classes that handle HTTP requests (like form submissions).

### Servlet Lifecycle

```
1. User submits form or clicks link
2. Tomcat receives HTTP request
3. Tomcat calls appropriate Servlet
4. Servlet processes request (calls DAO, etc.)
5. Servlet sends response (redirect or forward)
```

### Servlet Structure

```java
@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    
    // Handles POST requests (form submissions)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException {
        
        // 1. GET FORM DATA
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        
        // 2. PROCESS DATA (call DAO)
        UserDAO userDAO = new UserDAO();
        Map<String, String> user = userDAO.getUserDetails(email, password);
        
        // 3. SEND RESPONSE
        if (user != null) {
            // Login successful
            HttpSession session = req.getSession(true);
            session.setAttribute("userId", Integer.parseInt(user.get("id")));
            session.setAttribute("username", user.get("name"));
            resp.sendRedirect("home.jsp");  // Redirect to home page
        } else {
            // Login failed
            resp.sendRedirect("login.jsp?error=1");  // Redirect back with error
        }
    }
    
    // Handles GET requests (link clicks)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException {
        // Handle GET requests here
    }
}
```

### Servlet Annotations

```java
//@WebServlet(name = "LoginServlet", urlPatterns = "/login")
```

**What it means:**
- `name`: Servlet name (for reference)
- `urlPatterns`: URL that triggers this servlet
- When user goes to `http://localhost:8080/yourapp/login`, this servlet handles it

### Request Parameters

When a form is submitted:
```html
<form action="login" method="post">
    <input name="email" value="john@email.com">
    <input name="password" value="pass123">
</form>
```

In the servlet:
```java
String email = req.getParameter("email");      // "john@email.com"
String password = req.getParameter("password"); // "pass123"
```

### Sessions

**Sessions** store user-specific data across multiple requests.

```java
// Create or get session
HttpSession session = req.getSession(true);

// Store data in session
//session.setAttribute("userId", 123);
//session.setAttribute("username", "John");

// Retrieve data from session (in JSP or another servlet)
Integer userId = (Integer) session.getAttribute("userId");
String username = (String) session.getAttribute("username");

// Remove data
//session.removeAttribute("userId");

// Destroy session (logout)
//session.invalidate();
```

**Why sessions?**
HTTP is stateless (each request is independent). Sessions let you remember who the user is across multiple pages.

### Redirect vs Forward

**Redirect** (used in this project):
```java
//resp.sendRedirect("home.jsp");
```
- Browser gets a new URL
- User sees `home.jsp` in address bar
- Two separate requests

**Forward** (alternative):
```java
//req.getRequestDispatcher("home.jsp").forward(req, resp);
```
- Server forwards internally
- User still sees original URL
- Same request

---

## Project Structure Explained

```
TaskManagementSystem/
│
├── pom.xml                          # Maven configuration (dependencies)
│
├── src/main/
│   ├── java/
│   │   ├── dao/                     # Data Access Objects (database logic)
│   │   │   ├── DBConnection.java   # Database connection manager
│   │   │   ├── UserDAO.java        # User-related database operations
│   │   │   └── TaskDAO.java        # Task-related database operations
│   │   │
│   │   └── servlets/                # Request handlers
│   │       ├── LoginServlet.java   # Handles login
│   │       ├── RegisterServlet.java # Handles registration
│   │       ├── LogoutServlet.java  # Handles logout
│   │       ├── AddTaskServlet.java # Handles adding tasks
│   │       ├── UpdateTaskServlet.java # Handles updating tasks
│   │       └── DeleteTaskServlet.java # Handles deleting tasks
│   │
│   └── webapp/                      # Web files (JSP, CSS, HTML)
│       ├── WEB-INF/
│       │   └── web.xml             # Web app configuration
│       ├── css/
│       │   └── style.css           # Styling
│       ├── index.jsp               # Landing page
│       ├── login.jsp               # Login page
│       ├── register.jsp            # Registration page
│       ├── home.jsp                # Main dashboard
│       ├── addTask.jsp             # Add task page
│       ├── viewTasks.jsp           # View all tasks
│       └── updateTask.jsp          # Update task page
│
└── target/                          # Compiled files (generated by Maven)
    └── TaskManagementSystem.war    # Deployable web archive
```

### File Purposes

#### DAO Layer (Data Access Objects)

**DBConnection.java** - Manages database connections
```java
// Provides connection to database
Connection con = DBConnection.getConnection();
```

**UserDAO.java** - User operations
- `register()`: Create new user account
- `login()`: Verify credentials
- `getUserDetails()`: Get user info

**TaskDAO.java** - Task operations
- `addTask()`: Create new task
- `getTasks()`: Get all tasks for a user
- `updateTask()`: Modify task details
- `deleteTask()`: Remove task

#### Servlet Layer (Controllers)

**LoginServlet.java** - Handles login
1. Gets email and password from form
2. Calls `UserDAO.getUserDetails()`
3. If valid, creates session and redirects to home
4. If invalid, redirects back to login with error

**RegisterServlet.java** - Handles registration
1. Gets name, email, password from form
2. Calls `UserDAO.register()`
3. Redirects to login page

**AddTaskServlet.java** - Handles adding tasks
1. Checks if user is logged in
2. Gets task details from form
3. Calls `TaskDAO.addTask()`
4. Redirects to home

**UpdateTaskServlet.java** - Handles updating tasks
1. Gets task ID and new details
2. Calls `TaskDAO.updateTask()`
3. Redirects back to home

**DeleteTaskServlet.java** - Handles deleting tasks
1. Gets task ID
2. Calls `TaskDAO.deleteTask()`
3. Redirects back to home

**LogoutServlet.java** - Handles logout
1. Destroys session
2. Redirects to login page

#### JSP Layer (Views)

**index.jsp** - Landing page (first page user sees)

**login.jsp** - Login form
- Form submits to `/login` (LoginServlet)

**register.jsp** - Registration form
- Form submits to `/register` (RegisterServlet)

**home.jsp** - Main dashboard
- Shows all user's tasks
- Fetches tasks using `TaskDAO.getTasks()`
- Displays in table format

**addTask.jsp** - Add task form
- Form submits to `/addTask` (AddTaskServlet)

**updateTask.jsp** - Update task form
- Pre-fills form with existing task data
- Form submits to `/updateTask` (UpdateTaskServlet)

**viewTasks.jsp** - View all tasks
- Similar to home.jsp
- Different layout

---

## How Everything Works Together

### User Registration Flow

```
1. User visits register.jsp
   ↓
2. Fills form: name, email, password
   ↓
3. Submits form → RegisterServlet (/register)
   ↓
4. RegisterServlet:
   - Gets form data: req.getParameter("name"), etc.
   - Creates UserDAO object
   - Calls userDAO.register(name, email, password)
   ↓
5. UserDAO.register():
   - Gets database connection from DBConnection
   - Prepares SQL: INSERT INTO users(name, email, password) VALUES(?,?,?)
   - Executes query via JDBC
   - Returns true/false
   ↓
6. RegisterServlet:
   - If successful: redirects to login.jsp
   - If failed: shows error on register.jsp
   ↓
7. User sees login page
```

### User Login Flow

```
1. User visits login.jsp
   ↓
2. Enters email and password
   ↓
3. Submits form → LoginServlet (/login)
   ↓
4. LoginServlet:
   - Gets email and password from form
   - Creates UserDAO object
   - Calls userDAO.getUserDetails(email, password)
   ↓
5. UserDAO.getUserDetails():
   - Connects to database
   - SQL: SELECT id, name FROM users WHERE email=? AND password=?
   - If found, returns Map with user data
   - If not found, returns null
   ↓
6. LoginServlet:
   - If user found:
     * Creates session: req.getSession(true)
     * Stores userId and username in session
     * Redirects to home.jsp
   - If not found:
     * Redirects to login.jsp with error
   ↓
7. User sees home page with their tasks
```

### Add Task Flow

```
1. User is on home.jsp
   ↓
2. Clicks "Add New Task" → goes to addTask.jsp
   ↓
3. Fills form: title, description, deadline, priority
   ↓
4. Submits form → AddTaskServlet (/addTask)
   ↓
5. AddTaskServlet:
   - Gets userId from session
   - If not logged in → redirect to login.jsp
   - Gets task details from form
   - Creates TaskDAO object
   - Calls taskDAO.addTask(userId, title, desc, deadline, priority)
   ↓
6. TaskDAO.addTask():
   - Connects to database
   - SQL: INSERT INTO tasks(user_id, title, description, deadline, priority, status) 
         VALUES(?,?,?,?,?,?)
   - Sets status to "Pending"
   - Executes query
   ↓
7. AddTaskServlet:
   - Redirects to home.jsp
   ↓
8. User sees home page with new task in the list
```

### View Tasks Flow

```
1. User visits home.jsp
   ↓
2. JSP code executes:
   <%
       Integer userId = (Integer) session.getAttribute("userId");
       List<Map<String, String>> tasks = new TaskDAO().getTasks(userId);
   %>
   ↓
3. TaskDAO.getTasks():
   - Connects to database
   - SQL: SELECT * FROM tasks WHERE user_id=?
   - Loops through results
   - For each row, creates Map with task data
   - Adds to List
   - Returns List
   ↓
4. JSP displays tasks:
   <% for (Map<String, String> task : tasks) { %>
       <tr>
           <td><%= task.get("title") %></td>
           <td><%= task.get("description") %></td>
           ...
       </tr>
   <% } %>
   ↓
5. Browser shows table with all tasks
```

### Update Task Flow

```
1. User clicks "Edit" on a task → goes to updateTask.jsp?id=5
   ↓
2. updateTask.jsp:
   - Gets task ID from URL
   - Fetches task details from database
   - Pre-fills form with current values
   ↓
3. User modifies fields and submits → UpdateTaskServlet (/updateTask)
   ↓
4. UpdateTaskServlet:
   - Gets task ID and new values from form
   - Calls taskDAO.updateTask(id, title, desc, deadline, priority, status)
   ↓
5. TaskDAO.updateTask():
   - SQL: UPDATE tasks SET title=?, description=?, deadline=?, priority=?, status=? 
         WHERE id=?
   - Executes query
   ↓
6. UpdateTaskServlet:
   - Redirects to home.jsp
   ↓
7. User sees updated task
```

### Delete Task Flow

```
1. User clicks "Delete" on a task
   ↓
2. JavaScript confirms: "Are you sure?"
   ↓
3. Redirects to DeleteTaskServlet (/deleteTask?id=5)
   ↓
4. DeleteTaskServlet:
   - Gets task ID from URL
   - Calls taskDAO.deleteTask(id)
   ↓
5. TaskDAO.deleteTask():
   - SQL: DELETE FROM tasks WHERE id=?
   - Executes query
   ↓
6. DeleteTaskServlet:
   - Redirects to home.jsp
   ↓
7. Task is removed from list
```

### Logout Flow

```
1. User clicks "Logout"
   ↓
2. Redirects to LogoutServlet (/logout)
   ↓
3. LogoutServlet:
   - Gets session: req.getSession(false)
   - Destroys session: session.invalidate()
   - Redirects to login.jsp
   ↓
4. User is logged out, session data cleared
```

---

## Setup & Installation

### Prerequisites

1. **Java JDK 8 or higher**
   - Download: https://www.oracle.com/java/technologies/javase-downloads.html
   - Verify: `java -version`

2. **Apache Tomcat 9**
   - Download: https://tomcat.apache.org/download-90.cgi
   - Extract to a folder (e.g., C:\tomcat)

3. **MySQL Server**
   - Download: https://dev.mysql.com/downloads/mysql/
   - Install and remember root password

4. **Maven** (build tool)
   - Download: https://maven.apache.org/download.cgi
   - Add to PATH
   - Verify: `mvn -version`

5. **IDE** (optional but recommended)
   - IntelliJ IDEA or Eclipse

### Installation Steps

#### Step 1: Set Up Database

```sql
-- Open MySQL Command Line or MySQL Workbench

-- Create database
CREATE DATABASE taskdb;

-- Use database
USE taskdb;

-- Create users table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

-- Create tasks table
CREATE TABLE tasks (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    deadline DATE NOT NULL,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

#### Step 2: Update Database Credentials

Edit `src/main/java/dao/DBConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/taskdb";
private static final String USER = "root";        // Your MySQL username
private static final String PASS = "YOUR_PASSWORD"; // Your MySQL password
```

#### Step 3: Build the Project

Open Command Prompt in project directory:
```bash
mvn clean package
```

This creates `target/TaskManagementSystem.war`

#### Step 4: Deploy to Tomcat

**Option A: Manual Deployment**
1. Copy `target/TaskManagementSystem.war` to `<TOMCAT_HOME>/webapps/`
2. Start Tomcat: `<TOMCAT_HOME>/bin/startup.bat` (Windows) or `./startup.sh` (Linux/Mac)
3. Tomcat automatically deploys the WAR file

**Option B: IDE Deployment**
1. Configure Tomcat in your IDE
2. Run/Debug the project
3. IDE handles deployment

#### Step 5: Access the Application

Open browser and go to:
```
http://localhost:8080/TaskManagementSystem/
```

---

## How to Use

### 1. Register a New Account
1. Go to http://localhost:8080/TaskManagementSystem/
2. Click "Register here"
3. Fill in name, email, password
4. Click "Register"

### 2. Login
1. Enter email and password
2. Click "Login"
3. You'll be redirected to home page

### 3. Add a Task
1. On home page, click "Create New Task"
2. Fill in:
   - Task Title
   - Description (optional)
   - Deadline
   - Priority (High/Medium/Low)
3. Click "Create Task"

### 4. View Tasks
- Your tasks are displayed on the home page
- You can see:
  - Title
  - Description
  - Deadline
  - Priority
  - Status (Pending/In Progress/Completed)

### 5. Update a Task
1. Click "Edit" button on a task
2. Modify fields
3. Click "Update Task"

### 6. Delete a Task
1. Click "Delete" button on a task
2. Confirm deletion
3. Task is removed

### 7. Logout
- Click "Logout" button in top-right corner

---

## Key Concepts Summary

### What is a Web Application?

A web application runs on a server and users access it through a browser. Unlike desktop apps, users don't install anything.

**Flow:**
```
Browser → HTTP Request → Server → Process → HTTP Response → Browser
```

### What is HTTP?

HTTP (HyperText Transfer Protocol) is how browsers and servers communicate.

**HTTP Methods:**
- **GET**: Request data (clicking a link)
- **POST**: Submit data (submitting a form)

**Example:**
```
GET /home.jsp HTTP/1.1
Host: localhost:8080
```

### What is a Session?

HTTP is stateless (doesn't remember previous requests). Sessions solve this by storing user data on the server.

**Example:**
```java
// Login servlet stores user ID
//session.setAttribute("userId", 123);

// Home page retrieves it
Integer userId = (Integer) session.getAttribute("userId");
```

### What is MVC?

**Model-View-Controller** pattern separates concerns:

- **Model (DAO)**: Business logic and data access
- **View (JSP)**: User interface
- **Controller (Servlet)**: Handles requests, coordinates Model and View

**Benefits:**
- Easier to maintain
- Changes to UI don't affect business logic
- Multiple developers can work on different parts

### What is Maven?

Maven is a build tool that:
- Manages dependencies (libraries)
- Compiles code
- Packages into WAR file
- Runs tests

**pom.xml** is the configuration file:
```xml
<dependencies>
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>4.0.1</version>
    </dependency>
</dependencies>
```

Maven downloads these libraries automatically.

### What is a WAR file?

**WAR (Web Application Archive)** is a packaged web application.

It contains:
- Compiled Java classes (.class files)
- JSP files
- HTML/CSS/JavaScript
- Libraries (JAR files)
- Configuration (web.xml)

You deploy the WAR file to Tomcat, and it runs your application.

---

## Common Issues & Solutions

### Issue 1: "Cannot connect to database"

**Solution:**
1. Check MySQL is running
2. Verify database exists: `SHOW DATABASES;`
3. Check credentials in `DBConnection.java`
4. Check MySQL port (default: 3306)

### Issue 2: "404 Not Found"

**Solution:**
1. Check Tomcat is running
2. Verify WAR file is in `webapps` folder
3. Check URL: `http://localhost:8080/TaskManagementSystem/`
4. Wait for Tomcat to deploy (check `webapps/TaskManagementSystem` folder exists)

### Issue 3: "Session not found / Redirects to login"

**Solution:**
- Session expired (default: 30 minutes)
- Login again

### Issue 4: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"

**Solution:**
1. Check `mysql-connector-j-8.0.33.jar` is in `WEB-INF/lib`
2. Rebuild project: `mvn clean package`

### Issue 5: "Port 8080 already in use"

**Solution:**
1. Stop Tomcat
2. Change port in `<TOMCAT_HOME>/conf/server.xml`
3. Or kill process using port 8080

---

## Security Notes

**⚠️ This project is for LEARNING purposes. For production, you MUST:**

1. **Hash passwords**: Don't store plain text passwords
   - Use BCrypt or similar
   
2. **Use HTTPS**: Encrypt data in transit

3. **Prevent SQL Injection**: This project uses PreparedStatement (good!)
   - Never concatenate user input into SQL queries

4. **Add input validation**: Validate all user inputs

5. **Use connection pooling**: Don't create new connections for each request

6. **Add error handling**: Don't expose stack traces to users

7. **Implement CSRF protection**: Prevent cross-site request forgery

8. **Add authentication filters**: Protect all pages that require login

---

## Next Steps to Improve

1. **Add password hashing** (BCrypt)
2. **Add email verification** for registration
3. **Add password reset** functionality
4. **Add task filtering** (by status, priority, date)
5. **Add search** functionality
6. **Add pagination** for large task lists
7. **Add task categories/tags**
8. **Add file attachments** to tasks
9. **Add notifications** for upcoming deadlines
10. **Improve UI** with Bootstrap or Material Design
11. **Add REST API** for mobile app integration
12. **Add unit tests**
13. **Deploy to cloud** (AWS, Heroku, etc.)

---

## Resources

### Learn More

**Java:**
- Oracle Java Tutorials: https://docs.oracle.com/javase/tutorial/

**Servlets & JSP:**
- Oracle Servlet Tutorial: https://docs.oracle.com/javaee/7/tutorial/servlets.htm
- JSP Tutorial: https://docs.oracle.com/javaee/7/tutorial/jsps.htm

**JDBC:**
- Oracle JDBC Tutorial: https://docs.oracle.com/javase/tutorial/jdbc/

**MySQL:**
- MySQL Documentation: https://dev.mysql.com/doc/
- W3Schools SQL: https://www.w3schools.com/sql/

**Maven:**
- Maven Getting Started: https://maven.apache.org/guides/getting-started/

**Tomcat:**
- Tomcat Documentation: https://tomcat.apache.org/tomcat-9.0-doc/

---

## Conclusion

You now have a complete understanding of:
- How web applications work
- How JSP creates dynamic pages
- How Servlets handle requests
- How JDBC connects to databases
- How MySQL stores data
- How everything works together in MVC pattern

**The Flow:**
1. User interacts with **JSP** (View)
2. Sends request to **Servlet** (Controller)
3. Servlet calls **DAO** (Model)
4. DAO uses **JDBC** to talk to **MySQL** (Database)
5. Data flows back to JSP
6. User sees result

