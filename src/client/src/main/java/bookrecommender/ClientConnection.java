package bookrecommender;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientConnection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientConnection(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public boolean login(String username, String password) throws IOException {
        out.println("LOGIN;" + username + "," + password);
        String response = in.readLine();
        return response.equals("LOGIN_OK");
    }

    public List<String> getListaLibri() throws IOException {
        out.println("LISTA_LIBRI");
        String response = in.readLine();
        return Arrays.asList(response.split(","));
    }

    public void close() throws IOException {
        socket.close();
    }
}