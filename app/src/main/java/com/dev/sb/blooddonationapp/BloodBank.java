package com.dev.sb.blooddonationapp;

public class BloodBank {
    private String name,location,phoneNo,lat,lon;

    public BloodBank() {
    }

    public BloodBank(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public BloodBank(String name, String location, String phoneNo, String lat, String lon) {
        this.name = name;
        this.location = location;
        this.phoneNo = phoneNo;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
