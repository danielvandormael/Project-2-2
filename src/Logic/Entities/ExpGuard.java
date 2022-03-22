package Logic.Entities;

import GUI.GamePanel;
import Logic.Tiles.Cell;
import Logic.Tiles.Map;

public class ExpGuard extends Guard {
    private int x;
    private int y;
    private int status;
    private int direction;
    private int baseSpeed;
    private int id;
    private int distanceViewing;
    private Map map;
    private Cell currentCell;
    private Cell targetCell;
    private int nextSpeed;
    private int nextDirection;
    boolean finished;

    private static final int OPEN = 0;
    private static final int TRIED = 1;
    private static final int WALL = 2;

    private int orientation; // 1. LEFT 2. DOWN 3.RIGHT 4.UP

    private double desiredAngle;
    private int desiredX;
    private int desiredY;

    int [] decision = new int[2]; // 1- movement  2- rotation


    public ExpGuard(double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        map = new Map(gamePanel.scenario.getMapWidth(), gamePanel.scenario.getMapHeight());
        this.baseSpeed = (int) baseSpeed;
        //this.distanceViewing = distanceViewing;
        distanceViewing = 5;
        this.x = (int) x;
        this.y = (int) y;
        desiredX = (int) x;
        desiredY = (int) y;
        desiredAngle = viewAngle;
        currentCell = map.getCell(x, y);
    }

    public void update(){
        System.out.println("updating guard");
        System.out.println("x " + getX() + " desired x " + desiredX);
        System.out.println("y " + getY() + " desired y " + desiredY);
        System.out.println("viewangle " + getViewAngle() + " desired viewangle " + desiredAngle);


        if ((int) getX() == desiredX && (int) getY() == desiredY && getViewAngle() == desiredAngle) {
            System.out.println("entered if");
            finished = gamePanel.mdfs.nextStep(this);
            desiredX = targetCell.getX();
            desiredY = targetCell.getY();
            desiredAngle = map.getDirection(currentCell, targetCell);
            switch (direction) {
                case 0:
                    setXYDirection(x, y + nextSpeed, direction);
                    break;
                case 1:
                    setXYDirection(x + nextSpeed, y, direction);
                    break;
                case 2:
                    setXYDirection(x, y - nextSpeed, direction);
                    break;
                case 3:
                    setXYDirection(x - nextSpeed, y, direction);
                    break;

            }
        }
        setAction(decision[0], decision[1]);
        System.out.println("desired x " + desiredX + " desired Y " + desiredY + " desired angle " + desiredAngle);
        System.out.println("movement " + decision[0] + " turning " + decision[1]);
        super.update();
    }



    public void setXYDirection(int x, int y, int direction){
        this.x = x;
        this.y = y;
        currentCell = map.getCell(x,y);
        this.direction = direction;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public Map getMap(){
        return map;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public int getId() {
        return id;
    }

    public int getDistanceViewing() {
        return distanceViewing;
    }

    public Cell getCurrentCell(){
        return currentCell;
    }

    public int getDirection() {
        return direction;
    }

    public void setTargetCell(Cell targetCell){
        this.targetCell = targetCell;
    }

    public Cell getTargetCell(){
        return targetCell;
    }

    public int getNextSpeed() {
        return nextSpeed;
    }

    public void setNextSpeed(int nextSpeed) {
        this.nextSpeed = nextSpeed;
    }

    public int getNextDirection() {
        return nextDirection;
    }

    public void setNextDirection(int nextDirection) {
        this.nextDirection = nextDirection;
    }

    public void setDecision(int movement, int rotation) {
        decision[0] = movement;
        decision[1] = rotation;
    }

    public int getTranslatedDirection(){
        switch (direction) {
            case 0:
                return 4;
            case 1:
                return 3;
            case 2:
                return 2;
            case 3:
                return 1;
            default:
                return 0;
        }


    }

}



