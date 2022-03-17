package nl.maastrichtuniversity.dke.explorer.GUI;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GUIMain extends Application {

    protected static Stage mainStage;
    protected static IntroScene introSc;
    protected static ExplorerScene expSc;
    protected static Rectangle2D screenBounds;

    public GUIMain(String[] args) {
        Application.launch(args);
    }

    public GUIMain(){
        // Empty
    }

    @Override
    public void start(Stage primaryStage) {

        mainStage = primaryStage;

        screenBounds = Screen.getPrimary().getBounds();

        introSc = new IntroScene();
        expSc = new ExplorerScene();
        introSc.setIntroScene();

        mainStage.setScene(introSc.getIntroScene());

        mainStage.setTitle("Explorer Simulator 18");
        mainStage.getIcons().add(new Image(GUIMain.class.getResourceAsStream("/logo.png")));
        mainStage.setFullScreen(true);
        mainStage.setResizable(false);
        mainStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        mainStage.show();
    }
}
