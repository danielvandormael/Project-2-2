package GUI;

import Logic.Scenario;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        GamePanel gamePanel= new GamePanel(new Scenario("C:\\Users\\danie\\Projects\\Project2_2\\src\\resources\\maps\\demomap.txt"));
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        gamePanel.startGameThread();
    }
}
