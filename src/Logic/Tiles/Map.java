package Logic.Tiles;

public class Map {

    private int width;
    private int height;
    private Cell[][] map;

    public Map(int width, int height){
        this.width = width;
        this.height = height;
        map = new Cell[width][height];
        for (int y = 0; y< height; y++) {
            for (int x=0; x < width; x++) {
                map[x][y] = new Cell(x,y);
            }
        }
    }

    public Cell getCell(double x, double y){
        return map[(int) x][(int) y];
    }

    public Cell getCellInFront(Cell currentCell, double viewAngle){
        if( viewAngle == 0){
            return getCell(currentCell.getX() + 1, currentCell.getY());
        }else if(viewAngle == 90){
            return getCell(currentCell.getX(), currentCell.getY() + 1);
        }else if(viewAngle == 180){
            return getCell(currentCell.getX() - 1, currentCell.getY());
        }else if(viewAngle == 270){
            return getCell(currentCell.getX(), currentCell.getY()  - 1);
        }
        return null;
    }

    public Cell getLeftCell(Cell currentCell, double viewAngle){
        if(viewAngle >= 270){
            return getCellInFront(currentCell, (0 + (viewAngle-270)));
        }else {
            return getCellInFront(currentCell, (viewAngle+90));
        }
    }

    public Cell getRightCell(Cell currentCell, double viewAngle){
        if(viewAngle < 90){
            return getCellInFront(currentCell, (360 - (90-viewAngle)));
        }else{
            return getCellInFront(currentCell, (viewAngle-90));
        }
    }

    public int getDistance(Cell currentCell, Cell targetCell) {
        return Math.abs(currentCell.getX() - targetCell.getX()) + Math.abs(currentCell.getY() - targetCell.getY());
    }

    public boolean isInDirection(Cell currentCell, Cell targetCell, double viewAngle) {
        int dx = targetCell.getX() - currentCell.getX();
        int dy = targetCell.getY() - currentCell.getY();
        if( viewAngle == 0){
            return dx > 0 && dy == 0;
        }else if(viewAngle == 90){
            return dx == 0 && dy > 0;
        }else if(viewAngle == 180){
            return dx < 0 && dy == 0;
        }else if(viewAngle == 270){
            return dx == 0 && dy < 0;
        }
        return false;
    }

    public int getDirection(Cell currentCell, Cell targetCell) {
        int dx = targetCell.getX()- currentCell.getX();
        int dy = targetCell.getY() -currentCell.getY();
        int desiredViewAngle = 0;
        if (dx > 0) desiredViewAngle = 0;
        if (dy > 0) desiredViewAngle = 90;
        if (dx < 0) desiredViewAngle = 180;
        if (dy < 0) desiredViewAngle = 270;
        return desiredViewAngle;
    }

    public String exportMap() {
        StringBuilder s = new StringBuilder();
        for (int y = 0; y< height; y++) {
            for (int x=0; x< width; x++) {
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
