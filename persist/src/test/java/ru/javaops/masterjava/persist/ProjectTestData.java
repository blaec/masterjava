package ru.javaops.masterjava.persist;

import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.javaops.masterjava.persist.GroupTestData.*;

public class ProjectTestData {
    public static Project TOPJAVA;
    public static Project MASTERJAVA;

    public static void init() {
        List<Group> groupsMasterjava = new ArrayList<>(Arrays.asList(MASTERJAVA01, MASTERJAVA02));
        List<Group> groupsTopjava = new ArrayList<>(Arrays.asList(TOPJAVA01, TOPJAVA02, TOPJAVA03, TOPJAVA04, TOPJAVA05, TOPJAVA06, TOPJAVA07, TOPJAVA08));
        MASTERJAVA = new Project("masterjava", "Masterjava", groupsMasterjava);
        TOPJAVA = new Project("topjava", "Topjava", groupsTopjava);
    }

    public static void setUp() {
        ProjectDao dao = DBIProvider.getDao(ProjectDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            dao.insert(MASTERJAVA);
            dao.insert(TOPJAVA);
        });
    }
}
