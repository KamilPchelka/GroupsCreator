package pl.kamilpchelka.codecool.groupscreator.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pl.kamilpchelka.codecool.groupscreator.controllers.Controller;
import pl.kamilpchelka.codecool.groupscreator.entites.CodeCoolClass;
import pl.kamilpchelka.codecool.groupscreator.entites.Student;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;

public class DataManager {
    private static final String DATA_FILE_PATH = "classes.xml";
    private static Document rootDocument;


    public static void loadData(Controller controller) throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = new File(DATA_FILE_PATH);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        rootDocument = dBuilder.parse(xmlFile);
        loadCodeCoolClasses(controller);

    }

    public static void saveData() throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource source = new DOMSource(rootDocument);
        StreamResult result = new StreamResult(new File(DATA_FILE_PATH ));
        transformer.transform(source, result);

    }

    private static void loadCodeCoolClasses(Controller controller){
        NodeList class_list = rootDocument.getDocumentElement().getChildNodes();
        for (int i = 0; i < class_list.getLength(); i++) {
            String className = class_list.item(i).getNodeName();
            if (class_list.item(i).getNodeType() == Node.ELEMENT_NODE) {
                new CodeCoolClass(className, controller, class_list.item(i).getChildNodes());
            }

        }
    }

    public static void updateStudentData(Student student) {
        String studentName = student.getName();
        String className = student.getClassName();
        String classGroup = student.getClassGroup().toString();
        String programmingLevel = student.getProgrammingLevelValue();
        NodeList nodeList = rootDocument.getElementsByTagName("student");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element studentElement = (Element) nodeList.item(i);
            if (studentElement.getAttribute("name").equalsIgnoreCase(studentName)) {
                studentElement.setAttribute("programminglevel", programmingLevel);
                try {
                    saveData();
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
                return;
            }

        }
    }
}
