package ru.javaops.masterjava.web.handler;

import com.sun.xml.ws.api.handler.MessageHandlerContext;
import ru.javaops.masterjava.web.Statistics;

public class SoapStatisticsHandler extends SoapBaseHandler {
    private static final long startTime = System.currentTimeMillis();

    @Override
    public boolean handleMessage(MessageHandlerContext context) {
        Statistics.count(getPayload(context),startTime, Statistics.RESULT.SUCCESS);
        return true;
    }

    @Override
    public boolean handleFault(MessageHandlerContext context) {
        Statistics.count(getPayload(context),startTime, Statistics.RESULT.FAIL);
        return true;
    }

    private String getPayload(MessageHandlerContext context) {
        return isOutbound(context) ? "SOAP request" : "SOAP response";
    }
}
