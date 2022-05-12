package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Cell;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Map;

import java.util.*;
import java.util.List;



public class BFSIntruder extends Intruder {

    private double desiredAngle;
    private int desiredX;
    private int desiredY;
    Cell targetCell;


    ArrayList<Cell> viewingArea = new ArrayList<>();

    int[] decision = new int[2]; // 1- movement  2- rotation

    Map map;

    public BFSIntruder(double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        map = new Map(gamePanel.scenario.getMapWidth(), gamePanel.scenario.getMapHeight());
        desiredX= (int) x;
        desiredY= (int) y;
        desiredAngle = viewAngle;
    }

    public void update() {
        bfs();
//        setAction(decision[0], decision[1]);
//        super.update();
    }

    public void bfs() {

        Cell currentCell = map.getCell(getX(), getY());

        Queue<List<Cell>> queue = new LinkedList<>();
        //Create a queue of path used to reach the cell instead of only the node itself
        Set<Cell> visitedList = new HashSet<>();
        //A Set to store visited cell

        List<Cell> pathToCell = new ArrayList<>();
        pathToCell.add(currentCell);

        queue.add(pathToCell);
        while (!queue.isEmpty()) {

            //remove top element in the queue
            pathToCell = queue.poll();

            //get the next node
            currentCell = pathToCell.get(pathToCell.size()-1);

            if(isTargetReached(currentCell)) {
                //print path
                System.out.println(pathToCell);
                for (Cell a: pathToCell){
                    System.out.println("x coord: " + a.getX() + ", " + "y coord: " +a.getY());
                }
                System.out.println("Takes " + pathToCell.size() +"steps");
                gamePanel.endGameThread();
            }

            //loop over neighbors
            for(Cell neighbor : getNeighbors(currentCell)){

                if(!isVisited(visitedList,neighbor)) {
                    //create a new path to nextNode
                    List<Cell> pathToNextNode = new ArrayList<>(pathToCell);
                    pathToNextNode.add(neighbor);
                    queue.add(pathToNextNode); //then add collection to the queue
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
        if (cellFront.getStatus()==0) neighbourCell.add(cellFront);
        if (cellRight.getStatus()==0) neighbourCell.add(cellRight);
        if (cellLeft.getStatus()==0) neighbourCell.add(cellLeft);
        return neighbourCell;

    }

    private boolean isVisited(Set<Cell> visitedList,Cell cell) {

        if(visitedList.contains(cell))
            { return true;}

        visitedList.add(cell);
        return false;
    }








}
