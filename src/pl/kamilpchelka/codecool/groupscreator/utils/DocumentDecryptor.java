package pl.kamilpchelka.codecool.groupscreator.utils;

import com.sun.org.apache.xml.internal.security.encryption.XMLCipher;
import com.sun.org.apache.xml.internal.security.utils.EncryptionConstants;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.crypto.SecretKey;

public class DocumentDecryptor {
    public static Document decryptDocument(Document document, SecretKey secretKey) throws Exception {
        Element encryptedDataElement = (Element) document.
                getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS, EncryptionConstants._TAG_ENCRYPTEDDATA).item(0);
        XMLCipher xmlCipher = XMLCipher.getInstance(XMLCipher.AES_256);
        xmlCipher.init(XMLCipher.DECRYPT_MODE, secretKey);
        xmlCipher.doFinal(document, encryptedDataElement);
        DataManager.setSecretKey(secretKey);
        return document;
    }

    public static void init() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setHeaderText("The classes.xml file is encrypted" + '\n' + "Type a password to decrypt it");
        DialogPane dialogPane = textInputDialog.getDialogPane();
        TextField textField = textInputDialog.getEditor();
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        stage.setOnCloseRequest(event -> event.consume());
        stage.setScene(dialogPane.getScene());
        dialogPane.lookupButton(ButtonType.CANCEL).addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            System.exit(0);
        });
        dialogPane.lookupButton(ButtonType.OK).addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            try {
                decryptDocument(DataManager.getRootDocument(), SecretKeyManager.getSecretKey(textField.getText()));
                stage.close();
            } catch (Exception e) {
                textInputDialog.setContentText("Wrong password!");
                event.consume();

            }
        });
        stage.showAndWait();

    }
}
