module Explorer {

    requires javafx.controls;
    requires javafx.fxml;

    // opens GUI to javafx.fxml;
    opens nl.maastrichtuniversity.dke.explorer.GUI to javafx.fxml;

    // exports GUI;
    exports nl.maastrichtuniversity.dke.explorer.GUI;
}