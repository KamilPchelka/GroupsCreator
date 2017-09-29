package pl.kamilpchelka.codecool.groupscreator.eventhandlers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import pl.kamilpchelka.codecool.groupscreator.controllers.Controller;
import pl.kamilpchelka.codecool.groupscreator.entites.CodeCoolClass;
import pl.kamilpchelka.codecool.groupscreator.entites.Student;
import pl.kamilpchelka.codecool.groupscreator.enums.ClassGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateEventHandler implements EventHandler {

    @Override
    public void handle(Event event) {
        Controller controller = Controller.getInstance();
        CodeCoolClass codeCoolClass = controller.getClassComboBox().getSelectionModel().getSelectedItem();
        ClassGroup classGroup = controller.getClassGroupComboBox().getSelectionModel().getSelectedItem();
        List<Student> studentList = new ArrayList<>(codeCoolClass.getStudentMap().get(classGroup));
        studentList = studentList.stream().filter(student -> student.getIsEnabled().isSelected()).collect(Collectors.toList());
        boolean includeProgrammingLevel = controller.getIncludeProgrammingLevel().isSelected();
        boolean preventDuplicationsDisabled = controller.getIncludeProgrammingLevel().isDisabled();
        int groupSize = Integer.valueOf(controller.getGroupSizeComboBox().getSelectionModel().getSelectedItem());
        int classSize = studentList.size();
        int fullGroupNumber = classSize / groupSize;
        int studentsWithoutGroup = classSize % groupSize;
        List<List<Student>> groupsList = new ArrayList<>();
        for (int i = 0; i < fullGroupNumber; i++) {
            while (true) {
                List<Student> group = new ArrayList<>();
                Collections.shuffle(studentList);
                Student student = studentList.get(0);
                List<Student> tempStudentList = new ArrayList<>(studentList);
                if (includeProgrammingLevel) {
                    tempStudentList.clear();
                    List<Student> equal = studentList.stream().filter(student1 ->
                            student1.compare(student, "==")).collect(Collectors.toList());
                    List<Student> greater = studentList.stream().filter(student1 ->
                            student1.compare(student, ">")).collect(Collectors.toList());
                    List<Student> less = studentList.stream().filter(student1 ->
                            student1.compare(student, "<")).collect(Collectors.toList());

                    if (!equal.isEmpty()) tempStudentList.addAll(equal);
                    if (!less.isEmpty()) tempStudentList.addAll(less);
                    if (!greater.isEmpty()) tempStudentList.addAll(greater);
                }
                for (int j = 0; j < groupSize; j++) {
                    Student randomStudent = tempStudentList.get(0);
                    group.add(randomStudent);
                    tempStudentList.remove(randomStudent);
                }
                if (!groupsList.contains(group)) {
                    groupsList.add(group);
                    studentList.removeAll(group);
                    break;
                }


            }
        }
        if (studentsWithoutGroup != 0) groupsList.add(studentList);
        printResult(groupsList, studentsWithoutGroup);


    }

    private void printResult(List<List<Student>> groupsList, int studentsWithoutGroup) {
        StringBuilder stringBuilder = new StringBuilder();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Generated groups");
        groupsList.forEach(students -> {
            int index = groupsList.indexOf(students);
            if (students.size() != 0 && students.size() == studentsWithoutGroup) {
                stringBuilder.append("The following students are not assigned to groups:" + '\n');
                stringBuilder.append(students.toString()).append('\n');
            } else {
                stringBuilder.append(index).append(". ").append(students.toString()).append('\n');
            }
        });
        alert.setContentText(stringBuilder.toString());
        alert.setResizable(true);
        alert.showAndWait();

    }
}

