package pl.kamilpchelka.codecool.groupscreator.views;

import javafx.scene.control.Alert;

public class ErrorView extends Alert {

    /**
     * Creates an alert with the given AlertType (refer to the {@link AlertType}
     * documentation for clarification over which one is most appropriate).
     * <p>
     * <p>By passing in an AlertType, default values for the
     * {@link #titleProperty() title}, {@link #headerTextProperty() headerText},
     * and {@link #graphicProperty() graphic} properties are set, as well as the
     * relevant {@link #getButtonTypes() buttons} being installed. Once the Alert
     * is instantiated, developers are able to modify the values of the alert as
     * desired.
     * <p>
     * <p>It is important to note that the one property that does not have a
     * default value set, and which therefore the developer must set, is the
     * {@link #contentTextProperty() content text} property (or alternatively,
     * the developer may call {@code alert.getDialogPane().setContent(Node)} if
     * they want a more complex alert). If the contentText (or content) properties
     * are not set, there is no useful information presented to end users.
     *
     * @param alertType
     */
    public ErrorView(AlertType alertType, String contextText) {
        super(alertType);
        this.setTitle("Error");
        this.setContentText(contextText);
        this.setResizable(true);
        this.getDialogPane().setPrefSize(350, 100);
        this.showAndWait();
    }
}
