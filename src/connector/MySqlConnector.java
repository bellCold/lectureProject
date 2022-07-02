package connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnector implements DBConnector {
    private final String ADDRESS = "jdbc:mysql://localhost:3306/university";
    private final String USERNAME = "root";
    private final String PASSWORD = "!Q@W3e4r";

    @Override
    public Connection makeConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(ADDRESS, USERNAME, PASSWORD);
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}