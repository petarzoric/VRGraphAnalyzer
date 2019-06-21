package com.petarzoric.vr_feedback.Model;

public class Location<X, Y> {
    public final X x;
    public final Y y;
    public Location(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }
}