package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import one.util.streamex.IntStreamEx;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
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

    @Transaction
    public int getSeqAndSkip(int step) {
        int id = getNextVal();
//        DBIProvider.getDBI().useHandle(h -> h.execute("ALTER SEQUENCE user_seq RESTART WITH " + (id + step)));
        DBIProvider.getDBI().useHandle(h -> h.execute("SELECT setval('user_seq', " + (id + step - 1) + ")"));
        return id;
    }

    @SqlQuery("SELECT nextval('user_seq')")
    abstract int getNextVal();

    @SqlUpdate("TRUNCATE user_groups, users, cities")
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

    @SqlQuery("SELECT * FROM cities WHERE id=:id")
    public abstract City getById(@Bind("id") int id);

    //    https://habrahabr.ru/post/264281/
    @SqlBatch("INSERT INTO cities (id, name) " +
              "VALUES (:id, :name)" +
              "ON CONFLICT DO NOTHING")
//            "ON CONFLICT (email) DO UPDATE SET full_name=:fullName, flag=CAST(:flag AS USER_FLAG)")
    public abstract int[] insertBatch(@BindBean List<City> cities, @BatchChunkSize int chunkSize);

    public List<String> insertAndGetConflictCities(List<City> cities) {
        int[] result = insertBatch(cities, cities.size());
        return IntStreamEx.range(0, cities.size())
                .filter(i -> result[i] == 0)
                .mapToObj(index -> cities.get(index).getName())
                .toList();
    }

}
