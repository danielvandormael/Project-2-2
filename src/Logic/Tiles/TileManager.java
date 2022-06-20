package Logic.Tiles;

import GUI.GamePanel;
import GUI.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TileManager {

    public Tile[] tile;
    public int [][] mapTile;

    GamePanel gamePanel;

    /*
           Tiles:
           0 - floor
           1 - wall
           2 - shaded
           3 - teleport
           4 - target
     */

    public TileManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        tile = new Tile[5];
        getTileImage8bit();
        generateMap();
    }

    public void generateMap(){
        mapTile = new int[gamePanel.scenario.getMapWidth()][gamePanel.scenario.getMapHeight()];

        //add floor
        for(int i = 0; i < mapTile.length; i++){
            for(int j = 0; j < mapTile[0].length; j++){
                mapTile[i][j] = 0;
            }
        }

        //add walls
        for(int x = 0; x < gamePanel.scenario.getWalls().size(); x++){
            for(int i = gamePanel.scenario.getWalls().get(x).getLeftBoundary(); i < gamePanel.scenario.getWalls().get(x).getRightBoundary(); i++){
                for(int j = gamePanel.scenario.getWalls().get(x).getBottomBoundary(); j < gamePanel.scenario.getWalls().get(x).getTopBoundary(); j++){
                    mapTile[i][j] = 1;
                }
            }
        }

        //add shaded
        for(int x = 0; x < gamePanel.scenario.getShaded().size(); x++){
            for(int i = gamePanel.scenario.getShaded().get(x).getLeftBoundary(); i < gamePanel.scenario.getShaded().get(x).getRightBoundary(); i++){
                for(int j = gamePanel.scenario.getShaded().get(x).getBottomBoundary(); j < gamePanel.scenario.getShaded().get(x).getTopBoundary(); j++){
                    mapTile[i][j] = 2;
                }
            }
        }

        //add teleport
        for(int x = 0; x < gamePanel.scenario.getTeleportals().size(); x++){
            for(int i = gamePanel.scenario.getTeleportals().get(x).getLeftBoundary(); i < gamePanel.scenario.getTeleportals().get(x).getRightBoundary(); i++){
                for(int j = gamePanel.scenario.getTeleportals().get(x).getBottomBoundary(); j < gamePanel.scenario.getTeleportals().get(x).getTopBoundary(); j++){
                    mapTile[i][j] = 3;
                }
            }
        }

        //add target
        for(int i = gamePanel.scenario.getTargetArea().getLeftBoundary(); i < gamePanel.scenario.getTargetArea().getRightBoundary(); i++){
            for(int j = gamePanel.scenario.getTargetArea().getBottomBoundary(); j < gamePanel.scenario.getTargetArea().getTopBoundary(); j++){
                mapTile[i][j] = 4;
            }
        }
    }


    public void getTileImage16bit(){
            setUp(0, "floor", false, 1);
            setUp(1, "wall", true, 1);
            setUp(2, "shaded", false, 1);
            setUp(3, "teleport", false, 5);
            setUp(4, "target", false, 5);
    }

    public void getTileImage8bit(){
        setUp(0, "floor", false, 1);
        setUp(1, "wall", true, 1);
        setUp(2, "shaded", false, 1);
        setUp(3, "teleport", false, 5);
        setUp(4, "target", false, 5);
    }

    public void setUp(int index, String imageName, boolean collision, int size){
        UtilityTool toolU = new UtilityTool();
        try{
            BufferedImage temp = ImageIO.read(getClass().getResource("/resources/bit8/tiles/" + imageName + ".png"));
            temp = toolU.scaleImage(temp, gamePanel.getTileSize()*size, gamePanel.getTileSize()*size);
            tile[index] = new Tile(collision, temp);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g){
        //draw floor
        for(int i = 0; i< gamePanel.scenario.getMapHeight(); i++){
            for(int j = 0; j< gamePanel.scenario.getMapWidth(); j++){
                g.drawImage(tile[0].image,
                        j*gamePanel.getTileSize(),
                        i*gamePanel.getTileSize(),
                        null);
            }
        }

        //draw teleport
        for(int i  = 0; i< gamePanel.scenario.getTeleportals().size(); i++){
            g.drawImage(tile[3].image,
                    gamePanel.scenario.getTeleportals().get(i).getLeftBoundary()*gamePanel.getTileSize(),
                    gamePanel.scenario.getTeleportals().get(i).getBottomBoundary()*gamePanel.getTileSize(),
                    null);
        }

        //draw walls
        for(int i = 0; i < gamePanel.scenario.getWalls().size(); i++){
            int upper = gamePanel.scenario.getWalls().get(i).getTopBoundary();
            int lower = gamePanel.scenario.getWalls().get(i).getBottomBoundary();
            int left = gamePanel.scenario.getWalls().get(i).getLeftBoundary();
            int right = gamePanel.scenario.getWalls().get(i).getRightBoundary();

            for(int j = lower; j < upper; j++){
                for (int k = left; k < right; k++){
                    g.drawImage(tile[1].image,
                            k*gamePanel.getTileSize(),
                            j*gamePanel.getTileSize(),
                            null);
                }
            }
        }

        //shaded area
        for(int i = 0; i < gamePanel.scenario.getShaded().size(); i++){
            int upper = gamePanel.scenario.getShaded().get(i).getTopBoundary();
            int lower = gamePanel.scenario.getShaded().get(i).getBottomBoundary();
            int left = gamePanel.scenario.getShaded().get(i).getLeftBoundary();
            int right = gamePanel.scenario.getShaded().get(i).getRightBoundary();

            for(int j = lower; j < upper; j++){
                for (int k = left; k < right; k++){
                    g.drawImage(tile[2].image,
                            k*gamePanel.getTileSize(),
                            j*gamePanel.getTileSize(),
                            null);
                }
            }
        }

        //draw target area
        g.drawImage(tile[4].image,
                gamePanel.scenario.getTargetArea().getLeftBoundary()*gamePanel.getTileSize(),
                gamePanel.scenario.getTargetArea().getBottomBoundary()*gamePanel.getTileSize(),
                null);
    }
}
