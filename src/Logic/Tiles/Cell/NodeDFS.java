package Logic.Tiles.Cell;

public class NodeDFS extends Node{

    private int status; // 0 = unexplored, 1 = explored, 2 = visited, 3 = wall, 4 = teleport

    public NodeDFS(int x, int y){
        super(x, y);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status){
        this.status = status;
    }


}
