package ru.javaops.masterjava.persist.dao;

import one.util.streamex.IntStreamEx;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.User;

import java.util.List;

//@RegisterMapperFactory(EntityMapperFactory.class)
@RegisterMapper(UserMapper.class)
public abstract class UserDao implements AbstractDao {

    public User insert(User user) {
        if (user.isNew()) {
            int id = insertGeneratedId(user, user.getCity().getId());
            user.setId(id);
        } else {
            insertWithId(user, user.getCity().getId());
        }
        List<Group> groups =user.getGroups();
        for (Group group : groups) {
            insertUserGroups(user.getId(), group.getId());
        }
        return user;
    }

    @SqlQuery("SELECT nextval('user_seq')")
    abstract int getNextVal();

    @Transaction
    public int getSeqAndSkip(int step) {
        int id = getNextVal();
//        DBIProvider.getDBI().useHandle(h -> h.execute("ALTER SEQUENCE user_seq RESTART WITH " + (id + step)));
        DBIProvider.getDBI().useHandle(h -> h.execute("SELECT setval('user_seq', " + (id + step - 1) + ")"));
        return id;
    }

    @SqlUpdate("INSERT INTO user_groups (user_id, group_id) VALUES (:userId, :groupId)")
    abstract void insertUserGroups(@Bind("userId") int userId, @Bind("groupId") int getUserId);

    @SqlQuery("SELECT group_id FROM user_groups WHERE user_id=:userId")
    public abstract List<Integer> getUserGroups(@Bind("userId") int userId);

    @SqlUpdate("INSERT INTO users (full_name, email, flag, city_id) " +
               "VALUES (:fullName, :email, CAST(:flag AS USER_FLAG), :city_id) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean User user, @Bind("city_id") int cityId);

    @SqlUpdate("INSERT INTO users (id, full_name, email, flag, city_id) " +
               "VALUES (:id, :fullName, :email, CAST(:flag AS USER_FLAG), :city_id) ")
    abstract void insertWithId(@BindBean User user, @Bind("city_id") int cityId);

    @SqlQuery("SELECT * FROM users ORDER BY full_name, email LIMIT :it")
    public abstract List<User> getWithLimit(@Bind int limit);

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE user_groups, users")
    @Override
    public abstract void clean();

    //    https://habrahabr.ru/post/264281/
    @SqlBatch("INSERT INTO users (id, full_name, email, flag, city_id) " +
            "VALUES (:id, :fullName, :email, CAST(:flag AS USER_FLAG), :city_id)" +
            "ON CONFLICT DO NOTHING")
//            "ON CONFLICT (email) DO UPDATE SET full_name=:fullName, flag=CAST(:flag AS USER_FLAG)")
    public abstract int[] insertBatch(@BindBean List<User> users, @BatchChunkSize int chunkSize);


    public List<String> insertAndGetConflictEmails(List<User> users) {
/*
        List<Integer> cityIds = users.stream()
                .map(u -> u.getCity().getId())
                .collect(Collectors.toList());
*/
        int[] result = insertBatch(users, users.size());
        return IntStreamEx.range(0, users.size())
                .filter(i -> result[i] == 0)
                .mapToObj(index -> users.get(index).getEmail())
                .toList();
    }
}
