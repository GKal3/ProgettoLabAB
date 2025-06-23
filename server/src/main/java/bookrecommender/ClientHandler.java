package bookrecommender;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private DBManager db;
   

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
             
            db = new DBManager();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        //Logica di connessione con il client
        try {
            String request;
            while ((request = in.readLine()) != null) {
                System.out.println("Ricevuto: " + request);

                String[] parts = request.split(";", 2);
                String command = parts[0];

                switch (command) {
                    case "LOGIN":
                        String[] cred = parts[1].split(",");
                        boolean ok = db.loginUtente(cred[0], cred[1]);
                        out.println(ok ? "LOGIN_OK" : "LOGIN_FAIL");
                        break;
                    case "LISTA_LIBRI":
                        List<String> libri = db.getLibri();
                        out.println(String.join(",", libri));
                        break;
                    default:
                        out.println("COMANDO_NON_RICONOSCIUTO");
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

}
