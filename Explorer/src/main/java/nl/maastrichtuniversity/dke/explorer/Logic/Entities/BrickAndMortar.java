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
        // System.out.println("decisions " + decision[0] + " " + decision[1]);
        super.update(isGuard);
    }

    public void BAM(){
        System.out.println("current: " + (int)getX() + " " + (int)getY() + " " + getViewAngle());
        System.out.println(desiredX + " " + desiredY + " " + desiredAngle);
        if (((int) getX() == desiredX && (int) getY() == desiredY && getViewAngle() == desiredAngle) || ((int) getX() > (desiredX + 1) || (int) getX() < (desiredX - 1) || (int) getY() > (desiredY + 1) || (int) getY() < (desiredY - 1))) {
            Marking();
            Navigation();
            // System.out.println("calculating");
        }
        move();
        System.out.println("accessible size: " + accessible.size());
    }

    public void Marking(){
        Cell currentCell = map.getCell(getX(), getY());

        //check to see if neighbor cells are walls
        Cell cellFront = map.getCellInFront(currentCell, getViewAngle());
        Cell cellLeft = map.getLeftCell(currentCell, getViewAngle());
        Cell cellRight = map.getRightCell(currentCell, getViewAngle());

        accessible = new ArrayList<Cell>();
        // System.out.println("current: " + (int)getX() + " " + (int)getY());
        System.out.println("front: " + cellFront.getX() + " " + cellFront.getY());
        if (gamePanel.tileM.mapTile[cellFront.getX()][cellFront.getY()] == 1) {
            cellFront.setStatus(3);
            System.out.println("updated");
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
        Cell[] cellFrontier = new Cell[3];
        cellFrontier[0] = cellFront;
        cellFrontier[1] = cellLeft;
        cellFrontier[2] = cellRight;
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
        // System.out.println("currentCell status " + currentCell.getStatus());
    }

    public void Navigation(){
        ArrayList<Integer> wallCount = new ArrayList<Integer>();
        for (int i = 0; i < accessible.size(); i++){
            wallCount.add(0);
            if (accessible.get(i).getStatus() == 0){
                Cell cell1 = map.getCell(accessible.get(i).getX()+1,accessible.get(i).getY());
                if (gamePanel.tileM.mapTile[cell1.getX()][cell1.getY()] == 1) {
                    cell1.setStatus(3);
                }
                Cell cell2 = map.getCell(accessible.get(i).getX(),accessible.get(i).getY()+1);
                if (gamePanel.tileM.mapTile[cell2.getX()][cell2.getY()] == 1) {
                    cell2.setStatus(3);
                }
                Cell cell3 = map.getCell(accessible.get(i).getX()-1,accessible.get(i).getY());
                if (gamePanel.tileM.mapTile[cell3.getX()][cell3.getY()] == 1) {
                    cell3.setStatus(3);
                }
                Cell cell4 = map.getCell(accessible.get(i).getX(),accessible.get(i).getY()-1);
                if (gamePanel.tileM.mapTile[cell4.getX()][cell4.getY()] == 1) {
                    cell4.setStatus(3);
                }
                Cell[] cellFrontier2 = {cell1,cell2,cell3,cell4};
                for (int j = 0; j < cellFrontier2.length; j++){
                    if (cellFrontier2[j].getStatus() == 3 || cellFrontier2[j].getStatus() == 2){
                        wallCount.set(i, wallCount.get(i) + 1);
                    }
                }
            }
        }
        System.out.println("wallCount " + wallCount);
        int max = Collections.max(wallCount);
        int index = wallCount.indexOf(max);
        targetCell = accessible.get(index);
        Cell currentCell = map.getCell(getX(), getY());
        desiredX = targetCell.getX();
        desiredY = targetCell.getY();
        desiredAngle = map.getDirection(currentCell, targetCell);
        // System.out.println("targetCell: " + targetCell.getX() + " " + targetCell.getY());
        System.out.println("targetCell status " + targetCell.getStatus());
    }

    public void move(){
        Cell currentCell = map.getCell(getX(), getY());
        if(map.isInDirection(currentCell, targetCell, getViewAngle())) {
            // move towards target
            decision[0] = 1;
            decision[1] = 0;
            moved = true;
        } else {
            // turn towards target
            decision[0] = 0;
            decision[1] = 1;
            System.out.println("turn");
            moved = false;
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
