package com.example.health_monitoring_app;

import com.google.gson.annotations.SerializedName;

public class Client {

    private String username, firstName, lastName, password, email, fullName;
    private int id;

    public Client(int id, String username, String firstName, String lastName, String password, String email) {
        this.setClientID(id);
        this.setUsername(username);
        this.setLastName(lastName);
        this.setPassword(password);
        this.setEmail(email);
        fullName = firstName + lastName;
    }

    public int getClientID() {
        return id;
    }

    public void setClientID(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername() {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName() {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullname() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
