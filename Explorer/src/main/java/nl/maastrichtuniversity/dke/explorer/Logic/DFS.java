package nl.maastrichtuniversity.dke.explorer.Logic;
import nl.maastrichtuniversity.dke.explorer.Logic.Scenario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DFS {
    ExpGuard guard;
    Cell currentCell;
    List<Cell> viewingArea;
    int direction; // 0 = north, 1 = east, 2 = south, 3 = west, 4 = unknown direction

    public DFS(ExpGuard guard){
        this.guard = guard;
    }

    public void DFSalgorithm(){
        currentCell = guard.getCurrentCell();
//        viewingArea = guard.getViewingArea;
        // Get
        direction = guard.getDirection();
        List<Cell> neighbour = guard.getPossibleMoves();
        ArrayList<Cell> unexploredMoves = new ArrayList<>();
        ArrayList<Cell> visitedMoves = new ArrayList<>();
        ArrayList<Cell> exploredMoves = new ArrayList<>();

        for(Cell move : neighbour){ //Add the unexplored cells to a list
            if(move.getMark() == null){
                // if the cell isn't marked with anything yet
                // (or we need to mark all the cell in map to 0(as for unexplored) at the beginning)
                move.setMark(0);
                unexploredMoves.add(move);
            }
            if(move.getMark()==2){ //We NEVER want to go to a
                // visited cell
                visitedMoves.add(move);
            }
        }

        if(unexploredMoves.size() > 0){
            Collections.shuffle(unexploredMoves);

            Cell chosenCell = unexploredMoves.get(0);
            //turnTo(chosenMove)  - rotate the agent to face the chosenCell if it's not in the same direction
            // viewingArea = guard.getViewingArea;
            // Get the forward cells based on the distance Viewing
            for (Cell cell: viewingArea)
            //Loops through the cell in viewing Area
            {
                //if the cell is not wall then move to it until it's a wall in front
                while((Scenario.inWall(cell.getX(),cell.getY())) == false)
                {
                    Cell oldPos = currentCell;
                    guard.move(cell.getX(),cell.getY());
                    cell.setMark(1);
                    cell.setAgentID(guard.getId());
                    cell.setParent(oldPos);
                    exploredMoves.add(cell);

                }
            }
        }
        else{
            //if the current cell is explored by the agent -> backtrack
            //this is for 1 agent
            if(currentCell.getAgentID() == guard.getId()){
                currentCell.setMark(2);
                Cell parentCell = currentCell.getParent();
                guard.move(parentCell.getX(), parentCell.getY());
            }


        }


















/**
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
**/


    }
}
