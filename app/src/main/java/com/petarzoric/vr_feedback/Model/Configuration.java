package com.petarzoric.vr_feedback.Model;

/**
 * Model class which represents the parameters for the quantification and analysis of the sketch.
 *
 */

public class Configuration {
    //general data for the study itself
    private long id;
    private long study_id;

    //determines how many points the quantification result will have
    private int pointsQuantity;

    private float extremePointsFilter;

    //extreme phases
    private float extremePhaseMinLength;
    private float extremePhaseDelta;
    private float extremePhaseGradientTolerance;

    //constant phases
    private float constantPhaseMinLength;
    private float constantPhaseDelta;
    private float constantPhaseGradientTolerance;
    private float constantPhaseDeviation;

    public Configuration(long id, long study_id, int pointsQuantity) {
        this.id = id;
        this.study_id = study_id;
        this.pointsQuantity = pointsQuantity;

    }

    public Configuration() {
    }

    public void configureExtremePhaseParams(float minLength, float delta, float tolerance){
        this.setExtremePhaseMinLength(minLength);
        this.setExtremePhaseDelta(delta);
        this.setExtremePhaseGradientTolerance(tolerance);
    }

    public void configureConstantPhaseParams(float minLength, float delta, float exTolerance, float deviation){
        this.setConstantPhaseMinLength(minLength);
        this.setConstantPhaseDelta(delta);
        this.setConstantPhaseGradientTolerance(exTolerance);
        this.setConstantPhaseDeviation(deviation);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStudy_id() {
        return study_id;
    }

    public void setStudy_id(long study_id) {
        this.study_id = study_id;
    }

    public int getPointsQuantity() {
        return pointsQuantity;
    }

    public void setPointsQuantity(int pointsQuantity) {
        this.pointsQuantity = pointsQuantity;
    }

    public float getExtremePhaseMinLength() {
        return extremePhaseMinLength;
    }

    public void setExtremePhaseMinLength(float extremePhaseMinLength) {
        this.extremePhaseMinLength = extremePhaseMinLength;
    }

    public float getExtremePhaseGradientTolerance() {
        return extremePhaseGradientTolerance;
    }

    public void setExtremePhaseGradientTolerance(float extremePhaseGradientTolerance) {
        this.extremePhaseGradientTolerance = extremePhaseGradientTolerance;
    }

    public float getConstantPhaseMinLength() {
        return constantPhaseMinLength;
    }

    public void setConstantPhaseMinLength(float constantPhaseMinLength) {
        this.constantPhaseMinLength = constantPhaseMinLength;
    }

    public float getExtremePointsFilter() {
        return extremePointsFilter;
    }

    public void setExtremePointsFilter(float extremePointsFilter) {
        this.extremePointsFilter = extremePointsFilter;
    }



    public float getExtremePhaseDelta() {
        return extremePhaseDelta;
    }

    public void setExtremePhaseDelta(float extremePhaseDelta) {
        this.extremePhaseDelta = extremePhaseDelta;
    }


    public float getConstantPhaseDelta() {
        return constantPhaseDelta;
    }

    public void setConstantPhaseDelta(float constantPhaseDelta) {
        this.constantPhaseDelta = constantPhaseDelta;
    }

    public float getConstantPhaseGradientTolerance() {
        return constantPhaseGradientTolerance;
    }

    public void setConstantPhaseGradientTolerance(float constantPhaseGradientTolerance) {
        this.constantPhaseGradientTolerance = constantPhaseGradientTolerance;
    }

    public float getConstantPhaseDeviation() {
        return constantPhaseDeviation;
    }

    public void setConstantPhaseDeviation(float constantPhaseDeviation) {
        this.constantPhaseDeviation = constantPhaseDeviation;
    }
}
