package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:postgresql://localhost:5432/studs";

    private final String username;
    private final String password;

    private Connection connection;

    public DatabaseManager(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(URL, username, password);
        System.out.println("Connected to PostgreSQL");
    }

    public Connection getConnection() {
        return connection;
    }

}