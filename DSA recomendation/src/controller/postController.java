package controller;

import database.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class postController {

    public void addPost(String post) {
        try (Connection conn = MyConnection.dbConnect(); PreparedStatement pstFetchUsername = conn.prepareStatement("SELECT username FROM currentuser"); ResultSet rs = pstFetchUsername.executeQuery(); PreparedStatement pstInsertPost = conn.prepareStatement("INSERT INTO post (username, discription) VALUES (?, ?)"); PreparedStatement pstFetchPostId = conn.prepareStatement("SELECT LAST_INSERT_ID() as id")) {

            // Fetch current user's username
            String username = "";

            int postId = 1;
            if (rs.next()) {
                username = rs.getString("username");
            }

            // Insert post into the post table
            pstInsertPost.setString(1, username);
            pstInsertPost.setString(2, post);

            int rowsAffected = pstInsertPost.executeUpdate();

            if (rowsAffected > 0) {
                // Fetch the post ID

                ResultSet postIdResult = pstFetchPostId.executeQuery();

                if (postIdResult.next()) {
                    postId = postIdResult.getInt("id");
                    System.out.println("Data Inserted, Post ID: " + postId);
                } else {
                    System.out.println("Failed to retrieve the post ID.");
                }
            } else {
                System.out.println("Failed to insert data");
            }

            // Extract hashtags from the post
            ArrayList<String> hashtags = extractHashtags(post);

            for (String table : hashtags) {
                insertOrUpdateTable(table, postId);
            }

            // Print extracted hashtags
            System.out.println("Extracted hashtags: " + hashtags);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private ArrayList<String> extractHashtags(String post) {
        ArrayList<String> hashtags = new ArrayList<>();

        // Define a regex pattern for hashtags
        Pattern pattern = Pattern.compile("#(\\w+)");
        Matcher matcher = pattern.matcher(post);

        // Find hashtags and add the part after the hashtag to the ArrayList
        while (matcher.find()) {
            String hashtagContent = matcher.group(1);
            hashtags.add(hashtagContent);
        }

        return hashtags;
    }

    public int getLikesCount(int postId) {
        int likesCount = 0;

        try (Connection conn = MyConnection.dbConnect(); PreparedStatement pst = conn.prepareStatement("SELECT likeCount FROM post WHERE id = ?")) {

            pst.setInt(1, postId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    likesCount = rs.getInt("likeCount");
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return likesCount;
    }

    public void incrementLikes(int postId, String post) {
        try (Connection conn = MyConnection.dbConnect(); PreparedStatement pst = conn.prepareStatement("UPDATE post SET likeCount = likeCount+ 1 WHERE id = ?")) {

            pst.setInt(1, postId);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        LoginController lc = new LoginController();
        SocialGraph sg = new SocialGraph();
        int user = lc.getCurrentUserID();

        ArrayList<String> hashtags = extractHashtags(post);

        for (String table : hashtags) {
            insertOrUpdateTable(table, postId);
            sg.updatePreference(user, table);

        }

    }

    public void decrementLikes(int postId, String post) {
        try (Connection conn = MyConnection.dbConnect(); PreparedStatement pst = conn.prepareStatement("UPDATE post SET likeCount = likeCount- 1 WHERE id = ?")) {

            pst.setInt(1, postId);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        LoginController lc = new LoginController();
        SocialGraph sg = new SocialGraph();
        int user = lc.getCurrentUserID();

        ArrayList<String> hashtags = extractHashtags(post);

        for (String table : hashtags) {
            insertOrUpdateTable(table, postId);
            sg.updatePreferenceMinus(user, table);

        }
    }

    //insert as per hastag
    public void insertOrUpdateTable(String tableName, int data) {
        try (Connection conn = MyConnection.dbConnect()) {

            // Check if the table exists
            if (!tableExists(conn, tableName)) {
                // If the table doesn't exist, create it
                System.out.println("no such table");
                createTable(conn, tableName);
            }

            // Insert data into the specified table
            insertData(conn, tableName, data);

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private boolean tableExists(Connection conn, String tableName) throws SQLException {
        // Check if the table exists in the database
        try (PreparedStatement pst = conn.prepareStatement("SHOW TABLES LIKE ?")) {
            pst.setString(1, tableName);
            return pst.executeQuery().next();
        }
    }

    private void createTable(Connection conn, String tableName) throws SQLException {
        if (tableName != null && !tableName.trim().isEmpty()) {
            // Create the table with columns: id (primary key, auto-increment) and postid (foreign key)
            String createTableQuery = "CREATE TABLE " + tableName + " ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "postid INT UNIQUE, "
                    + "FOREIGN KEY (postid) REFERENCES post(id))";

            try (PreparedStatement pst = conn.prepareStatement(createTableQuery)) {
                pst.executeUpdate();
                System.out.println("Table '" + tableName + "' created.");
            } catch (SQLException e) {
                System.out.println("Error creating table '" + tableName + "': " + e.getMessage());
            }
        } else {
            System.out.println("Invalid table name: " + tableName);
        }
    }

    private void insertData(Connection conn, String tableName, int data) throws SQLException {
        // Insert data into the specified table
        String insertDataQuery = "INSERT INTO " + tableName + " (postid) VALUES (?)";

        try (PreparedStatement pst = conn.prepareStatement(insertDataQuery)) {
            pst.setInt(1, data);

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data inserted into table '" + tableName + "'.");
            } else {
                System.out.println("Failed to insert data into table '" + tableName + "'.");
            }
        }
    }

    public static Map<String, Integer> getNonZeroPreferences(int userId) {
        Map<String, Integer> nonZeroPreferences = new HashMap<>();

        try (Connection conn = MyConnection.dbConnect()) {
            // Check if the user exists in the preference table
            if (userExistsInPreference(conn, userId)) {
                // Get non-zero preferences for the user
                try (PreparedStatement pst = conn.prepareStatement(
                        "SELECT * FROM preference WHERE user = ?")) {

                    pst.setInt(1, userId);

                    try (ResultSet rs = pst.executeQuery()) {
                        // Retrieve column names from the ResultSet metadata
                        java.sql.ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();

                        while (rs.next()) {
                            for (int i = 2; i <= columnCount; i++) {
                                String columnName = metaData.getColumnName(i);
                                int value = rs.getInt(columnName);

                                if (value != 0) {
                                    nonZeroPreferences.put(columnName, value);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return nonZeroPreferences;
    }

    private static boolean userExistsInPreference(Connection conn, int userId) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(
                "SELECT 1 FROM preference WHERE user = ?")) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next(); // Return true if user exists, false otherwise
            }
        }
    }
    
    public static Set<Integer> getTopLikeCountPostIds(List<String> tableNames) {
        Set<Integer> topPostIds = new HashSet<>();

        // Assuming you have a database connection
        try (Connection connection = MyConnection.dbConnect()) {

            for (String tableName : tableNames) {
                // Prepare the SQL query to get the likeCount of every postid from the given table
                String likeCountQuery = "SELECT " + tableName + ".postid, post.likeCount " +
                        "FROM " + tableName +
                        " LEFT JOIN post ON " + tableName + ".postid = post.id " +
                        "ORDER BY post.likeCount DESC LIMIT 5";

                // Execute the query and get the result set
                try (PreparedStatement likeCountStatement = connection.prepareStatement(likeCountQuery);
                     ResultSet likeCountResultSet = likeCountStatement.executeQuery()) {

                    // Iterate through the result set and add unique postids to the set
                    while (likeCountResultSet.next()) {
                        int postid = likeCountResultSet.getInt("postid");
                        topPostIds.add(postid);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately
        }

        return topPostIds;
    }
    
    

}
