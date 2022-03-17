package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import javafx.geometry.Point2D;

import java.awt.image.BufferedImage;

public class Entity {
    public double x, y;
    public Point2D position = new Point2D(x,y);
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

    public void walk(Point2D vector) {
        this.position = position.add(vector);
    }

    public void rotate(double angle){
        viewAngle += angle;
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


    public double getWidth() {return x;}

    public double getHeight() {return y;}

    public Point2D getPosition() {return position;}

    public double getAngle() {return viewAngle;}

    public Point2D getCenter() {
        return new Point2D(position.getX() + x / 2, position.getY() + y / 2);
    }


// --------------------------------Movement--------------------------------------

    private Point2D curVector = new Point2D(0, 0);
    private float maxTorque = 5f;
    private float curTorque = 0;

    public void addMovement(double curSpeed) {
        addMovement(curSpeed, getAngle());
    }
    private void addMovement(double curSpeed, double angle) {
        Point2D thrustVector =  newVector(curSpeed, Math.toRadians(-angle));
        curVector = curVector.add(thrustVector);
        curVector = limit(curVector);
    }

    private Point2D limit(Point2D tempVector) {
        if (tempVector.magnitude() > baseSpeed) {
            return curVector = tempVector.normalize().multiply(baseSpeed);
        } else {
            return curVector = tempVector;
        }
    }

    public void addTorque(float torqueForce) {
        float newTorque = curTorque + torqueForce;
        if (torqueForce > 0) {
            curTorque = Math.min(newTorque, maxTorque);
        } else {
            maxTorque = Math.max(newTorque, -maxTorque);
        }
    }

    private Point2D newVector(double scalar, double angle) {
        return new Point2D(
                (float) (Math.sin(angle) * baseSpeed),
                (float) (Math.cos(angle) * baseSpeed));
    }

    private void applyDrag() {
        float movementDrag = curVector.magnitude() < 0.5 ? 0.01f : 0.07f;
        float rotationDrag = curTorque < 0.2f ? 0.05f : 0.1f;
        curVector = new Point2D(
                toZero((float) curVector.getX(), movementDrag),
                toZero((float) curVector.getY(), movementDrag));
        curTorque = toZero(curTorque, rotationDrag);
    }

    private float toZero(float value, float modifier) {
        float newValue = 0;
        if (value > modifier) {
            newValue = value - modifier;
        } else if (value < -modifier) {
            newValue = value + modifier;
        }
        return newValue;
    }

    public void update() {
        applyDrag();
        walk(curVector);
        rotate(curTorque);
    }

}
