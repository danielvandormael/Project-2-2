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
        0 - Alert Marker
        1 - Caution Marker
        2 - Dead End Marker
        3 - Finish Marker
        4 - Unexplored Marker
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
