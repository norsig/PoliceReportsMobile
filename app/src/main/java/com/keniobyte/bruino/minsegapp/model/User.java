package com.keniobyte.bruino.minsegapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 *      - First and last name
 *      - Social security number
 *      - Birthday
 *      - Address
 *      - Nationality
 *      - Email
 *      - Job/Occupation
 *      - Marital status
 *      - Phone number
 */
public class User {
    private String name, lastName, address, nationality, email, job, maritalStatus, phone;
    private int dni;
    private Date birthday;

    public User(String name, String lastName, String address, String nationality, String email, String job, String maritalStatus, int dni, Date birthday) {
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.nationality = nationality;
        this.email = email;
        this.job = job;
        this.maritalStatus = maritalStatus;
        this.dni = dni;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public JSONObject getJson(){
        JSONObject json = new JSONObject();
        try {
            json.put("name", getName())
                    .put("lastName", getLastName())
                    .put("dni", getDni())
                    .put("phone", getPhone())
                    .put("address", getAddress())
                    .put("nationality", getNationality())
                    .put("birthday", getBirthday())
                    .put("marginalState", getMaritalStatus())
                    .put("job", getJob())
                    .put("email", getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
