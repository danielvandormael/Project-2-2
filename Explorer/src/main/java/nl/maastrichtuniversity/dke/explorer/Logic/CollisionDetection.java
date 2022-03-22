package nl.maastrichtuniversity.dke.explorer.Logic;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Logic.Entities.Entity;

public class CollisionDetection {

    GamePanel gamePanel;

    public CollisionDetection(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public void checkTile(Entity entity){
        int entityMoveToX1;
        int entityMoveToY1;
        int entityMoveToX2;
        int entityMoveToY2;

        if(entity.getActionMove() == 1){ //walk
            entityMoveToX1 = (int) (entity.getX() + ( (1/ (double) gamePanel.getTileSize()) * (entity.getBaseSpeed()/entity.getSpeedRatio()) ) * Math.cos(Math.toRadians(entity.getViewAngle())));
            entityMoveToY1 = (int) (entity.getY() + ( (1/ (double) gamePanel.getTileSize()) * (entity.getBaseSpeed()/entity.getSpeedRatio()) ) * Math.sin(Math.toRadians(entity.getViewAngle())));
            entityMoveToX2 = entityMoveToX1;
            entityMoveToY2 = entityMoveToY1;
        }else{ //sprint
            entityMoveToX1 = (int) (entity.getX() + ( (1/ (double) gamePanel.getTileSize()) * (entity.getSprintSpeed()/entity.getSpeedRatio()) ) * Math.cos(Math.toRadians(entity.getViewAngle())));
            entityMoveToY1 = (int) (entity.getY() + ( (1/ (double) gamePanel.getTileSize()) * (entity.getSprintSpeed()/entity.getSpeedRatio()) ) * Math.sin(Math.toRadians(entity.getViewAngle())));
            entityMoveToX2 = entityMoveToX1;
            entityMoveToY2 = entityMoveToY1;
        }

        int nextTile1, nextTile2, nextTile3;

        if(entity.getViewAngle() <= 90 && entity.getViewAngle() >= 0){
            nextTile1 = gamePanel.tileM.mapTile[entityMoveToX1][entityMoveToY1];
            nextTile2 = gamePanel.tileM.mapTile[entityMoveToX2][entityMoveToY1];
            nextTile3 = gamePanel.tileM.mapTile[entityMoveToX2][entityMoveToY2];
            if(gamePanel.tileM.tile[nextTile1].collision == true || gamePanel.tileM.tile[nextTile2].collision == true || gamePanel.tileM.tile[nextTile3].collision == true){
                entity.setCollision(true);
            }
        }else if( entity.getViewAngle() < 180 && entity.getViewAngle() > 90){
            nextTile1 = gamePanel.tileM.mapTile[entityMoveToX1][entityMoveToY1];
            nextTile2 = gamePanel.tileM.mapTile[entityMoveToX1][entityMoveToY2];
            nextTile3 = gamePanel.tileM.mapTile[entityMoveToX2][entityMoveToY1];
            if(gamePanel.tileM.tile[nextTile1].collision == true || gamePanel.tileM.tile[nextTile2].collision == true || gamePanel.tileM.tile[nextTile3].collision == true){
                entity.setCollision(true);
            }
        }else if( entity.getViewAngle() <= 270  && entity.getViewAngle() >= 180){
            nextTile1 = gamePanel.tileM.mapTile[entityMoveToX1][entityMoveToY1];
            nextTile2 = gamePanel.tileM.mapTile[entityMoveToX1][entityMoveToY2];
            nextTile3 = gamePanel.tileM.mapTile[entityMoveToX2][entityMoveToY2];
            if(gamePanel.tileM.tile[nextTile1].collision == true || gamePanel.tileM.tile[nextTile2].collision == true || gamePanel.tileM.tile[nextTile3].collision == true){
                entity.setCollision(true);
            }
        }else if(entity.getViewAngle() < 360 && entity.getViewAngle() > 270 ){
            nextTile1 = gamePanel.tileM.mapTile[entityMoveToX1][entityMoveToY2];
            nextTile2 = gamePanel.tileM.mapTile[entityMoveToX2][entityMoveToY1];
            nextTile3 = gamePanel.tileM.mapTile[entityMoveToX2][entityMoveToY2];
            if(gamePanel.tileM.tile[nextTile1].collision == true || gamePanel.tileM.tile[nextTile2].collision == true || gamePanel.tileM.tile[nextTile3].collision == true){
                entity.setCollision(true);
            }
        }
    }
}
