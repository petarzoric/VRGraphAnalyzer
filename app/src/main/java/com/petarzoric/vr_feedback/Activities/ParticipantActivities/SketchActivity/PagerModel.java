package com.petarzoric.vr_feedback.Activities.ParticipantActivities.SketchActivity;

import com.petarzoric.vr_feedback.Model.GraphPoint;

import java.util.List;

/**
 * NOT USED ANYMORE (see {@link PagerAdapter})
 *
 * This was my first approach to implement the possibility to comment.
 * Was replaced by the possibility to just tap on the specific phase or point, but the pager itself
 * looks still pretty cool.
 * After the user submitted his graph it popped up under the PaintView.
 * The first page displays a table with all the extreme points and a column for the comment.
 * Second page showed extreme phases, third showed constant phases.
 * The plan was to highlight the specific point/phase on the paintview whenever
 * the focus was on the column for the specific point(not implemented).
 * Didn't work out so well as the keyboard overlapped it (that's why I decided to look for another solution).
 * But in case someone wants to reuse and/or improve it(maybe in another Activity?), I'll let it
 * in the files.
 * Credits: www.youtube.com/watch?v=fgaj-aai90I
 */

public class PagerModel {

    String id;
    String title;
    String type;
    private List<GraphPoint> createdPoints;

    public PagerModel(String id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public List<GraphPoint> getCreatedPoints() {
        return createdPoints;
    }

    public void setCreatedPoints(List<GraphPoint> createdPoints) {
        this.createdPoints = createdPoints;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
