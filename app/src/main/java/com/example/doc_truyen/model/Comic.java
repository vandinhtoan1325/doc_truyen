package com.example.doc_truyen.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Comic implements Serializable {
    int id;
    String mota;
    String tacgia;
    String tentruyen;
    String theloai;
    String thumb;
    String trangtruyen;
    int vote;
    int view;

    public Comic() {}

    public Comic(int id, String mota, String tacgia, String tentruyen, String theloai, String thumb, String trangtruyen, int vote, int view) {
        this.id = id;
        this.mota = mota;
        this.tacgia = tacgia;
        this.tentruyen = tentruyen;
        this.theloai = theloai;
        this.thumb = thumb;
        this.trangtruyen = trangtruyen;
        this.vote = vote;
        this.view = view;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getTacgia() {
        return tacgia;
    }

    public void setTacgia(String tacgia) {
        this.tacgia = tacgia;
    }

    public String getTentruyen() {
        return tentruyen;
    }

    public void setTentruyen(String tentruyen) {
        this.tentruyen = tentruyen;
    }

    public String getTheloai() {
        return theloai;
    }

    public void setTheloai(String theloai) {
        this.theloai = theloai;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTrangtruyen() {
        return trangtruyen;
    }

    public void setTrangtruyen(String trangtruyen) {
        this.trangtruyen = trangtruyen;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "Comic{" +
                "id=" + id +
                ", mota='" + mota + '\'' +
                ", tacgia='" + tacgia + '\'' +
                ", tentruyen='" + tentruyen + '\'' +
                ", theloai='" + theloai + '\'' +
                ", thumb='" + thumb + '\'' +
                ", trangtruyen='" + trangtruyen + '\'' +
                ", vote=" + vote +
                ", view=" + view +
                '}';
    }
}
