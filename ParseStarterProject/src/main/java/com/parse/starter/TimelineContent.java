package com.parse.starter;

import java.util.Date;

/**
 * Created by Nasser on 4/20/2018.
 */

public class TimelineContent implements Comparable<TimelineContent>{

    private String actor;
    private String type;
    private String eventid;
    private Date date;

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int compareTo(TimelineContent timelineContent) {
        //Date comparedate = ((TimelineContent) timelineContent).getDate();
        //ascending order
        //return this.getDate() - comparedate;
        return getDate().compareTo(timelineContent.getDate());
        //descending order
        //return compareQuantity - this.quantity;

    }

}
