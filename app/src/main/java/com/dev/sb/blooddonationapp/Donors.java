package com.dev.sb.blooddonationapp;

public class Donors {
    private String name, email, bloodGrp, city, phNo;

    public Donors() {
    }

    public Donors(String name, String email, String phNo) {
        this.name = name;
        this.email = email;
        this.phNo = phNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }
}
