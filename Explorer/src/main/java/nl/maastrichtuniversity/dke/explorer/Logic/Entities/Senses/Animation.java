package nl.maastrichtuniversity.dke.explorer.Logic.Entities.Senses;

import nl.maastrichtuniversity.dke.explorer.Logic.Entities.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Animation {
    public BufferedImage left_stand, left_walk, right_stand, right_walk, down_stand, down_walk1, down_walk2, up_stand, up_walk1, up_walk2;
    public BufferedImage hearImg, shoutImg, arrowImg;
    String direction;
    int picSprite;
    int picSpriteCounter;


    Entity entity;

    public Animation(Entity entity) {
        this.direction = "down";
        this.picSprite = 1;
        this.picSpriteCounter = 0;
        this.entity = entity;

        getSenseImage();
    }

    public void update(){
        if(entity.getActionMove() > 0){
            if(picSprite == 0){
                picSprite = 2;
            }
            picSpriteCounter++;
            if(picSpriteCounter > 12){
                if(picSprite == 1){
                    picSprite = 2;
                }else if(picSprite == 2){
                    picSprite = 1;
                }
                picSpriteCounter = 0;
            }
        }else{
            picSprite = 0;
        }
    }

    public void draw(Graphics2D g){
        g.drawImage(getImage(), (int) (entity.movement.getX()*entity.gamePanel.getTileSize() - entity.gamePanel.getTileSize()),
                (int) (entity.movement.getY()*entity.gamePanel.getTileSize() - entity.gamePanel.getTileSize()),
                entity.gamePanel.getTileSize()*2,
                entity.gamePanel.getTileSize()*2, null);

        if(entity.soundSense.isWalkSound() == true || entity.soundSense.isShoutSound() == true){
            g.drawImage(shoutImg,
                    (int) (entity.movement.getX()*entity.gamePanel.getTileSize()),
                    (int) (entity.movement.getY()*entity.gamePanel.getTileSize() - entity.gamePanel.getTileSize()*2),
                    entity.gamePanel.getTileSize(),
                    entity.gamePanel.getTileSize(),
                    null);
        }

        if(entity.soundSense.getSoundsSize() > 0){
            g.drawImage(hearImg,
                    (int) (entity.movement.getX()*entity.gamePanel.getTileSize() - entity.gamePanel.getTileSize()),
                    (int) (entity.movement.getY()*entity.gamePanel.getTileSize() - entity.gamePanel.getTileSize()*2),
                    entity.gamePanel.getTileSize(),
                    entity.gamePanel.getTileSize(),
                    null);
        }
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    //sprite of entities
    private BufferedImage getImage(){

        BufferedImage image = null;

        switch (direction){
            case "left":
                if(picSprite == 1 || picSprite == 0){
                    image = left_stand;
                }else if(picSprite == 2){
                    image = left_walk;
                }
                break;
            case "right":
                if(picSprite == 1 || picSprite == 0){
                    image = right_stand;
                }else if(picSprite == 2){
                    image = right_walk;
                }
                break;
            case "up":
                if(picSprite == 0){
                    image = up_stand;
                }else if(picSprite == 1){
                    image = up_walk1;
                }else if(picSprite == 2){
                    image = up_walk2;
                }
                break;
            case "down":
                if(picSprite == 0){
                    image = down_stand;
                }else if(picSprite == 1){
                    image = down_walk1;
                }else if(picSprite == 2){
                    image = down_walk2;
                }
                break;
        }
        return image;
    }

    private void getSenseImage(){
        try{
            hearImg = ImageIO.read(Animation.class.getResourceAsStream("/bit8/sense/hear.png"));
            shoutImg = ImageIO.read(Animation.class.getResourceAsStream("/bit8/sense/shout.png"));
            arrowImg = ImageIO.read(Animation.class.getResourceAsStream("/bit8/sense/arrow.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getEntityImages(BufferedImage [] imgs){
        left_stand = imgs[0];
        left_walk = imgs[1];
        right_stand = imgs[2];
        right_walk = imgs[3];
        down_stand = imgs[4];
        down_walk1 = imgs[5];
        down_walk2 = imgs[6];
        up_stand = imgs[7];
        up_walk1 = imgs[8];
        up_walk2 = imgs[9];
    }

    public String getDirection() { return this.direction; }
}
