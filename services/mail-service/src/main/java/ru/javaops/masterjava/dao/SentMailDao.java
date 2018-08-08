package ru.javaops.masterjava.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.model.SentMail;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class SentMailDao {
    public SentMail insert(SentMail sentMail) {
        if (sentMail.isNew()) {
            int id = insertGeneratedId(sentMail);
            sentMail.setId(id);
        } else {
            insertWithId(sentMail);
        }
        return sentMail;
    }

    @SqlUpdate("INSERT INTO mails (has_attachment, from_mail, from_name, subject, body, to_list, cc_list) " +
               "VALUES (:hasAttachment, :fromMail, :fromName, :subject, :body, :toList, :ccList) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean SentMail sentMail);

    @SqlUpdate("INSERT INTO  mails (id, has_attachment, from_mail, from_name, subject, body, to_list, cc_list) " +
               "VALUES (:id, :hasAttachment, :fromMail, :fromName, :subject, :body, :toList, :ccList) ")
    abstract void insertWithId(@BindBean SentMail sentMail);

    @SqlUpdate("TRUNCATE mails")
    public abstract void clean();
}
