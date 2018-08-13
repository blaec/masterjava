package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.persist.model.type.GroupType;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Slf4j
public class ProjectGroupProcessor {
    private final ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);
    private final GroupDao groupDao = DBIProvider.getDao(GroupDao.class);

    public void process(StaxStreamProcessor processor) throws XMLStreamException {
        Map<String, Project> projectMap = projectDao.getAsMap();
        val groupMap = groupDao.getAsMap();
        val newProjects = new ArrayList<Project>();
        val newGroups = new HashMap<String, Group>();

        while (processor.startElement("Project", "Projects")) {
            val ref = processor.getAttribute("name");

            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.END_ELEMENT && "Project".equals((event == XMLEvent.CHARACTERS) ? reader.getText() : reader.getLocalName())) {
                    break;
                }
                if (event == XMLEvent.START_ELEMENT) {
                    if ("Group".equals(reader.getLocalName())) {
                        String name = processor.getAttribute("name");
                        String type = processor.getAttribute("type");
                        String rndSuffix = String.format("-%03d",((new Random()).nextInt(1000)));
                        newGroups.put(ref + rndSuffix, new Group(name, GroupType.valueOf(type), 1));
                    } else {
                        if (!projectMap.containsKey(ref)) {
                            newProjects.add(new Project(ref, processor.getText()));
                        }
                    }
                }
            }
        }

        log.info("Insert new project(s) " + newProjects);
        for (Project project : newProjects) {
            projectDao.insert(project);
        }
        projectMap = projectDao.getAsMap();

        log.info("Insert new group(s) " + newGroups);
        for (String key : newGroups.keySet()) {
            Group group = newGroups.get(key);
            if (!groupMap.containsKey(group.getName())) {
                String project = key.split("-")[0];
                Integer id = projectMap.get(project).getId();
                group.setProjectId(id);
                groupDao.insert(group);
            }
        }
    }
}
