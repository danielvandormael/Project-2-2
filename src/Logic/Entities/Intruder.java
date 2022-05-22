package Logic.Entities;

import GUI.GamePanel;
import Logic.Area;
import Logic.Objects.Object;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Intruder extends Entity{

    //vector for target area
    private double XDirectTarget;
    private double YDirectTarget;

    private int timeOnTarget;

    public Intruder(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(id, x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed,new Color(255, 255, 255, 70), gamePanel);
        getPlayerImage();
        directionToTarget();
    }

    public void getPlayerImage(){
        try{
            left_stand = ImageIO.read(getClass().getResource("/resources/bit16/prisoner/left_stand.png"));
            left_walk = ImageIO.read(getClass().getResource("/resources/bit16/prisoner/left_walk.png"));
            right_stand = ImageIO.read(getClass().getResource("/resources/bit16/prisoner/right_stand.png"));
            right_walk = ImageIO.read(getClass().getResource("/resources/bit16/prisoner/right_walk.png"));
            up_stand = ImageIO.read(getClass().getResource("/resources/bit16/prisoner/back_stand.png"));
            up_walk1 = ImageIO.read(getClass().getResource("/resources/bit16/prisoner/back_walk1.png"));
            up_walk2 = ImageIO.read(getClass().getResource("/resources/bit16/prisoner/back_walk2.png"));
            down_stand = ImageIO.read(getClass().getResource("/resources/bit16/prisoner/front_standing.png"));
            down_walk1 = ImageIO.read(getClass().getResource("/resources/bit16/prisoner/front_walk1.png"));
            down_walk2 = ImageIO.read(getClass().getResource("/resources/bit16/prisoner/front_walk2.png"));

            //senses

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onTarget(){
        if(gamePanel.tileM.mapTile[(int) getX()][(int) getY()] == 4){
            if(timeOnTarget >= 3 * gamePanel.getFPS()){
                System.out.println("Intruders Win!");
                System.exit(1);
            }else{
                timeOnTarget++;
            }
        }else{
            timeOnTarget =  0;
        }
    }

    public void directionToTarget(){
        Area target = gamePanel.scenario.getTargetArea();
        int[] centerTarget = target.getCenterAreaCoord();

        XDirectTarget = centerTarget[0] - this.getX();
        YDirectTarget = centerTarget[1] - this.getY();
    }


    public void update(){
        super.update();
        onTarget();
    }

    public double getXDirectTarget() {
        return XDirectTarget;
    }

    public double getYDirectTarget() {
        return YDirectTarget;
    }
}
