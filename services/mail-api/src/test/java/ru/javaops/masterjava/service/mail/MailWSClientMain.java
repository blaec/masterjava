package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.web.WebStateException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MailWSClientMain {
    public static void main(String[] args) throws WebStateException {
        String state = MailWSClient.sendToGroup(
                ImmutableSet.of(new Addressee("To <blaec@yandex.ru>")),
                ImmutableSet.of(new Addressee("Copy <blaec@yandex.ru>")), "Subject", "Body", null);
        System.out.println(state);
    }
}