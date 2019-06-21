package com.petarzoric.vr_feedback.Model;

public class Participant {

    private long id;
    private long study_id;
    private String pseudonym;

    public long getStudy_id() {

        return study_id;
    }

    public void setStudy_id(long study_id) {
        this.study_id = study_id;
    }

    public Participant(String pseudonym) {
        this.pseudonym = pseudonym;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public Participant(long id, String pseudonym) {

        this.id = id;
        this.pseudonym = pseudonym;
    }

    public Participant() {

    }
}
