package Logic.Entities.Senses;

import java.awt.*;

public class Sound {

    private double x;
    private double y;
    private boolean shout;

    public Sound(double x, double y, boolean shout){
        this.x = x;
        this.y = y;
        this.shout = shout;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
