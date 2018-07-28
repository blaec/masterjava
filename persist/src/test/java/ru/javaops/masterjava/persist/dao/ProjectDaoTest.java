package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.GroupTestData;
import ru.javaops.masterjava.persist.ProjectTestData;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.javaops.masterjava.persist.ProjectTestData.MASTERJAVA;

public class ProjectDaoTest extends AbstractDaoTest<ProjectDao> {

    public ProjectDaoTest() {
        super(ProjectDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        GroupTestData.init();
        GroupTestData.setUp();

        ProjectTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        ProjectTestData.setUp();
    }

    @Test
    public void getWithLimit() {
        List<Project> expected = new ArrayList<>();
        expected.add(MASTERJAVA);
//        expected.add(TOPJAVA);
        List<Project> projects = dao.getWithLimit(expected.size());

        GroupDao groupDao = DBIProvider.getDao(GroupDao.class);
        Map<Integer, Group> groupMap = new HashMap<>();

        for (Project project : projects) {
            List<Integer> groupIds = dao.getProjectGroups(project.getId());
            List<Group> groups = new ArrayList<>();
            for (int groupId : groupIds) {
                Group group;
                if (groupMap.containsKey(groupId)) {
                    group = groupMap.get(groupId);
                } else {
                    group = groupDao.getById(groupId);
                    groupMap.put(groupId, group);
                }
                groups.add(group);
            }
            project.setGroups(groups);
        }

        Assert.assertEquals(expected, projects);
    }
}