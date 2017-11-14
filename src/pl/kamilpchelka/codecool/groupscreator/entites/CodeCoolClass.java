package pl.kamilpchelka.codecool.groupscreator.entites;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pl.kamilpchelka.codecool.groupscreator.controllers.Controller;
import pl.kamilpchelka.codecool.groupscreator.enums.ClassGroup;
import pl.kamilpchelka.codecool.groupscreator.utils.DataManager;

import java.util.*;

public class CodeCoolClass {
    private static final String STUDENT_NAME_ATTRIBUTE = "name";
    private static final String STUDENT_PROGRAMMING_LEVEL_ATTRIBUTE = "programminglevel";
    private String className;


    //A list of all loaded classes
    private static List<CodeCoolClass> CodeCoolClassList = new ArrayList<>();

    private List<ClassGroup> classGroupsList = new ArrayList<>();

    //Collection that contains students sorted by class versions
    private Map<ClassGroup, ArrayList<Student>> studentMap = new HashMap<>();

    public List<ClassGroup> getClassGroupsList() {
        return classGroupsList;
    }


    public Map<ClassGroup, ArrayList<Student>> getStudentMap() {
        return studentMap;
    }


    public CodeCoolClass(String className, Controller controller, NodeList groups) {
        this.className = className;
        divideStudentsIntoGroups(groups);
        controller.initializeSelectClassComboBox(this);
    }

    private void divideStudentsIntoGroups(NodeList groups) {
        for (int i = 0; i < groups.getLength(); i++) {
            Node groupNode = groups.item(i);
            String groupNodeName = groupNode.getNodeName();
            if (groupNode.getNodeType() == Node.ELEMENT_NODE) {
                classGroupsList.add(ClassGroup.valueOf(groupNodeName));
                ClassGroup classGroup = ClassGroup.valueOf(groupNodeName.toUpperCase());
                NodeList studentNodesList = groupNode.getChildNodes();
                for (int j = 0; j < studentNodesList.getLength(); j++) {
                    Node studentNode = studentNodesList.item(j);
                    if(studentNode.getNodeType() == Node.ELEMENT_NODE) {
                        Student student = getStudentFromElement((Element) studentNode);
                        if(studentMap.containsKey(classGroup)){
                            ArrayList<Student> studentList = studentMap.get(classGroup);
                            studentList.add(student);
                            studentMap.put(classGroup, studentList);
                        } else {
                            studentMap.put(classGroup, new ArrayList<>(Arrays.asList(student)));
                        }
                    }
                }
            }
        }
    }

    private Student getStudentFromElement(Element student) {
        String name = student.getAttribute(STUDENT_NAME_ATTRIBUTE);
        String programmingLevel = student.getAttribute(STUDENT_PROGRAMMING_LEVEL_ATTRIBUTE);
        String isActive = student.getAttribute("isActive");
        Student newStudent = new Student(name, programmingLevel, isActive);
        if (isActive.isEmpty()) try {
            DataManager.updateStudentData(newStudent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (newStudent);

    }

    @Override
    public String toString() {
        return className;
    }
}
