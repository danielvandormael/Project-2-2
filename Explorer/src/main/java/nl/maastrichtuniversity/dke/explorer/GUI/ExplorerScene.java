package nl.maastrichtuniversity.dke.explorer.GUI;

import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import nl.maastrichtuniversity.dke.explorer.Scenario;

public class ExplorerScene {

    private BorderPane expPane;
    private HBox topBox;
    private GridPane expZone;

    Scenario scene;

    public ExplorerScene() {
        // Empty.
    }

    public void setExplorerScene(int width, int height) {

        expPane = new BorderPane();
        topBox = new HBox(450);
        expZone = new GridPane();

    }

    public void renderScene(){
        String mapD="src/main/resources/maps/testmap.txt";
        scene = new Scenario(mapD);

    }

}
