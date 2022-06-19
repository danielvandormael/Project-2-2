package Logic.Entities;

import GUI.GamePanel;
import GUI.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Guard extends Entity{

    private boolean shoutSound;

    public Guard(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(id, x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, new Color(255, 250, 134, 70), gamePanel);
        this.animation.getEntityImages(getPlayerImage());
        isGuard = true;
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
            image = ImageIO.read(getClass().getResource("/resources/bit16/guard/" + imageName +".png"));
            image = toolU.scaleImage(image, gamePanel.getTileSize()*2, gamePanel.getTileSize()*2);
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

    public void eliminateIntruder(){
        for(int i = 0; i< gamePanel.entityM.intruders.length; i++){
            if(!gamePanel.entityM.intruders[i].isEliminated()){
                if(movement.distanceBetween(gamePanel.entityM.intruders[i].movement.getX(), gamePanel.entityM.intruders[i].movement.getY()) <= 1.5){
                    gamePanel.entityM.intruders[i].eliminate();
                }
            }
        }
    }

    public void update(){
        super.update();
        eliminateIntruder();
    }
}
