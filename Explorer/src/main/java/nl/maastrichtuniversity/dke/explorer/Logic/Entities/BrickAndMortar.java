package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Cell.Node;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Cell.NodeDFS;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Map;
import java.util.Collections;

import java.util.ArrayList;

public class BrickAndMortar extends Guard {

    Map map;
    int[] decision = new int[2]; // 1-movement  2-rotation
    ArrayList<NodeDFS> accessible = new ArrayList<NodeDFS>();
    private int desiredX;
    private int desiredY;
    private double desiredAngle;
    private NodeDFS targetNode;
    private int type;
    private boolean moved = false;

    public BrickAndMortar(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel, int type) {
        super(id, x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        map = new Map(gamePanel.scenario.getMapWidth(), gamePanel.scenario.getMapHeight());
        desiredX= (int) x;
        desiredY= (int) y;
        desiredAngle = viewAngle;
        this.type = type;
        this.Marking();
        this.Navigation();
    }

    public void update(boolean isGuard) {
        this.BAM();
        this.setAction(decision[0], decision[1]);
        super.update(isGuard);
    }

    public void BAM(){
        if (((int) this.movement.getX() == this.desiredX && (int) this.movement.getY() == this.desiredY && this.vision.getViewAngle() == desiredAngle)
                || ((int) this.movement.getX() > (this.desiredX + 1) || (int) this.movement.getX() < (this.desiredX - 1)
                || (int) this.movement.getY() > (desiredY + 1) || (int) this.movement.getY() < (this.desiredY - 1))) {
            this.Marking();
            this.Navigation();
        }
        else{
            this.move();
        }
    }

    public void Marking(){
        NodeDFS currentNode = map.getNode(this.movement.getX(), this.movement.getY());

        //check to see if neighbor nodes are walls
        NodeDFS nodeFront = map.getNodeInFront(currentNode, vision.getViewAngle());
        NodeDFS nodeLeft = map.getLeftNode(currentNode, vision.getViewAngle());
        NodeDFS nodeRight = map.getRightNode(currentNode, vision.getViewAngle());

        accessible = new ArrayList<NodeDFS>();

        if (gamePanel.tileM.mapTile[nodeFront.getX()][nodeFront.getY()] == 1) {
            nodeFront.setStatus(3);
        }
        if (gamePanel.tileM.mapTile[nodeLeft.getX()][nodeLeft.getY()] == 1) {
            nodeLeft.setStatus(3);
        }
        if (gamePanel.tileM.mapTile[nodeRight.getX()][nodeRight.getY()] == 1) {
            nodeRight.setStatus(3);
        }
        if (gamePanel.tileM.mapTile[getNode2().getX()][getNode2().getY()] == 1) {
            getNode2().setStatus(3);
        }
        if (gamePanel.tileM.mapTile[getNode4().getX()][getNode4().getY()] == 1) {
            getNode4().setStatus(3);
        }

        NodeDFS[] nodeFrontier = new NodeDFS[3];
        nodeFrontier[0] = nodeFront;
        nodeFrontier[1] = nodeLeft;
        nodeFrontier[2] = nodeRight;

        for (int i = 0; i < nodeFrontier.length; i++){
            if (nodeFrontier[i].getStatus()!=3 && nodeFrontier[i].getStatus()!=2){
                accessible.add(nodeFrontier[i]);
            }
        }

        if (accessible.size() == 1){
            currentNode.setStatus(2);
        } else if (accessible.size() == 2) {
            if (accessible.get(0).equals(nodeFront) && accessible.get(1).equals(nodeLeft)){
                if (getNode2().getStatus()!=3 && getNode2().getStatus()!=2){
                    currentNode.setStatus(2);
                } else {
                    currentNode.setStatus(1);
                }
            } else if (accessible.get(0).equals(nodeFront) && accessible.get(1).equals(nodeRight)) {
                if (getNode4().getStatus()!=3 && getNode4().getStatus()!=2){
                    currentNode.setStatus(2);
                } else {
                    currentNode.setStatus(1);
                }
            } else if (accessible.get(0).equals(nodeLeft) && accessible.get(1).equals(nodeRight)){
                currentNode.setStatus(1);
            }
        } else if (accessible.size() == 3) {
            if (getNode2().getStatus()!=3 && getNode2().getStatus()!=2 && getNode4().getStatus()!=3 && getNode4().getStatus()!=2){
                currentNode.setStatus(2);
            } else {
                currentNode.setStatus(1);
            }
        } else if (accessible.size() == 0) {
            decision[0] = 0;
            decision[1] = 1;
        }
    }

    public void Navigation(){
        ArrayList<Integer> wallCount = new ArrayList<Integer>();
        for (int i = 0; i < accessible.size(); i++){
            wallCount.add(0);
            if (accessible.get(i).getStatus() == 0){
                NodeDFS node1 = map.getNode(accessible.get(i).getX()+1,accessible.get(i).getY());
                if (gamePanel.tileM.mapTile[node1.getX()][node1.getY()] == 1) {
                    node1.setStatus(3);
                }
                NodeDFS node2 = map.getNode(accessible.get(i).getX(),accessible.get(i).getY()+1);
                if (gamePanel.tileM.mapTile[node2.getX()][node2.getY()] == 1) {
                    node2.setStatus(3);
                }
                NodeDFS node3 = map.getNode(accessible.get(i).getX()-1,accessible.get(i).getY());
                if (gamePanel.tileM.mapTile[node3.getX()][node3.getY()] == 1) {
                    node3.setStatus(3);
                }
                NodeDFS node4 = map.getNode(accessible.get(i).getX(),accessible.get(i).getY()-1);
                if (gamePanel.tileM.mapTile[node4.getX()][node4.getY()] == 1) {
                    node4.setStatus(3);
                }

                NodeDFS[] nodeFrontier2 = {node1, node2, node3, node4};
                for (int j = 0; j < nodeFrontier2.length; j++){
                    if (nodeFrontier2[j].getStatus() == 3 || nodeFrontier2[j].getStatus() == 2){
                        wallCount.set(i, wallCount.get(i) + 1);
                    }
                }
            }
        }
        int max = Collections.max(wallCount);
        int index = wallCount.indexOf(max);
        targetNode = accessible.get(index);
        NodeDFS currentNode = map.getNode(this.movement.getX(), this.movement.getY());
        desiredX = targetNode.getX();
        desiredY = targetNode.getY();
        desiredAngle = map.getDirection(currentNode, targetNode);
    }

    public void move(){
        NodeDFS currentNode = map.getNode(movement.getX(), movement.getY());
        if(map.isInDirection(currentNode, targetNode, vision.getViewAngle())) {
            // move towards target
            decision[0] = 1;
            decision[1] = 0;
            moved = true;
        } else {
            // turn towards target
            decision[0] = 0;
            decision[1] = 1;
            //desiredAngle = map.getDirection(currentNode, targetNode);
            moved = false;
        }
    }

    public NodeDFS getNode2(){
        // facing right
        if (vision.getViewAngle() == 0){
            return map.getNode(movement.getX()+1,movement.getY()-1);
        }
        // facing downwards
        else if (vision.getViewAngle() == 90) {
            return map.getNode(movement.getX()+1,movement.getY()+1);
        }
        // facing left
        else if (vision.getViewAngle() == 180) {
            return map.getNode(movement.getX()-1,movement.getY()+1);
        }
        // facing upwards
        else if (vision.getViewAngle() == 270) {
            return map.getNode(movement.getX()-1,movement.getY()-1);
        }
        return null;
    }

    public NodeDFS getNode4(){
        // facing right
        if (vision.getViewAngle() == 0){
            return map.getNode(movement.getX()+1,movement.getY()+1);
        }
        // facing downwards
        else if (vision.getViewAngle() == 90) {
            return map.getNode(movement.getX()-1,movement.getY()+1);
        }
        // facing left
        else if (vision.getViewAngle() == 180) {
            return map.getNode(movement.getX()-1,movement.getY()-1);
        }
        // facing upwards
        else if (vision.getViewAngle() == 270) {
            return map.getNode(movement.getX()+1,movement.getY()-1);
        }
        return null;
    }

}
