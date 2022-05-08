package nl.maastrichtuniversity.dke.explorer.GUI;

import nl.maastrichtuniversity.dke.explorer.Logic.CollisionDetection;
import nl.maastrichtuniversity.dke.explorer.Logic.Objects.ObjectManager;
import nl.maastrichtuniversity.dke.explorer.Logic.Scenario;
import nl.maastrichtuniversity.dke.explorer.Logic.Entities.EntityManager;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    int originalTileSize = 8;
    int scale = 1; //relative size of objects

    final int tileSize = originalTileSize * scale;

    final int maxScreenCol = 120;
    final int maxScreenRow = 80;
    final int expMenuHeight = 22;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = (tileSize * maxScreenRow) + expMenuHeight;
    final int FPS = 1000;
    public Scenario scenario;

    Thread gameThread;

    public TileManager tileM;
    public EntityManager entityM;
    public ObjectManager objectM;
    public CollisionDetection collisionD;


    public GamePanel(Scenario scenario){
        this.scenario = scenario;

        this.tileM = new TileManager(this);
        this.entityM = new EntityManager(this);
        this.objectM = new ObjectManager(this);
        this.collisionD = new CollisionDetection(this);

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.GRAY);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void endGameThread(){
        gameThread.interrupt();
        System.out.println("You found the target area");
    }

    @Override
    public void run() {

        double interval = 1000000000/FPS;
        double nextLoop = System.nanoTime() + interval;

        while (gameThread != null){

            update();
            leaveMarker();

            repaint();

            try{
                double remainTime = nextLoop - System.nanoTime();
                remainTime = remainTime/1000000;

                if(remainTime < 0){
                    remainTime = 0;
                }
                Thread.sleep((long) remainTime);

                nextLoop += interval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void update() { entityM.update(); }

    public void leaveMarker() { entityM.leaveMarker(); }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        tileM.draw(g2);
        entityM.draw(g2);
        objectM.draw(g2);

        g2.dispose();
    }

    public int getTileSize() {
        return tileSize;
    }

    public EntityManager getEntityManager(){ return entityM; }
}

