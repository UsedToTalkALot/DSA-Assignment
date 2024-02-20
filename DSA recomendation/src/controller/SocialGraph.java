/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author subad
 */
import com.mysql.cj.jdbc.DatabaseMetaData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import database.MyConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SocialGraph {

    private Map<Integer, UserTree> userNodes;

    public SocialGraph() {
        this.userNodes = new HashMap<>();
    }

    public UserTree getUserNode(int userId) {
        return userNodes.get(userId);
    }

    // Function to establish a connection between two users
    public void establishConnection(String folwr_username, String folng_username) {
        try (Connection conn = MyConnection.dbConnect(); PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO connection (follower_username, following_username) VALUES (?, ?)")) {

            pst.setString(1, folwr_username);
            pst.setString(2, folng_username);

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Connection established between users: " + folwr_username + " and " + folng_username);
            } else {
                System.out.println("Failed to establish connection.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void detachConnection(String folwr_username, String folng_username) {
        try (Connection conn = MyConnection.dbConnect(); PreparedStatement pst = conn.prepareStatement(
                "DELETE FROM connection WHERE follower_username = ? AND following_username = ?")) {

            pst.setString(1, folwr_username);
            pst.setString(2, folng_username);

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Connection detached between users: " + folwr_username + " and " + folng_username);
            } else {
                System.out.println("No connection found to detach.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public List<Posts> getPostsFromFollowingUsers() {
        List<Posts> posts = new ArrayList<>();

        try (Connection conn = MyConnection.dbConnect(); PreparedStatement pst = conn.prepareStatement("SELECT post.id, post.username, post.discription, post.likeCount FROM post "
                + "JOIN connection ON post.username = connection.following_username "
                + "WHERE connection.follower_username = (SELECT username FROM currentuser LIMIT 1)")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int postId = rs.getInt("id");
                    String uName = rs.getString("username");
                    String description = rs.getString("discription");
                    int likes = rs.getInt("likeCount");

                    Posts post = new Posts(postId, uName, description, likes);
                    posts.add(post);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return posts;
    }
    
    public List<Posts> getPostsByIds(Set<Integer> postIds) {
        List<Posts> posts = new ArrayList<>();

        if (postIds.isEmpty()) {
            return posts;  // Return an empty list if there are no post IDs
        }

        try (Connection conn = MyConnection.dbConnect()) {
            // Construct the SQL query with a WHERE IN clause to filter by post IDs
            String query = "SELECT id, username, discription, likeCount FROM post WHERE id IN (";
            for (int postId : postIds) {
                query += postId + ",";
            }
            query = query.substring(0, query.length() - 1) + ")";

            try (PreparedStatement pst = conn.prepareStatement(query)) {
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        int postId = rs.getInt("id");
                        String username = rs.getString("username");
                        String description = rs.getString("discription");
                        int likeCount = rs.getInt("likeCount");

                        Posts post = new Posts(postId, username, description, likeCount);
                        posts.add(post);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately
        }

        return posts;
    }
    
    public List<Posts> getCombinedPosts(Set<Integer> postIds) {
        // Call the first function to get posts from following users
        List<Posts> postsFromFollowingUsers = getPostsFromFollowingUsers();

        // Call the second function to get posts by specific IDs
        List<Posts> postsByIds = getPostsByIds(postIds);

        // Combine the two lists
        postsFromFollowingUsers.addAll(postsByIds);

        return postsFromFollowingUsers;
    }

    public List<Posts> getAllPosts() {
        List<Posts> posts = new ArrayList<>();

        try (Connection conn = MyConnection.dbConnect(); PreparedStatement pst = conn.prepareStatement("SELECT * FROM post"); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int postId = rs.getInt("id");
                String uName = rs.getString("username");

                String description = rs.getString("discription");
                int likes = rs.getInt("likeCount");

                Posts post = new Posts(postId, uName, description, likes);
                posts.add(post);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return posts;
    }

    // Method to fetch the current user's ID from the currentuser table
    private int getCurrentUserId() {
        int currentUserId = -1;  // Default value if not found

        String query = "SELECT id FROM currentuser";
        try (Connection conn = MyConnection.dbConnect(); PreparedStatement pst = conn.prepareStatement(query); ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                currentUserId = rs.getInt("id");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return currentUserId;
    }

    public boolean isFollowing(String targetUser, String specificUser) {
        try (Connection conn = MyConnection.dbConnect(); PreparedStatement pst = conn.prepareStatement(
                "SELECT * FROM connection WHERE follower_username = ? AND following_username = ?")) {

            pst.setString(1, targetUser);
            pst.setString(2, specificUser);

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next(); // If the result set has a row, the targetUser is following specificUser
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false; // Return false in case of an exception or database error
        }
    }

    public boolean updatePreference(int userId, String liked) {
        try (Connection conn = MyConnection.dbConnect()) {

            // Check if the user exists in the preference table
            boolean userExists = userExistsInPreference(conn, userId);

            if (!userExists) {
                // If the user doesn't exist, insert a new row
                insertUserIntoPreference(conn, userId);
            }

            // Check if the column exists in the preference table
            boolean columnExists = columnExistsInPreference(conn, liked);

            if (!columnExists) {
                // If the column doesn't exist, add it
                addColumnToPreference(conn, liked);
            }

            // Now, update the preference for the user
            try (PreparedStatement pst = conn.prepareStatement(
                    "UPDATE preference SET " + liked + " = " + liked + " + 1 WHERE user = ?")) {

                pst.setInt(1, userId);

                int rowsAffected = pst.executeUpdate();

                return rowsAffected > 0; // If rows were affected, the update was successful

            } catch (SQLException e) {
                System.out.println("Error updating preference: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false; // Return false in case of an exception or database error
        }
    }
    
    public boolean updatePreferenceMinus(int userId, String liked) {
        try (Connection conn = MyConnection.dbConnect()) {

            // Check if the user exists in the preference table
            boolean userExists = userExistsInPreference(conn, userId);

            if (!userExists) {
                // If the user doesn't exist, insert a new row
                insertUserIntoPreference(conn, userId);
            }

            // Check if the column exists in the preference table
            boolean columnExists = columnExistsInPreference(conn, liked);

            if (!columnExists) {
                // If the column doesn't exist, add it
                addColumnToPreference(conn, liked);
            }

            // Now, update the preference for the user
            try (PreparedStatement pst = conn.prepareStatement(
                    "UPDATE preference SET " + liked + " = " + liked + " - 1 WHERE user = ?")) {

                pst.setInt(1, userId);

                int rowsAffected = pst.executeUpdate();

                return rowsAffected > 0; // If rows were affected, the update was successful

            } catch (SQLException e) {
                System.out.println("Error updating preference: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false; // Return false in case of an exception or database error
        }
    }

    private boolean userExistsInPreference(Connection conn, int userId) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(
                "SELECT 1 FROM preference WHERE user = ?")) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next(); // Return true if user exists, false otherwise
            }
        }
    }

    private void insertUserIntoPreference(Connection conn, int userId) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO preference (user) VALUES (?)")) {
            pst.setInt(1, userId);
            pst.executeUpdate();
        }
    }

    private boolean columnExistsInPreference(Connection conn, String columnName) throws SQLException {
        DatabaseMetaData metaData = (DatabaseMetaData) conn.getMetaData();
        ResultSet rs = null;

        try {
            rs = metaData.getColumns(null, null, "preference", columnName);
            return rs.next(); // Return true if column exists, false otherwise
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    private void addColumnToPreference(Connection conn, String columnName) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement( 
            // Using dynamic SQL to add the column
            "ALTER TABLE preference ADD COLUMN " + columnName + " INT DEFAULT 0")){
            stmt.executeUpdate();
        }

        // Other methods for recommendation algorithms, user interactions, etc., can be added here
    }
}
