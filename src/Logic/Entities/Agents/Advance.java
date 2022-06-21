package Logic.Entities.Agents;

import GUI.GamePanel;
import Logic.Entities.Intruder;
import Logic.Tiles.Cell.Node;
import Logic.Tiles.Cell.NodeAStar;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Advance extends Intruder {
    private NodeAStar[][] node;
    private ArrayList<NodeAStar> openNode = new ArrayList<>();
    private ArrayList<NodeAStar> closedNode = new ArrayList<>();
    private ArrayList<NodeAStar> solidNode = new ArrayList<>();
    Node startNode, goalNode, currentNode, headingToNode;

    public int coordX;
    public int coordY;


    int [] decisions = new int[2]; // 1- movement  2- rotation


    public Advance(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(id, x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        instantiateNodes();
        desiredX= (int) x;
        desiredY= (int) y;
        desiredAngle = viewAngle;

        startNode = node[(int) x][(int) y];
        goalNode = node[(int) x][(int) y];
        headingToNode = node[(int) x][(int) y];
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
        search();
        if(atHeading()){
            nextStep();
            desiredX = headingToNode.getX();
            desiredY = headingToNode.getY();
            desiredAngle = vision.angleBetween(desiredX, desiredY);
        }
        //SET UP COORDS
        coordX = (int) movement.getX();
        coordY = (int) movement.getY();

        //Check for target Area
        ArrayList<Node> inView = this.vision.tilesInView();
        for (int i = 0; i < inView.size(); i++) {
            if(gamePanel.tileM.mapTile[inView.get(i).getX()][inView.get(i).getY()] == 4){
                desiredX = inView.get(i).getX();
                desiredY = inView.get(i).getY();
                desiredAngle= vision.angleBetween(desiredX, desiredY);
                return true;
            }
        }

        //when through teleport
        if(this.movement.isThroughTeleport()){
            reset(coordX, coordY);
            desiredX = coordX;
            desiredY = coordY;
            desiredAngle = this.vision.getViewAngle();
        }else //ENTITY IN WALL
            if(gamePanel.tileM.mapTile[coordX][coordY] == 4){
                decisions = new int[]{0, 0};
            }else if(coordX == desiredX && coordY == desiredY && vision.getViewAngle() == desiredAngle){
                if(coordX == headingToNode.getX() && coordY == headingToNode.getY() &&  coordX == goalNode.getX() && coordY == goalNode.getY()){

                }else if(coordX == headingToNode.getX() && coordY == headingToNode.getY()){

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

    public void reset(int x, int y){
        startNode = node[x][y];
        goalNode = node[x][y];

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


    public boolean nextStep() {
        //REEVALUATE COST OF OPEN
        for (int i = 0; i < openNode.size(); i++) {
            getCost(openNode.get(i));
        }

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


    public void search(){
        //update current node
        currentNode = node[(int) movement.getX()][(int) movement.getY()];
        //check Nodes in view
        setTilesInView();
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

    public boolean atHeading(){
        currentNode = node[(int) movement.getX()][(int) movement.getY()];
        if(currentNode == headingToNode){
            return true;
        }
        return false;
    }



    public void setTilesInView(){
        //CURRENT COORDS
        int row = currentNode.getX();
        int col = currentNode.getY();
        if(node[row][col].isClosed() == false
                && node[row][col].isOpen() == false
                && node[row][col].isSolid() == false){
            node[row][col].setClosed();
            closedNode.add(node[row][col]);
        }

        ArrayList<Node> view = vision.tilesInView();
        for (int i = 0; i < view.size(); i++) {
            row = view.get(i).getX();
            col = view.get(i).getY();

            //SOLID
            if(gamePanel.tileM.mapTile[row][col] == 1){
                if(openNode.contains(node[row][col])){
                    openNode.remove(node[row][col]);
                }
                node[row][col].setSolid();
                solidNode.add(node[row][col]);
            }

            //CLOSED
            if(node[row][col].isOpen() == true
                    && node[row][col].isClosed() == false
                    && node[row][col].isSolid() == false){
                openNode.remove(node[row][col]);
                node[row][col].setClosed();
                closedNode.add(node[row][col]);
            }else if(node[row][col].isOpen() == false
                    && node[row][col].isClosed() == false
                    && node[row][col].isSolid() == false){
                node[row][col].setParent(node[currentNode.getX()][currentNode.getY()]);
                closedNode.add(node[row][col]);
            }
        }

        //OPEN
        for (int i = 0; i < closedNode.size(); i++) {


            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if(j != k && j+k != 0){
                        row = closedNode.get(i).getX() + j;
                        col = closedNode.get(i).getY() + k;
                        if(node[row][col].isClosed() == false
                                && node[row][col].isOpen() == false
                                && node[row][col].isSolid() == false
                                && gamePanel.tileM.mapTile[row][col] != 1){
                            getCost(node[row][col]);
                            node[row][col].setOpen();
                            openNode.add(node[row][col]);

                            boolean closeParent = false;
                            for (int l = -1; l < 2; l++) {
                                for (int m = -1; m < 2; m++) {
                                    if(!(l == 0 && m == 0)){
                                        if(gamePanel.tileM.mapTile[row+l][col+m] == 1){
                                            closeParent = true;
                                        }
                                    }
                                }
                            }

                            if(closeParent){
                                node[row][col].setParent(node[closedNode.get(i).getX()][closedNode.get(i).getY()]);
                            }else{
                                node[row][col].setParent(node[currentNode.getX()][currentNode.getY()]);
                            }
                        }
                    }
                }
            }
        }
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

        //V Cost
        ArrayList<Node> guardsInView = vision.guardsInView();
        int highestScore = 0;
        if(guardsInView.size() > 0){

            for (int i = 0; i < guardsInView.size(); i++) {
                int angleGuard = (int) (Math.atan2(guardsInView.get(i).getY() - coordX, guardsInView.get(i).getY() - coordY) * 180 / Math.PI);

                int angleNode = (int) (Math.atan2(node.getY() - coordX, node.getY() - coordY) * 180 / Math.PI);

                int result = getCostAngle(angleGuard,angleNode, 5);
                if(result > highestScore){
                    highestScore = result;
                }
            }
        }
        node.setvCost(highestScore);

        //S Cost
        highestScore = 0;
        if(soundSense.sounds.size() > 0){
            for (int i = 0; i < guardsInView.size(); i++) {
                int angleSound = (int) (Math.atan2(soundSense.sounds.get(i).getY()- coordX, soundSense.sounds.get(i).getY() - coordY) * 180 / Math.PI);

                int angleNode = (int) (Math.atan2(node.getY() - coordX, node.getY() - coordY) * 180 / Math.PI);
                int result = getCostAngle(angleSound,angleNode, 2);
                if(result > highestScore){
                    highestScore = result;
                }
            }
        }
        node.setsCost(highestScore);


        //F Cost
        node. setfCost(node.getgCost() + node.gethCost() + node.getvCost() + node.getsCost());
    }

    public int getCostAngle(int angleAvoid, int angleNode, int multiplier){

        for (int i = 0; i < 360; i++) {
            int lower = (-1)*i;
            int higher = i;
            if(angleNode + lower == angleAvoid){
                return (360 - Math.abs(lower))*multiplier;
            }else if(angleNode + higher == angleAvoid){
                return (360- higher)*multiplier;
            }
        }
        return 0;
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

