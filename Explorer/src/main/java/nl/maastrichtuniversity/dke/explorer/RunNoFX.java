package nl.maastrichtuniversity.dke.explorer;

import nl.maastrichtuniversity.dke.explorer.GUI.GamePanel;
import nl.maastrichtuniversity.dke.explorer.Logic.Scenario;
import java.io.File;

public class RunNoFX {
    
    public static void main(String[] args) {
        File file = new File("C:/Users/Sam/Documents/DSAI/Year 2/Project 2-2/testmap.txt");
        String absPath = file.getAbsolutePath();
        GamePanel gp = new GamePanel(new Scenario(absPath));
    }
}
