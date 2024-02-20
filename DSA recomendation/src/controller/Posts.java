/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author subad
 */
public class Posts {
    public int id;
    public int likes;
    public String username;
    public String discription;
    private List<String> hastags;

    // Constructor
    public Posts(int id, String username, String discription, int like) {
        this.id = id;
        this.username = username;
        this.discription = discription;
        this.likes = like;
        this.hastags = extractHashtags();
    }
    
    

    // Getters (you may also add setters if needed)
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return discription;
    }
    
    public void incrementLikes() {
        postController pc = new postController();
        
        pc.incrementLikes(id,discription);
    }

    // Method to retrieve the current like count
    public int getLikes() {
        postController pc = new postController();
        
        return pc.getLikesCount(id);
    }
    
    public ArrayList<String> extractHashtags() {
        String post = this.discription;
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

}

