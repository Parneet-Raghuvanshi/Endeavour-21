package com.ecell.end_eavour.sponsors;

import java.util.List;

public class SponsorsMain_Model {

    String name;
    List<Sponsors_Model> sponsorsModels;

    public SponsorsMain_Model() {
    }

    public SponsorsMain_Model(String name, List<Sponsors_Model> sponsorsModels) {
        this.name = name;
        this.sponsorsModels = sponsorsModels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Sponsors_Model> getSponsorsModels() {
        return sponsorsModels;
    }

    public void setSponsorsModels(List<Sponsors_Model> sponsorsModels) {
        this.sponsorsModels = sponsorsModels;
    }
}
