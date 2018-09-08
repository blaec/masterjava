package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.web.WebStateException;

import javax.activation.DataHandler;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MailWSClientMain {
    public static void main(String[] args) throws WebStateException, MalformedURLException {
        List<Attachment> attachments = ImmutableList.of(
                new Attachment("FileName", new DataHandler(new File("config_templates/version.html").toURI().toURL())));

        String state = MailWSClient.sendToGroup(
                ImmutableSet.of(new Addressee("To <blaec@yandex.ru>")),
                ImmutableSet.of(new Addressee("Copy <blaec@yandex.ru>")), "Subject", "Body", attachments);

        GroupResult groupResult = MailWSClient.sendBulk(ImmutableSet.of(
                new Addressee("Мастер Java <blaec@yandex.ru>"),
                new Addressee("Bad Email <bad_email.ru>")), "Bulk mail subject", "Bulk mail body", attachments);
        System.out.println("\nBulk mail groupResult:\n" + groupResult);
        System.out.println(state);
    }
}