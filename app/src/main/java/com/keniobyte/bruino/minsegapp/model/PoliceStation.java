package com.keniobyte.bruino.minsegapp.model;

/**
 *      - Police station name
 *      - Address
 *      - City
 *      - Phone number.
 * */
public class PoliceStation {

    private String name, city, address, phone;

    public PoliceStation(String name, String city, String address, String phone) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
