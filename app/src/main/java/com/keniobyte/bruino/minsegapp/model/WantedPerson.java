package com.keniobyte.bruino.minsegapp.model;

/**
 * @author bruino
 * @version 06/01/17.
 */

public class WantedPerson extends Person {
    private String crime;
    private int reward = 0;

    public String getCrime() {
        return crime;
    }

    public void setCrime(String crime) {
        this.crime = crime;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }
}
