package com.keniobyte.bruino.minsegapp.model;

/**
 * Missing/Wanted person class.
 *      - First and last name.
 *      - Age
 *      - Profile picture (url)
 *      - Crime (For wanted)
 *      - Last time seen (For missing).
 */
public class FindOutPerson {

    private String name, lastTimeSeen, age, crime, urlProfileImage;
    private int id;

    public FindOutPerson(int id,String name, String lastTimeSeen, String age, String crime, String urlProfileImage) {
        this.id = id;
        this.name = name;
        this.lastTimeSeen = lastTimeSeen;
        this.age = age;
        this.crime = crime;
        this.urlProfileImage = urlProfileImage;
    }

    public String getName() {
        return name;
    }

    public String getLastTimeSeen() {
        return lastTimeSeen;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastTimeSeen(String lastTimeSeen) {
        this.lastTimeSeen = lastTimeSeen;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCrime() {
        return crime;
    }

    public void setCrime(String crime) {
        this.crime = crime;
    }

    public String getUrlProfileImage() {
        return urlProfileImage;
    }

    public void setUrlProfileImage(String urlProfileImage) {
        this.urlProfileImage = urlProfileImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
