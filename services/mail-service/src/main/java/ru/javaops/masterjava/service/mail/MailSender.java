package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.*;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.masterjava.service.dao.SentMailDao;
import ru.javaops.masterjava.service.model.SentMail;
import ru.javaops.masterjava.persist.DBIProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MailSender {
    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) throws EmailException, MalformedURLException {
        Config mail = Configs.getConfig("mail.conf", "mail");
        MultiPartEmail  email = new MultiPartEmail ();

        // Create and add the attachment
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(mail.getString("attachment"));
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("Picture of " + mail.getString("fromName"));
        attachment.setName(mail.getString("fromName"));
        email.attach(attachment);

        // Create and add the attachment with reference any valid URL
        EmailAttachment attachmentUrl = new EmailAttachment();
        attachmentUrl.setURL(new URL("http://www.apache.org/images/asf_logo_wide.gif"));
        attachmentUrl.setDisposition(EmailAttachment.ATTACHMENT);
        attachmentUrl.setDescription("Apache logo");
        attachmentUrl.setName("Apache logo");
        email.attach(attachmentUrl);

        // Create the email message
        String[] sendTo = mailList(to);
        String[] sendCc = mailList(cc);
        String from = "odeskonst@yandex.ru";

        email.setHostName(mail.getString("host"));
        email.setSmtpPort(mail.getInt("port"));
        email.setAuthenticator(new DefaultAuthenticator(mail.getString("username"), mail.getString("password")));
        email.setSSLOnConnect(mail.getBoolean("useSSL"));
        email.setTLS(mail.getBoolean("useTLS"));
        email.setFrom("odeskonst@yandex.ru", mail.getString("fromName"));
        email.setSubject(subject);
        email.setMsg(body);
        email.addTo(sendTo);
        email.setDebug(mail.getBoolean("debug"));
        if (sendCc.length != 0) {
            email.addCc(sendCc);
        }

        // send the email
        email.send();

        log.info("Send mail to \'" + Arrays.toString(sendTo) + "\' " +
                "cc \'" + Arrays.toString(sendCc) + "\' " +
                "subject \'" + subject +
                (log.isDebugEnabled() ? "\nbody=" + body : ""));

        // Save results to db
        SentMail sentMail = new SentMail(
                email.isBoolHasAttachments(), from, mail.getString("fromName"), subject, body,
                String.join(",",sendTo), String.join(",",sendCc));

        Config db = Configs.getConfig("persist.conf","db");
        DBIProvider.init(() -> {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("PostgreSQL driver not found", e);
            }
            return DriverManager.getConnection(db.getString("url"), db.getString("user"), db.getString("password"));
        });

        SentMailDao dao = DBIProvider.getDao(SentMailDao.class);
        dao.insert(sentMail);
    }

    private static String[] mailList(List<Addressee> input) {
        List<String> toMails = (input.stream().map(Addressee::getEmail).collect(Collectors.toList()));
        return toMails.toArray(new String[toMails.size()]);
    }
}