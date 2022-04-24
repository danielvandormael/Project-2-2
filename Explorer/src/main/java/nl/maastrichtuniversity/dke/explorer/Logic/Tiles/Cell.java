package nl.maastrichtuniversity.dke.explorer.Logic.Tiles;

public class Cell {

    private Cell parentCell; // 0 = north, 1 = east, 2 = south, 3 = west
    private int status; // 0 = unexplored, 1 = explored, 2 = visited, 3 = wall, 4 = teleport
    private int x;
    private int y;

    public boolean isGuardThere() {
        return isGuardThere;
    }

    public void setGuardThere(boolean guardThere) {
        isGuardThere = guardThere;
    }

    private boolean isGuardThere;



    public Cell(int x, int y){
        this.x = x;
        this.y = y;
        status = 0;
    }

    public Cell getParentCell() {
        return parentCell;
    }

    public void setParentCell(Cell oldPos) {
        parentCell = oldPos;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Cell otherCell){
        if (this.getX() == otherCell.getX() && this.getY() == otherCell.getY()){
            return true;
        }
        else {
            return false;
        }
    }
}
