/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import database.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author subad
 */
public class DashController {
    public boolean findFriend(String username) {
        boolean isValid = false;

        try (Connection conn = MyConnection.dbConnect();
             PreparedStatement pstSelect = conn.prepareStatement("SELECT * FROM user WHERE username = ?"))
              {

            

            // Check if the user is valid
            pstSelect.setString(1, username);

            try (ResultSet rs = pstSelect.executeQuery()) {
                isValid = rs.next();

                
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return isValid;
    }
    
}
