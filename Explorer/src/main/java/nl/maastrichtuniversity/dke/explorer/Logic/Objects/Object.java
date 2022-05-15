package nl.maastrichtuniversity.dke.explorer.Logic.Objects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Object {

    public double x, y;
    public int markerType;
    public boolean interact;
    public BufferedImage image;

    public Object(boolean interact, BufferedImage image) {
        this.interact = interact;
        this.image = image;
    }

    public void setCoord(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setMarkerType(int mT) { this.markerType = mT; }

    public double getX() { return this.x; }

    public double getY() { return this.y; }

    public int getMarkerType() { return this.markerType; }

    public boolean isInteractable() { return this.interact; }

    public Image getImage() { return this.image; }

}
