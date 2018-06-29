package ru.javaops.masterjava.concretepage;

import com.google.common.base.Splitter;
import com.google.common.io.Resources;
import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import static com.google.common.base.Strings.nullToEmpty;

public class WelcomeApplication {
    public static final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getValue).thenComparing(User::getEmail);

    public void process(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        WebContext ctx = new WebContext(request, response, request.getServletContext(),
                request.getLocale());
        String arg_project = new Random().nextInt(2) == 1 ? "masterjava" : "topjava";
        ctx.setVariable("users", processByStax(arg_project, Resources.getResource("payload.xml")));
        ctx.setVariable("project", arg_project);
        ThymeleafAppUtil.getTemplateEngine().process("welcome", ctx, response.getWriter());
    }

    public static Set<User> processByStax(String projectName, URL payloadUrl) throws Exception {

        try (InputStream is = payloadUrl.openStream()) {
            StaxStreamProcessor processor = new StaxStreamProcessor(is);
            final Set<String> groupNames = new HashSet<>();

            // Projects loop
            while (processor.startElement("Project", "Projects")) {
                if (projectName.equals(processor.getAttribute("name"))) {
                    while (processor.startElement("Group", "Project")) {
                        groupNames.add(processor.getAttribute("name"));
                    }
                    break;
                }
            }
            if (groupNames.isEmpty()) {
                throw new IllegalArgumentException("Invalid " + projectName + " or no groups");
            }

            // Users loop
            Set<User> users = new TreeSet<>(USER_COMPARATOR);

            JaxbParser parser = new JaxbParser(User.class);
            while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                String groupRefs = processor.getAttribute("groupRefs");
                if (!Collections.disjoint(groupNames, Splitter.on(' ').splitToList(nullToEmpty(groupRefs)))) {
                    User user = parser.unmarshal(processor.getReader(), User.class);
                    users.add(user);
                }
            }
            return users;
        }
    }
}
