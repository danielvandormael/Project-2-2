package Logic;

public class Map {

    private int width;
    private int height;
    private Cell[][] map;

    public Map(int width, int height){
        this.width = width;
        this.height = height;
        map = new Cell[width][height];
        for (int y = 0; y< height; y++) {
            for (int x=0; x<width; x++) {
                map[x][y] = new Cell(x,y);
            }
        }
    }

    public Cell getCell(int x, int y){
        if (x < width && x >= 0 && y < height && y>= 0){
            return map[x][y];
        }
        else{
            System.out.println("Cell is null " + x + " " + y);
            return null;
        }

    }

    public Cell getCellInFront(Cell currentCell, int direction){
        switch(direction){
            case 0:
                return getCell(currentCell.getX(), currentCell.getY() + 1);
            case 1:
                return getCell(currentCell.getX() + 1, currentCell.getY());
            case 2:
                return getCell(currentCell.getX(), currentCell.getY() - 1);
            case 3:
                return getCell(currentCell.getX() - 1, currentCell.getY());
        }
        return null;
    }

    public Cell getLeftCell(Cell currentCell, int direction){
        return getCellInFront(currentCell, ((direction+3)%4));
    }

    public Cell getRightCell(Cell currentCell, int direction){
        return getCellInFront(currentCell, ((direction+1)%4));
    }

    public int getDistance(Cell currentCell, Cell targetCell) {
        return Math.abs(currentCell.getX() - targetCell.getX()) + Math.abs(currentCell.getY() - targetCell.getY());
    }

    public boolean isInDirection(Cell currentCell, Cell targetCell, int direction) {
        int dx = targetCell.getX()- currentCell.getX();
        int dy = targetCell.getY() -currentCell.getY();
        switch(direction){
            case 0:
                return dx == 0 && dy > 0;
            case 1:
                return dx > 0 && dy == 0;
            case 2:
                return dx == 0 && dy < 0;
            case 3:
                return dx < 0 && dy == 0;
        }
        return false;
    }

    public int getDirection(Cell currentCell, Cell targetCell) {
        int dx = targetCell.getX()- currentCell.getX();
        int dy = targetCell.getY() -currentCell.getY();
        int direction = 0;
        if (dy > 0) direction = 0;
        if (dx > 0) direction = 1;
        if (dy < 0) direction = 2;
        if (dx < 0) direction = 3;
        return direction;
    }

    public String exportMap() {
        StringBuilder s = new StringBuilder();
        for (int y = 0; y< height; y++) {
            for (int x=0; x<width; x++) {
                s.append(getCell(x,y).getStatus());
            }
            s.append('\n');
        }
        return s.toString();
    }

    public boolean isExplored(){
        for (int y = 0; y< height; y++) {
            for (int x=0; x<width; x++) {
                if (getCell(x,y).getStatus() == 0){
                    return false;
                }
            }
        }
        return true;
    }
}
