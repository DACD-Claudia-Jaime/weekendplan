package com.proyecto1.businessunit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class RealTimeEventListener {

    private Connection connection;
    private Session session;
    private MessageConsumer ticketmasterConsumer;
    private MessageConsumer amadeusConsumer;
    private final DatamartManager datamartManager;

    public RealTimeEventListener(DatamartManager datamartManager) {
        this.datamartManager = datamartManager;
    }

    public void startListening() throws JMSException {
        String brokerUrl = "tcp://localhost:61616";
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        connection = factory.createConnection();
        connection.setClientID("BusinessUnitRealtimeListener");
        connection.start();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic ticketmasterTopic = session.createTopic("TicketmasterEvents");
        Topic amadeusTopic = session.createTopic("AmadeusFlights");

        ticketmasterConsumer = session.createDurableSubscriber(ticketmasterTopic, "business-unit-ticketmaster");
        amadeusConsumer = session.createDurableSubscriber(amadeusTopic, "business-unit-amadeus");

        ticketmasterConsumer.setMessageListener(message -> {
            if (message instanceof TextMessage) {
                try {
                    String json = ((TextMessage) message).getText();
                    System.out.println("[Tiempo Real - Ticketmaster] Evento recibido: " + json);

                    JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
                    String ts = obj.get("ts").getAsString();
                    String ss = obj.get("ss").getAsString();
                    String nombre = obj.get("nombre").getAsString();
                    String ciudad = obj.get("ciudad").getAsString();
                    String fecha = obj.get("fecha").getAsString();

                    datamartManager.insertSocialEvent(ts, ss, nombre, ciudad, fecha, 0.0); // El precio real puede ser 0 si no se recibe

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        amadeusConsumer.setMessageListener(message -> {
            if (message instanceof TextMessage) {
                try {
                    String json = ((TextMessage) message).getText();
                    System.out.println("[Tiempo Real - Amadeus] Vuelo recibido: " + json);

                    JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
                    String ts = obj.get("ts").getAsString();
                    String ss = obj.get("ss").getAsString();
                    String origen = obj.get("origen").getAsString();
                    String destino = obj.get("destino").getAsString();
                    String fecha = obj.get("fecha").getAsString();
                    double precio = obj.get("precio").getAsDouble();

                    datamartManager.insertFlight(ts, ss, origen, destino, fecha, precio);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("Escuchando eventos en tiempo real en los topics TicketmasterEvents y AmadeusFlights.");
    }

    public void stopListening() throws JMSException {
        if (ticketmasterConsumer != null) ticketmasterConsumer.close();
        if (amadeusConsumer != null) amadeusConsumer.close();
        if (session != null) session.close();
        if (connection != null) connection.close();
        System.out.println("Conexión en tiempo real cerrada.");
    }
}