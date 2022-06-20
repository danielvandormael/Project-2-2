package Logic;

import GUI.GamePanel;
import Logic.Entities.Entity;


public class CollisionDetection {

    GamePanel gamePanel;

    public CollisionDetection(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public void checkTile(Entity entity){
        //check for collisions
        double entityMoveToX = entity.movement.getX();
        double entityMoveToY = entity.movement.getY();



        if(entity.getActionMove() == 1){ //walk

            entityMoveToX += ( (1 / (double) gamePanel.getTileSize()) * (entity.movement.getBaseSpeed()/entity.movement.getSpeedRatio()) ) * Math.cos(Math.toRadians(entity.vision.getViewAngle()));
            entityMoveToY += ( (1 / (double) gamePanel.getTileSize()) * (entity.movement.getBaseSpeed()/entity.movement.getSpeedRatio()) ) * Math.sin(Math.toRadians(entity.vision.getViewAngle()));

        }else if(entity.getActionMove() == 2){ //sprint
            entityMoveToX += ( (1 / (double) gamePanel.getTileSize()) * (entity.movement.getSprintSpeed()/entity.movement.getSpeedRatio()) ) * Math.cos(Math.toRadians(entity.vision.getViewAngle()));
            entityMoveToY += ( (1 / (double) gamePanel.getTileSize()) * (entity.movement.getSprintSpeed()/entity.movement.getSpeedRatio()) ) * Math.sin(Math.toRadians(entity.vision.getViewAngle()));
        }

        int nextTile = gamePanel.tileM.mapTile[(int) entityMoveToX][(int) entityMoveToY];

        if(gamePanel.tileM.tile[nextTile].collision == true){
            entity.movement.cantMove = true;
        }
    }
}
