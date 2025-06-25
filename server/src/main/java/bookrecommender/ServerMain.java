package bookrecommender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class ServerMain {
    public static void main(String[] args) {
        int port = 1024;
        try (
            // Crea la connessione una sola volta
            Connection dbConnection = DriverManager.getConnection("jdbc:sqlite:bookrecommender.db");
            ServerSocket serverSocket = new ServerSocket(port)
        ) {
            System.out.println("Server in ascolto sulla porta " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Nuovo client connesso");
                // Passa la connessione al ClientHandler
                new Thread(new ClientHandler(socket, dbConnection)).start();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}