package Logic.Entities;

import GUI.GamePanel;
import Logic.Area;
import Logic.Tiles.Cell.Node;
import Logic.Tiles.Map;
import Logic.Tiles.Cell.NodeAStar;

import java.util.ArrayList;

public class ASAgent extends Intruder{
    private NodeAStar[][] node;
    private ArrayList<NodeAStar> openList;
    private ArrayList<NodeAStar> closedList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    NodeAStar startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    //vector for target area
    private double XDirectTarget;
    private double YDirectTarget;

    private double desiredAngle;
    private int desiredX;
    private int desiredY;

    int [] decision = new int[2]; // 1- movement  2- rotation

    Map map;


    public ASAgent(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(id, x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        instantiateNodes();
        desiredX= (int) x;
        desiredY= (int) y;
        desiredAngle = viewAngle;
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

    public void resetNodes(){
        int row = 0;
        int col = 0;

        while(row < gamePanel.scenario.getMapWidth() && col < gamePanel.scenario.getMapHeight()){
            node[row][col].resetNode();

            row++;
            if(row  ==  gamePanel.scenario.getMapWidth()){
                row = 0;
                col++;
            }
        }
        //reset other setting
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startX, int startY, int goalX, int goalY){

        resetNodes();

        //set start node and goal node
        startNode = node[startX][startY];
        currentNode = startNode;
        goalNode = node[goalX][goalY];
        openList.add(currentNode);

        int row = 0;
        int col = 0;

        while(row < gamePanel.scenario.getMapWidth() && col < gamePanel.scenario.getMapHeight()){
            //set solid nodes
            //check tiles
            int tileValue = gamePanel.tileM.mapTile[row][col];
            if(gamePanel.tileM.tile[tileValue].collision == true){
                node[row][col].setSolid();
            }

            //Check for Interactive Tiles

            row++;
            if(row  ==  gamePanel.scenario.getMapWidth()){
                row = 0;
                col++;
            }
        }
    }

    public boolean search(){
        while(goalReached == false && step < 500){

            int row = currentNode.getX();
            int col = currentNode.getY();

            currentNode.setClosed();
            openList.remove(currentNode);

            //Open all node in view
            vision.tilesInView();

            //find best node
            int bestNodeIndex = 0;
            int bestNodeFCost = 9999;

            for (int i = 0; i < openList.size(); i++) {

                //Check if node has better F cost
                if(openList.get(i).getfCost() < bestNodeFCost){
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).getfCost();
                }
                //If F cost is equal, check the G cost
                else if (openList.get(i).getfCost() == bestNodeFCost){
                    if(openList.get(i).getgCost() < openList.get(bestNodeIndex).getgCost()){
                        bestNodeIndex = i;
                    }
                }

            }

            //If there is no nodes in openList, end the loop
            if(openList.size() == 0){
                break;
            }

            //After the loop, openList [bestNodeIndex] is the next step (=currentNode)
            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode){
                goalReached = true;
                trackPath();
            }
            step++;
        }

        return goalReached;
    }

    //tack path from start to goal
    public void trackPath(){
        Node current = goalNode;

        while(current != startNode){
            pathList.add(0, current);
            current = current.getParent();
        }
    }

    //track path from current node to desired
    public void trackPath(Node node){
        Node current = currentNode;

        while(current != node){
            pathList.add(0, current);
            if(current.getParent() == node.getParent()){

            }
            current = current.getParent();
        }

        if(current.getParent() == node.getParent()){

        }else{

        }
    }

    public void OpenNode(NodeAStar node){
        if(node.isOpen() == false && node.isClosed() == false && node.isSolid() == false){
            node.setOpen();
            node.setParent(currentNode);
            openList.add(node);
        }
    }

    public void getCost(NodeAStar node){
        //G Cost
        int xDistance = Math.abs(node.getX() - startNode.getX());
        int yDistance = Math.abs(node.getY() - startNode.getY());
        node.setgCost(xDistance + yDistance);

        //H Cost
        xDistance = Math.abs(node.getX() - goalNode.getX());
        yDistance = Math.abs(node.getY() - goalNode.getY());
        node.sethCost(xDistance + yDistance);

        //F Cost
        node. setfCost(node.getgCost() + node.gethCost());
    }

    public void update(){
        //AStar();
        setAction(0, 0);
        super.update();
    }


    public void directionToTarget(){
        Area target = gamePanel.scenario.getTargetArea();
        int[] centerTarget = target.getCenterAreaCoord();

        XDirectTarget = centerTarget[0] - this.movement.getX();
        YDirectTarget = centerTarget[1] - this.movement.getY();
    }

}
