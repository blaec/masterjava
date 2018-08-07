package ru.javaops.masterjava.service.mail;

import org.apache.commons.mail.EmailException;

import javax.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "ru.javaops.masterjava.service.mail.MailService")
public class MailServiceImpl implements MailService {
    public void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        try {
            MailSender.sendMail(to, cc, subject, body);
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}