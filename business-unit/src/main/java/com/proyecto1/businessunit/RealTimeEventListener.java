package com.proyecto1.businessunit;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class RealTimeEventListener {

    private Connection connection;
    private Session session;
    private MessageConsumer ticketmasterConsumer;
    private MessageConsumer amadeusConsumer;
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
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        amadeusConsumer.setMessageListener(message -> {
            if (message instanceof TextMessage) {
                try {
                    String json = ((TextMessage) message).getText();
                    System.out.println("[Tiempo Real - Amadeus] Vuelo recibido: " + json);
                } catch (JMSException e) {
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