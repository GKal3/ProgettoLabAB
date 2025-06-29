/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
 */
package bookrecommender;

import java.util.Locale;

/**
 * Entry point of the BookRecommender application.
 * <p>
 * This class contains the main method that launches the JavaFX application.
 * </p>
 */
public class BookRecommender {
    /**
     * Starts the JavaFX application.
     * @param args command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        MainStart.launch(MainStart.class);
   }
}