package com.ecell.end_eavour.schedule;

public class Schedule_CheckDate {

    public String dayOneDate;
    public String dayTwoDate;

    public Schedule_CheckDate() {
    }

    public Schedule_CheckDate(String dayOneDate, String dayTwoDate) {
        this.dayOneDate = dayOneDate;
        this.dayTwoDate = dayTwoDate;
    }

    public String getDayOneDate() {
        return dayOneDate;
    }

    public void setDayOneDate(String dayOneDate) {
        this.dayOneDate = dayOneDate;
    }

    public String getDayTwoDate() {
        return dayTwoDate;
    }

    public void setDayTwoDate(String dayTwoDate) {
        this.dayTwoDate = dayTwoDate;
    }
}
