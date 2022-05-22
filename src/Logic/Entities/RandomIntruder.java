package Logic.Entities;

import GUI.GamePanel;
import Logic.Entities.astar.Node;

import java.util.ArrayList;

public class RandomIntruder extends Intruder{

    private int desiredX;
    private int desiredY;
    private double desiredAngle;

    //rotate after reaching a celll
    private boolean rotate;

    private int[] decisions;

    public RandomIntruder(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(id, x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        this.desiredX = (int) getX();
        this.desiredY = (int) getY();
        decisions = new int[2];
        rotate = false;
    }

    public void update(){
        randomDecision();
        setAction(decisions[0], decisions[1]);
        super.update();
    }

    public boolean randomDecision(){

        if(gamePanel.tileM.mapTile[(int) getX()][(int) getY()] == 4){
            decisions = new int[]{0, 0};
        }else if((int) getX() == desiredX && (int) getY() == desiredY && desiredAngle == getViewAngle()){
            if(rotate == true){
                desiredAngle = Math.round(Math.random()*180);
                rotate = false;
            }else{
                ArrayList<Node> inView = tilesInView();

                for (int i = 0; i < inView.size(); i++) {
                    if(gamePanel.tileM.mapTile[inView.get(i).getX()][inView.get(i).getY()] == 4){
                        desiredX = inView.get(i).getX();
                        desiredY = inView.get(i).getY();
                        desiredAngle=angleBetween(desiredX, desiredY);
                        return true;
                    }
                }
                do{
                    int choice = (int) (Math.random()*inView.size());
                    desiredX = inView.get(choice).getX();
                    desiredY = inView.get(choice).getY();
                    desiredAngle= angleBetween(desiredX, desiredY);
                }while(gamePanel.tileM.mapTile[desiredX][desiredY] == 1);
                rotate = true;
            }

        }
        if(desiredAngle != getViewAngle()){
            decisions = new int[]{0, 1};
            return false;
        }else if(((int) getX() != desiredX || (int) getY() != desiredY) && desiredAngle == getViewAngle()){
            decisions = new int[]{1, 0};
            return false;
        }else {
            return false;
        }
    }

    public double angleBetween(int x2, int y2){
        int deltaX = x2 - (int)getX();
        int deltaY = y2 - (int)getY();

        double angle = Math.round(Math.toDegrees(Math.atan2(deltaY, deltaX)));
        if(angle < 0){
            angle = 360 + angle;
        }
        return angle;
    }
}
