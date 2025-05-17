package com.proyecto1.businessunit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatamartManager {

    private static final String DB_URL = "jdbc:sqlite:datamart.db";
    private Connection connection;
    public DatamartManager() throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        createTables();
    }
    private void createTables() throws SQLException {
        String createSocialEvents = """
            CREATE TABLE IF NOT EXISTS social_events (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                ts TEXT,
                ss TEXT,
                nombre TEXT,
                ciudad TEXT,
                fecha TEXT,
                precio REAL
            );
            """;
        String createFlights = """
            CREATE TABLE IF NOT EXISTS flights (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                ts TEXT,
                ss TEXT,
                origen TEXT,
                destino TEXT,
                fecha TEXT,
                precio REAL
            );
            """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createSocialEvents);
            stmt.execute(createFlights);
        }
    }
    public void insertSocialEvent(String ts, String ss, String nombre, String ciudad, String fecha, double precio) throws SQLException {
        String sql = "INSERT INTO social_events(ts, ss, nombre, ciudad, fecha, precio) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, ts);
            pstmt.setString(2, ss);
            pstmt.setString(3, nombre);
            pstmt.setString(4, ciudad);
            pstmt.setString(5, fecha);
            pstmt.setDouble(6, precio);
            pstmt.executeUpdate();
        }
    }
    public void insertFlight(String ts, String ss, String origen, String destino, String fecha, double precio) throws SQLException {
        String sql = "INSERT INTO flights(ts, ss, origen, destino, fecha, precio) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, ts);
            pstmt.setString(2, ss);
            pstmt.setString(3, origen);
            pstmt.setString(4, destino);
            pstmt.setString(5, fecha);
            pstmt.setDouble(6, precio);
            pstmt.executeUpdate();
        }
    }

    public List<String> getAllSocialEvents() throws SQLException {
        List<String> results = new ArrayList<>();
        String sql = "SELECT nombre, ciudad, fecha, precio FROM social_events ORDER BY fecha";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                results.add(rs.getString("nombre") + " | " +
                        rs.getString("ciudad") + " | " +
                        rs.getString("fecha") + " | €" +
                        rs.getDouble("precio"));
            }
        }
        return results;
    }
    public List<String> getAllFlights() throws SQLException {
        List<String> results = new ArrayList<>();
        String sql = "SELECT origen, destino, fecha, precio FROM flights ORDER BY fecha";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                results.add(rs.getString("origen") + " -> " +
                        rs.getString("destino") + " | " +
                        rs.getString("fecha") + " | €" +
                        rs.getDouble("precio"));
            }
        }
        return results;
    }
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}