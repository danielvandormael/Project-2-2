package nl.maastrichtuniversity.dke.explorer.GUI;

import javafx.scene.Scene;
import nl.maastrichtuniversity.dke.explorer.Logic.Scenario;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public class ExplorerScene extends GUIMain {

    private Scene expScene;
    private JFrame expFrame;

    public ExplorerScene() {
        // Empty.
    }

    public void setExplorerFrame() throws FileNotFoundException, URISyntaxException {

        expFrame = new JFrame("EXPLORER SIMULATOR XVIII");
        ImageIcon logo = new ImageIcon("src/main/resources/logo.png");
        expFrame.setIconImage(logo.getImage());
        expFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Decentralize absolute pathing
        File file = new File("src/main/resources/maps/testmap.txt");
        String absPath = file.getAbsolutePath();
        //System.out.println(absPath);

        GamePanel gamePanel = new GamePanel(new Scenario(absPath));
        expFrame.add(gamePanel);
        expFrame.pack();
        expFrame.setLocationRelativeTo(null);
        expFrame.setVisible(true);

        gamePanel.startGameThread();
        //expScene = new Scene(frame, screenBounds.getWidth(), screenBounds.getHeight());

    }

//    public Scene getExplorerScene() { return this.expScene; }
//
//    public JFrame getExplorerFrame() { return this.expFrame; }

//    public void renderScene(){
//        String mapD="src/main/resources/maps/testmap.txt";
//        scene = new Scenario(mapD);
//
//    }

}
