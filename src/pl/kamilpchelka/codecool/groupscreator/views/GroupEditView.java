package pl.kamilpchelka.codecool.groupscreator.views;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import pl.kamilpchelka.codecool.groupscreator.controllers.Controller;
import pl.kamilpchelka.codecool.groupscreator.entites.CodeCoolClass;
import pl.kamilpchelka.codecool.groupscreator.entites.Student;
import pl.kamilpchelka.codecool.groupscreator.enums.ClassGroup;
import pl.kamilpchelka.codecool.groupscreator.utils.ClassGroupUtils;
import pl.kamilpchelka.codecool.groupscreator.utils.DataManager;

import java.util.ArrayList;
import java.util.List;

public class GroupEditView {

    private List<ContextMenu> contextMenuArrayList = new ArrayList<>();

    public void start() {
        Controller controller = Controller.getInstance();
        ClassGroup classGroup = controller.getClassGroupComboBox().getSelectionModel().getSelectedItem();
        CodeCoolClass codeCoolClass = controller.getClassComboBox().getSelectionModel().getSelectedItem();

        Scene scene = new Scene(new Group());

        Stage stage = new Stage();
        stage.setTitle("Group editor");
        stage.setResizable(false);

        TableView table = new TableView();
        TableColumn<Student, TextFieldTableCell> nameColumn = new TableColumn();
        table.getColumns().addAll(nameColumn);
        nameColumn.prefWidthProperty().bind(table.widthProperty().multiply(1));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.setOnMousePressed(event -> {
            Student student = (Student) table.getSelectionModel().getSelectedItem();
            if (event.getButton() == MouseButton.SECONDARY) {
                final ContextMenu contextMenu = new ContextMenu();
                String menu_item_text = student.getActive() ? "set as un-active" : "set as active";
                final MenuItem item1 = new MenuItem(menu_item_text);
                item1.setOnAction(event1 -> {
                    if (student.getActive()) student.setActive(false);
                    else student.setActive(true);
                    try {
                        DataManager.updateStudentData(student);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ClassGroupUtils.updateTable();
                    table.getItems().setAll(codeCoolClass.getStudentMap().get(classGroup));
                });

                contextMenu.getItems().addAll(item1);
                contextMenuArrayList.forEach(contextMenu1 -> contextMenu1.hide());
                contextMenuArrayList.add(contextMenu);
                contextMenu.show(table, event.getScreenX(), event.getScreenY());
            }
        });
        try {
            table.getItems().setAll(codeCoolClass.getStudentMap().get(classGroup));

        } catch (Exception e) {
            new ErrorView(Alert.AlertType.ERROR, "First elect a group you'd like to modify !");
            return;
        }



        GridPane gridPane = new GridPane();
        gridPane.getChildren().addAll(table);
        GridPane.setHgrow(table, Priority.ALWAYS);
        GridPane.setVgrow(table, Priority.ALWAYS);


        ((Group) scene.getRoot()).getChildren().addAll(gridPane);

        stage.setScene(scene);
        stage.show();
        stage.setOnHiding(event -> Controller.getInstance().getClassGroupComboBox().setDisable(false));
        Controller.getInstance().getClassGroupComboBox().setDisable(true);

    }
}
