package com.petarzoric.vr_feedback.Model;

import java.util.ArrayList;

public class GraphAnalysisResult {

    double averagePresence;
    double virtualPresenceShare;
    double realPresenceShare;

    double p1Share;
    double p2Share;
    double p3Share;

    double p1Gradient;
    double p2Gradient;
    double p3Gradient;

    double p1Presence;
    double p2Presence;
    double p3Presence;

    double p1Angle;
    double p2Angle;
    double p3Angle;

    int p1duration;
    int p2duration;
    int p3duration;

    double max_presence;
    int breaksInPresence;


    ArrayList<GraphPoint> maxPoints;
    ArrayList<GraphPoint> minPoints;
    ArrayList<GraphPhase> extremePhases;
    ArrayList<GraphPhase> constantPhases;
    ArrayList<RecoveryPhase> recoveryPhases;



    public GraphAnalysisResult() {
    }

    public double getAveragePresence() {
        return averagePresence;
    }

    public double getP1Presence() {
        return p1Presence;
    }

    public void setP1Presence(double p1Presence) {
        this.p1Presence = p1Presence;
    }

    public double getP2Presence() {
        return p2Presence;
    }

    public void setP2Presence(double p2Presence) {
        this.p2Presence = p2Presence;
    }

    public double getP3Presence() {
        return p3Presence;
    }

    public void setP3Presence(double p3Presence) {
        this.p3Presence = p3Presence;
    }

    public void setAveragePresence(double averagePresence) {
        this.averagePresence = averagePresence;
    }

    public double getVirtualPresenceShare() {
        return virtualPresenceShare;
    }

    public void setVirtualPresenceShare(double virtualPresenceShare) {
        this.virtualPresenceShare = virtualPresenceShare;
    }

    public double getRealPresenceShare() {
        return realPresenceShare;
    }

    public void setRealPresenceShare(double realPresenceShare) {
        this.realPresenceShare = realPresenceShare;
    }

    public double getP1Share() {
        return p1Share;
    }

    public void setP1Share(double p1Share) {
        this.p1Share = p1Share;
    }

    public double getP2Share() {
        return p2Share;
    }

    public void setP2Share(double p2Share) {
        this.p2Share = p2Share;
    }

    public double getP3Share() {
        return p3Share;
    }

    public void setP3Share(double p3Share) {
        this.p3Share = p3Share;
    }

    public double getP1Gradient() {
        return p1Gradient;
    }

    public void setP1Gradient(double p1Gradient) {
        this.p1Gradient = p1Gradient;
    }

    public double getP2Gradient() {
        return p2Gradient;
    }

    public void setP2Gradient(double p2Gradient) {
        this.p2Gradient = p2Gradient;
    }

    public double getP3Gradient() {
        return p3Gradient;
    }

    public void setP3Gradient(double p3Gradient) {
        this.p3Gradient = p3Gradient;
    }

    public double getP1Angle() {
        return p1Angle;
    }

    public void setP1Angle(double p1Angle) {
        this.p1Angle = p1Angle;
    }

    public double getP2Angle() {
        return p2Angle;
    }

    public void setP2Angle(double p2Angle) {
        this.p2Angle = p2Angle;
    }

    public double getP3Angle() {
        return p3Angle;
    }

    public void setP3Angle(double p3Angle) {
        this.p3Angle = p3Angle;
    }

    public int getP1duration() {
        return p1duration;
    }

    public void setP1duration(int p1duration) {
        this.p1duration = p1duration;
    }

    public int getP2duration() {
        return p2duration;
    }

    public void setP2duration(int p2duration) {
        this.p2duration = p2duration;
    }

    public int getP3duration() {
        return p3duration;
    }

    public void setP3duration(int p3duration) {
        this.p3duration = p3duration;
    }

    public double getMax_presence() {
        return max_presence;
    }

    public void setMax_presence(double max_presence) {
        this.max_presence = max_presence;
    }

    public int getBreaksInPresence() {
        return breaksInPresence;
    }

    public void setBreaksInPresence(int breaksInPresence) {
        this.breaksInPresence = breaksInPresence;
    }

    public ArrayList<GraphPoint> getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(ArrayList<GraphPoint> maxPoints) {
        this.maxPoints = maxPoints;
    }

    public ArrayList<GraphPoint> getMinPoints() {
        return minPoints;
    }

    public void setMinPoints(ArrayList<GraphPoint> minPoints) {
        this.minPoints = minPoints;
    }

    public ArrayList<GraphPhase> getExtremePhases() {
        return extremePhases;
    }

    public void setExtremePhases(ArrayList<GraphPhase> extremePhases) {
        this.extremePhases = extremePhases;
    }

    public ArrayList<GraphPhase> getConstantPhases() {
        return constantPhases;
    }

    public void setConstantPhases(ArrayList<GraphPhase> constantPhases) {
        this.constantPhases = constantPhases;
    }

    public ArrayList<RecoveryPhase> getRecoveryPhases() {
        return recoveryPhases;
    }

    public void setRecoveryPhases(ArrayList<RecoveryPhase> recoveryPhases) {
        this.recoveryPhases = recoveryPhases;
        this.breaksInPresence = recoveryPhases.size();
    }
}
