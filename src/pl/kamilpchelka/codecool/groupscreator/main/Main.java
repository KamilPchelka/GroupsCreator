package pl.kamilpchelka.codecool.groupscreator.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.kamilpchelka.codecool.groupscreator.controllers.Controller;
import pl.kamilpchelka.codecool.groupscreator.utils.DataManager;

public class Main extends Application {

    private static final String WINDOW_TITLE = "Groups generator";
    private static final boolean IS_WINDOW_RESIZABLE = false;
    private static final String DATA_FILE_PATH = "classes.xml";
    private static final String INTERFACE_DESIGN_PATH = "groupsgenerator.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(INTERFACE_DESIGN_PATH));
        Controller controller = new Controller();
        loader.setController(controller);
        Parent root = loader.load();
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(IS_WINDOW_RESIZABLE);
        primaryStage.show();
        DataManager.loadData(controller);

    }



    public static void main(String[] args) {
        launch(args);
    }
}
