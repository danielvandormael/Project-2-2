package nl.maastrichtuniversity.dke.explorer.GUI.entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Prisoner  extends Entity{

    public Prisoner(double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed) {
        super(x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed);
        getPlayerImage();
    }

    public void getPlayerImage(){
        try{
            left_stand = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\16bit\\prisoner\\left_stand.png"));
            left_walk = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\16bit\\prisoner\\left_walk.png"));
            right_stand = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\16bit\\prisoner\\right_stand.png"));
            right_walk = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\16bit\\prisoner\\right_walk.png"));
            up_stand = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\16bit\\prisoner\\back_stand.png"));
            up_walk1 = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\16bit\\prisoner\\back_walk1.png"));
            up_walk2 = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\16bit\\prisoner\\back_walk2.png"));
            down_stand = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\16bit\\prisoner\\front_standing.png"));
            down_walk1 = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\16bit\\prisoner\\front_walk1.png"));
            down_walk2 = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\16bit\\prisoner\\front_walk2.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
