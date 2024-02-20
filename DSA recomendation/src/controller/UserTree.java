package controller;

import database.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserTree {

    public String username;
    public List<String> followers;
    public List<String> following;
    public List<String> mutual;
    

    public UserTree(String uName) {
        this.username = uName;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.followers = getFollowerUsers(uName);
        this.following = getFollowedUsers(uName);
        this.mutual = getCommonUsers(uName);
    }

    public void addFollower(String follower) {
        this.followers.add(follower);
    }

    public void addFollowing(String following) {
        this.following.add(following);
    }

    public List<String> getFollowers() {
        return this.followers;
    }

    public List<String> getFollowing() {
        return this.following;
    }

    private List<String> getFollowedUsers(String username) {
        List<String> followingUsers = new ArrayList<>();

        try (Connection conn = MyConnection.dbConnect(); PreparedStatement pst = conn.prepareStatement("SELECT following_username FROM connection WHERE follower_username = ?")) {

            pst.setString(1, username);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String followingUsername = rs.getString("following_username");
                    followingUsers.add(followingUsername);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return followingUsers;
    }

    private List<String> getFollowerUsers(String username) {
        List<String> followerUsers = new ArrayList<>();

        try (Connection conn = MyConnection.dbConnect(); PreparedStatement pst = conn.prepareStatement("SELECT follower_username FROM connection WHERE following_username  = ?")) {

            pst.setString(1, username);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String followingUsername = rs.getString("follower_username");
                    followerUsers.add(followingUsername);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return followerUsers;
    }

    private List<String> getCommonUsers(String username) {
        List<String> commonUsers = new ArrayList<>();

        List<String> followedUsers = getFollowedUsers(username);
        List<String> followerUsers = getFollowerUsers(username);

        for (String user : followedUsers) {
            if (followerUsers.contains(user)) {
                commonUsers.add(user);
            }
        }

        return commonUsers;

    }

    public List<String> getRecommendation(String targetUser) {
        List<String> recommendations = new ArrayList<>();

        // Get the mutual users
        List<String> mutualUsers = this.mutual;

        // Iterate through mutual users
        for (String mutualUser : mutualUsers) {
            // Get the users followed by the mutual user
            List<String> usersFollowedByMutualUser = new ArrayList<>(new UserTree(mutualUser).getFollowing());

            // Check if the user is not followed by the target user
            for (String user : usersFollowedByMutualUser) {
                if (!this.following.contains(user) && !user.equals(targetUser) && !user.equals(LoginController.getCurrentUser())) {
                    recommendations.add(user);
                }
            }
        }

        return recommendations;
    }

}
