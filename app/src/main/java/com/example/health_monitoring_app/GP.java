package com.example.health_monitoring_app;

public class GP {

    private String gpClinic, gpName, gpEmail;
    private int gpId, gpContact;

    public GP(int gpId, int gpContact, String clinic, String gp, String gpEmail) {
        this.setClientID(gpId);
        this.setGpClinic(clinic);
        this.setGpName(gp);
        this.setPassword(gpContact);
        this.setGpEmail(gpEmail);
    }

    public int getClientID() {
        return gpId;
    }

    public void setClientID(int gpId) {
        this.gpId = gpId;
    }

    public String getGpClinic() {
        return gpClinic;
    }

    public void setGpClinic(String username) {
        this.gpClinic = username;
    }

    public int getPassword() {
        return gpContact;
    }

    public void setPassword(int contact) {
        this.gpContact = contact;
    }

    public String getGpName() {
        return gpName;
    }

    public void setGpName(String gp) {
        this.gpName = gpName;
    }

    public String getGpEmail() {
        return gpEmail;
    }

    public void setGpEmail(String email) {
        this.gpEmail = email;
    }



}
