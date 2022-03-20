package Logic.Entities;

import GUI.GamePanel;

public class DFSAgent extends Guard{
    private static final int OPEN = 0;
    private static final int TRIED = 1;
    private static final int Wall = 2;

    private int orientation; // 1. LEFT 2. DOWN 3.RIGHT 4.UP

    private double desiredAngle;
    private double ChooseOfMove;


    public DFSAgent(double x, double y, double viewAngle, double viewRange, double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel) {
        super(x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
    }



    public void checkView(){

    }

}
