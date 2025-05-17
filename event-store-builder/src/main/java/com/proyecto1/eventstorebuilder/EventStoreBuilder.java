package com.proyecto1.eventstorebuilder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class EventStoreBuilder {
    public void run() throws Exception {
        String brokerUrl = "tcp://localhost:61616";
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = factory.createConnection();
        connection.setClientID("EventStoreBuilderClient");
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        String[] topics = { "TicketmasterEvents", "AmadeusFlights" };

        for (String topicName : topics) {
            Topic topic = session.createTopic(topicName);
            MessageConsumer consumer = session.createDurableSubscriber(topic, "event-store-" + topicName);

            consumer.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    try {
                        String json = ((TextMessage) message).getText();
                        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
                        String source = jsonObject.get("ss").getAsString();
                        OffsetDateTime ts = Instant.parse(jsonObject.get("ts").getAsString()).atOffset(ZoneOffset.UTC);
                        Path dir = Paths.get("eventstore", topicName, source);
                        Files.createDirectories(dir);
                        File file = new File(dir.toFile(),ts.get(ChronoField.YEAR) + String.format("%02d", ts.get(ChronoField.MONTH_OF_YEAR)) +String.format("%02d",  ts.get(ChronoField.DAY_OF_MONTH)) + ".events");
                        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), CREATE, APPEND)) {
                            writer.write(json);
                            writer.newLine();
                            System.out.println(" [" + topicName + "] Evento guardado en: " + file); // Mensaje de depuración
                        }
                    } catch (Exception e) {
                        System.err.println(" Error al procesar mensaje de " + topicName);
                        e.printStackTrace();
                    }
                }
            });
        }
        System.out.println(" Escuchando eventos en: TicketmasterEvents y AmadeusFlights");
        Thread.sleep(Long.MAX_VALUE);
    }
}