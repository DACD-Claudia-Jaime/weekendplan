package com.proyecto1.eventfeeder.controller;

import com.proyecto1.eventfeeder.model.SocialEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteEventStore implements EventStore {
    private final String dbUrl;
    private Connection connection;

    public SqliteEventStore(String databasePath) {
        dbUrl = "jdbc:sqlite:" + databasePath;
        connect();
        createTables();
    }
    @Override
    public void saveEvent(SocialEvent event) {
        String insertSQL = "INSERT OR REPLACE INTO eventos (id, nombre, ciudad, fecha) VALUES (?, ?, ?, ?)";
        connect();
        try (var pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, event.getSs());
            pstmt.setString(2, event.getNombre());
            pstmt.setString(3, event.getCiudad());
            pstmt.setString(4, event.getFecha());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar el evento: " + e.getMessage());
        }
        disconnect();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(dbUrl);
            System.out.println("Conexión con SQLite establecida.");
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
    public void createTables() {
        String createVuelos = """
            CREATE TABLE IF NOT EXISTS vuelos (
                id TEXT PRIMARY KEY,
                origen TEXT,
                destino TEXT,
                fechaSalida TEXT,
                precio REAL
            );
            """;
        String createEventos = """
            CREATE TABLE IF NOT EXISTS eventos (
                id TEXT PRIMARY KEY,
                nombre TEXT,
                ciudad TEXT,
                fecha TEXT
            );
            """;
        connect();
        execute(createVuelos);
        execute(createEventos);
        disconnect();
    }
    private void execute(String sql) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error al ejecutar SQL: " + e.getMessage());
        }
    }
    public Connection getConnection() {
        return connection;
    }
}