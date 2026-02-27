package com.oceanview.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private static DatabaseConnectionManager instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/oceanview";
    private static final String USERNAME = "root";
    private static final String PASSWORD = ""; // change if needed

    private DatabaseConnectionManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConnectionManager getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}