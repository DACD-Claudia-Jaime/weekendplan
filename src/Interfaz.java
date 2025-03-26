import api.AmadeusApi;
import api.TicketmasterApi;
import models.Vuelo;
import models.Evento;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class Interfaz {
    public Interfaz() {
        JFrame ventana = new JFrame("Buscador de Vuelos y Eventos");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(600, 600);
        ventana.setLayout(new BorderLayout());

        JPanel panelVuelos = new JPanel();
        panelVuelos.setLayout(new GridLayout(4, 1));

        JLabel labelOrigen = new JLabel("Origen (Ej: MAD): ");
        JTextField textOrigen = new JTextField();
        JLabel labelDestino = new JLabel("Destino: (Ej: NYC): ");
        JTextField textDestino = new JTextField();
        JLabel labelFecha = new JLabel("Fecha de Salida: (YYYY-MM-DD): ");
        JTextField textFecha = new JTextField();

        JButton botonBuscarVuelos = new JButton("Buscar Vuelos");
        panelVuelos.add(labelOrigen);
        panelVuelos.add(textOrigen);
        panelVuelos.add(labelDestino);
        panelVuelos.add(textDestino);
        panelVuelos.add(labelFecha);
        panelVuelos.add(textFecha);
        panelVuelos.add(new JLabel());
        panelVuelos.add(botonBuscarVuelos);

        JPanel panelEventos = new JPanel();
        panelEventos.setLayout(new GridLayout(2, 2));

        JLabel labelCiudad = new JLabel("Ciudad de Eventos (Ej: Nueva York: ");
        JTextField textCiudad = new JTextField();
        JButton botonBuscarEventos = new JButton("Buscar Eventos");

        panelEventos.add(labelCiudad);
        panelEventos.add(textCiudad);
        panelEventos.add(new JLabel());
        panelEventos.add(botonBuscarEventos);

        JTextArea areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        JScrollPane scrollResultados = new JScrollPane(areaResultados);

        ventana.add(panelVuelos, BorderLayout.NORTH);
        ventana.add(panelEventos, BorderLayout.CENTER);
        ventana.add(scrollResultados, BorderLayout.SOUTH);

        botonBuscarVuelos.addActionListener(e -> {
            String origen = textOrigen.getText();
            String destino = textDestino.getText();
            String fecha = textFecha.getText();

            AmadeusApi amadeusApi = new AmadeusApi();
            List<Vuelo> vuelos = amadeusApi.buscarVuelos(origen, destino, fecha);

            areaResultados.setText("Resultados de vuelos: \n");
            for (Vuelo vuelo : vuelos) {
                areaResultados.append(vuelo.toString() + "\n");
            }
        });


        botonBuscarEventos.addActionListener(e -> {
            String ciudad = textCiudad.getText();
            TicketmasterApi ticketmasterApi = new TicketmasterApi();
            List<Evento> eventos = ticketmasterApi.buscarEventos(ciudad);

            areaResultados.setText("");
            for (Evento evento : eventos) {
                areaResultados.append(evento.toString() + "\n");
            }
        });
        ventana.setVisible(true);
    }
}
