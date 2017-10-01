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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DataManager {
    private static final String DATA_FILE_PATH = "classes.xml";
    private static Document rootDocument;
    private static List<String> previousGroupsList = new ArrayList<>();
    private static File previousGroupsFile = new File("previousgroups.txt");


    public static void loadData(Controller controller) throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = new File(DATA_FILE_PATH);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        rootDocument = dBuilder.parse(xmlFile);
        loadCodeCoolClasses(controller);
        loadPreviousGroupsCache();

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

    private static void loadPreviousGroupsCache() throws IOException {
        previousGroupsFile.createNewFile();
        try (Stream<String> lines = Files.lines(previousGroupsFile.toPath())) {
            lines.map(s -> s = s.replace("\n", "")).forEach(previousGroupsList::add);
            //previousGroupsList.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveGroupToCache(String group) {
        try (FileWriter fileWriter = new FileWriter(previousGroupsFile, true)) {
            fileWriter.write(group + '\n');
            previousGroupsList.add(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void updateStudentData(Student student) {
        String studentName = student.getName();
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

    public static boolean isNewGroupDuplicated(String group) {
        return previousGroupsList.contains(group);

    }
}
