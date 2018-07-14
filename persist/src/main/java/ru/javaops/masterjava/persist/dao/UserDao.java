package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class UserDao implements AbstractDao {

    public User insert(User user) {
        if (user.isNew()) {
            int id = insertGeneratedId(user);
            user.setId(id);
        } else {
            insertWithId(user);
        }
        return user;
    }

    public void batchInsert(List<User> users, int size) {
/*
        int id = getNextVal();
        System.out.println(setNewSequence(id + size));;
        users.forEach(user -> user.setId(id+1));
*/
        int[] ids = insertBatchGeneratedId(users.iterator(), size);
        System.out.printf("%d entries inserted into masterjava-db%n", IntStream.of(ids).sum());
//        System.out.println(getCurrVal());
    }

    @SqlUpdate("INSERT INTO users (full_name, email, flag) VALUES (:fullName, :email, CAST(:flag AS user_flag)) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean User user);

    @SqlBatch("INSERT INTO users (full_name, email, flag) " +
            "VALUES (:fullName, :email, " +
            "CAST(:flag AS user_flag)) " +
            "ON CONFLICT ON CONSTRAINT email_idx DO NOTHING ")
    abstract int[] insertBatchGeneratedId(@BindBean Iterator<User> user, @BatchChunkSize int size);
/*

    @SqlQuery("SELECT currval('user_seq')")
    abstract int getCurrVal();

    @SqlQuery("SELECT nextval('user_seq')")
    abstract int getNextVal();

    @SqlQuery("ALTER SEQUENCE user_seq RESTART WITH :newId")
    abstract int setNewSequence(@Bind("newId") int restartWith);
*/

    @SqlUpdate("INSERT INTO users (id, full_name, email, flag) VALUES (:id, :fullName, :email, CAST(:flag AS user_flag)) ")
    abstract void insertWithId(@BindBean User user);

    @SqlQuery("SELECT * FROM users ORDER BY full_name, email LIMIT :it")
    public abstract List<User> getWithLimit(@Bind int limit);

    @SqlQuery("SELECT * FROM users LIMIT :it")
    public abstract List<User> getTop(@Bind int limit);

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE users")
    @Override
    public abstract void clean();
}
