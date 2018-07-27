package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.Group;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class GroupDao implements AbstractDao {
    public Group insert(Group group) {
        if (group.isNew()) {
            int id = insertGeneratedId(group);
            group.setId(id);
        } else {
            insertWithId(group);
        }
        return group;
    }

    @SqlUpdate("TRUNCATE user_groups, users, projects, groups")
    @Override
    public void clean() {
    }

    @SqlUpdate("INSERT INTO groups (name, type) VALUES (:name, CAST(:type AS group_type)) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean Group group);

    @SqlUpdate("INSERT INTO groups (id, name, type) VALUES (:id, :name, CAST(:type AS group_type)) ")
    abstract void insertWithId(@BindBean Group group);

    @SqlQuery("SELECT * FROM groups LIMIT :it")
    public abstract List<Group> getWithLimit(@Bind int limit);

}
