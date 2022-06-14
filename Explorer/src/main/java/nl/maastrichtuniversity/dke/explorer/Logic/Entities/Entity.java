package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GUIMain;
import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Logic.Entities.Senses.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Entity extends GUIMain {

    private final int id;

    /*
           Action Move:
           0 - stand still
           1 - walk
           2 - run

           Action Rotate:
           0 - stand still
           1 - rotate left
           2 - rotate right

           Action Shout:
           false - not shouting
           true - shouting
     */
    private int actionMove, actionRotate;

    public ArrayList<Double> prevX = new ArrayList<>();
    public ArrayList<Double> prevY = new ArrayList<>();
    public boolean deadEnd, foundMarker;

    public Animation animation;
    public Movement movement;
    public Vision vision;
    public SoundSense soundSense;
    public Collision collision;

    public GamePanel gamePanel;

    public Entity(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, Color viewColor, GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.id = id;

        animation = new Animation(this);
        movement = new Movement(x, y, baseSpeed, sprintSpeed, this);
        vision = new Vision(viewAngle, viewRange, viewAngleSize, viewColor, this);
        soundSense = new SoundSense(this);
        collision = new Collision(this);

        setAction(0, 0);
    }

    public void update(boolean isGuard) {

        //isStranded(500);

        leaveMarker(isGuard);

        vision.update();
        soundSense.update();
        movement.update();
        animation.update();
    }

    public void draw(Graphics2D g) {
        vision.draw(g);
        animation.draw(g);
    }

    public void setAction(int actionMove, int actionRotate) {
        this.actionMove = actionMove;
        this.actionRotate = actionRotate;
    }

    /**
     * Solves any issue of stranding that might occur for a panoply of reasons
     * @param margin the amount of update() runs before it's considered an entity is stranded
     */
    public void isStranded(int margin) {
        int count = 1;
        this.prevX.add(this.movement.getX());
        this.prevY.add(this.movement.getY());

        if(this.prevX.size() >= margin) {
            for (int i = this.prevX.size() - 1; i > (this.prevX.size()-margin); i--) {
                if (this.movement.getX() == this.prevX.get(i) && this.movement.getY() == this.prevY.get(i)) {
                    count++;
                }
            }
        }

        if(count == margin) {
            double threshold = Math.random();
            if(threshold < 0.5) {
                this.vision.turnWhichWay(90);
            } else {
                this.vision.turnWhichWay(180);
            }
            this.prevX.clear();
            this.prevY.clear();
        // Memory-saver, so that these array list never get exorbitantly big if no conflicts occur
        } else if(prevX.size() > margin*2) {
            this.prevX.clear();
            this.prevY.clear();
        }
    }

    /**
     * Leaves a marker at the current position of the entity in question
     *
     * @return isDeadEnd true, if the entity has detected the existence of a dead end
     */
    private boolean isDeadEnd() {
        boolean isDeadEnd = deadEnd;
        return isDeadEnd;
    }

    /*
    This first check will depend on hierarchy of markers, if there's one more important than another,
    we might have to remove one of them. Otherwise, they can both coexist, even if that's not visible in the GUI.
    However, since we're using an ArrayList to keep track of objects, it should be possible to add AL of objects,
    but that would make checking for markers harder.
    This is why removing markers, having only one at a time, can be much easier to handle further down the line.
     */

    /**
     * Leaves a marker at the current position of the entity in question
     *
     * @param isGuard true, if the entity in question is a guard; false, otherwise
     */
    public void leaveMarker(boolean isGuard) {

        foundMarker = false;

        // Clean any markers placed previously on the current coordinates
        gamePanel.objectM.loopCleanMarker((int) movement.getX(), (int) movement.getY(), isGuard);

        // Firstly, detect if there's a marker in the upcoming tile
        // Guards only detect guard markers, and intruders only detect intruder markers (isGuard defines that)
        if (animation.getDirection() == "up") {
            foundMarker = gamePanel.objectM.detectMarker((int) movement.getX(), ((int) movement.getY()) - 1, isGuard);
            //gamePanel.objectM.loopCleanMarker((int) x, ((int) y)-1, isGuard);
        } else if (animation.getDirection() == "down") {
            foundMarker = gamePanel.objectM.detectMarker((int) movement.getX(), ((int) movement.getY()) + 1, isGuard);
            //gamePanel.objectM.loopCleanMarker((int) x, ((int) y)+1, isGuard);
        } else if (animation.getDirection() == "left") {
            foundMarker = gamePanel.objectM.detectMarker(((int) movement.getX()) - 1, (int) movement.getY(), isGuard);
            //gamePanel.objectM.loopCleanMarker(((int) x)-1, (int) y, isGuard);
        } else {
            foundMarker = gamePanel.objectM.detectMarker(((int) movement.getX()) + 1, (int) movement.getY(), isGuard);
            //gamePanel.objectM.loopCleanMarker(((int) x)+1, (int) y, isGuard);
        }

        if(foundMarker) {
            vision.turnWhichWay(90);
        }

        int newMarkerIndex = selectMarkerType(isGuard);
        gamePanel.objectM.addMarker((int) movement.getX(), (int) movement.getY(), newMarkerIndex);
    }

    /**
     * Defines what type of marker to add later on
     * Hierarchy of markers (i.e. which markers should have priority, since there can only be one at a time per tile)
     * FOR GUARD:
     * BY-WALL (-) > DEAD END (1) > TIME PHEROMONE (0)
     * FOR INTRUDERS:
     * WARNING (4) > BY-WALL (-) > DEAD END (3) > TIME PHEROMONE (2)
     * This means if none of the beforehand listed marker types is applicable, there will always be one to add.
     *
     * @param isGuard true, if the entity in question is a guard; false, otherwise
     * @return markerTypeIndex index of the type of the marker to add
     */
    private int selectMarkerType(boolean isGuard) {

        int markerTypeIndex;

        if (isGuard) { // Specific markers for *guards*

            markerTypeIndex = 0;

//            if (isDeadEnd()) {
//                markerTypeIndex = 1; // DEAD END MARKER
//            } else {
//                markerTypeIndex = 0; // The TIME PHEROMONE is the definite one to add (margin of error)
//            }

        } else { // Specific markers for *intruders*

            if (vision.areGuardsInView()) { // This one is exclusive for intruders
                markerTypeIndex = 4; // WARNING MARKER
                // TODO: Also move the intruder that saw the guard
            } else if (isDeadEnd()) {
                markerTypeIndex = 3; // DEAD END MARKER
            } else {
                // TIME PHEROMONE - BASIC DEFAULT i.e. to be added (at least) every time! This marker
                // implies that type 3 markers are to be added too, could be done
                // in the same method 2 is created.
                markerTypeIndex = 2;
            }
        }

        return markerTypeIndex;
    }

    public int getId() { return this.id; }

    public int getActionMove() {
        return this.actionMove;
    }

    public int getActionRotate() {
        return this.actionRotate;
    }

    public double distanceBetween(double x2, double y2) {
        return Math.sqrt(Math.pow(this.movement.getX() - x2, 2) + Math.pow(this.movement.getY() - y2, 2));
    }

    public double[] directionToCoords(double x2, double y2) {
        double[] temp = new double[2];

        temp[0] = x2 - this.movement.getX();
        temp[1] = y2 - this.movement.getY();
        return temp;
    }
}
