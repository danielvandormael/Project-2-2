package nl.maastrichtuniversity.dke.explorer.GUI;

import javafx.scene.Scene;
import nl.maastrichtuniversity.dke.explorer.Logic.Scenario;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public class ExplorerScene extends GUIMain {

    public Scene expScene;
    public JFrame expFrame;
    private JMenuBar expMenuBar;
    private JMenu keyMenu;
    // Respectively: 0 - wall, 1 - noShadeBlock, 2 - shadeBlock, 3 - target, 4 - teleport, 5 - guard, 6 - intruder
    private JMenuItem[] keyItems;

    public ExplorerScene() {
        // Empty.
    }

    public void setExplorerFrame() throws FileNotFoundException, URISyntaxException {

        expFrame = new JFrame("EXPLORER SIMULATOR XVIII");
        ImageIcon logo = new ImageIcon("src/main/resources/logo.png");
        expFrame.setIconImage(logo.getImage());
        //expFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        expFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        expFrame.setResizable(false);
        //expFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Decentralize absolute pathing
        File file = new File("src/main/resources/maps/demomap.txt");
        String absPath = file.getAbsolutePath();
        //System.out.println(absPath);

        GamePanel gamePanel = new GamePanel(new Scenario(absPath));
        expFrame.add(gamePanel);

        expFrame.pack();
        expFrame.setLocationRelativeTo(null);
        expFrame.setVisible(true);

        setExplorerKey();

        gamePanel.startGameThread();
        //expScene = new Scene(frame, screenBounds.getWidth(), screenBounds.getHeight());

    }

    @SuppressWarnings("unchecked")
    public void setExplorerKey() {

        expMenuBar = new JMenuBar();
        keyMenu = new JMenu();
        keyItems = new JMenuItem[13];
        for(int i = 0; i < keyItems.length; i++) {
            keyItems[i] = new JMenuItem();
        }

        keyMenu.setText("Exploration Key");

        //menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        keyItems[0].setIcon(new ImageIcon("src/main/resources/bit8/tiles/wall.png"));
        keyItems[0].setText("Wall");

        keyItems[1].setIcon(new ImageIcon("src/main/resources/bit8/tiles/floor.png"));
        keyItems[1].setText("Unshaded Floor");

        keyItems[2].setIcon(new ImageIcon("src/main/resources/bit8/tiles/shaded.png"));
        keyItems[2].setText("Shaded Floor");

        keyItems[3].setIcon(new ImageIcon("src/main/resources/bit8/tiles/target.png"));
        keyItems[3].setText("Target");

        keyItems[4].setIcon(new ImageIcon("src/main/resources/bit8/tiles/teleport.png"));
        keyItems[4].setText("Teleport");

        keyItems[5].setIcon(new ImageIcon("src/main/resources/bit16/guard/front_standing.png"));
        keyItems[5].setText("Guard");

        keyItems[6].setIcon(new ImageIcon("src/main/resources/bit16/prisoner/front_standing.png"));
        keyItems[6].setText("Intruder");

        keyItems[7].setIcon(new ImageIcon("src/main/resources/bit8/objects/marker1.png"));
        keyItems[7].setText("Time Pheromone");

        keyItems[8].setIcon(new ImageIcon("src/main/resources/bit8/objects/marker2.png"));
        keyItems[8].setText("Dead End Marker");

        keyItems[9].setIcon(new ImageIcon("src/main/resources/bit8/objects/marker3.png"));
        keyItems[9].setText("Warning Marker");

        keyItems[10].setIcon(new ImageIcon("src/main/resources/bit8/sense/arrow.png"));
        keyItems[10].setText("Arrow");

        keyItems[11].setIcon(new ImageIcon("src/main/resources/bit8/sense/hear.png"));
        keyItems[11].setText("Hear Notice");

        keyItems[12].setIcon(new ImageIcon("src/main/resources/bit8/sense/shout.png"));
        keyItems[12].setText("Shout Icon");

        for(int i = 0; i < keyItems.length; i++) {
            keyMenu.add(keyItems[i]);
        }

        expMenuBar.add(keyMenu);
        expFrame.setJMenuBar(expMenuBar);

        GroupLayout expLayout = new GroupLayout(expFrame.getContentPane());
        expFrame.getContentPane().setLayout(expLayout);
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
