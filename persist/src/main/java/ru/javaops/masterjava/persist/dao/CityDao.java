package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao {
    public City insert(City city) {
        if (city.isNew()) {
            int id = insertGeneratedId(city);
            city.setId(id);
        } else {
            insertWithId(city);
        }
        return city;
    }

    @SqlUpdate("TRUNCATE users, cities")
    @Override
    public void clean() {
    }

    @SqlUpdate("INSERT INTO cities (name) VALUES (:name) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean City city);

    @SqlUpdate("INSERT INTO cities (id, name) VALUES (:id, :name) ")
    abstract void insertWithId(@BindBean City city);

    @SqlQuery("SELECT * FROM cities LIMIT :it")
    public abstract List<City> getWithLimit(@Bind int limit);

}
