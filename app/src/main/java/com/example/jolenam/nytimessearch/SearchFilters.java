package com.example.jolenam.nytimessearch;

import java.io.Serializable;

/**
 * Created by gbelton on 6/23/16.
 */
public class SearchFilters implements Serializable {

    String sort;
    boolean chkArts;
    boolean chkFashion;
    String spinnerMonth;
    String spinnerYear;

    public SearchFilters(String sort, boolean chkArts, boolean chkFashion, boolean chkSports, String spinnerDay, String spinnerMonth, String spinnerYear) {
        this.sort = sort;
        this.chkArts = chkArts;
        this.chkFashion = chkFashion;
        this.chkSports = chkSports;
        this.spinnerDay = spinnerDay;
        this.spinnerMonth = spinnerMonth;
        this.spinnerYear = spinnerYear;
    }

    boolean chkSports;
    String spinnerDay;


    public String getSpinnerDay() {
        return spinnerDay;
    }

    public String getSpinnerMonth() {
        return spinnerMonth;
    }

    public String getSpinnerYear() {
        return spinnerYear;
    }

    public boolean getChkArts() {
        return chkArts;
    }

    public boolean getChkFashion() {
        return chkFashion;
    }

    public boolean getChkSports() {
        return chkSports;
    }

    public String getSort() {
        return sort;
    }


}
