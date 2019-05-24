package log;

import enums.LogType;
import enums.MessageType;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JobLoggerTest {

    private final String message = "Test Message";

    @Test
    public void logMessageLogTypesEmpty() throws Exception {
        List<MessageType> messageTypes = new ArrayList();
        messageTypes.add(MessageType.MESSAGE);
        messageTypes.add(MessageType.WARNING);

        new JobLogger(null, messageTypes);

        try {
            JobLogger.logMessage(message, MessageType.MESSAGE);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Invalid logger configuration.");
        }
    }

    @Test
    public void logMessageTypesEmpty() {
        List<LogType> logTypes = new ArrayList();
        logTypes.add(LogType.CONSOLE);
        logTypes.add(LogType.DATABASE);

        new JobLogger(logTypes, null);

        try {
            JobLogger.logMessage(message, MessageType.MESSAGE);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Invalid logger configuration.");
        }
    }

    @Test
    public void logMessageWithoutType() {
        List<MessageType> messageTypes = new ArrayList();
        messageTypes.add(MessageType.MESSAGE);
        messageTypes.add(MessageType.WARNING);

        List<LogType> logTypes = new ArrayList();
        logTypes.add(LogType.CONSOLE);

        new JobLogger(logTypes, messageTypes);

        try {
            JobLogger.logMessage(message, null);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "The type of the log message must be specified.");
        }
    }

    @Test
    public void logMessageNotSupported() {
        List<MessageType> messageTypes = new ArrayList();
        messageTypes.add(MessageType.MESSAGE);
        messageTypes.add(MessageType.WARNING);

        List<LogType> logTypes = new ArrayList();
        logTypes.add(LogType.CONSOLE);

        new JobLogger(logTypes, messageTypes);

        try {
            JobLogger.logMessage(message, MessageType.ERROR);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "The type of message is not supported.");
        }
    }

    @Test
    public void logMessage() {
        List<MessageType> messageTypes = new ArrayList();
        messageTypes.add(MessageType.MESSAGE);
        messageTypes.add(MessageType.WARNING);
        messageTypes.add(MessageType.ERROR);

        List<LogType> logTypes = new ArrayList();
        logTypes.add(LogType.CONSOLE);

        new JobLogger(logTypes, messageTypes);

        try {
            JobLogger.logMessage(message, MessageType.WARNING);
        } catch (Exception e) {
        }
    }
}