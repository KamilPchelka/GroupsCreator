package pl.kamilpchelka.codecool.groupscreator.entites;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pl.kamilpchelka.codecool.groupscreator.controllers.Controller;
import pl.kamilpchelka.codecool.groupscreator.enums.ClassGroup;

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


    public static String getStudentNameAttribute() {
        return STUDENT_NAME_ATTRIBUTE;
    }

    public static String getStudentProgrammingLevelAttribute() {
        return STUDENT_PROGRAMMING_LEVEL_ATTRIBUTE;
    }

    public static List<CodeCoolClass> getCodeCoolClassList() {
        return CodeCoolClassList;
    }

    public static void setCodeCoolClassList(List<CodeCoolClass> codeCoolClassList) {
        CodeCoolClassList = codeCoolClassList;
    }

    public List<ClassGroup> getClassGroupsList() {
        return classGroupsList;
    }

    public void setClassGroupsList(List<ClassGroup> classGroupsList) {
        this.classGroupsList = classGroupsList;
    }

    public Map<ClassGroup, ArrayList<Student>> getStudentMap() {
        return studentMap;
    }

    public void setStudentMap(Map<ClassGroup, ArrayList<Student>> studentMap) {
        this.studentMap = studentMap;
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
                        String studentNodeName = groupNode.getNodeName();
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
        String className = student.getParentNode().getParentNode().getNodeName();
        ClassGroup classGroup = ClassGroup.valueOf(student.getParentNode().getNodeName());
        return(new Student(name, programmingLevel, className, classGroup));

    }
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return className;
    }
}
