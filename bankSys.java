

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Scanner;
import java.util.Random;

class User {
    private String name;
    private String password;
    private String accNumber;
    private double balance;
    private int pin;

    //contructor
    public User(String name,String password,String accNumber,double balance,int pin){
        this.name = name;
        this.password = password;
        this.accNumber = accNumber;
        this.pin = pin;
        this.balance = balance;
    }

    // Getters
    public String getName() { return name; }
    public double getBalance() { return balance; }
    public int getPin() { return pin; }
    public String getAccountNumber() { return accNumber; }
    public String getPassword() { return password; }

    // Setters
    public void setName(String name) {this.name = name;}
    public void setPassword(String password) {this.password = password;}
    public void setAccNumber(String accNumber) {this.accNumber = accNumber;}
    public void setBalance(double balance) {this.balance = balance;}
    public void setPin(int pin) {this.pin = pin;}
}

public class bankSys {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/bankdb";
    private static final String user = "root";
    private static final String password = "Mysql@907!Md";
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Random random = new Random();
        boolean running = true;


        System.out.println("Welcome to Islamic Bank");

        while (running) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Register your Account");
            System.out.println("2. Login to your Account");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int option = scan.nextInt();
            scan.nextLine();

            switch (option) {
                case 1:
                    registerAccount(scan, random);
                    break;
                case 2:
                    loginAccount(scan);
                    break;
                case 3:
                    System.out.println("Thank you for visiting. Hope you have a good time.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose (1/2/3)");
            }
        }
        scan.close();
    }

    static void registerAccount(Scanner scan, Random random) {
        
        System.out.println("\n--- Account Registration ---");
        User newUser = new User("Basshar","123456","0000000000",5000.0,0);
        boolean validName = false;
        boolean validPass = false;

        // Get Name
        while (!validName) {
            System.out.print("Enter your name: ");
            String userName = scan.nextLine();
            if (userName.length() < 3) {
                System.out.println("Name should be at least 3 characters.");
            } else {
                newUser.setName(userName); 
                validName = true;
            }
        }

        // Get Password
        while (!validPass) {
            System.out.print("Enter your password: ");
            String password = scan.nextLine();
            if (password.length() < 6) {
                System.out.println("Password should be at least 6 characters.");
            } else {
                newUser.setPassword(password);;

                // Generate unique account number
                String accNum;
                do {
                    long num = 1122509032 + random.nextLong(10000, 100000 * (newUser.getName().length() + password.length()));
                    accNum = Long.toString(num);
                } while (valid_acc_number(accNum));
                newUser.setAccNumber(accNum);;

                // Generate pin & balance
                newUser.setPin(random.nextInt(121122, 999999));
                newUser.setBalance(random.nextInt(1000, 10000 * newUser.getName().length()));

                
                acc_holders(newUser.getAccountNumber(), newUser.getName(), newUser.getPassword(), newUser.getPin());
                System.out.printf("\nAccount Number: %s\nAccount Pin: %d\n", accNum, newUser.getPin());
                System.out.println("Please log in and change your pin for security.");

                validPass = true;
            }
        }
    }

    static void loginAccount(Scanner scan) {
        System.out.println("\n--- Login ---");
        System.out.print("Enter your account number: ");
        String accNum = scan.nextLine();


        if (!valid_acc_number(accNum)) {
            System.out.println("Account not found. Please check the number or register.");
            return;
        }

        int attempts = 3;

        while (attempts > 0) {
            System.out.print("Enter your password: ");
            String pass = scan.nextLine();

            if (get_acc_password(accNum).equals(pass)) {
                System.out.println("\nWelcome, " + get_acc_name(accNum));
                userSession(scan, accNum);
                return;
            } else {
                attempts--;
                System.out.println("Incorrect password. Attempts left: " + attempts);
            }
        }

        System.out.println("Too many failed attempts. Account is locked temporarily.");
    }

    static void userSession(Scanner scan,String accNum) {
        boolean session = true;

        while (session) {
            System.out.println("\nAccount Menu:");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Transfer Money");
            System.out.println("4. Change PIN");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int option = scan.nextInt();
            scan.nextLine();

            switch (option) {
                case 1:
                    System.out.printf("Current Balance: $%.2f%n", get_acc_balance(accNum));
                    break;
                case 2:
                    depositMoney(scan, accNum);
                    break;
                case 3:
                    transferMoney(scan, accNum);
                    break;
                case 4:
                    changePin(scan, accNum);
                    break;
                case 5:
                    System.out.println("Logged out successfully.");
                    session = false;
                    break;
                default:
                    System.out.println("Invalid option. Choose (1/2/3/4/5).");
            }
        }
    }

    static void depositMoney(Scanner scan,String accNum) {
        System.out.print("Enter amount to deposit: ");
        double amount = scan.nextDouble();
        scan.nextLine();

        if (verifyPin(scan, accNum)) {
            set_acc_balance(accNum, (get_acc_balance(accNum) + amount)); 
            System.out.printf("$%.2f deposited. New Balance: $%.2f%n", amount,get_acc_balance(accNum));
        }
    }

    static void transferMoney(Scanner scan, String accNum) {
        System.out.print("Enter amount to transfer: ");
        double amount = scan.nextDouble();
        scan.nextLine();

        if (amount > get_acc_balance(accNum)) {
            System.out.println("Insufficient balance.");
            return;
        }

        if (verifyPin(scan, accNum)) {
            set_acc_balance(accNum, (get_acc_balance(accNum) - amount));
            System.out.printf("$%.2f transferred. New Balance: $%.2f%n", amount, get_acc_balance(accNum));
        }
    }

    static void changePin(Scanner scan, String accNum) {
        System.out.print("Enter current pin: ");
        int oldPin = scan.nextInt();
        scan.nextLine();

        if (get_acc_pin(accNum) == oldPin) {
            while (true) {
                System.out.print("Enter new 6-digit pin: ");
                int newPin = scan.nextInt();
                scan.nextLine();

                if (String.valueOf(newPin).length() == 6) {
                    set_acc_pin(accNum, newPin);;
                    System.out.println("Pin changed successfully.");
                    break;
                } else {
                    System.out.println("Pin must be exactly 6 digits.");
                }
            }
        } else {
            System.out.println("Incorrect current pin.");
        }
    }

    static boolean verifyPin(Scanner scan, String accNum) {
        int attempts = 3;

        while (attempts > 0) {
            System.out.print("Enter your 6-digit pin: ");
            int pin = scan.nextInt();
            scan.nextLine();

            if (get_acc_pin(accNum) == pin) return true;
            else {
                attempts--;
                System.out.println("Incorrect pin. Attempts left: " + attempts);
            }
        }

        System.out.println("Too many failed pin attempts. Session ended.");
        return false;
    }

    static void acc_holders(String acc_number,String acc_name,String acc_password,int acc_pin){
        try{
            Connection connection = getConnection();
            String query = "INSERT INTO accounts(acc_number,acc_name,acc_password,acc_pin) VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,acc_number);
            preparedStatement.setString(2,acc_name);
            preparedStatement.setString(3,acc_password);
            preparedStatement.setInt(4,acc_pin);
            int res = preparedStatement.executeUpdate();
            if(res > 0){
                System.out.println("Account has been created!!");
            }else{
                System.out.println("Error in creating account.");
            }
            connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    static boolean valid_acc_number(String acc_number){
        try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
             "SELECT 1 FROM accounts WHERE acc_number = ? LIMIT 1")) {
        
        preparedStatement.setString(1, acc_number);
        
        try (ResultSet rs = preparedStatement.executeQuery()) {
            return rs.next();
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
        return false;
    }

    static String get_acc_name(String acc_number){
        try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
             "SELECT acc_name FROM accounts WHERE acc_number = ? LIMIT 1")) {
        
        preparedStatement.setString(1, acc_number);
        
        try (ResultSet rs = preparedStatement.executeQuery()) {
            if(rs.next()){
                return rs.getString("acc_name");
            }
        }
        
        } catch (SQLException e) {
        e.printStackTrace();
        }   
        return null;
    }
    static String get_acc_password(String acc_number){
        try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
             "SELECT acc_password FROM accounts WHERE acc_number = ? LIMIT 1")) {
        
        preparedStatement.setString(1, acc_number);
        
        try (ResultSet rs = preparedStatement.executeQuery()) {
            if(rs.next()){
                return rs.getString("acc_password");
            }
        }
        
        } catch (SQLException e) {
        e.printStackTrace();
        }   
        return null;
    }
    static Double get_acc_balance(String acc_number){
        try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
             "SELECT acc_balance FROM accounts WHERE acc_number = ? LIMIT 1")) {
        
        preparedStatement.setString(1, acc_number);
        
        try (ResultSet rs = preparedStatement.executeQuery()) {
            if(rs.next()){
                return rs.getDouble("acc_balance");
            }
        }
        
        } catch (SQLException e) {
        e.printStackTrace();
        }   
        return null;
    }
    static int get_acc_pin(String acc_number){
        try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
             "SELECT acc_pin FROM accounts WHERE acc_number = ? LIMIT 1")) {
        
        preparedStatement.setString(1, acc_number);
        
        try (ResultSet rs = preparedStatement.executeQuery()) {
            if(rs.next()){
                return rs.getInt("acc_pin");
            }
        }
        
        } catch (SQLException e) {
        e.printStackTrace();
        }   
        return 0;
    }
    static void set_acc_balance(String acc_number,Double updatedAmount){
        try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
             "UPDATE accounts SET acc_balance = ? WHERE acc_number = ?")) {
        
        preparedStatement.setDouble(1, updatedAmount);
        preparedStatement.setString(2, acc_number);
        
        int rowAffected = preparedStatement.executeUpdate();
        if(rowAffected >0){
            System.out.println("amount updated");
        }
        else{
            System.out.println("something went wrong");
        }
        
        } catch (SQLException e) {
        e.printStackTrace();
        }   
    }
    static void set_acc_pin(String acc_number,int newPin){
        try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
             "UPDATE accounts SET acc_pin = ? WHERE acc_number = ?")) {
        
        preparedStatement.setDouble(1, newPin);
        preparedStatement.setString(2, acc_number);
        
        int rowAffected = preparedStatement.executeUpdate();
        if(rowAffected >0){
            System.out.println("pin updated");
        }
        else{
            System.out.println("something went wrong");
        }
        
        } catch (SQLException e) {
        e.printStackTrace();
        }   
    }
}