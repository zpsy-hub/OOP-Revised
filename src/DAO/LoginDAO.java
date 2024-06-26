package DAO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;
import service.SQL_client;

public class LoginDAO {
    private static final String HASH_ALGORITHM = "SHA-256";

    public LoginDAO() {
    }

    public User authenticateUser(String username, String password) {
        try {
            Connection conn = SQL_client.getInstance().getConnection(); 
            if (conn == null) {
                System.err.println("Database connection is null.");
                return null;
            }
            
            // Query to join user and employee tables
            PreparedStatement ps = conn.prepareStatement(
                "SELECT u.user_id, u.username, u.password, e.emp_id, e.last_name, e.first_name " +
                "FROM payrollsystem_db.user u " +
                "JOIN payrollsystem_db.employee e ON u.emp_id = e.emp_id " +
                "WHERE u.username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (verifyPassword(password, storedPassword)) {
                    int empId = rs.getInt("emp_id");
                    String lastName = rs.getString("last_name");
                    String firstName = rs.getString("first_name");
                    return new User(username, null, empId, lastName, firstName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return null; 
    }

    public int getEmployeeIdByUsername(String username) {
        try {
            Connection conn = SQL_client.getInstance().getConnection(); 
            if (conn == null) {
                System.err.println("Database connection is null.");
                return -1;
            }
            
            PreparedStatement ps = conn.prepareStatement(
                "SELECT emp_id FROM payrollsystem_db.user WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("emp_id");
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return -1; 
    }
    
    public boolean updatePassword(int employeeId, String newPassword) {
        try {
            Connection conn = SQL_client.getInstance().getConnection(); 
            if (conn == null) {
                System.err.println("Database connection is null.");
                return false;
            }
            
            String hashedPassword = hashPassword(newPassword);
            
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE payrollsystem_db.user SET password = ? WHERE emp_id = ?");
            ps.setString(1, hashedPassword);
            ps.setInt(2, employeeId);
            int rowsAffected = ps.executeUpdate();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace(); 
            return false;
        }
    }

    private boolean verifyPassword(String password, String storedPassword) {
        return hashPassword(password).equals(storedPassword);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] digest = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
