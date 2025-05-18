package bookrecommender;

//import prog.io.*;
//import java.io.*;

public class RegUtente extends Ricerca {
    //registrazione
    /*
    public static void registrazione() {
        ConsoleInputManager in = new ConsoleInputManager();
        ConsoleOutputManager out= new ConsoleOutputManager();

        // Raccolta dei dati dell'utente
        out.println("Inserisci il tuo Nome: ");
        String nome = in.readLine("");
        
        out.println("Inserisci il tuo Cognome: ");
        String cognome = in.readLine("");

        out.println("Inserisci il tuo Codice Fiscale : ");
        String cf = in.readLine("");
        
        out.println("Inserisci la tua Email: ");
        String email = in.readLine("");
        
        out.println("Inserisci il tuo UserID : ");
        String userid = in.readLine("");
        
        out.println("Inserisci la tua password: ");
        String password = in.readLine("");
        
        //dati utente in unica stringa
        String utente =   nome + " " + cognome  + ", " + cf + ", " +  email + ", " + userid + ", "  + password;
       
        //Salvare i dati all'interno del file UtentiRegistrati.dati
        try (BufferedWriter wr = new BufferedWriter (new FileWriter("C:\\Users\\giuli\\Desktop\\progetto\\UtentiRegistrati.dati.csv", true))) {
            //"true" per non sovrascrivere il contenuto all'interno del file
            wr.write(utente);
            wr.newLine();
            out.println("Registrazione completata con successo!");

        } catch (IOException e) {
            System.out.println(e.toString());
            
        }
        
    }
    
    //login
    public static boolean login (String userid, String password) {
        boolean risultato = false;
        try (BufferedReader read = new BufferedReader(new FileReader("C:\\Users\\giuli\\Desktop\\progetto\\UtentiRegistrati.dati.csv"))) {
            String riga;
            // Leggere il file CSV riga per riga
            while ((riga = read.readLine()) != null) {
                
                String [] tipo = riga.split(","); //divide la stringa in sottostringhe ogni volta che c'è una virgola
                
                if (tipo.length > 0) {
                //verifica se l'usereid e la password forniti dall'utente corridpondono a quelli memorizzati nel file
                    if (tipo[3].trim().equals(userid) && tipo[4].trim().equals(password)) { //trim toglie gli spazi
                        //Login riuscito
                        risultato = true;
                        break;  //interrompe la lettura quando trova un match
                    }
                }
            }
        } catch (IOException e) {
            System.out.println (e.toString()); 
        }
       return risultato;
    }

    public static void main(String[] args) {
        /*ConsoleInputManager in = new ConsoleInputManager();
        ConsoleOutputManager out = new ConsoleOutputManager();
        out.println("Inserisci il tuo UserID: ");
        String userid = in.readLine("");
        
        out.println("Inserisci la tua password: ");
        String password = in.readLine("");
        
        if (login(userid, password)) {
            out.println("Login riuscito!");
        } else {
            out.println("Login fallito!");
        }
        registrazione();
    }
    */
}
