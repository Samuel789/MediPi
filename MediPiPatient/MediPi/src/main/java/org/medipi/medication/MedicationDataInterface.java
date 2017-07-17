package org.medipi.medication;

import org.medipi.MediPiProperties;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class MedicationDataInterface {

    Connection connection;
    Properties properties;

    public MedicationDataInterface() {
        properties = MediPiProperties.getInstance().getProperties();
        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return;

        }
        connection = null;
        String dbhost = properties.getProperty("medipi.db.dbhost");
        String dbname = properties.getProperty("medipi.db.dbname");
        String dbuser = properties.getProperty("medipi.db.username");
        String dbpass = properties.getProperty("medipi.db.password");
        try {
            connection = DriverManager.getConnection(String.format(
                    "jdbc:postgresql://%s/%s", dbhost, dbname), dbuser, dbpass);
        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;

        }

        if (connection == null) {
            System.out.println("Failed to make connection!");
        }
    }

    
}