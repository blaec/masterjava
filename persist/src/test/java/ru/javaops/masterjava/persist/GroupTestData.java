package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.GroupType;

import java.util.List;

public class GroupTestData {
    public static Group MASTERJAVA01;
    public static Group MASTERJAVA02;
    public static Group TOPJAVA01;
    public static Group TOPJAVA02;
    public static Group TOPJAVA03;
    public static Group TOPJAVA04;
    public static Group TOPJAVA05;
    public static Group TOPJAVA06;
    public static Group TOPJAVA07;
    public static Group TOPJAVA08;
    public static List<Group> FIRST5_GROUPS;

    public static void init() {
        MASTERJAVA01 = new Group("masterjava01", GroupType.CURRENT);
        MASTERJAVA02 = new Group("masterjava02", GroupType.REGISTERING);
        TOPJAVA01 = new Group("topjava01", GroupType.FINISHED);
        TOPJAVA02 = new Group("topjava02", GroupType.FINISHED);
        TOPJAVA03 = new Group("topjava03", GroupType.FINISHED);
        TOPJAVA04 = new Group("topjava04", GroupType.FINISHED);
        TOPJAVA05 = new Group("topjava05", GroupType.FINISHED);
        TOPJAVA06 = new Group("topjava06", GroupType.FINISHED);
        TOPJAVA07 = new Group("topjava07", GroupType.FINISHED);
        TOPJAVA08 = new Group("topjava08", GroupType.CURRENT);
        FIRST5_GROUPS = ImmutableList.of(MASTERJAVA01, MASTERJAVA02, TOPJAVA01, TOPJAVA02, TOPJAVA03);
    }

    public static void setUp() {
        GroupDao dao = DBIProvider.getDao(GroupDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIRST5_GROUPS.forEach(dao::insert);
            dao.insert(TOPJAVA04);
            dao.insert(TOPJAVA05);
            dao.insert(TOPJAVA06);
            dao.insert(TOPJAVA07);
            dao.insert(TOPJAVA08);
        });
    }
}
