package com.example.health_monitoring_app;

import com.google.gson.annotations.SerializedName;

public class Client {

    @SerializedName("clientID")
    public int clientID;

    @SerializedName("username")
    public String username;

    @SerializedName("firstName")
    public String firstName;

    @SerializedName("lastName")
    public String lastName;

    @SerializedName("password")
    public String password;

    @SerializedName("email")
    public String email;

}
