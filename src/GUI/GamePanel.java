package GUI;


import Logic.Scenario;
import Logic.Entities.EntityManager;
import Logic.Tiles.TileManager;




import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    int originalTileSize = 8;
    int scale = 1; //relative size of objects

    final int tileSize = originalTileSize * scale;

    final int maxScreenCol = 120;
    final int maxScreenRow = 80;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;
    public Scenario scenario;

    Thread gameThread;

    TileManager tileM;
    EntityManager entities;

    public GamePanel(Scenario scenario){
        this.scenario = scenario;
        tileM = new TileManager(this);
        entities = new EntityManager(this);

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.GRAY);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        while (gameThread != null){
            update();

            repaint();

        }

    }

    public void update(){

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        tileM.draw(g2);
        entities.draw(g2);

        g2.dispose();
    }

    public int getTileSize() {
        return tileSize;
    }
}
