package Logic.Entities.Senses;

import Logic.CollisionDetection;
import Logic.Entities.Entity;

import java.awt.*;

public class Movement {
    private double x, y;
    private final double baseSpeed;
    private final  double sprintSpeed;
    private final double speedRatio = 20;
    private boolean throughTeleport;

    public boolean cantMove;


    private Entity entity;



    public Movement(double x, double y, double baseSpeed, double sprintSpeed, Entity entity) {
        this.x = x;
        this.y = y;
        this.baseSpeed = baseSpeed;
        this.sprintSpeed = sprintSpeed;
        this.throughTeleport = false;
        this.entity = entity;
        this.cantMove = false;
    }

    public void update(){
        entity.gamePanel.collisionDetection.checkTile(entity);
        move();
        onTopOf();
    }

    /*
    Movement equation:
    current position + ( (relative size of a pixel) * (chosen speed / speed ratio) * (cos or sin)(view angle) )
    */
    private void move(){
        if(entity.getActionMove() == 1){ //walk
            if(!entity.movement.cantMove){
                x += ( (1 / (double) entity.gamePanel.getTileSize()) * (baseSpeed/speedRatio) ) * Math.cos(Math.toRadians(entity.vision.getViewAngle()));
                y += ( (1 / (double) entity.gamePanel.getTileSize()) * (baseSpeed/speedRatio) ) * Math.sin(Math.toRadians(entity.vision.getViewAngle()));
            }
            entity.soundSense.setWalkSound(true);
        }else if (entity.getActionMove() == 2){ //sprint
            if(!entity.movement.cantMove){
                x += ( (1 / (double) entity.gamePanel.getTileSize()) * (sprintSpeed/speedRatio) ) * Math.cos(Math.toRadians(entity.vision.getViewAngle()));
                y += ( (1 / (double) entity.gamePanel.getTileSize()) * (sprintSpeed/speedRatio) ) * Math.sin(Math.toRadians(entity.vision.getViewAngle()));
            }
            entity.soundSense.setWalkSound(true);
        }else{
            entity.soundSense.setWalkSound(false);
        }
    }

    private void onTopOf(){
        if(entity.gamePanel.tileM.mapTile[(int)x][(int)y] == 3){
            for(int i = 0; i < entity.gamePanel.scenario.getTeleportals().size(); i++){
                if(entity.gamePanel.scenario.getTeleportals().get(i).isHit(x,y) || entity.gamePanel.scenario.getTeleportals().get(i).isHit(x+1,y+1)){
                    int [] temp = entity.gamePanel.scenario.getTeleportals().get(i).getNewLocation();
                    x = temp[0];
                    y = temp[1];
                    throughTeleport = true;
                }
            }
        }else if(entity.gamePanel.tileM.mapTile[(int)x][(int)y] == 2){
            entity.vision.setViewHinder(2);
            throughTeleport = false;
        }else{
            entity.vision.setViewHinder(1);
            throughTeleport = false;
        }
    }

    public double distanceBetween(double x2, double y2){
        return Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2));
    }

    public double[] directionToCoords(double x2 , double y2){
        double[] temp = new double[2];
        temp[0] = x2 - x;
        temp[1] = y2 - y;
        return  temp;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public double getSprintSpeed() {
        return sprintSpeed;
    }

    public double getSpeedRatio() {
        return speedRatio;
    }

    public boolean isThroughTeleport() {
        return throughTeleport;
    }
}
