package Logic.Entities;

import GUI.GamePanel;

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
    }

    public BufferedImage[] getPlayerImage(){
        BufferedImage[] temp = new BufferedImage[10];
        try{
            //left_stand
            temp[0] = ImageIO.read(getClass().getResource("/resources/bit16/guard/left_stand.png"));
            //left_walk
            temp[1] = ImageIO.read(getClass().getResource("/resources/bit16/guard/left_walk.png"));
            //right_stand
            temp[2] = ImageIO.read(getClass().getResource("/resources/bit16/guard/right_stand.png"));
            //right_walk
            temp[3] = ImageIO.read(getClass().getResource("/resources/bit16/guard/right_walk.png"));
            //up_stand
            temp[4] = ImageIO.read(getClass().getResource("/resources/bit16/guard/back_stand.png"));
            //up_walk1
            temp[5] = ImageIO.read(getClass().getResource("/resources/bit16/guard/back_walk1.png"));
            //up_walk2
            temp[6] = ImageIO.read(getClass().getResource("/resources/bit16/guard/back_walk2.png"));
            //down_stand
            temp[7] = ImageIO.read(getClass().getResource("/resources/bit16/guard/front_standing.png"));
            //down_walk1
            temp[8] = ImageIO.read(getClass().getResource("/resources/bit16/guard/front_walk1.png"));
            //down_walk2
            temp[9] = ImageIO.read(getClass().getResource("/resources/bit16/guard/front_walk2.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public void setShout(boolean choice){
        this.shoutSound = choice;
    }

    public void eliminateIntruder(){
        for(int i = 0; i< gamePanel.entityM.intruders.length; i++){
            if(gamePanel.entityM.intruders[i] != null){
                if(distanceBetween(gamePanel.entityM.intruders[i].movement.getX(), gamePanel.entityM.intruders[i].movement.getY()) <= 0.5){
                    gamePanel.entityM.intruders[i] = null;
                }
            }
        }
    }

    public void update(){
        super.update();
        eliminateIntruder();
    }
}
