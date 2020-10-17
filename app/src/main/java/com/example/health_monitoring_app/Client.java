package com.example.health_monitoring_app;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Client implements Serializable {

    private String username, firstName, lastName, password, email, fullName;
    private int id, gp_id;

    public Client(int id, String username, String firstName, String lastName, String password, String email, int gp_id) {
        this.setClientID(id);
        this.setUsername(username);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setPassword(password);
        this.setEmail(email);
        this.setGpID(gp_id);
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
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

    public int getGpID() {
        return id;
    }

    public void setGpID(int gp_id) {
        this.gp_id = gp_id;
    }

}
