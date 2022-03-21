package nl.maastrichtuniversity.dke.explorer.Logic.Objects;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Logic.Entities.Intruder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ObjectManager {

    GamePanel gamePanel;
    Object[] object;
    ArrayList<Object> activeObjects;
    private int newIndexCounter;


    /*
        OBJECTS:
        0 - Alert Marker
        1 - Caution Marker
        2 - Dead End Marker
        3 - finish Marker
        4 - unexplored Marker
     */

    public ObjectManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        this.object = new Object[5];
        this.newIndexCounter = 0;
        getObjectImage8bit();
    }

    public void getObjectImage8bit(){
        try {
            object[0] = new Object(false, ImageIO.read(ObjectManager.class.getResourceAsStream("/bit8/objects/alertMarker.png")));
            object[1] = new Object(false, ImageIO.read(ObjectManager.class.getResourceAsStream("/bit8/objects/cautionMarker.png")));
            object[2] = new Object(false, ImageIO.read(ObjectManager.class.getResourceAsStream("/bit8/objects/deadMarker.png")));
            object[3] = new Object(false, ImageIO.read(ObjectManager.class.getResourceAsStream("/bit8/objects/finishMarker.png")));
            object[4] = new Object(false, ImageIO.read(ObjectManager.class.getResourceAsStream("/bit8/objects/unexploredMarker.png")));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void addObject(int x, int y, int index){
        Object temp = object[index];
        temp.setCoord(x, y);
        activeObjects.add(newIndexCounter, temp);
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
