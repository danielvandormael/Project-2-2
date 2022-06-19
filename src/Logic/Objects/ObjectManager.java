package Logic.Objects;

import GUI.GamePanel;
import GUI.UtilityTool;

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

/* Types of Markers:
    (3) WARNING MARKER (for intruders only)
        -> Whenever an intruder sees a guard (since it has a bigger viewing area), it should turn back and
        leave a warning marker at that tile to warn the other intruder(s) that the guard had been checking
        that area recently. Should wear off after a while, since it's understood the guard kept moving.
    (2) DEAD END MARKER
        -> Whenever a guard/intruder goes into a dead end (and recognizes), on its way back, it marks the
        initial tile of that dead end.
    ? - Unexplored Marker
    * NEW POSSIBLE TYPES *
    (1) TIME / STEP-BASED PHEROMONE
        -> after x sec (or steps) from the tile the guard left the marker on,
        it disappears, as it represents how recently that tile was covered.
    (-) SMELL VARIATION (FROM TYPE 2)
        -> each stepped-on tile is marked w/ 2, as explained. Similarly, each tile covered by the viewing area also
        gets a marker to expand optimization. This will also wear out after x sec (or steps). These view-markers
        can be thought of as the smell of the original pheromone 2.
    (-) DEFINITE BY-WALL MARKER
        -> if an agent has tracked a wall, every step on a tile immediately next to it is
        marked. This is because it's an easy way of reducing any irrelevant re-tracks, since guards can more often
        than not view the by-wall tiles from any adjacent tiles.
 */

    public ObjectManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        this.objImg = new BufferedImage[5];
        this.activeObjects = new ArrayList<>();
        getObjectImage8bit();
    }

    public void getObjectImage8bit(){
            // Guard specific, 0 TIME | 1 DEAD END
            objImg[0] = setUp("marker1");
            objImg[1] = setUp("marker2");
            // Intruder specific, 2 TIME | 3 DEAD END | 4 WARNING
            objImg[2] = setUp("marker1");
            objImg[3] = setUp("marker2");
            objImg[4] = setUp("marker3");
    }

    /**
     * Pre renders image to speed up repaint
     @param imageName name of image

     */
    public BufferedImage setUp(String imageName){
        UtilityTool toolU = new UtilityTool();
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResource("/resources/bit8/objects/" + imageName +".png"));
            image = toolU.scaleImage(image, gamePanel.tileSize, gamePanel.tileSize);
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Detects if there is a marker at the provided coordinates
     @param x position on game panel
     @param y position on game panel
     */
    public boolean detectMarker(int x, int y, boolean isGuard) {

        boolean foundMarker = false;

        if(isGuard) {
            // Iterates all active marker objects (i.e. already created)
            for (int i = 0; i < activeObjects.size(); i++) {
                // Check if there is a marker in the provided position
                if (activeObjects.get(i) != null) {
                    if ((activeObjects.get(i).getX() == x) && (activeObjects.get(i).getY() == y) &&
                            ((activeObjects.get(i).getMarkerType() == 1) ||
                                    (activeObjects.get(i).getMarkerType() == 0))) {
                        //System.out.println("FOUND A MARKER AT " + x + " " + y);
                        foundMarker = true;

                    }
                }
            }
        } else {
            // Iterates all active marker objects (i.e. already created)
            for (int i = 0; i < activeObjects.size(); i++) {
                // Check if there is a marker in the provided position
                if (activeObjects.get(i) != null) {
                    if ((activeObjects.get(i).getX() == x) && (activeObjects.get(i).getY() == y) &&
                            ((activeObjects.get(i).getMarkerType() == 4) ||
                                    (activeObjects.get(i).getMarkerType() == 3) ||
                                    (activeObjects.get(i).getMarkerType() == 2))) {
                        //System.out.println("FOUND A MARKER AT " + x + " " + y);
                        foundMarker = true;

                    }
                }
            }
        }
        return foundMarker;
    }

    /**
     * Cleans the activeObjects list to make it not have repeated markers
     * Uses a loop, since it searches for the coordinates where a marker might be located
     @param x position on game panel
     @param y position on game panel
     */
    public void loopCleanMarker(int x, int y, boolean isGuard) {

        if(isGuard) {
            // Iterates all active marker objects (i.e. already created)
            for (int i = 0; i < activeObjects.size(); i++) {
                // Check if there is a marker in the provided position
                if (activeObjects.get(i) != null) {
                    if ((activeObjects.get(i).getX() == x) && (activeObjects.get(i).getY() == y) &&
                            ((activeObjects.get(i).getMarkerType() == 1) ||
                                    (activeObjects.get(i).getMarkerType() == 0))) {
                        activeObjects.remove(i);

                    }
                }
            }
        } else {
            // Iterates all active marker objects (i.e. already created)
            for (int i = 0; i < activeObjects.size(); i++) {
                // Check if there is a marker in the provided position
                if (activeObjects.get(i) != null) {
                    if ((activeObjects.get(i).getX() == x) && (activeObjects.get(i).getY() == y) &&
                            ((activeObjects.get(i).getMarkerType() == 4) ||
                                    (activeObjects.get(i).getMarkerType() == 3) ||
                                    (activeObjects.get(i).getMarkerType() == 2))) {
                        activeObjects.remove(i);

                    }
                }
            }
        }
    }

    /**
     * Cleans the activeObjects list to make it not have repeated markers
     * Can remove a marker instantly, since it knows the marker already
     @param marker Marker object to remove from activeObjects list
     */
    private void instaCleanMarker(Object marker) { activeObjects.remove(marker); }

    /**
     * Adds a marker to the activeObjects list
     @param x position on game panel
     @param y position on game panel
     @param typeIndex the index of the marker type to add
     */
    public void addMarker(int x, int y, int typeIndex) {

        Object newMarker = new Object(false, objImg[typeIndex]);
        //System.out.println(x + " " + y);
        newMarker.setCoord(x,y);
        newMarker.setMarkerType(typeIndex);

        activeObjects.add(newMarker);

        // Different markers have different lifespans, others are permanent
        if ((newMarker.getMarkerType() == 0) || (newMarker.getMarkerType() == 2)) { // TIME
            wearOutMarker(newMarker, 2500);
        } else if(newMarker.getMarkerType() == 4) { // WARNING
            wearOutMarker(newMarker, 5000);
        }
    }

    /**
     * Schedules a task to remove the time-based marker in question
     @param marker the marker that will wear out
     @param delay lifetime of the marker, in milliseconds (before timing out)
     */
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
//                System.out.println("size " + activeObjects.size());

                // Scale each marker to be drawn in the correct position on the panel
                if(markerToDraw != null) {
                    g.drawImage(markerToDraw.getImage(),
                            markerToDraw.getX()*gamePanel.getTileSize(), markerToDraw.getY()*gamePanel.getTileSize(), null);
                }
            }
        }
    }

    public ArrayList<Object> getActiveObjects() { return this.activeObjects; }
}
