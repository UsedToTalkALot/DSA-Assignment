package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.MyConnection;

public class LoginController {

    public boolean isUserValid(String username, String password) {
        boolean isValid = false;

        try (Connection conn = MyConnection.dbConnect();
             PreparedStatement pstSelect = conn.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?");
             PreparedStatement pstClear = conn.prepareStatement("DELETE FROM currentuser");
             PreparedStatement pstInsert = conn.prepareStatement("INSERT INTO currentuser (username, password) VALUES (?, ?)")) {

            // Clear all data present in currentuser table
            pstClear.executeUpdate();

            // Check if the user is valid
            pstSelect.setString(1, username);
            pstSelect.setString(2, password);

            try (ResultSet rs = pstSelect.executeQuery()) {
                isValid = rs.next();

                if (isValid) {
                    // Insert the retrieved data into currentuser table
                    pstInsert.setString(1, rs.getString("username"));
                    pstInsert.setString(2, rs.getString("password"));
                    pstInsert.executeUpdate();
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return isValid;
    }
    public static String getCurrentUser() {
        String currentUserId = "";  // Default value if not found

        String query = "SELECT username FROM currentuser";
        try (Connection conn = MyConnection.dbConnect(); PreparedStatement pst = conn.prepareStatement(query); ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                currentUserId = rs.getString("username");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return currentUserId;
    }
    
    public static int getCurrentUserID() {
    int currentUserId = 0;

    String query = "SELECT id FROM user WHERE username = ?";
    try (Connection conn = MyConnection.dbConnect(); 
         PreparedStatement pst = conn.prepareStatement(query)) {

        pst.setString(1, getCurrentUser());

        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                currentUserId = rs.getInt("id");
            }
        }

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }

    return currentUserId;
}

}
