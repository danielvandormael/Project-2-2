package Logic.PathFinder;

import Logic.Tiles.Cell.Node;
import Logic.Tiles.Cell.NodeAStar;

import java.util.ArrayList;

public class AStar {
    NodeAStar[][] nodes;
     public ArrayList<Node> path = new ArrayList<>();

    ArrayList<NodeAStar> openNode = new ArrayList<>();

    ArrayList<NodeAStar> canUse;


    NodeAStar current, start, goal;
    boolean goalReached = false;

    public AStar(int width, int height, Node start, Node goal, ArrayList<NodeAStar> canUse){
        nodes = new NodeAStar[width][height];
        this.current = (NodeAStar) start;
        this.start = (NodeAStar) start;
        this.goal= (NodeAStar) goal;
        this.canUse = canUse;
        this.canUse.add((NodeAStar) goal);

        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[0].length; j++) {
                nodes[i][j] = new NodeAStar(i,j);
            }
        }

        openNode.add(nodes[start.getX()][start.getY()]);
    }

    public boolean search(){

        int step = 0;

        while(goalReached == false && step < 500){

            int row = current.getX();
            int col = current.getY();

            current.setClosed();
            openNode.remove(current);

            //Open all node in view
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if( (j != 0 || i != 0) && canUse.contains(nodes[row + i][col + j])){
                        getCost(nodes[row + i][col + j]);
                        openNode.add(nodes[row + i][col + j]);
                    }
                }
            }

            //find best node
            int bestNodeIndex = 0;
            int bestNodeFCost = 9999;

            for (int i = 0; i < openNode.size(); i++) {

                //Check if node has better F cost
                if(openNode.get(i).getfCost() < bestNodeFCost){
                    bestNodeIndex = i;
                    bestNodeFCost = openNode.get(i).getfCost();
                }
                //If F cost is equal, check the G cost
                else if (openNode.get(i).getfCost() == bestNodeFCost){
                    if(openNode.get(i).getgCost() < openNode.get(bestNodeIndex).getgCost()){
                        bestNodeIndex = i;
                    }
                }
            }

            //If there is no nodes in openNode, end the loop
            if(openNode.size() == 0){
                break;
            }

            //After the loop, openNode [bestNodeIndex] is the next step (=currentNode)
            current = openNode.get(bestNodeIndex);

            if(current == goal){
                goalReached = true;
                trackPath();
            }
            step++;
        }
        return goalReached;
    }

    private void getCost(NodeAStar node){
        //G Cost
        int xDistance = Math.abs(node.getX() - start.getX());
        int yDistance = Math.abs(node.getY() - start.getY());
        node.setgCost(xDistance + yDistance);

        //H Cost
        // Ax + By + C == 0 if (x,y) are on vector
        // d = |Ax + By + C| / sqrt(A^2 + B^2)
        xDistance = Math.abs(node.getX() - goal.getX());
        yDistance = Math.abs(node.getY() - goal.getY());

        node.sethCost(xDistance + yDistance);

        //F Cost
        node. setfCost(node.getgCost() + node.gethCost());
    }

    //tack path from start to goal
    public void trackPath(){
        Node temp = goal;

        while(current != start){
            path.add(0, temp);
            temp = temp.getParent();
        }
    }
}
