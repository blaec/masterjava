package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.util.*;
import java.util.stream.Collectors;

public class MainXmlStax {
    private String project;
    private Map<String, String> projectMap;

    public MainXmlStax(String project) {
        this.project = project;
        try {
            projectMap = fillProjectList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> fillProjectList() throws Exception {
        Map<String, String> map = new HashMap<>();
        String projectName = null;

        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT) {
                    if ("name".equals(reader.getLocalName())) {
                        projectName = reader.getElementText();
                    }
                    if ("Group".equals(reader.getLocalName())) {
                        map.put( reader.getAttributeValue(1), projectName);
                    }
                }
            }
        }
        return map;
    }

    public String printUserDetails() throws Exception {
        String userName = null;
        String email = null;
        List<String> userDetails = new ArrayList<>();

        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT) {
                    if ("User".equals(reader.getLocalName())) {
                        email = reader.getAttributeValue(2);
                    }
                    if ("fullName".equals(reader.getLocalName())) {
                        userName = reader.getElementText();
                    }
                    if ("UserGroups".equals(reader.getLocalName())) {
                        if (belongsToGroup(reader.getElementText())) {
                            userDetails.add(userName + "/" + email);
                        }
                    }
                }
            }
        }

        return userDetails.toString();
    }

    private boolean belongsToGroup(String userGroups) {
        List<String> groups = Arrays.asList(userGroups.split(" "));
        groups = groups.stream()
                .filter(g -> projectMap.get(g).equals(project))
                .collect(Collectors.toList());
        return groups.size() > 0;
    }

    public static void main(String[] args) {
        MainXmlStax mainXmlStax = new MainXmlStax("masterjava");
        try {
            System.out.println(mainXmlStax.printUserDetails());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
