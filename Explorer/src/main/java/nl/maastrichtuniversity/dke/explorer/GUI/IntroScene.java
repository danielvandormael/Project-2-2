package nl.maastrichtuniversity.dke.explorer.GUI;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public class IntroScene extends GUIMain {

    private Scene introScene, expScene;
    private StackPane beginPane;
    private Label introLabel;
    private VBox introBox;
    private Button beginButton, exitButton;

    public IntroScene() {
        // Empty.
    }

    public void setIntroScene() {

        beginPane = new StackPane();

        introScene = new Scene(beginPane, screenBounds.getWidth(), screenBounds.getHeight());
        introScene.getStylesheets().clear();
        introScene.getStylesheets().add("/Stylesheet.css");

        introBox = new VBox(screenBounds.getHeight()/17.3);
        introLabel = new Label("EXPLORER SIMULATOR XVIII");
        introLabel.getStyleClass().add("introLabel");
        beginButton = new Button("Begin!");
        beginButton.getStyleClass().add("beginButton");
        exitButton = new Button("Exit");
        exitButton.getStyleClass().add("exitButton");

        introBox.getChildren().addAll(introLabel, beginButton, exitButton);

        beginButton.setPrefSize(screenBounds.getWidth()/6.1, screenBounds.getHeight()/11.5);
        exitButton.setPrefSize(screenBounds.getWidth()/6.1,screenBounds.getHeight()/11.5);

        setButtonActions();

        introBox.setAlignment(Pos.CENTER);
        beginPane.getChildren().add(introBox);
    }

    private void setButtonActions() {

        beginButton.setOnAction(e -> {
            mainStage.setFullScreen(true);
            mainStage.setResizable(false);
            mainStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            mainStage.close();

            try {
                expSc.setExplorerFrame();
            } catch (FileNotFoundException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        });

        exitButton.setOnAction(e -> { System.exit(0); });

    }

    public Scene getIntroScene() { return introScene; }

}
