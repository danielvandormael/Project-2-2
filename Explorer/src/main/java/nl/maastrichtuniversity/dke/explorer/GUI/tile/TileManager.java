package nl.maastrichtuniversity.dke.explorer.GUI.tile;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

public class TileManager {

    Tile[] tile;

    GamePanel gamePanel;

    public TileManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        tile = new Tile[5];
        getTileImage16bit();
    }

    public void getTileImage16bit(){
        try{
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\16bit\\objects\\floor.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\16bit\\objects\\wall.png"));

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\16bit\\objects\\teleport.png"));

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\16bit\\objects\\shaded.png"));

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\16bit\\objects\\target.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void getTileImage8bit(){
        try{
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\objects\\floor.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\objects\\wall.png"));

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\objects\\teleport.png"));

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\objects\\shaded.png"));

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(new FileInputStream("C:\\Users\\danie\\Projects\\Project-2-2\\Explorer\\src\\main\\resources\\objects\\target.png"));
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
            g.drawImage(tile[2].image, gamePanel.scenario.getTeleportals().get(i).getLeftBoundary()*gamePanel.getTileSize(), gamePanel.scenario.getTeleportals().get(i).getBottomBoundary()*gamePanel.getTileSize(), gamePanel.getTileSize()*5, gamePanel.getTileSize()*5, null);
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
                    g.drawImage(tile[3].image, k*gamePanel.getTileSize(), j*gamePanel.getTileSize(), gamePanel.getTileSize(), gamePanel.getTileSize(), null);
                }
            }
        }

        //draw target area
        g.drawImage(tile[4].image, gamePanel.scenario.getTargetArea().getLeftBoundary()*gamePanel.getTileSize(), gamePanel.scenario.getTargetArea().getBottomBoundary()*gamePanel.getTileSize(), gamePanel.getTileSize()*5, gamePanel.getTileSize()*5, null);
    }
}
