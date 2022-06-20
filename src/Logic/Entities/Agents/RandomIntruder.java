package Logic.Entities.Agents;

import GUI.GamePanel;
import Logic.Entities.Intruder;
import Logic.Tiles.Cell.Node;

import java.util.ArrayList;

public class RandomIntruder extends Intruder {

    private int desiredX;
    private int desiredY;
    private double desiredAngle;

    //rotate after reaching a Node
    private boolean rotate;

    private int[] decisions;

    public RandomIntruder(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(id, x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        this.desiredX = (int) this.movement.getX();
        this.desiredY = (int) this.movement.getY();
        decisions = new int[2];
        rotate = false;
    }

    public void update(){
        randomDecision();
        setAction(decisions[0], decisions[1]);
        super.update();
    }

    public boolean randomDecision(){
        if(this.movement.isThroughTeleport()){
            desiredX = (int) this.movement.getX();
            desiredY = (int) this.movement.getY();
            desiredAngle = this.vision.getViewAngle();
        }else if(gamePanel.tileM.mapTile[(int) this.movement.getX()][(int) this.movement.getY()] == 4){
            decisions = new int[]{0, 0};
        }else if((int) this.movement.getX() == desiredX && (int) this.movement.getY() == desiredY && desiredAngle == this.vision.getViewAngle()){
            if(rotate == true){
                desiredAngle = Math.round(Math.random()*180);
                rotate = false;
            }else{
                ArrayList<Node> inView = this.vision.tilesInView();

                for (int i = 0; i < inView.size(); i++) {
                    if(gamePanel.tileM.mapTile[inView.get(i).getX()][inView.get(i).getY()] == 4){
                        desiredX = inView.get(i).getX();
                        desiredY = inView.get(i).getY();
                        desiredAngle= vision.angleBetween(desiredX, desiredY);
                        return true;
                    }
                }
                do{
                    int choice = (int) (Math.random()*inView.size());
                    desiredX = inView.get(choice).getX();
                    desiredY = inView.get(choice).getY();
                    desiredAngle= vision.angleBetween(desiredX, desiredY);
                }while(gamePanel.tileM.mapTile[desiredX][desiredY] == 1);
                rotate = true;
            }

        }
        if(desiredAngle != this.vision.getViewAngle()){

            decisions = new int[]{0, this.vision.turnWhichWay(desiredAngle)};
            return false;
        }else if(((int) this.movement.getX() != desiredX || (int) this.movement.getY() != desiredY) && desiredAngle == this.vision.getViewAngle()){
            if(((int) this.movement.getX() == desiredX && (int) this.movement.getY() != desiredY) || ((int) this.movement.getX() != desiredX && (int) this.movement.getY() == desiredY)){
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


}
