package com.example.MyBuddy.Model;

public class Topic
{
    String topicName;
    String topicId;
    String imageUrl;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicId() {
        return getTopicId();
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isDilihat() {
        return dilihat;
    }

    public void setDilihat(boolean dilihat) {
        this.dilihat = dilihat;
    }

    String topic;

    public Topic() {
    }

    String sender;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Topic(String topicName, String topicId)//, boolean dilihat) {
    {
        this.topicName = topicName;
        this.topicId = topicId;
        //this.sender = sender;
        //this.type = type;
        //this.timestamp = timestamp;

    }

    String type;

    String timestamp;

    boolean dilihat;
}
