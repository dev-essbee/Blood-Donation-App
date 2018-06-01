package com.dev.sb.blooddonationapp;

import android.util.Log;

public class UserDetails {
    private String name, age, phNo, gender, bloodGrp, city, email;
    private boolean eligible;

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        Log.d("User Details",this.name);
    }
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
        Log.d("User Details",this.age);
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
        Log.d("User Details",this.phNo);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        Log.d("User Details",this.gender);
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
        Log.d("User Details",this.bloodGrp);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        Log.d("User Details",this.city);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        Log.d("User Details",this.email);
    }
}
