package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableSet;

public class MailWSClientMain {
    public static void main(String[] args) {
        MailWSClient.sendToGroup(
                ImmutableSet.of(new Addressee("To <blaec@yandex.ru>")),
                ImmutableSet.of(new Addressee("Copy <blaec@yandex.ru>")), "Subject", "Body");
    }
}