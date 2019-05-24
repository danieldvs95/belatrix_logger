package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

    private ConnectionManager() {}

    private static Connection connection = null;

    private static String DBMS = "postgresql";

    private static String SERVER_NAME = "localhost";

    private static String PORT_NUMBER = "5432";

    private static String USERNAME = "DanielVegaSantos";

    private static String PASSWORD = "";

    private static String DB = "logs";

    public static Connection getConnection() throws SQLException {

        if (connection == null) {
            Properties connectionProps = new Properties();
            connectionProps.put("user", USERNAME);
            connectionProps.put("password", PASSWORD);
            connection = DriverManager.getConnection("jdbc:" + DBMS + "://" + SERVER_NAME
                    + ":" + PORT_NUMBER + "/" + DB, connectionProps);
        }

        return connection;
    }
}
