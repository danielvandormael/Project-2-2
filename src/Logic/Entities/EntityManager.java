package Logic.Entities;

import GUI.GamePanel;
import Logic.Entities.Agents.*;

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
            //guards[i]= new BrickAndMortar(id, tmp[i][0], tmp[i][1], 0, 6, 180, gamePanel.scenario.getBaseSpeedGuard(), gamePanel.scenario.getBaseSpeedGuard(), gamePanel, i%2);
            id++;
        }
    }

    public void generateIntruder(){
        double [][] tmp = gamePanel.scenario.spawnIntruders();
        for(int i = 0; i< gamePanel.scenario.getNumIntruders(); i++){
            //intruders[i]= new ASAgent(id, tmp[i][0], tmp[i][1], 0, 8, 180, gamePanel.scenario.getBaseSpeedIntruder(), gamePanel.scenario.getSprintSpeedIntruder(), gamePanel);
            intruders[i]= new RandomIntruder(id, tmp[i][0], tmp[i][1], 0, 8, 180, gamePanel.scenario.getBaseSpeedIntruder(), gamePanel.scenario.getSprintSpeedIntruder(), gamePanel);
            //intruders[i]= new BFSIntruder(id, tmp[i][0], tmp[i][1], 0, 8, 180, gamePanel.scenario.getBaseSpeedIntruder(), gamePanel.scenario.getSprintSpeedIntruder(), gamePanel);
            id++;
        }
    }

    public boolean stillAreIntruders(){
        for(int i = 0; i < intruders.length; i++){
            if( !intruders[i].isEliminated()){
                return true;
            }
        }
        return false;
    }

    public void update(){
        if(stillAreIntruders() != true){
            gamePanel.guardWins++;
            if(gamePanel.intruderWins + gamePanel.guardWins == gamePanel.sampleSize) {
                System.out.println("Guards won: "+ gamePanel.guardWins);
                System.out.println("Intruders won: "+ gamePanel.intruderWins);
                System.exit(0);
            }
            //gamePanel.gameState = gamePanel.guardsWinState;
            gamePanel.resetGamePanel();
        }

        for(int i = 0; i < guards.length; i++){
            guards[i].update(true);
        }
        for(int i = 0; i < intruders.length; i++){
            if( !intruders[i].isEliminated()){
                intruders[i].update(false);
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
            if(!intruders[i].isEliminated()){
                intruders[i].draw(g);
            }
        }
    }
}
