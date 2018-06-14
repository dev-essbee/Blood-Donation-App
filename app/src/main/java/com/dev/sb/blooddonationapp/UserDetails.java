package com.dev.sb.blooddonationapp;

import android.util.Log;

public class UserDetails {
    private String name;
    private String dob;
    private String phNo;
    private String gender;
    private String bloodGrp;
    private String city;

    private String eligible;
    private String verified;

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }



    public String getEligible() {
        return eligible;
    }

    public void setEligible(String eligible) {
        this.eligible = eligible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        Log.d("User Details", this.name);
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
        Log.d("User Details", this.dob);
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
        Log.d("User Details", this.phNo);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        Log.d("User Details", this.gender);
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
        Log.d("User Details", this.bloodGrp);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        Log.d("User Details", this.city);
    }
}
