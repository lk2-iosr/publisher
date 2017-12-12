package iosr.faceboookapp.publisher.stats;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Leszek Placzkiewicz on 07.12.17.
 */
public class Post {

    private final String id;
    private final String message;
    private final String link;
    private final int shares;
    private final int likes;
    private final int comments;
    private final String createdTime;


    @JsonCreator
    public Post(@JsonProperty("id") String id, @JsonProperty("message") String message,
                      @JsonProperty("link") String link, @JsonProperty("shares") int shares,
                      @JsonProperty("likes") int likes, @JsonProperty("comments") int comments,
                      @JsonProperty("createdTime") String createdTime) {
        this.id = id;
        this.message = message;
        this.link = link;
        this.shares = shares;
        this.likes = likes;
        this.comments = comments;
        this.createdTime = createdTime;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getLink() {
        return link;
    }

    public int getShares() {
        return shares;
    }

    public int getLikes() {
        return likes;
    }

    public int getComments() {
        return comments;
    }

    public String getCreatedTime() {
        return createdTime;
    }
}
