package com.dev.sb.blooddonationapp;

public class OtherUsers {
    private String name, email, bloodGrp, location, phnNo;

    public OtherUsers() {
    }

    public OtherUsers(String name, String email, String bloodGrp, String location, String phnNo) {
        this.name = name;
        this.email = email;
        this.bloodGrp = bloodGrp;
        this.location = location;
        this.phnNo = phnNo;
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

    public String getBloodGrp() {
        return bloodGrp;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhnNo() {
        return phnNo;
    }

    public void setPhnNo(String phnNo) {
        this.phnNo = phnNo;
    }
}
