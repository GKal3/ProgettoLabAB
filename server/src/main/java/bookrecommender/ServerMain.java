package bookrecommender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Map;

public class ServerMain extends Thread {
    public static final int port = 10001;
    public static void main(String[] args) {
        
        Map<String, String> credentials = DBLoginWindow.showLoginDialog();
        if (credentials == null) {
            System.out.println("Login annullato. Uscita...");
            return;
        }

        try (
           
            Connection dbConnection = DBManager.connect(
                credentials.get("user"),
                credentials.get("password")
            );
            ServerSocket serverSocket = new ServerSocket(port)
        ) {
            System.out.println("Server in ascolto sulla porta " + port);
            while (true) {
                System.out.println("In attesa di connessione...");
                Socket socket = serverSocket.accept();
                System.out.println("Nuovo client connesso");
                ClientHandler handler = new ClientHandler(socket, dbConnection);
                Thread t = new Thread(handler);
                t.start();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}