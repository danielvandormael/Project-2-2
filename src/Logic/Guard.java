package Logic;

public class Guard {
    private double x;
    private double y;
    private int status;
    private double angle;
    private double baseSpeed;
    private int id;
    private int distanceViewing;
    private double currentSpeed;
    private Cell[][] map;
    private Cell currentCell;

    public Guard(double x, double y, int status, double angle, double baseSpeed, int id, int distanceViewing, double currentSpeed){
        this.x = x;
        this.y = y;
        this.status = status;
        this.angle = angle;
        this.baseSpeed = baseSpeed;
        this.id = id;
        this.distanceViewing = distanceViewing;
        this.currentSpeed = currentSpeed;
        createMap();
    }

    private void createMap() {
        //?
    }

    public Cell[][] getMap(){
        return map;
    }

    public void move(int x, int y){
        currentCell = map[x][y];
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getStatus() {
        return status;
    }

    public double getAngle() {
        return angle;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public int getId() {
        return id;
    }

    public int getDistanceViewing() {
        return distanceViewing;
    }

    public double getCurrentSpeed(){
        return currentSpeed;
    }

    public Cell getCurrentCell(){
        return map[(int) x][(int) y];
    }

    public int getDirection(){
        if (angle == 0){
            return 0;
        }
        if (angle == 90){
            return 1;
        }
        if (angle == 180){
            return 2;
        }
        if (angle == 270){
            return 3;
        }
        else return 4;
    }
}
