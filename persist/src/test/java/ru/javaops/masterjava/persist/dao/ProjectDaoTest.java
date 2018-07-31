package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.GroupTestData;
import ru.javaops.masterjava.persist.ProjectTestData;
import ru.javaops.masterjava.persist.model.Project;

import java.util.ArrayList;
import java.util.List;

import static ru.javaops.masterjava.persist.ProjectTestData.MASTERJAVA;
import static ru.javaops.masterjava.persist.dao.TestUtils.getGroups;

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

        for (Project project : projects) {
            List<Integer> groupIds = dao.getProjectGroups(project.getId());
            project.setGroups(getGroups(groupIds));
        }

        Assert.assertEquals(expected, projects);
    }

}