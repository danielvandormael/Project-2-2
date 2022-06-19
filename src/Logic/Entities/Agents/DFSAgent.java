package Logic.Entities.Agents;

import GUI.GamePanel;
import Logic.Entities.Guard;
import Logic.Tiles.Map;
import Logic.Tiles.Cell.Node;
import Logic.Tiles.Cell.NodeDFS;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DFSAgent extends Guard {
    private static final int OPEN = 0;
    private static final int TRIED = 1;
    private static final int WALL = 2;

    private int orientation; // 1. LEFT 2. DOWN 3.RIGHT 4.UP


    Node previousNode;
    Node targetNode;

    boolean biggerX;
    boolean biggerY;


    int [] decision = new int[2]; // 1- movement  2- rotation

    Map map;

    int type; // 0 favors turning left, 1 favors turning right

    public DFSAgent(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel, int type) {
        super(id, x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        map = new Map(gamePanel.scenario.getMapWidth(), gamePanel.scenario.getMapHeight());
        this.type = type;
    }

    public void update(boolean isGuard) {
        DFS();
        setAction(decision[0], decision[1]);
        soundSense.setShoutSound(true);
        super.update(isGuard);
    }

    public void DFS() {
        if ((int) this.movement.getX() == desiredX && (int) this.movement.getY() == desiredY && this.vision.getViewAngle() == desiredAngle) {
            nextStep();
        }

        if ((int) this.movement.getX() == desiredX && (int) this.movement.getY() == desiredY && this.vision.getViewAngle() == desiredAngle) {
            nextStep();
        }
    }

    public boolean nextStep() {
        NodeDFS currentNode = map.getNode(this.movement.getX(), this.movement.getY());
        targetNode = map.getNode(desiredX, desiredY);

        //this.setDeadEnd(false);
        //useless
        if (targetNode != null) {
            if (!currentNode.equals(targetNode)) {
                if (map.isInDirection(currentNode, targetNode, this.vision.getViewAngle())) { //only need to change the desired position

                    desiredX = targetNode.getX();
                    desiredY = targetNode.getY();
                    return false;
                }
            }
        }

        //labeling parent Node
        if(currentNode.getStatus() == 0){
            currentNode.setStatus(1);
            if(currentNode.getParent() == null){
                NodeDFS fromNode = currentNode;
                Node previousNode;
                if(this.vision.getViewAngle() < 180){
                    previousNode = map.getNodeInFront(fromNode, this.vision.getViewAngle()+180);
                }else if(this.vision.getViewAngle() == 180){
                    previousNode = map.getNodeInFront(fromNode, 0);
                }else{
                    previousNode = map.getNodeInFront(fromNode, this.vision.getViewAngle()-180);
                }
                fromNode.setParent(previousNode);
                fromNode.setStatus(1);
            }
        }


        //check to see if neighbor Nodes are walls
        NodeDFS NodeFront = map.getNodeInFront(currentNode, this.vision.getViewAngle());
        if (gamePanel.tileM.mapTile[NodeFront.getX()][NodeFront.getY()] == 1) {
            NodeFront.setStatus(3);
        }

        NodeDFS NodeLeft = map.getLeftNode(currentNode, this.vision.getViewAngle());
        if (gamePanel.tileM.mapTile[NodeLeft.getX()][NodeLeft.getY()] == 1) {
            NodeLeft.setStatus(3);
        }

        NodeDFS NodeRight = map.getRightNode(currentNode, this.vision.getViewAngle());
        if (gamePanel.tileM.mapTile[NodeRight.getX()][NodeRight.getY()] == 1) {
            NodeRight.setStatus(3);
        }

        // Check if there are intruders in the view
        ArrayList<Node> intruders = this.vision.intrudersInView();
        Node closest = null;
        if(intruders.size() != 0){
            boolean move = false;
            // If there is more than 1 intruder in view, go to the closest one
            if (intruders.size() > 1){
                closest = getClosestIntruder(intruders);
            }
            else closest = intruders.get(0);
            // If intruder is in front of the guard, sprint forwards
            if (map.isInDirection(currentNode, targetNode, this.vision.getViewAngle())) {
                targetNode = closest;
                move = true;

                // If intruder is not right in front, targetNode is a Node that has the same x or y coordinate as intruder
            } else if (map.isInDirection(currentNode, map.getNode(this.movement.getX(), closest.getY()), this.vision.getViewAngle())){
                targetNode = map.getNode(this.movement.getX(), closest.getY());
                move = true;
            }
            else if (map.isInDirection(currentNode, map.getNode(closest.getX(), this.movement.getY()), this.vision.getViewAngle())){
                targetNode = map.getNode(closest.getX(), this.movement.getY());
                move = true;
            }

            if (move) {
                // move towards target
                decision[0] = 2;
                decision[1] = 0;
                desiredX= targetNode.getX();
                desiredY= targetNode.getY();
                return false;
            }
            else {
                double distanceX = Math.abs(this.movement.getX() - closest.getX());
                double distanceY = Math.abs(this.movement.getY() - closest.getY());
                if (distanceX > distanceY){
                    targetNode = map.getNode(closest.getX(), this.movement.getY());
                }
                else {
                    targetNode = map.getNode(this.movement.getX(), closest.getY());
                }

                // turn towards target
                decision[0] = 0;
                decision[1] = 1;
                desiredX= (int) this.movement.getX();
                desiredY= (int) this.movement.getY();
                desiredAngle = map.getDirection(currentNode, targetNode);
                return false;
            }
        }


        // Will wait and turn randomly if it sees another guard searching nearby or a marker
        if(this.vision.guardsInView().size() >= 1 || marker.foundMarker){
            double random = Math.random();
            if (random < 0.33) {
                List<Node> candidateNodes = new ArrayList();
                if (NodeLeft.getStatus() == 0) candidateNodes.add(NodeLeft);
                if (NodeRight.getStatus() == 0) candidateNodes.add(NodeRight);
                if (candidateNodes.size() > 1){
                    Node choice = candidateNodes.get(type);
                    // turn and then check again how far you can go in that direction
                    decision[0] = 0;
                    decision[1] = 1;
                    desiredAngle = map.getDirection(currentNode, choice);
                    desiredX= (int) this.movement.getX();
                    desiredY= (int) this.movement.getY();
                    return false;
                }
                if (candidateNodes.size() > 0) {
                    Node choice = candidateNodes.get(0);
                    // turn and then check again how far you can go in that direction
                    decision[0] = 0;
                    decision[1] = 1;
                    desiredAngle = map.getDirection(currentNode, choice);
                    desiredX = (int) this.movement.getX();
                    desiredY = (int) this.movement.getY();
                    return false;
                }
            }

        }

        if (NodeFront.getStatus() == 0) {
            // we can continue in same direction
            targetNode = NodeFront;
            desiredX = targetNode.getX();
            desiredY = targetNode.getY();
            decision[0] = 1;
            decision[1] = 0;
            return false;
        }


        // check if left or right is still unexplored, if yes turn to one of them (favor left),
        // if not proceed with next part
        List<Node> candidateNodes = new ArrayList();
        if (NodeLeft.getStatus() == 0) candidateNodes.add(NodeLeft);
        if (NodeRight.getStatus() == 0) candidateNodes.add(NodeRight);
        if (candidateNodes.size() > 1){
            Node choice = candidateNodes.get(type);
            // turn and then check again how far you can go in that direction
            decision[0] = 0;
            decision[1] = 1;
            desiredAngle = map.getDirection(currentNode, choice);
            desiredX= (int) this.movement.getX();
            desiredY= (int) this.movement.getY();
            return false;
        }
        if (candidateNodes.size() > 0) {
            Node choice = candidateNodes.get(0);
            // turn and then check again how far you can go in that direction
            decision[0] = 0;
            decision[1] = 1;
            desiredAngle = map.getDirection(currentNode, choice);
            desiredX= (int) this.movement.getX();
            desiredY= (int) this.movement.getY();
            return false;
        }

        // if there are no unexplored neighbours

        // Technically, works as a dead-end (at least for the intruder, whose goal is to explore everything)
        //this.setDeadEnd(true);

        currentNode.setStatus(2);
        targetNode = currentNode.getParent();

        if(map.isInDirection(currentNode, targetNode, this.vision.getViewAngle())) {
            // move towards target
            decision[0] = 1;
            decision[1] = 0;
            desiredX= targetNode.getX();
            desiredY= targetNode.getY();
            return false;
        } else {
            // turn towards target
            decision[0] = 0;
            decision[1] = 1;
            desiredX= (int) this.movement.getX();
            desiredY= (int) this.movement.getY();
            desiredAngle = map.getDirection(currentNode, targetNode);
            return false;
        }

    }

    public Map getMap() {
        return map;
    }

    public Node getClosestIntruder(ArrayList<Node> intruders){
        double distance = 100;
        Node closestNodeWithIntruder = null;
        for (int i = 0; i < intruders.size(); i++){
            double newDistance = Point2D.distance(this.movement.getX(), this.movement.getY(), intruders.get(i).getX(), intruders.get(i).getY());
            if(newDistance < distance){
                distance = newDistance;
                closestNodeWithIntruder = intruders.get(i);
            }
        }
        return closestNodeWithIntruder;
    }
}
