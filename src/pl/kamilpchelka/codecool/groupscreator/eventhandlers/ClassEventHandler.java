package pl.kamilpchelka.codecool.groupscreator.eventhandlers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import pl.kamilpchelka.codecool.groupscreator.controllers.Controller;
import pl.kamilpchelka.codecool.groupscreator.entites.CodeCoolClass;

public class ClassEventHandler implements EventHandler {

    @Override
    public void handle(Event event) {
        Controller controller = Controller.getInstance();
        ComboBox classGroup = controller.getClassGroupComboBox();
        CodeCoolClass codeCoolClass = controller.getClassComboBox().getSelectionModel().getSelectedItem();
        controller.getNames().setPlaceholder(new Label("Now select a class group"));
        if (classGroup.getItems().isEmpty())
            classGroup.getItems().addAll(codeCoolClass.getClassGroupsList());
    }
}
