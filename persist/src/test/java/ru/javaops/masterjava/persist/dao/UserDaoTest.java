package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.CityTestData;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.GroupTestData;
import ru.javaops.masterjava.persist.UserTestData;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.javaops.masterjava.persist.UserTestData.FIST5_USERS;

public class UserDaoTest extends AbstractDaoTest<UserDao> {

    public UserDaoTest() {
        super(UserDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        CityTestData.init();
        CityTestData.setUp();

        GroupTestData.init();
        GroupTestData.setUp();

        UserTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        UserTestData.setUp();
    }

    @Test
    public void getWithLimit() {
        List<User> users = dao.getWithLimit(5);

        CityDao cityDao = DBIProvider.getDao(CityDao.class);
        GroupDao groupDao = DBIProvider.getDao(GroupDao.class);
        Map<Integer, City> cityMap = new HashMap<>();
        Map<Integer, Group> groupMap = new HashMap<>();

        for (User user : users) {
            int cityId = user.getCity().getId();
            City city;
            if (cityMap.containsKey(cityId)) {
                city = cityMap.get(cityId);
            }  else {
                city = cityDao.getById(cityId);
                cityMap.put(cityId, city);
            }
            user.setCity(city);

            List<Integer> groupIds = dao.getUserGroups(user.getId());
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
            user.setGroups(groups);
        }

        Assert.assertEquals(FIST5_USERS, users);
    }

    @Test
    public void insertBatch() throws Exception {
        dao.clean();
        List<Integer> cityIds = FIST5_USERS.stream()
                .map(u -> u.getCity().getId())
                .collect(Collectors.toList());
        dao.insertBatch(FIST5_USERS, 3, cityIds);
        Assert.assertEquals(5, dao.getWithLimit(100).size());
    }

    @Test
    public void getSeqAndSkip() throws Exception {
        int seq1 = dao.getSeqAndSkip(5);
        int seq2 = dao.getSeqAndSkip(1);
        Assert.assertEquals(5, seq2 - seq1);
    }
}