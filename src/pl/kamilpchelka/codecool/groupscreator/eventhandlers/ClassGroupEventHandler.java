package pl.kamilpchelka.codecool.groupscreator.eventhandlers;

import javafx.event.Event;
import javafx.event.EventHandler;
import pl.kamilpchelka.codecool.groupscreator.controllers.Controller;
import pl.kamilpchelka.codecool.groupscreator.utils.ClassGroupUtils;

import java.util.Optional;

public class ClassGroupEventHandler implements EventHandler {

    @Override
    public void handle(Event event) {
        Controller controller = Controller.getInstance();

        controller.getNames().getItems().setAll(ClassGroupUtils.getActiveUsers());
        Optional<String> optional = Optional.ofNullable(controller.getGroupSizeComboBox().getSelectionModel().getSelectedItem());
        if (optional.isPresent()) controller.getGenerate().setDisable(false);


    }
}
