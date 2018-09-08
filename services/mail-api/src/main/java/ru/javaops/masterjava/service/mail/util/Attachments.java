package ru.javaops.masterjava.service.mail.util;

import lombok.AllArgsConstructor;
import ru.javaops.masterjava.service.mail.Attachment;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class Attachments {
/*
    public static Attachment getAttach(String name, InputStream inputStream) {
        return new Attachment(name, new DataHandler(new InputStreamDataSource(inputStream)));
    }
*/
    public static Attachment getAttach(InputStream inputStream) {
        return new Attachment(new DataHandler(new InputStreamDataSource(inputStream)));
    }

    @AllArgsConstructor
    private static class InputStreamDataSource implements DataSource {
        private InputStream inputStream;

        @Override
        public InputStream getInputStream() throws IOException {
            if (inputStream == null) {
                throw new IOException("Second getInputStream() call is not supported");
            }
            InputStream res = inputStream;
            inputStream = null;
            return res;
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new UnsupportedEncodingException("Not implemented");
        }

        @Override
        public String getContentType() {
            return "application/octet-stream";
        }

        @Override
        public String getName() {
            return "";
        }
    }
}
