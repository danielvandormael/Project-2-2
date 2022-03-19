package Logic;

public class ExpGuard {
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

    public ExpGuard(int baseSpeed, int id, int distanceViewing, Map map){
        this.baseSpeed = baseSpeed;
        this.id = id;
        this.distanceViewing = distanceViewing;
        this.map = map;
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getBaseSpeed() {
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

}
