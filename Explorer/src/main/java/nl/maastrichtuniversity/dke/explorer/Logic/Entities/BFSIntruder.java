package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Cell.Node;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Cell.NodeDFS;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Map;

import java.util.*;
import java.util.List;
// based on : https://stackoverflow.com/questions/41789767/finding-the-shortest-path-nodes-with-breadth-first-search


public class BFSIntruder extends Intruder {

    private List<Node> pathToTarget;
    private int count = 1;

    int[] decision = new int[2]; // 1- movement  2- rotation

    Map map;

    public BFSIntruder(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(id, x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        map = new Map(gamePanel.scenario.getMapWidth(), gamePanel.scenario.getMapHeight());
        OneSearchbfs();
    }

    public void update(boolean isGuard) {
        walk();
        setAction(decision[0],decision[1]);
        super.update(isGuard);
    }

    private void walk() {
        //if next cell is infront and facing in the right dir -> walk
        //if next cell is down then rotate
        boolean moved = false;
        // if the next cell is to the right of current intruder cell
        if ((int) movement.getX() < pathToTarget.get(count).getX()) {
            // If intruder is facing to the right
            if (vision.getViewAngle() == 0) {
                decision[0] = 1;
                decision[1] = 0;
                moved = true;
            }
            // If intruder is facing downwards
            else if (vision.getViewAngle() == 90) {
                decision[0] = 0;
                decision[1] = 2;
            }
            // If intruder is facing to the left
            else if (vision.getViewAngle() == 180) {
                decision[0] = 0;
                decision[1] = 1;
            }
            // If intruder is facing upwards
            else if (vision.getViewAngle() == 270) {
                decision[0] = 0;
                decision[1] = 1;
            }
        }
        // if the next cell is to the left of current intruder cell
        else if ((int) movement.getX() > pathToTarget.get(count).getX()) {
            // If intruder is facing to the right
            if (vision.getViewAngle() == 0) {
                decision[0] = 0;
                decision[1] = 1;
            }
            // If intruder is facing downwards
            else if (vision.getViewAngle() == 90) {
                decision[0] = 0;
                decision[1] = 1;
            }
            // If intruder is facing to the left
            else if (vision.getViewAngle() == 180) {
                decision[0] = 1;
                decision[1] = 0;
                moved = true;
            }
            // If intruder is facing upwards
            else if (vision.getViewAngle() == 270) {
                decision[0] = 0;
                decision[1] = 1;
            }
        }
        // if the next cell is below the current intruder cell
        else if ((int) movement.getY() < pathToTarget.get(count).getY()) {
            // If intruder is facing to the right
            if (vision.getViewAngle() == 0) {
                decision[0] = 0;
                decision[1] = 1;
            }
            // If intruder is facing downwards
            else if (vision.getViewAngle() == 90) {
                decision[0] = 1;
                decision[1] = 0;
                moved = true;
            }
            // If intruder is facing to the left
            else if (vision.getViewAngle() == 180) {
                decision[0] = 0;
                decision[1] = 1;
            }
            // If intruder is facing upwards
            else if (vision.getViewAngle() == 270) {
                decision[0] = 0;
                decision[1] = 2;
            }
        }
        // if the next cell is above the current intruder cell
        else if ((int) movement.getY() > pathToTarget.get(count).getY()) {
            // If intruder is facing to the right
            if (vision.getViewAngle() == 0) {
                decision[0] = 0;
                decision[1] = 1;
            }
            // If intruder is facing downwards
            else if (vision.getViewAngle() == 90) {
                decision[0] = 0;
                decision[1] = 1;
            }
            // If intruder is facing to the left
            else if (vision.getViewAngle() == 180) {
                decision[0] = 0;
                decision[1] = 1;
            }
            // If intruder is facing upwards
            else if (vision.getViewAngle() == 270) {
                decision[0] = 1;
                decision[1] = 0;
                moved = true;
            }
        }
        // if it the intruder is on the next cell don't move
        else if ((int) movement.getX() == pathToTarget.get(count).getX() && (int) movement.getY() == pathToTarget.get(count).getY()){
            decision[0] = 0;
            decision[1] = 0;
            moved = true;
        }

        System.out.println("view angle: " + vision.getViewAngle());
        System.out.println("current x: " + (int) movement.getX() + " current y: " + (int) movement.getY());
        System.out.println("target x: " + pathToTarget.get(count).getX() + " target y: " + pathToTarget.get(count).getY());
        System.out.println("walk: " + decision[0] + " rotate: " + decision[1]);

        if (count != pathToTarget.size() - 1 && moved){
            count++;
        }

        // intruder stops moving when it reaches target
        if ((int) movement.getX() == gamePanel.scenario.getTargetArea().getLeftBoundary() && (int) movement.getY() == gamePanel.scenario.getTargetArea().getBottomBoundary()){
            System.out.println("done");
            decision[0] = 0;
            decision[1] = 0;
        }

    }


    public void OneSearchbfs() {

        Node currentNode = map.getNode(movement.getX(), movement.getY());
        Queue<List<Node>> queue = new LinkedList<>();
        //Create a queue of path used to reach the cell instead of only the node itself
        Set<Node> visitedList = new HashSet<>();
        //A Set to store visited cell

        List<Node> pathToCell = new ArrayList<>();
        pathToCell.add(currentNode);

        queue.add(pathToCell);
        while (!queue.isEmpty()) {

            //remove top element in the queue
            pathToCell = queue.poll();

            //get the next node
            currentNode = pathToCell.get(pathToCell.size()-1);

            if(isTargetReached(currentNode)) {
                //print path
                for (Node a: pathToCell){
                    System.out.println("x coord: " + a.getX() + ", " + "y coord: " +a.getY());
                }
                System.out.println("Takes " + pathToCell.size() +"steps");
                pathToTarget = pathToCell;
                break;

            }

            else {

                //loop over neighbors
                for (Node neighbor : getNeighbors(currentNode)) {

                    if (!isVisited(visitedList, neighbor)) {
                        //create a new path to nextNode
                        List<Node> pathToNextNode = new ArrayList<>(pathToCell);
                        pathToNextNode.add(neighbor);
                        queue.add(pathToNextNode); //then add collection to the queue
                    }
                }
            }
        }

    }

    public boolean isTargetReached(Node currentNode) {
        return gamePanel.tileM.mapTile[currentNode.getX()][currentNode.getY()] == 4;
    }

    private List<Node> getNeighbors(Node currentCell) {
        NodeDFS cellFront = map.getNodeInFront(currentCell, vision.getViewAngle());
        if (gamePanel.tileM.mapTile[cellFront.getX()][cellFront.getY()] == 1) {
            cellFront.setStatus(3);
        }

        NodeDFS cellLeft = map.getLeftNode(currentCell, vision.getViewAngle());
        if (gamePanel.tileM.mapTile[cellLeft.getX()][cellLeft.getY()] == 1) {
            cellLeft.setStatus(3);
        }

        NodeDFS cellRight = map.getRightNode(currentCell, vision.getViewAngle());
        if (gamePanel.tileM.mapTile[cellRight.getX()][cellRight.getY()] == 1) {
            cellRight.setStatus(3);
        }
        List<Node> neighbourCell = new ArrayList<>();
        if (cellFront.getStatus()!=3) neighbourCell.add((Node) cellFront);
        if (cellRight.getStatus()!=3) neighbourCell.add((Node)cellRight);
        if (cellLeft.getStatus()!=3) neighbourCell.add((Node)cellLeft);
        return neighbourCell;

    }

    private boolean isVisited(Set<Node> visitedList,Node node) {

        if(visitedList.contains(node))
        { return true;}

        visitedList.add(node);
        return false;
    }

}
