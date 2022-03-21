package nl.maastrichtuniversity.dke.explorer.Logic.Objects;

import java.awt.image.BufferedImage;

public class Object {

    public int x, y;
    public boolean interact;
    public BufferedImage image;

    Object(boolean interact, BufferedImage image){
        this.interact = interact;
        this.image = image;
    }

    public void setCoord(int x, int y){
        this.x = x;
        this.y = y;
    }
}
