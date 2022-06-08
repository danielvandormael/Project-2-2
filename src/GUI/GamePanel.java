package GUI;


import Logic.Objects.ObjectManager;
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
    final int FPS = 20;
    public Scenario scenario;

    Thread gameThread;

    public TileManager tileM;
    public EntityManager entityM;
    ObjectManager objectM;

    public GamePanel(Scenario scenario){
        this.scenario = scenario;

        this.tileM = new TileManager(this);
        this.entityM = new EntityManager(this);
        this.objectM = new ObjectManager(this);

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
    }

    @Override
    public void run() {

        double interval = 1000000000/FPS;
        double nextLoop = System.nanoTime() + interval;

        while (gameThread != null){

            update();

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

    public void update(){

        entityM.update();

    }

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
    public int getFPS() {
        return FPS;
    }
}
