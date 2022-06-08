package Logic.Entities;

import GUI.GamePanel;
import Logic.Entities.Senses.*;

import java.awt.*;

public class Entity {
    private final int id;

    /*
           Action Move:
           0 - stand still
           1 - walk
           2 - run

           Action Rotate:
           0 - stand still
           1 - rotate left
           2 - rotate right
     */
    private int actionMove;
    private int actionRotate;

    public Animation animation;
    public Movement movement;
    public Vision vision;
    public SoundSense soundSense;
    public Collision collision;

    public GamePanel gamePanel;


    public Entity(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, Color viewColor, GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.id = id;

        animation = new Animation(this);
        movement = new Movement(x, y, baseSpeed, sprintSpeed, this);
        vision = new Vision(viewAngle, viewRange, viewAngleSize, viewColor,this);
        soundSense = new SoundSense(this);
        collision = new Collision(this);

        setAction(0, 0);
    }

    public void update(){
        vision.update();
        soundSense.update();
        movement.update();
        animation.update();
    }

    public void draw(Graphics2D g){
        vision.draw(g);
        animation.draw(g);
    }

    public void setAction(int actionMove, int actionRotate){
        this.actionMove = actionMove;
        this.actionRotate = actionRotate;
    }

    public int getId() {
        return id;
    }

    public int getActionMove() {
        return actionMove;
    }

    public int getActionRotate() {
        return actionRotate;
    }

    public double distanceBetween(double x2, double y2){
        return Math.sqrt(Math.pow(this.movement.getX() - x2, 2) + Math.pow(this.movement.getY() - y2, 2));
    }

    public double[] directionToCoords(double x2 , double y2){
        double[] temp = new double[2];

        temp[0] = x2 - this.movement.getX();
        temp[1] = y2 - this.movement.getY();
        return  temp;
    }
}
