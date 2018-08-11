package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;

@Slf4j
public class ProjectProcessor {
    private final ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);

    public void process(StaxStreamProcessor processor) throws XMLStreamException {
        val map = projectDao.getAsMap();
        val newProjects = new ArrayList<Project>();

        while (processor.startElement("Project", "Projects")) {
            val ref = processor.getAttribute("name");
            if (!map.containsKey(ref)) {
                while (processor.startElement("description", "Project")) {
                    newProjects.add(new Project(ref, processor.getText()));
                }
            }
        }
        log.info("Insert new project(s) " + newProjects);
        for (Project project : newProjects) {
            projectDao.insert(project);
        }
    }
}
