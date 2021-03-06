package Logic.Tiles.Cell;

public class NodeAStar extends Node implements Comparable<NodeAStar>{
    private int gCost;
    private int hCost;
    private int vCost;
    private int sCost;
    private int fCost;


    public boolean solid;
    public boolean open;
    public boolean closed;

    public NodeAStar(int x, int y){
        super(x, y);
    }

    public void resetNode(){
        this.solid = false;
        this.open = false;
        this.closed = false;
    }

    @Override
    public int compareTo(NodeAStar node) {
        return Integer.compare(this.fCost, node.getfCost());
    }

    public void setgCost(int gCost) {
        this.gCost = gCost;
    }

    public void sethCost(int hCost) {
        this.hCost = hCost;
    }

    public void setvCost(int vCost) {
        this.vCost = vCost;
    }

    public void setsCost(int sCost) {
        this.sCost = sCost;
    }

    public void setfCost(int fCost) {
        this.fCost = fCost;
    }

    public void setOpen(){
        this.open = true;
    }

    public void setSolid(){
        this.solid = true;
    }

    public void setClosed() {
        this.closed = true;
    }

    public int getgCost() {
        return gCost;
    }

    public int gethCost() {
        return hCost;
    }

    public int getvCost() {
        return vCost;
    }

    public int getsCost() {
        return sCost;
    }

    public int getfCost() {
        return fCost;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isClosed() {
        return closed;
    }
}
