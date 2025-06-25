package bookrecommender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class ClientTester {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 10001);
            System.out.println("✅ Client connesso al server!");
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
