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
            ps.setString(1, data[0]); // Name_Surname
            ps.setString(2, data[1]); // CF
            ps.setString(3, data[2]); // Email
            ps.setString(4, data[3]); // UserID
            ps.setString(5, data[4]); // Password
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Restituisce true se l'inserimento è andato a buon fine
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Restituisce false in caso di errore
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
                return userData; // Restituisce i dati dell'utente se il login ha successo
            } else {
                return null; // Restituisce null se le credenziali non sono corrette
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Restituisce null in caso di eccezione
        }
    }
}