package org.camunda.common.libs.messaging.nats;

import io.nats.client.*;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nats {

    //private static Logger logger = LoggerFactory.getLogger(Nats.class.getName());
    private static Logger logger = LoggerFactory.getLogger("commonLogger");

    public static Connection connect(boolean allowReconnect) throws Exception {
        Connection nc = io.nats.client.Nats.connect(createConnectionOptions(Options.DEFAULT_URL, allowReconnect));
        return nc;
    }

    private static Options createConnectionOptions(String server, boolean allowReconnect) throws Exception {
        Options.Builder builder = new Options.Builder().
                server(server).
                connectionTimeout(Duration.ofSeconds(5000)).
                pingInterval(Duration.ofSeconds(10)).
                reconnectWait(Duration.ofSeconds(1000)).
                errorListener(new ErrorListener() {
                    public void exceptionOccurred(Connection conn, Exception exp) {
                        logger.error("Exception " + exp.getMessage());
                    }

                    public void errorOccurred(Connection conn, String type) {
                        logger.error("Error " + type);
                    }

                    public void slowConsumerDetected(Connection conn, Consumer consumer) {
                        logger.debug("Slow consumer");
                    }
                }).
                connectionListener(new ConnectionListener() {
                    public void connectionEvent(Connection conn, Events type) {
                        logger.info("Status change " + type);
                    }
                });

        if (!allowReconnect) {
            builder = builder.noReconnect();
        } else {
            builder = builder.maxReconnects(-1);
        }

        /*
        if (System.getenv("NATS_NKEY") != null && System.getenv("NATS_NKEY") != "") {
            AuthHandler handler = new ExampleAuthHandler(System.getenv("NATS_NKEY"));
            builder.authHandler(handler);
        } else if (System.getenv("NATS_CREDS") != null && System.getenv("NATS_CREDS") != "") {
            builder.authHandler(Nats.credentials(System.getenv("NATS_CREDS")));
        }
         */
        return builder.build();
    }

    public static void subscribe(Connection connection, String subject, MessageHandler handler) {


        logger.info(String.format("[NATS] Listening for %s subject....\n", subject));

        Dispatcher dispatcher = connection.createDispatcher((msg) -> {

            String str = new String(msg.getData(), StandardCharsets.UTF_8);
            logger.info(String.format("[NATS] Message received: %s", str));

            handler.onMessage(msg);

        });

        dispatcher.subscribe(subject);
    }

    public static void publish(String subject, String message) {

        try {

            logger.info(String.format("[NATS] Send message to %s", subject));

            Connection conn = null;

            try {
                conn = connect(false);
                conn.publish(subject, message.getBytes(StandardCharsets.UTF_8));
                conn.flush(Duration.ZERO);
            } finally {
                if (conn != null)
                    conn.close();
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

}
