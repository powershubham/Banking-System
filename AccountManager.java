package BankingManagementSystem;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;


public class AccountManager {
    private Connection con;
    private Scanner sc;

    public AccountManager(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public void debit_money(long account_number)throws SQLException{
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security Pin: ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement preparedStatement = con.prepareStatement("select * from accounts_table where account_number=? and security_pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "update accounts_table set balance = balance - ? where account_number = ?";
                        PreparedStatement preparedStatement1 = con.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, account_number);
                        int rowAffected = preparedStatement1.executeUpdate();
                        if (rowAffected > 0) {
                            System.out.println("Rs." + amount + " debited Successfully");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    } else
                        System.out.println("Insufficient Balance");
                } else
                    System.out.println("Invaild Pin!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void credit_money(long account_number)throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security Pin: ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if(account_number!=0) {
                PreparedStatement preparedStatement = con.prepareStatement("select * from accounts_table where account_number=? and security_pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String credit_query = "update accounts_table set balance = balance + ? where account_number = ?";
                    PreparedStatement preparedStatement1 = con.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, account_number);
                    int rowAffected = preparedStatement1.executeUpdate();
                    if (rowAffected > 0) {
                        System.out.println("Rs." + amount + " credited Successfully");
                        con.commit();
                        con.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Invaild Security Pin!");
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_number)throws SQLException {
        sc.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number = sc.nextLong();
        System.out.print("Enter Amount to Transfer: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();
        try{
            con.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0) {
                PreparedStatement preparedStatement=con.prepareStatement("select * from accounts_table where account_number=? and security_pin = ?");
                preparedStatement.setLong(1, sender_account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet=preparedStatement.executeQuery();
                if(resultSet.next()) {
                    double current_balacne=resultSet.getDouble("Balance");
                    if(amount<=current_balacne){
                        String debit_query = "update accounts_table set balance = balance - ? where account_number = ?";
                        String cradit_query = "update accounts_table set balance = balance + ? where account_number = ?";
                        PreparedStatement craditPreparedStatement = con.prepareStatement(cradit_query);
                        PreparedStatement debitPreparedStatement = con.prepareStatement(debit_query);
                        craditPreparedStatement.setDouble(1, amount);
                        craditPreparedStatement.setLong(2, receiver_account_number);
                        debitPreparedStatement.setDouble(1, amount);
                        debitPreparedStatement.setLong(2, sender_account_number);
                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
                        int rowsAffected2 = craditPreparedStatement.executeUpdate();
                        if(rowsAffected1>0 && rowsAffected2>0) {
                            System.out.println("Transfer Successful!");
                            System.out.println("Rs."+amount+" Transferred Successfully");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("Transaction Failed");
                        }
                    }else{
                        System.out.println("Insufficient Balance!");
                    }
                }else{
                    System.out.println("Invalid Security Pin");
                }
            }else{
                System.out.println("Invalid Account Number");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

    public void getBalance(long account_number){
        sc.nextLine();
        System.out.println("Enter Security Pin: ");
        String security_pin = sc.nextLine();
        try{
            PreparedStatement preparedStatement=con.prepareStatement("select balance from accounts_table where account_number = ? and security_pin = ?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance: "+balance);
            }else{
                System.out.println("Invalid Pin!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
