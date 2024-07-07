package gpsProcessing;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


public class GpsProcessing {

    private KafkaConsumer<String, String> consumer;
    private Connection connection;

    public GpsProcessing(String bootstrapServers, String topicSubscribe, String consumerGroup) {

        // Create Kafka consumer.
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(topicSubscribe));

        
        // Establish TimescaleDB database connection.
        try {
            // String dbUrl = System.getenv("POSTGRES_DB_URL");
            // String dbUser = System.getenv("POSTGRES_USER");
            // String dbPassword = System.getenv("POSTGRES_PASSWORD");
            // connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            Properties dbProperties = new Properties();
            dbProperties.setProperty("user", System.getenv("POSTGRES_USER"));
            dbProperties.setProperty("password", System.getenv("POSTGRES_PASSWORD"));
            dbProperties.setProperty("stringtype", "unspecified");

            connection = DriverManager.getConnection(
                System.getenv("POSTGRES_DB_URL"),
                dbProperties
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database");
        }
    }

    public void consumeKafkaMessage() {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                String gpggaMessage = record.value();
                processGpsData(gpggaMessage);

            }
        }
    }

    private void processGpsData(String gpggaMessage) {
        String[] parts = gpggaMessage.split(",");

        if (parts.length < 6) {
            System.err.println("Invalid NMEA sentence");
            return;
        }

        // Extract time, latitude, and longitude
        String time = parts[1];
        String rawLatitude = parts[2];
        String latitudeDirection = parts[3];
        String rawLongitude = parts[4];
        String longitudeDirection = parts[5];

        // Convert raw latitude and longitude to decimal degrees
        double latitude = convertToDecimalDegrees(rawLatitude, latitudeDirection);
        double longitude = convertToDecimalDegrees(rawLongitude, longitudeDirection);

        // Convert time to ISO 8601 format
        IsoAndUnixTime isoAndUnixTime = convertToIso8601AndUnixTime(time);
        
        // Insert data into TimescaleDB
        insertGpsData(isoAndUnixTime.isoTime, isoAndUnixTime.unixTime, latitude, longitude);
        System.out.println("\nTime: " + isoAndUnixTime.isoTime + ", "+ isoAndUnixTime.unixTime + ", Lat: " + latitude + ", Long: " + longitude);
    }

    private class IsoAndUnixTime {
        String isoTime;
        long unixTime;
    }

    private IsoAndUnixTime convertToIso8601AndUnixTime(String rawTime) {
        if (rawTime.length() != 9) {
            System.err.println("Invalid time format");
            return null;
        }

        String hours = rawTime.substring(0, 2);
        String minutes = rawTime.substring(2, 4);
        String seconds = rawTime.substring(4, 6);

        // Get today's date
        LocalDate today = LocalDate.now();
        // Create LocalDateTime with today's date and extracted time
        LocalDateTime dateTime = LocalDateTime.of(
            today.getYear(), today.getMonth(), today.getDayOfMonth(), 
            Integer.parseInt(hours), 
            Integer.parseInt(minutes), 
            Integer.parseInt(seconds)
        );

        // Format to ISO 8601
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String isoTime = dateTime.format(formatter);

        // Convert to Unix timestamp in seconds
        long unixTime = dateTime.toEpochSecond(ZoneOffset.UTC);

        IsoAndUnixTime time = new IsoAndUnixTime();
        time.isoTime = isoTime;
        time.unixTime = unixTime;
        return time;
    }

    private void insertGpsData(String isoTime, long unixTime, double latitude, double longitude) {
        String insertSql = "INSERT INTO gps_data (timeISO, time, latitude, longitude) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
            preparedStatement.setString(1, isoTime);
            preparedStatement.setLong(2, unixTime);
            preparedStatement.setDouble(3, latitude);
            preparedStatement.setDouble(4, longitude);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to insert GPS data into TimescaleDB");
        }
    }

    private double convertToDecimalDegrees(String rawCoordinate, String direction) {
        int degrees = Integer.parseInt(rawCoordinate.substring(0, 2));
        double minutes = Double.parseDouble(rawCoordinate.substring(2));

        double decimalDegrees = degrees + (minutes / 60);

        if (direction.equals("S") || direction.equals("W")) {
            decimalDegrees = -decimalDegrees;
        }

        return decimalDegrees;
    }

    public void close() {
        consumer.close();
    }
}
