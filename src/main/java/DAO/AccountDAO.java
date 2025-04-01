package DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
     public List<Account> RetrieveAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM accounts;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getString("username"),rs.getString("password"));
                accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }
    public boolean registerAccount(Account account) {
      Connection connection = ConnectionUtil.getConnection();
      try {
          String sql = "INSERT INTO accounts (username, password) VALUES (?, ?);";
          PreparedStatement preparedStatement = connection.prepareStatement(sql);
          preparedStatement.setString(1, account.getUsername());
          preparedStatement.setString(2, account.getPassword());
        
          int rowsAffected = preparedStatement.executeUpdate();
          if(rowsAffected > 0){
            return true;
          }
          else{
            return false;
          }
      } catch(SQLException e) {
          System.out.println(e.getMessage());
          return false;
      }
  }
  public Account login(String username, String password) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "SELECT * FROM accounts WHERE username = ? AND password = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()) {
            return new Account(rs.getString("username"), rs.getString("password"));
        }
    } catch(SQLException e) {
        System.out.println(e.getMessage());
    }
    return null;
   }
   public boolean usernameExists(String username) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "SELECT * FROM accounts WHERE username = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next(); // Returns true if username exists
    } catch(SQLException e) {
        System.out.println(e.getMessage());
        return false;
    }
  }
}
