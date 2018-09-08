package ru.javaops.masterjava.webapp;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import ru.javaops.masterjava.service.mail.Attachment;
import ru.javaops.masterjava.service.mail.MailWSClient;
import ru.javaops.masterjava.service.mail.util.Attachments;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/send")
@Slf4j
public class SendServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result;
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        try {
            log.info("Start sending");
            ImmutableMap.Builder<String, String> paramsBuilder = ImmutableMap.builder();

            final ServletFileUpload upload = new ServletFileUpload();
            final FileItemIterator itemIterator = upload.getItemIterator(req);
            List<Attachment> attaches = ImmutableList.of();
            while (itemIterator.hasNext()) {
                FileItemStream item = itemIterator.next();
                if (item.isFormField()) {
                    paramsBuilder.put(item.getFieldName(),
                            Streams.asString(item.openStream(), "UTF-8"));
                } else {
                    if (!Strings.isNullOrEmpty(item.getName())) {
//                            attaches = ImmutableList.of(Attachments.getAttach(item.getName(), item.openStream()));
                        attaches = ImmutableList.of(Attachments.getAttach(item.openStream()));
                    }
                    break;
                }
            }
            ImmutableMap<String, String> params = paramsBuilder.build();
            result = MailWSClient.sendToGroup(MailWSClient.split(params.get("users")),
                                              MailWSClient.split(params.get("users")),
                                              params.get("subject"), params.get("body"), attaches);
            log.info("Processing finished with result: {}", result);
        } catch (Exception e) {
            log.error("Processing failed", e);
            result = e.toString();
        }
        resp.getWriter().write(result);
    }
}
