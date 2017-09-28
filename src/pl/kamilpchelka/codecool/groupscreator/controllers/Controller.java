package pl.kamilpchelka.codecool.groupscreator.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import pl.kamilpchelka.codecool.groupscreator.entites.CodeCoolClass;
import pl.kamilpchelka.codecool.groupscreator.entites.Student;
import pl.kamilpchelka.codecool.groupscreator.enums.ClassGroup;
import pl.kamilpchelka.codecool.groupscreator.eventhandlers.ClassEventHandler;
import pl.kamilpchelka.codecool.groupscreator.eventhandlers.ClassGroupEventHandler;
import pl.kamilpchelka.codecool.groupscreator.eventhandlers.GenerateEventHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private static final int MINIMUM_GROUP_SIZE = 2;

    private static final Integer MAXIMUM_GROUP_SIZE = 100;

    private static Controller instance = null;
    @FXML
    private ComboBox<String> groupSizeComboBox;

    @FXML
    private ComboBox<ClassGroup> classGroupComboBox;

    @FXML
    private ComboBox<CodeCoolClass> classComboBox;

    @FXML
    private TableView names;

    @FXML
    private Button generate;


    protected Controller() {
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    @FXML
    public void initializeSelectClassComboBox(CodeCoolClass codeCoolClass) {
        classComboBox.getItems().add(codeCoolClass);
    }

    @FXML
    private void initializeSelectGroupSizeComboBox() {
        ObservableList<String> data = FXCollections.observableArrayList();
        for (int i = MINIMUM_GROUP_SIZE; i <= MAXIMUM_GROUP_SIZE; i++) {
            data.add(String.valueOf(i));
        }
        groupSizeComboBox.setItems(data);
    }

    @FXML
    private void initializeNamesTableView() {
        names.setEditable(true);
        names.setPlaceholder(new Label("Please select a class first"));
        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Student, String> isStudentEnabledColumn = new TableColumn<>("Enabled");
        isStudentEnabledColumn.setMinWidth(100);
        isStudentEnabledColumn.setCellValueFactory(new PropertyValueFactory<>("isEnabled"));

        TableColumn<Student, TextFieldTableCell> programmingLevelColumn = new TableColumn<>("Programming Level");
        programmingLevelColumn.setMinWidth(200);
        programmingLevelColumn.setCellValueFactory(new PropertyValueFactory<>("programmingLevel"));
        names.getColumns().addAll(nameColumn, programmingLevelColumn, isStudentEnabledColumn);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSelectGroupSizeComboBox();
        initializeNamesTableView();
        classGroupComboBox.setOnAction(new ClassGroupEventHandler()::handle);
        classComboBox.setOnAction(new ClassEventHandler()::handle);
        generate.setOnAction(new GenerateEventHandler()::handle);


    }

    public ComboBox getGroupSizeComboBox() {
        return groupSizeComboBox;
    }

    public ComboBox<ClassGroup> getClassGroupComboBox() {
        return classGroupComboBox;
    }

    public ComboBox<CodeCoolClass> getClassComboBox() {
        return classComboBox;
    }

    public TableView getNames() {
        return names;
    }

    public Button getGenerate() {
        return generate;
    }
}
