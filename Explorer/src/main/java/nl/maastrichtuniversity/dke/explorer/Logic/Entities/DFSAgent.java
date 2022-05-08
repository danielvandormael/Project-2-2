package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Cell;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Map;
//import nl.maastrichtuniversity.dke.explorer.TestDFSAgent;

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


    public DFSAgent(double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        map = new Map(gamePanel.scenario.getMapWidth(), gamePanel.scenario.getMapHeight());
        desiredX= (int) x;
        desiredY= (int) y;
        desiredAngle = viewAngle;
    }

    public void update(){
        DFS();
        setAction(decision[0], decision[1]);
        super.update();
    }

    public void DFS() {
        if ((int) getX() == desiredX && (int) getY() == desiredY && getViewAngle() == desiredAngle) {
            nextStep();
        }
    }

    public boolean nextStep() {
        Cell currentCell = map.getCell(getX(), getY());
        targetCell = map.getCell(desiredX, desiredY);

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

        if(guardsInView()){
            Random rn = new Random();
            double random = Math.random();
            if (random < 0.33) {
                System.out.println("Turn randomly");
                List<Cell> candidateCells = new ArrayList();
                if (cellLeft.getStatus() == 0) candidateCells.add(cellLeft);
                if (cellRight.getStatus() == 0) candidateCells.add(cellRight);
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
}
