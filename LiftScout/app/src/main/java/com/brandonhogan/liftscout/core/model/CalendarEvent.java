package com.brandonhogan.liftscout.core.model;

import java.util.Date;

import io.realm.annotations.Required;

/**
 * Created by Brandon on 2/22/2017.
 * Description :
 */

public class CalendarEvent {
    @Required
    private int color;
    @Required
    private Date date;

    public CalendarEvent(int color, Date date) {
        this.color = color;
        this.date = date;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
