package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Logic.Area;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Intruder extends Entity {

    //vector for target area
    private double XDirectTarget, YDirectTarget;

    private int timeOnTarget;

    public Intruder(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(id, x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed,new Color(255, 255, 255, 70), gamePanel);
        this.animation.getEntityImages(getPlayerImage());
        directionToTarget();
    }

    public void update(boolean isGuard){
        super.update(isGuard);
        onTarget();
    }

    public BufferedImage[] getPlayerImage(){

        BufferedImage[] temp = new BufferedImage[10];

        try{
            //left_stand
            temp[0] = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/left_stand.png"));
            //left_walk
            temp[1] = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/left_walk.png"));
            //right_stand
            temp[2] = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/right_stand.png"));
            //right_walk
            temp[3] = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/right_walk.png"));
            //up_stand
            temp[4] = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/back_stand.png"));
            //up_walk1
            temp[5] = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/back_walk1.png"));
            //up_walk2
            temp[6] = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/back_walk2.png"));
            //down_stand
            temp[7] = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/front_standing.png"));
            //down_walk1
            temp[8] = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/front_walk1.png"));
            //down_walk2
            temp[9] = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/front_walk2.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public void onTarget(){
        if(gamePanel.tileM.mapTile[(int) this.movement.getX()][(int) this.movement.getY()] == 4){
            if(timeOnTarget >= 3 * gamePanel.getFPS()){
                JOptionPane.showMessageDialog(expSc.getExpFrame(), "The intruders have won!");
                System.exit(0);
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

    public void setDeadEnd(boolean dE) { super.deadEnd = dE; }
}
