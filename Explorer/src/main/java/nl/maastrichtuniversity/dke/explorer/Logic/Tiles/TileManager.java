package nl.maastrichtuniversity.dke.explorer.Logic.Tiles;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

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

        // TODO: REMOVE - just to see the tile map
        for(int i = 0; i < mapTile.length; i++) {
            for(int j = 0; j < mapTile[i].length; j++) {
                System.out.print(mapTile[i][j] + "");
            }
            System.out.println();
        }

    }

    public void getTileImage16bit(){
        try{
            //floor
            tile[0] = new Tile(false, ImageIO.read(getClass().getResource("/resources/bit16/tiles/floor.png")));

            //wall
            tile[1] = new Tile(true, ImageIO.read(getClass().getResource("/resources/bit16/tiles/wall.png")));

            //shaded
            tile[2] = new Tile(false, ImageIO.read(getClass().getResource("/resources/bit16/tiles/shaded.png")));

            //teleport
            tile[3] = new Tile(false, ImageIO.read(getClass().getResource("/resources/bit16/tiles/teleport.png")));

            //target
            tile[4] = new Tile(false, ImageIO.read(getClass().getResource("/resources/bit16/tiles/target.png")));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void getTileImage8bit(){
        try{
            tile[0] = new Tile(false, ImageIO.read(TileManager.class.getResourceAsStream("/bit8/tiles/floor.png")));

            tile[1] = new Tile(true, ImageIO.read(TileManager.class.getResourceAsStream("/bit8/tiles/wall.png")));

            tile[2] = new Tile(false, ImageIO.read(TileManager.class.getResourceAsStream("/bit8/tiles/shaded.png")));

            tile[3] = new Tile(false, ImageIO.read(TileManager.class.getResourceAsStream("/bit8/tiles/teleport.png")));

            tile[4] = new Tile(false, ImageIO.read(TileManager.class.getResourceAsStream("/bit8/tiles/target.png")));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g){
        //draw floor
        for(int i = 0; i< gamePanel.scenario.getMapHeight(); i++){
            for(int j = 0; j< gamePanel.scenario.getMapWidth(); j++){
                g.drawImage(tile[0].image, j*gamePanel.getTileSize(), i*gamePanel.getTileSize(), gamePanel.getTileSize(), gamePanel.getTileSize(), null);
            }
        }

        //draw portal
        for(int i  = 0; i< gamePanel.scenario.getTeleportals().size(); i++){
            g.drawImage(tile[3].image, gamePanel.scenario.getTeleportals().get(i).getLeftBoundary()*gamePanel.getTileSize(), gamePanel.scenario.getTeleportals().get(i).getBottomBoundary()*gamePanel.getTileSize(), gamePanel.getTileSize()*5, gamePanel.getTileSize()*5, null);
        }

        //draw walls
        for(int i = 0; i < gamePanel.scenario.getWalls().size(); i++){
            int upper = gamePanel.scenario.getWalls().get(i).getTopBoundary();
            int lower = gamePanel.scenario.getWalls().get(i).getBottomBoundary();
            int left = gamePanel.scenario.getWalls().get(i).getLeftBoundary();
            int right = gamePanel.scenario.getWalls().get(i).getRightBoundary();

            for(int j = lower; j < upper; j++){
                for (int k = left; k < right; k++){
                    g.drawImage(tile[1].image, k*gamePanel.getTileSize(), j*gamePanel.getTileSize(), gamePanel.getTileSize(), gamePanel.getTileSize(), null);
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
                    g.drawImage(tile[2].image, k*gamePanel.getTileSize(), j*gamePanel.getTileSize(), gamePanel.getTileSize(), gamePanel.getTileSize(), null);
                }
            }
        }

        //draw target area
        g.drawImage(tile[4].image, gamePanel.scenario.getTargetArea().getLeftBoundary()*gamePanel.getTileSize(), gamePanel.scenario.getTargetArea().getBottomBoundary()*gamePanel.getTileSize(), gamePanel.getTileSize()*5, gamePanel.getTileSize()*5, null);
    }
}