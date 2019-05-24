package log;

import java.io.File;

import enums.LogType;
import enums.MessageType;
import repository.LogValueRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;


public class JobLogger {

    private static List<LogType> logTypes;
    private static List<MessageType> messageTypes;
    private static String FILE_FOLDER = "folder_name";
    private static Logger logger;
    private static LogValueRepository logRepository;

    public JobLogger(List<LogType> logList,
                     List<MessageType> messageList) {
        logger = Logger.getLogger("MyLog");
        logRepository = new LogValueRepository();
        messageTypes = messageList;
        logTypes = logList;
        removeLoggerHandlers();
    }

    public static void logMessage(String messageText, MessageType type) {

        if (messageText == null || messageText.length() == 0) {
            return;
        }
        messageText.trim();

        try {
            validateLogConfig(type);
            handleFileLog();
            handleConsoleLog();
            logger.log(Level.INFO, type.name() + " - " + messageText);
            handleDatabaseLog(messageText, type);
            removeLoggerHandlers();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private static void validateLogConfig(MessageType type) throws Exception {

        if ((logTypes == null || logTypes.isEmpty()) || (messageTypes == null || messageTypes.isEmpty())) {
            throw new Exception("Invalid logger configuration.");
        }

        if (type == null) {
            throw new Exception("The type of the log message must be specified.");
        }

        if (!messageTypes.contains(type)) {
           throw new Exception("The type of message is not supported.");
        }
    }

    private static void handleFileLog() throws IOException {

        if(logTypes.contains(LogType.FILE)) {
            File logFile = new File(FILE_FOLDER + "/logFile.txt");

            if (!logFile.getParentFile().exists()) {
                logFile.getParentFile().mkdirs();
            }

            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            FileHandler fh = new FileHandler(FILE_FOLDER + "/logFile.txt");
            logger.addHandler(fh);
        }
    }

    private static void handleConsoleLog() {
        ConsoleHandler ch = new ConsoleHandler();

        if(logTypes.contains(LogType.CONSOLE)) {
            logger.addHandler(ch);
        }
    }

    private static void handleDatabaseLog(String messageText, MessageType type) {

        if(logTypes.contains(LogType.DATABASE)) {
            try {
                logRepository.insertLog(messageText, type.ordinal() + 1);
            } catch(SQLException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    private static void removeLoggerHandlers() {

        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }
    }
}
