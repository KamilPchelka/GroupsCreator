package pl.kamilpchelka.codecool.groupscreator.controllers;

import com.sun.org.apache.bcel.internal.classfile.Code;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import pl.kamilpchelka.codecool.groupscreator.entites.CodeCoolClass;
import pl.kamilpchelka.codecool.groupscreator.entites.Student;
import pl.kamilpchelka.codecool.groupscreator.enums.ClassGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public static final int MINIMUM_GROUP_SIZE = 2;
    public static final Integer MAXIMUM_GROUP_SIZE = 100;

    @FXML
    private ComboBox GroupSizeComboBox, ClassGroupComboBox;

    @FXML
    private ComboBox<CodeCoolClass> ClassComboBox;

    @FXML
    private TableView names;

    @FXML
    public void initializeSelectClassComboBox(CodeCoolClass codeCoolClass) {
        ClassComboBox.getItems().add(codeCoolClass);
    }

    @FXML
    private void initializeSelectGroupSizeComboBox() {
        ObservableList<String> data = FXCollections.observableArrayList();
        for (int i = MINIMUM_GROUP_SIZE; i <= MAXIMUM_GROUP_SIZE; i++) {
            data.add(String.valueOf(i));
        }
        GroupSizeComboBox.setItems(data);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSelectGroupSizeComboBox();
        names.setEditable(true);
        names.setPlaceholder(new Label("Please select a class first"));
        ClassGroupComboBox.setOnAction(e -> {

            ClassGroup classGroup = (ClassGroup) ClassGroupComboBox.getSelectionModel().getSelectedItem();
            CodeCoolClass codeCoolClass = ClassComboBox.getSelectionModel().getSelectedItem();
            names.getItems().setAll(codeCoolClass.getStudentMap().get(classGroup));

        });
        ClassComboBox.setOnAction(e -> {
            CodeCoolClass codeCoolClass = (CodeCoolClass) ClassComboBox.getSelectionModel().getSelectedItem();
            names.setPlaceholder(new Label("Now select a class group"));
           if (ClassGroupComboBox.getItems().isEmpty())
               ClassGroupComboBox.getItems().addAll(codeCoolClass.getClassGroupsList());
        });
        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Student, String> isStudentEnabledColumn = new TableColumn<>("Enabled");
        isStudentEnabledColumn.setMinWidth(200);
        isStudentEnabledColumn.setCellValueFactory(new PropertyValueFactory<>("isEnabled"));

        TableColumn<Student, TextFieldTableCell> programmingLevelColumn = new TableColumn<>("Programming Level");
        programmingLevelColumn.setMinWidth(200);
        programmingLevelColumn.setCellValueFactory(new PropertyValueFactory<>("programmingLevel"));



        names.getColumns().addAll(nameColumn, programmingLevelColumn, isStudentEnabledColumn);
    }
}
