package com.parse.starter;

import com.parse.ParseFile;

/**
 * Created by Nasser on 3/29/2018.
 */

public class Tickets {

   private String eventid;
   private String eventname;
   private String eventdate;

    public void setEventdate(String eventdate) {
        this.eventdate = eventdate;
    }

    public String getEventdate() {

        return eventdate;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getEventname() {

        return eventname;
    }

    public String getEventid() {
        return eventid;
    }

    public ParseFile getQrcode() {
        return qrcode;
    }

    public void setEventid(String eventname) {

        this.eventid = eventname;
    }

    public void setQrcode(ParseFile qrcode) {
        this.qrcode = qrcode;
    }

    private ParseFile qrcode;

}
