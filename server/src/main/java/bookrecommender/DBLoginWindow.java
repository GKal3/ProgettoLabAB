/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
 */
package bookrecommender;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible for displaying a login dialog to the user.
 * It prompts the user to enter their username and password for database access.
 */
public class DBLoginWindow {

    /**
     * Displays a login dialog to the user.
     * The dialog contains fields for username and password.
     * If the user enters valid credentials, they are returned as a map.
     * If the user cancels the dialog, null is returned.
     * @return a map containing the username and password, or null if cancelled
     */
    public static Map<String, String> showLoginDialog() {
        JTextField userField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Username:"));
        panel.add(userField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        while (true) {
            int result = JOptionPane.showConfirmDialog(
            null, panel, "Enter your credentials for dbBR",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
            String username = userField.getText();
            String password = new String(passwordField.getPassword());

           
            boolean valid = !username.isEmpty() && !password.isEmpty();

            if (valid) {
                Map<String, String> credentials = new HashMap<>();
                credentials.put("user", username);
                credentials.put("password", password);
                return credentials;
            } else {
                JOptionPane.showMessageDialog(
                null,
                "Invalid credentials. Try Again.",
                "Error",
                JOptionPane.ERROR_MESSAGE
                );
            }
            } else {
            return null;
            }
        }
    }
}