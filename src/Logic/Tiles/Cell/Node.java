package Logic.Tiles.Cell;

public class Node {
    private Node parent;
    private int x;
    private int y;


    public Node(int x, int y){
        this.x = x;
        this.y = y;
    }


    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Node getParent() {
        return parent;
    }
}
