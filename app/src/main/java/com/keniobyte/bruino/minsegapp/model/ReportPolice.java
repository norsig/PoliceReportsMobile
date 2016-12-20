package com.keniobyte.bruino.minsegapp.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by bruino on 01/12/16.
 */

public class ReportPolice {
    private Integer anonymus_id;
    private String incident_date, incident_description, address, report_type, perpetrator;
    private double lat, lng;

    public ReportPolice(String incident_date, String incident_description, String report_type) {
        this.incident_date = incident_date;
        this.incident_description = incident_description;
        this.report_type = report_type;
    }

    public Integer getAnonymus_id() {
        return anonymus_id;
    }

    public void setAnonymus_id(Integer anonymus_id) {
        this.anonymus_id = anonymus_id;
    }

    public String getIncident_date() {
        return incident_date;
    }

    public void setIncident_date(Calendar calendar) {
        this.incident_date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
                .format(calendar.getTime());
    }

    public String getIncident_description() {
        return incident_description;
    }

    public void setIncident_description(String incident_description) {
        this.incident_description = incident_description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReport_type() {
        return report_type;
    }

    public void setReport_type(String report_type) {
        this.report_type = report_type;
    }

    public String getPerpetrator() {
        return perpetrator;
    }

    public void setPerpetrator(String perpetrator) {
        this.perpetrator = perpetrator;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
