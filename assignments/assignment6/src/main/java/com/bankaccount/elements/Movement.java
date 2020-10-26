package com.bankaccount.elements;

import java.time.LocalDate;

public class Movement {
    private LocalDate date;
    private Purpose purpose;

    public Movement() {}

    public Movement(LocalDate date, Purpose purpose) {
        this.date = date;
        this.purpose = purpose;
    }

    public String getDate() {
        return this.date.toString();
    }

    public Purpose getPurpose() {
        return this.purpose;
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }


    @Override
    public String toString() {
        return "Movement{" +
                "date=" + date +
                ", purpose=" + purpose +
                '}';
    }
}
