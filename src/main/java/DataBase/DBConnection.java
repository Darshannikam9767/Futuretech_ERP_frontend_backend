package DataBase;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    // We use a static Properties object to hold all our secrets
    private static Properties props = new Properties();

    static {
        try {
            // 1. Load the Driver
            System.out.println("Loading Driver.......");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded Successfully......");

            // 2. Load the Properties File (the .env equivalent)
            try (InputStream is = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (is != null) {
                    props.load(is);
                    System.out.println("Secrets loaded successfully from db.properties...");
                } else {
                    System.err.println("CRITICAL ERROR: db.properties file not found in src folder!");
                }
            }
        } catch (Exception e) {
            System.err.println("Initial Setup Failed!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection con = null;
        try {
            // Pull values from the loaded properties
            String conn_string = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            System.out.println("\n\nEstablishing connection with Database........");
            con = DriverManager.getConnection(conn_string, user, password);
            System.out.println("Database connection established successfully...");
        } catch (Exception e) {
            System.err.println("Connection Failed! Check your db.properties values.");
            e.printStackTrace();
        }
        return con;
    }

    // NEW: Helper method to get your Email Password or other secrets
    public static String getProperty(String key) {
        return props.getProperty(key);
    }
}