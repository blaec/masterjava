package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.mail.EmailException;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MailSender {
    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) throws EmailException, MalformedURLException {

        try {
            val email = MailConfig.createHtmlEmail();
            email.setSubject(subject);
            email.setHtmlMsg(body);
            for (Addressee addressee : to) {
                email.addTo(addressee.getEmail(), addressee.getName());
            }
            for (Addressee addressee : cc) {
                email.addCc(addressee.getEmail(), addressee.getName());
            }
            email.attach(MailConfig.attachFile(false));
            email.attach(MailConfig.attachFile(true));
            email.setHeaders(ImmutableMap.of("List-Unsubscribe", "<mailto:odeskonst@yandex.ru?subject=Unscubscribe&body=Unsubscribe>"));

            email.send();
        } catch (EmailException e) {
            log.error(e.getMessage(), e);
        }

        // Create the email message
        String[] sendTo = mailList(to);
        String[] sendCc = mailList(cc);
//        String from = "odeskonst@yandex.ru";

        log.info("Send mail to \'" + Arrays.toString(sendTo) + "\' " +
                "cc \'" + Arrays.toString(sendCc) + "\' " +
                "subject \'" + subject +
                (log.isDebugEnabled() ? "\nbody=" + body : ""));

/*
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
*/
    }

    private static String[] mailList(List<Addressee> input) {
        List<String> toMails = (input.stream().map(Addressee::getEmail).collect(Collectors.toList()));
        return toMails.toArray(new String[toMails.size()]);
    }
}