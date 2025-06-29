package bookrecommender.dao;

import java.sql.*;
import java.util.List;

public class Registra {
    private Connection conn;
    
    public Registra (Connection conn) {
        this.conn = conn;
    }

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