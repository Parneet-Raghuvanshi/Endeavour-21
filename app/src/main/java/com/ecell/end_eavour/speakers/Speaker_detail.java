package com.ecell.end_eavour.speakers;

public class Speaker_detail {

    public String name;
    public String desc;
    public String id;
    public String imguri;
    public String t;
    public String d;

    public Speaker_detail() {
    }

    public Speaker_detail(String name, String desc, String id, String imguri, String t, String d) {
        this.name = name;
        this.desc = desc;
        this.id = id;
        this.imguri = imguri;
        this.t = t;
        this.d = d;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImguri() {
        return imguri;
    }

    public void setImguri(String imguri) {
        this.imguri = imguri;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }
}
