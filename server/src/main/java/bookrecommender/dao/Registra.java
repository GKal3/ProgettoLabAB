package bookrecommender.dao;

import java.sql.*;
import java.util.List;

public class Registra {
    private Connection conn;
    
    public Registra (Connection conn) {
        this.conn = conn;
    }

    public boolean registrazione (List<String> data) {
        String query = """
            INSERT INTO \"UtentiRegistrati\" (\"Name_Surname\", \"CF\", \"Email\", \"UserID\", \"Password\")
            VALUES (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, data.get(0)); // Name_Surname
            ps.setString(2, data.get(1)); // CF
            ps.setString(3, data.get(2)); // Email
            ps.setString(4, data.get(3)); // UserID
            ps.setString(5, data.get(4)); // Password
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Restituisce true se l'inserimento è andato a buon fine
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Restituisce false in caso di errore
        }
    }
}