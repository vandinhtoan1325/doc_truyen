package com.example.doc_truyen.model;

public class Users {


    String username, email, id;

    public Users(String username, String email, String id) {
        this.username = username;
        this.email = email;
        this.id = id;
    }

    public Users() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
