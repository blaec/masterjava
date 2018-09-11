package ru.javaops.masterjava.web.handler;

import com.sun.xml.ws.api.handler.MessageHandlerContext;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.web.AuthUtil;

import javax.xml.ws.handler.MessageContext;
import java.util.List;
import java.util.Map;

@Slf4j
public class SoapServerSecurityHandler extends SoapBaseHandler {
    @Override
    public boolean handleMessage(MessageHandlerContext context) {
        log.info("handleMessage is outbound: "+ isOutbound(context));
        return true;
    }

    @Override
    public boolean handleFault(MessageHandlerContext context) {
        log.info("handleFault is outbound: "+ isOutbound(context));
        return true;
    }

    public static void checkLogin(String auth, MessageContext mCtx) {
        Map<String, List<String>> headers = (Map<String, List<String>>) mCtx.get(MessageContext.HTTP_REQUEST_HEADERS);

        int code = AuthUtil.checkBasicAuth(headers, auth);
        if (code != 0) {
            mCtx.put(MessageContext.HTTP_RESPONSE_CODE, code);
            throw new SecurityException();
        }
        log.info("Login is correct.");
    }
}
