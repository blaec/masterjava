package ru.javaops.masterjava.service.dao;


import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.dao.AbstractDaoTest;
import ru.javaops.masterjava.service.SentMailData;

import static ru.javaops.masterjava.service.SentMailData.TEST;


public class SentMailDaoTest extends AbstractDaoTest<SentMailDao> {

    public SentMailDaoTest() {
        super(SentMailDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        SentMailData.init();
    }

    @Before
    public void setUp() throws Exception {
        SentMailData.setUp();
    }

    @Test
    public void insertTest() throws Exception {
        dao.clean();
        dao.insert(TEST);
        Assert.assertEquals(TEST, dao.getFirst());
    }
}
