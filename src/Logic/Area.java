package Logic;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the
 */

/**
 *
 * @author joel
 */
public class Area {
    protected int leftBoundary;
    protected int rightBoundary;
    protected int topBoundary;
    protected int bottomBoundary;

    public Area(){
        leftBoundary=0;
        rightBoundary=1;
        topBoundary=0;
        bottomBoundary=1;
    }

    public Area(int x1, int y1, int x2, int y2){
        leftBoundary= Math.min(x1,x2);
        rightBoundary= Math.max(x1,x2);
        topBoundary= Math.max(y1,y2);
        bottomBoundary= Math.min(y1,y2);
    }
    /*
        Line Segments array:
           s_px = position x
           s_py = position y
           s_dx = direction x
           s_dy = direction y
        { {s_px, s_py, s_dx, s_dy} }
     */
    public double [][] getLineSegments(){
        double [][] temp = new double[4][4];
        temp[0][0] = leftBoundary; //s_px
        temp[0][1] = bottomBoundary; //s_py
        temp[0][2] = rightBoundary - leftBoundary; //s_dx
        temp[0][3] = 0; //s_dy

        temp[1][0] = rightBoundary;
        temp[1][1] = bottomBoundary;
        temp[1][2] = 0;
        temp[1][3] = topBoundary - bottomBoundary;


        temp[2][0] = rightBoundary;
        temp[2][1] = topBoundary;
        temp[2][2] = leftBoundary - rightBoundary;
        temp[2][3] = 0;

        temp[3][0] = leftBoundary;
        temp[3][1] = topBoundary;
        temp[3][2] = 0;
        temp[3][3] = bottomBoundary - topBoundary;

        return temp;

    }

    /*
        Check whether a point is in the target area
    */
    public boolean isHit(double x,double y){
        return (y>bottomBoundary)&(y<topBoundary)&(x>leftBoundary)&(x<rightBoundary);
    }

    /*
        Check whether something with a radius is in the target area
        STILL TO BE IMPLEMENTED
    */
    public boolean isHit(double x,double y,double radius){
        return false;
    }

    public int getLeftBoundary() {
        return leftBoundary;
    }

    public int getRightBoundary() {
        return rightBoundary;
    }

    public int getTopBoundary() {
        return topBoundary;
    }

    public int getBottomBoundary() {
        return bottomBoundary;
    }

    public int[] getCenterAreaCoord(){
        int[] temp = new int[2];
        temp[0] = leftBoundary + (rightBoundary - leftBoundary)/2; //x coord
        temp[1] = bottomBoundary + (topBoundary - bottomBoundary)/2; //y coord
        return  temp;
    }
}
