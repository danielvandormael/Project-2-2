package Logic.Entities.Senses;

import GUI.UtilityTool;
import Logic.Entities.Entity;
import Logic.Tiles.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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
        //Draw Entity
        g.drawImage(getImage(), (int) (entity.movement.getX()*entity.gamePanel.getTileSize() - entity.gamePanel.getTileSize()),
                (int) (entity.movement.getY()*entity.gamePanel.getTileSize() - entity.gamePanel.getTileSize()),
                null);

        //Draw icon if making noise
        if(entity.soundSense.isWalkSound() == true || entity.soundSense.isShoutSound() == true){
            g.drawImage(shoutImg,
                    (int) (entity.movement.getX()*entity.gamePanel.getTileSize()),
                    (int) (entity.movement.getY()*entity.gamePanel.getTileSize() - entity.gamePanel.getTileSize()*2),
                    null);
        }

        //Draw icon if can hear someone else
        if(entity.soundSense.getSoundsSize() > 0){
            g.drawImage(hearImg,
                    (int) (entity.movement.getX()*entity.gamePanel.getTileSize() - entity.gamePanel.getTileSize()),
                    (int) (entity.movement.getY()*entity.gamePanel.getTileSize() - entity.gamePanel.getTileSize()*2),
                    null);
        }

        //Draw Desired X and Y
        if(entity.gamePanel.ui.showDesired == true){
            g.setColor(new Color(47, 59, 34, 200));
            g.drawRect(entity.desiredX*entity.gamePanel.tileSize, entity.desiredY*entity.gamePanel.tileSize, entity.gamePanel.tileSize, entity.gamePanel.tileSize);
            g.drawLine((int)(entity.movement.getX()*entity.gamePanel.tileSize), (int) (entity.movement.getY()*entity.gamePanel.tileSize), entity.desiredX*entity.gamePanel.tileSize, entity.desiredY*entity.gamePanel.tileSize);
        }
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
        hearImg = setUp("hear");
        shoutImg = setUp("shout");
        arrowImg = setUp("arrow");
    }

    public BufferedImage setUp(String imageName){
        UtilityTool toolU = new UtilityTool();
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResource("/resources/bit8/sense/" + imageName +".png"));
            image = toolU.scaleImage(image, entity.gamePanel.getTileSize(), entity.gamePanel.getTileSize());
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
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

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
