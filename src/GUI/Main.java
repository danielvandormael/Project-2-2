package GUI;

import Logic.Scenario;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        File file = new File("src/resources/maps/demomap.txt");
        String absPath = file.getAbsolutePath();
        GamePanel gamePanel= new GamePanel(new Scenario(absPath));

        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        BufferedImage icon = ImageIO.read(Main.class.getResource("/resources/bit16/guard/front_stand.png"));
        frame.setIconImage(icon);
        frame.setTitle("Explorer Simulator 18");
        gamePanel.startGameThread();
    }
}
