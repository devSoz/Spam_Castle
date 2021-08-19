package com.example.MyBuddy.Model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class user implements Serializable {
    public String uid;
    public String name;
    @SuppressWarnings("WeakerAccess")
    public String email;
    public String imageUrl;
    public Boolean typing;
    @Exclude
    public boolean isAuthenticated;
    @Exclude
    boolean isNew, isCreated;

    public user() {}

    public user(String uid, String name, String email, String imageUrl, Boolean typing, Boolean isNew)
    {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.typing = typing;
        this.isNew = isNew;
    }
}