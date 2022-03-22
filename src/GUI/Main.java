package GUI;

import Logic.Scenario;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        GamePanel gamePanel= new GamePanel(new Scenario("C:\\Users\\jiska\\OneDrive\\Documents\\Maastricht University\\Project 2-2 v1\\src\\resources\\maps\\demomap.txt"));
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        gamePanel.startGameThread();
    }
}
