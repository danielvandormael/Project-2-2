package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;

import java.awt.*;

public class EntityManager {

    Entity[] guards;
    Entity[] intruders;
    GamePanel gamePanel;

    public EntityManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        guards = new Entity[gamePanel.scenario.getNumGuards()];
        intruders = new Entity[gamePanel.scenario.getNumIntruders()];
        generateGuards();
        generateIntruder();
    }

    public void generateGuards() {
        double [][] tmp = gamePanel.scenario.spawnGuards();
        for(int i = 0; i< gamePanel.scenario.getNumGuards(); i++) {
            guards[i]= new DFSAgent(tmp[i][0], tmp[i][1], 0, 8, 180, gamePanel.scenario.getBaseSpeedGuard(), gamePanel.scenario.getBaseSpeedGuard(), gamePanel, (i+2)%2);
        }
    }

    public void generateIntruder() {
        double [][] tmp = gamePanel.scenario.spawnIntruders();
        for(int i = 0; i< gamePanel.scenario.getNumIntruders(); i++) {
            intruders[i]= new Intruder(tmp[i][0], tmp[i][1], 0, 12, 180, gamePanel.scenario.getBaseSpeedIntruder(), gamePanel.scenario.getSprintSpeedIntruder(), gamePanel);
        }
    }

    public void update() {
        for(int i = 0; i < guards.length; i++) {
            guards[i].update(true);
        }
        for(int i = 0; i < intruders.length; i++) {
            intruders[i].update(false);
        }
    }

    public void draw(Graphics2D g) {
        for(int i = 0; i < guards.length; i++) {
            guards[i].draw(g);
        }

        for(int i = 0; i < intruders.length; i++) {
            intruders[i].draw(g);
        }
    }
}
