package repository;

import connection.ConnectionManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

public class LogValueRepository {

    public void insertLog(String messageText, int type) throws SQLException {
        Connection connection = null;

        Statement stmt = null;
        try {
            stmt = ConnectionManager.getConnection().createStatement();
            stmt.executeUpdate("insert into Log_Values(message, type) VALUES('" + messageText + "', " + type + ")");
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

}
