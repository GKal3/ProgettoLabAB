/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender;

import java.io.*;
import java.net.URL;
import java.util.*;
/**
 * Classe che fornisce metodi per la ricerca di libri basata su titolo, autore e anno.
 */
public class Ricerca {
    /**
     * URL del file CSV contenente i dati dei libri, recuperato tramite il class loader.
     */
    private final URL link = getClass().getResource("/csv/Libri.dati.csv");
    /**
     * Cerca i libri sulla base del titolo.
     * @param ricerca la stringa da cercare nei titoli dei libri.
     * @return una lista contenente i titoli dei libri che contengono la stringa di ricerca.
     */  
    public List<String> cercaTitolo (String ricerca) { 
        List<String> result = new ArrayList<>();
        try (BufferedReader read = new BufferedReader(new InputStreamReader(link.openStream()))) {
            String riga = read.readLine();
    
            while ((riga = read.readLine()) != null) {
                String [] tipo = riga.split(","); 

                if (tipo.length > 0) { 
                    String titolo = tipo[0]; 

                    if (titolo.toLowerCase().contains(ricerca.toLowerCase())) {
                        result.add(titolo);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * Cerca i libri sulla base dell'autore.
     * @param ricerca il nome dell'autore da cercare.
     * @return una lista contenente i titoli dei libri associati all'autore cercato.
     */
    public List<String> cercaAutore (String ricerca) {
        List<String> result = new ArrayList<>();
        try(BufferedReader read = new BufferedReader(new InputStreamReader(link.openStream()))) {
            String riga = read.readLine();

            while ((riga = read.readLine()) != null) {
                String [] tipo = riga.split(",");

                if(tipo.length > 0) {
                    String titolo = tipo[0];
                    String autore = tipo[1].replace("\"", "").substring(2);

                    if(autore.toLowerCase().contains(ricerca.toLowerCase())) {
                        result.add(titolo);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * Cerca i libri in base a un autore specifico e a un anno di pubblicazione.
     * @param ricerca il nome dell'autore da cercare.
     * @param year l'anno di pubblicazione dei libri da cercare.
     * @return una lista contenente i titoli dei libri associati all'autore e all'anno specificati.
     */
    public List<String> cercaAutoreAnno (String ricerca, String year) {
        List<String> result= new ArrayList<>();
        
        try(BufferedReader read = new BufferedReader(new InputStreamReader(link.openStream()))) {
       
            String riga = read.readLine();

            while ((riga = read.readLine()) != null ) {
                String [] tipo = riga.split(",");
                
                if(tipo.length > 0) {
                    String titolo = tipo[0];
                    String autore = tipo[1].replace("\"", "").substring(2);
                    String anno = tipo[4].replace("\"", "");

                    if(autore.toLowerCase().contains(ricerca.toLowerCase()) && anno.equals(year)) {
                        result.add(titolo);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }  
}