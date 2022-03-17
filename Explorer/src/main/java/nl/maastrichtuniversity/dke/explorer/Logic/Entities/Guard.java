package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import javax.imageio.ImageIO;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Guard extends Entity{

    public Guard(double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed) {
        super(x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed);
        getPlayerImage();
    }

    public void getPlayerImage(){
        try{
            left_stand = ImageIO.read(Guard.class.getResourceAsStream("/bit16/guard/left_stand.png"));
            left_walk = ImageIO.read(Guard.class.getResourceAsStream("/bit16/guard/left_walk.png"));
            right_stand = ImageIO.read(Guard.class.getResourceAsStream("/bit16/guard/right_stand.png"));
            right_walk = ImageIO.read(Guard.class.getResourceAsStream("/bit16/guard/right_walk.png"));
            up_stand = ImageIO.read(Guard.class.getResourceAsStream("/bit16/guard/back_stand.png"));
            up_walk1 = ImageIO.read(Guard.class.getResourceAsStream("/bit16/guard/back_walk1.png"));
            up_walk2 = ImageIO.read(Guard.class.getResourceAsStream("/bit16/guard/back_walk2.png"));
            down_stand = ImageIO.read(Guard.class.getResourceAsStream("/bit16/guard/front_standing.png"));
            down_walk1 = ImageIO.read(Guard.class.getResourceAsStream("/bit16/guard/front_walk1.png"));
            down_walk2 = ImageIO.read(Guard.class.getResourceAsStream("/bit16/guard/front_walk2.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}