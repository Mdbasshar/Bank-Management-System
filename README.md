# 🏦 Bank Management System (Java + MySQL)

A secure, console-based **Banking Application** built using **Java**, **MySQL**, and **JDBC**.  
It allows users to register, log in securely, manage account balance, transfer money, and update PINs, with real-time database persistence.

---

## 📋 Features

- 🔒 User Registration & Secure Login (Password + 6-digit PIN)
- 💰 Deposit and Withdraw Money
- 🔄 Transfer Money to Other Accounts
- 🧾 Check Account Balance
- 🔑 Change Account PIN securely
- 🗄 MySQL Database Integration with JDBC
- 🧹 Clean Codebase (OOP + DRY Principle)

---

## 🛠 Tech Stack

- **Programming Language:** Java
- **Database:** MySQL
- **Database Connectivity:** JDBC API
- **Tools:** MySQL Workbench, VS Code / IntelliJ IDEA

---


---

## 🗄 Database Setup

Run this SQL script to create your database and accounts table:

```sql
CREATE DATABASE IF NOT EXISTS bankdb;

USE bankdb;

CREATE TABLE accounts (
    acc_number VARCHAR(20) PRIMARY KEY,
    acc_name VARCHAR(50) NOT NULL,
    acc_password VARCHAR(50) NOT NULL,
    acc_pin INT(6) NOT NULL,
    acc_balance DOUBLE DEFAULT 0
);
```
## How to Run Locally
clone this repo;
```
git clone https://github.com/yourusername/bank-management-system.git
cd bank-management-system
```
cofigure your database detail inside;
```
private static final String url = "url_connector";
private static final String user = "your_username";
private static final String password = "your_mysql_password";
```
Finally, compile and run
```
javac bankSys.java
java bankSys
```

# License
This project is open source and free to use under the MIT License.

# Author
MD Basshar
## LinkedIn 
[Connect with me on LinkedIn](https://linkedin.com/in/md-basshar-1b7aaa247)
