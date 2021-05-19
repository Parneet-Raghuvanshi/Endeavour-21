package com.ecell.end_eavour.sponsors;

public class Sponsors_Model {

    String sponsorName;
    String sponsorCategory;
    String imageSponsor;
    String sponsorLink;

    public Sponsors_Model() {
    }

    public Sponsors_Model(String sponsorName, String sponsorCategory, String imageSponsor, String sponsorLink) {
        this.sponsorName = sponsorName;
        this.sponsorCategory = sponsorCategory;
        this.imageSponsor = imageSponsor;
        this.sponsorLink = sponsorLink;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getSponsorCategory() {
        return sponsorCategory;
    }

    public void setSponsorCategory(String sponsorCategory) {
        this.sponsorCategory = sponsorCategory;
    }

    public String getImageSponsor() {
        return imageSponsor;
    }

    public void setImageSponsor(String imageSponsor) {
        this.imageSponsor = imageSponsor;
    }

    public String getSponsorLink() {
        return sponsorLink;
    }

    public void setSponsorLink(String sponsorLink) {
        this.sponsorLink = sponsorLink;
    }
}
