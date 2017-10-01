package pl.kamilpchelka.codecool.groupscreator.eventhandlers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import pl.kamilpchelka.codecool.groupscreator.controllers.Controller;
import pl.kamilpchelka.codecool.groupscreator.entites.CodeCoolClass;
import pl.kamilpchelka.codecool.groupscreator.entites.Student;
import pl.kamilpchelka.codecool.groupscreator.enums.ClassGroup;
import pl.kamilpchelka.codecool.groupscreator.utils.DataManager;

import java.util.*;
import java.util.stream.Collectors;

public class GenerateEventHandler implements EventHandler {
    private boolean duplication = false;
    @Override
    public void handle(Event event) {
        duplication = false;
        Controller controller = Controller.getInstance();
        CodeCoolClass codeCoolClass = controller.getClassComboBox().getSelectionModel().getSelectedItem();
        ClassGroup classGroup = controller.getClassGroupComboBox().getSelectionModel().getSelectedItem();
        List<Student> studentList = new ArrayList<>(codeCoolClass.getStudentMap().get(classGroup));
        studentList = studentList.stream().filter(student -> student.getIsEnabled().isSelected()).collect(Collectors.toList());
        boolean includeProgrammingLevel = controller.getIncludeProgrammingLevel().isSelected();
        boolean preventDuplications = controller.getPreventDuplications().isSelected();
        int groupSize = Integer.valueOf(controller.getGroupSizeComboBox().getSelectionModel().getSelectedItem());
        int classSize = studentList.size();
        int fullGroupNumber = classSize / groupSize;
        int studentsWithoutGroup = classSize % groupSize;
        List<List<Student>> groupsList = new ArrayList<>();
        for (int i = 0; i < fullGroupNumber; i++) {
            Date d1 = new Date();
            while (true) {
                if (duplication) return;
                List<Student> group = new ArrayList<>();
                Collections.shuffle(studentList);
                Student student = studentList.get(0);
                List<Student> tempStudentList = new ArrayList<>(studentList);
                if (includeProgrammingLevel) {
                    tempStudentList.clear();
                    studentList.stream().filter(student1 -> Objects.equals(Integer.valueOf(student.getProgrammingLevelValue()),
                            Integer.valueOf(student1.getProgrammingLevelValue()))).forEach(tempStudentList::add);
                    studentList.stream().filter(student1 -> Integer.valueOf(student.getProgrammingLevelValue())
                            > Integer.valueOf(student1.getProgrammingLevelValue())).forEach(tempStudentList::add);
                    studentList.stream().filter(student1 -> Integer.valueOf(student.getProgrammingLevelValue())
                            > Integer.valueOf(student1.getProgrammingLevelValue())).forEach(tempStudentList::add);
                }
                try {
                    for (int j = 0; j < groupSize; j++) {
                        Student randomStudent = tempStudentList.get(0);
                        group.add(randomStudent);
                        tempStudentList.remove(randomStudent);
                    }

                } catch (Exception e) {
                    continue;
                }
                if (DataManager.isNewGroupDuplicated(group.toString()) && preventDuplications) {
                    Date d2 = new Date();
                    long time_difference = d2.getTime() - d1.getTime();
                    if (time_difference > 100) showCannotGenerateGroupError();
                } else if (!groupsList.contains(group)) {
                    groupsList.add(group);
                    studentList.removeAll(group);
                    DataManager.saveGroupToCache(group.toString());
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
                stringBuilder.append(index + 1).append(". ").append(students.toString()).append('\n');
            }
        });
        alert.setContentText(stringBuilder.toString());
        alert.setResizable(true);
        alert.showAndWait();

    }

    private void showCannotGenerateGroupError() {
        String contextText = "The program used up of all posibilities to create unique groups" + '\n' +
                "You can do one of the following options to get rid off of this error:" + '\n' +
                "- unselect 'Prevent duplications' checbox" + '\n' +
                "- change 'GroupSize' option to another" + '\n' +
                "- purge or delete 'previousgroups.txt' file" + '\n' +
                "Hope it helped :)";
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Generating error");
        alert.setHeaderText("Cannot create unique groups!");
        alert.setContentText(contextText);
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(500, 300);
        alert.showAndWait();
        duplication = true;
    }
}

