package com.example.doc_truyen.model;

public class RatingTruyen {
    String id;
    String nametruye,iduser;
    float ratingcount;

    public RatingTruyen() {
    }

    public RatingTruyen(String id, String nametruye, String iduser, float ratingcount) {
        this.id = id;
        this.nametruye = nametruye;
        this.iduser = iduser;
        this.ratingcount = ratingcount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNametruye() {
        return nametruye;
    }

    public void setNametruye(String nametruye) {
        this.nametruye = nametruye;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public float getRatingcount() {
        return ratingcount;
    }

    public void setRatingcount(float ratingcount) {
        this.ratingcount = ratingcount;
    }
}
