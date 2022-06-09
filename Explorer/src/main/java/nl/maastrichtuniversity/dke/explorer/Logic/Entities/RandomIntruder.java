package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Cell;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomIntruder extends Intruder {

    private double desiredAngle;
    private int desiredX;
    private int desiredY;
    Cell targetCell;
    Cell currentCell;
    ArrayList<Cell> viewingArea = new ArrayList<>();

    int[] decision = new int[2]; // 1- movement  2- rotation

    Map map;

    public RandomIntruder(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(id, x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        map = new Map(gamePanel.scenario.getMapWidth(), gamePanel.scenario.getMapHeight());
        desiredX = (int) x;
        desiredY = (int) y;
        desiredAngle = viewAngle;
    }

    public void update(boolean isGuard) {
        if ((int) getX() == desiredX && (int) getY() == desiredY && getViewAngle() == desiredAngle){ randomlyMove();}
        setAction(decision[0], decision[1]);
        super.update(isGuard);
    }

    private void randomlyMove() {
        currentCell = map.getCell(getX(), getY());
        targetCell = map.getCell(desiredX, desiredY);

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

        List<Cell> candidateCells = new ArrayList<>();
        if (cellLeft.getStatus() != 3) candidateCells.add(cellLeft);
        if (cellRight.getStatus() != 3) candidateCells.add(cellRight);
        Collections.shuffle(candidateCells);


        if (cellFront.getStatus() != 3) {
            // if cell in front is not wall and do not have guard then go straight
            targetCell = cellFront;
            desiredX = targetCell.getX();
            desiredY = targetCell.getY();

            System.out.println("NO GUARD");
            decision[0] = 1;
            decision[1] = 0;
        }

//        else if (cellFront.getStatus() != 3 && guardsInView() ) {
//                //see guard within the viewRange -> rotate
//                // still not working bc the agent takes too long to rotate???
//                decision[0] = 0;
//                decision[1] = 1;
//                desiredAngle = map.getDirection(currentCell, candidateCells.get(0));
//                desiredX = (int) getX();
//                desiredY = (int) getY();
//        }

        //if cell in front is wall then check left and right cell and turn randomly to one of them
        else if (candidateCells.size() > 0) {
            Collections.shuffle(candidateCells);
            Cell choice = candidateCells.get(0);

            decision[0] = 0;
            decision[1] = 1;
            desiredAngle = map.getDirection(currentCell, choice);
            desiredX = (int) getX();
            desiredY = (int) getY();
        } else {
            if (isTargetReached(currentCell)) {
                decision[0] = 0;
                decision[1] = 0;
                System.out.println("The target has reached");
            }
        }
    }


    private boolean identifyGuard(Cell currentCell) {

        boolean guard = false;

//        for (int i = 1; i <= getViewRange(); i++) {
//            // For all cells in front within the viewRange
//            // if the cells in front is not wall then continue to add to viewing Area
//            //Only take straight cell infront into account
//            while (gamePanel.tileM.mapTile[currentCell.getX()][currentCell.getY()] != 1) {
//                currentCell = map.getCellInFront(currentCell, getViewAngle());
//                viewingArea.add(currentCell);
//            }
//        }
//
//        for (Cell cell : viewingArea) {
//            guard = cell.isGuardThere();
//        }
//        System.out.println(guard);
        return guard;

    }

    public boolean isTargetReached(Cell currentCell) {
        return gamePanel.tileM.mapTile[currentCell.getX()][currentCell.getY()] == 4;
    }
}
