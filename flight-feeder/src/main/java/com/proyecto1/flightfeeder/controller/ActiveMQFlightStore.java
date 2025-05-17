package com.proyecto1.flightfeeder.controller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.proyecto1.flightfeeder.model.Flight;
import com.proyecto1.flightfeeder.util.InstantAdapter;
import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.io.IOException;
import java.time.Instant;

public class ActiveMQFlightStore implements FlightStore {
    private final String url;
    private final String topic;
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private Gson gson;

    public ActiveMQFlightStore(String url, String topic) {
        this.url = url;
        this.topic = topic;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();
        setupConnection();
    }
    private void setupConnection() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(topic);

            producer = session.createProducer(destination);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void saveFlight(Flight flight) {
        try {
            String jsonFlight = gson.toJson(flight);
            TextMessage message = session.createTextMessage(jsonFlight);
            producer.send(message);
            System.out.println(" Vuelo enviado: " + jsonFlight);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    public void close() {
        try {
            if (producer != null) producer.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}