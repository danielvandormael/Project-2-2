package GUI;

import Logic.Scenario;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GamePanel gamePanel= new GamePanel(new Scenario("C:\\Users\\danie\\Projects\\Project2_2\\src\\resources\\maps\\demomap.txt"));
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        BufferedImage icon = ImageIO.read(Main.class.getResource("/resources/bit16/guard/front_stand.png"));
        frame.setIconImage(icon);
        frame.setTitle("Multi Agent");
        gamePanel.startGameThread();
    }
}
