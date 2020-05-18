package com.se.masakapa.models;

public class User {
    private String userId;
    private String profilePicture;
    private String email;
    private String displayName;
    public User(String userId, String email, String displayName, String profilePicture){
        this.userId = userId;
        this.profilePicture = profilePicture;
        this.email = email;
        this.displayName = displayName;
    }

    public User(String email, String displayName, String profilePicture){
        this.profilePicture = profilePicture;
        this.email = email;
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
