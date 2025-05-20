package com.yourname.voiceassistant.services;

import java.sql.*;

public class DatabaseService {
    private static final String DB_URL = "jdbc:sqlite:assistant.db";

    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Create preferences table
            String sql = "CREATE TABLE IF NOT EXISTS preferences (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "key TEXT NOT NULL UNIQUE," +
                    "value TEXT NOT NULL)";
            stmt.execute(sql);

            // Create reminders table
            sql = "CREATE TABLE IF NOT EXISTS reminders (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "description TEXT NOT NULL," +
                    "reminder_time TEXT NOT NULL," +
                    "created_at TEXT DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(sql);

        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }

    public static void setPreference(String key, String value) {
        String sql = "INSERT OR REPLACE INTO preferences(key, value) VALUES(?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, key);
            pstmt.setString(2, value);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error setting preference: " + e.getMessage());
        }
    }

    public static String getPreference(String key) {
        String sql = "SELECT value FROM preferences WHERE key = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, key);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("value");
            }
        } catch (SQLException e) {
            System.err.println("Error getting preference: " + e.getMessage());
        }
        return null;
    }

    // Additional methods for reminders would go here
}