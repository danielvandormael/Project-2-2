package nl.maastrichtuniversity.dke.explorer.Logic.Entities;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Cell;
import nl.maastrichtuniversity.dke.explorer.Logic.Tiles.Map;

import java.util.LinkedList;
import java.util.Queue;

public class BFS<T> extends Intruder {

    Queue<T> queue;  //to store the cells that are encountered but not visited
    LinkedList<T> Neighbors;
    Cell targetCell;
    Map map;
    Cell CurrentCell;
    Cell cellFront;
    Cell cellLeft;
    Cell cellRight;
    int [] visited;  //to store cells which get visited

public BFS( double x, double y, double viewAngle, double viewRange,
            double viewAngleSize, double baseSpeed, double sprintSpeed, GamePanel gamePanel)
{
    super(x, y, viewAngle, viewRange, viewAngleSize, baseSpeed, sprintSpeed, gamePanel);
    queue= new LinkedList<T>();
    CurrentCell = map.getCell(getX(),getY());
    this.map = map;
    this.targetCell = targetCell;
     cellFront = map.getCellInFront(CurrentCell, getViewAngle());
     cellLeft = map.getLeftCell(CurrentCell, getViewAngle());
     cellRight = map.getRightCell(CurrentCell, getViewAngle());
     LinkedList<T> Neighbors = new LinkedList<T>();
     visited = new int[Neighbors.size()];

}

public LinkedList createListOfNeighnors(Cell cellFront,Cell cellLeft,Cell cellRight) //creates a list of the neighboring cells
{
   Neighbors.add((T) cellFront);
   Neighbors.add((T) cellLeft);
   Neighbors.add((T) cellRight);

   return Neighbors;
}
public void AddToQueue()
{
    if( CurrentCell.getStatus()==0)  //if the cell is not visited,add it to the queue
    {
        queue.add((T) CurrentCell);

    }
}
public void RemoveFromQueueAndAddToVisited()
{
    while(!queue.isEmpty())
    {
        int cell= (int) queue.remove();
        for(int i=0;i<i+1;i++) {
            visited[i] =cell;
        }
    }
}
//generate the path



}
