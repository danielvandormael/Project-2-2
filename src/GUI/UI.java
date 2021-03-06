package GUI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {

    GamePanel gamePanel;
    BufferedImage[] stateBgs;
    Graphics2D g;
    Font arial_40;
    Font maruMonica;
    private int uiHeight;

    public boolean hideMenu;
    public boolean showDesired;
    public int commandNum = 0;

    public UI(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        this.stateBgs = new BufferedImage[3];
        uiHeight = 150;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        hideMenu = false;
        showDesired = false;

        for(int i = 0; i < stateBgs.length; i++) {
            try {
                stateBgs[i] = ImageIO.read(getClass().getResource("/resources/bg" + Integer.toString(i) +".jpg"));
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        InputStream is = getClass().getResourceAsStream("/resources/fonts/x12y16pxMaruMonica.ttf");
        try {
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g){
        this.g = g;
        g.setFont(maruMonica);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.WHITE);

        if(gamePanel.gameState == gamePanel.titleState){
            drawTitleScreen();
        }else if(gamePanel.gameState == gamePanel.playState){
            if(hideMenu == false){
                drawMenu();
            }
        }else if(gamePanel.gameState == gamePanel.guardsWinState){
            drawWin("GUARDS WIN!", new Color(22, 93, 229, 255));
        }else if(gamePanel.gameState == gamePanel.intrudersWinState){
            drawWin("INTRUDERS WIN!", new Color(222, 120, 19, 255));
        }
    }

    public void drawTitleScreen(){

        // BACKGROUND
        g.drawImage(stateBgs[0], 0, 0, gamePanel.maxScreenCol*gamePanel.tileSize, gamePanel.maxScreenRow*gamePanel.tileSize, null);

        //TITLE NAME
        g.setFont(g.getFont().deriveFont(Font.BOLD, 98F));
        String text = "Explorer Simulator 18";
        int x = getXforCenterText(text, gamePanel.screenWidth);
        int y = gamePanel.tileSize*20;

        //SHADOW
        g.setColor(Color.gray);
        g.drawString(text, x+5, y+5);
        //MAIN COLOR
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);

        //ENTITY IMAGE
        x = gamePanel.screenWidth/2 - (gamePanel.tileSize*8);
        y += gamePanel.tileSize*6;
        g.drawImage(gamePanel.entityM.guards[0].animation.up_stand, x, y, gamePanel.tileSize*8, gamePanel.tileSize*8, null);
        x += gamePanel.tileSize*8;
        g.drawImage(gamePanel.entityM.intruders[0].animation.up_stand, x, y, gamePanel.tileSize*8, gamePanel.tileSize*8, null);

        //MENU
        g.setFont(g.getFont().deriveFont(Font.BOLD, 48F));

        text = "NEW GAME";
        x = getXforCenterText(text, gamePanel.screenWidth);
        y += gamePanel.tileSize*18;
        g.drawString(text, x, y);
        if(commandNum == 0){
            g.drawString(">", x-gamePanel.tileSize*4, y);
        }

        text = "QUIT";
        x = getXforCenterText(text, gamePanel.screenWidth);
        y += gamePanel.tileSize*10;
        g.drawString(text, x, y);
        if(commandNum == 1){
            g.drawString(">", x-gamePanel.tileSize*4, y);
        }
    }

    public void drawMenu(){
        //Window
        int x = gamePanel.getTileSize()*2;
        int y = gamePanel.getScreenHeight() - (uiHeight + gamePanel.getTileSize()*4);
        int width = gamePanel.getScreenWidth() - (x*2);
        int height = uiHeight;

        drawSubWindow(x, y, width, height);
        if(commandNum == 0){
            displayIntruders(x, y);
        }else if(commandNum == 1){
            displayGuards(x, y);
        }else if(commandNum == 2){
            displayMarkerInfo(x, y);
        }else{
            displayExtraInfo(x, y);
        }
    }

    public void drawSubWindow(int x , int y, int width, int height){
        int subWidth;
        int subHeight;
        int subX;
        int subY;
        int divisor = 2;
        int lineOutHeight;
        Color c;
        String[] subMenuNames = {"Intruders", "Guards", "Marker Info", "Extra Info"};
        int titleX;
        int titleY;
        for (int i = 0; i < subMenuNames.length; i++) {
            subWidth = gamePanel.getScreenWidth()/10;
            subHeight = uiHeight/7;
            subX = x + 30 + (subWidth + (divisor*2))*i;
            subY =  y - subHeight;
            titleX = subX+15;
            //Highlight active submenu
            if(commandNum == i){
                c = new Color(253, 200, 23, 150);
                subX -= 2*divisor;
                subY -= divisor;
                subHeight += divisor*2;
                lineOutHeight = subHeight-divisor;
                titleY = y - 4;
            }else{
                c = new Color(0,0,0, 150);
                subWidth -= 2*divisor;
                lineOutHeight = subHeight;
                titleY = y - 2;
            }

            g.setColor(c);
            g.fillRoundRect(subX, subY, subWidth, subHeight, 10, 5);

            //outline of options
            c = new Color(255,255,255);
            g.setColor(c);
            g.setStroke(new BasicStroke(2));
            g.drawRoundRect(subX+5, subY+5, subWidth-10, lineOutHeight, 5, 5);

            //String representation
            g.setFont(g.getFont().deriveFont(Font.PLAIN, 15F));
            g.drawString(subMenuNames[i], titleX , titleY);

        }

        c = new Color(0, 0, 0, 150);
        g.setColor(c);
        g.fillRoundRect(x, y, width, height, 35, 35);
        c = new Color(255,255,255);
        g.setColor(c);
        g.setStroke(new BasicStroke(5));
        g.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public void displayIntruders(int x , int y){
        int padding = 4;
        int leftBound = x;
        int rightBound = x + 160;
        int lowerBound = y + padding*4;
        int upperBound = lowerBound + (uiHeight-padding*8);
        for (int i = 0; i < gamePanel.entityM.intruders.length; i++) {
           //DIVIDER
            g.setStroke(new BasicStroke(2));
            g.setColor(new Color(113, 113, 113, 200));
            if(i != gamePanel.entityM.intruders.length-1){
                g.drawLine(rightBound, lowerBound, rightBound, upperBound);
            }

            //ENTITY IMAGE
            g.setColor(new Color(60, 63, 65, 150));
            g.fillRoundRect(leftBound + padding*3, lowerBound + padding, gamePanel.tileSize * 5, gamePanel.tileSize * 5, 5, 5);
            g.drawImage(gamePanel.entityM.intruders[0].animation.up_stand, leftBound + padding*3, lowerBound + padding, gamePanel.tileSize * 5, gamePanel.tileSize * 5, null);

            //ID
            g.setFont(g.getFont().deriveFont(Font.BOLD, 34F));
            g.setColor(Color.WHITE);
            String text = "#"+ gamePanel.entityM.intruders[i].getId();
            g.drawString(text, rightBound - padding*20, lowerBound + padding*8);

            //STATS
            g.setFont(g.getFont().deriveFont(Font.PLAIN, 17F));
            int row = leftBound + padding*3;
            int col = lowerBound + padding*15;

            text = "Current Coords : (" + (int) gamePanel.entityM.intruders[i].movement.getX() + ", " + (int) gamePanel.entityM.intruders[i].movement.getY() + ")";
            g.drawString(text, row, col);

            col += padding*5;
            text = "Desired Coords : (" + gamePanel.entityM.intruders[i].desiredX + ", " + gamePanel.entityM.intruders[i].desiredY + ")";
            g.drawString(text, row, col);

            //ELIMINATED
            if(gamePanel.entityM.intruders[i].isEliminated()){
                g.setColor(Color.RED);
                g.drawLine(leftBound, lowerBound, rightBound, upperBound);
                g.drawLine(rightBound, lowerBound, leftBound, upperBound);
            }

            leftBound+=160;
            rightBound+=160;
        }

    }

    public void displayGuards(int x , int y){

        int padding = 4;
        int leftBound = x;
        int rightBound = x + 160;
        int lowerBound = y + padding*4;
        int upperBound = lowerBound + (uiHeight-padding*8);
        for (int i = 0; i < gamePanel.entityM.guards.length; i++) {
            //DIVIDER
            g.setStroke(new BasicStroke(2));
            g.setColor(new Color(113, 113, 113, 200));
            if(i != gamePanel.entityM.guards.length-1){
                g.drawLine(rightBound, lowerBound, rightBound, upperBound);
            }

            //ENTITY IMAGE
            g.setColor(new Color(0, 82, 162, 150));
            g.fillRoundRect(leftBound + padding*3, lowerBound + padding, gamePanel.tileSize * 5, gamePanel.tileSize * 5, 5, 5);
            g.drawImage(gamePanel.entityM.guards[0].animation.up_stand, leftBound + padding*3, lowerBound + padding, gamePanel.tileSize * 5, gamePanel.tileSize * 5, null);

            //ID
            g.setFont(g.getFont().deriveFont(Font.BOLD, 34F));
            g.setColor(Color.WHITE);
            String text = "#"+ gamePanel.entityM.guards[i].getId();
            g.drawString(text, rightBound - padding*20, lowerBound + padding*8);

            //STATS
            g.setFont(g.getFont().deriveFont(Font.PLAIN, 16F));
            int row = leftBound + padding*3;
            int col = lowerBound + padding*15;

            text = "Current Coords : (" + (int) gamePanel.entityM.guards[i].movement.getX() + ", " + (int) gamePanel.entityM.guards[i].movement.getY() + ")";
            g.drawString(text, row, col);

            col += padding*5;
            text = "Desired Coords : (" + gamePanel.entityM.guards[i].desiredX + ", " + gamePanel.entityM.guards[i].desiredY + ")";
            g.drawString(text, row, col);

            leftBound+=160;
            rightBound+=160;
        }
    }

    public void displayMarkerInfo(int x , int y){
        int padding = 4;
        int width = 310;
        int leftBound = x;
        int rightBound = x + width;
        int lowerBound = y + padding*4;
        int upperBound = lowerBound + (uiHeight-padding*8);
        String[] names = {"TIME PHEROMONE", "DEAD END MARKER", "WARNING MARKER"};
        for(int i = 2; i < gamePanel.objectM.objImg.length; i++) {
            //DIVIDER
            g.setStroke(new BasicStroke(2));
            g.setColor(new Color(113, 113, 113, 200));
            if (i != gamePanel.objectM.objImg.length - 1) {
                g.drawLine(rightBound, lowerBound, rightBound, upperBound);
            }

            //TILE IMAGE
            int size = gamePanel.tileSize * 8;
            int tempX = leftBound + (rightBound - leftBound) / 2 - size / 2;
            int tempY = lowerBound;
            g.setColor(new Color(65, 12, 26, 150));
            g.fillRoundRect(tempX - padding, tempY - padding, size + padding * 2, size + padding * 2, 5, 5);
            g.drawImage(gamePanel.objectM.objImg[i], tempX, tempY, size, size, null);

            //TILE NAME
            g.setFont(g.getFont().deriveFont(Font.BOLD, 34F));
            g.setColor(Color.WHITE);
            tempX = getXforCenterText(names[i-2], width) + leftBound;
            tempY += gamePanel.tileSize * 13;
            g.drawString(names[i-2], tempX, tempY);

            leftBound += width;
            rightBound += width;
        }
    }

    public void displayExtraInfo(int x , int y){
        int padding = 4;
        int leftBound = x;
        int rightBound = x + 160;
        int lowerBound = y + padding*4;
        int upperBound = lowerBound + (uiHeight-padding*8);
        String[] names = {"FLOOR", "WALL", "SHADED", "TELEPORT", "TARGET"};
        for (int i = 0; i < gamePanel.tileM.tile.length; i++) {
            //DIVIDER
            g.setStroke(new BasicStroke(2));
            g.setColor(new Color(113, 113, 113, 200));
            if(i != gamePanel.tileM.tile.length-1){
                g.drawLine(rightBound, lowerBound, rightBound, upperBound);
            }


            //TILE IMAGE
            int size = gamePanel.tileSize * 8;
            int tempX = leftBound + (rightBound - leftBound) / 2 - size / 2;
            int tempY = lowerBound;
            g.setColor(new Color(65, 12, 26, 150));
            g.fillRoundRect(tempX - padding, tempY - padding, size + padding * 2, size + padding * 2, 5, 5);
            g.drawImage(gamePanel.tileM.tile[i].image, tempX, tempY, size, size, null);

            //TILE NAME
            g.setFont(g.getFont().deriveFont(Font.BOLD, 34F));
            g.setColor(Color.WHITE);
            tempX = getXforCenterText(names[i], 160) + leftBound;
            tempY += gamePanel.tileSize * 13;
            g.drawString(names[i], tempX, tempY);

            leftBound += 160;
            rightBound += 160;
        }
    }

    public void drawWin(String text, Color color){

        // BACKGROUND
        if(text == "GUARDS WIN!") {
            g.drawImage(stateBgs[1], 0, 0, gamePanel.maxScreenCol*gamePanel.tileSize, gamePanel.maxScreenRow*gamePanel.tileSize, null);
        } else {
            g.drawImage(stateBgs[2], 0, 0, gamePanel.maxScreenCol*gamePanel.tileSize, gamePanel.maxScreenRow*gamePanel.tileSize, null);
        }

        g.setColor(color);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 80F));

        int x = getXforCenterText(text, gamePanel.screenWidth);
        int y = 150;

        g.drawString(text, x, y);

        if(text == "GUARDS WIN!") {
            g.setColor(new Color(255, 255, 255, 255));
        } else {
            g.setColor(Color.BLACK);
        }
        g.setFont(g.getFont().deriveFont(Font.BOLD, 48F));


        text = "NEW GAME";
        x = getXforCenterText(text, gamePanel.screenWidth);
        y += gamePanel.tileSize*18;
        g.drawString(text, x, y);
        if(commandNum == 0){
            g.drawString(">", x-gamePanel.tileSize*4, y);
        }

        text = "QUIT";
        x = getXforCenterText(text, gamePanel.screenWidth);
        y += gamePanel.tileSize*10;
        g.drawString(text, x, y);
        if(commandNum == 1){
            g.drawString(">", x-gamePanel.tileSize*4, y);
        }
    }

    public int getXforCenterText(String text, int width){
        int length = (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
        int x = width/2 - length/2;
        return x;
    }

}
