package com.scc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:trainingSession.db";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    public static void initializeDatabase() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS training_sessions (
                    id           INTEGER PRIMARY KEY AUTOINCREMENT,
                    client_name  TEXT    NOT NULL,
                    trainer_name TEXT    NOT NULL,
                    session_fee  REAL    NOT NULL,
                    status       TEXT    NOT NULL
                        CHECK (status IN ('SCHEDULED', 'COMPLETED'))
                )
                """;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
}