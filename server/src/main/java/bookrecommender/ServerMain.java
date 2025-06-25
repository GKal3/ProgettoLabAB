package bookrecommender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class ServerMain extends Thread {
    public static final int port = 10001;
    public static void main(String[] args) {
        try (
            // Crea la connessione una sola volta
            Connection dbConnection = DriverManager.getConnection("jdbc:sqlite:bookrecommender.db");
            ServerSocket serverSocket = new ServerSocket(port)
        ) {
            System.out.println("Server in ascolto sulla porta " + port);
            while (true) {
                System.out.println("In attesa di connessione...");
                Socket socket = serverSocket.accept();
                System.out.println("Nuovo client connesso");
                // Passa la connessione al ClientHandler
                //new Thread(new ClientHandler(socket, dbConnection)).start();
                ClientHandler handler = new ClientHandler(socket, dbConnection);
                Thread t = new Thread(handler);
                t.start();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}