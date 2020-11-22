
package io.helidon.examples.oracle.madhacks.evo;

import java.util.concurrent.atomic.AtomicReference;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
///////////////



import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


import io.helidon.config.Config;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

import java.io.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.stream.Stream;

import java.sql.Connection;
import java.sql.DriverManager;

import oracle.soda.rdbms.OracleRDBMSClient;
import oracle.soda.OracleDatabase;
import oracle.soda.OracleCursor;
import oracle.soda.OracleCollection;
import oracle.soda.OracleDocument;
import oracle.soda.OracleException;


///////////////

/**
 * Provider for greeting message.
 */
@ApplicationScoped
public class EvoProvider {
    private final AtomicReference<String> message = new AtomicReference<>();

    /**
     * Initialise Connection object
     */
    public static Connection conn = null;

    /**
     * Initialise OracleDatabase object
     */
    public static OracleDatabase db = null;

    /**
     * database data
     */
    private static boolean UseDB = true;
    private final static String ATP_CONNECT_NAME = "sockshopdb_medium";
    private final static String ATP_PASSWORD_FILENAME = "atp_password.txt";
    private final static String WALLET_LOCATION = "/home/opc/Wallet_atpsodadb";
    private final static String DB_URL = "jdbc:oracle:thin:@" + ATP_CONNECT_NAME + "?TNS_ADMIN=" + WALLET_LOCATION;
    private final static String DB_USER = "admin";
    private static String DB_PASSWORD;

    /**
     * Create a new greeting provider, reading the message from configuration.
     *
     * @param message greeting to use
     */
    @Inject
    public EvoProvider(@ConfigProperty(name = "app.greeting") String message) {
        this.message.set(message);
    }

    String getMessage() {
        return message.get();
    }

    void setMessage(String message) {
        this.message.set(message);
    }


    public OracleDatabase dbConnect(){
        try {

            /**
             * Connect to ATP and verify database connectivity
             */
            System.out.println("\n**checking DB ATP SODA");

            // load password from file in wallet location
            StringBuilder contentBuilder = new StringBuilder();
            try (Stream<String> stream = Files.lines( Paths.get(WALLET_LOCATION + "/" + ATP_PASSWORD_FILENAME), StandardCharsets.UTF_8)) {
                    stream.forEach(s -> contentBuilder.append(s).append("\n"));
                }
            catch (IOException e) {
                e.printStackTrace();
            }

            DB_PASSWORD = contentBuilder.toString();

            // set DB properties
            Properties props = new Properties();
            props.setProperty("user", DB_USER);
            props.setProperty("password", DB_PASSWORD);


            // Get a JDBC connection to an Oracle instance.
            conn = DriverManager.getConnection(DB_URL, props);

             // Get an OracleRDBMSClient - starting point of SODA for Java application.
             OracleRDBMSClient cl = new OracleRDBMSClient();

             // Get a database.
             db = cl.getDatabase(conn);

            System.out.println("DB Connection established successfully!!!");
         

        } catch (Exception e) {
            e.printStackTrace();
        }
        return db;
    }


}
