package com.petarzoric.vr_feedback.Model;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Model that represents a VR study.
 * It contains name, creator, image, the date and the configuration.
 */

public class Study {

    private Configuration configuration;
    private long id;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    private String name;
    private String creator;
    private String date;
    private String image;
    private String description;
    private int duration;
    private List<StudyEvent> events = new ArrayList<StudyEvent>();

    public Study(String name, String creator) {
        this.name = name;
        this.creator = creator;
        configuration = new Configuration();
        configuration.configureExtremePhaseParams(0.035f, 0.005f, 0.01f);
        configuration.configureConstantPhaseParams(0.035f, 0.022f, 0.012f, 0.65f);
        configuration.setPointsQuantity(200);
        configuration.setExtremePointsFilter(0.2f);
    }

    public Study(String name, String creator, int duration) {
        this.name = name;
        this.creator = creator;
        this.duration = duration;
        configuration = new Configuration();
        configuration.configureExtremePhaseParams(0.035f, 0.005f, 0.01f);
        configuration.configureConstantPhaseParams(0.035f, 0.022f, 0.012f, 0.65f);
        configuration.setPointsQuantity(200);
        configuration.setExtremePointsFilter(0.2f);

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Study(String name, String creator, int duration, String description) {
        this.name = name;
        this.creator = creator;
        this.duration = duration;
        this.description = description;

        configuration = new Configuration();
        configuration.configureExtremePhaseParams(0.035f, 0.005f, 0.01f);
        configuration.configureConstantPhaseParams(0.035f, 0.022f, 0.012f, 0.65f);
        configuration.setPointsQuantity(200);
        configuration.setExtremePointsFilter(0.2f);

    }

    public Study(String name, String creator, String image) {

        this.name = name;
        this.creator = creator;
        this.image = image;
        configuration = new Configuration();
        configuration.configureExtremePhaseParams(0.035f, 0.005f, 0.01f);
        configuration.configureConstantPhaseParams(0.035f, 0.022f, 0.012f, 0.65f);
        configuration.setPointsQuantity(200);
        configuration.setExtremePointsFilter(0.2f);
    }

    public List<StudyEvent> getEvents() {
        return events;
    }

    public void setEvents(List<StudyEvent> events) {
        this.events = events;
    }

    public void addEvent(StudyEvent event){
        events.add(event);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDate() {
        return date;
    }

    public Study() {
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDate(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        String month = Integer.toString(calendar.get(Calendar.MONTH));
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        String today = day +"."+month;
        this.date = today;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static String formatDateTime(Context context, String timeToFormat) {

        String finalDateTime = "";

        SimpleDateFormat iso8601Format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        Date date = null;
        if (timeToFormat != null) {
            try {
                date = iso8601Format.parse(timeToFormat);
            } catch (ParseException e) {
                date = null;
            }

            if (date != null) {
                long when = date.getTime();
                int flags = 0;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
                flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

                finalDateTime = android.text.format.DateUtils.formatDateTime(context,
                        when + TimeZone.getDefault().getOffset(when), flags);
            }
        }
        return finalDateTime;
    }
}
