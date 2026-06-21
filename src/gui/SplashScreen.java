package gui;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {

    public SplashScreen() {
        setSize(1000, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new GridBagLayout());

        ImageIcon originalIcon = new ImageIcon("logo.jpg");
        Image scaledImage = originalIcon.getImage().getScaledInstance(450, 450, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));

        add(logoLabel);
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