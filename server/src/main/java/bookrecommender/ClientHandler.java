package bookrecommender;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.util.*;
import bookrecommender.dao.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private DBManager db;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    
    private Ricerca ricerca;
    private Valutazione valutazione;
    private Visualizza visualizza;
    private Librerie librerie;
    private Registra registra;

    public ClientHandler (Socket socket, Connection dbConnection) {
        this.socket = socket;
        try {
            db = new DBManager(dbConnection);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush(); 
            in = new ObjectInputStream(socket.getInputStream());

            ricerca = new Ricerca(dbConnection);
            valutazione = new Valutazione(dbConnection);
            visualizza = new Visualizza(dbConnection);
            librerie = new Librerie(dbConnection);
            registra = new Registra(dbConnection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
    
            while (true) {
                if (socket.isClosed()) break;
                Object reqObj;
                try {
                    reqObj = in.readObject();
                } catch (EOFException | SocketException e) {
                    System.out.println("Client disconnesso.");
                    break; 
                }
                if (reqObj == null) break;

                if (reqObj instanceof String request) {

                    String[] parts = request.split(";", 2);
                    String command = parts[0];

                    switch (command) {
                        case "CERCA_TITOLO":
                            
                            if (parts.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            List<String> titoli = ricerca.cercaTitolo(parts[1]);
                            out.writeObject(titoli);
                            out.writeObject("FINE");
                            break;

                        case "CERCA_AUTORE":
            
                            if (parts.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            List<String> titoliAutore = ricerca.cercaAutore(parts[1]);
                            out.writeObject(titoliAutore);
                            out.writeObject("FINE");
                            break;

                        case "CERCA_AUTORE_ANNO":
        
                            if (parts.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI1");
                                out.writeObject("FINE");
                                break;
                            }
                            String[] paramAA = parts[1].split(";", 2);
                            if (paramAA.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI1");
                                out.writeObject("FINE");
                                break;
                            }
                            List<String> titoliAutoreAnno = ricerca.cercaAutoreAnno(paramAA[1], Integer.parseInt(paramAA[0]));
                            out.writeObject(titoliAutoreAnno);
                            out.writeObject("FINE");
                            break;

                        case "CERCA_LIB":
        
                            if (parts.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            String[] paramLibS = parts[1].split(",");
                            if (paramLibS.length < 3) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            List<String> libri = ricerca.searchLib(paramLibS[0], paramLibS[1], paramLibS[2]);
                            out.writeObject(libri);
                            out.writeObject("FINE");
                            break;

                        case "VISUALIZZA_INFO":
                            if (parts.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            String[] info = visualizza.infoLibro(parts[1]);
                            out.writeObject(info);
                            out.writeObject("FINE");
                            break;

                        case "VISUALIZZA_NOTE":
                            if (parts.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            String[] noteParams = parts[1].split(",");
                            if (noteParams.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            List<String> note = visualizza.note(noteParams[0], noteParams[1]);
                            out.writeObject(note);
                            out.writeObject("FINE");
                            break;

                        case "VISUALIZZA_VALUTAZIONI":
                            if (parts.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            int[] val = visualizza.recapVal(parts[1]);
                            out.writeObject(val);
                            out.writeObject("FINE");
                            break;

                        case "VISUALIZZA_SUGGERIMENTI":
                            if (parts.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            List<String> suggerimenti = visualizza.recapSugg(parts[1]);
                            out.writeObject(suggerimenti);
                            out.writeObject("FINE");
                            break;

                        case "REGISTRA_LIBRERIA":
                            if (parts.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            String[] paramLib = parts[1].split(",");
                            if (paramLib.length < 3) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            boolean esitoLib = librerie.registraLibreria(paramLib[0], paramLib[1], paramLib[2]);
                            out.writeObject(esitoLib ? "LIBRERIA_REGISTRATA" : "LIBRERIA_AGGIORNATA");
                            out.writeObject("FINE");
                            break;

                        case "VISUALIZZA_LIBRERIA":
                            if (parts.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            String[] paramVisLib = parts[1].split(",");
                            if (paramVisLib.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            List<String> libriLibreria = librerie.visLib(paramVisLib[0], paramVisLib[1]);
                            out.writeObject(libriLibreria);
                            out.writeObject("FINE");
                            break;

                        case "DEL_LIB":
                            if (parts.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            String[] paramDelLib = parts[1].split(",");
                            if (paramDelLib.length < 2) { 
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            boolean esitoDel = librerie.deleteLib(paramDelLib[0], paramDelLib[1]);
                            out.writeObject(esitoDel ? "DELETED" : "NOT_DELETED");
                            out.writeObject("FINE");
                            break;

                        case "LOGIN":
                            if (parts.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            String[] paramLogin = parts[1].split(",");
                            if (paramLogin.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            String[] data = registra.login(paramLogin[0], paramLogin[1]);
                            out.writeObject(data);
                            out.writeObject("FINE");
                            break;

                        case "REG":
                            if (parts.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            String[] paramReg = parts[1].split(",");
                            if (paramReg.length < 5) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            registra.registrazione(paramReg);
                            out.writeObject("FINE");
                            break;

                        case "CHECK_REG":
                            if (parts.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            String[] paramCheck = parts[1].split(",");
                            if (paramCheck.length < 3) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            String response = registra.checkReg(paramCheck[0], paramCheck[1], paramCheck[2]);
                            if ("USER_EXISTS".equalsIgnoreCase(response)) {
                                out.writeObject("USER_EXISTS");
                            } else if ("EMAIL_EXISTS".equalsIgnoreCase(response)) {
                                out.writeObject("EMAIL_EXISTS");
                            } else if ("CF_EXISTS".equalsIgnoreCase(response)) {
                                out.writeObject("CF_EXISTS");
                            } else {
                                out.writeObject("OK");
                            }
                            out.writeObject("FINE");
                            break;
                        case "VIS_LIB_LIST":
                            if (parts.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            String id = parts[1];
                            List<String> libList = visualizza.libList(id);
                            out.writeObject(libList);
                            out.writeObject("FINE");
                            break;
                            
                        case "INS_SUGG":
                            if (parts.length < 2) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            String[] paramSugg = parts[1].split(",");
                            if (paramSugg.length < 3) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                                break;
                            }
                            boolean esitoSugg = valutazione.inserisciSuggerimentoLibri(paramSugg[0], paramSugg[1], paramSugg[2]);
                            out.writeObject(esitoSugg ? "SUGG_INS" : "ERROR_INS");
                            out.writeObject("FINE");
                            break;

                        case "INS_VAL":
                            try {
                                String [] idAndTit = (String []) in.readObject();
                                int[] val1 = (int[]) in.readObject();
                                @SuppressWarnings("unchecked")
                                List<String> note1 = (List<String>) in.readObject();

                                boolean esito = valutazione.inserisciValutazioneLibro(idAndTit[0], idAndTit[1], val1, note1);
                                out.writeObject(esito ? "VAL_INS" : "ERROR_INS");
                                out.writeObject("FINE");
                            } catch (Exception ex) {
                                out.writeObject("ERRORE_PARAMETRI");
                                out.writeObject("FINE");
                            }
                            break;
                        
                        case "QUIT":
                            out.writeObject("FINE");
                            return; 

                        default:
                        out.writeObject("COMANDO_NON_RICONOSCIUTO");
                
                    }
                }   
            }
        } catch (EOFException e) {
            System.out.println("Client disconnesso normalmente.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}