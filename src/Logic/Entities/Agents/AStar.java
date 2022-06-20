package Logic.Entities.Agents;

import GUI.GamePanel;
import Logic.Entities.Intruder;
import Logic.Tiles.Cell.Node;
import Logic.Tiles.Cell.NodeAStar;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class AStar extends Intruder {
    private NodeAStar[][] node;
    private ArrayList<NodeAStar> openNode = new ArrayList<>();
    private ArrayList<NodeAStar> closedNode = new ArrayList<>();
    private ArrayList<NodeAStar> solidNode = new ArrayList<>();
    Node startNode, goalNode, currentNode, headingToNode;

    public int coordX;
    public int coordY;


    int [] decisions = new int[2]; // 1- movement  2- rotation


    public AStar(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(id, x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        instantiateNodes();
        desiredX= (int) x;
        desiredY= (int) y;
        desiredAngle = viewAngle;


        //set start node && close node
        startNode = node[(int) x][(int) y];
        goalNode = node[(int) x][(int) y];
    }

    public void instantiateNodes(){
        node = new NodeAStar[gamePanel.scenario.getMapWidth()][gamePanel.scenario.getMapHeight()];

        int row = 0;
        int col = 0;

        while(row < gamePanel.scenario.getMapWidth() && col < gamePanel.scenario.getMapHeight()){
            node[row][col] = new NodeAStar(row, col);

            row++;
            if(row  ==  gamePanel.scenario.getMapWidth()){
                row = 0;
                col++;
            }
        }
    }

    public void update(boolean isGuard){
        controller();
        setAction(decisions[0], decisions[1]);
        super.update(isGuard);
    }

    public boolean controller() {
        search();
        if(goalInView()){
            nextStep();
            desiredX = headingToNode.getX();
            desiredY = headingToNode.getY();
            desiredAngle = vision.angleBetween(desiredX, desiredY);
        }

        //SET UP DESIRED
        coordX = (int) movement.getX();
        coordY = (int) movement.getY();

        System.out.println();
        System.out.println("Current coords: " + coordX + " , "+ coordY);
        System.out.println("Desired coords: " + desiredX + " , "+ desiredY);

        //when through teleport
        if(this.movement.isThroughTeleport()){
            desiredX = (int) this.movement.getX();
            desiredY = (int) this.movement.getY();
            desiredAngle = this.vision.getViewAngle();
        }else //ENTITY IN WALL
            if(gamePanel.tileM.mapTile[coordX][coordY] == 4){
                decisions = new int[]{0, 0};
            }
        if(desiredAngle != this.vision.getViewAngle()){
            decisions = new int[]{0, this.vision.turnWhichWay(desiredAngle)};
            return false;
        }else if(( coordX != desiredX || coordY != desiredY)
                && desiredAngle == this.vision.getViewAngle()){
            //if entity misses desireX and desiredY
            if((coordX == desiredX && coordY != desiredY)
                    || (coordX != desiredX && coordY == desiredY)){
                if(desiredAngle != vision.angleBetween(desiredX, desiredY)){
                    desiredAngle = vision.angleBetween(desiredX, desiredY);
                    decisions = new int[]{0, this.vision.turnWhichWay(desiredAngle)};
                }else{
                    decisions = new int[]{1, 0};
                }
            }else{
                decisions = new int[]{1, 0};
            }
            return false;
        }else{
            decisions = new int[]{0, 0};
            return false;
        }
    }

    public boolean nextStep() {
        Collections.sort(openNode);

        //no more open node to search
        if(openNode.size() < 1){
            return false;
        }

        boolean found = false;
        int counter = 0;
        while(!found && counter < openNode.size()){
            //find lowest cost of openNode && with a common parent node as current
            Node temp = getPathNode(openNode.get(counter));
            if(temp != null){
                headingToNode = temp;
                goalNode = openNode.get(counter);
                found = true;
            }
            counter++;
        }
        return false;
    }


    public void search(){
        //update current node
        currentNode = node[(int) movement.getX()][(int) movement.getY()];
        //check Nodes in view
        setNodesInView();
    }

    public boolean goalInView(){
        for (int i = 0; i < closedNode.size(); i++) {
            if(closedNode.get(i).getX() == goalNode.getX() && closedNode.get(i).getY() == goalNode.getY()){
                return true;
            }
        }
        for (int i = 0; i < solidNode.size(); i++) {
            if(solidNode.get(i).getX() == goalNode.getX() && solidNode.get(i).getY() == goalNode.getY()){
                return true;
            }
        }
        return false;
    }



    public void setNodesInView(){
        //current tile
        int row = currentNode.getX();
        int col = currentNode.getY();
        if(node[row][col].isClosed() == false
                && node[row][col].isOpen() == false
                && node[row][col].isSolid() == false){

            node[row][col].setClosed();
            closedNode.add(node[row][col]);
        }

        //tiles in vision
        ArrayList<Node> temp = vision.tilesInView();
        for (int i = 0; i < temp.size(); i++) {
            row = temp.get(i).getX();
            col = temp.get(i).getY();
            //if a node is solid
            if(gamePanel.tileM.mapTile[row][col] == 1){
                //remove from openList
                if(openNode.contains(node[row][col])){
                    openNode.remove(node[row][col]);
                }
                node[row][col].setSolid();
                solidNode.add(node[row][col]);
            }else
                //check if node has never been seen
                if(node[row][col].isClosed() == false
                        && node[row][col].isSolid() == false){
                    //check if node is part of OpenNode or not
                    if(node[row][col].isOpen() == false & node[row][col] != startNode){
                        node[row][col].setParent(currentNode);
                    }else
                        //check if node is part of Open List
                        if(openNode.contains(node[row][col])){
                            openNode.remove(node[row][col]);
                        }
                    node[row][col].setClosed();
                    closedNode.add(node[row][col]);
                }
        }

        //set Open nodes
        for (int i = 0; i < closedNode.size(); i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if(j != k && j+k != 0){
                        row = closedNode.get(i).getX() + j;
                        col = closedNode.get(i).getY() + k;
                        if(node[row][col].isClosed() == false
                                && node[row][col].isOpen() == false
                                && node[row][col].isSolid() == false){
                            getCost(node[row][col]);
                            node[row][col].setParent(node[closedNode.get(i).getX()][closedNode.get(i).getY()]);
                            node[row][col].setOpen();
                            openNode.add(node[row][col]);
                        }
                    }
                }
            }
        }
    }

    public void getCost(NodeAStar node){
        //G Cost
        int xDistance = Math.abs(node.getX() - startNode.getX());
        int yDistance = Math.abs(node.getY() - startNode.getY());
        node.setgCost(xDistance + yDistance);

        //H Cost
        // Ax + By + C == 0 if (x,y) are on vector
        // d = |Ax + By + C| / sqrt(A^2 + B^2)
        double A = (getYDirectTarget()/getXDirectTarget());
        int c = (int) (startNode.getY() - startNode.getX()*A);

        node.sethCost((int) (Math.abs( node.getY() - A*node.getX() - c)/Math.sqrt(Math.pow(A, 2) + 1)));

        //F Cost
        node. setfCost(node.getgCost() + node.gethCost());
    }


    public Node getPathNode(Node goal){
        Node current = currentNode;
        Node desired = goal;
        int counterCurrent = getPathLength(current);
        int counterDesired = getPathLength(desired);

        boolean toFar = false;

        for (int i = 0; i < counterCurrent; i++) {
            for (int j = 0; j < counterDesired; j++) {
                if(desired.getParent() != null){
                    if(current == desired.getParent()){
                        if(toFar){
                            return currentNode.getParent();
                        }else{
                            return desired;
                        }
                    }
                    desired = desired.getParent();
                }
            }
            toFar = true;
            current = current.getParent();
        }
        return null;
    }

    public int getPathLength(Node node){
        Node temp = node;
        int counter = 1;
        while (temp.getParent() != null){
            counter++;
            temp = temp.getParent();
        }
        return  counter;
    }

    public void draw(Graphics2D g){
        super.draw(g);

        if(gamePanel.ui.showDesired == true){
            //SOLID NODES
            for (int i = 0; i < solidNode.size(); i++) {
                g.setColor(new Color(0, 205, 15, 200));
                g.fillRect(solidNode.get(i).getX() * gamePanel.tileSize, solidNode.get(i).getY() * gamePanel.tileSize, gamePanel.tileSize, gamePanel.tileSize);
            }

            //OPEN NODES
            for (int i = 0; i < openNode.size(); i++) {
                g.setColor(new Color(205, 14, 142, 200));
                g.fillRect(openNode.get(i).getX() * gamePanel.tileSize, openNode.get(i).getY() * gamePanel.tileSize, gamePanel.tileSize, gamePanel.tileSize);
            }

            //CLOSED NODES
            for (int i = 0; i < closedNode.size(); i++) {
                g.setColor(new Color(47, 59, 34, 200));
                g.drawRect(closedNode.get(i).getX() * gamePanel.tileSize, closedNode.get(i).getY() * gamePanel.tileSize, gamePanel.tileSize, gamePanel.tileSize);
            }

            g.setColor(new Color(0, 0, 0, 255));
            g.fillRect(goalNode.getX() * gamePanel.tileSize, goalNode.getY() * gamePanel.tileSize, gamePanel.tileSize, gamePanel.tileSize);
            g.drawRect(headingToNode.getX() * gamePanel.tileSize, headingToNode.getY() * gamePanel.tileSize, gamePanel.tileSize, gamePanel.tileSize);
        }
    }
}
