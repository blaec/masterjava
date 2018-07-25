package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

public class CityTestData {
    public static City ST_PETERSBURG;
    public static City MOSCOW;
    public static City KYIV;
    public static City MINSK;
    public static List<City> FIRST3_CITIES;

    public static void init() {
        ST_PETERSBURG = new City("Санкт-Петербург");
        MOSCOW = new City("Москва");
        KYIV = new City("Киев");
        MINSK = new City("Минск");
        FIRST3_CITIES = ImmutableList.of(ST_PETERSBURG, MOSCOW, KYIV);
    }

    public static void setUp() {
        CityDao dao = DBIProvider.getDao(CityDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIRST3_CITIES.forEach(dao::insert);
            dao.insert(MINSK);
        });
    }
}
