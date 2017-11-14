package pl.kamilpchelka.codecool.groupscreator.entites;


import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pl.kamilpchelka.codecool.groupscreator.utils.DataManager;

public class Student {

    private String name;
    public boolean isActive = true;


    private TextField programmingLevel;
    private CheckBox isEnabled;


    public Student(String name, String programmingLevel, String isActive) {
        this.name = name;

        if (isActive.isEmpty()) this.isActive = true;
        else this.isActive = Boolean.parseBoolean(isActive);
        initializeProgrammingLevelTextField(programmingLevel);
        initializeIsEnabledCheckBox();
    }

    public boolean getActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Text getName() {
        Text text = new Text(name);
        if (!isActive) text.setStrikethrough(true);
        return text;
    }

    public TextField getProgrammingLevel() {
        return programmingLevel;
    }

    public String getProgrammingLevelValue() {
        return programmingLevel.getText();
    }

    public void setProgrammingLevel(String programmingLevel) {
        this.programmingLevel.setText(programmingLevel);
    }

    public CheckBox getIsEnabled() {
        return isEnabled;
    }

    public static boolean isNewProgrammingLevelPropertyCorrect(String str) {
        if (str.isEmpty() || str.equalsIgnoreCase("NaN"))
            return true;
        try {
            int d = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void initializeProgrammingLevelTextField(String programmingLevel) {
        this.programmingLevel = new TextField(programmingLevel);
        this.programmingLevel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isNewProgrammingLevelPropertyCorrect(newValue))
                setProgrammingLevel(oldValue);
            else try {
                DataManager.updateStudentData(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (newValue.equalsIgnoreCase("NaN")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Inappropriate programming level property");
                alert.setContentText("The Programming Level field cannot be empty!");
                alert.showAndWait();
            }
        });
        this.programmingLevel.focusedProperty().addListener((observable, oldValue, newValue) -> {
            String fieldText = getProgrammingLevel().getText();
            if (newValue)
                if (fieldText.equalsIgnoreCase("NaN"))
                    setProgrammingLevel("");
            if (oldValue)
                if (fieldText.equals(""))
                    setProgrammingLevel("0");
        });
    }

    private void initializeIsEnabledCheckBox() {
        this.isEnabled = new CheckBox();
        this.isEnabled.setSelected(true);
    }

    @Override
    public String toString() {
        return getName().getText() + " " + getProgrammingLevelValue();
    }

}
