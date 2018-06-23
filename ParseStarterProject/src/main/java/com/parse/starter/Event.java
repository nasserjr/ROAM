package com.parse.starter;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nasser on 3/8/2018.
 */

public class Event{

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    private LatLng location;
    private String id;
    private  String name;
    private String interest;
    private String createdby;
    private Date createdAt;
    private String eventdate;
    private String eventtime;
    private String eventdescription;
    private String address;
    private int numbofattendees;
    private int minage;
    private int maxage;
    private String allowedgender;
    private ParseFile eventpicture;
    private List<String> othermanagers = new ArrayList<>();
    private float rate;
    private int promoted;

    public int getPromoted() {
        return promoted;
    }

    public void setPromoted(int promoted) {
        this.promoted = promoted;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getEventdate() {
        return eventdate;
    }

    public void setEventdate(String eventdate) {
        this.eventdate = eventdate;
    }

    public String getEventtime() {
        return eventtime;
    }

    public void setEventtime(String eventtime) {
        this.eventtime = eventtime;
    }

    public String getEventdescription() {
        return eventdescription;
    }

    public void setEventdescription(String eventdescription) {
        this.eventdescription = eventdescription;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumbofattendees() {
        return numbofattendees;
    }

    public void setNumbofattendees(int numbofattendees) {
        this.numbofattendees = numbofattendees;
    }

    public int getMinage() {
        return minage;
    }

    public void setMinage(int minage) {
        this.minage = minage;
    }

    public int getMaxage() {
        return maxage;
    }

    public void setMaxage(int maxage) {
        this.maxage = maxage;
    }

    public String getAllowedgender() {
        return allowedgender;
    }

    public void setAllowedgender(String allowedgender) {
        this.allowedgender = allowedgender;
    }

    public ParseFile getEventpicture() {
        return eventpicture;
    }

    public void setEventpicture(ParseFile eventpicture) {
        this.eventpicture = eventpicture;
    }

    public List<String> getOthermanagers() {
        return othermanagers;
    }

    public void setOthermanagers(List<String> denied) {
        this.othermanagers = denied;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }



}
