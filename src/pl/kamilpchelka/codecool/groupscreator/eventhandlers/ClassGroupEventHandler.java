package pl.kamilpchelka.codecool.groupscreator.eventhandlers;

import javafx.event.Event;
import javafx.event.EventHandler;
import pl.kamilpchelka.codecool.groupscreator.controllers.Controller;
import pl.kamilpchelka.codecool.groupscreator.entites.CodeCoolClass;
import pl.kamilpchelka.codecool.groupscreator.enums.ClassGroup;

import java.util.Optional;

public class ClassGroupEventHandler implements EventHandler {


    @Override
    public void handle(Event event) {
        Controller controller = Controller.getInstance();
        ClassGroup classGroup = controller.getClassGroupComboBox().getSelectionModel().getSelectedItem();
        CodeCoolClass codeCoolClass = controller.getClassComboBox().getSelectionModel().getSelectedItem();
        controller.getNames().getItems().setAll(codeCoolClass.getStudentMap().get(classGroup));
        String selection = controller.getGroupSizeComboBox().getSelectionModel().getSelectedItem();
        Optional<String> optional = Optional.ofNullable(controller.getGroupSizeComboBox().getSelectionModel().getSelectedItem());
        if (optional.isPresent()) controller.getGenerate().setDisable(false);


    }
}
