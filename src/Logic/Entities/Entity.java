package Logic.Entities;

import GUI.GamePanel;
import Logic.Area;
import Logic.Entities.astar.Node;
import Logic.Objects.Object;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Entity {
    private final int id;
    private double x, y;
    private double viewAngle;
    private final double viewRange;
    private final double viewAngleSize;
    private double viewHinder;
    private final double baseSpeed;
    private final  double sprintSpeed;
    private final double speedRatio = 20;
    private boolean collision = false;

    private final int hearRadius;
    private boolean walkSound;
    protected boolean shoutSound;
    private ArrayList<Sound> sounds;


    final int rayAmount = 15;
    private double [][] rayT = new double[rayAmount][3];
    Polygon viewArea;

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
    public BufferedImage hearImg, shoutImg, arrowImg;
    String direction;
    int picSprite;
    int picSpriteCounter;
    Color viewColor;


    GamePanel gamePanel;


    public Entity(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, Color viewColor, GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.id = id;
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
        this.hearRadius = 15;
        this.shoutSound = false;

        sounds = new ArrayList<Sound>();

        //initialize Senses images
        getSenseImage();

        //initialize viewArea
        rayCasting();
        updateViewArea();
    }


    public void setAction(int actionMove, int actionRotate){
        this.actionMove = actionMove;
        this.actionRotate = actionRotate;
    }

    public void update(){

        rotate();

        canHear(gamePanel.entityM.guards, gamePanel.entityM.intruders);

        collision = false;
        gamePanel.collisionD.checkTile(this);


        if(collision == false){
            move();
        }


        //update sprite
        if(actionMove > 0){
            if(picSprite == 0){
                picSprite = 2;
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
        updateViewArea();
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


    private void canHear(Entity[] guards, Entity[] intruders){
        sounds.clear();

        for(int i = 0; i<guards.length; i++){
            if(this.id != guards[i].getId()){
                if(guards[i].isWalkSound() == true && distanceBetween(guards[i].getX(), guards[i].getY()) <= hearRadius){
                    double[] dirVector = directionToCoords(guards[i].getX(), guards[i].getY());
                    sounds.add(new Sound(dirVector[0], dirVector[1], false));
                }
                if( guards[i].isShoutSound() == true && distanceBetween(guards[i].getX(), guards[i].getY()) <= hearRadius){
                    double[] dirVector = directionToCoords(guards[i].getX(), guards[i].getY());
                    sounds.add(new Sound(dirVector[0], dirVector[1], true));
                }
            }
        }
        for(int i = 0; i<intruders.length; i++){
            if(this.id != intruders[i].getId()){
                if(intruders[i].isWalkSound() == true && distanceBetween(intruders[i].getX(), intruders[i].getY()) <= hearRadius){
                    double[] dirVector = directionToCoords(intruders[i].getX(), intruders[i].getY());
                    sounds.add(new Sound(dirVector[0], dirVector[1], false));
                }
            }
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
            walkSound = true;
        }else if (actionMove == 2){ //sprint
            x += ( (1 / (double) gamePanel.getTileSize()) * (sprintSpeed/speedRatio) ) * Math.cos(Math.toRadians(viewAngle));
            y += ( (1 / (double) gamePanel.getTileSize()) * (sprintSpeed/speedRatio) ) * Math.sin(Math.toRadians(viewAngle));
            walkSound = true;
        }else{
            walkSound = false;
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
                        t1 = possibleT1 + 1;
                    }
                }
            }
        }
        return t1;
    }

    public void updateViewArea(){
        int[] arcX = new int[rayAmount+1];
        int[] arcY = new int[rayAmount+1];
        arcX[0] = (int) (x*gamePanel.getTileSize());
        arcY[0] = (int) (y*gamePanel.getTileSize());

        for(int i = 0; i < rayT.length ; i++){
            arcX[i+1] = (int) ((x+rayT[i][1]*rayT[i][0])*gamePanel.getTileSize());
            arcY[i+1] = (int) ((y+rayT[i][2]*rayT[i][0])*gamePanel.getTileSize());
        }

        viewArea = new Polygon(arcX, arcY, rayAmount+1);
    }

    public ArrayList<Node> tilesInView(){
        int startX;
        int startY;
        int endX;
        int endY;
        if((int) getX() < 20 && (int) getY() >= 20 && (int) getY() <= gamePanel.scenario.getMapHeight() - 20){
            startX = 0;
            startY = (int) getY()-20;
            endX = (int) getX()+20;
            endY = (int) getY()+20;
        }else if((int) getX() >= 20 && (int) getY() < 20 && (int) getX() <= gamePanel.scenario.getMapWidth() - 20){
            startX = (int) getX()-20;
            startY = 0;
            endX = (int) getX()+20;
            endY = (int) getY()+20;
        }else if((int) getX() < 20 && (int) getY() < 20){
            startX = 0;
            startY = 0;
            endX = (int) getX()+20;
            endY = (int) getY()+20;
        }else if((int) getY() >= 20 && (int) getX() > gamePanel.scenario.getMapWidth() - 20 &&
                (int) getY() <= gamePanel.scenario.getMapHeight() - 20){
            startX = (int) getX()-20;
            startY = (int) getY()-20;
            endX = gamePanel.scenario.getMapWidth();
            endY = (int) getY()+20;
        }else if((int) getX() >= 20 && (int) getX() <= gamePanel.scenario.getMapWidth() - 20 &&
                (int) getY() > gamePanel.scenario.getMapHeight() - 20){
            startX = (int) getX()-20;
            startY = (int) getY()-20;
            endX = (int) getX()+20;
            endY = gamePanel.scenario.getMapHeight();
        }else if((int) getX() > gamePanel.scenario.getMapWidth() - 20 &&
                (int) getY() > gamePanel.scenario.getMapHeight() - 20){
            startX = (int) getX()-20;
            startY = (int) getY()-20;
            endX = gamePanel.scenario.getMapWidth();
            endY = gamePanel.scenario.getMapHeight();
        }else{
            startX = (int) getX()-20;
            startY = (int) getY()-20;
            endX = (int) getX()+20;
            endY = (int) getY()+20;
        }


        ArrayList<Node> temp = new ArrayList<Node>();

        for(int i = startX; i< endX; i++){
            for(int j = startY; j< endY; j++){
                if (viewArea.contains(i * gamePanel.getTileSize() + gamePanel.getTileSize()/2, j * gamePanel.getTileSize() + gamePanel.getTileSize()/2) == true) {
                    temp.add(new Node(i,j));
                }
            }
        }

        return temp;
    }


    private void onTopOf(){
        if(gamePanel.tileM.mapTile[(int)x][(int)y] == 3){
            for(int i = 0; i < gamePanel.scenario.getTeleportals().size(); i++){
                if(gamePanel.scenario.getTeleportals().get(i).isHit(x,y) || gamePanel.scenario.getTeleportals().get(i).isHit(x+1,y+1)){
                    int [] temp = gamePanel.scenario.getTeleportals().get(i).getNewLocation();
                    x = temp[0];
                    y = temp[1];
                }
            }
        }else if(gamePanel.tileM.mapTile[(int)x][(int)y] == 2){
            viewHinder = 2;
        }else{
            viewHinder = 1;
        }
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

    private void getSenseImage(){
        try{
            hearImg = ImageIO.read(getClass().getResource("/resources/bit8/sense/hear.png"));
            shoutImg = ImageIO.read(getClass().getResource("/resources/bit8/sense/shout.png"));
            arrowImg = ImageIO.read(getClass().getResource("/resources/bit8/sense/arrow.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g){
        g.setColor(viewColor);
        g.fillPolygon(viewArea);

        g.drawImage(getImage(), (int) (x*gamePanel.getTileSize() - gamePanel.getTileSize()), (int) (y*gamePanel.getTileSize() - gamePanel.getTileSize()), gamePanel.getTileSize()*2,  gamePanel.getTileSize()*2, null);
        if(walkSound == true || shoutSound == true){
            g.drawImage(shoutImg, (int) (x*gamePanel.getTileSize()), (int) (y*gamePanel.getTileSize() - gamePanel.getTileSize()*2), gamePanel.getTileSize(),  gamePanel.getTileSize(), null);
        }

        if(sounds.size() > 0){
            g.drawImage(hearImg, (int) (x*gamePanel.getTileSize() - gamePanel.getTileSize()), (int) (y*gamePanel.getTileSize() - gamePanel.getTileSize()*2), gamePanel.getTileSize(),  gamePanel.getTileSize(), null);
        }
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getViewAngle() {
        return viewAngle;
    }

    public double getViewRange() {
        return viewRange;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public double getSprintSpeed() {
        return sprintSpeed;
    }

    public double getSpeedRatio() {
        return speedRatio;
    }

    public int getActionMove() {
        return actionMove;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public boolean isWalkSound() {
        return walkSound;
    }

    public boolean isShoutSound() {
        return shoutSound;
    }

    public double distanceBetween(double x2, double y2){
        return Math.sqrt(Math.pow(this.x - x2, 2) + Math.pow(this.y - y2, 2));
    }

    public double[] directionToCoords(double x2 , double y2){
        double[] temp = new double[2];

        temp[0] = x2 - this.getX();
        temp[1] = y2 - this.getY();
        return  temp;
    }
}
