package nl.maastrichtuniversity.dke.explorer.Logic.Entities.Senses;

import nl.maastrichtuniversity.dke.explorer.Logic.Entities.Entity;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Cell.Node;

import java.awt.*;
import java.util.ArrayList;

public class Vision {
    private double viewAngle;
    private final double viewRange;
    private final double viewAngleSize;
    private double viewHinder;
    private Color viewColor;

    final int rayAmount = 15;
    private double [][] rayT = new double[rayAmount][3];
    Polygon viewArea;

    Entity entity;

    public Vision(double viewAngle, double viewRange, double viewAngleSize, Color viewColor,Entity entity) {
        this.viewAngle = viewAngle;
        this.viewRange = viewRange;
        this.viewAngleSize = viewAngleSize;
        this.viewColor = viewColor;
        this.viewHinder = 1;
        this.entity = entity;

        //initialize viewArea
        rayCasting();
        updateViewArea();
    }

    public void update(){
        rotate();
        rayCasting();
        updateViewArea();
    }

    public void draw(Graphics2D g){
        g.setColor(viewColor);
        g.fillPolygon(viewArea);
    }

    private void rotate(){
        if(entity.getActionRotate() == 1){ //rotate left
            if(viewAngle + 0.5 >= 360){
                viewAngle = 0;
            }else {
                viewAngle += 0.5;
            }
        }else if (entity.getActionRotate() == 2){ //rotate right
            if(viewAngle - 0.5 <= 0){
                viewAngle = 360;
            }else {
                viewAngle -= 0.5;
            }
        }

        //update direction for image of entity
        if(viewAngle <= 315 && viewAngle >= 225){
            entity.animation.setDirection("down");
        }else if( viewAngle <= 135 && viewAngle >= 45){
            entity.animation.setDirection("up");
        }else if( viewAngle < 225 && viewAngle > 135){
            entity.animation.setDirection("left");
        }else if(viewAngle < 45 || viewAngle > 315 ){
            entity.animation.setDirection("right");
        }
    }

    private void rayCasting(){
        double currentAngle = viewAngle - viewAngleSize/2;
        double rayInterval = viewAngleSize/rayAmount;
        for(int i = 0; i < rayAmount; i++){
            double r_dx = Math.cos(Math.toRadians(currentAngle));
            double r_dy = Math.sin(Math.toRadians(currentAngle));
            rayT[i][0] = rayIntersectionSegment(entity.movement.getX(),entity.movement.getY(),r_dx, r_dy);
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

        for(int i = 0; i< entity.gamePanel.scenario.getWalls().size(); i++){
            double [][] temp = entity.gamePanel.scenario.getWalls().get(i).getLineSegments();
            for(int j = 0; j< temp.length; j++){
                t2 = (r_dx*(temp[j][1]-r_py) + r_dy*(r_px-temp[j][0]))/(temp[j][2]*r_dy - temp[j][3]*r_dx);
                if(t2 <= 1 && t2 >= 0){
                    double possibleT1 = (temp[j][0]+temp[j][2]*t2-r_px)/r_dx;
                    if(possibleT1 >= 0 && possibleT1 < t1){
                        t1 = possibleT1 + 0.7; //0.7 so we can see walls
                    }
                }
            }
        }
        if(t1 == viewRange/viewHinder){
            t1 += 0.7;
        }
        return t1;
    }

    private void updateViewArea(){
        int[] arcX = new int[rayAmount+1];
        int[] arcY = new int[rayAmount+1];
        arcX[0] = (int) (entity.movement.getX()*entity.gamePanel.getTileSize());
        arcY[0] = (int) (entity.movement.getY()*entity.gamePanel.getTileSize());

        for(int i = 0; i < rayT.length ; i++){
            arcX[i+1] = (int) ((entity.movement.getX()+rayT[i][1]*rayT[i][0])*entity.gamePanel.getTileSize());
            arcY[i+1] = (int) ((entity.movement.getY()+rayT[i][2]*rayT[i][0])*entity.gamePanel.getTileSize());
        }

        viewArea = new Polygon(arcX, arcY, rayAmount+1);
    }

    public boolean areGuardsInView(){
        int[][] tiles = oldTilesInView();
        boolean guard = false;
        Entity[] guards = entity.gamePanel.getEntityManager().guards;

        for (int i = 0; i < tiles.length; i++){
            for (int j = 0; j < guards.length; j++){
                if((int) guards[j].movement.getX() == tiles[i][1] && (int) guards[j].movement.getY() == tiles[i][2]
                        && ((int) this.entity.movement.getX() != tiles[i][1] && (int)this.entity.movement.getY() != tiles[i][2])) {
                    //System.out.println("guard = true");
                    guard = true;
                }
            }
        }
        return guard;
    }

    public ArrayList<Node> guardsInView(){
        ArrayList<Node> guardsInView = new ArrayList<>();
        for (int i = 0; i < entity.gamePanel.entityM.guards.length; i++) {
            if(entity.getId() != entity.gamePanel.entityM.guards[i].getId()){
                if (viewArea.contains(entity.gamePanel.entityM.guards[i].movement.getX() * entity.gamePanel.getTileSize(), entity.gamePanel.entityM.guards[i].movement.getY()* entity.gamePanel.getTileSize()) == true) {
                    //if(i < entity.gamePanel.entityM.intruders.length) {
                        guardsInView.add(new Node((int) entity.gamePanel.entityM.intruders[i].movement.getX(),(int) entity.gamePanel.entityM.intruders[i].movement.getY()));
                    //}
                }
            }
        }
        return guardsInView;
    }

    // TODO: Should return an array of Cells in which the intruders are that can be seen by the entity
    // This should basically be the same as the guardsInView,
    // so might be combined into entityInView
    public ArrayList<Node> intrudersInView(){
        ArrayList<Node> intrudersInView = new ArrayList<>();
        for (int i = 0; i < entity.gamePanel.entityM.intruders.length; i++) {
            if( entity.gamePanel.entityM.intruders[i] != null){
                if(entity.getId() != entity.gamePanel.entityM.intruders[i].getId()){
                    if(viewArea.contains(entity.gamePanel.entityM.intruders[i].movement.getX() * entity.gamePanel.getTileSize(), entity.gamePanel.entityM.intruders[i].movement.getY()* entity.gamePanel.getTileSize()) == true) {
                        intrudersInView.add(new Node((int) entity.gamePanel.entityM.intruders[i].movement.getX(),(int) entity.gamePanel.entityM.intruders[i].movement.getY()));
                    }
                }
            }
        }
        return intrudersInView;
    }

    public ArrayList<Node> tilesInView(){

        //area of tiles to check if in view
        int startX;
        int startY;
        int endX;
        int endY;

        int searchdistance = 15;
        if((int) entity.movement.getX() < searchdistance
                && (int) entity.movement.getY() >= searchdistance
                && (int) entity.movement.getY() <= entity.gamePanel.scenario.getMapHeight() - searchdistance){
            startX = 0;
            startY = (int) entity.movement.getY()-searchdistance;
            endX = (int) entity.movement.getX()+searchdistance;
            endY = (int) entity.movement.getY()+searchdistance;
        }else if((int) entity.movement.getX() >= searchdistance
                && (int) entity.movement.getY() < searchdistance
                && (int) entity.movement.getX() <= entity.gamePanel.scenario.getMapWidth() - searchdistance){
            startX = (int) entity.movement.getX()-searchdistance;
            startY = 0;
            endX = (int) entity.movement.getX()+searchdistance;
            endY = (int) entity.movement.getY()+searchdistance;
        }else if((int) entity.movement.getX() < searchdistance
                && (int) entity.movement.getY() < searchdistance){
            startX = 0;
            startY = 0;
            endX = (int) entity.movement.getX()+searchdistance;
            endY = (int) entity.movement.getY()+searchdistance;
        }else if((int) entity.movement.getY() >= searchdistance
                && (int) entity.movement.getX() > entity.gamePanel.scenario.getMapWidth() - searchdistance
                && (int) entity.movement.getY() <= entity.gamePanel.scenario.getMapHeight() - searchdistance){
            startX = (int) entity.movement.getX()-searchdistance;
            startY = (int) entity.movement.getY()-searchdistance;
            endX = entity.gamePanel.scenario.getMapWidth();
            endY = (int) entity.movement.getY()+searchdistance;
        }else if((int) entity.movement.getX() >= searchdistance
                && (int) entity.movement.getX() <= entity.gamePanel.scenario.getMapWidth() - searchdistance
                && (int) entity.movement.getY() > entity.gamePanel.scenario.getMapHeight() - searchdistance){
            startX = (int) entity.movement.getX()-searchdistance;
            startY = (int) entity.movement.getY()-searchdistance;
            endX = (int) entity.movement.getX()+searchdistance;
            endY = entity.gamePanel.scenario.getMapHeight();
        }else if((int) entity.movement.getX() > entity.gamePanel.scenario.getMapWidth() - searchdistance
                && (int) entity.movement.getY() > entity.gamePanel.scenario.getMapHeight() - searchdistance){
            startX = (int) entity.movement.getX()-searchdistance;
            startY = (int) entity.movement.getY()-searchdistance;
            endX = entity.gamePanel.scenario.getMapWidth();
            endY = entity.gamePanel.scenario.getMapHeight();
        }else{
            startX = (int) entity.movement.getX()-searchdistance;
            startY = (int) entity.movement.getY()-searchdistance;
            endX = (int) entity.movement.getX()+searchdistance;
            endY = (int) entity.movement.getY()+searchdistance;
        }


        ArrayList<Node> tilesInView = new ArrayList<>();

        for(int i = startX; i< endX; i++){
            for(int j = startY; j< endY; j++){
                if (viewArea.contains(i * entity.gamePanel.getTileSize() + entity.gamePanel.getTileSize()/2,
                        j * entity.gamePanel.getTileSize() + entity.gamePanel.getTileSize()/2) == true) {
                    tilesInView.add(new Node(i,j));
                }
            }
        }

        return tilesInView;
    }

    public int[][] oldTilesInView(){
        int amount = 0;
        for(int i = 0; i < rayT.length; i++){
            amount += rayT[i][0]*5;
        }

        int [][] temp = new int[amount][3];

        int counter = 0;
        for(int i = 0; i < rayT.length; i++){
            for(double j = 0; j < rayT[i][0]; j += 0.5){
                int coordX = (int) (entity.movement.getX() + j*rayT[i][1])-1;
                int coordY = (int) (entity.movement.getY() + j*rayT[i][2])-1;

                if (coordX > 0 && coordY > 0 && coordX < entity.gamePanel.tileM.mapTile.length-1 && coordY < entity.gamePanel.tileM.mapTile[0].length-1) {
                    temp[counter][0] = entity.gamePanel.tileM.mapTile[coordX][coordY];
                    temp[counter][1] = coordX;
                    temp[counter][2] = coordY;
                    counter++;
                }
            }
        }
        return temp;
    }

    public int turnWhichWay(double goalAngle){
        if(getViewAngle() - goalAngle >= 180){
            return 1;
        }else if(getViewAngle() - goalAngle < 180  && getViewAngle() - goalAngle >= 0){
            return 2;
        }else if(getViewAngle() - goalAngle >= -180){
            return 1;
        }else if(getViewAngle() - goalAngle < -180){
            return 2;
        }
        return 1;
    }

    public double getViewAngle() {
        return viewAngle;
    }

    public void setViewHinder(double viewHinder) {
        this.viewHinder = viewHinder;
    }
}
