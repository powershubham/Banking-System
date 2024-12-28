package BankingManagementSystem;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class User {
    private Connection con;
    private Scanner sc;

    public User(Connection con, Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    public void register(){
        sc.nextLine();
        System.out.println("Full Name: ");
        String fullName = sc.nextLine();
        System.out.println("Email: ");
        String email = sc.nextLine();
        System.out.println("Password: ");
        String password = sc.nextLine();

        if(user_exist(email)){
            System.out.println("User already exists for this Email Address!");
            return;
        }

        String sql = "INSERT INTO user_table VALUES(?,?,?)";
        try{
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int affectedStatement=preparedStatement.executeUpdate();
            if(affectedStatement>0){
                System.out.println("Registration Successful!");
            }else{
                System.out.println("Registration Failed!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public String login(){
        sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        String sql = "SELECT * FROM user_table WHERE email = ? AND password = ?";
        try{
            PreparedStatement preparedStatement=con.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return email;
            }else{
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean user_exist(String email){
        String sql = "SELECT * FROM user_table WHERE email = ?";
        try{
            PreparedStatement preparedStatement=con.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
