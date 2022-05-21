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
        Cell currentCell = map.getCell(getX(), getY());
        bfs(currentCell);
//        setAction(decision[0], decision[1]);
//        super.update();
    }

    public void bfs(Cell startingCell) {


        Queue<List<Cell>> queue = new LinkedList<>();
        //Create a queue of path used to reach the cell instead of only the node itself
        Set<Cell> visitedList = new HashSet<>();
        //A Set to store visited cell

        List<Cell> pathToCell = new ArrayList<>();
        pathToCell.add(startingCell);

        queue.add(pathToCell);
        while (!queue.isEmpty()) {

            //remove top element in the queue
            pathToCell = queue.poll();

            //get the next node
            startingCell = pathToCell.get(pathToCell.size()-1);

            if(isTargetReached(startingCell)) {
                //print path
                System.out.println(pathToCell);
                for (Cell a: pathToCell){
                    System.out.println("x coord: " + a.getX() + ", " + "y coord: " +a.getY());
                }
                System.out.println("Takes " + pathToCell.size() +"steps");
                break;

            }

            if (startingCell.getX()== 20 && startingCell.getY()==14){

                //move to the nearestCell
                System.out.println("FInd the nearest cell");
                for (Cell a: pathToCell){
                    System.out.println("x coord: " + a.getX() + ", " + "y coord: " +a.getY());
                }
                System.out.println("Takes " + pathToCell.size() +"steps to reached nearest cell");

                Cell nextNeighbor = map.getCell(startingCell.getX(),startingCell.getY());

                bfs2(nextNeighbor);
                //bfs again

            }
            else {

                //loop over neighbors WITHIN VIEW RANGE
                for (Cell neighbor : getNeighbors(startingCell)) {

                    if (!isVisited(visitedList, neighbor)) {
                        //create a new path to nextNode
                        List<Cell> pathToNextNode = new ArrayList<>(pathToCell);
                        if (pathToNextNode.size() > getViewRange()) {
                            break;
                        }
                        pathToNextNode.add(neighbor);
                        queue.add(pathToNextNode); //then add collection to the queue
                    }
                }
            }
        }

    }

    private void bfs2(Cell startingCell) {
        Queue<List<Cell>> queue = new LinkedList<>();
        //Create a queue of path used to reach the cell instead of only the node itself
        Set<Cell> visitedList = new HashSet<>();
        //A Set to store visited cell

        List<Cell> pathToCell = new ArrayList<>();
        pathToCell.add(startingCell);

        queue.add(pathToCell);
        while (!queue.isEmpty()) {

            //remove top element in the queue
            pathToCell = queue.poll();

            //get the next node
            startingCell = pathToCell.get(pathToCell.size()-1);

            if(isTargetReached(startingCell)) {
                //print path
                System.out.println(pathToCell);
                for (Cell a: pathToCell){
                    System.out.println("x coord: " + a.getX() + ", " + "y coord: " +a.getY());
                }
                System.out.println("Takes " + pathToCell.size() +"steps");
                gamePanel.endGameThread();
            }

            if (startingCell.getX()== 20 && startingCell.getY()==27){

                //move to the nearestCell
                System.out.println("FInd the nearest cell");
                for (Cell a: pathToCell){
                    System.out.println("x coord: " + a.getX() + ", " + "y coord: " +a.getY());
                }
                System.out.println("Takes " + pathToCell.size() +"steps to reached nearest cell");

                Cell nextNeighbor = map.getCell(startingCell.getX(),startingCell.getY());

                bfs3(nextNeighbor);
                //bfs again

            }

            else {

                //loop over neighbors WITHIN VIEW RANGE
                for (Cell neighbor : getNeighbors(startingCell)) {

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

    private void bfs3(Cell startingCell) {
        Queue<List<Cell>> queue = new LinkedList<>();
        //Create a queue of path used to reach the cell instead of only the node itself
        Set<Cell> visitedList = new HashSet<>();
        //A Set to store visited cell

        List<Cell> pathToCell = new ArrayList<>();
        pathToCell.add(startingCell);

        queue.add(pathToCell);
        while (!queue.isEmpty()) {

            //remove top element in the queue
            pathToCell = queue.poll();

            //get the next node
            startingCell = pathToCell.get(pathToCell.size()-1);

            if(isTargetReached(startingCell)) {
                //print path
                System.out.println(pathToCell);
                for (Cell a: pathToCell){
                    System.out.println("x coord: " + a.getX() + ", " + "y coord: " +a.getY());
                }
                System.out.println("Takes " + pathToCell.size() +"steps");
                gamePanel.endGameThread();
            }



            else {

                //loop over neighbors WITHIN VIEW RANGE
                for (Cell neighbor : getNeighbors(startingCell)) {

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

    private void moveNextCell(Cell cell){
        // if the next cell is to the right of current intruder cell
        if (getX() < cell.getX()){
            // If intruder is facing to the right
            if (getViewAngle() == 0){
                setAction(1, 0);
                super.update();
            }
            // If intruder is facing downwards
            else if (getViewAngle() == 270){
                setAction(0, 1);
                super.update();
                setAction(1, 0);
                super.update();
            }
            // If intruder is facing to the left
            else if (getViewAngle() == 180){
                setAction(0, 1);
                super.update();
                setAction(0, 1);
                super.update();
                setAction(1, 0);
                super.update();
            }
            // If intruder is facing upwards
            else if (getViewAngle() == 90){
                setAction(0, 2);
                super.update();
                setAction(1, 0);
                super.update();
            }
        }
        // if the next cell is below the current intruder cell
        else if (getY() < cell.getY()){
            // If intruder is facing to the right
            if (getViewAngle() == 0){
                setAction(0, 2);
                super.update();
                setAction(1, 0);
                super.update();
            }
            // If intruder is facing downwards
            else if (getViewAngle() == 270){
                setAction(1, 0);
                super.update();
            }
            // If intruder is facing to the left
            else if (getViewAngle() == 180){
                setAction(0, 1);
                super.update();
                setAction(1, 0);
                super.update();
            }
            // If intruder is facing upwards
            else if (getViewAngle() == 90){
                setAction(0, 2);
                super.update();
                setAction(0, 2);
                super.update();
                setAction(1, 0);
                super.update();
            }
        }







}
