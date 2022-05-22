package Logic.Entities;

import GUI.GamePanel;

import java.awt.*;

public class EntityManager {

    Entity[] guards;
    Entity[] intruders;
    GamePanel gamePanel;
    private int id;

    public EntityManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;


        guards = new Entity[gamePanel.scenario.getNumGuards()];
        intruders = new Entity[gamePanel.scenario.getNumIntruders()];

        id = 0;
        generateGuards();
        generateIntruder();
    }

    public void generateGuards(){
        double [][] tmp = gamePanel.scenario.spawnGuards();
        for(int i = 0; i< gamePanel.scenario.getNumGuards(); i++){
            guards[i]= new DFSAgent(id, tmp[i][0], tmp[i][1], 0, 8, 180, gamePanel.scenario.getBaseSpeedGuard(), gamePanel.scenario.getBaseSpeedGuard(), gamePanel);
            id++;
        }
    }

    public void generateIntruder(){
        double [][] tmp = gamePanel.scenario.spawnIntruders();
        for(int i = 0; i< gamePanel.scenario.getNumIntruders(); i++){
            intruders[i]= new ASAgent(id, tmp[i][0], tmp[i][1], 0, 12, 180, gamePanel.scenario.getBaseSpeedIntruder(), gamePanel.scenario.getSprintSpeedIntruder(), gamePanel);
            id++;
        }
    }

    public void update(){
        for(int i = 0; i < guards.length; i++){
            guards[i].update();
        }
        for(int i = 0; i < intruders.length; i++){
            intruders[i].update();
        }
    }


    public void draw(Graphics2D g){

        //draw all guards
        for(int i = 0; i < guards.length; i++){
            guards[i].draw(g);
        }

        //draw all intruders
        for(int i = 0; i < intruders.length; i++){
            intruders[i].draw(g);
        }
    }
}
