package com.proyecto1.flightfeeder.controller;

import com.proyecto1.flightfeeder.model.Flight;
import java.sql.*;

public class SqliteFlightStore implements FlightStore {
    private final String dbPath;
    public SqliteFlightStore(String dbPath) {
        this.dbPath = dbPath;
        createTableIfNotExists();
    }
    private void createTableIfNotExists() {
        String sql = """
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
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println(" Error al crear la tabla de vuelos:");
            e.printStackTrace();
        }
    }
    @Override
    public void saveFlight(Flight flight) {
        String sql = "INSERT INTO flights(ts, ss, origen, destino, fecha, precio) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, flight.getTs().toString());
            pstmt.setString(2, flight.getSs());
            pstmt.setString(3, flight.getOrigen());
            pstmt.setString(4, flight.getDestino());
            pstmt.setString(5, flight.getFecha());
            pstmt.setDouble(6, flight.getPrecio());
            pstmt.executeUpdate();
            System.out.println(" Vuelo guardado en base de datos: " + flight);
        } catch (SQLException e) {
            System.err.println(" Error al guardar vuelo en SQLite:");
            e.printStackTrace();
        }
    }
}