package com.keniobyte.bruino.minsegapp.model;

import java.util.Date;

/**
 * @author bruino
 * @version 05/01/17.
 */

public class Person {
    private int id;
    private String fullName;
    private int age;
    private Date lastTimeSee;
    private String urlProfile;
    private String description;

    public Person() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getLastTimeSee() {
        return lastTimeSee;
    }

    public void setLastTimeSee(Date lastTimeSee) {
        this.lastTimeSee = lastTimeSee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlProfile() {
        return urlProfile;
    }

    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;
    }
}
