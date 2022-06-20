package Logic.Entities.Agents;

import GUI.GamePanel;
import Logic.Entities.Intruder;
import Logic.Tiles.Cell.Node;
import Logic.Tiles.Cell.NodeAStar;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class ASAgent extends Intruder{
    private NodeAStar[][] node;
    private ArrayList<NodeAStar> openNode = new ArrayList<>();
    private ArrayList<NodeAStar> closedNode = new ArrayList<>();
    private ArrayList<NodeAStar> solidNode = new ArrayList<>();
    private ArrayList<NodeAStar> noParent = new ArrayList<>();
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

        inicializeAStar((int) x, (int) y);
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

    public boolean controller() {
        //SET UP COORDS
        coordX = (int) movement.getX();
        coordY = (int) movement.getY();

        //when through teleport
        if(this.movement.isThroughTeleport()){
            desiredX = coordX;
            desiredY = coordY;
            desiredAngle = this.vision.getViewAngle();
        }else //ENTITY IN WALL
            if(gamePanel.tileM.mapTile[coordX][coordY] == 4){
                decisions = new int[]{0, 0};
            }else if(coordX == desiredX && coordY == desiredY && vision.getViewAngle() == desiredAngle){
                ArrayList<Node> inView = this.vision.tilesInView();
                for (int i = 0; i < inView.size(); i++) {
                    if(gamePanel.tileM.mapTile[inView.get(i).getX()][inView.get(i).getY()] == 4){
                        desiredX = inView.get(i).getX();
                        desiredY = inView.get(i).getY();
                        desiredAngle= vision.angleBetween(desiredX, desiredY);
                        return true;
                    }
                }
                if(coordX == headingToNode.getX() && coordY == headingToNode.getY() &&  coordX == goalNode.getX() && coordY == goalNode.getY()){
                    atGoal();
                    findNext();
                }else if(coordX == headingToNode.getX() && coordY == headingToNode.getY()){
                    findNext();
                }
                desiredX = headingToNode.getX();
                desiredY = headingToNode.getY();
                desiredAngle = vision.angleBetween(desiredX, desiredY);
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

    public void inicializeAStar(int x, int y){
        startNode = node[x][y];
        goalNode = node[x][y];

        headingToNode = goalNode;
        currentNode = startNode;

        node[x][y].setClosed();
        closedNode.add(node[x][y]);



        for (int j = -1; j < 2; j++) {
            for (int k = -1; k < 2; k++) {
                if(j != k && j+k != 0){
                    int row = goalNode.getX() + j;
                    int col = goalNode.getY() + k;
                    if(gamePanel.tileM.mapTile[row][col] == 1){
                        node[row][col].setSolid();
                        solidNode.add(node[row][col]);
                    }
                    if(gamePanel.tileM.mapTile[row][col] == 1){
                        node[row][col].setSolid();
                        solidNode.add(node[row][col]);
                    }
                    if(node[row][col].isClosed() == false
                            && node[row][col].isOpen() == false
                            && node[row][col].isSolid() == false){
                        getCost(node[row][col]);
                        node[row][col].setParent(node[currentNode.getX()][currentNode.getY()]);
                        node[row][col].setOpen();
                        openNode.add(node[row][col]);
                    }
                }
            }
        }
    }

    public void atGoal(){
        currentNode = node[(int) movement.getX()][(int) movement.getY()];

        //Make current Node Closed
        if(currentNode != startNode){
            node[goalNode.getX()][goalNode.getY()].setClosed();
            closedNode.add(node[goalNode.getX()][goalNode.getY()]);
            openNode.remove(node[goalNode.getX()][goalNode.getY()]);
        }

        //add new Open Nodes
        for (int j = -1; j < 2; j++) {
            for (int k = -1; k < 2; k++) {
                if(j != k && j+k != 0){
                    int row = goalNode.getX() + j;
                    int col = goalNode.getY() + k;
                    if(gamePanel.tileM.mapTile[row][col] == 1){
                        node[row][col].setSolid();
                        solidNode.add(node[row][col]);
                    }
                    if(node[row][col].isClosed() == false
                            && node[row][col].isOpen() == false
                            && node[row][col].isSolid() == false){
                        getCost(node[row][col]);
                        node[row][col].setParent(node[currentNode.getX()][currentNode.getY()]);
                        node[row][col].setOpen();
                        openNode.add(node[row][col]);
                    }
                }
            }
        }
    }



    public boolean findNext(){
        currentNode = node[(int) movement.getX()][(int) movement.getY()];

        Collections.sort(openNode);
        for (int i = 0; i < openNode.size(); i++) {
            Node temp = getPathNode(openNode.get(i));
            if(temp != null){
                headingToNode = temp;
                goalNode = openNode.get(i);
                return true;
            }
        }
        return false;
    }

    public Node getPathNode(Node target) {
        Node current = currentNode;
        Node goal = target;
        Node goalParent = target.getParent();

        int counterCurrent = getPathLength(current);
        int counterDesired = getPathLength(goalParent);

        boolean toFar = false;
        for (int i = 0; i <= counterCurrent; i++) {
            for (int j = 0; j <= counterDesired; j++) {
                if(current.getX() == goalParent.getX() && current.getY() == goalParent.getY()){
                    if(toFar){
                        return currentNode.getParent();
                    }else{
                        return goal;
                    }
                }
                if(j+1 != counterDesired){
                    goalParent = goalParent.getParent();
                    goal = goal.getParent();
                }
            }
            if(i+1 != counterCurrent){
                current = current.getParent();
            }
            goal = target;
            goalParent = target.getParent();
            toFar = true;
        }
        return null;
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

        //A cost

        //S cost

        //F Cost
        node. setfCost(node.getgCost() + node.gethCost());
    }

    public int getPathLength(Node node){
        Node temp = node;
        if(temp == null){
            System.out.println("invalid node");
        }
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
