package nl.maastrichtuniversity.dke.explorer.Logic;

import java.util.ArrayList;

public class Cell {
    private int parentDirection; // 0 = north, 1 = east, 2 = south, 3 = west
    private int status; // 0 = unexplored, 1 = explored, 2 = visited, 3 = wall, 4 = teleport
    private double x;
    private double y;

    public int getParentDirection() {
        return parentDirection;
    }

    public int getStatus() {
        return status;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Cell(int parentDirection, int status, double x, double y){

    }

    // this method is incorrect
    public ArrayList<Cell> getUnexploredMoves(Cell[][] map, Cell currentCell) {
        ArrayList<Cell> moves = null;
        if (map[(int) (x+1)][(int) y].getStatus() == 0){
            moves.add(map[(int) (x+1)][(int) y]);
        }
        if (map[(int) (x-1)][(int) y].getStatus() == 0){
            moves.add(map[(int) (x-1)][(int) y]);
        }
        if (map[(int) (x)][(int) (y+1)].getStatus() == 0){
            moves.add(map[(int) (x)][(int) (y+1)]);
        }
        if (map[(int) (x)][(int) (y-1)].getStatus() == 0){
            moves.add(map[(int) (x)][(int) (y-1)]);
        }

        return moves;
    }

    // this method is incorrect
    public ArrayList<Cell> getExploredMoves(Cell[][] map, Cell currentCell) {
        ArrayList<Cell> moves = null;
        if (map[(int) (x+1)][(int) y].getStatus() == 1){
            moves.add(map[(int) (x+1)][(int) y]);
        }
        if (map[(int) (x-1)][(int) y].getStatus() == 1){
            moves.add(map[(int) (x-1)][(int) y]);
        }
        if (map[(int) (x)][(int) (y+1)].getStatus() == 1){
            moves.add(map[(int) (x)][(int) (y+1)]);
        }
        if (map[(int) (x)][(int) (y-1)].getStatus() == 1){
            moves.add(map[(int) (x)][(int) (y-1)]);
        }

        return moves;
    }
}
