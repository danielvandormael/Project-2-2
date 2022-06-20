package Logic.Entities.Senses;

import Logic.Entities.Entity;

import java.awt.*;

public class Collision {
    private double X1;
    private double Y1;
    private double X2;
    private double Y2;

    private boolean collisionX;
    private boolean collisionY;

    Entity entity;

    public Collision(Entity entity) {
        this.entity = entity;
        this.X1 = entity.movement.getX() - 0.5;
        this.Y1 = entity.movement.getY() - 0.5;
        this.X2 = entity.movement.getX() + 0.5;
        this.Y2 = entity.movement.getY() + 0.5;
        this.collisionX = false;
        this.collisionY = false;
    }

    public void collisionDetection(){
        collisionX = false;
        collisionY = false;

        this.X1 = entity.movement.getX() - 0.5;
        this.Y1 = entity.movement.getY() - 0.5;
        this.X2 = entity.movement.getX() + 0.5;
        this.Y2 = entity.movement.getY() + 0.5;

        if(entity.getActionMove() == 1){ //walk
            X1 += ( (1 / (double) entity.gamePanel.getTileSize()) * (entity.movement.getBaseSpeed()/entity.movement.getSpeedRatio()) ) * Math.cos(Math.toRadians(entity.vision.getViewAngle()));
            Y1 += ( (1 / (double) entity.gamePanel.getTileSize()) * (entity.movement.getBaseSpeed()/entity.movement.getSpeedRatio()) ) * Math.sin(Math.toRadians(entity.vision.getViewAngle()));
            X2 += ( (1 / (double) entity.gamePanel.getTileSize()) * (entity.movement.getBaseSpeed()/entity.movement.getSpeedRatio()) ) * Math.cos(Math.toRadians(entity.vision.getViewAngle()));
            Y2 += ( (1 / (double) entity.gamePanel.getTileSize()) * (entity.movement.getBaseSpeed()/entity.movement.getSpeedRatio()) ) * Math.sin(Math.toRadians(entity.vision.getViewAngle()));
        }else  if(entity.getActionMove() == 2){ //sprint
            X1 += ( (1 / (double) entity.gamePanel.getTileSize()) * (entity.movement.getSprintSpeed()/entity.movement.getSpeedRatio()) ) * Math.cos(Math.toRadians(entity.vision.getViewAngle()));
            Y1 += ( (1 / (double) entity.gamePanel.getTileSize()) * (entity.movement.getSprintSpeed()/entity.movement.getSpeedRatio()) ) * Math.sin(Math.toRadians(entity.vision.getViewAngle()));
            X2 += ( (1 / (double) entity.gamePanel.getTileSize()) * (entity.movement.getSprintSpeed()/entity.movement.getSpeedRatio()) ) * Math.cos(Math.toRadians(entity.vision.getViewAngle()));
            Y2 += ( (1 / (double) entity.gamePanel.getTileSize()) * (entity.movement.getSprintSpeed()/entity.movement.getSpeedRatio()) ) * Math.sin(Math.toRadians(entity.vision.getViewAngle()));
        }

        collisionTiles(X1, Y1, X2, Y2);
        //collisionEntities(X1, Y1, X2, Y2, entity.gamePanel.entityM.intruders);
        //collisionEntities(X1, Y1, X2, Y2, entity.gamePanel.entityM.guards);
    }

    private void collisionTiles(double X1, double Y1, double X2, double Y2){
        if(entity.vision.getViewAngle() <= 90 && entity.vision.getViewAngle() >= 0){
            checkTile(X1, Y2, X2, Y1, true, true);
        }else if( entity.vision.getViewAngle() < 180 && entity.vision.getViewAngle() > 90){
            checkTile(X2, Y2, X1, Y1, false, true);
        }else if( entity.vision.getViewAngle() <= 270  && entity.vision.getViewAngle() >= 180){
            checkTile(X2, Y1, X1, Y2, false, false);
        }else if(entity.vision.getViewAngle() < 360 && entity.vision.getViewAngle() > 270 ){
            checkTile(X1, Y1, X2, Y2, true, false);
        }
    }

    private void checkTile(double coord1X, double coord1Y, double coord2X, double coord2Y, boolean subtractX, boolean subtractY){
        if(isTileCollision(coord1X, coord1Y) == true || isTileCollision(coord2X, coord1Y) == true || isTileCollision(coord2X, coord2Y) == true){
            if(isTileCollision(coord1X, coord1Y) == true && isTileCollision(coord2X, coord1Y) == true && isTileCollision(coord2X, coord2Y) == false){
                collisionY = true;
            }else if(isTileCollision(coord1X, coord1Y) == false && isTileCollision(coord2X, coord1Y) == true && isTileCollision(coord2X, coord2Y) == true){
                collisionX = true;
            }else if(isTileCollision(coord1X, coord1Y) == false && isTileCollision(coord2X, coord1Y) == true && isTileCollision(coord2X, coord2Y) == false){
                double addOn = 0.05;
                if(subtractX == true && subtractY == true){
                    if(isTileCollision(coord2X - addOn, coord1Y) == false){
                        collisionX = true;
                    }else if(isTileCollision(coord2X, coord1Y - addOn) == false){
                        collisionY = true;
                    }else{
                        collisionX = true;
                        collisionY = true;
                    }
                }else if(subtractX == true){
                    if(isTileCollision(coord2X - addOn, coord1Y) == false){
                        collisionX = true;
                    }else if(isTileCollision(coord2X, coord1Y + addOn) == false){
                        collisionY = true;
                    }else{
                        collisionX = true;
                        collisionY = true;
                    }
                }else if(subtractY == true){
                    if(isTileCollision(coord2X + addOn, coord1Y) == false){
                        collisionX = true;
                    }else if(isTileCollision(coord2X, coord1Y - addOn) == false){
                        collisionY = true;
                    }else{
                        collisionX = true;
                        collisionY = true;
                    }
                }else{
                    if(isTileCollision(coord2X + addOn, coord1Y) == false){
                        collisionX = true;
                    }else if(isTileCollision(coord2X, coord1Y + addOn) == false){
                        collisionY = true;
                    }else{
                        collisionX = true;
                        collisionY = true;
                    }
                }
            }else if(isTileCollision(coord1X, coord1Y) == true && isTileCollision(coord2X, coord1Y) == false && isTileCollision(coord2X, coord2Y) == false){
                collisionY = true;
            }else if(isTileCollision(coord1X, coord1Y) == false && isTileCollision(coord2X, coord1Y) == false && isTileCollision(coord2X, coord2Y) == true){
                collisionX = true;
            }else{
                collisionX = true;
                collisionY = true;
            }
        }
    }

    private boolean isTileCollision(double x, double y){
        int tile = entity.gamePanel.tileM.mapTile[(int) x][(int) y];
        return entity.gamePanel.tileM.tile[tile].collision;
    }

    private void collisionEntities(double X1, double Y1, double X2, double Y2, Entity[] entities){
        for (int i = 0; i < entities.length; i++) {
            if(entities[i] != null && entity.getId() != entities[i].getId()){
                if(entity.vision.getViewAngle() <= 90 && entity.vision.getViewAngle() >= 0){
                    checkEntity(X1, Y2, X2, Y1,true, true, entities[i]);
                }else if( entity.vision.getViewAngle() < 180 && entity.vision.getViewAngle() > 90){
                    checkEntity(X2, Y2, X1, Y1, false, true, entities[i]);
                }else if( entity.vision.getViewAngle() <= 270  && entity.vision.getViewAngle() >= 180){
                    checkEntity(X2, Y1, X1, Y2, false, false, entities[i]);
                }else if(entity.vision.getViewAngle() < 360 && entity.vision.getViewAngle() > 270 ){
                    checkEntity(X1, Y1, X2, Y2, true, false, entities[i]);
                }
            }
        }
    }

    private void checkEntity(double coord1X, double coord1Y, double coord2X, double coord2Y,  boolean subtractX, boolean subtractY, Entity other){
        if(other.collision.isHit(coord1X, coord1Y) == true || other.collision.isHit(coord2X, coord1Y) == true || other.collision.isHit(coord2X, coord2Y) == true){
            if(other.collision.isHit(coord1X, coord1Y) == true && other.collision.isHit(coord2X, coord2Y) == true && other.collision.isHit(coord2X, coord2Y) == false){
                collisionY = true;
            }else if(other.collision.isHit(coord1X, coord1Y) == false && other.collision.isHit(coord2X, coord1Y) == true && other.collision.isHit(coord2X, coord2Y) == true){
                collisionX = true;
            }else if(other.collision.isHit(coord1X, coord1Y) == false && other.collision.isHit(coord2X, coord1Y) == true && other.collision.isHit(coord2X, coord2Y) == false){
                double addOn = 0.5;
                if(subtractX == true && subtractY == true){
                    if(other.collision.isHit(coord2X - addOn, coord1Y) == false){
                        collisionX = true;
                    }else if(other.collision.isHit(coord2X, coord1Y - addOn) == false){
                        collisionY = true;
                    }else{
                        collisionX = true;
                        collisionY = true;
                    }
                }else if(subtractX == true){
                    if(other.collision.isHit(coord2X - addOn, coord1Y) == false){
                        collisionX = true;
                    }else if(other.collision.isHit(coord2X, coord1Y + addOn) == false){
                        collisionY = true;
                    }else{
                        collisionX = true;
                        collisionY = true;
                    }
                }else if(subtractY == true){
                    if(other.collision.isHit(coord2X + addOn, coord1Y) == false){
                        collisionX = true;
                    }else if(other.collision.isHit(coord2X, coord1Y - addOn) == false){
                        collisionY = true;
                    }else{
                        collisionX = true;
                        collisionY = true;
                    }
                }else{
                    if(other.collision.isHit(coord2X + addOn, coord1Y) == false){
                        collisionX = true;
                    }else if(other.collision.isHit(coord2X, coord1Y + addOn) == false){
                        collisionY = true;
                    }else{
                        collisionX = true;
                        collisionY = true;
                    }
                }
            } else if(other.collision.isHit(coord1X, coord1Y) == true && other.collision.isHit(coord2X, coord1Y) == false && other.collision.isHit(coord2X, coord2Y) == false){
                collisionY = true;
            }else if(other.collision.isHit(coord1X, coord1Y) == false && other.collision.isHit(coord2X, coord1Y) == false && other.collision.isHit(coord2X, coord2Y) == true){
                collisionX = true;
            }else{
                collisionX = true;
                collisionY = true;
            }
        }
    }

    public boolean isHit(double x, double y){
        return (y>=Y1)&(y<=Y2)&(x>=X1)&(x<=X2);
    }

    public boolean isCollisionX() {
        return collisionX;
    }

    public boolean isCollisionY() {
        return collisionY;
    }
}
