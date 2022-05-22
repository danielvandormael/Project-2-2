package Logic.Entities.astar;

public class Node {
    private int id;
    private Node parent;
    private int x;
    private int y;
    private int scoreF;

    public Node(int id, Node parent, int x, int y, int scoreF) {
        this.id = id;
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.scoreF = scoreF;
    }

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setScoreF(int scoreF) {
        this.scoreF = scoreF;
    }

    public int getId() {
        return id;
    }

    public Node getParent() {
        return parent;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getScoreF() {
        return scoreF;
    }


}
