package com.example.dontyoudare;

public class Users {

    private String User;
    private String email;
    private String userId;

    //Senden Daten an Firebase
    public Users() {

    }

    public String getUser() {
        return User;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUser(String user) {
        User = user;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
