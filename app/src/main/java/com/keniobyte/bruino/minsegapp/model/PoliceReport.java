package com.keniobyte.bruino.minsegapp.model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by bruino on 01/12/16.
 */

public class PoliceReport {
    public final static String TYPE_POLICE_REPORT_DRUGS = "drugs";
    public final static String TYPE_POLICE_REPORT_AFFAIR = "corruption";
    public final static String TYPE_POLICE_REPORT_AIRCRAFT = "suspect_aircraft";
    public final static String TYPE_POLICE_REPORT_ONLINE = "online";//TODO: defined

    private String typePoliceReport;
    private String incidentAddress;
    private String namePerpetrator;
    private String incidentDescription;
    private String incidentDate;
    private ArrayList<Uri> arrayListUriAttachFile;
    private double latitude;
    private double longitude;

    public PoliceReport() {
    }

    public PoliceReport(String incidentDescription, String typePoliceReport) {
        this.incidentDescription = incidentDescription;
        this.typePoliceReport = typePoliceReport;
    }

    public String getTypePoliceReport() {
        return typePoliceReport;
    }

    public void setTypePoliceReport(String typePoliceReport) {
        this.typePoliceReport = typePoliceReport;
    }

    public String getIncidentAddress() {
        return incidentAddress;
    }

    public void setIncidentAddress(String incidentAddress) {
        this.incidentAddress = incidentAddress;
    }

    public String getNamePerpetrator() {
        return namePerpetrator;
    }

    public void setNamePerpetrator(String namePerpetrator) {
        this.namePerpetrator = namePerpetrator;
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    public String getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(String incidentDate) {
        this.incidentDate = incidentDate;
    }

    public ArrayList<Uri> getArrayListUriAttachFile() {
        return arrayListUriAttachFile;
    }

    public void setArrayListUriAttachFile(ArrayList<Uri> arrayListUriAttachFile) {
        this.arrayListUriAttachFile = arrayListUriAttachFile;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}