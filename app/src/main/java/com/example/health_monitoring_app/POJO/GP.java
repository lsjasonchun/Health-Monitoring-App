package com.example.health_monitoring_app.POJO;

public class GP {

    private String gpClinic, gpName, gpEmail;
    private int gpId, gpContact;

    public GP(int gpId, int gpContact, String clinic, String gp, String gpEmail) {
        this.setGpID(gpId);
        this.setGpClinic(clinic);
        this.setGpName(gp);
        this.setGpContact(gpContact);
        this.setGpEmail(gpEmail);
    }

    public int getGpID() {
        return gpId;
    }

    public void setGpID(int gpId) {
        this.gpId = gpId;
    }

    public String getGpClinic() {
        return gpClinic;
    }

    public void setGpClinic(String username) {
        this.gpClinic = username;
    }

    public int getGpContact() {
        return gpContact;
    }

    public void setGpContact(int contact) {
        this.gpContact = contact;
    }

    public String getGpName() {
        return gpName;
    }

    public void setGpName(String gp) {
        this.gpName = gp;
    }

    public String getGpEmail() {
        return gpEmail;
    }

    public void setGpEmail(String email) {
        this.gpEmail = email;
    }

    public String toString() {
        return gpName+" "+gpClinic+" "+gpEmail;
    }

}
