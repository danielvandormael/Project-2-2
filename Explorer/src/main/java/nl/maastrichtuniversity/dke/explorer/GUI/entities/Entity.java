package nl.maastrichtuniversity.dke.explorer.GUI.entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public double x, y;
    public double viewAngle;
    public double viewRange;
    public double viewAngleSize;
    public double baseSpeed;
    public double sprintSpeed;

    public String direction;

    int pictureSwitch;

    public BufferedImage left_stand, left_walk, right_stand, right_walk, down_stand, down_walk1, down_walk2, up_stand, up_walk1, up_walk2;


    public Entity(double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed) {
        this.x = x;
        this.y = y;
        this.viewAngle = viewAngle;
        this.viewRange = viewRange;
        this.viewAngleSize = viewAngleSize;
        this.baseSpeed = baseSpeed;
        this.sprintSpeed = sprintSpeed;


        this.direction = "down";
        pictureSwitch = 1;
    }

    public BufferedImage getImage(){

        BufferedImage image = null;

        switch (direction){
            case "left":
                if(pictureSwitch == 1){
                    image = left_stand;
                }else if(pictureSwitch == 2){
                    image = left_walk;
                }
                break;
            case "right":
                if(pictureSwitch == 1){
                    image = right_stand;
                }else if(pictureSwitch == 2){
                    image = right_walk;
                }
                break;
            case "up":
                if(pictureSwitch == 1){
                    image = up_stand;
                }else if(pictureSwitch == 2){
                    image = up_walk1;
                }
                break;
            case "down":
                if(pictureSwitch == 1){
                    image = down_stand;
                }else if(pictureSwitch == 2){
                    image = down_walk1;
                }
                break;
        }
        return image;
    }



}
