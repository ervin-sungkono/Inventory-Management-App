package com.example.finalprojectmobile.models;

public class User {
    private String id;
    private String username, email;
    private byte[] profilePic;

    public User(String id, String username, String email, byte[] profilePic) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profilePic = profilePic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }
}
