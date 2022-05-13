package nl.maastrichtuniversity.dke.explorer.Logic.Objects;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ObjectManager {

    GamePanel gamePanel;
    BufferedImage[] objImg;
    ArrayList<Object> activeObjects;

    private int newIndexCounter;


    /* Types of Markers:
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
        this.objImg = new BufferedImage[5];
        this.activeObjects = new ArrayList<>();
        this.newIndexCounter = 0;
        getObjectImage8bit();
    }

    public void getObjectImage8bit(){
        try {
            // Should be interactable
            objImg[0] = ImageIO.read(ObjectManager.class.getResourceAsStream("/bit8/objects/marker0.png"));
            objImg[1] = ImageIO.read(ObjectManager.class.getResourceAsStream("/bit8/objects/marker1.png"));
            objImg[2] = ImageIO.read(ObjectManager.class.getResourceAsStream("/bit8/objects/marker2.png"));
            objImg[3] = ImageIO.read(ObjectManager.class.getResourceAsStream("/bit8/objects/marker3.png"));
            objImg[4] = ImageIO.read(ObjectManager.class.getResourceAsStream("/bit8/objects/marker4.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     @param x position on game panel
     @param y position on game panel
     */
    public void loopCleanMarker(int x, int y) {
        // Iterates all active marker objects (i.e. already created)
        for(int i = 0; i < activeObjects.size(); i++) {
            // Check if there is a marker in the provided position
            if((activeObjects.get(i).getX() == x) && (activeObjects.get(i).getY() == y)) {
                //System.out.println("FOUND A MARKER AT " + x + " " + y);
                activeObjects.remove(i);
            }
        }
    }

    /**
     @param marker Marker object to remove from activeObjects list
     */
    private void instaCleanMarker(Object marker) {
        //System.out.println("cleaning marker nr." + marker.getMarkerType() + " at " + marker.getX() + " " + marker.getY());
        activeObjects.remove(marker);
    }

    /**
     @param x position on game panel
     @param y position on game panel
     @param typeIndex the index of the marker type to add
     */
    public void addMarker(int x, int y, int typeIndex) {

        Object newMarker = new Object(false, objImg[typeIndex]);
        //System.out.println(x + " " + y);
        newMarker.setCoord(x,y);
        newMarker.setMarkerType(typeIndex);

        // TODO: Find a way to make markers recognizable what each agent
        //  should do once they see a certain marker, mostly, switch directions
        // Only add an object if it has different coordinates from the already added ones
        if(activeObjects.size() >= 1) {
            for (int i = activeObjects.size(); i < (activeObjects.size()+1); i++) {
                //System.out.println(newMarker.getX() + " " + activeObjects.get(i-1).getX());
                if (newMarker.getX() != activeObjects.get(i-1).getX()
                        || newMarker.getY() != activeObjects.get(i-1).getY()) {
                    activeObjects.add(newMarker);
//                    System.out.println("nr" + i + " - added marker at add()"
//                            + newMarker.getX() + " " + newMarker.getY() + " vs "
//                            + activeObjects.get(i-1).getX() + " " + activeObjects.get(i-1).getY());
                }
            }
        // Initial activeObjects addition (while size is 0)
        } else {
            activeObjects.add(newMarker);
            //System.out.println("1st - added marker at add(), x is " + activeObjects.get(0).getX());
        }

        //System.out.println("Adding marker " + activeObjects.size());
        // TODO: Make time-based marker (2) wear out every 2.5sec
//        if(newMarker.getMarkerType() == 2) {
//            //wearOutMarker(newMarker, 2500);
//        }
    }

    // Schedules a task to remove the time-based marker in question
    private void wearOutMarker(Object marker, int delay) {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        instaCleanMarker(marker);
                    }
                },
                delay
        );
    }

    public void draw(Graphics2D g){
        if(activeObjects.size() > 0){
            for(int i = 0; i < activeObjects.size(); i++){

                Object markerToDraw = activeObjects.get(i);
//                System.out.println("drawing marker nr." + i + " type " + markerToDraw.getMarkerType()
//                        + " at " + markerToDraw.getX() + " " + markerToDraw.getY() + " " + markerToDraw);
                //System.out.println(activeObjects.size());

                // Scale each marker to be drawn in the correct position on the panel
                g.drawImage(markerToDraw.getImage(),
                        markerToDraw.getX()*gamePanel.getTileSize(), markerToDraw.getY()*gamePanel.getTileSize(),
                        gamePanel.getTileSize(), gamePanel.getTileSize(), null);
            }
        }
    }

    public ArrayList<Object> getActiveObjects() { return this.activeObjects; }
}
