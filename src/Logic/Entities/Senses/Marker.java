package Logic.Entities.Senses;

import Logic.Entities.Entity;

public class Marker {
    Entity entity;

    public boolean deadEnd, foundMarker;

    public Marker(Entity entity){
        this.entity = entity;
    }

    public void update(boolean isGuard){
        leaveMarker(isGuard);
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

        this.foundMarker = false;

        // Clean any markers placed previously on the current coordinates
        this.entity.gamePanel.objectM.loopCleanMarker((int) entity.movement.getX(), (int) entity.movement.getY(), isGuard);

        // Firstly, detect if there's a marker in the upcoming tile
        // Guards only detect guard markers, and intruders only detect intruder markers (isGuard defines that)
        if (entity.animation.getDirection() == "up") {
            this.foundMarker = entity.gamePanel.objectM.detectMarker((int) entity.movement.getX(), ((int) entity.movement.getY()) - 1, isGuard);
            //gamePanel.objectM.loopCleanMarker((int) x, ((int) y)-1, isGuard);
        } else if (entity.animation.getDirection() == "down") {
            this.foundMarker = entity.gamePanel.objectM.detectMarker((int) entity.movement.getX(), ((int) entity.movement.getY()) + 1, isGuard);
            //gamePanel.objectM.loopCleanMarker((int) x, ((int) y)+1, isGuard);
        } else if (entity.animation.getDirection() == "left") {
            this.foundMarker = entity.gamePanel.objectM.detectMarker(((int) entity.movement.getX()) - 1, (int) entity.movement.getY(), isGuard);
            //gamePanel.objectM.loopCleanMarker(((int) x)-1, (int) y, isGuard);
        } else {
            this.foundMarker = entity.gamePanel.objectM.detectMarker(((int) entity.movement.getX()) + 1, (int) entity.movement.getY(), isGuard);
            //gamePanel.objectM.loopCleanMarker(((int) x)+1, (int) y, isGuard);
        }

        if(foundMarker) {
            this.entity.vision.turnWhichWay(90);
        }

        int newMarkerIndex = selectMarkerType(isGuard);
        this.entity.gamePanel.objectM.addMarker((int) entity.movement.getX(), (int) entity.movement.getY(), newMarkerIndex);
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

            if (entity.vision.guardsInView().size() > 0) { // This one is exclusive for intruders
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

}
