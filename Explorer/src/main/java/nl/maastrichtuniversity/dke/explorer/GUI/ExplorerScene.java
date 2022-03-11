package nl.maastrichtuniversity.dke.explorer.GUI;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import nl.maastrichtuniversity.dke.explorer.Scenario;

import javax.swing.*;
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

        expFrame = new JFrame();
        expFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Decentralize absolute pathing
        File file = new File("src/main/resources/maps/testmap.txt");
        String absPath = file.getAbsolutePath();
        //System.out.println(absPath);

        expFrame.add(new GamePanel(new Scenario(absPath)));
        expFrame.setUndecorated(true);
        expFrame.pack();
        expFrame.setLocationRelativeTo(null);
        expFrame.setVisible(true);

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
