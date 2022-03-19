package Logic;

import java.util.ArrayList;
import java.util.List;

public class MDFS {

    Scenario scenario;

    public MDFS(Scenario scenario) {
        this.scenario = scenario;
    }

    public boolean nextStep(ExpGuard guard) {
        Cell currentCell = guard.getCurrentCell();
        Cell targetCell = guard.getTargetCell();
        Map map = guard.getMap();
        int direction = guard.getDirection();

        if (targetCell != null) {
            if (!currentCell.equals(targetCell)) {
                if (map.isInDirection(currentCell, targetCell, direction)) {
                    guard.setNextDirection(direction);
                    guard.setNextSpeed(map.getDistance(currentCell, targetCell));
                    return false;
                }
            }
        }
        if (currentCell.getStatus() == 0) {
            currentCell.setStatus(1);
            // traverse back to previous Cell and set the parent for each intermediate Cell
            int directionFromLastMove = guard.getNextDirection();
            int distanceFromLastMove = guard.getNextSpeed();
            Cell fromCell = currentCell;
            while (distanceFromLastMove > 0) {
                distanceFromLastMove--;
                // next cell in opposite direction
                Cell nextCell = map.getCellInFront(fromCell, (directionFromLastMove + 2) % 4);
                fromCell.setParentCell(nextCell);
                fromCell.setStatus(1);
                fromCell = nextCell;
            }
        }

        Cell cellInFront = map.getCellInFront(currentCell, direction);
        if (inWall(cellInFront.getX(), cellInFront.getY())) {
            cellInFront.setStatus(3);
        }

        Cell cellLeft = map.getLeftCell(currentCell, direction);
        if (inWall(cellLeft.getX(), cellLeft.getY())) {
            cellLeft.setStatus(3);
        }

        Cell cellRight = map.getRightCell(currentCell, direction);
        if (inWall(cellRight.getX(), cellRight.getY())) {
            cellRight.setStatus(3);
        }

        if (cellInFront.getStatus() == 0) {
            // we can continue in same direction
            targetCell = cellInFront;
            int nextSpeed = 1;
            Cell nextCell = map.getCellInFront(targetCell, direction);
            while (nextSpeed < guard.getBaseSpeed() && nextSpeed < guard.getDistanceViewing() &&
                    nextCell.getStatus() == 0 && !inWall(nextCell.getX(), nextCell.getY())) {
                targetCell = nextCell;
                nextCell = map.getCellInFront(nextCell, direction);
                nextSpeed++;
            }
            guard.setNextSpeed(nextSpeed);
            guard.setNextDirection(direction);
            guard.setTargetCell(targetCell);
            return false;
        }
        // check if left or right is still unexplored, if yes turn to one of them (random),
        // if not proceed with next part
        List<Cell> candidateCells = new ArrayList();
        if (cellLeft.getStatus() == 0) candidateCells.add(cellLeft);
        if (cellRight.getStatus() == 0) candidateCells.add(cellRight);
        if (candidateCells.size() > 0) {
            Cell choice;
            if (candidateCells.size() == 1) {choice = candidateCells.get(0);}
            else {
                choice = candidateCells.get((int) Math.round( Math.random()));
            }
            // turn and then check again how far you can go in that direction
            guard.setNextSpeed(0);
            guard.setNextDirection(map.getDirection(currentCell, choice));
            guard.setTargetCell(currentCell);
            return false;
        }


        // if there are no unexplored neighbours
        currentCell.setStatus(2);
        targetCell = currentCell.getParentCell();
        if (targetCell == null) {
            return true;
        }
        if (map.isInDirection(currentCell, targetCell, direction)) {
            // move towards target
            guard.setNextDirection(direction);
            guard.setNextSpeed(map.getDistance(currentCell, targetCell));
            guard.setTargetCell(targetCell);
            return false;
        } else {
            // turn towards target
            guard.setNextDirection(map.getDirection(currentCell, targetCell));
            guard.setNextSpeed(0);
            guard.setTargetCell(targetCell);
            return false;
        }

    }

    public boolean inWall(int x, int y){
        return scenario.inWall(x+0.5, y+0.5);
    }
}