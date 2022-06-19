package Logic.Entities.Agents;

import GUI.GamePanel;
import Logic.Entities.Intruder;
import Logic.Tiles.Cell.Node;
import Logic.Tiles.Cell.NodeAStar;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class ASAgent extends Intruder implements Agent{
    private NodeAStar[][] node;
    private ArrayList<NodeAStar> openNode = new ArrayList<>();
    private ArrayList<NodeAStar> closedNode = new ArrayList<>();
    private ArrayList<NodeAStar> solidNode = new ArrayList<>();
    private ArrayList<NodeAStar> withoutParent = new ArrayList<>();
    Node startNode, goalNode, currentNode, headingToNode;

    public int coordX;
    public int coordY;


    int [] decisions = new int[2]; // 1- movement  2- rotation


    public ASAgent(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(id, x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        instantiateNodes();
        desiredX= (int) x;
        desiredY= (int) y;
        desiredAngle = viewAngle;


        //set start node && close node
        startNode = node[(int) x][(int) y];
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

    public void update(){
        controller();
        setAction(decisions[0], decisions[1]);
        super.update();
    }

    @Override
    public boolean controller() {
        search();

        //SET UP DESIRED
        desiredX = headingToNode.getX();
        desiredY = headingToNode.getY();
        desiredAngle = vision.angleBetween(desiredX, desiredY);

        //AVOID COLLISIONS WITH WALL
        if((int) movement.getX() > desiredX){
            greaterThanDesiredX = true;
        }else if((int) movement.getX() < desiredX){
            greaterThanDesiredX = false;
        }
        if((int) movement.getY() > desiredY){
            greaterThanDesiredY = true;
        }else if((int) movement.getY() < desiredX){
            greaterThanDesiredY = false;
        }

        //CURRENT COORDS WITH COLLISION HANDLE
        if(greaterThanDesiredX){
            coordX = (int) (movement.getX() + 0.5);
        }else{
            coordX = (int) (movement.getX() - 0.5);
        }
        if(greaterThanDesiredY){
            coordY = (int) (movement.getY() + 0.5);
        }else{
            coordY = (int) (movement.getY() - 0.5);
        }
        coordX = (int) movement.getX();
        coordY = (int) movement.getX();

        //when through teleport
        if(this.movement.isThroughTeleport()){
            desiredX = (int) this.movement.getX();
            desiredY = (int) this.movement.getY();
            desiredAngle = this.vision.getViewAngle();
        }else //ENTITY IN WALL
            if(gamePanel.tileM.mapTile[coordX][coordY] == 4){
            decisions = new int[]{0, 0};
        }else //SEARCH NEW DESIRED
            if(coordX == desiredX && (int) coordY == desiredY && desiredAngle == this.vision.getViewAngle()){
                //TARGET IN VIEW
            ArrayList<Node> inView = this.vision.tilesInView();
            for (int i = 0; i < inView.size(); i++) {
                if(gamePanel.tileM.mapTile[inView.get(i).getX()][inView.get(i).getY()] == 4){
                    desiredX = inView.get(i).getX();
                    desiredY = inView.get(i).getY();
                    desiredAngle= vision.angleBetween(desiredX, desiredY);
                    return true;
                }
            }
            return true;
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
                    //check if there is a wall on the way
                    if(!wallInWay()){

                    }
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

    @Override
    public void nextStep() {

    }

    public boolean search(){
        //update current node
        currentNode = node[(int) movement.getX()][(int) movement.getY()];

        //check Nodes in view
        setNodesInView();

        Collections.sort(openNode, Collections.reverseOrder());

        //no more open node to search
        if(openNode.size() < 1){
            return false;
        }

        //find lowest cost of openNode && with a common parent node as current
        for (int i = 0; i < openNode.size(); i++) {
            Node temp = getPathNode(openNode.get(i));
            if(temp != null){
                headingToNode = temp;
                goalNode = openNode.get(i);
            }
        }
        return true;
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
                    //withoutParent.add(node[row][col]);
                }else
                    //check if node is part of Open List
                if(openNode.contains(node[row][col])){
                    openNode.remove(node[row][col]);
                }
                node[row][col].setClosed();
                closedNode.add(node[row][col]);
            }
        }
        //giveParent();

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

    public void giveParent(){
        while (withoutParent.size() < 1){
            ArrayList<NodeAStar> temp = withoutParent;
            for (int i = 0; i < temp.size(); i++) {
                for (int j = -1; j < 2; j++) {
                    for (int k = -1; k < 2; k++) {
                        if(j != k && j+k != 0){
                            int row = temp.get(i).getX() + j;
                            int col = temp.get(i).getY() + k;
                            if(node[row][col].getParent() != null || node[row][col] == startNode){
                                node[withoutParent.get(i).getX()][withoutParent.get(i).getY()].setParent(node[row][col]);
                                withoutParent.remove(node[withoutParent.get(i).getX()][withoutParent.get(i).getY()]);
                            }
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
        boolean found = false;

        for (int i = 0; i < counterCurrent; i++) {
            for (int j = 0; j < counterDesired; j++) {
                if(desired.getParent() != null){
                    if(current == desired.getParent()){
                        if(toFar){
                            found = true;
                        }else{
                            return desired;
                        }
                    }else if(current.getParent() != null & current.getParent() == desired.getParent()){
                        if(toFar){
                            found = true;
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

        if(found){
            return currentNode.getParent();
        }

        return null;
    }

    public boolean wallInWay(){
        int x = (int) this.movement.getX();
        int y = (int) this.movement.getY();

        while( x != desiredX || y != desiredY){
            if(node[x][y].isClosed()){
                return true;
            }
            if(x < desiredX){
                x++;
            }else if(x > desiredX){
                x--;
            }
            if(y < desiredY){
                y++;
            }else if(y > desiredY){
                y--;
            }
        }
        return false;
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
