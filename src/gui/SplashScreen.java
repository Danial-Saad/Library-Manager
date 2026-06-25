package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class SplashScreen extends JWindow {

    public SplashScreen() {
        setSize(1000, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new GridBagLayout());

        ImageIcon originalIcon = null;
        URL imgURL = getClass().getResource("/logo.jpg");

        if (imgURL != null) {
            originalIcon = new ImageIcon(imgURL);
        } else {
            File imgFile = new File("src/logo.jpg");
            if (imgFile.exists()) {
                originalIcon = new ImageIcon(imgFile.getAbsolutePath());
            } else {
                System.err.println("Error: Logo not found in Classpath or src/logo.jpg");
            }
        }

        if (originalIcon != null) {
            Image scaledImage = originalIcon.getImage().getScaledInstance(450, 450, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            add(logoLabel);
        }
    }

    public void showSplashAndExit() {
        setVisible(true);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
        }

        dispose();

        SwingUtilities.invokeLater(() -> {
            new MainDashboard().setVisible(true);
        });
    }

    public static void main(String[] args) {
        new SplashScreen().showSplashAndExit();
    }
}