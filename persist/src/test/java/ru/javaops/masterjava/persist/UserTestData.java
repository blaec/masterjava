package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.javaops.masterjava.persist.CityTestData.*;
import static ru.javaops.masterjava.persist.GroupTestData.*;

public class UserTestData {
    public static User ADMIN;
    public static User DELETED;
    public static User FULL_NAME;
    public static User USER1;
    public static User USER2;
    public static User USER3;
    public static List<User> FIST5_USERS;

    public static void init() {
        List<Group> fullNameGroups = new ArrayList<>(Arrays.asList(MASTERJAVA01, TOPJAVA07));
        List<Group> adminGroups = new ArrayList<>(Arrays.asList(MASTERJAVA01, TOPJAVA07, TOPJAVA08));
        List<Group> user1Groups = new ArrayList<>(Arrays.asList(MASTERJAVA01, TOPJAVA06));
        List<Group> user2Groups = new ArrayList<>(Collections.singletonList(MASTERJAVA01));

        ADMIN = new User("Admin", "admin@javaops.ru", UserFlag.superuser, ST_PETERSBURG, adminGroups);
        DELETED = new User("Deleted", "deleted@yandex.ru", UserFlag.deleted, ST_PETERSBURG, Collections.emptyList());
        FULL_NAME = new User("Full Name", "gmail@gmail.com", UserFlag.active, KYIV, fullNameGroups);
        USER1 = new User("User1", "user1@gmail.com", UserFlag.active, MOSCOW, user1Groups);
        USER2 = new User("User2", "user2@yandex.ru", UserFlag.active, KYIV, user2Groups);
        USER3 = new User("User3", "user3@yandex.ru", UserFlag.active, MINSK, user2Groups);
        FIST5_USERS = ImmutableList.of(ADMIN, DELETED, FULL_NAME, USER1, USER2);
    }

    public static void setUp() {
        UserDao dao = DBIProvider.getDao(UserDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIST5_USERS.forEach(dao::insert);
            dao.insert(USER3);
        });
    }
}
