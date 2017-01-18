package com.keniobyte.bruino.minsegapp.models;

import java.util.List;

/**
 * @author bruino
 * @version 01/12/16.
 */

public class PoliceReport {
    public final static String TYPE_POLICE_REPORT_DRUGS = "drugs";
    public final static String TYPE_POLICE_REPORT_AFFAIR = "corruption";
    public final static String TYPE_POLICE_REPORT_AIRCRAFT = "suspect_aircraft";
    public final static String TYPE_POLICE_REPORT_ONLINE = "online";

    private String typePoliceReport;
    private String incidentAddress;
    private String namePerpetrator;
    private String incidentDescription;
    private String incidentDate;
    private List<String> listPathsAttachments;
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

    public List<String> getListPathsAttachments() {
        return listPathsAttachments;
    }

    public void setListPathsAttachments(List<String> listPathsAttachments) {
        this.listPathsAttachments = listPathsAttachments;
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