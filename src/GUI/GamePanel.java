package GUI;


import Logic.CollisionDetection;
import Logic.Objects.ObjectManager;
import Logic.Scenario;
import Logic.Entities.EntityManager;
import Logic.Tiles.TileManager;




import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    public final int tileSize = 8;

    final int maxScreenCol = 120;
    final int maxScreenRow = 80;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;
    public final int FPS = 1000;
    public Scenario scenario;

    Thread gameThread;

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int guardsWinState = 2;
    public final int intrudersWinState = 3;

    public TileManager tileM;
    public EntityManager entityM;
    public ObjectManager objectM;
    public UI ui;
    public CollisionDetection collisionDetection;
    public KeyHandler keyHandler;

    public int intruderWins;
    public int guardWins;
    public final int sampleSize = 1;
    public double currentTime;

    public GamePanel(Scenario scenario){
        this.scenario = scenario;

        this.tileM = new TileManager(this);
        this.entityM = new EntityManager(this);
        this.objectM = new ObjectManager(this);
        this.ui = new UI(this);
        this.collisionDetection = new CollisionDetection(this);
        this.keyHandler = new KeyHandler(this);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        guardWins = 0;
        intruderWins = 0;

        gameState = titleState;
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        currentTime = System.currentTimeMillis();
        gameThread.start();
    }

    public void endGameThread(){
        gameThread.interrupt();
    }

    public void stopGameThread(){
        System.out.println(System.currentTimeMillis() - currentTime);

        gameThread.stop();
    }

    public void resetGamePanel(){
        this.entityM = new EntityManager(this);
        this.objectM = new ObjectManager(this);
        this.ui = new UI(this);
        this.keyHandler = new KeyHandler(this);
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
        if(gameState == playState){
            entityM.update();
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;


        if(gameState == playState){
            tileM.draw(g2);
            entityM.draw(g2);
            objectM.draw(g2);
        }

        ui.draw(g2);

        g2.dispose();
    }

    public int getScreenWidth() {
        return screenWidth;
    }
    public int getScreenHeight() {
        return screenHeight;
    }
    public int getTileSize() {
        return tileSize;
    }
    public int getFPS() {
        return FPS;
    }
}
