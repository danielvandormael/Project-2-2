package nl.maastrichtuniversity.dke.explorer.Logic;

import java.util.ArrayList;

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
            checkIntruderCaught(entity, entityMoveToX1, entityMoveToY1);
            checkIntruderCaught(entity, entityMoveToX2, entityMoveToY1);
            checkIntruderCaught(entity, entityMoveToX2, entityMoveToY2);
            if(gamePanel.tileM.tile[nextTile1].collision == true || gamePanel.tileM.tile[nextTile2].collision == true || gamePanel.tileM.tile[nextTile3].collision == true){
                entity.setCollision(true);
            }
        }else if( entity.getViewAngle() < 180 && entity.getViewAngle() > 90){
            nextTile1 = gamePanel.tileM.mapTile[entityMoveToX1][entityMoveToY1];
            nextTile2 = gamePanel.tileM.mapTile[entityMoveToX1][entityMoveToY2];
            nextTile3 = gamePanel.tileM.mapTile[entityMoveToX2][entityMoveToY1];
            checkIntruderCaught(entity, entityMoveToX1, entityMoveToY1);
            checkIntruderCaught(entity, entityMoveToX1, entityMoveToY2);
            checkIntruderCaught(entity, entityMoveToX2, entityMoveToY1);
            if(gamePanel.tileM.tile[nextTile1].collision == true || gamePanel.tileM.tile[nextTile2].collision == true || gamePanel.tileM.tile[nextTile3].collision == true){
                entity.setCollision(true);
            }
        }else if( entity.getViewAngle() <= 270  && entity.getViewAngle() >= 180){
            nextTile1 = gamePanel.tileM.mapTile[entityMoveToX1][entityMoveToY1];
            nextTile2 = gamePanel.tileM.mapTile[entityMoveToX1][entityMoveToY2];
            nextTile3 = gamePanel.tileM.mapTile[entityMoveToX2][entityMoveToY2];
            checkIntruderCaught(entity, entityMoveToX1, entityMoveToY1);
            checkIntruderCaught(entity, entityMoveToX1, entityMoveToY2);
            checkIntruderCaught(entity, entityMoveToX2, entityMoveToY2);
            if(gamePanel.tileM.tile[nextTile1].collision == true || gamePanel.tileM.tile[nextTile2].collision == true || gamePanel.tileM.tile[nextTile3].collision == true){
                entity.setCollision(true);
            }
        }else if(entity.getViewAngle() < 360 && entity.getViewAngle() > 270 ){
            nextTile1 = gamePanel.tileM.mapTile[entityMoveToX1][entityMoveToY2];
            nextTile2 = gamePanel.tileM.mapTile[entityMoveToX2][entityMoveToY1];
            nextTile3 = gamePanel.tileM.mapTile[entityMoveToX2][entityMoveToY2];
            checkIntruderCaught(entity, entityMoveToX1, entityMoveToY2);
            checkIntruderCaught(entity, entityMoveToX2, entityMoveToY1);
            checkIntruderCaught(entity, entityMoveToX2, entityMoveToY2);
            if(gamePanel.tileM.tile[nextTile1].collision == true || gamePanel.tileM.tile[nextTile2].collision == true || gamePanel.tileM.tile[nextTile3].collision == true){
                entity.setCollision(true);
            }
        }


    }

    // Checks if any guards will collide with intruders after moving
    public void checkIntruderCaught(Entity entity, int nextTileX, int nextTileY){
        boolean intruderCaught = false;
        //get positions of all guards and intruders
        ArrayList<Double> guardXPositions = new ArrayList<Double>();
        ArrayList<Double> guardYPositions = new ArrayList<Double>();
        for (int i = 0; i < gamePanel.entityM.guards.length; i++){
            guardXPositions.add(gamePanel.entityM.guards[i].getX());
            guardYPositions.add(gamePanel.entityM.guards[i].getY());
        }
        ArrayList<Double> intruderXPositions = new ArrayList<Double>();
        ArrayList<Double> intruderYPositions = new ArrayList<Double>();
        for (int i = 0; i < gamePanel.entityM.intruders.length; i++){
            intruderXPositions.add(gamePanel.entityM.intruders[i].getX());
            intruderYPositions.add(gamePanel.entityM.intruders[i].getY());
        }
        // If entity is a guard then check for collisions with intruders and vice versa
        if (guardXPositions.contains(entity.getX()) && guardYPositions.contains(entity.getY())){
            if (intruderXPositions.contains(nextTileX) && intruderYPositions.contains(nextTileY)){
                intruderCaught = true;
            }
        }
        else if (intruderXPositions.contains(entity.getX()) && intruderYPositions.contains(entity.getY())){
            if (guardXPositions.contains(nextTileX) && guardYPositions.contains(nextTileY)){
                intruderCaught = true;
            }
        }
    }
}
