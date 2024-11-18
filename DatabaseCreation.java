import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreation {
    private static final String JDBC_URL = "";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";
    
    public static void main(String[] args) {
        try {
            // Establish connection to MySQL server
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // Create a statement object
            Statement statement = connection.createStatement();

            // Create the database if it does not exist
            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS airlineDB";
            statement.executeUpdate(createDatabaseQuery);
            System.out.println("Database 'airlineDB' created successfully.");

            // Use the 'airlineDB' database
            statement.executeUpdate("USE airlineDB");

            // Create the 'bookings' table if it does not exist
            String createTableQuery = "CREATE TABLE IF NOT EXISTS bookings (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "passenger_name VARCHAR(255)," +
                    "passenger_email VARCHAR(255)," +
                    "passenger_phone VARCHAR(20)," +
                    "flight_number VARCHAR(10)," +
                    "num_seats INT," +
                    "total_cost DOUBLE" +
                    ")";
            statement.executeUpdate(createTableQuery);
            System.out.println("Table 'bookings' created successfully.");

            // Close the statement and connection
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
