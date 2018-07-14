package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.upload.UserProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.DriverManager;
import java.util.List;

public class AddToDB {
    public static void main(String[] args) throws IOException, JAXBException, XMLStreamException {
        URL payloadUrl = Resources.getResource("payload.xml");
        UserProcessor userProcessor = new UserProcessor();
        InputStream is = payloadUrl.openStream();
        List<User> users = userProcessor.process(is);

        DBIProvider.init(() -> {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("PostgreSQL driver not found", e);
            }
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/masterjava", "postgres", "postgres");
        });
        UserDao dao = DBIProvider.getDao(UserDao.class);
//        dao.clean();
        int batchSize = 3;
        DBIProvider.getDBI().useTransaction((conn, status) -> dao.batchInsert(users.iterator(), batchSize));
    }
}
