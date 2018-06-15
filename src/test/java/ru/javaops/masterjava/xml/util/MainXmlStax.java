package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.List;

public class MainXmlStax {
    private String project;

    public MainXmlStax(String project) {
        this.project = project;
    }

    public String printUserDetails() throws Exception {
        String userName = null;
        String email = null;
        List<String> userDetails = new ArrayList<>();
        String compareString = "masterjava".equals(project) ? "mj" : "tj";

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
                        if (reader.getElementText().contains(compareString)) {
                            userDetails.add(userName + "/" + email);
                        }
                    }
                }
            }
        }
        return userDetails.toString();
    }

    public static void main(String[] args) {
        MainXmlStax mainXmlStax = new MainXmlStax("masterjava");
        try {
            System.out.println(mainXmlStax.printUserDetails());
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
