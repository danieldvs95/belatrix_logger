package main;

import enums.LogType;
import enums.MessageType;
import log.JobLogger;

import java.util.ArrayList;
import java.util.List;

public class JobLoggerApplication {

    public static void main(String[] args) {

        List<MessageType> messageTypes = new ArrayList();
        messageTypes.add(MessageType.MESSAGE);
        messageTypes.add(MessageType.WARNING);

        List<LogType> logTypes = new ArrayList();
        //logTypes.add(LogType.CONSOLE);
        logTypes.add(LogType.DATABASE);

        new JobLogger(logTypes, messageTypes);

        JobLogger.logMessage("Test Message", MessageType.MESSAGE);
    }
}
