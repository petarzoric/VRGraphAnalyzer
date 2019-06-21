package com.petarzoric.vr_feedback.Model;

import java.util.ArrayList;

public class GraphPhase {
    private ArrayList<GraphPoint> points;
    private float averageGradient;
    private float gradientStartToEnd;
    private float length;
    private double angle;
    private String type;
    private int startIndex;
    private int endIndex;
    private String comment = "";

    public GraphPhase(ArrayList<GraphPoint> points, int type) {
        this.points = points;

        if(type==0){
            this.type="ConstantPhase";
            for(GraphPoint p: points){
                p.setPartOfConstantPhase(true);
            }
        } else {
            this.type="ExtremePhase";
            for(GraphPoint p: points){
                p.setPartOfExtremePhase(true);
            }
        }
        Float sum = 0f;
        for(GraphPoint f: points){
            sum = sum + Math.abs(f.getFirstDerivate());
            f.setHp(false);
            f.setTp(false);
            f.setExtremePoint(false);
        }
        float avg = sum / points.size();
        this.startIndex = points.get(0).getIndex();
        this.endIndex = points.get(points.size()-1).getIndex();
        this.averageGradient=avg;
        float yDelta = points.get(points.size()-1).getY() - points.get(0).getY();
        float xDelta = points.get(points.size()-1).getX() - points.get(0).getX();
        this.gradientStartToEnd = yDelta / xDelta;
        double gradientStartEnd = (double) gradientStartToEnd;
        this.angle =  Math.toDegrees(Math.atan(Math.sin(gradientStartEnd)));

    }

    public ArrayList<GraphPoint> getPoints() {
        return points;

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        for(GraphPoint p: this.points){
            p.setComment(comment);
        }
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public void setPoints(ArrayList<GraphPoint> points) {
        this.points = points;
    }

    public float getAverageGradient() {
        return averageGradient;
    }

    public void setAverageGradient(float averageGradient) {
        this.averageGradient = averageGradient;
    }

    public float getGradientStartToEnd() {
        return gradientStartToEnd;
    }

    public void setGradientStartToEnd(float gradientStartToEnd) {
        this.gradientStartToEnd = gradientStartToEnd;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
