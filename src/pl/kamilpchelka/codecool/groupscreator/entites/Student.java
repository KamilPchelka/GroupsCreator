package pl.kamilpchelka.codecool.groupscreator.entites;


import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import pl.kamilpchelka.codecool.groupscreator.enums.ClassGroup;
import pl.kamilpchelka.codecool.groupscreator.utils.DataManager;

public class Student {

    String name;
    String className;


    ClassGroup classGroup;
    TextField programmingLevel;
    CheckBox isEnabled;


    public Student(String name, String programmingLevel, String className, ClassGroup classGroup) {
        this.name = name;
        this.className = className;
        this.classGroup = classGroup;
        initializeProgrammingLevelTextField(programmingLevel);
        initializeIsEnabledCheckBox();
    }

    public String getClassName() {
        return className;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TextField getProgrammingLevel() {
        return programmingLevel;
    }

    public ClassGroup getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroup classGroup) {
        this.classGroup = classGroup;
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
            else if (!newValue.isEmpty() || !newValue.equalsIgnoreCase("NaN"))
                try {
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
        return getName() + " " + getProgrammingLevelValue();
    }

    public boolean compare(Student student, String operator) {
        int programmminglevel1 = Integer.valueOf(getProgrammingLevelValue());
        int programmminglevel2 = Integer.valueOf(student.getProgrammingLevelValue());

        if (programmminglevel1 == programmminglevel2 && operator.equalsIgnoreCase("==")) return true;
        else if (programmminglevel1 > programmminglevel2 && operator.equalsIgnoreCase(">")) return true;
        else if (programmminglevel1 < programmminglevel2 && operator.equalsIgnoreCase("<")) return true;

        return false;

    }
}
