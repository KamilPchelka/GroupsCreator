package pl.kamilpchelka.codecool.groupscreator.utils;

import pl.kamilpchelka.codecool.groupscreator.controllers.Controller;
import pl.kamilpchelka.codecool.groupscreator.entites.CodeCoolClass;
import pl.kamilpchelka.codecool.groupscreator.entites.Student;
import pl.kamilpchelka.codecool.groupscreator.enums.ClassGroup;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClassGroupUtils {
    private static Controller controller = Controller.getInstance();

    public static List<Student> getActiveUsers() {

        Predicate<Student> isStudentActive = student -> student.getActive();
        CodeCoolClass codeCoolClass = controller.getClassComboBox().getSelectionModel().getSelectedItem();

        ClassGroup classGroup = controller.getClassGroupComboBox().getSelectionModel().getSelectedItem();
        List<Student> studentList = codeCoolClass.getStudentMap().get(classGroup);

        return studentList.stream().
                filter(isStudentActive).
                collect(Collectors.toList());
    }

    public static void updateTable() {

        controller.getNames().getItems().setAll(ClassGroupUtils.getActiveUsers());
    }
}
