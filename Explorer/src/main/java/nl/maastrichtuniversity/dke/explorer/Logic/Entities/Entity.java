package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Logic.Objects.Object;
import nl.maastrichtuniversity.dke.explorer.Logic.Objects.ObjectManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Entity {

    private double x, y;
    private double viewAngle;
    private final double viewRange;
    private final double viewAngleSize;
    private double viewHinder;
    private final double baseSpeed;
    private final  double sprintSpeed;
    private final double speedRatio = 20;
    private boolean collision = false;

    final int rayAmount = 15;
    private double [][] rayT = new double[rayAmount][3];

    /*
       Action Move:
       0 - stand still
       1 - walk
       2 - run
       Action Rotate:
       0 - stand still
       1 - rotate left
       2 - rotate right
     */
    private int actionMove;
    private int actionRotate;

    //variables for graphics
    public BufferedImage left_stand, left_walk, right_stand, right_walk, down_stand, down_walk1, down_walk2, up_stand, up_walk1, up_walk2;
    String direction;
    int picSprite;
    int picSpriteCounter;
    Color viewColor;

    GamePanel gamePanel;

    public Entity(double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, Color viewColor, GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.x = x;
        this.y = y;
        this.viewAngle = viewAngle;
        this.viewRange = viewRange;
        this.viewAngleSize = viewAngleSize;
        this.baseSpeed = baseSpeed;
        this.sprintSpeed = sprintSpeed;
        this.viewColor = viewColor;

        this.direction = "down";
        this.picSprite = 1;
        this.picSpriteCounter = 0;
        this.viewHinder = 1;
    }


    public void setAction(int actionMove, int actionRotate){
        this.actionMove = actionMove;
        this.actionRotate = actionRotate;
    }

    public void update(boolean isGuard){

        leaveMarker(isGuard);

        rotate();

        collision = false;
        gamePanel.collisionD.checkTile(this);

        if(collision == false){
            move();
        }

        //System.out.println("actionMove: " + actionMove);
        //update sprite
        if(actionMove > 0){
            if(picSprite == 0){
                picSpriteCounter = 2;
            }
            picSpriteCounter++;
            if(picSpriteCounter > 12){
                if(picSprite == 1){
                    picSprite = 2;
                }else if(picSprite == 2){
                    picSprite = 1;
                }
                picSpriteCounter = 0;
            }
        }else{
            picSprite = 0;
        }

        onTopOf();

        rayCasting();
    }

    private void rotate(){
        if(actionRotate == 1){ //rotate left
            if(viewAngle + 0.5 >= 360){
                viewAngle = 0;
            }else {
                viewAngle += 0.5;
            }
        }else if (actionRotate == 2){ //rotate right
            if(viewAngle - 0.5 <= 0){
                viewAngle = 360;
            }else {
                viewAngle -= 0.5;
            }
        }

        //update direction for image of entity
        if(viewAngle <= 315 && viewAngle >= 225){
            direction = "up";
        }else if( viewAngle <= 135 && viewAngle >= 45){
            direction = "down";
        }else if( viewAngle < 225 && viewAngle > 135){
            direction = "left";
        }else if(viewAngle < 45 || viewAngle > 315 ){
            direction = "right";
        }
    }
    /*
        Movement equation:
        current position + (relative size of a pixel) * (speed ratio)
     */
    private void move(){

        if(actionMove == 1){ //walk
            x += ( (1 / (double) gamePanel.getTileSize()) * (baseSpeed/speedRatio) ) * Math.cos(Math.toRadians(viewAngle));
            y += ( (1 / (double) gamePanel.getTileSize()) * (baseSpeed/speedRatio) ) * Math.sin(Math.toRadians(viewAngle));
        }else if (actionMove == 2){ //sprint
            x += ( (1 / (double) gamePanel.getTileSize()) * (sprintSpeed/speedRatio) ) * Math.cos(Math.toRadians(viewAngle));
            y += ( (1 / (double) gamePanel.getTileSize()) * (sprintSpeed/speedRatio) ) * Math.sin(Math.toRadians(viewAngle));
        }
    }

    private void rayCasting(){
        double currentAngle = viewAngle - viewAngleSize/2;
        double rayInterval = viewAngleSize/rayAmount;
        for(int i = 0; i < rayAmount; i++){
            double r_dx = Math.cos(Math.toRadians(currentAngle));
            double r_dy = Math.sin(Math.toRadians(currentAngle));
            rayT[i][0] = rayIntersectionSegment(x,y,r_dx, r_dy);
            rayT[i][1] = r_dx;
            rayT[i][2] = r_dy;
            currentAngle += rayInterval;
        }
    }

    /*
    Ray intersection Equation:
     r_px+r_dx*T1 = s_px+s_dx*T2
     r_py+r_dy*T1 = s_py+s_dy*T2
     T2 = (r_dx*(s_py-r_py) + r_dy*(r_px-s_px))/(s_dx*r_dy - s_dy*r_dx)
     T1 = (s_px+s_dx*T2-r_px)/r_dx
     */
    private double rayIntersectionSegment(double r_px, double r_py, double r_dx, double r_dy){
        double t1 = viewRange/viewHinder;
        double t2;

        for(int i = 0; i< gamePanel.scenario.getWalls().size(); i++){
            double [][] temp = gamePanel.scenario.getWalls().get(i).getLineSegments();
            for(int j = 0; j< temp.length; j++){
                t2 = (r_dx*(temp[j][1]-r_py) + r_dy*(r_px-temp[j][0]))/(temp[j][2]*r_dy - temp[j][3]*r_dx);
                if(t2 <= 1 && t2 >= 0){
                    double possibleT1 = (temp[j][0]+temp[j][2]*t2-r_px)/r_dx;
                    if(possibleT1 >= 0 && possibleT1 < t1){
                        t1 = possibleT1;
                    }
                }
            }
        }
        return t1;
    }

    // This should return an array of Tiles in which a guard is located
    // If there are no guards, return null
    public boolean guardsInView(){
        int[][] tiles = tilesInView();
        boolean guard = false;
        Entity[] guards = gamePanel.getEntityManager().guards;

        for (int i = 0; i < tiles.length; i++){
            for (int j = 0; j < guards.length; j++){
                if((int) guards[j].getX() == tiles[i][1] && (int) guards[j].getY() == tiles[i][2] && ((int) this.getX() != tiles[i][1] && (int)this.getY() != tiles[i][2])) {
                    //System.out.println("guard = true");
                    guard = true;
                }
            }
        }
        return guard;
    }

    public int[][] tilesInView(){
        int amount = 0;
        for(int i = 0; i < rayT.length; i++){
            amount += rayT[i][0]*5;
        }

        int [][] temp = new int[amount][3];

        int counter = 0;
        for(int i = 0; i < rayT.length; i++){
            for(double j = 0; j < rayT[i][0]; j += 0.5){
                int coordX = (int) (x + j*rayT[i][1])-1;
                int coordY = (int) (y + j*rayT[i][2])-1;

                if (coordX > 0 && coordY > 0 && coordX < gamePanel.tileM.mapTile.length-1 && coordY < gamePanel.tileM.mapTile[0].length-1) {
                    temp[counter][0] = gamePanel.tileM.mapTile[coordX][coordY];
                    temp[counter][1] = coordX;
                    temp[counter][2] = coordY;
                    counter++;
                }
            }
        }
        return temp;
    }

    private void onTopOf(){
        if(gamePanel.tileM.mapTile[(int)x][(int)y] == 4 || gamePanel.tileM.mapTile[(int)(x + 1)][(int)(y + 1)] == 4){
            //gamePanel.endGameThread();
        }else if(gamePanel.tileM.mapTile[(int)x][(int)y] == 3 || gamePanel.tileM.mapTile[(int)(x + 1)][(int)(y + 1)] == 3){
            for(int i = 0; i < gamePanel.scenario.getTeleportals().size(); i++){
                if(gamePanel.scenario.getTeleportals().get(i).isHit(x,y) || gamePanel.scenario.getTeleportals().get(i).isHit(x+1,y+1)){
                    int [] temp = gamePanel.scenario.getTeleportals().get(i).getNewLocation();
                    x = temp[0];
                    y = temp[1];
                }
            }
        }else if(gamePanel.tileM.mapTile[(int)x][(int)y] == 2 || gamePanel.tileM.mapTile[(int)(x + 1)][(int)(y + 1)] == 2){
            viewHinder = 2;
        }else{
            viewHinder = 1;
        }
    }

    /*
    This first check will depend on hierarchy of markers, if there's one more important than another,
    we might have to remove one of them. Otherwise, they can both coexist, even if that's not visible in the GUI.
    However, since we're using an ArrayList to keep track of objects, it should be possible to add AL of objects,
    but that would make checking for markers harder.
    This is why removing markers, having only one at a time, can be much easier to handle further down the line.
     */
    public void leaveMarker(boolean isGuard) {
        // Firstly, check if there's a marker in the current tile (if there is, it'll be removed)
        //gamePanel.objectM.loopCleanMarker((int) x, (int) y);

        int newMarkerIndex = selectMarkerType(isGuard);
        gamePanel.objectM.addMarker((int) x, (int) y, newMarkerIndex);

    }

    /**
     * Defines what type of marker to add later on
     * Hierarchy of markers (i.e. which markers should have priority, since there can only be one at a time per tile)
     * WARNING (O) > BY-WALL (4) > DEAD END (1) > TIME PHEROMONE (2)
     * This means if none of the beforehand listed marker types is applicable, there will always be one to add.
     *
     * @param isGuard true, if the entity in question is a guard; false, otherwise
     *
     * @return markerTypeIndex index of the type of the marker to add
     */
    private int selectMarkerType(boolean isGuard) {

        int markerTypeIndex;

        // TODO: Duplicate markers that can be used by both, so they're easier to share/separate between sides (G vs I).
        //  So, instead of 5 markers, we'll have 9 (essentially 5 still):
        //  0 (exclusive for intruders) + 1 to 4 (other markers, for guards) + 5 to 8 (same markers, but for intruders)
        if(isGuard) { // Specific markers for *guards*

            markerTypeIndex = 2; // The TIME PHEROMONE is the definite one to add (margin of error)

        } else { // Specific markers for *intruders*
            if (guardsInView()) { // This one is exclusive for intruders
                //System.out.println("FOUND GUARDS");
                markerTypeIndex = 0; // WARNING MARKER
                // TODO: Also move the intruder that saw the guard
            } else {
                // TIME PHEROMONE - BASIC DEFAULT i.e. to be added (at least) every time! This marker
                // implies that type 3 markers are to be added too, could be done
                // in the same method 2 is created.
                markerTypeIndex = 2;
            }
        }
        /*
        PSEUDOCODE:

        // TODO: Add specific methods to handle the different types of marker checks
        IF (INTRUDER.VIEWS(GUARD))
            markerTypeIndex = 0;
        ELSE IF (INTRUDER/GUARD.IS_NEXT_TO(WALL))
            markerTypeIndex = 4; // BY-WALL
        ELSE IF (INTRUDER/GUARD.RETURNED_FROM_DEADEND())
            markerTypeIndex = 1; // DEAD END (TO LEAVE AS IT'S EXITING THE DEAD END i.e. it saw the same squares again
                                                so the marker will be left once it detects it can move into a new one)
        ELSE
            markerTypeIndex = 2;
         */

        return markerTypeIndex;
    }

    //sprite of entities
    private BufferedImage getImage(){

        BufferedImage image = null;

        switch (direction){
            case "left":
                if(picSprite == 1 || picSprite == 0){
                    image = left_stand;
                }else if(picSprite == 2){
                    image = left_walk;
                }
                break;
            case "right":
                if(picSprite == 1 || picSprite == 0){
                    image = right_stand;
                }else if(picSprite == 2){
                    image = right_walk;
                }
                break;
            case "up":
                if(picSprite == 0){
                    image = up_stand;
                }else if(picSprite == 1){
                    image = up_walk1;
                }else if(picSprite == 2){
                    image = up_walk2;
                }
                break;
            case "down":
                if(picSprite == 0){
                    image = down_stand;
                }else if(picSprite == 1){
                    image = down_walk1;
                }else if(picSprite == 2){
                    image = down_walk2;
                }
                break;
        }
        return image;
    }

    public void draw(Graphics2D g){

        g.setColor(viewColor);

        for(int i = 0; i < rayT.length - 1; i++){
            int arcX1 = (int) (x*gamePanel.getTileSize()) + gamePanel.getTileSize();
            int arcY1 = (int) (y*gamePanel.getTileSize()) + gamePanel.getTileSize();
            int arcX2 = (int) ((x+rayT[i][1]*rayT[i][0])*gamePanel.getTileSize());
            int arcY2 = (int) ((y+rayT[i][2]*rayT[i][0])*gamePanel.getTileSize());

            //get tail of third
            int arcX3 = (int) ((x+rayT[i+1][1]*rayT[i+1][0])*gamePanel.getTileSize());
            int arcY3 = (int) ((y+rayT[i+1][2]*rayT[i+1][0])*gamePanel.getTileSize());

            Polygon p = new Polygon(new int[]{arcX1, arcX2, arcX3}, new int[]{arcY1, arcY2, arcY3}, 3);
            g.fillPolygon(p);
        }
        g.drawImage(getImage(), (int) x*gamePanel.getTileSize() - gamePanel.getTileSize()/2, (int) y*gamePanel.getTileSize() - gamePanel.getTileSize()/2, gamePanel.getTileSize()*2,  gamePanel.getTileSize()*2, null);
    }

    public double getX() { return x; }

    public double getY() { return y; }

    public double getViewAngle() { return viewAngle; }

    public double getViewRange() { return viewRange; }

    public double getBaseSpeed() { return baseSpeed; }

    public double getSprintSpeed() { return sprintSpeed; }

    public double getSpeedRatio() { return speedRatio; }

    public int getActionMove() { return actionMove; }

    public void setCollision(boolean collision) { this.collision = collision; }

}
