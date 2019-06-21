package com.petarzoric.vr_feedback.Model;

/**
 * Represents a point on a graph.
 * A set of graphpoints is the result of quantification ({@see PaintView.quantifyGraph()}).
 * Holds coordinates, derivates and flags(e.g. isExtremePoint)
 */

public class GraphPoint {
    float x;
    float y;
    float screenX;
    float screenY;
    float firstDerivate;
    float secondDerivate;
    float thirdDerivate;
    GraphPoint prev = null;
    GraphPoint next = null;
    boolean isExtremePoint = false;
    boolean isTurningPoint = false;
    boolean drawn = false;
    boolean isPartOfRecoveryPhase = false;
    boolean hp = false;
    boolean tp = false;
    boolean isPartOfConstantPhase = false;
    boolean isPartOfExtremePhase = false;
    int index;
    int phase =-1;
    String comment = "";

    public boolean isPartOfExtremePhase() {
        return isPartOfExtremePhase;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public GraphPoint() {


    }

    public boolean isDrawn() {
        return drawn;
    }

    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPartOfExtremePhase(boolean partOfExtremePhase) {
        this.isPartOfExtremePhase = partOfExtremePhase;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public GraphPoint(float x, float y) {
        this.screenX = x;


        this.screenY = y;
    }

    public GraphPoint(float x, float y, int index) {
        this.screenX = x;

        this.screenY = y;

        this.index = index;
    }

    public boolean isPartOfConstantPhase() {
        return isPartOfConstantPhase;
    }

    public void setPartOfConstantPhase(boolean partOfConstantPhase) {
        this.isPartOfConstantPhase = partOfConstantPhase;
    }

    public boolean isHp() {
        return hp;
    }

    public void setHp(boolean hp) {
        this.hp = hp;
        this.setExtreme();
    }

    public boolean isTp() {
        return tp;
    }

    public void setTp(boolean tp) {
        this.tp = tp;
        this.setExtreme();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getScreenX() {
        return screenX;
    }

    public float getScreenY() {
        return screenY;
    }


    public float getFirstDerivate() {
        return firstDerivate;
    }

    public void setFirstDerivate(float firstDerivate) {
        this.firstDerivate = firstDerivate;
    }

    public float getSecondDerivate() {
        return secondDerivate;
    }

    public void setSecondDerivate(float secondDerivate) {
        this.secondDerivate = secondDerivate;
    }

    public float getThirdDerivate() {
        return thirdDerivate;
    }

    public void setThirdDerivate(float thirdDerivate) {
        this.thirdDerivate = thirdDerivate;
    }

    public GraphPoint getPrev() {
        return prev;
    }

    public void setPrev(GraphPoint prev) {
        this.prev = prev;
    }

    public GraphPoint getNext() {
        return next;
    }

    public void setNext(GraphPoint next) {
        this.next = next;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setScreenX(float screenX) {
        this.screenX = screenX;
    }

    public void setScreenY(float screenY) {
        this.screenY = screenY;
    }

    public boolean isExtremePoint() {
        return isExtremePoint;
    }

    public boolean isTurningPoint() {
        return isTurningPoint;
    }

    public void setTurningPoint(boolean turningPoint) {
        isTurningPoint = turningPoint;
    }

    public void setExtremePoint(boolean extremePoint) {

        isExtremePoint = extremePoint;
    }

    public boolean hasNext(){
        if(this.getNext() == null){
            return false;
        } else {
            return true;
        }

    }

    public void setExtreme(){
        this.isExtremePoint = true;
    }

    public boolean isPartOfRecoveryPhase() {
        return isPartOfRecoveryPhase;
    }

    public void setPartOfRecoveryPhase(boolean partOfRecoveryPhase) {
        isPartOfRecoveryPhase = partOfRecoveryPhase;
    }

    public void setWende(){
        this.isTurningPoint = true;
    }

    public boolean hasPrev(){
        if(this.getPrev() == null){
            return false;
        } else {
            return true;
        }
    }
}