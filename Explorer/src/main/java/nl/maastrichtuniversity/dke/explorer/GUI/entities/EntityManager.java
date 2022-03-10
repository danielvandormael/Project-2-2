package nl.maastrichtuniversity.dke.explorer.GUI.entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Scenario;

import java.awt.*;

public class EntityManager {

    Entity[] guards;
    Entity[] intruders;
    GamePanel gamePanel;

    public EntityManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;


        guards = new Entity[gamePanel.scenario.getNumGuards()];
        intruders = new Entity[gamePanel.scenario.getNumIntruders()];
        generateGuards();
        generateIntruder();
    }

    public void generateGuards(){
        double [][] tmp = gamePanel.scenario.spawnGuards();
        for(int i = 0; i< gamePanel.scenario.getNumGuards(); i++){
            guards[i]= new Guard(tmp[i][0], tmp[i][1], tmp[i][2], 5, 70, gamePanel.scenario.getBaseSpeedGuard(), gamePanel.scenario.getBaseSpeedGuard());
        }
    }

    public void generateIntruder(){
        double [][] tmp = gamePanel.scenario.spawnIntruders();
        for(int i = 0; i< gamePanel.scenario.getNumIntruders(); i++){
            intruders[i]= new Prisoner(tmp[i][0], tmp[i][1], tmp[i][2], 8, 70, gamePanel.scenario.getBaseSpeedIntruder(), gamePanel.scenario.getSprintSpeedIntruder());
        }
    }

    public void draw(Graphics2D g){

        //draw all guards
        for(int i = 0; i < guards.length; i++){
            g.setColor(new Color(255, 250, 134, 70));
            g.fillArc(((int) guards[i].x*gamePanel.getTileSize()-(( (int) guards[i].viewRange*gamePanel.getTileSize())/2)+(gamePanel.getTileSize()/2)), ((int) guards[i].y*gamePanel.getTileSize()-(( (int) guards[i].viewRange*gamePanel.getTileSize())/2)+(gamePanel.getTileSize()/2)), (int) guards[i].viewRange*gamePanel.getTileSize(),(int) guards[i].viewRange*gamePanel.getTileSize(), (int)guards[i].viewAngle - 30, (int) guards[i].viewAngle +30);
            g.drawImage(guards[i].getImage(), (int) guards[i].x*gamePanel.getTileSize(), (int) guards[i].y*gamePanel.getTileSize(), gamePanel.getTileSize(),  gamePanel.getTileSize(), null);
        }

        //draw all intruders
        for(int i = 0; i < intruders.length; i++){
            g.setColor(new Color(255, 255, 255, 70));
            g.fillArc(((int) intruders[i].x*gamePanel.getTileSize()-(( (int) intruders[i].viewRange*gamePanel.getTileSize())/2)+(gamePanel.getTileSize()/2)), ((int) intruders[i].y*gamePanel.getTileSize()-(( (int) intruders[i].viewRange*gamePanel.getTileSize())/2)+(gamePanel.getTileSize()/2)), (int) intruders[i].viewRange*gamePanel.getTileSize(),(int) intruders[i].viewRange*gamePanel.getTileSize(), (int)intruders[i].viewAngle - 30, (int) intruders[i].viewAngle +30);
            g.drawImage(intruders[i].getImage(), (int) intruders[i].x*gamePanel.getTileSize(), (int) intruders[i].y*gamePanel.getTileSize(), gamePanel.getTileSize(),  gamePanel.getTileSize(), null);
        }
    }
}
