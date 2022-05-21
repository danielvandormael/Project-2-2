package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Cell;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Map;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Tile;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DFSAgent extends Guard {

    private static final int OPEN = 0;
    private static final int TRIED = 1;
    private static final int WALL = 2;

    private int orientation; // 1. LEFT 2. DOWN 3.RIGHT 4.UP

    private double desiredAngle;
    private int desiredX;
    private int desiredY;
    Cell previousCell;
    Cell targetCell;

    int [] decision = new int[2]; // 1- movement  2- rotation

    Map map;

    int type; // 0 favors turning left, 1 favors turning right

    public DFSAgent(double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel, int type) {
        super(x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        map = new Map(gamePanel.scenario.getMapWidth(), gamePanel.scenario.getMapHeight());
        desiredX= (int) x;
        desiredY= (int) y;
        desiredAngle = viewAngle;
        this.type = type;
    }

    public void update(boolean isGuard) {
        DFS();
        setAction(decision[0], decision[1]);
        super.update(isGuard);
    }

    public void DFS() {
        if ((int) getX() == desiredX && (int) getY() == desiredY && getViewAngle() == desiredAngle) {
            nextStep();
        }
    }

    public boolean nextStep() {
        Cell currentCell = map.getCell(getX(), getY());
        targetCell = map.getCell(desiredX, desiredY);

        this.setDeadEnd(false);
        //useless
        if (targetCell != null) {
            if (!currentCell.equals(targetCell)) {
                if (map.isInDirection(currentCell, targetCell, getViewAngle())) { //only need to change the desired position

                    desiredX = targetCell.getX();
                    desiredY = targetCell.getY();
                    return false;
                }
            }
        }

        //labeling parent cell
        if(currentCell.getStatus() == 0){
            currentCell.setStatus(1);
            if(currentCell.getParentCell() == null){
                Cell fromCell = currentCell;
                Cell previousCell;
                if(getViewAngle() < 180){
                    previousCell = map.getCellInFront(fromCell, getViewAngle()+180);
                }else if(getViewAngle() == 180){
                    previousCell = map.getCellInFront(fromCell, 0);
                }else{
                    previousCell = map.getCellInFront(fromCell, getViewAngle()-180);
                }
                fromCell.setParentCell(previousCell);
                fromCell.setStatus(1);
            }
        }


        //check to see if neighbor cells are walls
        Cell cellFront = map.getCellInFront(currentCell, getViewAngle());
        if (gamePanel.tileM.mapTile[cellFront.getX()][cellFront.getY()] == 1) {
            cellFront.setStatus(3);
        }

        Cell cellLeft = map.getLeftCell(currentCell, getViewAngle());
        if (gamePanel.tileM.mapTile[cellLeft.getX()][cellLeft.getY()] == 1) {
            cellLeft.setStatus(3);
        }

        Cell cellRight = map.getRightCell(currentCell, getViewAngle());
        if (gamePanel.tileM.mapTile[cellRight.getX()][cellRight.getY()] == 1) {
            cellRight.setStatus(3);
        }

        // Check if there are intruders in the view
        Cell[] intruders = intrudersInView();
        Cell closest = null;
        if(intruders != null){
            boolean move = false;
            // If there is more than 1 intruder in view, go to the closest one
            if (intruders.length > 1){
                closest = getClosestIntruder(intruders);
            }
            else closest = intruders[0];
            // If intruder is in front of the guard, sprint forwards
            if (map.isInDirection(currentCell, targetCell, getViewAngle())) {
                targetCell = closest;
                move = true;
                // If intruder is not right in front, targetCell is a cell that has the same x or y coordinate as intruder
            } else if (map.isInDirection(currentCell, map.getCell(getX(), closest.getY()), getViewAngle())){
                targetCell = map.getCell(getX(), closest.getY());
                move = true;
            }
            else if (map.isInDirection(currentCell, map.getCell(closest.getX(), getY()), getViewAngle())){
                targetCell = map.getCell(closest.getX(), getY());
                move = true;
            }

            if (move) {
                // move towards target
                decision[0] = 2;
                decision[1] = 0;
                desiredX= targetCell.getX();
                desiredY= targetCell.getY();
                return false;
            }
            else {
                double distanceX = Math.abs(getX() - closest.getX());
                double distanceY = Math.abs(getY() - closest.getY());
                if (distanceX > distanceY){
                    targetCell = map.getCell(closest.getX(), getY());
                }
                else {
                    targetCell = map.getCell(getX(), closest.getY());
                }

                // turn towards target
                decision[0] = 0;
                decision[1] = 1;
                desiredX= (int) getX();
                desiredY= (int) getY();
                desiredAngle = map.getDirection(currentCell, targetCell);
                return false;
            }
        }

        // Will wait and turn randomly if it sees another guard searching nearby or a marker
        if(guardsInView() || foundMarker){
            Random rn = new Random();
            double random = Math.random();
            if (random < 0.33) {
                System.out.println("Turn randomly");
                List<Cell> candidateCells = new ArrayList();
                if (cellLeft.getStatus() == 0) candidateCells.add(cellLeft);
                if (cellRight.getStatus() == 0) candidateCells.add(cellRight);
                if (candidateCells.size() > 1){
                    Cell choice = candidateCells.get(type);
                    // turn and then check again how far you can go in that direction
                    decision[0] = 0;
                    decision[1] = 1;
                    desiredAngle = map.getDirection(currentCell, choice);
                    desiredX= (int) getX();
                    desiredY= (int) getY();
                    return false;
                }
                if (candidateCells.size() > 0) {
                    Cell choice = candidateCells.get(0);
                    // turn and then check again how far you can go in that direction
                    decision[0] = 0;
                    decision[1] = 1;
                    desiredAngle = map.getDirection(currentCell, choice);
                    desiredX = (int) getX();
                    desiredY = (int) getY();
                    return false;
                }
            }
        }

        if (cellFront.getStatus() == 0) {
            // we can continue in same direction
            targetCell = cellFront;
            desiredX = targetCell.getX();
            desiredY = targetCell.getY();
            decision[0] = 1;
            decision[1] = 0;
            return false;
        }


        // check if left or right is still unexplored, if yes turn to one of them (favor left),
        // if not proceed with next part
        List<Cell> candidateCells = new ArrayList();
        if (cellLeft.getStatus() == 0) candidateCells.add(cellLeft);
        if (cellRight.getStatus() == 0) candidateCells.add(cellRight);
        if (candidateCells.size() > 1){
            Cell choice = candidateCells.get(type);
            // turn and then check again how far you can go in that direction
            decision[0] = 0;
            decision[1] = 1;
            desiredAngle = map.getDirection(currentCell, choice);
            desiredX= (int) getX();
            desiredY= (int) getY();
            return false;
        }
        if (candidateCells.size() > 0) {
            Cell choice = candidateCells.get(0);
            // turn and then check again how far you can go in that direction
            decision[0] = 0;
            decision[1] = 1;
            desiredAngle = map.getDirection(currentCell, choice);
            desiredX= (int) getX();
            desiredY= (int) getY();
            return false;
        }

        // if there are no unexplored neighbours

        // Technically, works as a dead-end (at least for the intruder, whose goal is to explore everything)
        this.setDeadEnd(true);

        currentCell.setStatus(2);
        targetCell = currentCell.getParentCell();

        if(map.isInDirection(currentCell, targetCell, getViewAngle())) {
            // move towards target
            decision[0] = 1;
            decision[1] = 0;
            desiredX= targetCell.getX();
            desiredY= targetCell.getY();
            return false;
        } else {
            // turn towards target
            decision[0] = 0;
            decision[1] = 1;
            desiredX= (int) getX();
            desiredY= (int) getY();
            desiredAngle = map.getDirection(currentCell, targetCell);
            return false;
        }

    }

    public Map getMap() {
        return map;
    }

    public Cell getClosestIntruder(Cell[] intruders){
        double distance = 100;
        Cell closestCellWithIndruder = null;
        for (int i = 0; i < intruders.length; i++){
            double newDistance = Point2D.distance(getX(), getY(), intruders[i].getX(), intruders[i].getX());
            if(newDistance < distance){
                distance = newDistance;
                closestCellWithIndruder = intruders[i];
            }
        }
        return closestCellWithIndruder;
    }
}
