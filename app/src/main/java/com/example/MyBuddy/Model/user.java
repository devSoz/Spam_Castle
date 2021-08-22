package com.example.MyBuddy.Model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.List;

public class user implements Serializable {
    private String uid;
    private String name;

    private String email;
    private String imageUrl;
    private Boolean istyping;
   // @Exclude
   // private boolean isAuthenticated;
    private Boolean isNew;

    public String getuid() {
        return uid;
    }
    public String getuserName() {
        return name;
    }
    public String getEmail() { return email; }
    public String getUserImageUrl() { return imageUrl; }
    public Boolean getisNew() {
        return isNew;
    }
    public Boolean getisTyping() { return istyping; }

    public void setuserName(String Name) {
        this.name = Name;
    }
    public void setUserImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setuid(String uid) { this.uid = uid; }
    public void setisNew(Boolean isNew) {
        this.isNew = isNew;
    }
    public void setisTyping(Boolean typing) { this.istyping = typing; }


    public user() {}

    public user(String uid, String name, String email, String imageUrl, Boolean istyping, Boolean isNew)
    {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.istyping = istyping;
        this.isNew = isNew;
    }


}