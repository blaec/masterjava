package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.GroupType;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;

import java.util.List;

public class GroupTestData {
    public static Group masterjava01;
    public static Group masterjava02;
    public static Group topjava01;
    public static Group topjava02;
    public static Group topjava03;
    public static Group topjava04;
    public static Group topjava05;
    public static Group topjava06;
    public static Group topjava07;
    public static Group topjava08;
    public static List<Group> FIRST5_GROUPS;

    public static void init() {
        masterjava01 = new Group("masterjava01", GroupType.CURRENT);
        masterjava02 = new Group("masterjava02", GroupType.REGISTERING);
        topjava01 = new Group("topjava01", GroupType.FINISHED);
        topjava02 = new Group("topjava02", GroupType.FINISHED);
        topjava03 = new Group("topjava03", GroupType.FINISHED);
        topjava04 = new Group("topjava04", GroupType.FINISHED);
        topjava05 = new Group("topjava05", GroupType.FINISHED);
        topjava06 = new Group("topjava06", GroupType.FINISHED);
        topjava07 = new Group("topjava07", GroupType.FINISHED);
        topjava08 = new Group("topjava08", GroupType.CURRENT);
        FIRST5_GROUPS = ImmutableList.of(masterjava01, masterjava02, topjava01, topjava02, topjava03);
    }

    public static void setUp() {
        GroupDao dao = DBIProvider.getDao(GroupDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIRST5_GROUPS.forEach(dao::insert);
            dao.insert(topjava04);
            dao.insert(topjava05);
            dao.insert(topjava06);
            dao.insert(topjava07);
            dao.insert(topjava08);
        });
    }
}
