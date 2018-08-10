package ru.javaops.masterjava.service;

import ru.javaops.masterjava.service.dao.SentMailDao;
import ru.javaops.masterjava.service.model.SentMail;
import ru.javaops.masterjava.persist.DBIProvider;

public class SentMailData {
    public static SentMail TEST;

    public static void init() {

        TEST = new SentMail(true, "test@test.ts", "tester", "Test", "This is a test mail", "a@a.ru,b@b.com","");
    }

    public static void setUp() {
        SentMailDao dao = DBIProvider.getDao(SentMailDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            dao.insert(TEST);
        });
    }
}
