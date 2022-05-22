package Logic.Entities;

import GUI.GamePanel;
import Logic.Area;
import Logic.Entities.astar.Node;
import Logic.Tiles.Map;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class ASAgent extends Intruder{

    //vector for target area
    private double XDirectTarget;
    private double YDirectTarget;

    private double desiredAngle;
    private int desiredX;
    private int desiredY;


    private ArrayList<Node> openNodes;
    private ArrayList<Node> closedNodes;

    private Node currentNode;

    private Node parent;

    private int idcounter;

    int [] decision = new int[2]; // 1- movement  2- rotation

    Map map;


    public ASAgent(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(id, x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        map = new Map(gamePanel.scenario.getMapWidth(), gamePanel.scenario.getMapHeight());
        desiredX= (int) x;
        desiredY= (int) y;
        desiredAngle = viewAngle;
        openNodes = new ArrayList<Node>();
        closedNodes = new ArrayList<Node>();
        idcounter = 0;
    }

    public void update(){
        //AStar();
        setAction(0, 0);
        super.update();
    }

    public void AStar(){
        if((int) getX() == desiredX && (int) getY() == desiredY){
            currentNode=null;
            int choiceOfCurrent = -1;
            for(int i = 0; i<openNodes.size(); i++){
                if(openNodes.get(i).getScoreF() < currentNode.getScoreF()){
                    currentNode = openNodes.get(i);
                    choiceOfCurrent = i;
                }
            }

            //remove current from open
            openNodes.remove(choiceOfCurrent);

            //if current is target
            if (gamePanel.tileM.mapTile[currentNode.getX()][currentNode.getY()] == 4) {
                //stand and wait
            }

            //get neighbours of current
            //check if neighbor is transversable or is in closed
            for(int i = -1; i<= 1; i++){
                for(int j = -1; j <= 1; j++){

                }
            }

            //if new path to neighbour is shorter or neighbour is not in openlist
                //set f_cost of neighbor
                //set parent of neighbour to current
                //if neighbour is not in openlist
                    //add neighbour to openlist

        }

    }

    public void demo(){
        if(gamePanel.tileM.mapTile[currentNode.getX()][currentNode.getY()] == 4){

        }else if((int)getX() == currentNode.getX() && (int)getY() == currentNode.getY()){ // reached current node and wants new nodes
            closedNodes.add(currentNode);
            currentNode=null;
            int choiceOfCurrent = -1;
            for(int i = 0; i<openNodes.size(); i++){
                if(openNodes.get(i).getScoreF() < currentNode.getScoreF()){
                    currentNode = openNodes.get(i);
                    choiceOfCurrent = i;
                }
            }
            //remove current from open
            openNodes.remove(choiceOfCurrent);
        }
    }

    public void StartAStar(){
        directionToTarget();
        openNodes.add(new Node(idcounter, null,(int) getX(), (int) getY(), 0));
        idcounter++;
    }

    public void directionToTarget(){
        Area target = gamePanel.scenario.getTargetArea();
        int[] centerTarget = target.getCenterAreaCoord();

        XDirectTarget = centerTarget[0] - this.getX();
        YDirectTarget = centerTarget[1] - this.getY();
    }

    public void draw(Graphics2D g){
        double angleTarget = Math.atan(YDirectTarget/XDirectTarget);
        int distanceFromEntity = 3;

        AffineTransform at = AffineTransform.getTranslateInstance( gamePanel.getTileSize()*(getX() + (Math.cos(angleTarget) * distanceFromEntity)), gamePanel.getTileSize()*(getY() + (Math.sin(angleTarget) * distanceFromEntity)));
        at.rotate(angleTarget + Math.PI/2, arrowImg.getWidth()/2, arrowImg.getHeight()/2);
        at.scale(1.5, 1.5);
        g.drawImage(arrowImg, at, null);
        super.draw(g);
    }
}
