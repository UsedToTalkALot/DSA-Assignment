package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

    public static Connection dbConnect() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/recomendation", "root", "cpktnwt");
//            System.out.println("Connected to the database :)");
        } 
        catch (ClassNotFoundException e) {
//            System.err.println("Error loading JDBC driver: " + e.getMessage());
        } 
        catch (SQLException e) {
//            System.err.println("Error connecting to the database: " + e.getMessage());
        }

        return conn;
    }
}
