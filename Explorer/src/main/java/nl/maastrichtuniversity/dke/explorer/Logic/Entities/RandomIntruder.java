package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Cell;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Map;

import java.util.ArrayList;
import java.util.List;

public class RandomIntruder extends Intruder {

//    private double desiredAngle;
//    private int desiredX;
//    private int desiredY;
    Cell previousCell;
    Cell targetCell;
    Cell currentCell;
    ArrayList<Cell> viewingArea;

    int [] decision = new int[2]; // 1- movement  2- rotation

    Map map;

    public RandomIntruder(double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
        map = new Map(gamePanel.scenario.getMapWidth(), gamePanel.scenario.getMapHeight());
//        desiredX= (int) x;
//        desiredY= (int) y;
//        desiredAngle = viewAngle;
    }

    public void update(){
        randomlyMove();
        setAction(decision[0], decision[1]);
        super.update();
    }

    private void randomlyMove() {
        if (identifyGuard()){
            decision[1]=1;
            decision[0]=2;
        }
        decision[0] = 1;
        decision[1] = 0;
    }

    private boolean identifyGuard() {

        boolean guard = false;

        for (int i = 1; i <= getViewRange() ; i++) {
            currentCell = map.getCellInFront(currentCell,getViewAngle());
            viewingArea.add(currentCell);
        }

        for (Cell cell: viewingArea) {
            if (cell.isGuardThere()) {
                guard = true;
            }
        }
        return guard;
    }

}
