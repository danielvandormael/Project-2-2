package nl.maastrichtuniversity.dke.explorer.Logic.Objects;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ObjectManager {

    GamePanel gamePanel;
    Object[] objects;
    ArrayList<Object> activeObjects;
    private int newIndexCounter;


    /*
        Types of Markers:
        ? - Alert Marker
        ? - Caution Marker
        (0) WARNING MARKER (for intruders only)
            -> Whenever an intruder sees a guard (since it has a bigger viewing area), it should turn back and
            leave a warning marker at that tile to warn the other intruder(s) that the guard had been checking
            that area recently. Should wear off after a while, since it's understood the guard kept moving.
        (1) DEAD END MARKER
            -> Whenever a guard/intruder goes into a dead end (and recognizes), on its way back, it marks the
            initial tile of that dead end.
        ? - Unexplored Marker

        * NEW POSSIBLE TYPES *
        (2) TIME / STEP-BASED PHEROMONE
            -> after x sec (or steps) from the tile the guard left the marker on,
            it disappears, as it represents how recently that tile was covered.
        (3) SMELL VARIATION (FROM TYPE 2)
            -> each stepped-on tile is marked w/ 2, as explained. Similarly, each tile covered by the viewing area also
            gets a marker to expand optimization. This will also wear out after x sec (or steps). These view-markers
            can be thought of as the smell of the original pheromone 2.
        (4) DEFINITE BY-WALL MARKER
            -> if an agent has tracked a wall, every step on a tile immediately next to it is
            marked. This is because it's an easy way of reducing any irrelevant re-tracks, since guards can more often
            than not view the by-wall tiles from any adjacent tiles.
     */

    public ObjectManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        this.objects = new Object[5];
        this.newIndexCounter = 0;
        getObjectImage8bit();
    }

    public void getObjectImage8bit(){
        try {
            objects[0] = new Object(false, ImageIO.read(ObjectManager.class.getResourceAsStream("/bit8/objects/alertMarker.png")));
            objects[1] = new Object(false, ImageIO.read(ObjectManager.class.getResourceAsStream("/bit8/objects/cautionMarker.png")));
            objects[2] = new Object(false, ImageIO.read(ObjectManager.class.getResourceAsStream("/bit8/objects/deadMarker.png")));
            objects[3] = new Object(false, ImageIO.read(ObjectManager.class.getResourceAsStream("/bit8/objects/finishMarker.png")));
            objects[4] = new Object(false, ImageIO.read(ObjectManager.class.getResourceAsStream("/bit8/objects/unexploredMarker.png")));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void removeMarker(int x, int y, int markerIndex) {
        activeObjects.remove(markerIndex);
    }

    public void addMarker(int x, int y, int typeIndex) {
        Object marker = objects[typeIndex];
        marker.setCoord(x, y);
        activeObjects.add(newIndexCounter, marker);
        newIndexCounter++;
    }

    public void draw(Graphics2D g){
        if(activeObjects != null){
            for(int i = 0; i < activeObjects.size(); i++){
                g.drawImage(activeObjects.get(i).image, activeObjects.get(i).x, activeObjects.get(i).y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
            }
        }
    }
}
