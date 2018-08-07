package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import ru.javaops.masterjava.config.Configs;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MailSender {
    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) throws EmailException {
        Config mail = Configs.getConfig("mail.conf","mail");

        String sendTo = String.join("; ",to.stream().map(Addressee::getEmail).collect(Collectors.toList()));
        log.info("Send mail to \'" + sendTo + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));

        Email email = new SimpleEmail();
        email.setHostName(mail.getString("host"));
        email.setSmtpPort(mail.getInt("port"));
        email.setAuthenticator(new DefaultAuthenticator("odeskonst@yandex.ru", mail.getString("password")));
        email.setSSLOnConnect(mail.getBoolean("useSSL"));
        email.setFrom("odeskonst@yandex.ru");
        email.setSubject("TestMail");
        email.setMsg("This is a test mail ... :-)");
        email.addTo(sendTo);
        email.send();
    }
}
