package bookrecommender;

import java.io.*;
import java.net.*;

public class SimpleClient {
    public static void main(String[] args) {
        try {
            // 1. Connessione al server (localhost:1234)
            Socket socket = new Socket("localhost", 10001);

            // 2. Ottenere stream di input/output per comunicare col server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 3. Esempio: invio e ricezione di messaggi
            out.println("Ciao, server!");
            String risposta = in.readLine();
            System.out.println("Risposta del server: " + risposta);

            // 4. Chiudere risorse
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}