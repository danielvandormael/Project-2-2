package GUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gamePanel;

    public KeyHandler(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if(gamePanel.gameState == gamePanel.titleState){
            if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
                gamePanel.ui.commandNum--;
                if(gamePanel.ui.commandNum < 0){
                    gamePanel.ui.commandNum = 1;
                }
            }
            if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
                gamePanel.ui.commandNum++;
                if(gamePanel.ui.commandNum > 1){
                    gamePanel.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){
                gamePanel.ui.commandNum--;
                if(gamePanel.ui.commandNum < 0){
                    gamePanel.ui.commandNum = 1;
                }
            }
            if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){
                gamePanel.ui.commandNum++;
                if(gamePanel.ui.commandNum > 1){
                    gamePanel.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER){
                if(gamePanel.ui.commandNum == 0){
                    gamePanel.gameState = gamePanel.playState;
                }
                if(gamePanel.ui.commandNum == 1){
                    System.exit(0);
                }
            }

        }else if(gamePanel.gameState == gamePanel.playState){
            if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
                gamePanel.ui.commandNum--;
                if(gamePanel.ui.commandNum < 0){
                    gamePanel.ui.commandNum = 3;
                }
            }
            if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
                gamePanel.ui.commandNum++;
                if(gamePanel.ui.commandNum > 3){
                    gamePanel.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){
                gamePanel.ui.commandNum--;
                if(gamePanel.ui.commandNum < 0){
                    gamePanel.ui.commandNum = 3;
                }
            }
            if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){
                gamePanel.ui.commandNum++;
                if(gamePanel.ui.commandNum > 3){
                    gamePanel.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ESCAPE){
                if(gamePanel.ui.hideMenu == true){
                    gamePanel.ui.hideMenu= false;
                }else{
                    gamePanel.ui.hideMenu = true;
                }
            }
            if(code == KeyEvent.VK_SPACE){
                if(gamePanel.ui.showDesired == true){
                    gamePanel.ui.showDesired = false;
                }else{
                    gamePanel.ui.showDesired = true;
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
