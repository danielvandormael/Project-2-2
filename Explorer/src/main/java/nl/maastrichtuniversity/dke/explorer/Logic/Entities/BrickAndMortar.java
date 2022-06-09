package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Cell;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Map;
import java.util.Collections;

import java.util.ArrayList;

public class BrickAndMortar extends Guard {

    Map map;
    int[] decision = new int[2]; // 1- movement  2- rotation
    ArrayList<Cell> accessible = new ArrayList<Cell>();
    private int desiredX;
    private int desiredY;
    private double desiredAngle;
    private Cell targetCell;
    private int type;
    private boolean moved = false;

    public BrickAndMortar(int id, double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel, int type) {
        super(id, x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        map = new Map(gamePanel.scenario.getMapWidth(), gamePanel.scenario.getMapHeight());
        desiredX= (int) x;
        desiredY= (int) y;
        desiredAngle = viewAngle;
        this.type = type;
        Marking();
        Navigation();
    }

    public void update(boolean isGuard) {
        BAM();
        setAction(decision[0], decision[1]);
        super.update(isGuard);
    }

    public void BAM(){
        System.out.println("current: " + (int)getX() + " " + (int)getY());
        System.out.println(desiredX + " " + desiredY);
        if (moved) {
            Marking();
            Navigation();
            moved = false;
        }
        else{
            move();
        }
    }

    public void Marking(){
        Cell currentCell = map.getCell(getX(), getY());

        //check to see if neighbor cells are walls
        Cell cellFront = map.getCellInFront(currentCell, getViewAngle());
        Cell cellLeft = map.getLeftCell(currentCell, getViewAngle());
        Cell cellRight = map.getRightCell(currentCell, getViewAngle());
        Cell[] cellFrontier = {cellFront, cellLeft, cellRight};
        accessible = new ArrayList<Cell>();
        if (gamePanel.tileM.mapTile[cellFront.getX()][cellFront.getY()] == 1) {
            cellFront.setStatus(3);
        }
        if (gamePanel.tileM.mapTile[cellLeft.getX()][cellLeft.getY()] == 1) {
            cellLeft.setStatus(3);
        }
        if (gamePanel.tileM.mapTile[cellRight.getX()][cellRight.getY()] == 1) {
            cellRight.setStatus(3);
        }
        if (gamePanel.tileM.mapTile[getCell2().getX()][getCell2().getY()] == 1) {
            getCell2().setStatus(3);
        }
        if (gamePanel.tileM.mapTile[getCell4().getX()][getCell4().getY()] == 1) {
            getCell4().setStatus(3);
        }

        for (int i = 0; i < cellFrontier.length; i++){
            if (cellFrontier[i].getStatus()!=3 && cellFrontier[i].getStatus()!=2){
                accessible.add(cellFrontier[i]);
            }
        }

        if (accessible.size() == 1){
            currentCell.setStatus(2);
        }
        else if (accessible.size() == 2){
            if (accessible.get(0).equals(cellFront) && accessible.get(1).equals(cellLeft)){
                if (getCell2().getStatus()!=3 && getCell2().getStatus()!=2){
                    currentCell.setStatus(2);
                }
                else {
                    currentCell.setStatus(1);
                }
            }
            else if (accessible.get(0).equals(cellFront) && accessible.get(1).equals(cellRight)){
                if (getCell4().getStatus()!=3 && getCell4().getStatus()!=2){
                    currentCell.setStatus(2);
                }
                else {
                    currentCell.setStatus(1);
                }
            }
            else if (accessible.get(0).equals(cellLeft) && accessible.get(1).equals(cellRight)){
                currentCell.setStatus(1);
            }
        }
        else if (accessible.size() == 3){
            if (getCell2().getStatus()!=3 && getCell2().getStatus()!=2 && getCell4().getStatus()!=3 && getCell4().getStatus()!=2){
                currentCell.setStatus(2);
            }
            else{
                currentCell.setStatus(1);
            }
        }
        else if (accessible.size() == 0){
            decision[0] = 0;
            decision[1] = 1;
        }
    }

    public void Navigation(){
        ArrayList<Integer> wallCount = new ArrayList<Integer>();
        for (int i = 0; i < accessible.size(); i++){
            wallCount.add(0);
            if (accessible.get(i).getStatus() == 0){
                Cell cellFront = map.getCellInFront(accessible.get(i), getViewAngle());
                Cell cellLeft = map.getLeftCell(accessible.get(i), getViewAngle());
                Cell cellRight = map.getRightCell(accessible.get(i), getViewAngle());
                Cell[] cellFrontier = {cellFront, cellLeft, cellRight};
                for (int j = 0; j < cellFrontier.length; j++){
                    if (cellFrontier[j].getStatus() == 3 || cellFrontier[j].getStatus() == 2){
                        wallCount.set(i, wallCount.get(i) + 1);
                    }
                }
            }
        }
        int max = Collections.max(wallCount);
        int index = wallCount.indexOf(max);
        targetCell = accessible.get(index);
        Cell currentCell = map.getCell(getX(), getY());
        desiredX = targetCell.getX();
        desiredY = targetCell.getY();
        desiredAngle = map.getDirection(currentCell, targetCell);
    }

    public void move(){
        Cell currentCell = map.getCell(getX(), getY());
        if(map.isInDirection(currentCell, targetCell, getViewAngle())) {
            // move towards target
            decision[0] = 1;
            decision[1] = 0;
            desiredX= (int) getX();
            desiredY= (int) getY();
            moved = true;
        } else {
            // turn towards target
            decision[0] = 0;
            decision[1] = 1;
            desiredX= targetCell.getX();
            desiredY= targetCell.getY();
            desiredAngle = map.getDirection(currentCell, targetCell);
        }
    }

    public Cell getCell2(){
        // facing right
        if (getViewAngle() == 0){
            return map.getCell(getX()+1,getY()-1);
        }
        // facing downwards
        else if (getViewAngle() == 90) {
            return map.getCell(getX()+1,getY()+1);
        }
        // facing left
        else if (getViewAngle() == 180) {
            return map.getCell(getX()-1,getY()+1);
        }
        // facing upwards
        else if (getViewAngle() == 270) {
            return map.getCell(getX()-1,getY()-1);
        }
        return null;
    }

    public Cell getCell4(){
        // facing right
        if (getViewAngle() == 0){
            return map.getCell(getX()+1,getY()+1);
        }
        // facing downwards
        else if (getViewAngle() == 90) {
            return map.getCell(getX()-1,getY()+1);
        }
        // facing left
        else if (getViewAngle() == 180) {
            return map.getCell(getX()-1,getY()-1);
        }
        // facing upwards
        else if (getViewAngle() == 270) {
            return map.getCell(getX()+1,getY()-1);
        }
        return null;
    }

}
