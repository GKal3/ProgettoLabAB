/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
 */
package bookrecommender.dao;

import java.sql.*;
import java.util.List;

/**
 * Class responsible for user registration, login, and checking existing users.
 * It interacts with the "UtentiRegistrati" table in the database.
 */
public class Registra {

    /**
     * Database connection used to interact with user data.
     */
    private Connection conn;
    
    /**
     * Constructor that initializes the Registra object with a database connection.
     * @param conn the database connection to be used for operations
     */
    public Registra (Connection conn) {
        this.conn = conn;
    }

    /**
     * Registers a new user in the "UtentiRegistrati" table.
     * @param data an array containing user data: Name_Surname, CF, Email, UserID, Password
     * @return true if registration was successful, false otherwise
     */
    public boolean registrazione (String[] data) {
        String query = """
            INSERT INTO \"UtentiRegistrati\" (\"Name_Surname\", \"CF\", \"Email\", \"UserID\", \"Password\")
            VALUES (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, data[0]); 
            ps.setString(2, data[1]); 
            ps.setString(3, data[2]); 
            ps.setString(4, data[3]); 
            ps.setString(5, data[4]); 
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; 
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }

    /**
     * Checks if a user with the given UserID, CF, or Email already exists in the database.
     * @param userID the UserID to check
     * @param cf the CF to check
     * @param email the Email to check
     * @return "USER_EXISTS", "CF_EXISTS", "EMAIL_EXISTS", or "OK" if no conflicts are found
     */
    public String checkReg (String userID, String cf, String email) {
        String query = """
            SELECT * FROM \"UtentiRegistrati\"
            WHERE \"UserID\" = ? OR \"CF\" = ? OR \"Email\" = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, userID);
            ps.setString(2, cf);
            ps.setString(3, email);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("UserID").equals(userID)) {
                    return "USER_EXISTS"; 
                } else if (rs.getString("CF").equals(cf)) {
                    return "CF_EXISTS"; 
                } else if (rs.getString("Email").equals(email)) {
                    return "EMAIL_EXISTS"; 
                }
            }
            return "OK"; 
        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR"; 
        }
    }

    /**
     * Logs in a user by checking the provided UserID and Password against the database.
     * @param userID the UserID of the user trying to log in
     * @param password the Password of the user trying to log in
     * @return an array containing user data if login is successful, null otherwise
     */
    public String [] login (String userID, String password) {
        String query = """
            SELECT * FROM \"UtentiRegistrati\"
            WHERE \"UserID\" = ? AND \"Password\" = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, userID);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String [] userData = new String[5];
                userData[0] = rs.getString("Name_Surname");
                userData[1] = rs.getString("CF");
                userData[2] = rs.getString("Email");
                userData[3] = rs.getString("UserID");
                userData[4] = rs.getString("Password");
                return userData; 
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; 
        }
    }
}