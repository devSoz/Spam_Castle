package com.example.MyBuddy.Model;

import java.util.List;

public class Topic
{

    String topicName;
    String Description;
    String topicId;
    String imageUrl;
    String timestamp;
    String lastchat;
    List<String> Users;


    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public List<String> getUserId() {        return Users;    }

    public void setUserId(List<String> userids)
    {        this.Users=userids;   }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getDescription() {
        return Description;
    }
    public String getTopicName() { return topicName;
    }

    public void setName(String Name) {
        this.topicName = Name;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setLastchat(String lastchat) {
        this.lastchat = lastchat;
    }
    public String getLastchat() {
        return lastchat;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }


    public String getTimestamp() {
        return timestamp;
   }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Topic() {
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public Topic(String topicName , String description, String picture,String topicId,String timestamp, String lastchat, List<String> userids)//, boolean dilihat) {
    {
        this.topicName = topicName;
        this.Description = description;
        this.imageUrl = picture;
        this.Users=userids;
        this.topicId = topicId;
        this.lastchat=lastchat;
        this.timestamp = timestamp;

    }


}
