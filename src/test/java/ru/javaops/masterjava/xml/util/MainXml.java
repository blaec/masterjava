package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.User;

import java.util.List;
import java.util.stream.Collectors;

public class MainXml {
    private String project;
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public MainXml(String project) {
        this.project = project;
    }

    public String showUsers() throws Exception {
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());
        List<String> users = payload.getUsers().getUser().stream()
                .filter(user -> user.getUserGroups().stream()
                                    .filter(gr -> gr.getValue().startsWith(project))
                                    .collect(Collectors.toList()).size() > 0)
                .map(User::getFullName)
                .sorted()
                .collect(Collectors.toList());
        return users.toString();
    }

    public static void main(String[] args) {
        MainXml mainXml = new MainXml("masterjava");
        try {
            System.out.println(mainXml.showUsers());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
