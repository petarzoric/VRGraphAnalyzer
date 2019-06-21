package com.petarzoric.vr_feedback.Activities.ParticipantActivities.SketchActivity;

import com.petarzoric.vr_feedback.Model.Location;

import java.util.ArrayList;

/**
 * Represents the point on the {@link DrawingArea} which the user has to drag in order to draw.
 */
public class CanvasPointer {
    float x = 300;
    float y = 300;
    float radius = 20;
    ArrayList<Location> lastLocations = new ArrayList<>();


    public CanvasPointer(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public ArrayList<Location> getLastLocations() {
        return lastLocations;
    }

    public void setLastLocations(ArrayList<Location> lastLocations) {
        this.lastLocations = lastLocations;
    }

    public void setLocation(Location location){
        setX((Float)location.getX());
        setY((Float)location.getY());
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
