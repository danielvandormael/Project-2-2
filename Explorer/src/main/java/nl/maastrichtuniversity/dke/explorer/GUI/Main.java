package nl.maastrichtuniversity.dke.explorer.GUI;

import nl.maastrichtuniversity.dke.explorer.Scenario;

import javax.swing.*;

public class Main {
    public static void main(String [] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(new GamePanel(new Scenario("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\maps\\testmap.txt")));
        frame.setUndecorated(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
