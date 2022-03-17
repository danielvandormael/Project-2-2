package GUI;

import Logic.Scenario;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        GamePanel gamePanel= new GamePanel(new Scenario("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\maps\\testmap.txt"));
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        gamePanel.startGameThread();
    }
}
