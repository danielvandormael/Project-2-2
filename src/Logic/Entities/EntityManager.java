package Logic.Entities;

import GUI.GamePanel;
import Logic.Entities.Agents.ASAgent;
import Logic.Entities.Agents.DFSAgent;

import java.awt.*;

public class EntityManager {

    public Entity[] guards;
    public Entity[] intruders;
    GamePanel gamePanel;
    private int id;

    public EntityManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;


        guards = new Entity[gamePanel.scenario.getNumGuards()];
        intruders = new Entity[gamePanel.scenario.getNumIntruders()];

        id = 0;
        generateGuards();
        generateIntruder();
        //generateTest();
    }

    public void generateGuards(){
        double [][] tmp = gamePanel.scenario.spawnGuards();
        for(int i = 0; i< gamePanel.scenario.getNumGuards(); i++){
            guards[i]= new DFSAgent(id, tmp[i][0], tmp[i][1], 0, 6, 180, gamePanel.scenario.getBaseSpeedGuard(), gamePanel.scenario.getBaseSpeedGuard(), gamePanel, i%2);
            id++;
        }
    }

    public void generateIntruder(){
        double [][] tmp = gamePanel.scenario.spawnIntruders();
        for(int i = 0; i< gamePanel.scenario.getNumIntruders(); i++){
            intruders[i]= new ASAgent(id, tmp[i][0], tmp[i][1], 0, 8, 180, gamePanel.scenario.getBaseSpeedIntruder(), gamePanel.scenario.getSprintSpeedIntruder(), gamePanel);
            id++;
        }
    }

    public void generateTest(){
        intruders[0]= new Intruder(id, 3, 2, 0, 8, 180, gamePanel.scenario.getBaseSpeedIntruder(), gamePanel.scenario.getSprintSpeedIntruder(), gamePanel);
        id++;
        intruders[1]= new Intruder(id, 10, 2, 180, 8, 180, gamePanel.scenario.getBaseSpeedIntruder(), gamePanel.scenario.getSprintSpeedIntruder(), gamePanel);
        intruders[0].setAction(1, 0);
        intruders[1].setAction(1, 0);
    }

    public boolean stillAreIntruders(){
        for(int i = 0; i < intruders.length; i++){
            if( intruders[i] != null){
                return true;
            }
        }
        return false;
    }

    public void update(){
        if(stillAreIntruders() != true){
            System.out.println("Guards Win!");
            System.exit(1);
        }

        for(int i = 0; i < guards.length; i++){
            guards[i].update();
        }
        for(int i = 0; i < intruders.length; i++){
            if( intruders[i] != null){
                intruders[i].update();
            }
        }
    }


    public void draw(Graphics2D g){

        //draw all guards
        for(int i = 0; i < guards.length; i++){
            guards[i].draw(g);
        }

        //draw all intruders
        for(int i = 0; i < intruders.length; i++){
            if(intruders[i] != null){
                intruders[i].draw(g);
            }
        }
    }
}
