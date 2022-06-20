package Logic.Entities;

import GUI.GamePanel;
import Logic.Entities.Senses.*;

import java.awt.*;


public class Entity {
    private final int id;
    private boolean eliminated;

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

    public double desiredAngle;
    public int desiredX;
    public int desiredY;

    public boolean greaterThanDesiredX;
    public boolean greaterThanDesiredY;

    public Animation animation;
    public Movement movement;
    public Vision vision;
    public Collision collision;
    public SoundSense soundSense;
    public Marker marker;

    public GamePanel gamePanel;


    public Entity(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, Color viewColor, GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.id = id;
        eliminated = false;

        animation = new Animation(this);
        movement = new Movement(x, y, baseSpeed, sprintSpeed, this);
        vision = new Vision(viewAngle, viewRange, viewAngleSize, viewColor,this);
        collision = new Collision(this);
        soundSense = new SoundSense(this);
        marker = new Marker(this);

        desiredX= (int) x;
        desiredY= (int) y;
        desiredAngle = viewAngle;

        setAction(0, 0);
    }

    public void update(boolean isGuard){
        this.marker.update(isGuard);
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

    public void eliminate(){ this.eliminated = true; }

    public boolean isEliminated() {
        return eliminated;
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

}
