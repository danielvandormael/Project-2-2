package nl.maastrichtuniversity.dke.explorer.Logic;

import java.util.ArrayList;
import java.util.List;

public class ExpGuard {
    private int x;
    private int y;
    private int status;
    private double angle;
    private double baseSpeed;
    private int id;
    private int distanceViewing;
    private double currentSpeed;
    private Cell[][] map;
    private Cell currentCell;

    public ExpGuard(int x, int y, int status, double angle, double baseSpeed, int id, int distanceViewing, double currentSpeed){
        this.x = x;
        this.y = y;
        this.status = status;
        this.angle = angle;
        this.baseSpeed = baseSpeed;
        this.id = id;
        this.distanceViewing = distanceViewing;
        this.currentSpeed = currentSpeed;
        createMap();
    }

    private void createMap() {
        //?
    }

    public Cell[][] getMap(){
        return map;
    }

    public void move(int x, int y){
        currentCell = map[x][y];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getStatus() {
        return status;
    }

    public double getAngle() {
        return angle;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public int getId() {
        return id;
    }

    public int getDistanceViewing() {
        return distanceViewing;
    }

    public double getCurrentSpeed(){
        return currentSpeed;
    }

    public Cell getCurrentCell(){
        return map[(int) x][(int) y];
    }

    public int getDirection(){
        if (angle == 0){
            return 0;
        }
        if (angle == 90){
            return 1;
        }
        if (angle == 180){
            return 2;
        }
        if (angle == 270){
            return 3;
        }
        else return 4;
    }

    public List<Cell> getPossibleMoves() {
        //This methods returns 3 neighbour cells around the agent
        // cell infront, left + right cell
        //Since we don't know how to get the coordinates, this is what we have
        List<Cell> neighbours = new ArrayList<>();
        Cell cell1 = map[(int) (x+1)][(int) (y)];
        Cell cell2 = map[(int) (x-1)][(int) (y)];
        Cell cell3 = map[(int) (x)][(int) (y+1)];
        neighbours.add(cell1);
        neighbours.add(cell2);
        neighbours.add(cell3);
        return neighbours;
    }

    public List<Cell> getViewingArea() {
        // This method returns a list a Cells in front of the agent within the distance Viewing
        List<Cell> viewingArea = new ArrayList<>();
        for (int i = 1; i <= getBaseSpeed(); i++) {
            //Add the Cell to the viewing area according to the base speed(maximum cell that agent can move)
            //Since we don't know how to get the coordinates, this is what we have

            Cell a = map[(int) x][(int) (y+i)];
            viewingArea.add(a);

        }

        return viewingArea;
    }
}