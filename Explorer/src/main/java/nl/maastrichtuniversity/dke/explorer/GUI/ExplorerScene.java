package nl.maastrichtuniversity.dke.explorer.GUI;

import javafx.geometry.Insets;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ExplorerScene {

    private BorderPane expPane;
    private HBox topBox;
    private GridPane expZone;

    public ExplorerScene() {
        // Empty.
    }

    public void setExplorerScene() {

        expPane = new BorderPane();
        topBox = new HBox(450);
        expZone = new GridPane();
        //expZone.setPadding(new Insets());

    }
}
