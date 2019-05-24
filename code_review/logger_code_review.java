import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobLogger {

	// This requirement "We also want the ability to selectively be able to choose what gets logged, such as to be able to log only errors or only errors and warnings".
	// is not acomplish with the code written below.
	// The type of log allowed is not validated.

	// These variables can be more easier to handle with enums.
	private static boolean logToFile;
	private static boolean logToConsole;
	private static boolean logMessage;
	private static boolean logWarning;
	private static boolean logError;
	private static boolean logToDatabase;
	private boolean initialized; // This variable is never used
	private static Map dbParams;
	private static Logger logger;

	public JobLogger(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
			boolean logMessageParam, boolean logWarningParam, boolean logErrorParam, Map dbParamsMap) {
		logger = Logger.getLogger("MyLog");  
		logError = logErrorParam;
		logMessage = logMessageParam;
		logWarning = logWarningParam;
		logToDatabase = logToDatabaseParam;
		logToFile = logToFileParam;
		logToConsole = logToConsoleParam;
		dbParams = dbParamsMap;
	}

	// After the message is logged, it is required to remove the handlers in order to support multiple calls without multiplying handlers.
	public static void LogMessage(String messageText, boolean message, boolean warning, boolean error) throws Exception {
		messageText.trim(); // if the messageText is null, this statement can throw a NullPointerException
		if (messageText == null || messageText.length() == 0) {
			return;
		}
		if (!logToConsole && !logToFile && !logToDatabase) {
			throw new Exception("Invalid configuration");
		}
		if ((!logError && !logMessage && !logWarning) || (!message && !warning && !error)) {
			throw new Exception("Error or Warning or Message must be specified");
		}

		Connection connection = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", dbParams.get("userName")); // if dbParams is null, this statement can throw a NullPointerException.
		connectionProps.put("password", dbParams.get("password")); // if dbParams is null, this statement can throw a NullPointerException.

		connection = DriverManager.getConnection("jdbc:" + dbParams.get("dbms") + "://" + dbParams.get("serverName")
				+ ":" + dbParams.get("portNumber") + "/", connectionProps);  // if dbParams is null, this statement can throw a NullPointerException.
				// This statement has to be inside a try catch in order to control the SQLException that can be thrown by.

		int t = 0; // Because the constructor allow the message to be (message and/or error and/or warning)
		// the type of the message to be store in the database log, its going to be overwritten.
		// The problem specifically says that it can mark a message as an (message || error || warning)
		if (message && logMessage) {
			t = 1;
		}

		if (error && logError) {
			t = 2;
		}

		if (warning && logWarning) {
			t = 3;
		}
		

		Statement stmt = connection.createStatement(); // Check the state of the connection object.

		String l = null; // this variable is never used.
		File logFile = new File(dbParams.get("logFileFolder") + "/logFile.txt");
		if (!logFile.exists()) {
			logFile.createNewFile(); // this can throw an IOException if the parent directory of the file does not exist.
		}
		
		FileHandler fh = new FileHandler(dbParams.get("logFileFolder") + "/logFile.txt");
		ConsoleHandler ch = new ConsoleHandler();
		
		// "l" is never used, so the blocks of code below are never used.
		if (error && logError) {
			l = l + "error " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
		}

		if (warning && logWarning) {
			l = l + "warning " +DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
		}

		if (message && logMessage) {
			l = l + "message " +DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
		}
		
		if(logToFile) {
			logger.addHandler(fh);
			logger.log(Level.INFO, messageText);
		}
		
		if(logToConsole) {
			logger.addHandler(ch);
			logger.log(Level.INFO, messageText);// This statement is redundant (same above)
		}
		
		if(logToDatabase) {
			// It would be a good idea to add a timestamp, like a "created_at" column to track the time when the log was inserted.
			stmt.executeUpdate("insert into Log_Values('" + message + "', " + String.valueOf(t) + ")");"
			// Theere is a postgresql syntax error in here. It needs the VALUES keyword --> "insert into Log_Values *VALUES*('" + message + "', " + String.valueOf(t) + ")"
			//The connection and the statement has to be closed and requires to handle the respective Exceptions
		}
	}
}