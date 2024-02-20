/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import database.MyConnection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author subad
 */
public class signupController {

    public boolean signUp(String username, String password) {
        try (Connection conn = MyConnection.dbConnect(); PreparedStatement checkIfExists = conn.prepareStatement("SELECT * FROM user WHERE Username = ?"); PreparedStatement insertUser = conn.prepareStatement("INSERT INTO user(Username, Password) VALUES (?, ?)")) {

            // Check if the username already exists
            checkIfExists.setString(1, username);
            try (ResultSet rs = checkIfExists.executeQuery()) {
                if (rs.next()) {
                    // Username already exists, return false
                    System.out.println("Username already exists. Please choose a different username.");
                    return false;
                }
            }

            // Username doesn't exist, proceed with the insertion
            insertUser.setString(1, username);
            insertUser.setString(2, password);

            int rowsAffected = insertUser.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data Inserted");
                return true;
            } else {
                System.out.println("Failed to insert data");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

}
