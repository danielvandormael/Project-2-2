package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Intruder extends Entity {

    private int timeOnTarget;

    public Intruder(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(id, x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed,new Color(255, 255, 255, 70), gamePanel);
        getPlayerImage();
    }

    public void update(boolean isGuard){
        super.update(isGuard);
        onTarget();
    }

    public void getPlayerImage(){
        try{
            left_stand = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/left_stand.png"));
            left_walk = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/left_walk.png"));
            right_stand = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/right_stand.png"));
            right_walk = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/right_walk.png"));
            up_stand = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/back_stand.png"));
            up_walk1 = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/back_walk1.png"));
            up_walk2 = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/back_walk2.png"));
            down_stand = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/front_standing.png"));
            down_walk1 = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/front_walk1.png"));
            down_walk2 = ImageIO.read(Intruder.class.getResourceAsStream("/bit16/prisoner/front_walk2.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onTarget(){
        if(gamePanel.tileM.mapTile[(int) getX()][(int) getY()] == 4){
            if(timeOnTarget >= 3 * gamePanel.getFPS()){
                JOptionPane.showMessageDialog(explorerScene.getExpFrame(), "The intruders have won!");
                System.exit(0);
            }else{
                timeOnTarget++;
            }
        }else{
            timeOnTarget =  0;
        }
    }

    public void setDeadEnd(boolean dE) { super.deadEnd = dE; }
}
