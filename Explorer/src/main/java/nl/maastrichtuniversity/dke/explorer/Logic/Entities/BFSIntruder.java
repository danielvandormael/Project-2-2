package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Cell;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Map;

import java.util.*;
import java.util.List;
// based on : https://stackoverflow.com/questions/41789767/finding-the-shortest-path-nodes-with-breadth-first-search


public class BFSIntruder extends Intruder {

    private List<Cell> pathToTarget;
    private int count = 1;

    int[] decision = new int[2]; // 1- movement  2- rotation

    Map map;

    public BFSIntruder(double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        map = new Map(gamePanel.scenario.getMapWidth(), gamePanel.scenario.getMapHeight());
        OneSearchbfs();
    }

    public void update() {
//        walk();
//        setAction(decision[0],decision[1]);
//        super.update();
    }

    private void walk() {
        //if next cell is infront and facing in the right dir -> walk
        //if next cell is down then rotate

        // if the next cell is to the right of current intruder cell
        if (getX() < pathToTarget.get(count).getX()) {
            // If intruder is facing to the right
            if (getViewAngle() == 0) {
                System.out.println("facing right");
                decision[0] = 1;
                decision[1] = 0;
            }
            // If intruder is facing downwards
            else if (getViewAngle() == 90) {
                System.out.println("face down");
                decision[0] = 0;
                decision[1] = 1;
            }
            // If intruder is facing to the left
            else if (getViewAngle() == 180) {
                System.out.println("face left");
                decision[0] = 0;
                decision[1] = 1;
            }
            // If intruder is facing upwards
            else if (getViewAngle() == 270) {
                System.out.println("face up");
                decision[0] = 0;
                decision[1] = 2;
            }
        }
        // if the next cell is below the current intruder cell
        else if (getY() < pathToTarget.get(count).getY()) {
            // If intruder is facing to the right
            System.out.println(getViewAngle());
            if (getViewAngle() == 0) {
                decision[0] = 0;
                decision[1] = 1;
            }
            // If intruder is facing downwards
            else if (getViewAngle() == 90) {
                decision[0] = 1;
                decision[1] = 0;
            }
            // If intruder is facing to the left
            else if (getViewAngle() == 180) {
                decision[0] = 0;
                decision[1] = 1;
            }
            // If intruder is facing upwards
            else if (getViewAngle() == 270) {
                decision[0] = 0;
                decision[1] = 2;
            }
        }
        if (count != pathToTarget.size() - 1){
            count++;
        }
    }


    public void OneSearchbfs() {

        Cell currenctCell = map.getCell(getX(),getY()) ;
        Queue<List<Cell>> queue = new LinkedList<>();
        //Create a queue of path used to reach the cell instead of only the node itself
        Set<Cell> visitedList = new HashSet<>();
        //A Set to store visited cell

        List<Cell> pathToCell = new ArrayList<>();
        pathToCell.add(currenctCell);

        queue.add(pathToCell);
        while (!queue.isEmpty()) {

            //remove top element in the queue
            pathToCell = queue.poll();

            //get the next node
            currenctCell = pathToCell.get(pathToCell.size()-1);

            if(isTargetReached(currenctCell)) {
                //print path
                for (Cell a: pathToCell){
                    System.out.println("x coord: " + a.getX() + ", " + "y coord: " +a.getY());
                }
                System.out.println("Takes " + pathToCell.size() +"steps");
                pathToTarget = pathToCell;
                break;

            }

            else {

                //loop over neighbors
                for (Cell neighbor : getNeighbors(currenctCell)) {

                    if (!isVisited(visitedList, neighbor)) {
                        //create a new path to nextNode
                        List<Cell> pathToNextNode = new ArrayList<>(pathToCell);
                        pathToNextNode.add(neighbor);
                        queue.add(pathToNextNode); //then add collection to the queue
                    }
                }
            }
        }

    }

    public boolean isTargetReached(Cell currentCell) {
        return gamePanel.tileM.mapTile[currentCell.getX()][currentCell.getY()] == 4;
    }

    private List<Cell> getNeighbors(Cell currentCell) {
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
        List<Cell> neighbourCell = new ArrayList<>();
        if (cellFront.getStatus()!=3) neighbourCell.add(cellFront);
        if (cellRight.getStatus()!=3) neighbourCell.add(cellRight);
        if (cellLeft.getStatus()!=3) neighbourCell.add(cellLeft);
        return neighbourCell;

    }

    private boolean isVisited(Set<Cell> visitedList,Cell cell) {

        if(visitedList.contains(cell))
            { return true;}

        visitedList.add(cell);
        return false;
    }








}
