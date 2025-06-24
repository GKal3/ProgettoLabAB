package bookrecommender.dao;
import java.util.*;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ricerca {
    private Connection conn;
    
    public Ricerca(Connection conn) {
        this.conn = conn;
    }

    public List<String> cercaTitolo (String search) { 
        List<String> result = new ArrayList<>();
        //LOWER(Title) è una funzione SQL che trasforma una stringa in minuscolo, per fare ricerche senza distinguere tra maiuscole e minuscole.
        String query= "SELECT Title FROM Libri WHERE LOWER(Title) LIKE ?";

        try (PreparedStatement stmt=conn.prepareStatement(query)) {
            stmt.setString(1, "%" + search.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("Title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    
    public List<String> cercaAutore (String search) {
        List<String> result = new ArrayList<>(); 
        String query = "SELECT Title FROM Libri WHERE LOWER(Authors) LIKE ?";
        
        try(PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + search.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("Title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> cercaAutoreAnno (String search, int year) {
        List<String> result= new ArrayList<>();
        String query = "SELECT Title FROM Libri WHERE LOWER(Authors) LIKE ? AND Year = ?";
    
    try(PreparedStatement  stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + search.toLowerCase() + "%");
            stmt.setInt(2, year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("Title")); 
                }
        } catch (SQLException e) {
            e.printStackTrace();
    }  
    return result;
}
}