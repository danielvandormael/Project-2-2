package nl.maastrichtuniversity.dke.explorer.Logic;

import java.util.ArrayList;

public class DFS {
    ExpGuard guard;
    Cell currentCell;
    int direction; // 0 = north, 1 = east, 2 = south, 3 = west, 4 = unknown direction

    public DFS(ExpGuard guard){
        this.guard = guard;
    }

    public void DFSalgorithm(){
        currentCell = guard.getCurrentCell();
        direction = guard.getDirection();



        // return a list of cells which are unexplored and can be reached from the current cell
        // method is incorrect and needs to be changed
        ArrayList<Cell> unexploredMoves = currentCell.getUnexploredMoves(guard.getMap(), guard.getCurrentCell());
        // return a list of cells which are explored and can be reached from the current cell
        // method is incorrect and needs to be changed
        ArrayList<Cell> exploredMoves = currentCell.getExploredMoves(guard.getMap(), guard.getCurrentCell());

        if (unexploredMoves != null){
            //if we can reach a cell that is marked with 0, go forward if it's possible
            //  check if cell is wall or teleport, and if this is the case mark the cell correctly
            //  keep going forward and mark parent cells until we reach cell
            //  that is not marked with a 0 or we reach a wall
            //else randomly go to the one on the right or left, if they are marked with 0,
            //  but we first need to turn which takes time
            //set parent cell
            //mark the cell as 1
        }
        else if (exploredMoves != null){
            //if we can't reach a cell that is marked with a 0,
            //  go to a cell that is marked with a 1
            //mark the cell as 2
            //go back to parent cell
        }
        else {
            // you're finished with the exploration
        }



        //if you see walls or teleports, update the status of these cells


    }
}
