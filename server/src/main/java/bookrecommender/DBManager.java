package bookrecommender;

import java.sql.*;

public class DBManager {

  private static final String DB_URL = "jdbc:postgresql://localhost:5432/dbBR";
  private static final String USER = "postgres";
  private static final String PASSWORD = "131103Giuk";
  
  private Connection connection;

    // Costruttore che accetta una Connection
    public DBManager(Connection connection) {
        this.connection = connection;
    }

    // Getter per la Connection, se serve
    public Connection getConnection() {
        return connection;
    }

  public static Connection connect() throws SQLException{
    return DriverManager.getConnection(DB_URL, USER, PASSWORD);
  }
}