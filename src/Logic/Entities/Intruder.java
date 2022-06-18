package Logic.Entities;

import GUI.GamePanel;
import GUI.UtilityTool;
import Logic.Area;
import Logic.Objects.Object;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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
        this.animation.getEntityImages(getPlayerImage());
        directionToTarget();
    }

    public BufferedImage[] getPlayerImage(){
        BufferedImage[] temp = new BufferedImage[10];
        //left_stand
        temp[0] = setUp("left_stand");
        //left_walk
        temp[1] = setUp("left_walk");
        //right_stand
        temp[2] = setUp("right_stand");
        //right_walk
        temp[3] = setUp("right_walk");
        //back_stand
        temp[4] = setUp("back_stand");
        //back_walk1
        temp[5] = setUp("back_walk1");
        //back_walk2
        temp[6] = setUp("back_walk2");
        //front_stand
        temp[7] = setUp("front_stand");
        //front_walk1
        temp[8] = setUp("front_walk1");
        //front_walk2
        temp[9] = setUp("front_walk2");
        return temp;
    }

    public BufferedImage setUp(String imageName){
        UtilityTool toolU = new UtilityTool();
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResource("/resources/bit16/prisoner/" + imageName +".png"));
            image = toolU.scaleImage(image, gamePanel.getTileSize()*2, gamePanel.getTileSize()*2);
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

    public void onTarget(){
        if(gamePanel.tileM.mapTile[(int) this.movement.getX()][(int) this.movement.getY()] == 4){
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

        XDirectTarget = centerTarget[0] - this.movement.getX();
        YDirectTarget = centerTarget[1] - this.movement.getY();
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
