package pl.kamilpchelka.codecool.groupscreator.eventhandlers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import pl.kamilpchelka.codecool.groupscreator.controllers.Controller;

public class GroupSizeEventHandler implements EventHandler {

    @Override
    public void handle(Event event) {
        Controller controller = Controller.getInstance();
        Button generate = controller.getGenerate();
        if (generate.isDisabled() && !controller.getNames().getItems().isEmpty()) generate.setDisable(false);
    }
}
