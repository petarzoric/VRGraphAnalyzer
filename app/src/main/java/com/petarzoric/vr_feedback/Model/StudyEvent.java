package com.petarzoric.vr_feedback.Model;

/**
 * Represents an Event in a VR Experience. Studies can display events, if
 * the {@link com.petarzoric.vr_feedback.Activities.ParticipantActivities.SketchActivity.SketchActivity}
 * also has an {@link com.petarzoric.vr_feedback.Activities.ParticipantActivities.SketchActivity.EventsBarView}
 */

public class StudyEvent {
    private long id;
    private String name;
    private int time;
    private long study_id;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public StudyEvent() {
    }

    public StudyEvent(String name, int duration) {
        this.name = name;
        this.time = duration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getStudy_id() {
        return study_id;
    }

    public void setStudy_id(long study_id) {
        this.study_id = study_id;
    }
}
