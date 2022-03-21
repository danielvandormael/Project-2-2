package nl.maastrichtuniversity.dke.explorer.Logic.Tiles;

import java.awt.image.BufferedImage;

public class Tile {

    public BufferedImage image;
    public boolean collision;

    public Tile(boolean collision, BufferedImage image){
        this.collision = collision;
        this.image = image;
    }
}
