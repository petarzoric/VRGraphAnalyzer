package com.petarzoric.vr_feedback.Model;

public class RecoveryPhase {

    GraphPoint diveIntoReal;
    GraphPoint diveIntoVirtual;

    int startIndex;
    int endIndex;

    int startTime;
    int endTime;
    int recoveryTime;

    double minPresence;
    double recoveryShare;


    public RecoveryPhase(GraphPoint diveIntoReal, GraphPoint diveIntoVirtual) {
        this.diveIntoReal = diveIntoReal;
        this.diveIntoVirtual = diveIntoVirtual;
        this.startIndex = diveIntoReal.getIndex();
        this.endIndex = diveIntoVirtual.getIndex();
    }

    public GraphPoint getDiveIntoReal() {
        return diveIntoReal;
    }

    public void setDiveIntoReal(GraphPoint diveIntoReal) {
        this.diveIntoReal = diveIntoReal;
    }

    public GraphPoint getDiveIntoVirtual() {
        return diveIntoVirtual;
    }

    public void setDiveIntoVirtual(GraphPoint diveIntoVirtual) {
        this.diveIntoVirtual = diveIntoVirtual;
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

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getRecoveryTime() {
        return recoveryTime;
    }

    public void setRecoveryTime(int recoveryTime) {
        this.recoveryTime = recoveryTime;
    }

    public double getMinPresence() {
        return minPresence;
    }

    public void setMinPresence(double minPresence) {
        this.minPresence = minPresence;
    }

    public double getRecoveryShare() {
        return recoveryShare;
    }

    public void setRecoveryShare(double recoveryShare) {
        this.recoveryShare = recoveryShare;
    }
}
