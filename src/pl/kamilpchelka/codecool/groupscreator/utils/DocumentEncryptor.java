package pl.kamilpchelka.codecool.groupscreator.utils;

import com.sun.org.apache.xml.internal.security.encryption.XMLCipher;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.crypto.SecretKey;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class DocumentEncryptor {
    public static void encryptDocument(Document document, SecretKey secretKey) throws Exception {
        Element rootElement = document.getDocumentElement();
        XMLCipher xmlCipher = XMLCipher.getInstance(XMLCipher.AES_256);

        xmlCipher.init(XMLCipher.ENCRYPT_MODE, secretKey);
        xmlCipher.doFinal(document, rootElement, true);
        saveEncryptedDocumentTo(document);
        DataManager.setSecretKey(secretKey);
    }

    public static void init() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setHeaderText("Your file is not encrypted" + '\n' + "Type a password to encrypt it" + '\n' + "(max 16 characters)");
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
            if (!(textField.getText().length() == 0) && !(textField.getText().length() > 16)) {
                stage.close();
                String password = textField.getText();
                try {
                    encryptDocument(DataManager.getRootDocument(), SecretKeyManager.getSecretKey(password));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            event.consume();
        });
        stage.showAndWait();

    }

    public static void saveEncryptedDocumentTo(Document document)
            throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(DataManager.DATA_FILE_PATH));
        transformer.transform(source, result);
    }


}

