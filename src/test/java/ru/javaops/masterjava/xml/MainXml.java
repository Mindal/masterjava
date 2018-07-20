package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MainXml {
    public static void main(String[] args) throws Exception {
        System.out.println("Input project name");
        Scanner scanner = new Scanner(System.in);
        String projectName = scanner.next();

        jaxbFindUsersInProject(projectName);
        System.out.println("______________");
        staxFindUsersInProject(projectName);
    }

    private static void staxFindUsersInProject(String projectName) throws Exception {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            Map<User, List<String>> userGroups = new HashMap<>();
            processor.doUntil(XMLEvent.START_ELEMENT, "User");
            //find all user belonged groups
            while (processor.doUntilWithBreak(XMLEvent.START_ELEMENT, "email", "Users")) {
                User user = getUser(processor);
                userGroups.put(user, new ArrayList<>());
                processor.doUntil(XMLEvent.START_ELEMENT, "groupRefs");
                Collections.addAll(userGroups.get(user), processor.getText().split(" "));
            }
            List<String> groupsWithCurrentProject = new ArrayList<>();
            String project;
            //find all groups belonged to project with name of method parameter
            while ((project = processor.getAttributeValue("Group", 2)) != null) { //index of project name is 2 see payload.xml
                if (projectName.equals(project)) {
                    groupsWithCurrentProject.add(processor.getAttributeText(0));
                }
            }
            List<User> result = userGroups.entrySet().stream()
                    .filter(entry -> !Collections.disjoint(entry.getValue(), groupsWithCurrentProject))
                    .map(Map.Entry::getKey)
                    .sorted(Comparator.comparing(User::getFullName))
                    .collect(Collectors.toList());
            result.forEach(
                    user -> System.out.println(user.getFullName() + " " + user.getEmail())
            );

        }

    }

    private static User getUser(StaxStreamProcessor processor) throws XMLStreamException {
        String email = processor.getText();
        processor.doUntil(XMLEvent.START_ELEMENT, "fullName");
        String fullName = processor.getText();
        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        return user;
    }

    private static void jaxbFindUsersInProject(String projectName) throws javax.xml.bind.JAXBException, IOException {
        JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
        jaxbParser.setSchema(Schemas.ofClasspath("payload.xsd"));
        Payload payload = jaxbParser.unmarshal(
                Resources.getResource("payload.xml").openStream());

        List<Group> groupsForChosenProject = payload.getGroups().getGroup().stream()
                .filter(group -> ((Project) group.getProject()).getName().equals(projectName))
                .collect(Collectors.toList());

        List<User> collect = payload.getUsers().getUser().stream()
                .filter(user -> !Collections.disjoint(groupsForChosenProject, user.getGroupRefs()))
                .sorted(Comparator.comparing(User::getFullName))
                .collect(Collectors.toList());
        collect.forEach(user -> System.out.println(user.getFullName() + " " + user.getEmail()));
    }

}
