package com.ecell.end_eavour.team;

public class Team_Model {

    String name;
    String desig;
    String domain;
    String imguri;
    String googleProfile;
    String linkedinProfile;
    String facebookProfile;

    public Team_Model() {
    }

    public Team_Model(String name, String desig, String domain, String imguri, String googleProfile, String linkedinProfile, String facebookProfile) {
        this.name = name;
        this.desig = desig;
        this.domain = domain;
        this.imguri = imguri;
        this.googleProfile = googleProfile;
        this.linkedinProfile = linkedinProfile;
        this.facebookProfile = facebookProfile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesig() {
        return desig;
    }

    public void setDesig(String desig) {
        this.desig = desig;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getImguri() {
        return imguri;
    }

    public void setImguri(String imguri) {
        this.imguri = imguri;
    }

    public String getGoogleProfile() {
        return googleProfile;
    }

    public void setGoogleProfile(String googleProfile) {
        this.googleProfile = googleProfile;
    }

    public String getLinkedinProfile() {
        return linkedinProfile;
    }

    public void setLinkedinProfile(String linkedinProfile) {
        this.linkedinProfile = linkedinProfile;
    }

    public String getFacebookProfile() {
        return facebookProfile;
    }

    public void setFacebookProfile(String facebookProfile) {
        this.facebookProfile = facebookProfile;
    }
}
