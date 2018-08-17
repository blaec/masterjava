package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import org.apache.commons.mail.*;
import ru.javaops.masterjava.config.Configs;

import javax.mail.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class MailConfig {
    private static final MailConfig INSTANCE = new MailConfig(Configs.getConfig("mail.conf", "mail"));

    final private int port;
    final private String host;
    final private String username;
    final private String attachPath;
    final private String fromName;
    final private boolean useSSL;
    final private boolean useTLS;
    final private boolean debug;
    final private Authenticator auth;

    public MailConfig(Config conf) {
        host = conf.getString("host");
        port = conf.getInt("port");
        username = conf.getString("username");
        useSSL = conf.getBoolean("useSSL");
        useTLS = conf.getBoolean("useTLS");
        fromName = conf.getString("fromName");
        debug = conf.getBoolean("debug");
        attachPath = conf.getString("attachment");
        auth = new DefaultAuthenticator(username, conf.getString("password"));
    }

    private <T extends Email> T preparareEmail(T email) throws EmailException {
        email.setFrom(username, fromName);
        email.setHostName(host);
        if (useSSL) {
            email.setSslSmtpPort(String.valueOf(port));
        } else {
            email.setSmtpPort(port);
        }
        email.setSSLOnConnect(useSSL);
        email.setStartTLSEnabled(useTLS);
        email.setDebug(debug);
        email.setAuthenticator(auth);
        email.setCharset(StandardCharsets.UTF_8.name());
        return email;
    }

    public static HtmlEmail createHtmlEmail() throws EmailException {
        return INSTANCE.preparareEmail(new HtmlEmail());
    }

    private EmailAttachment addAttachment(boolean isUrl) throws MalformedURLException {
        EmailAttachment attachment = new EmailAttachment();
        String description;
        String name;

        if (!isUrl) {
            attachment.setPath(attachPath);
            description = "Picture of " + fromName;
            name = fromName;
        } else {
            attachment.setURL(new URL("http://www.apache.org/images/asf_logo_wide.gif"));
            description = "Apache logo";
            name = "Apache logo";
        }
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription(description);
        attachment.setName(name);
        return attachment;
    }

    public static EmailAttachment attachFile(boolean hasUrlPath) throws MalformedURLException {
        return INSTANCE.addAttachment(hasUrlPath);
    }

    @Override
    public String toString() {
        return  "\nhost='" + host + '\'' +
                "\nport=" + port +
                "\nuseSSL=" + useSSL +
                "\nuseTLS=" + useTLS +
                "\ndebug=" + debug +
                "\nusername='" + username + '\'' +
                "\nfromName='" + fromName + '\'';
    }
}
