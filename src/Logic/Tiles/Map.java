package Logic.Tiles;

import Logic.Tiles.Cell.Node;
import Logic.Tiles.Cell.NodeDFS;

public class Map {

    private int width;
    private int height;
    private NodeDFS[][] map;

    public Map(int width, int height){
        this.width = width;
        this.height = height;
        map = new NodeDFS[width][height];
        for (int y = 0; y< height; y++) {
            for (int x=0; x < width; x++) {
                map[x][y] = new NodeDFS(x,y);
            }
        }
    }

    public NodeDFS getNode(double x, double y){
        return map[(int) x][(int) y];
    }

    public NodeDFS getNodeInFront(Node current, double viewAngle){
        if( viewAngle == 0){
            return getNode(current.getX() + 1, current.getY());
        }else if(viewAngle == 90){
            return getNode(current.getX(), current.getY() + 1);
        }else if(viewAngle == 180){
            return getNode(current.getX() - 1, current.getY());
        }else if(viewAngle == 270){
            return getNode(current.getX(), current.getY()  - 1);
        }
        return null;
    }

    public NodeDFS getLeftNode(Node current, double viewAngle){
        if(viewAngle >= 270){
            return getNodeInFront(current, (0 + (viewAngle-270)));
        }else {
            return getNodeInFront(current, (viewAngle+90));
        }
    }

    public NodeDFS getRightNode(Node current, double viewAngle){
        if(viewAngle < 90){
            return getNodeInFront(current, (360 - (90-viewAngle)));
        }else{
            return getNodeInFront(current, (viewAngle-90));
        }
    }

    public int getDistance(Node current, Node target) {
        return Math.abs(current.getX() - target.getX()) + Math.abs(current.getY() - target.getY());
    }

    public boolean isInDirection(Node current, Node target, double viewAngle) {
        int dx = target.getX() - current.getX();
        int dy = target.getY() - current.getY();
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

    public int getDirection(Node current, Node target) {
        int dx = target.getX()- current.getX();
        int dy = target.getY() -current.getY();
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
                s.append(getNode(x,y).getStatus());
            }
            s.append('\n');
        }
        return s.toString();
    }

    public boolean isExplored(){
        for (int y = 0; y< height; y++) {
            for (int x=0; x<width; x++) {
                if (getNode(x,y).getStatus() == 0){
                    return false;
                }
            }
        }
        return true;
    }
}
